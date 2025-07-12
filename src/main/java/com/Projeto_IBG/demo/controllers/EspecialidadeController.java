package com.Projeto_IBG.demo.controllers;


import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<List<Especialidade>> findAll() {
        List<Especialidade> especialidades = especialidadeService.findAll();
        return ResponseEntity.ok(especialidades);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Especialidade> findById(@PathVariable Integer id) {
        Especialidade especialidade = especialidadeService.findById(id);
        return ResponseEntity.ok(especialidade);
    }
    
    @PostMapping
    public ResponseEntity<Especialidade> create(@Valid @RequestBody Especialidade especialidade) {
        Especialidade novaEspecialidade = especialidadeService.save(especialidade);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaEspecialidade);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Especialidade> update(@PathVariable Integer id, @Valid @RequestBody Especialidade especialidade) {
        Especialidade especialidadeAtualizada = especialidadeService.update(id, especialidade);
        return ResponseEntity.ok(especialidadeAtualizada);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        especialidadeService.delete(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/buscar/nome")
    public ResponseEntity<List<Especialidade>> findByNome(@RequestParam String nome) {
        List<Especialidade> especialidades = especialidadeService.findByNome(nome);
        return ResponseEntity.ok(especialidades);
    }
    
    @GetMapping("/com-pacientes")
    public ResponseEntity<List<Especialidade>> findEspecialidadesComPacientes() {
        List<Especialidade> especialidades = especialidadeService.findEspecialidadesComPacientes();
        return ResponseEntity.ok(especialidades);
    }
}
