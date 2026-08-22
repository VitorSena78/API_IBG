package com.Projeto_IBG.demo.services;


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
    
    private final PacienteRepository pacienteRepository;

    public PacienteService(PacienteRepository pacienteRepository) {
        this.pacienteRepository = pacienteRepository;
    }
    
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
        if (paciente.getCpf() != null && !paciente.getCpf().isBlank() && pacienteRepository.existsByCpf(paciente.getCpf())) {
            throw new BusinessException("CPF já cadastrado: " + paciente.getCpf());
        }
        
        if (paciente.getSus() != null && !paciente.getSus().isBlank() && pacienteRepository.existsBySus(paciente.getSus())) {
            throw new BusinessException("SUS já cadastrado: " + paciente.getSus());
        }
        
        calcularIdade(paciente);
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
        
        // Atualizar apenas os campos informados (evita apagar dados em payloads parciais)
        if (pacienteAtualizado.getNome() != null) {
            pacienteExistente.setNome(pacienteAtualizado.getNome());
        }
        if (pacienteAtualizado.getDataNascimento() != null) {
            pacienteExistente.setDataNascimento(pacienteAtualizado.getDataNascimento());
        }
        if (pacienteAtualizado.getNomeDaMae() != null) {
            pacienteExistente.setNomeDaMae(pacienteAtualizado.getNomeDaMae());
        }
        if (pacienteAtualizado.getCpf() != null) {
            pacienteExistente.setCpf(pacienteAtualizado.getCpf());
        }
        if (pacienteAtualizado.getSus() != null) {
            pacienteExistente.setSus(pacienteAtualizado.getSus());
        }
        if (pacienteAtualizado.getTelefone() != null) {
            pacienteExistente.setTelefone(pacienteAtualizado.getTelefone());
        }
        if (pacienteAtualizado.getEndereco() != null) {
            pacienteExistente.setEndereco(pacienteAtualizado.getEndereco());
        }
        
        calcularIdade(pacienteExistente);
        return pacienteRepository.save(pacienteExistente);
    }

    private void calcularIdade(Paciente paciente) {
        if (paciente.getDataNascimento() != null) {
            paciente.setIdade(java.time.Period.between(paciente.getDataNascimento(), LocalDate.now()).getYears());
        }
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

