package com.github.dougmab.ygibringer.server.controller;

import com.github.dougmab.ygibringer.app.model.Account;
import com.github.dougmab.ygibringer.server.model.AccountData;
import com.github.dougmab.ygibringer.server.model.MessageData;
import com.github.dougmab.ygibringer.server.model.SuccessResponse;
import com.github.dougmab.ygibringer.server.service.AccountManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.UUID;

@RestController("/account")
public class AccountController {

    @Autowired
    AccountManagerService manager;

    @GetMapping("/next")
    public ResponseEntity<SuccessResponse<AccountData>> nextAccount() {
        String id = UUID.randomUUID().toString();
        Account account = manager.getNextAccount(id);

        return ResponseEntity.ok(
                new SuccessResponse<>(new AccountData(id, account.getLogin(), account.getPassword()))
        );
    }

    @PutMapping("/update")
    public ResponseEntity<SuccessResponse<MessageData>> updateAccount(@RequestParam("token") String token, @RequestParam(name = "status") int status) throws IOException {

        manager.updateAccount(token, status);

        return ResponseEntity.ok(new SuccessResponse<>(
                new MessageData("Account status updated")
        ));
    }
}
