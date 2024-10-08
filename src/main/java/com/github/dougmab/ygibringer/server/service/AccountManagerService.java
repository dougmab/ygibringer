package com.github.dougmab.ygibringer.server.service;

import com.github.dougmab.ygibringer.app.model.Account;
import com.github.dougmab.ygibringer.app.model.Status;
import com.github.dougmab.ygibringer.app.model.StatusType;
import com.github.dougmab.ygibringer.app.service.ConfigurationService;
import com.github.dougmab.ygibringer.server.exception.EndOfListException;
import com.github.dougmab.ygibringer.server.exception.NoUserAssociatedException;
import com.github.dougmab.ygibringer.server.exception.UserOccupiedException;
import com.github.dougmab.ygibringer.server.model.Configuration;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
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
                loadedManager.allAccounts.forEach(Account::syncProperties);
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

        Pattern pattern = Pattern.compile(config.regexStr, Pattern.DOTALL | Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(accountsRaw);

        Status repeatedStatus = ConfigurationService.getConfig().repeatedStatus;
//        System.out.println(repeatedStatus);
        while(matcher.find()) {
            Account account = new Account(matcher.group(1), matcher.group(2));
            System.out.println(account);
            account.syncProperties();

            if (allAccounts.contains(account) && repeatedStatus != null) {
                account.setStatus(repeatedStatus);
                account.setManager("YgiBringer");
                concludedAccounts.add(account);
            } else {
                pendingAccounts.add(account);
            }

            allAccounts.add(account);
        }
    }

    public Account getManagedAccount(String user) {
        Account account = managedAccounts.get(user);
        if (account == null) throw new NoUserAssociatedException("There is no account associated to this user");

        return account;
    }

    public Account getNextAccount(String user) {
        if (managedAccounts.containsKey(user)) throw new UserOccupiedException("User already handling an account");

        // Associates account to client
        Account account = pendingAccounts.poll();

        if (account == null) throw new EndOfListException("Account list has reached it's end");


        account.setStatus(Status.managing());
        managedAccounts.put(user, account);
        account.setManager(user);
        System.out.println(account.getManager());
        return account;
    }

    public Account updateAccount(String user, int statusIndex) throws IOException {
        // Gets account and disassociates client
        Account account = managedAccounts.remove(user);

        if (account == null) throw new NoUserAssociatedException("There is no account associated to this user");

        account.setStatus(ConfigurationService.getConfig().customStatus.get(statusIndex));

        // Add account to concluded list
        concludedAccounts.add(account);

        return account;
    }

    public Account updateAccountWithCustomizedValue(String token, Status status) {
        // Gets account and disassociates client
        Account account = managedAccounts.remove(token);
        account.setStatus(status);

        // Add account to concluded list
        concludedAccounts.add(account);

        return account;
    }

    public void returnAccountToPendingQueue(Account account) {
        concludedAccounts.remove(account);
        account.setStatus(Status.pending());
        account.setManager("");
        pendingAccounts.add(account);
    }

    public void skipAccount(Account account) {
        pendingAccounts.remove(account);
        account.setStatus(Status.skipped());
        account.setManager("YgiBringer");
        concludedAccounts.add(account);
    }

    public boolean saveAccountsReport() {
        StringBuilder successAccounts = new StringBuilder();
        StringBuilder errorAccounts = new StringBuilder();
        int successCount = 0;
        int errorCount = 0;

        Configuration config = ConfigurationService.getConfig();
        for (Account account : concludedAccounts) {
            if (account.getStatus().getType().equals(StatusType.SUCCESS)) {
                successAccounts.append(account);
                successCount++;
            }

            if (account.getStatus().getType().equals(StatusType.ERROR)) {
                errorAccounts.append(account);
                errorCount++;
            }
        }

        Path successPath = config.inputFile.toPath().getParent().resolve(config.outputFileName.replace("{count}", Integer.toString(successCount)));
        Path errorPath = config.inputFile.toPath().getParent().resolve(config.errorFileName.replace("{count}", Integer.toString(errorCount)));

        try {
            Files.writeString(successPath, successAccounts.toString(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            Files.writeString(errorPath, errorAccounts.toString(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static Iterator<Account> getIterator() {
        return AccountManagerService.getInstance().allAccounts.iterator();
    }

}
