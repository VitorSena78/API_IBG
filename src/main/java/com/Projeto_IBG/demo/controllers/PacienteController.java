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
    
    @GetMapping
    public ResponseEntity<Page<Paciente>> findAll(Pageable pageable) {
        Page<Paciente> pacientes = pacienteService.findAll(pageable);
        return ResponseEntity.ok(pacientes);
    }
    
    @GetMapping("/todos")
    public ResponseEntity<ApiResponse<List<Paciente>>> findAll() {
        try {
            List<Paciente> pacientes = pacienteService.findAll();
            
            if (pacientes.isEmpty()) {
                return ResponseEntity.ok(
                    ApiResponse.success(pacientes, "Nenhum paciente encontrado")
                );
            }
            
            return ResponseEntity.ok(
                ApiResponse.success(pacientes, "Pacientes encontrados com sucesso")
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Erro interno do servidor", e.getMessage()));
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Paciente>> findById(@PathVariable Integer id) {
        try {
            Paciente paciente = pacienteService.findById(id);
            
            if (paciente == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Paciente não encontrado", "ID: " + id));
            }
            
            return ResponseEntity.ok(
                ApiResponse.success(paciente, "Paciente encontrado com sucesso")
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
        
        // Usa o mapper
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
    public ResponseEntity<ApiResponse<Paciente>> create(@Valid @RequestBody Paciente paciente) {
        try {
            Paciente novoPaciente = pacienteService.save(paciente);
            
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(novoPaciente, "Paciente criado com sucesso"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("Erro ao criar paciente", e.getMessage()));
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Paciente>> update(@PathVariable Integer id, @Valid @RequestBody PacienteDTO pacienteDTO) {
        try {
            // Usar o mapper para converter DTO em entidade
            Paciente paciente = pacienteMapper.toEntity(pacienteDTO);
            
            Paciente pacienteAtualizado = pacienteService.update(id, paciente);
            
            if (pacienteAtualizado == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Paciente não encontrado para atualização", "ID: " + id));
            }
            
            return ResponseEntity.ok(
                ApiResponse.success(pacienteAtualizado, "Paciente atualizado com sucesso")
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("Erro ao atualizar paciente", e.getMessage()));
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Integer id) {
        try {
            // Verificar se existe antes de deletar
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
    public ResponseEntity<ApiResponse<List<Paciente>>> findByNome(@RequestParam String nome) {
        try {
            if (nome == null || nome.trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Nome é obrigatório", "Parâmetro 'nome' não pode ser vazio"));
            }
            
            List<Paciente> pacientes = pacienteService.findByNome(nome);
            
            if (pacientes.isEmpty()) {
                return ResponseEntity.ok(
                    ApiResponse.success(pacientes, "Nenhum paciente encontrado com o nome: " + nome)
                );
            }
            
            return ResponseEntity.ok(
                ApiResponse.success(pacientes, "Pacientes encontrados por nome")
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Erro interno do servidor", e.getMessage()));
        }
    }
    
    @GetMapping("/buscar/nome/paginado")
    public ResponseEntity<Page<Paciente>> findByNome(@RequestParam String nome, Pageable pageable) {
        Page<Paciente> pacientes = pacienteService.findByNome(nome, pageable);
        return ResponseEntity.ok(pacientes);
    }
    
    @GetMapping("/buscar/cpf/{cpf}")
    public ResponseEntity<ApiResponse<Paciente>> findByCpf(@PathVariable String cpf) {
        try {
            Optional<Paciente> paciente = pacienteService.findByCpf(cpf);
            
            if (paciente.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Paciente não encontrado", "CPF: " + cpf));
            }
            
            return ResponseEntity.ok(
                ApiResponse.success(paciente.get(), "Paciente encontrado com sucesso")
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Erro interno do servidor", e.getMessage()));
        }
    }
    
    @GetMapping("/buscar/sus/{sus}")
    public ResponseEntity<ApiResponse<Paciente>> findBySus(@PathVariable String sus) {
        try {
            Optional<Paciente> paciente = pacienteService.findBySus(sus);
            
            if (paciente.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Paciente não encontrado", "SUS: " + sus));
            }
            
            return ResponseEntity.ok(
                ApiResponse.success(paciente.get(), "Paciente encontrado com sucesso")
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Erro interno do servidor", e.getMessage()));
        }
    }
    
    @GetMapping("/buscar/especialidade/{especialidadeId}")
    public ResponseEntity<ApiResponse<List<Paciente>>> findByEspecialidade(@PathVariable Integer especialidadeId) {
        try {
            List<Paciente> pacientes = pacienteService.findByEspecialidade(especialidadeId);
            
            if (pacientes.isEmpty()) {
                return ResponseEntity.ok(
                    ApiResponse.success(pacientes, "Nenhum paciente encontrado para a especialidade ID: " + especialidadeId)
                );
            }
            
            return ResponseEntity.ok(
                ApiResponse.success(pacientes, "Pacientes encontrados por especialidade")
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Erro interno do servidor", e.getMessage()));
        }
    }
    
    @GetMapping("/buscar/data-atendimento")
    public ResponseEntity<ApiResponse<List<Paciente>>> findByDataAtendimento(@RequestParam LocalDate dataAtendimento) {
        try {
            List<Paciente> pacientes = pacienteService.findByDataAtendimento(dataAtendimento);
            
            if (pacientes.isEmpty()) {
                return ResponseEntity.ok(
                    ApiResponse.success(pacientes, "Nenhum paciente encontrado para a data: " + dataAtendimento)
                );
            }
            
            return ResponseEntity.ok(
                ApiResponse.success(pacientes, "Pacientes encontrados por data de atendimento")
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Erro interno do servidor", e.getMessage()));
        }
    }
    
    @GetMapping("/buscar/faixa-etaria")
    public ResponseEntity<ApiResponse<List<Paciente>>> findByFaixaEtaria(@RequestParam Integer idadeMin, @RequestParam Integer idadeMax) {
        try {
            if (idadeMin < 0 || idadeMax < 0 || idadeMin > idadeMax) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Parâmetros inválidos", "Idades devem ser positivas e idadeMin <= idadeMax"));
            }
            
            List<Paciente> pacientes = pacienteService.findByFaixaEtaria(idadeMin, idadeMax);
            
            if (pacientes.isEmpty()) {
                return ResponseEntity.ok(
                    ApiResponse.success(pacientes, "Nenhum paciente encontrado na faixa etária " + idadeMin + "-" + idadeMax + " anos")
                );
            }
            
            return ResponseEntity.ok(
                ApiResponse.success(pacientes, "Pacientes encontrados por faixa etária")
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Erro interno do servidor", e.getMessage()));
        }
    }

    @PostMapping("/batch")
    public ResponseEntity<ApiResponse<List<Paciente>>> createBatch(@Valid @RequestBody List<PacienteDTO> pacientesDTO) {
        try {
            if (pacientesDTO == null || pacientesDTO.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Lista de pacientes não pode estar vazia", ""));
            }

            // Usar o mapper para converter DTOs em entidades
            List<Paciente> pacientes = pacientesDTO.stream()
                .map(pacienteMapper::toEntity)
                .collect(Collectors.toList());

            List<Paciente> pacientesSalvos = pacienteService.saveBatch(pacientes);
            
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(pacientesSalvos, 
                    pacientesSalvos.size() + " pacientes criados com sucesso"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Erro ao criar pacientes em lote", e.getMessage()));
        }
    }
}