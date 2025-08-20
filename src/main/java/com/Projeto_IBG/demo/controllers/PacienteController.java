package com.Projeto_IBG.demo.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.Projeto_IBG.demo.dto.ApiResponse;
import com.Projeto_IBG.demo.dto.PacienteDTO;
import com.Projeto_IBG.demo.mappers.PacienteMapper;
import com.Projeto_IBG.demo.model.Paciente;
import com.Projeto_IBG.demo.services.PacienteService;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/pacientes")
@CrossOrigin(origins = "*")
public class PacienteController {
    
    @Autowired
    private PacienteService pacienteService;
    
    @Autowired
    private PacienteMapper pacienteMapper;
    
    // Endpoint paginado - convertendo para DTO
    @GetMapping
    public ResponseEntity<ApiResponse<List<PacienteDTO>>> findAll(Pageable pageable) {
        try {
            Page<Paciente> pacientesPage = pacienteService.findAll(pageable);
            
            List<PacienteDTO> pacientesDTO = pacientesPage.getContent().stream()
                .map(pacienteMapper::toDTO)
                .collect(Collectors.toList());
            
            if (pacientesDTO.isEmpty()) {
                return ResponseEntity.ok(
                    ApiResponse.success(pacientesDTO, "Nenhum paciente encontrado na página solicitada")
                );
            }
            
            return ResponseEntity.ok(
                ApiResponse.success(pacientesDTO, 
                    String.format("Página %d de %d - Total: %d pacientes", 
                        pacientesPage.getNumber() + 1, 
                        pacientesPage.getTotalPages(), 
                        pacientesPage.getTotalElements()))
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Erro interno do servidor", e.getMessage()));
        }
    }
    
    @GetMapping("/todos")
    public ResponseEntity<ApiResponse<List<PacienteDTO>>> findAll() {
        try {
            List<Paciente> pacientes = pacienteService.findAll();
            
            List<PacienteDTO> pacientesDTO = pacientes.stream()
                .map(pacienteMapper::toDTO)
                .collect(Collectors.toList());
            
            if (pacientesDTO.isEmpty()) {
                return ResponseEntity.ok(
                    ApiResponse.success(pacientesDTO, "Nenhum paciente encontrado")
                );
            }
            
            return ResponseEntity.ok(
                ApiResponse.success(pacientesDTO, "Pacientes encontrados com sucesso")
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Erro interno do servidor", e.getMessage()));
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PacienteDTO>> findById(@PathVariable Integer id) {
        try {
            Paciente paciente = pacienteService.findById(id);
            
            if (paciente == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Paciente não encontrado", "ID: " + id));
            }
            
            PacienteDTO pacienteDTO = pacienteMapper.toDTO(paciente);
            
            return ResponseEntity.ok(
                ApiResponse.success(pacienteDTO, "Paciente encontrado com sucesso")
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Erro interno do servidor", e.getMessage()));
        }
    }

    @GetMapping("/updated")
    public ResponseEntity<ApiResponse<List<PacienteDTO>>> findUpdatedPacientes(@RequestParam Long since) {
        try {
            LocalDateTime sinceDateTime = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(since), 
                ZoneId.systemDefault()
            );
            
            List<Paciente> pacientes = pacienteService.findUpdatedSince(sinceDateTime);
            
            List<PacienteDTO> pacientesDTO = pacientes.stream()
                .map(pacienteMapper::toDTO)
                .collect(Collectors.toList());
            
            if (pacientesDTO.isEmpty()) {
                return ResponseEntity.ok(
                    ApiResponse.success(pacientesDTO, "Nenhum paciente atualizado encontrado desde " + sinceDateTime)
                );
            }
            
            return ResponseEntity.ok(
                ApiResponse.success(pacientesDTO, pacientesDTO.size() + " pacientes atualizados encontrados")
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Erro ao buscar pacientes atualizados", e.getMessage()));
        }
    }
    
    @PostMapping
    public ResponseEntity<ApiResponse<PacienteDTO>> create(@Valid @RequestBody PacienteDTO pacienteDTO) {
        try {
            Paciente paciente = pacienteMapper.toEntity(pacienteDTO);
            Paciente novoPaciente = pacienteService.save(paciente);
            
            PacienteDTO novoDTO = pacienteMapper.toDTO(novoPaciente);
            
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(novoDTO, "Paciente criado com sucesso"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("Erro ao criar paciente", e.getMessage()));
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PacienteDTO>> update(@PathVariable Integer id, @Valid @RequestBody PacienteDTO pacienteDTO) {
        try {
            Paciente paciente = pacienteMapper.toEntity(pacienteDTO);
            Paciente pacienteAtualizado = pacienteService.update(id, paciente);
            
            if (pacienteAtualizado == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Paciente não encontrado para atualização", "ID: " + id));
            }
            
            PacienteDTO pacienteDTO_result = pacienteMapper.toDTO(pacienteAtualizado);
            
            return ResponseEntity.ok(
                ApiResponse.success(pacienteDTO_result, "Paciente atualizado com sucesso")
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("Erro ao atualizar paciente", e.getMessage()));
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Integer id) {
        try {
            Paciente paciente = pacienteService.findById(id);
            if (paciente == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Paciente não encontrado para exclusão", "ID: " + id));
            }
            
            pacienteService.delete(id);
            
            return ResponseEntity.ok(
                ApiResponse.success("Paciente deletado com sucesso")
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Erro ao deletar paciente", e.getMessage()));
        }
    }
    
    @GetMapping("/buscar/nome")
    public ResponseEntity<ApiResponse<List<PacienteDTO>>> findByNome(@RequestParam String nome) {
        try {
            if (nome == null || nome.trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Nome é obrigatório", "Parâmetro 'nome' não pode ser vazio"));
            }
            
            List<Paciente> pacientes = pacienteService.findByNome(nome);
            
            List<PacienteDTO> pacientesDTO = pacientes.stream()
                .map(pacienteMapper::toDTO)
                .collect(Collectors.toList());
            
            if (pacientesDTO.isEmpty()) {
                return ResponseEntity.ok(
                    ApiResponse.success(pacientesDTO, "Nenhum paciente encontrado com o nome: " + nome)
                );
            }
            
            return ResponseEntity.ok(
                ApiResponse.success(pacientesDTO, "Pacientes encontrados por nome")
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Erro interno do servidor", e.getMessage()));
        }
    }
    
    @GetMapping("/buscar/nome/paginado")
    public ResponseEntity<ApiResponse<List<PacienteDTO>>> findByNome(@RequestParam String nome, Pageable pageable) {
        try {
            if (nome == null || nome.trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Nome é obrigatório", "Parâmetro 'nome' não pode ser vazio"));
            }
            
            Page<Paciente> pacientesPage = pacienteService.findByNome(nome, pageable);
            
            List<PacienteDTO> pacientesDTO = pacientesPage.getContent().stream()
                .map(pacienteMapper::toDTO)
                .collect(Collectors.toList());
            
            if (pacientesDTO.isEmpty()) {
                return ResponseEntity.ok(
                    ApiResponse.success(pacientesDTO, "Nenhum paciente encontrado com o nome: " + nome)
                );
            }
            
            return ResponseEntity.ok(
                ApiResponse.success(pacientesDTO, 
                    String.format("Página %d de %d - %d pacientes encontrados com nome: %s", 
                        pacientesPage.getNumber() + 1, 
                        pacientesPage.getTotalPages(), 
                        pacientesPage.getTotalElements(),
                        nome))
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Erro interno do servidor", e.getMessage()));
        }
    }
    
    @GetMapping("/buscar/cpf/{cpf}")
    public ResponseEntity<ApiResponse<PacienteDTO>> findByCpf(@PathVariable String cpf) {
        try {
            Optional<Paciente> paciente = pacienteService.findByCpf(cpf);
            
            if (paciente.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Paciente não encontrado", "CPF: " + cpf));
            }
            
            PacienteDTO pacienteDTO = pacienteMapper.toDTO(paciente.get());
            
            return ResponseEntity.ok(
                ApiResponse.success(pacienteDTO, "Paciente encontrado com sucesso")
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Erro interno do servidor", e.getMessage()));
        }
    }
    
    @GetMapping("/buscar/sus/{sus}")
    public ResponseEntity<ApiResponse<PacienteDTO>> findBySus(@PathVariable String sus) {
        try {
            Optional<Paciente> paciente = pacienteService.findBySus(sus);
            
            if (paciente.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Paciente não encontrado", "SUS: " + sus));
            }
            
            PacienteDTO pacienteDTO = pacienteMapper.toDTO(paciente.get());
            
            return ResponseEntity.ok(
                ApiResponse.success(pacienteDTO, "Paciente encontrado com sucesso")
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Erro interno do servidor", e.getMessage()));
        }
    }
    
    @GetMapping("/buscar/especialidade/{especialidadeId}")
    public ResponseEntity<ApiResponse<List<PacienteDTO>>> findByEspecialidade(@PathVariable Integer especialidadeId) {
        try {
            List<Paciente> pacientes = pacienteService.findByEspecialidade(especialidadeId);
            
            List<PacienteDTO> pacientesDTO = pacientes.stream()
                .map(pacienteMapper::toDTO)
                .collect(Collectors.toList());
            
            if (pacientesDTO.isEmpty()) {
                return ResponseEntity.ok(
                    ApiResponse.success(pacientesDTO, "Nenhum paciente encontrado para a especialidade ID: " + especialidadeId)
                );
            }
            
            return ResponseEntity.ok(
                ApiResponse.success(pacientesDTO, "Pacientes encontrados por especialidade")
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Erro interno do servidor", e.getMessage()));
        }
    }
    
    @GetMapping("/buscar/data-atendimento")
    public ResponseEntity<ApiResponse<List<PacienteDTO>>> findByDataAtendimento(@RequestParam LocalDate dataAtendimento) {
        try {
            List<Paciente> pacientes = pacienteService.findByDataAtendimento(dataAtendimento);
            
            List<PacienteDTO> pacientesDTO = pacientes.stream()
                .map(pacienteMapper::toDTO)
                .collect(Collectors.toList());
            
            if (pacientesDTO.isEmpty()) {
                return ResponseEntity.ok(
                    ApiResponse.success(pacientesDTO, "Nenhum paciente encontrado para a data: " + dataAtendimento)
                );
            }
            
            return ResponseEntity.ok(
                ApiResponse.success(pacientesDTO, "Pacientes encontrados por data de atendimento")
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Erro interno do servidor", e.getMessage()));
        }
    }
    
    @GetMapping("/buscar/faixa-etaria")
    public ResponseEntity<ApiResponse<List<PacienteDTO>>> findByFaixaEtaria(@RequestParam Integer idadeMin, @RequestParam Integer idadeMax) {
        try {
            if (idadeMin < 0 || idadeMax < 0 || idadeMin > idadeMax) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Parâmetros inválidos", "Idades devem ser positivas e idadeMin <= idadeMax"));
            }
            
            List<Paciente> pacientes = pacienteService.findByFaixaEtaria(idadeMin, idadeMax);
            
            List<PacienteDTO> pacientesDTO = pacientes.stream()
                .map(pacienteMapper::toDTO)
                .collect(Collectors.toList());
            
            if (pacientesDTO.isEmpty()) {
                return ResponseEntity.ok(
                    ApiResponse.success(pacientesDTO, "Nenhum paciente encontrado na faixa etária " + idadeMin + "-" + idadeMax + " anos")
                );
            }
            
            return ResponseEntity.ok(
                ApiResponse.success(pacientesDTO, "Pacientes encontrados por faixa etária")
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Erro interno do servidor", e.getMessage()));
        }
    }

    @GetMapping("/count")
    public ResponseEntity<ApiResponse<Long>> countPacientes() {
        try {
            long count = pacienteService.count();
            return ResponseEntity.ok(
                ApiResponse.success(count, "Contagem de pacientes realizada com sucesso")
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Erro ao contar pacientes", e.getMessage()));
        }
    }

    @PostMapping("/batch")
    public ResponseEntity<ApiResponse<List<PacienteDTO>>> createBatch(@Valid @RequestBody List<PacienteDTO> pacientesDTO) {
        try {
            if (pacientesDTO == null || pacientesDTO.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Lista de pacientes não pode estar vazia", ""));
            }

            List<Paciente> pacientes = pacientesDTO.stream()
                .map(pacienteMapper::toEntity)
                .collect(Collectors.toList());

            List<Paciente> pacientesSalvos = pacienteService.saveBatch(pacientes);
            
            List<PacienteDTO> pacientesSalvosDTO = pacientesSalvos.stream()
                .map(pacienteMapper::toDTO)
                .collect(Collectors.toList());
            
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(pacientesSalvosDTO, 
                    pacientesSalvosDTO.size() + " pacientes criados com sucesso"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Erro ao criar pacientes em lote", e.getMessage()));
        }
    }
}