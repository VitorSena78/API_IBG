package com.Projeto_IBG.demo.controllers;

import com.Projeto_IBG.demo.dto.ApiResponse;
import com.Projeto_IBG.demo.services.RelatorioService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/relatorios")
public class RelatorioController {

    private final RelatorioService relatorioService;

    public RelatorioController(RelatorioService relatorioService) {
        this.relatorioService = relatorioService;
    }

    @GetMapping("/diario")
    public ResponseEntity<ApiResponse<Map<String, Object>>> relatorioDiario(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        try {
            if (data == null) data = LocalDate.now();
            Map<String, Object> relatorio = relatorioService.relatorioDiario(data);
            return ResponseEntity.ok(ApiResponse.success(relatorio, "Relatório diário"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Erro ao gerar relatório", e.getMessage()));
        }
    }

    @GetMapping("/mensal")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> relatorioMensal(
            @RequestParam(required = false) Integer ano) {
        try {
            if (ano == null) ano = LocalDate.now().getYear();
            List<Map<String, Object>> relatorio = relatorioService.relatorioMensal(ano);
            return ResponseEntity.ok(ApiResponse.success(relatorio, "Relatório mensal"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Erro ao gerar relatório", e.getMessage()));
        }
    }

    @GetMapping("/resumo")
    public ResponseEntity<ApiResponse<Map<String, Object>>> resumoGeral() {
        try {
            Map<String, Object> resumo = relatorioService.resumoGeral();
            return ResponseEntity.ok(ApiResponse.success(resumo, "Resumo geral"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Erro ao gerar resumo", e.getMessage()));
        }
    }
}
