package com.github.dougmab.ygibringer.server.service;

import com.github.dougmab.ygibringer.app.model.Account;
import com.github.dougmab.ygibringer.app.model.Status;
import com.github.dougmab.ygibringer.app.service.ConfigurationService;
import com.github.dougmab.ygibringer.server.exception.EndOfListException;
import com.github.dougmab.ygibringer.server.model.Configuration;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class AccountManagerService implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final List<Account> allAccounts = new ArrayList<>();

    private final Queue<Account> pendingAccounts = new LinkedList<>();
    private final Queue<Account> concludedAccounts = new LinkedList<>();
    private final Map<String, Account> managedAccounts = new HashMap<>();

    private final Configuration config;

    private static AccountManagerService INSTANCE;

    public static AccountManagerService getInstance() {
        if (AccountManagerService.INSTANCE == null) {
            AccountManagerService loadedManager = ConfigurationService.getManagerState();

            if (loadedManager != null) {
                loadedManager.allAccounts.forEach(Account::syncStatus);
            }

            if (loadedManager == null) loadedManager = new AccountManagerService();

            AccountManagerService.INSTANCE = loadedManager;
        }
        return AccountManagerService.INSTANCE;
    }

    public static void resetManager() {
        AccountManagerService.INSTANCE = null;
        ConfigurationService.removeManagerState();
        System.gc();
    }

    private AccountManagerService() {
        config = ConfigurationService.getConfig();
        getAccounts();
    }

    private void getAccounts() {
        StringBuilder accountsRaw = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(config.inputFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                accountsRaw.append(line);
                accountsRaw.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        };

        Pattern pattern = Pattern.compile(config.regexStr, Pattern.DOTALL);
        Matcher matcher = pattern.matcher(accountsRaw);

        while(matcher.find()) {
            Account account = new Account(matcher.group(1), matcher.group(2));
            System.out.println(account);
            account.syncStatus();
            allAccounts.add(account);
            pendingAccounts.add(account);
        }
    }

    public Account getNextAccount(String token) {
        // Associates account to client
        Account account = pendingAccounts.poll();

        if (account == null) throw new EndOfListException("Account list has reached it's end");

        account.setStatus(Status.managing());
        managedAccounts.put(token, account);
        return account;
    }

    public Account updateAccount(String token, int statusIndex) throws IOException {
        // Gets account and disassociates client
        Account account = managedAccounts.remove(token);
        account.setStatus(ConfigurationService.getConfig().customStatus.get(statusIndex));

        // Add account to concluded list
        concludedAccounts.add(account);

        return account;
    }

    public static Iterator<Account> getIterator() {
        return AccountManagerService.getInstance().allAccounts.iterator();
    }

}
