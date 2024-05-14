package com.github.dougmab.ygibringer.app.service;

import com.github.dougmab.ygibringer.app.model.Status;
import com.github.dougmab.ygibringer.server.model.AppProperties;
import com.github.dougmab.ygibringer.server.model.Configuration;
import com.github.dougmab.ygibringer.server.service.AccountManagerService;

import java.io.*;
import java.util.List;

public class ConfigurationService {
    private static AppProperties properties = new AppProperties();

    public static void save() {
        // Save properties into file
        try (ObjectOutputStream outSt = new ObjectOutputStream(new FileOutputStream("./properties.conf"))) {
            outSt.writeObject(properties);
            System.out.println("Properties saved");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean load() {
        File configFile = new File("./properties.conf");

        if (!configFile.exists()) return false;

        System.out.println("Properties file found.");

        try (ObjectInputStream inSt = new ObjectInputStream(new FileInputStream(configFile))) {
            System.out.println("Reading properties...");
            properties = (AppProperties) inSt.readObject();
            if (properties != null) return true;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            System.out.println("Properties file outdated");
            configFile.delete();
            return false;
        }

        System.out.println("Something went wrong");
        return false;
    }

    public static Configuration getConfig() {
        return properties.getConfigs();
    }

    public static AccountManagerService getManagerState() {
        return properties.getAccountManagerState();
    }

    public static void updateManagerState() {
        properties.setAccountManagerState(AccountManagerService.getInstance());
        save();
    }

    public static void removeManagerState() {
        properties.setAccountManagerState(null);
    }

    public static void setConfig(File input, String output, String error, String regex, List<Status> statuses) {
        properties.setConfigs(new Configuration(input, output, error, regex, statuses));
        save();
    }
}
