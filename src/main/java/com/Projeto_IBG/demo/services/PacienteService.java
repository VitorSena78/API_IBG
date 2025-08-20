package com.Projeto_IBG.demo.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.Projeto_IBG.demo.exception.BusinessException;
import com.Projeto_IBG.demo.exception.ResourceNotFoundException;
import com.Projeto_IBG.demo.model.Paciente;
import com.Projeto_IBG.demo.repositories.PacienteRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PacienteService {
    
    @Autowired
    private PacienteRepository pacienteRepository;
    
    public List<Paciente> findAll() {
        return pacienteRepository.findAll();
    }
    
    public Page<Paciente> findAll(Pageable pageable) {
        return pacienteRepository.findAll(pageable);
    }
    
    public Paciente findById(Integer id) {
        return pacienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente não encontrado com ID: " + id));
    }
    
    public Paciente save(Paciente paciente) {
        // Validações de negócio
        if (paciente.getCpf() != null && pacienteRepository.existsByCpf(paciente.getCpf())) {
            throw new BusinessException("CPF já cadastrado: " + paciente.getCpf());
        }
        
        if (paciente.getSus() != null && pacienteRepository.existsBySus(paciente.getSus())) {
            throw new BusinessException("SUS já cadastrado: " + paciente.getSus());
        }
        
        return pacienteRepository.save(paciente);
    }
    
    public Paciente update(Integer id, Paciente pacienteAtualizado) {
        Paciente pacienteExistente = findById(id);
        
        // Validar CPF se foi alterado
        if (pacienteAtualizado.getCpf() != null && 
            !pacienteAtualizado.getCpf().equals(pacienteExistente.getCpf()) &&
            pacienteRepository.existsByCpf(pacienteAtualizado.getCpf())) {
            throw new BusinessException("CPF já cadastrado: " + pacienteAtualizado.getCpf());
        }
        
        // Validar SUS se foi alterado
        if (pacienteAtualizado.getSus() != null && 
            !pacienteAtualizado.getSus().equals(pacienteExistente.getSus()) &&
            pacienteRepository.existsBySus(pacienteAtualizado.getSus())) {
            throw new BusinessException("SUS já cadastrado: " + pacienteAtualizado.getSus());
        }
        
        // Atualizar campos
        pacienteExistente.setNome(pacienteAtualizado.getNome());
        pacienteExistente.setDataNascimento(pacienteAtualizado.getDataNascimento());
        pacienteExistente.setIdade(pacienteAtualizado.getIdade());
        pacienteExistente.setNomeDaMae(pacienteAtualizado.getNomeDaMae());
        pacienteExistente.setCpf(pacienteAtualizado.getCpf());
        pacienteExistente.setSus(pacienteAtualizado.getSus());
        pacienteExistente.setTelefone(pacienteAtualizado.getTelefone());
        pacienteExistente.setEndereço(pacienteAtualizado.getEndereço());
        pacienteExistente.setPaXMmhg(pacienteAtualizado.getPaXMmhg());
        pacienteExistente.setFcBpm(pacienteAtualizado.getFcBpm());
        pacienteExistente.setFrIbpm(pacienteAtualizado.getFrIbpm());
        pacienteExistente.setTemperaturaC(pacienteAtualizado.getTemperaturaC());
        pacienteExistente.setHgtMgld(pacienteAtualizado.getHgtMgld());
        pacienteExistente.setSpo2(pacienteAtualizado.getSpo2());
        pacienteExistente.setPeso(pacienteAtualizado.getPeso());
        pacienteExistente.setAltura(pacienteAtualizado.getAltura());
        
        return pacienteRepository.save(pacienteExistente);
    }
    
    public void delete(Integer id) {
        if (!pacienteRepository.existsById(id)) {
            throw new ResourceNotFoundException("Paciente não encontrado com ID: " + id);
        }
        pacienteRepository.deleteById(id);
    }
    
    public List<Paciente> findByNome(String nome) {
        return pacienteRepository.findByNomeContainingIgnoreCase(nome);
    }
    
    public Page<Paciente> findByNome(String nome, Pageable pageable) {
        return pacienteRepository.findByNomeContainingIgnoreCase(nome, pageable);
    }
    
    public Optional<Paciente> findByCpf(String cpf) {
        return pacienteRepository.findByCpf(cpf);
    }
    
    public Optional<Paciente> findBySus(String sus) {
        return pacienteRepository.findBySus(sus);
    }
    
    public List<Paciente> findByEspecialidade(Integer especialidadeId) {
        return pacienteRepository.findByEspecialidadeId(especialidadeId);
    }
    
    public List<Paciente> findByDataAtendimento(LocalDate dataAtendimento) {
        return pacienteRepository.findByDataAtendimento(dataAtendimento);
    }
    
    public List<Paciente> findByFaixaEtaria(Integer idadeMin, Integer idadeMax) {
        return pacienteRepository.findByIdadeBetween(idadeMin, idadeMax);
    }

    public List<Paciente> findUpdatedSince(LocalDateTime since) {
        return pacienteRepository.findUpdatedSince(since);
    }

    public List<Paciente> saveBatch(List<Paciente> pacientes) {
        try {
            // Validar e processar cada paciente
            for (Paciente paciente : pacientes) {
                // Definir timestamps
                paciente.setCreatedAt(LocalDateTime.now());
                paciente.setUpdatedAt(LocalDateTime.now());
            }
            
            return pacienteRepository.saveAll(pacientes);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao salvar pacientes em lote: " + e.getMessage(), e);
        }
    }

    public List<Paciente> updateBatch(List<Paciente> pacientes) {
        try {
            List<Paciente> pacientesParaAtualizar = new ArrayList<>();
            
            for (Paciente paciente : pacientes) {
                // Verificar se existe
                if (paciente.getId() != null) {
                    Optional<Paciente> existente = pacienteRepository.findById(paciente.getId());
                    if (existente.isPresent()) {
                        paciente.setUpdatedAt(LocalDateTime.now());
                        pacientesParaAtualizar.add(paciente);
                    }
                }
            }
            
            return pacienteRepository.saveAll(pacientesParaAtualizar);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao atualizar pacientes em lote: " + e.getMessage(), e);
        }
    }

    public long count() {
        return pacienteRepository.count();
    }
    
}

