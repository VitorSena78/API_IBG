package com.Projeto_IBG.demo.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.Projeto_IBG.demo.model.PacienteEspecialidade;
import com.Projeto_IBG.demo.services.PacienteEspecialidadeService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/atendimentos")
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
    public ResponseEntity<List<PacienteEspecialidade>> findByPaciente(@PathVariable Long pacienteId) {
        List<PacienteEspecialidade> atendimentos = pacienteEspecialidadeService.findByPaciente(pacienteId);
        return ResponseEntity.ok(atendimentos);
    }
    
    @GetMapping("/especialidade/{especialidadeId}")
    public ResponseEntity<List<PacienteEspecialidade>> findByEspecialidade(@PathVariable Long especialidadeId) {
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
}
