package com.github.dougmab.ygibringer.app.service;

import com.github.dougmab.ygibringer.server.model.ConfigurationProperties;

import java.io.*;

public class ConfigurationService {
    private static ConfigurationProperties config;

    public static void setConfig(File input, String output, String error, String regex) {
        config = new ConfigurationProperties(input, output, error, regex);
        save();
    }

    public static void save() {
        try (ObjectOutputStream outSt = new ObjectOutputStream(new FileOutputStream("./properties.conf"))) {
            outSt.writeObject(config);
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
            config = (ConfigurationProperties) inSt.readObject();
            if (config != null) return true;
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

    public static ConfigurationProperties get() {
        return config;
    }
}
