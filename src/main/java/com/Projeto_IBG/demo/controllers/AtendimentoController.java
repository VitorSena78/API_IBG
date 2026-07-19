package com.Projeto_IBG.demo.controllers;

import com.Projeto_IBG.demo.dto.ApiResponse;
import com.Projeto_IBG.demo.dto.AtendimentoRequestDTO;
import com.Projeto_IBG.demo.dto.AtendimentoResponseDTO;
import com.Projeto_IBG.demo.security.UserDetailsImpl;
import com.Projeto_IBG.demo.services.AtendimentoService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/atendimentos")
public class AtendimentoController {

    private final AtendimentoService atendimentoService;

    public AtendimentoController(AtendimentoService atendimentoService) {
        this.atendimentoService = atendimentoService;
    }

    private Integer getUserId(Authentication auth) {
        return ((UserDetailsImpl) auth.getPrincipal()).getId();
    }

    @PostMapping
    public ResponseEntity<ApiResponse<AtendimentoResponseDTO>> iniciar(
            @RequestBody AtendimentoRequestDTO request,
            Authentication auth) {
        try {
            Integer recepcionistaId = getUserId(auth);
            AtendimentoResponseDTO response = atendimentoService.iniciarAtendimento(request, recepcionistaId);
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Atendimento iniciado com sucesso"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("Erro ao iniciar atendimento", e.getMessage()));
        }
    }

    @PutMapping("/{id}/triagem")
    public ResponseEntity<ApiResponse<AtendimentoResponseDTO>> realizarTriagem(
            @PathVariable Integer id,
            @RequestBody AtendimentoRequestDTO request,
            Authentication auth) {
        try {
            Integer enfermeiraId = getUserId(auth);
            AtendimentoResponseDTO response = atendimentoService.realizarTriagem(id, request, enfermeiraId);
            return ResponseEntity.ok(ApiResponse.success(response, "Triagem realizada com sucesso"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("Erro ao realizar triagem", e.getMessage()));
        }
    }

    @PutMapping("/{id}/consulta")
    public ResponseEntity<ApiResponse<AtendimentoResponseDTO>> realizarConsulta(
            @PathVariable Integer id,
            @RequestBody AtendimentoRequestDTO request,
            Authentication auth) {
        try {
            Integer medicoId = getUserId(auth);
            AtendimentoResponseDTO response = atendimentoService.realizarConsulta(id, request, medicoId);
            return ResponseEntity.ok(ApiResponse.success(response, "Consulta realizada com sucesso"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("Erro ao realizar consulta", e.getMessage()));
        }
    }

    @PutMapping("/{id}/triagem/editar")
    public ResponseEntity<ApiResponse<AtendimentoResponseDTO>> editarTriagem(
            @PathVariable Integer id,
            @RequestBody AtendimentoRequestDTO request,
            Authentication auth) {
        try {
            Integer enfermeiraId = getUserId(auth);
            AtendimentoResponseDTO response = atendimentoService.editarTriagem(id, request, enfermeiraId);
            return ResponseEntity.ok(ApiResponse.success(response, "Triagem atualizada com sucesso"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("Erro ao editar triagem", e.getMessage()));
        }
    }

    @PutMapping("/{id}/consulta/editar")
    public ResponseEntity<ApiResponse<AtendimentoResponseDTO>> editarConsulta(
            @PathVariable Integer id,
            @RequestBody AtendimentoRequestDTO request,
            Authentication auth) {
        try {
            Integer medicoId = getUserId(auth);
            AtendimentoResponseDTO response = atendimentoService.editarConsulta(id, request, medicoId);
            return ResponseEntity.ok(ApiResponse.success(response, "Consulta atualizada com sucesso"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("Erro ao editar consulta", e.getMessage()));
        }
    }

    @PutMapping("/{id}/cancelar")
    public ResponseEntity<ApiResponse<AtendimentoResponseDTO>> cancelar(@PathVariable Integer id) {
        try {
            AtendimentoResponseDTO response = atendimentoService.cancelarAtendimento(id);
            return ResponseEntity.ok(ApiResponse.success(response, "Atendimento cancelado"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("Erro ao cancelar atendimento", e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<AtendimentoResponseDTO>>> listar(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Integer especialidadeId) {
        try {
            List<AtendimentoResponseDTO> lista = atendimentoService.listarPorStatus(status, especialidadeId);
            return ResponseEntity.ok(ApiResponse.success(lista, "Lista de atendimentos"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Erro ao listar atendimentos", e.getMessage()));
        }
    }

    @GetMapping("/data")
    public ResponseEntity<ApiResponse<List<AtendimentoResponseDTO>>> listarPorData(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        try {
            List<AtendimentoResponseDTO> lista = atendimentoService.listarPorData(data);
            return ResponseEntity.ok(ApiResponse.success(lista, "Atendimentos da data " + data));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Erro ao listar atendimentos por data", e.getMessage()));
        }
    }

    @GetMapping("/paciente/{pacienteId}")
    public ResponseEntity<ApiResponse<List<AtendimentoResponseDTO>>> historicoPaciente(
            @PathVariable Integer pacienteId) {
        try {
            List<AtendimentoResponseDTO> historico = atendimentoService.historicoPaciente(pacienteId);
            return ResponseEntity.ok(ApiResponse.success(historico, "Histórico do paciente"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Erro ao buscar histórico", e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AtendimentoResponseDTO>> buscarPorId(@PathVariable Integer id) {
        try {
            AtendimentoResponseDTO response = atendimentoService.buscarPorId(id);
            return ResponseEntity.ok(ApiResponse.success(response, "Atendimento encontrado"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error("Atendimento não encontrado", e.getMessage()));
        }
    }
}
