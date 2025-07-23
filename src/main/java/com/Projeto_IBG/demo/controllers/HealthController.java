package com.Projeto_IBG.demo.controllers;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {
    @GetMapping("/api/health")
    public ResponseEntity<Map<String, String>> healthCheck() {
        Map<String, String> status = Map.of("status", "UP");
        return ResponseEntity.ok(status);
    }
}
