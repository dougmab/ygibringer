package com.github.dougmab.ygibringer.server.service;

import com.github.dougmab.ygibringer.app.model.Account;
import com.github.dougmab.ygibringer.app.service.ConfigurationService;
import com.github.dougmab.ygibringer.server.model.ConfigurationProperties;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class AccountManagerService {
    private static final Queue<Account> accounts = new LinkedList<>();
    private final Map<String, Account> managedAccounts = new HashMap<>();

    private int accountCount;
    private int errorAccountCount;

    private final ConfigurationProperties config;


    public AccountManagerService() {
        config = ConfigurationService.get();
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
            AccountManagerService.accounts.add(account);
        }
    }

    public Account getNextAccount(String clientId) {
        // Associates account to client
        Account account = AccountManagerService.accounts.remove();
        account.setStatus("CONFIGURANDO");
        managedAccounts.put(clientId, account);
        return account;
    }

    public Account finishAccount(String clientId, String status, boolean isCorrect) throws IOException {
        // Gets account and disassociates client
        Account finishedAccount = managedAccounts.remove(clientId);
        finishedAccount.setStatus(status);

        // Increasing Functions
        Function<Boolean, Integer> increaseAccountCount = (Boolean increase) -> {
            if (increase) return ++accountCount;
            return accountCount;
        };

        Function<Boolean, Integer> increaseErrorAccountCount = (Boolean increase) -> {
            if (increase) return ++errorAccountCount;
            return errorAccountCount;
        };

        // Writes new Account to file
        if (isCorrect) {
            Files.writeString(updateFileName(config.outputFileName, increaseAccountCount).toPath(), finishedAccount.toString(), StandardOpenOption.APPEND, StandardOpenOption.CREATE);
            return finishedAccount;
        }

        Files.writeString(updateFileName(config.errorFileName, increaseErrorAccountCount).toPath(), finishedAccount.toString(), StandardOpenOption.APPEND, StandardOpenOption.CREATE);
        return finishedAccount;
    }

    private File updateFileName(String fileName, Function<Boolean, Integer> count) {
        File outputFile = new File(fileName.replace("{count}", Integer.toString(count.apply(false))));
        File newFile = new File(fileName.replace("{count}", Integer.toString(count.apply(true))));
        outputFile.renameTo(newFile);
        return newFile;
    }

    public static Iterator<Account> getIterator() {
        return AccountManagerService.accounts.iterator();
    }

}
