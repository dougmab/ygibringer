package com.github.dougmab.ygibringer.server.service;

import com.github.dougmab.ygibringer.app.model.Account;
import com.github.dougmab.ygibringer.app.model.Status;
import com.github.dougmab.ygibringer.app.service.ConfigurationService;
import com.github.dougmab.ygibringer.server.exception.EndOfListException;
import com.github.dougmab.ygibringer.server.model.ConfigurationProperties;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class AccountManagerService {
    private static final List<Account> allAccounts = new ArrayList<>();

    private static final Queue<Account> pendingAccounts = new LinkedList<>();
    private static final Queue<Account> concludedAccounts = new LinkedList<>();
    private final Map<String, Account> managedAccounts = new HashMap<>();

    private final ConfigurationProperties config;


    public AccountManagerService() {
        config = ConfigurationService.get();
        getAccounts();
    }

    private void getAccounts() {
        allAccounts.clear();
        pendingAccounts.clear();
        concludedAccounts.clear();
        managedAccounts.clear();

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
            AccountManagerService.allAccounts.add(account);
            AccountManagerService.pendingAccounts.add(account);
        }
    }

    public Account getNextAccount(String token) {
        // Associates account to client
        Account account = AccountManagerService.pendingAccounts.poll();

        if (account == null) throw new EndOfListException("Account list has reached it's end");

        account.setStatus(Status.managing());
        managedAccounts.put(token, account);
        return account;
    }

    public Account updateAccount(String token, int statusIndex) throws IOException {
        // Gets account and disassociates client
        Account account = managedAccounts.remove(token);
        account.setStatus(Status.getStatusByIndex(statusIndex));

        // Add account to concluded list
        concludedAccounts.add(account);

        return account;
    }

    public static Iterator<Account> getIterator() {
        return AccountManagerService.allAccounts.iterator();
    }

}
