package com.Projeto_IBG.demo.controllers;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.Projeto_IBG.demo.dto.ApiResponse;
import com.Projeto_IBG.demo.dto.PacienteEspecialidadeDTO;
import com.Projeto_IBG.demo.mappers.PacienteEspecialidadeMapper;
import com.Projeto_IBG.demo.model.PacienteEspecialidade;
import com.Projeto_IBG.demo.services.NotificationService;
import com.Projeto_IBG.demo.services.PacienteEspecialidadeService;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/paciente_has_especialidade")
public class PacienteEspecialidadeController {
    
    private static final Logger LOGGER = Logger.getLogger(PacienteEspecialidadeController.class.getName());
    
    private final PacienteEspecialidadeService pacienteEspecialidadeService;
    private final PacienteEspecialidadeMapper mapper;
    private final NotificationService notificationService;

    public PacienteEspecialidadeController(PacienteEspecialidadeService pacienteEspecialidadeService,
                                            PacienteEspecialidadeMapper mapper,
                                            NotificationService notificationService) {
        this.pacienteEspecialidadeService = pacienteEspecialidadeService;
        this.mapper = mapper;
        this.notificationService = notificationService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PacienteEspecialidadeDTO>>> findAll() {
        try {
            List<PacienteEspecialidade> entities = pacienteEspecialidadeService.findAll();
            List<PacienteEspecialidadeDTO> dtos = entities.stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
            
            return ResponseEntity.ok(
                ApiResponse.success(dtos, "Associações encontradas com sucesso")
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Erro ao buscar associações: " + e.getMessage()));
        }
    }
    
    @PostMapping
    public ResponseEntity<ApiResponse<PacienteEspecialidadeDTO>> create(
            @RequestParam Integer pacienteId, 
            @RequestParam Integer especialidadeId,
            @RequestParam(required = false) LocalDate dataAtendimento) {
        try {
            PacienteEspecialidade atendimento = pacienteEspecialidadeService.save(pacienteId, especialidadeId, dataAtendimento);
            PacienteEspecialidadeDTO dto = mapper.toDTO(atendimento);
            
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(dto, "Associação criada com sucesso"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("Erro ao criar associação", e.getMessage()));
        }
    }
    
    @DeleteMapping
    public ResponseEntity<Void> delete(@RequestParam Integer pacienteId, @RequestParam Integer especialidadeId) {
        pacienteEspecialidadeService.delete(pacienteId, especialidadeId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/pacientes/{pacienteId}/especialidades/{especialidadeId}")
    public ResponseEntity<Void> deleteByPathVariables(@PathVariable Integer pacienteId, 
                                                    @PathVariable Integer especialidadeId) {
        pacienteEspecialidadeService.delete(pacienteId, especialidadeId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/paciente/{pacienteId}")
    public ResponseEntity<ApiResponse<String>> deleteAllByPacienteId(@PathVariable Integer pacienteId) {
        try {
            // Buscar todas as associações do paciente antes de deletar para contar
            List<PacienteEspecialidade> associacoes = pacienteEspecialidadeService.findByPaciente(pacienteId);
            int count = associacoes.size();

            pacienteEspecialidadeService.deleteAllByPacienteId(pacienteId);

            notificationService.notifyPacienteEspecialidadeBatchDeleted(pacienteId, java.util.Collections.emptyList());

            return ResponseEntity.ok(
                ApiResponse.success("Associações removidas",
                    "Removidas " + count + " associações do paciente ID: " + pacienteId)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Erro ao remover associações do paciente: " + e.getMessage()));
        }
    }
    
    @GetMapping("/paciente/{pacienteId}")
    public ResponseEntity<ApiResponse<List<PacienteEspecialidadeDTO>>> findByPaciente(@PathVariable Integer pacienteId) {
        try {
            List<PacienteEspecialidade> entities = pacienteEspecialidadeService.findByPaciente(pacienteId);
            List<PacienteEspecialidadeDTO> dtos = entities.stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
            
            return ResponseEntity.ok(ApiResponse.success(dtos));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Erro: " + e.getMessage()));
        }
    }
    
    @GetMapping("/especialidade/{especialidadeId}")
    public ResponseEntity<ApiResponse<List<PacienteEspecialidadeDTO>>> findByEspecialidade(@PathVariable Integer especialidadeId) {
        try {
            List<PacienteEspecialidade> entities = pacienteEspecialidadeService.findByEspecialidade(especialidadeId);
            List<PacienteEspecialidadeDTO> dtos = entities.stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
            
            if (dtos.isEmpty()) {
                return ResponseEntity.ok(
                    ApiResponse.success(dtos, "Nenhuma associação encontrada para a especialidade ID: " + especialidadeId)
                );
            }
            
            return ResponseEntity.ok(
                ApiResponse.success(dtos, "Associações da especialidade encontradas com sucesso")
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Erro ao buscar associações da especialidade", e.getMessage()));
        }
    }
    
    @GetMapping("/data-atendimento")
    public ResponseEntity<ApiResponse<List<PacienteEspecialidadeDTO>>> findByDataAtendimento(@RequestParam LocalDate dataAtendimento) {
        try {
            List<PacienteEspecialidade> entities = pacienteEspecialidadeService.findByDataAtendimento(dataAtendimento);
            List<PacienteEspecialidadeDTO> dtos = entities.stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
            
            if (dtos.isEmpty()) {
                return ResponseEntity.ok(
                    ApiResponse.success(dtos, "Nenhum atendimento encontrado para a data: " + dataAtendimento)
                );
            }
            
            return ResponseEntity.ok(
                ApiResponse.success(dtos, "Atendimentos encontrados por data")
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Erro ao buscar atendimentos por data", e.getMessage()));
        }
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
                    // VERIFICAR SE É DELEÇÃO
                    if (data.getIsDeleted() != null && data.getIsDeleted()) {
                        LOGGER.info("Processando DELEÇÃO: Paciente " + data.getPacienteServerId() +
                                " - Especialidade " + data.getEspecialidadeServerId());

                        pacienteEspecialidadeService.delete(
                            data.getPacienteServerId(),
                            data.getEspecialidadeServerId()
                        );

                        PacienteEspecialidadeDTO deletedDto = new PacienteEspecialidadeDTO(
                            data.getPacienteServerId(),
                            data.getEspecialidadeServerId(),
                            data.getDataAtendimento(),
                            true,
                            LocalDateTime.now(),
                            data.getCreatedAt()
                        );

                        processedRecords.add(deletedDto);

                    } else {
                        LOGGER.info("Processando CRIAÇÃO: Paciente " + data.getPacienteServerId() +
                                " - Especialidade " + data.getEspecialidadeServerId());

                        PacienteEspecialidade saved = pacienteEspecialidadeService.save(
                            data.getPacienteServerId(),
                            data.getEspecialidadeServerId(),
                            data.getDataAtendimento()
                        );

                        PacienteEspecialidadeDTO dto = new PacienteEspecialidadeDTO(
                            saved.getId().getPacienteId(),
                            saved.getId().getEspecialidadeId(),
                            saved.getDataAtendimento(),
                            false,
                            saved.getUpdatedAt(),
                            saved.getCreatedAt()
                        );

                        processedRecords.add(dto);
                    }

                } catch (Exception e) {
                    LOGGER.severe("Erro ao processar registro: " + data + " - " + e.getMessage());
                    // Continue processando os outros registros
                }
            }
            
            return ResponseEntity.ok(
                ApiResponse.success(processedRecords, "Sincronização processada com sucesso")
            );
            
        } catch (Exception e) {
            LOGGER.severe("Erro interno ao sincronizar relacionamentos: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Erro interno ao sincronizar relacionamentos: " + e.getMessage()));
        }
    }

    @PostMapping("/batch")
    public ResponseEntity<ApiResponse<List<PacienteEspecialidadeDTO>>> createBatch(@RequestBody List<PacienteEspecialidadeDTO> pacienteEspecialidadeDTOs) {
        try {
            LOGGER.info("Iniciando batch create com " + pacienteEspecialidadeDTOs.size() + " itens");
            
            List<PacienteEspecialidadeDTO> savedAssociacoes = new ArrayList<>();
            Set<Integer> pacientesAfetados = new HashSet<>();
            
            for (int i = 0; i < pacienteEspecialidadeDTOs.size(); i++) {
                PacienteEspecialidadeDTO dto = pacienteEspecialidadeDTOs.get(i);
                
                try {
                    Integer pacienteId = null;
                    Integer especialidadeId = null;
                    
                    if (dto.getPacienteId() != null) {
                        pacienteId = dto.getPacienteId();
                    } else if (dto.getPacienteServerId() != null) {
                        pacienteId = dto.getPacienteServerId();
                    }
                    
                    if (dto.getEspecialidadeId() != null) {
                        especialidadeId = dto.getEspecialidadeId();
                    } else if (dto.getEspecialidadeServerId() != null) {
                        especialidadeId = dto.getEspecialidadeServerId();
                    }
                    
                    if (pacienteId == null || especialidadeId == null) {
                        LOGGER.warning("IDs inválidos no item " + (i+1) + " - pacienteId: " + pacienteId +
                                ", especialidadeId: " + especialidadeId);
                        continue;
                    }
                    
                    PacienteEspecialidade saved = pacienteEspecialidadeService.save(
                        pacienteId,
                        especialidadeId,
                        dto.getDataAtendimento()
                    );
                    
                    PacienteEspecialidadeDTO savedDTO = mapper.toDTO(saved);
                    savedAssociacoes.add(savedDTO);
                    
                    pacientesAfetados.add(saved.getPaciente().getId());
                    
                } catch (Exception e) {
                    LOGGER.severe("Erro ao processar item " + (i+1) + ": " + e.getMessage());
                    // Continue processando os outros itens
                }
            }
            
            // Notificar para cada paciente afetado
            for (Integer pacienteId : pacientesAfetados) {
                try {
                    List<PacienteEspecialidadeDTO> associacoesDoPaciente = savedAssociacoes.stream()
                        .filter(a -> a.getPacienteId().equals(pacienteId) || a.getPacienteServerId().equals(pacienteId))
                        .collect(Collectors.toList());
                    notificationService.notifyPacienteEspecialidadeBatchCreated(pacienteId, associacoesDoPaciente);
                } catch (Exception e) {
                    LOGGER.severe("Erro ao enviar notificação para paciente " + pacienteId + ": " + e.getMessage());
                }
            }
            
            LOGGER.info("Batch create concluído: " + savedAssociacoes.size() + " de " + pacienteEspecialidadeDTOs.size());
            
            return ResponseEntity.ok(
                ApiResponse.success(savedAssociacoes,
                    "Criadas " + savedAssociacoes.size() + " de " + pacienteEspecialidadeDTOs.size() + " associações")
            );
            
        } catch (Exception e) {
            LOGGER.severe("Erro geral no batch create: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Erro ao criar associações em lote: " + e.getMessage()));
        }
    }

}
