package com.github.dougmab.ygibringer.server.controller;

import com.github.dougmab.ygibringer.server.model.MessageData;
import com.github.dougmab.ygibringer.server.model.SuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/info")
public class ServerInfoController {

    @GetMapping("/health")
    public ResponseEntity<SuccessResponse<MessageData>> checkHealth() {
        return ResponseEntity.ok(new SuccessResponse<>(
                new MessageData("Server is running")
        ));
    }
}
