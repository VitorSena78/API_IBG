package com.Projeto_IBG.demo.controllers;


import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.Projeto_IBG.demo.model.Paciente;
import com.Projeto_IBG.demo.services.PacienteService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/pacientes")
@CrossOrigin(origins = "*")
public class PacienteController {
    
    @Autowired
    private PacienteService pacienteService;
    
    @GetMapping
    public ResponseEntity<Page<Paciente>> findAll(Pageable pageable) {
        Page<Paciente> pacientes = pacienteService.findAll(pageable);
        return ResponseEntity.ok(pacientes);
    }
    
    @GetMapping("/todos")
    public ResponseEntity<List<Paciente>> findAll() {
        List<Paciente> pacientes = pacienteService.findAll();
        return ResponseEntity.ok(pacientes);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Paciente> findById(@PathVariable Integer id) {
        Paciente paciente = pacienteService.findById(id);
        return ResponseEntity.ok(paciente);
    }
    
    @PostMapping
    public ResponseEntity<Paciente> create(@Valid @RequestBody Paciente paciente) {
        Paciente novoPaciente = pacienteService.save(paciente);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoPaciente);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Paciente> update(@PathVariable Integer id, @Valid @RequestBody Paciente paciente) {
        Paciente pacienteAtualizado = pacienteService.update(id, paciente);
        return ResponseEntity.ok(pacienteAtualizado);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        pacienteService.delete(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/buscar/nome")
    public ResponseEntity<List<Paciente>> findByNome(@RequestParam String nome) {
        List<Paciente> pacientes = pacienteService.findByNome(nome);
        return ResponseEntity.ok(pacientes);
    }
    
    @GetMapping("/buscar/nome/paginado")
    public ResponseEntity<Page<Paciente>> findByNome(@RequestParam String nome, Pageable pageable) {
        Page<Paciente> pacientes = pacienteService.findByNome(nome, pageable);
        return ResponseEntity.ok(pacientes);
    }
    
    @GetMapping("/buscar/cpf/{cpf}")
    public ResponseEntity<Paciente> findByCpf(@PathVariable String cpf) {
        Optional<Paciente> paciente = pacienteService.findByCpf(cpf);
        return paciente.map(ResponseEntity::ok)
                      .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/buscar/sus/{sus}")
    public ResponseEntity<Paciente> findBySus(@PathVariable String sus) {
        Optional<Paciente> paciente = pacienteService.findBySus(sus);
        return paciente.map(ResponseEntity::ok)
                      .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/buscar/especialidade/{especialidadeId}")
    public ResponseEntity<List<Paciente>> findByEspecialidade(@PathVariable Integer especialidadeId) {
        List<Paciente> pacientes = pacienteService.findByEspecialidade(especialidadeId);
        return ResponseEntity.ok(pacientes);
    }
    
    @GetMapping("/buscar/data-atendimento")
    public ResponseEntity<List<Paciente>> findByDataAtendimento(@RequestParam LocalDate dataAtendimento) {
        List<Paciente> pacientes = pacienteService.findByDataAtendimento(dataAtendimento);
        return ResponseEntity.ok(pacientes);
    }
    
    @GetMapping("/buscar/faixa-etaria")
    public ResponseEntity<List<Paciente>> findByFaixaEtaria(@RequestParam Integer idadeMin, @RequestParam Integer idadeMax) {
        List<Paciente> pacientes = pacienteService.findByFaixaEtaria(idadeMin, idadeMax);
        return ResponseEntity.ok(pacientes);
    }
}
