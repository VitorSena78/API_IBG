package com.Projeto_IBG.demo.dto.sync;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.Projeto_IBG.demo.dto.EspecialidadeDTO;
import com.Projeto_IBG.demo.dto.PacienteDTO;

public class SyncResponseDTO {
    
    @JsonProperty("pacientes_servidor")
    private List<PacienteDTO> pacientesServidor = new ArrayList<>();
    
    @JsonProperty("especialidades")
    private List<EspecialidadeDTO> especialidades = new ArrayList<>();
    
    @JsonProperty("conflitos")
    private List<ConflictDTO> conflitos = new ArrayList<>();
    
    @JsonProperty("sync_timestamp")
    private Long syncTimestamp;
    
    @JsonProperty("message")
    private String message;
    
    @JsonProperty("status")
    private String status = "SUCCESS";
    
    // Construtores
    public SyncResponseDTO() {
    }
    
    public SyncResponseDTO(String message) {
        this.message = message;
        this.status = "ERROR";
    }
    
    public SyncResponseDTO(List<PacienteDTO> pacientesServidor, List<EspecialidadeDTO> especialidades, 
                          List<ConflictDTO> conflitos, Long syncTimestamp, String message, String status) {
        this.pacientesServidor = pacientesServidor;
        this.especialidades = especialidades;
        this.conflitos = conflitos;
        this.syncTimestamp = syncTimestamp;
        this.message = message;
        this.status = status;
    }
    
    // getters e setters
    public List<PacienteDTO> getPacientesServidor() {
        return this.pacientesServidor;
    }
    
    public void setPacientesServidor(List<PacienteDTO> pacientesServidor) {
        this.pacientesServidor = pacientesServidor;
    }
    
    public List<EspecialidadeDTO> getEspecialidades() {
        return this.especialidades;
    }
    
    public void setEspecialidades(List<EspecialidadeDTO> especialidades) {
        this.especialidades = especialidades;
    }
    
    public List<ConflictDTO> getConflitos() {
        return this.conflitos;
    }
    
    public void setConflitos(List<ConflictDTO> conflitos) {
        this.conflitos = conflitos;
    }
    
    public Long getSyncTimestamp() {
        return this.syncTimestamp;
    }
    
    public void setSyncTimestamp(Long syncTimestamp) {
        this.syncTimestamp = syncTimestamp;
    }
    
    public String getMessage() {
        return this.message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public String getStatus() {
        return this.status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
}