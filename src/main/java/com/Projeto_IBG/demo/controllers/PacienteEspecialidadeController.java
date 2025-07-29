package com.Projeto_IBG.demo.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.Projeto_IBG.demo.dto.ApiResponse;
import com.Projeto_IBG.demo.dto.PacienteEspecialidadeDTO;
import com.Projeto_IBG.demo.model.PacienteEspecialidade;
import com.Projeto_IBG.demo.services.PacienteEspecialidadeService;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pacientes_has_especialidades")
@CrossOrigin(origins = "*")
public class PacienteEspecialidadeController {
    
    @Autowired
    private PacienteEspecialidadeService pacienteEspecialidadeService;
    
    @GetMapping
    public ResponseEntity<List<PacienteEspecialidade>> findAll() {
        List<PacienteEspecialidade> atendimentos = pacienteEspecialidadeService.findAll();
        return ResponseEntity.ok(atendimentos);
    }
    
    @PostMapping
    public ResponseEntity<PacienteEspecialidade> create(@RequestParam Integer pacienteId, 
                                                        @RequestParam Integer especialidadeId,
                                                        @RequestParam(required = false) LocalDate dataAtendimento) {
        PacienteEspecialidade atendimento = pacienteEspecialidadeService.save(pacienteId, especialidadeId, dataAtendimento);
        return ResponseEntity.status(HttpStatus.CREATED).body(atendimento);
    }
    
    @DeleteMapping
    public ResponseEntity<Void> delete(@RequestParam Integer pacienteId, @RequestParam Integer especialidadeId) {
        pacienteEspecialidadeService.delete(pacienteId, especialidadeId);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/paciente/{pacienteId}")
    public ResponseEntity<List<PacienteEspecialidade>> findByPaciente(@PathVariable Integer pacienteId) {
        List<PacienteEspecialidade> atendimentos = pacienteEspecialidadeService.findByPaciente(pacienteId);
        return ResponseEntity.ok(atendimentos);
    }
    
    @GetMapping("/especialidade/{especialidadeId}")
    public ResponseEntity<List<PacienteEspecialidade>> findByEspecialidade(@PathVariable Integer especialidadeId) {
        List<PacienteEspecialidade> atendimentos = pacienteEspecialidadeService.findByEspecialidade(especialidadeId);
        return ResponseEntity.ok(atendimentos);
    }
    
    @GetMapping("/data-atendimento")
    public ResponseEntity<List<PacienteEspecialidade>> findByDataAtendimento(@RequestParam LocalDate dataAtendimento) {
        List<PacienteEspecialidade> atendimentos = pacienteEspecialidadeService.findByDataAtendimento(dataAtendimento);
        return ResponseEntity.ok(atendimentos);
    }
    
    @GetMapping("/relatorio/por-especialidade")
    public ResponseEntity<List<Object[]>> getRelatorioAtendimentosPorEspecialidade() {
        List<Object[]> relatorio = pacienteEspecialidadeService.getRelatorioAtendimentosPorEspecialidade();
        return ResponseEntity.ok(relatorio);
    }
    
    @GetMapping("/relatorio/por-mes")
    public ResponseEntity<List<Object[]>> getRelatorioAtendimentosPorMes() {
        List<Object[]> relatorio = pacienteEspecialidadeService.getRelatorioAtendimentosPorMes();
        return ResponseEntity.ok(relatorio);
    }

    @GetMapping("/pacientes/especialidades/updated")
    public ResponseEntity<ApiResponse<List<PacienteEspecialidadeDTO>>> getUpdatedPacienteEspecialidades(
            @RequestParam("since") Long timestamp) {
        try {
            // Converte o timestamp para LocalDateTime
            LocalDateTime since = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(timestamp), 
                ZoneId.systemDefault()
            );
            
            List<PacienteEspecialidadeDTO> updatedRecords = 
                pacienteEspecialidadeService.findUpdatedSince(since);
            
            return ResponseEntity.ok(
                ApiResponse.success(updatedRecords, "Relacionamentos atualizados recuperados com sucesso")
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Erro ao buscar relacionamentos atualizados: " + e.getMessage()));
        }
    }

    @PostMapping("/pacientes/especialidades/sync")
    public ResponseEntity<ApiResponse<List<PacienteEspecialidadeDTO>>> syncPacienteEspecialidades(
            @RequestBody List<PacienteEspecialidadeDTO> syncData) {
        try {
            List<PacienteEspecialidadeDTO> processedRecords = new ArrayList<>();
            
            for (PacienteEspecialidadeDTO data : syncData) {
                try {
                    // Usar o método save existente que já verifica paciente e especialidade
                    PacienteEspecialidade saved = pacienteEspecialidadeService.save(
                        data.getPacienteServerId(), 
                        data.getEspecialidadeServerId(), 
                        data.getDataAtendimento()
                    );
                    
                    // Converter para DTO usando o seu DTO existente
                    PacienteEspecialidadeDTO dto = new PacienteEspecialidadeDTO(
                        saved.getId().getPacienteId(),
                        saved.getId().getEspecialidadeId(),
                        saved.getDataAtendimento(),
                        false, // isDeleted
                        saved.getUpdatedAt(),
                        saved.getCreatedAt()
                    );
                    
                    processedRecords.add(dto);
                    
                } catch (Exception e) {
                    // Log do erro específico para este registro
                    System.err.println("Erro ao processar registro: " + data.toString() + " - " + e.getMessage());
                    // Continue processando os outros registros
                }
            }
            
            // retorna o formato padronizado ApiResponse
            return ResponseEntity.ok(
                ApiResponse.success(processedRecords, "Sincronização processada com sucesso")
            );
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Erro interno ao sincronizar relacionamentos: " + e.getMessage()));
        }
    }

}
