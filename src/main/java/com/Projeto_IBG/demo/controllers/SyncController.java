package com.Projeto_IBG.demo.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Projeto_IBG.demo.dto.ConflictResolutionDTO;
import com.Projeto_IBG.demo.dto.EspecialidadeDTO;
import com.Projeto_IBG.demo.dto.sync.SyncRequestDTO;
import com.Projeto_IBG.demo.dto.sync.SyncResponseDTO;
import com.Projeto_IBG.demo.services.SyncService;

@RestController
@RequestMapping("/api/sync")
public class SyncController {
    
    private final SyncService syncService;

    public SyncController(SyncService syncService) {
        this.syncService = syncService;
    }
    
    @PostMapping("/pacientes")
    public ResponseEntity<SyncResponseDTO> syncPacientes(@RequestBody SyncRequestDTO request) {
        try {
            SyncResponseDTO response = syncService.syncPacientes(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new SyncResponseDTO("Erro na sincronização: " + e.getMessage()));
        }
    }
    
    @GetMapping("/especialidades")
    public ResponseEntity<List<EspecialidadeDTO>> getEspecialidades() {
        List<EspecialidadeDTO> especialidades = syncService.getAllEspecialidades();
        return ResponseEntity.ok(especialidades);
    }
    
    @PostMapping("/resolve-conflict")
    public ResponseEntity<ConflictResolutionDTO> resolveConflict(@RequestBody ConflictResolutionDTO resolution) {
        ConflictResolutionDTO result = syncService.resolveConflict(resolution);
        return ResponseEntity.ok(result);
    }
}
