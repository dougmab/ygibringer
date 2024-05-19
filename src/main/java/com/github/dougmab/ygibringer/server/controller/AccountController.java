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

@RestController
@RequestMapping("/account")
public class AccountController {

    AccountManagerService manager;

    @Autowired
    public AccountController(AccountManagerService manager) {
        this.manager = AccountManagerService.getInstance();
    }

    @GetMapping("/get")
    public ResponseEntity<SuccessResponse<AccountData>> getAccount(@RequestParam("user") String user) {
        Account account = manager.getManagedAccount(user);

        return ResponseEntity.ok(
                new SuccessResponse<>(new AccountData(user, account.getLogin(), account.getPassword()))
        );
    }

    @GetMapping("/next")
    public ResponseEntity<SuccessResponse<AccountData>> nextAccount(@RequestParam("user") String user) {
        Account account = manager.getNextAccount(user);

        return ResponseEntity.ok(
                new SuccessResponse<>(new AccountData(user, account.getLogin(), account.getPassword()))
        );
    }

    @PutMapping("/update")
    public ResponseEntity<SuccessResponse<MessageData>> updateAccount(@RequestParam("user") String user, @RequestParam("status") int status) throws IOException {

        manager.updateAccount(user, status);

        return ResponseEntity.ok(new SuccessResponse<>(
                new MessageData("Account status updated")
        ));
    }

    @PutMapping("/suspended")
    public ResponseEntity<SuccessResponse<MessageData>> blockAccount(@RequestParam("user") String user, @RequestParam("message") String message) {

        manager.updateAccountWithCustomizedValue(user, new Status("Suspenso", message, StatusType.ERROR));

        return ResponseEntity.ok(new SuccessResponse<>(
                new MessageData("Account status updated with block message")
        ));
    }
}
