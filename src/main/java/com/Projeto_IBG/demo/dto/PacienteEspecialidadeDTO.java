package com.Projeto_IBG.demo.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PacienteEspecialidadeDTO {
    
    @JsonProperty("paciente_local_id")
    private String pacienteLocalId;
    
    @JsonProperty("paciente_server_id")
    private Integer pacienteServerId;
    
    @JsonProperty("especialidade_server_id")
    private Integer especialidadeServerId;
    
    @JsonProperty("especialidade_local_id")
    private String especialidadeLocalId;
    
    private String localPacienteEspecialidadeId;
    private Integer serverPacienteEspecialidadeId;
    
    @JsonProperty("data_atendimento")
    private LocalDate dataAtendimento;
    
    private Boolean isDeleted; // Para soft delete
    
    @JsonProperty("updated_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
    
    @JsonProperty("created_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    
    private String action; // "CREATE", "DELETE"
    
    @JsonProperty("last_sync_timestamp")
    private Long lastSyncTimestamp;

    // Construtores existentes (manter todos)
    public PacienteEspecialidadeDTO() {
    }
    
    public PacienteEspecialidadeDTO(String pacienteLocalId, Integer pacienteServerId, Integer especialidadeServerId,
            String especialidadeLocalId, String localPacienteEspecialidadeId, Integer serverPacienteEspecialidadeId,
            LocalDate dataAtendimento, Boolean isDeleted, LocalDateTime updatedAt, LocalDateTime createdAt,
            String action, Long lastSyncTimestamp) {
        this.pacienteLocalId = pacienteLocalId;
        this.pacienteServerId = pacienteServerId;
        this.especialidadeServerId = especialidadeServerId;
        this.especialidadeLocalId = especialidadeLocalId;
        this.localPacienteEspecialidadeId = localPacienteEspecialidadeId;
        this.serverPacienteEspecialidadeId = serverPacienteEspecialidadeId;
        this.dataAtendimento = dataAtendimento;
        this.isDeleted = isDeleted;
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
        this.action = action;
        this.lastSyncTimestamp = lastSyncTimestamp;
    }

    public PacienteEspecialidadeDTO(Integer pacienteServerId, Integer especialidadeServerId, LocalDate dataAtendimento,
            Boolean isDeleted, LocalDateTime updatedAt, LocalDateTime createdAt) {
        this.pacienteServerId = pacienteServerId;
        this.especialidadeServerId = especialidadeServerId;
        this.dataAtendimento = dataAtendimento;
        this.isDeleted = isDeleted;
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
    }

    // Todos os getters e setters existentes (manter todos)
    public String getPacienteLocalId() {
        return this.pacienteLocalId;
    }

    public void setPacienteLocalId(String pacienteLocalId) {
        this.pacienteLocalId = pacienteLocalId;
    }

    public Integer getPacienteServerId() {
        return this.pacienteServerId;
    }

    public void setPacienteServerId(Integer pacienteServerId) {
        this.pacienteServerId = pacienteServerId;
    }

    public Integer getEspecialidadeServerId() {
        return this.especialidadeServerId;
    }

    public void setEspecialidadeServerId(Integer especialidadeId) {
        this.especialidadeServerId = especialidadeId;
    }

    public String getLocalPacienteEspecialidadeId() {
        return this.localPacienteEspecialidadeId;
    }

    public void setLocalPacienteEspecialidadeId(String localRelationId) {
        this.localPacienteEspecialidadeId = localRelationId;
    }

    public Integer getServerPacienteEspecialidadeId() {
        return this.serverPacienteEspecialidadeId;
    }

    public void setServerPacienteEspecialidadeId(Integer serverRelationId) {
        this.serverPacienteEspecialidadeId = serverRelationId;
    }

    public String getAction() {
        return this.action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Long getLastSyncTimestamp() {
        return this.lastSyncTimestamp;
    }

    public void setLastSyncTimestamp(Long lastSyncTimestamp) {
        this.lastSyncTimestamp = lastSyncTimestamp;
    }

    public String getEspecialidadeLocalId() {
        return especialidadeLocalId;
    }

    public void setEspecialidadeLocalId(String especialidadeLocalId) {
        this.especialidadeLocalId = especialidadeLocalId;
    }

    public LocalDate getDataAtendimento() {
        return dataAtendimento;
    }

    public void setDataAtendimento(LocalDate dataAtendimento) {
        this.dataAtendimento = dataAtendimento;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}