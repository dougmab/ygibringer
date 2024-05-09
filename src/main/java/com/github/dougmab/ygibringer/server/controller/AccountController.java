package com.github.dougmab.ygibringer.server.controller;

import com.github.dougmab.ygibringer.app.model.Account;
import com.github.dougmab.ygibringer.server.service.AccountManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.UUID;

@RestController
public class AccountController {

    public record AccountResponse(String id, String login, String password ) {}
    public record DoneResponse(String status, String message) {}
    public record HealthResponse(String message) {}

    @Autowired
    AccountManagerService manager;

    @GetMapping("/next")
    public ResponseEntity<AccountResponse> nextAccount() {
        String id = UUID.randomUUID().toString();
        Account account = manager.getNextAccount(id);
        return ResponseEntity.ok(new AccountResponse(id, account.getLogin(), account.getPassword()));
    }

    @GetMapping("/done")
    public ResponseEntity<DoneResponse> accountDone(@RequestParam("id") String clientId, @RequestParam(name = "status", required = false) String status) {
        if (status == null) status = "OK";

        try {
            manager.finishAccount(clientId, status, status.equals("OK"));
        } catch (IOException e) {
            return ResponseEntity.ok(new DoneResponse("Error", e.getLocalizedMessage()));

        }
        return ResponseEntity.ok(new DoneResponse("Ok", "Account updated successfully"));
    }

    @GetMapping("/")
    public ResponseEntity<HealthResponse> checkHealth() {
        return ResponseEntity.ok(new HealthResponse("Server is up!"));
    }
}

