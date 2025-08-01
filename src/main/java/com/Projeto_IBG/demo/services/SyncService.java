package com.Projeto_IBG.demo.services;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.ZoneId;

import com.Projeto_IBG.demo.dto.ConflictResolutionDTO;
import com.Projeto_IBG.demo.dto.EspecialidadeDTO;
import com.Projeto_IBG.demo.dto.PacienteEspecialidadeDTO;
import com.Projeto_IBG.demo.dto.PacienteDTO;
import com.Projeto_IBG.demo.dto.sync.ConflictDTO;
import com.Projeto_IBG.demo.dto.sync.SyncRequestDTO;
import com.Projeto_IBG.demo.dto.sync.SyncResponseDTO;
import com.Projeto_IBG.demo.dto.sync.SyncStatus;
import com.Projeto_IBG.demo.model.Paciente;
import com.Projeto_IBG.demo.model.PacienteEspecialidade;
import com.Projeto_IBG.demo.repositories.EspecialidadeRepository;
import com.Projeto_IBG.demo.repositories.PacienteEspecialidadeRepository;
import com.Projeto_IBG.demo.repositories.PacienteRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class SyncService {
    
    @Autowired
    private PacienteRepository pacienteRepository;
    
    @Autowired
    private EspecialidadeRepository especialidadeRepository;
    
    @Autowired
    private PacienteEspecialidadeRepository pacienteEspecialidadeRepository;
    
    public SyncResponseDTO syncPacientes(SyncRequestDTO request) {
        SyncResponseDTO response = new SyncResponseDTO();
        
        // 1. Processar dados enviados pelo app
        List<ConflictDTO> conflicts = new ArrayList<>();
        
        // 1.1 Processar pacientes
        for (PacienteDTO pacienteDTO : request.getPacientes()) {
            if (pacienteDTO.getServerId() == null) {
                // Novo paciente - criar no servidor
                createPacienteFromApp(pacienteDTO, request.getDeviceId());
            } else {
                // Paciente existente - verificar conflitos
                ConflictDTO conflict = checkAndUpdatePaciente(pacienteDTO, request.getDeviceId());
                if (conflict != null) {
                    conflicts.add(conflict);
                }
            }
        }
        
        // 1.2 Processar relações de especialidades
        for (PacienteEspecialidadeDTO relationDTO : request.getEspecialidadeRelations()) {
            ConflictDTO conflict = processEspecialidadeRelation(relationDTO, request.getDeviceId());
            if (conflict != null) {
                conflicts.add(conflict);
            }
        }
        
        // 2. Buscar dados do servidor para o app
        List<PacienteDTO> pacientesServidor = getPacientesForDevice(
            request.getDeviceId(), 
            request.getLastSyncTimestamp()
        );
        
        // 3. Preparar resposta
        response.setPacientesServidor(pacientesServidor);
        response.setEspecialidades(getAllEspecialidades());
        response.setConflitos(conflicts);
        response.setSyncTimestamp(System.currentTimeMillis());
        
        return response;
    }
    
    private void createPacienteFromApp(PacienteDTO pacienteDTO, String deviceId) {
        Paciente paciente = new Paciente();
        paciente.setNome(pacienteDTO.getNome());
        paciente.setDataNascimento(pacienteDTO.getDataNascimento());
        paciente.setIdade(pacienteDTO.getIdade());
        paciente.setNomeDaMae(pacienteDTO.getNomeDaMae());
        paciente.setCpf(pacienteDTO.getCpf());
        paciente.setSus(pacienteDTO.getSus());
        paciente.setTelefone(pacienteDTO.getTelefone());
        paciente.setEndereço(pacienteDTO.getEndereco());
        paciente.setDeviceId(deviceId);
        paciente.setLocalId(pacienteDTO.getLocalId());
        paciente.setSyncStatus(SyncStatus.SYNCED);
        
        paciente = pacienteRepository.save(paciente);
    }
    
    private ConflictDTO checkAndUpdatePaciente(PacienteDTO pacienteDTO, String deviceId) {
        Optional<Paciente> existingOpt = pacienteRepository.findById(pacienteDTO.getServerId());
        
        if (existingOpt.isPresent()) {
            Paciente existing = existingOpt.get();
            
            // Verificar se houve modificações no servidor após o último sync do app
            if (existing.getUpdatedAt().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() > pacienteDTO.getLastSyncTimestamp()) {
                // Conflito detectado
                return new ConflictDTO(
                    pacienteDTO.getServerId(),
                    "PACIENTE",
                    existing,
                    pacienteDTO,
                    "Dados modificados no servidor e no app"
                );
            } else {
                // Sem conflito - atualizar dados
                updatePacienteFromApp(existing, pacienteDTO);
                return null;
            }
        }
        
        return null;
    }
    
    private ConflictDTO processEspecialidadeRelation(PacienteEspecialidadeDTO relationDTO, String deviceId) {
        if ("CREATE".equals(relationDTO.getAction())) {
            // Criar nova relação
            return createEspecialidadeRelation(relationDTO, deviceId);
        } else if ("DELETE".equals(relationDTO.getAction())) {
            // Remover relação (apenas app desktop pode fazer isso)
            return deleteEspecialidadeRelation(relationDTO, deviceId);
        }
        return null;
    }
    
    private ConflictDTO createEspecialidadeRelation(PacienteEspecialidadeDTO relationDTO, String deviceId) {
        // Verificar se o paciente existe
        Integer pacienteId = relationDTO.getPacienteServerId();
        if (pacienteId == null) {
            // Buscar pelo localId se não tiver serverId
            Optional<Paciente> pacienteOpt = pacienteRepository.findByLocalIdAndDeviceId(
                relationDTO.getPacienteLocalId(), deviceId
            );
            if (pacienteOpt.isPresent()) {
                pacienteId = pacienteOpt.get().getId();
            } else {
                // Paciente não encontrado - pode ser um conflito
                return new ConflictDTO(
                    null,
                    "ESPECIALIDADE_RELATION",
                    null,
                    relationDTO,
                    "Paciente não encontrado para criar relação com especialidade"
                );
            }
        }
        
        // Verificar se a relação já existe
        Optional<PacienteEspecialidade> existingRelation = pacienteEspecialidadeRepository
            .findByPacienteIdAndEspecialidadeId(pacienteId, relationDTO.getEspecialidadeServerId());
        
        if (existingRelation.isPresent()) {
            // Relação já existe - não é necessário criar
            return null;
        }
        
        // Criar nova relação
        PacienteEspecialidade pe = new PacienteEspecialidade();
        pe.setPacienteId(pacienteId);
        pe.setEspecialidadeId(relationDTO.getEspecialidadeServerId());
        
        pacienteEspecialidadeRepository.save(pe);
        
        return null;
    }
    
    private ConflictDTO deleteEspecialidadeRelation(PacienteEspecialidadeDTO relationDTO, String deviceId) {
        // Apenas aplicativos desktop podem remover relações
        // Apps mobile não têm permissão para isso
        return new ConflictDTO(
            relationDTO.getServerPacienteEspecialidadeId(),
            "ESPECIALIDADE_RELATION",
            null,
            relationDTO,
            "App mobile não tem permissão para remover relações de especialidades"
        );
    }
    
    private void updatePacienteFromApp(Paciente paciente, PacienteDTO pacienteDTO) {
        // Atualizar apenas campos que o app pode modificar
        paciente.setNome(pacienteDTO.getNome());
        paciente.setDataNascimento(pacienteDTO.getDataNascimento());
        paciente.setIdade(pacienteDTO.getIdade());
        paciente.setNomeDaMae(pacienteDTO.getNomeDaMae());
        paciente.setCpf(pacienteDTO.getCpf());
        paciente.setSus(pacienteDTO.getSus());
        paciente.setTelefone(pacienteDTO.getTelefone());
        paciente.setEndereço(pacienteDTO.getEndereco());
        paciente.setSyncStatus(SyncStatus.SYNCED);
        paciente.setUpdatedAt(LocalDateTime.now());
        
        pacienteRepository.save(paciente);
    }
    
    private List<PacienteDTO> getPacientesForDevice(String deviceId, Long lastSyncTimestamp) {
        // Buscar pacientes modificados após o último sync
        List<Paciente> pacientes = pacienteRepository.findByUpdatedAtAfterAndDeviceIdNot(
            new Timestamp(lastSyncTimestamp != null ? lastSyncTimestamp : 0),
            deviceId
        );
        
        return pacientes.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    public List<EspecialidadeDTO> getAllEspecialidades() {
        return especialidadeRepository.findAll().stream()
            .map(esp -> new EspecialidadeDTO(esp.getId(), esp.getNome()))
            .collect(Collectors.toList());
    }
    
    public ConflictResolutionDTO resolveConflict(ConflictResolutionDTO resolution) {
        // Implementar lógica de resolução de conflitos
        // Por exemplo: usar dados do servidor, do app, ou dados mesclados
        
        if ("USE_SERVER".equals(resolution.getResolutionStrategy())) {
            // Manter dados do servidor
            return new ConflictResolutionDTO("SUCCESS", "Conflito resolvido - dados do servidor mantidos");
        } else if ("USE_APP".equals(resolution.getResolutionStrategy())) {
            // Usar dados do app
            Paciente paciente = pacienteRepository.findById(resolution.getEntityId()).orElse(null);
            if (paciente != null) {
                updatePacienteFromApp(paciente, resolution.getAppData());
                return new ConflictResolutionDTO("SUCCESS", "Conflito resolvido - dados do app aplicados");
            }
        }
        
        return new ConflictResolutionDTO("ERROR", "Falha ao resolver conflito");
    }
    
    private PacienteDTO convertToDTO(Paciente paciente) {
        PacienteDTO dto = new PacienteDTO();
        dto.setServerId(paciente.getId());
        dto.setNome(paciente.getNome());
        dto.setDataNascimento(paciente.getDataNascimento());
        dto.setIdade(paciente.getIdade());
        dto.setNomeDaMae(paciente.getNomeDaMae());
        dto.setCpf(paciente.getCpf());
        dto.setSus(paciente.getSus());
        dto.setTelefone(paciente.getTelefone());
        dto.setEndereco(paciente.getEndereço());
        dto.setLocalId(paciente.getLocalId());
        dto.setLastSyncTimestamp(paciente.getUpdatedAt().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        
        return dto;
    }
}
