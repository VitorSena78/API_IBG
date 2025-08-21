package com.Projeto_IBG.demo.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PacienteEspecialidadeDTO {
    
    // IDs dos relacionamentos (usando a chave composta)
    @JsonProperty("paciente_id")
    private Integer pacienteId;
    
    @JsonProperty("especialidade_id")
    private Integer especialidadeId;
    
    // IDs locais para sincronização
    @JsonProperty("paciente_local_id")
    private String pacienteLocalId;
    
    @JsonProperty("especialidade_local_id")
    private String especialidadeLocalId;
    
    // IDs do servidor para sincronização
    @JsonProperty("paciente_server_id")
    private Integer pacienteServerId;
    
    @JsonProperty("especialidade_server_id")
    private Integer especialidadeServerId;
    
    // ID próprio do relacionamento (se necessário)
    @JsonProperty("local_paciente_especialidade_id")
    private String localPacienteEspecialidadeId;
    
    @JsonProperty("server_paciente_especialidade_id")
    private Integer serverPacienteEspecialidadeId;
    
    // Campos do modelo
    @JsonProperty("data_atendimento")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataAtendimento;
    
    @JsonProperty("created_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    
    @JsonProperty("updated_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
    
    // Campos para controle de sincronização
    @JsonProperty("is_deleted")
    private Boolean isDeleted = false;
    
    @JsonProperty("action")
    private String action; // "CREATE", "UPDATE", "DELETE"
    
    @JsonProperty("last_sync_timestamp")
    private Long lastSyncTimestamp;

    // Construtores
    public PacienteEspecialidadeDTO() {}

    public PacienteEspecialidadeDTO(Integer pacienteId, Integer especialidadeId, LocalDate dataAtendimento) {
        this.pacienteId = pacienteId;
        this.especialidadeId = especialidadeId;
        this.dataAtendimento = dataAtendimento;
    }

    public PacienteEspecialidadeDTO(Integer pacienteId, Integer especialidadeId, LocalDate dataAtendimento,
                                   LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.pacienteId = pacienteId;
        this.especialidadeId = especialidadeId;
        this.dataAtendimento = dataAtendimento;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public PacienteEspecialidadeDTO(Integer pacienteServerId, Integer especialidadeServerId, 
                               LocalDate dataAtendimento, Boolean isDeleted, 
                               LocalDateTime updatedAt, LocalDateTime createdAt) {
        this.pacienteServerId = pacienteServerId;
        this.especialidadeServerId = especialidadeServerId;
        this.dataAtendimento = dataAtendimento;
        this.isDeleted = isDeleted;
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
    }

    // Construtor completo para sincronização
    public PacienteEspecialidadeDTO(Integer pacienteId, Integer especialidadeId, String pacienteLocalId,
                                   String especialidadeLocalId, Integer pacienteServerId, Integer especialidadeServerId,
                                   String localPacienteEspecialidadeId, Integer serverPacienteEspecialidadeId,
                                   LocalDate dataAtendimento, LocalDateTime createdAt, LocalDateTime updatedAt,
                                   Boolean isDeleted, String action, Long lastSyncTimestamp) {
        this.pacienteId = pacienteId;
        this.especialidadeId = especialidadeId;
        this.pacienteLocalId = pacienteLocalId;
        this.especialidadeLocalId = especialidadeLocalId;
        this.pacienteServerId = pacienteServerId;
        this.especialidadeServerId = especialidadeServerId;
        this.localPacienteEspecialidadeId = localPacienteEspecialidadeId;
        this.serverPacienteEspecialidadeId = serverPacienteEspecialidadeId;
        this.dataAtendimento = dataAtendimento;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.isDeleted = isDeleted;
        this.action = action;
        this.lastSyncTimestamp = lastSyncTimestamp;
    }

    // Getters e Setters
    public Integer getPacienteId() {
        return pacienteId;
    }

    public void setPacienteId(Integer pacienteId) {
        this.pacienteId = pacienteId;
    }

    public Integer getEspecialidadeId() {
        return especialidadeId;
    }

    public void setEspecialidadeId(Integer especialidadeId) {
        this.especialidadeId = especialidadeId;
    }

    public String getPacienteLocalId() {
        return pacienteLocalId;
    }

    public void setPacienteLocalId(String pacienteLocalId) {
        this.pacienteLocalId = pacienteLocalId;
    }

    public String getEspecialidadeLocalId() {
        return especialidadeLocalId;
    }

    public void setEspecialidadeLocalId(String especialidadeLocalId) {
        this.especialidadeLocalId = especialidadeLocalId;
    }

    public Integer getPacienteServerId() {
        return pacienteServerId;
    }

    public void setPacienteServerId(Integer pacienteServerId) {
        this.pacienteServerId = pacienteServerId;
    }

    public Integer getEspecialidadeServerId() {
        return especialidadeServerId;
    }

    public void setEspecialidadeServerId(Integer especialidadeServerId) {
        this.especialidadeServerId = especialidadeServerId;
    }

    public String getLocalPacienteEspecialidadeId() {
        return localPacienteEspecialidadeId;
    }

    public void setLocalPacienteEspecialidadeId(String localPacienteEspecialidadeId) {
        this.localPacienteEspecialidadeId = localPacienteEspecialidadeId;
    }

    public Integer getServerPacienteEspecialidadeId() {
        return serverPacienteEspecialidadeId;
    }

    public void setServerPacienteEspecialidadeId(Integer serverPacienteEspecialidadeId) {
        this.serverPacienteEspecialidadeId = serverPacienteEspecialidadeId;
    }

    public LocalDate getDataAtendimento() {
        return dataAtendimento;
    }

    public void setDataAtendimento(LocalDate dataAtendimento) {
        this.dataAtendimento = dataAtendimento;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Long getLastSyncTimestamp() {
        return lastSyncTimestamp;
    }

    public void setLastSyncTimestamp(Long lastSyncTimestamp) {
        this.lastSyncTimestamp = lastSyncTimestamp;
    }
}