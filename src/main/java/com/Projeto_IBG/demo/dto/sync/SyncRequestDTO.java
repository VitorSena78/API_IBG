package com.Projeto_IBG.demo.dto.sync;

import java.util.ArrayList;
import java.util.List;

import com.Projeto_IBG.demo.dto.EspecialidadeDTO;
import com.Projeto_IBG.demo.dto.PacienteEspecialidadeDTO;
import com.Projeto_IBG.demo.dto.PacienteDTO;

public class SyncRequestDTO {
    private String deviceId;
    private Long lastSyncTimestamp;
    private List<PacienteDTO> pacientes = new ArrayList<>();
    private List<PacienteEspecialidadeDTO> especialidadeRelations = new ArrayList<>();
    
    // Construtores

    public SyncRequestDTO() {
    }


    public SyncRequestDTO(String deviceId, Long lastSyncTimestamp, List<PacienteDTO> pacientes, List<PacienteEspecialidadeDTO> especialidadeRelations) {
        this.deviceId = deviceId;
        this.lastSyncTimestamp = lastSyncTimestamp;
        this.pacientes = pacientes;
        this.especialidadeRelations = especialidadeRelations;
    }
    

    // getters e setters

    public String getDeviceId() {
        return this.deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public Long getLastSyncTimestamp() {
        return this.lastSyncTimestamp;
    }

    public void setLastSyncTimestamp(Long lastSyncTimestamp) {
        this.lastSyncTimestamp = lastSyncTimestamp;
    }

    public List<PacienteDTO> getPacientes() {
        return this.pacientes;
    }

    public void setPacientes(List<PacienteDTO> pacientes) {
        this.pacientes = pacientes;
    }

    public List<PacienteEspecialidadeDTO> getEspecialidadeRelations() {
        return this.especialidadeRelations;
    }

    public void setEspecialidadeRelations(List<PacienteEspecialidadeDTO> especialidadeRelations) {
        this.especialidadeRelations = especialidadeRelations;
    }
    

}
