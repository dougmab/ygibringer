package com.github.dougmab.ygibringer.server.model;

import com.github.dougmab.ygibringer.server.service.AccountManagerService;

import java.io.Serial;
import java.io.Serializable;

public class AppProperties implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Configuration configs;
    private AccountManagerService accountManagerState;

    public Configuration getConfigs() {
        return configs;
    }

    public void setConfigs(Configuration configs) {
        this.configs = configs;
    }

    public AccountManagerService getAccountManagerState() {
        return accountManagerState;
    }

    public void setAccountManagerState(AccountManagerService accountManagerState) {
        this.accountManagerState = accountManagerState;
    }
}
