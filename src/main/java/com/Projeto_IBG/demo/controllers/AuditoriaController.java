package com.Projeto_IBG.demo.controllers;

import com.Projeto_IBG.demo.dto.ApiResponse;
import com.Projeto_IBG.demo.model.Auditoria;
import com.Projeto_IBG.demo.services.AuditoriaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auditoria")
public class AuditoriaController {

    private final AuditoriaService auditoriaService;

    public AuditoriaController(AuditoriaService auditoriaService) {
        this.auditoriaService = auditoriaService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Auditoria>>> listarTodas() {
        try {
            List<Auditoria> registros = auditoriaService.listarTodas();
            return ResponseEntity.ok(ApiResponse.success(registros, "Registros de auditoria"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Erro ao listar auditoria", e.getMessage()));
        }
    }

    @GetMapping("/entidade/{entidade}/{entidadeId}")
    public ResponseEntity<ApiResponse<List<Auditoria>>> listarPorEntidade(
            @PathVariable String entidade,
            @PathVariable Integer entidadeId) {
        try {
            List<Auditoria> registros = auditoriaService.listarPorEntidade(entidade, entidadeId);
            return ResponseEntity.ok(ApiResponse.success(registros, "Auditoria da entidade " + entidade));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Erro ao buscar auditoria", e.getMessage()));
        }
    }
}
