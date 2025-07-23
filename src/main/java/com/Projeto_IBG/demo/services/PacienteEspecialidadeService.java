package com.Projeto_IBG.demo.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.Projeto_IBG.demo.exception.ResourceNotFoundException;
import com.Projeto_IBG.demo.model.PacienteEspecialidade;
import com.Projeto_IBG.demo.model.PacienteEspecialidadeId;
import com.Projeto_IBG.demo.repositories.PacienteEspecialidadeRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class PacienteEspecialidadeService {
    
    @Autowired
    private PacienteEspecialidadeRepository pacienteEspecialidadeRepository;
    
    @Autowired
    private PacienteService pacienteService;
    
    @Autowired
    private EspecialidadeService especialidadeService;
    
    public List<PacienteEspecialidade> findAll() {
        return pacienteEspecialidadeRepository.findAll();
    }
    
    public PacienteEspecialidade findById(PacienteEspecialidadeId id) {
        return pacienteEspecialidadeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Relacionamento não encontrado"));
    }
    
    public PacienteEspecialidade save(Integer pacienteId, Integer especialidadeId, LocalDate dataAtendimento) {
        // Validar se paciente e especialidade existem
        var paciente = pacienteService.findById(pacienteId);
        var especialidade = especialidadeService.findById(especialidadeId);
        
        PacienteEspecialidadeId id = new PacienteEspecialidadeId(pacienteId, especialidadeId);
        
        PacienteEspecialidade pacienteEspecialidade = new PacienteEspecialidade();
        pacienteEspecialidade.setId(id);
        pacienteEspecialidade.setPaciente(paciente);
        pacienteEspecialidade.setEspecialidade(especialidade);
        pacienteEspecialidade.setDataAtendimento(dataAtendimento != null ? dataAtendimento : LocalDate.now());
        
        return pacienteEspecialidadeRepository.save(pacienteEspecialidade);
    }
    
    public void delete(Integer pacienteId, Integer especialidadeId) {
        PacienteEspecialidadeId id = new PacienteEspecialidadeId(pacienteId, especialidadeId);
        
        if (!pacienteEspecialidadeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Relacionamento não encontrado");
        }
        
        pacienteEspecialidadeRepository.deleteById(id);
    }
    
    public List<PacienteEspecialidade> findByPaciente(Integer pacienteId) {
        return pacienteEspecialidadeRepository.findByPacienteId(pacienteId);
    }
    
    public List<PacienteEspecialidade> findByEspecialidade(Integer especialidadeId) {
        return pacienteEspecialidadeRepository.findByEspecialidadeId(especialidadeId);
    }
    
    public List<PacienteEspecialidade> findByDataAtendimento(LocalDate dataAtendimento) {
        return pacienteEspecialidadeRepository.findByDataAtendimento(dataAtendimento);
    }
    
    public List<Object[]> getRelatorioAtendimentosPorEspecialidade() {
        return pacienteEspecialidadeRepository.findAtendimentosPorEspecialidade();
    }
    
    public List<Object[]> getRelatorioAtendimentosPorMes() {
        return pacienteEspecialidadeRepository.findAtendimentosPorMes();
    }
}
