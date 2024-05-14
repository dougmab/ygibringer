package com.github.dougmab.ygibringer.server.controller;

import com.github.dougmab.ygibringer.app.model.Status;
import com.github.dougmab.ygibringer.app.service.ConfigurationService;
import com.github.dougmab.ygibringer.server.model.SuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/status")
public class StatusController {

    @GetMapping
    public ResponseEntity<SuccessResponse<List<Status>>> getCustomStatus() {
        List<Status> customStatus = ConfigurationService.getConfig().customStatus;

        return ResponseEntity.ok(new SuccessResponse<>(customStatus));
    }
}
