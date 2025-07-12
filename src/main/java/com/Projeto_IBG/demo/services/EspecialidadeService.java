package com.Projeto_IBG.demo.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.Projeto_IBG.demo.exception.BusinessException;
import com.Projeto_IBG.demo.exception.ResourceNotFoundException;
import com.Projeto_IBG.demo.model.Especialidade;
import com.Projeto_IBG.demo.repositories.EspecialidadeRepository;

import java.util.List;

@Service
@Transactional
public class EspecialidadeService {
    
    @Autowired
    private EspecialidadeRepository especialidadeRepository;
    
    public List<Especialidade> findAll() {
        return especialidadeRepository.findAll();
    }
    
    public Especialidade findById(Integer id) {
        return especialidadeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Especialidade não encontrada com ID: " + id));
    }
    
    public Especialidade save(Especialidade especialidade) {
        if (especialidadeRepository.existsByNome(especialidade.getNome())) {
            throw new BusinessException("Especialidade já cadastrada: " + especialidade.getNome());
        }
        return especialidadeRepository.save(especialidade);
    }
    
    public Especialidade update(Integer id, Especialidade especialidadeAtualizada) {
        Especialidade especialidadeExistente = findById(id);
        
        if (!especialidadeAtualizada.getNome().equals(especialidadeExistente.getNome()) &&
            especialidadeRepository.existsByNome(especialidadeAtualizada.getNome())) {
            throw new BusinessException("Especialidade já cadastrada: " + especialidadeAtualizada.getNome());
        }
        
        especialidadeExistente.setNome(especialidadeAtualizada.getNome());
        return especialidadeRepository.save(especialidadeExistente);
    }
    
    public void delete(Integer id) {
        Especialidade especialidade = findById(id);
        
        Long qtdPacientes = especialidadeRepository.countPacientesByEspecialidadeId(id);
        if (qtdPacientes > 0) {
            throw new BusinessException("Não é possível excluir especialidade com pacientes vinculados");
        }
        
        especialidadeRepository.deleteById(id);
    }
    
    public List<Especialidade> findByNome(String nome) {
        return especialidadeRepository.findByNomeContainingIgnoreCase(nome);
    }
    
    public List<Especialidade> findEspecialidadesComPacientes() {
        return especialidadeRepository.findEspecialidadesComPacientes();
    }
}
