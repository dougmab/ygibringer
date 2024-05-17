package com.github.dougmab.ygibringer.server.controller;

import com.github.dougmab.ygibringer.app.model.Account;
import com.github.dougmab.ygibringer.app.model.Status;
import com.github.dougmab.ygibringer.app.model.StatusType;
import com.github.dougmab.ygibringer.server.model.AccountData;
import com.github.dougmab.ygibringer.server.model.MessageData;
import com.github.dougmab.ygibringer.server.model.SuccessResponse;
import com.github.dougmab.ygibringer.server.service.AccountManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/account")
public class AccountController {

    AccountManagerService manager;

    @Autowired
    public AccountController(AccountManagerService manager) {
        this.manager = AccountManagerService.getInstance();
    }

    @GetMapping("/next")
    public ResponseEntity<SuccessResponse<AccountData>> nextAccount() {
        String id = UUID.randomUUID().toString();
        Account account = manager.getNextAccount(id);

        return ResponseEntity.ok(
                new SuccessResponse<>(new AccountData(id, account.getLogin(), account.getPassword()))
        );
    }

    @PutMapping("/update")
    public ResponseEntity<SuccessResponse<MessageData>> updateAccount(@RequestParam("token") String token, @RequestParam("status") int status) throws IOException {

        manager.updateAccount(token, status);

        return ResponseEntity.ok(new SuccessResponse<>(
                new MessageData("Account status updated")
        ));
    }

    @PutMapping("/suspended")
    public ResponseEntity<SuccessResponse<MessageData>> blockAccount(@RequestParam("token") String token, @RequestParam("message") String message) {

        manager.updateAccountWithCustomizedValue(token, new Status("Suspenso", message, StatusType.ERROR));

        return ResponseEntity.ok(new SuccessResponse<>(
                new MessageData("Account status updated with block message")
        ));
    }
}
