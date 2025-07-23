package com.Projeto_IBG.demo.controllers;


import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.Projeto_IBG.demo.dto.ApiResponse;
import com.Projeto_IBG.demo.model.Especialidade;
import com.Projeto_IBG.demo.services.EspecialidadeService;

import java.util.List;

@RestController
@RequestMapping("/api/especialidades")
@CrossOrigin(origins = "*")
public class EspecialidadeController {
    
    @Autowired
    private EspecialidadeService especialidadeService;
    
    @GetMapping
    public ResponseEntity<ApiResponse<List<Especialidade>>> findAll() {
        try {
            List<Especialidade> especialidades = especialidadeService.findAll();
            
            if (especialidades.isEmpty()) {
                // Retorna sucesso mesmo com lista vazia, mas com mensagem informativa
                return ResponseEntity.ok(
                    ApiResponse.success(especialidades, "Nenhuma especialidade encontrada")
                );
            }
            
            return ResponseEntity.ok(
                ApiResponse.success(especialidades, "Especialidades encontradas com sucesso")
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Erro interno do servidor", e.getMessage()));
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Especialidade>> findById(@PathVariable Integer id) {
        try {
            Especialidade especialidade = especialidadeService.findById(id);
            
            if (especialidade == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Especialidade não encontrada", "ID: " + id));
            }
            
            return ResponseEntity.ok(
                ApiResponse.success(especialidade, "Especialidade encontrada com sucesso")
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Erro interno do servidor", e.getMessage()));
        }
    }
    
    @PostMapping
    public ResponseEntity<ApiResponse<Especialidade>> create(@Valid @RequestBody Especialidade especialidade) {
        try {
            Especialidade novaEspecialidade = especialidadeService.save(especialidade);
            
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(novaEspecialidade, "Especialidade criada com sucesso"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("Erro ao criar especialidade", e.getMessage()));
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Especialidade>> update(
            @PathVariable Integer id, 
            @Valid @RequestBody Especialidade especialidade) {
        try {
            Especialidade especialidadeAtualizada = especialidadeService.update(id, especialidade);
            
            if (especialidadeAtualizada == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Especialidade não encontrada para atualização", "ID: " + id));
            }
            
            return ResponseEntity.ok(
                ApiResponse.success(especialidadeAtualizada, "Especialidade atualizada com sucesso")
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
    public ResponseEntity<ApiResponse<List<Especialidade>>> findByNome(@RequestParam String nome) {
        try {
            if (nome == null || nome.trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Nome é obrigatório", "Parâmetro 'nome' não pode ser vazio"));
            }
            
            List<Especialidade> especialidades = especialidadeService.findByNome(nome);
            
            if (especialidades.isEmpty()) {
                return ResponseEntity.ok(
                    ApiResponse.success(especialidades, "Nenhuma especialidade encontrada com o nome: " + nome)
                );
            }
            
            return ResponseEntity.ok(
                ApiResponse.success(especialidades, "Especialidades encontradas por nome")
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Erro interno do servidor", e.getMessage()));
        }
    }
    
    @GetMapping("/com-pacientes")
    public ResponseEntity<ApiResponse<List<Especialidade>>> findEspecialidadesComPacientes() {
        try {
            List<Especialidade> especialidades = especialidadeService.findEspecialidadesComPacientes();
            
            if (especialidades.isEmpty()) {
                return ResponseEntity.ok(
                    ApiResponse.success(especialidades, "Nenhuma especialidade com pacientes encontrada")
                );
            }
            
            return ResponseEntity.ok(
                ApiResponse.success(especialidades, "Especialidades com pacientes encontradas")
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Erro interno do servidor", e.getMessage()));
        }
    }
}
