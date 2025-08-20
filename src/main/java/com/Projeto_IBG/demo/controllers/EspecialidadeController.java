package com.Projeto_IBG.demo.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.Projeto_IBG.demo.dto.ApiResponse;
import com.Projeto_IBG.demo.dto.EspecialidadeDTO;
import com.Projeto_IBG.demo.mappers.EspecialidadeMapper;
import com.Projeto_IBG.demo.model.Especialidade;
import com.Projeto_IBG.demo.services.EspecialidadeService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/especialidades")
@CrossOrigin(origins = "*")
public class EspecialidadeController {
    
    @Autowired
    private EspecialidadeService especialidadeService;
    
    @Autowired
    private EspecialidadeMapper especialidadeMapper;
    
    @GetMapping
    public ResponseEntity<ApiResponse<List<EspecialidadeDTO>>> findAll() {
        try {
            List<Especialidade> especialidades = especialidadeService.findAll();
            
            // Converter para DTO
            List<EspecialidadeDTO> especialidadesDTO = especialidades.stream()
                .map(especialidadeMapper::toDTO)
                .collect(Collectors.toList());
            
            if (especialidadesDTO.isEmpty()) {
                return ResponseEntity.ok(
                    ApiResponse.success(especialidadesDTO, "Nenhuma especialidade encontrada")
                );
            }
            
            return ResponseEntity.ok(
                ApiResponse.success(especialidadesDTO, "Especialidades encontradas com sucesso")
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Erro interno do servidor", e.getMessage()));
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EspecialidadeDTO>> findById(@PathVariable Integer id) {
        try {
            Especialidade especialidade = especialidadeService.findById(id);
            
            if (especialidade == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Especialidade não encontrada", "ID: " + id));
            }
            
            // Converter para DTO
            EspecialidadeDTO especialidadeDTO = especialidadeMapper.toDTO(especialidade);
            
            return ResponseEntity.ok(
                ApiResponse.success(especialidadeDTO, "Especialidade encontrada com sucesso")
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Erro interno do servidor", e.getMessage()));
        }
    }
    
    @PostMapping
    public ResponseEntity<ApiResponse<EspecialidadeDTO>> create(@Valid @RequestBody EspecialidadeDTO especialidadeDTO) {
        try {
            // Converter DTO para Entity
            Especialidade especialidade = especialidadeMapper.toEntity(especialidadeDTO);
            Especialidade novaEspecialidade = especialidadeService.save(especialidade);
            
            // Converter de volta para DTO
            EspecialidadeDTO novaEspecialidadeDTO = especialidadeMapper.toDTO(novaEspecialidade);
            
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(novaEspecialidadeDTO, "Especialidade criada com sucesso"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("Erro ao criar especialidade", e.getMessage()));
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<EspecialidadeDTO>> update(
            @PathVariable Integer id, 
            @Valid @RequestBody EspecialidadeDTO especialidadeDTO) {
        try {
            // Converter DTO para Entity
            Especialidade especialidade = especialidadeMapper.toEntity(especialidadeDTO);
            Especialidade especialidadeAtualizada = especialidadeService.update(id, especialidade);
            
            if (especialidadeAtualizada == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Especialidade não encontrada para atualização", "ID: " + id));
            }
            
            // Converter para DTO
            EspecialidadeDTO especialidadeAtualizadaDTO = especialidadeMapper.toDTO(especialidadeAtualizada);
            
            return ResponseEntity.ok(
                ApiResponse.success(especialidadeAtualizadaDTO, "Especialidade atualizada com sucesso")
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("Erro ao atualizar especialidade", e.getMessage()));
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Integer id) {
        try {
            // Verificar se existe antes de deletar
            Especialidade especialidade = especialidadeService.findById(id);
            if (especialidade == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Especialidade não encontrada para exclusão", "ID: " + id));
            }
            
            especialidadeService.delete(id);
            
            return ResponseEntity.ok(
                ApiResponse.success("Especialidade deletada com sucesso")
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Erro ao deletar especialidade", e.getMessage()));
        }
    }
    
    @GetMapping("/buscar/nome")
    public ResponseEntity<ApiResponse<List<EspecialidadeDTO>>> findByNome(@RequestParam String nome) {
        try {
            if (nome == null || nome.trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Nome é obrigatório", "Parâmetro 'nome' não pode ser vazio"));
            }
            
            List<Especialidade> especialidades = especialidadeService.findByNome(nome);
            
            // Converter para DTO
            List<EspecialidadeDTO> especialidadesDTO = especialidades.stream()
                .map(especialidadeMapper::toDTO)
                .collect(Collectors.toList());
            
            if (especialidadesDTO.isEmpty()) {
                return ResponseEntity.ok(
                    ApiResponse.success(especialidadesDTO, "Nenhuma especialidade encontrada com o nome: " + nome)
                );
            }
            
            return ResponseEntity.ok(
                ApiResponse.success(especialidadesDTO, "Especialidades encontradas por nome")
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Erro interno do servidor", e.getMessage()));
        }
    }
    
    @GetMapping("/com-pacientes")
    public ResponseEntity<ApiResponse<List<EspecialidadeDTO>>> findEspecialidadesComPacientes() {
        try {
            List<Especialidade> especialidades = especialidadeService.findEspecialidadesComPacientes();
            
            // Converter para DTO
            List<EspecialidadeDTO> especialidadesDTO = especialidades.stream()
                .map(especialidadeMapper::toDTO)
                .collect(Collectors.toList());
            
            if (especialidadesDTO.isEmpty()) {
                return ResponseEntity.ok(
                    ApiResponse.success(especialidadesDTO, "Nenhuma especialidade com pacientes encontrada")
                );
            }
            
            return ResponseEntity.ok(
                ApiResponse.success(especialidadesDTO, "Especialidades com pacientes encontradas")
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Erro interno do servidor", e.getMessage()));
        }
    }

    @GetMapping("/updated")
    public ResponseEntity<ApiResponse<List<EspecialidadeDTO>>> findUpdatedEspecialidades(
            @RequestParam("since") long timestamp) {
        try {
            if (timestamp <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Timestamp inválido", "O timestamp deve ser maior que 0"));
            }
            
            List<Especialidade> especialidades = especialidadeService.findUpdatedSince(timestamp);
            
            // Converter para DTO
            List<EspecialidadeDTO> especialidadesDTO = especialidades.stream()
                .map(especialidadeMapper::toDTO)
                .collect(Collectors.toList());
            
            if (especialidadesDTO.isEmpty()) {
                return ResponseEntity.ok(
                    ApiResponse.success(especialidadesDTO, "Nenhuma especialidade encontrada após o timestamp fornecido")
                );
            }
            
            return ResponseEntity.ok(
                ApiResponse.success(especialidadesDTO, 
                    String.format("Encontradas %d especialidades atualizadas", especialidadesDTO.size()))
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Erro interno do servidor", e.getMessage()));
        }
    }
}