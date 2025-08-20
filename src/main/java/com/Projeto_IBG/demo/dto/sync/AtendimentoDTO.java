package com.Projeto_IBG.demo.dto.sync;

import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AtendimentoDTO {
    
    @JsonProperty("paciente_id")
    private Integer pacienteId;
    
    @JsonProperty("paciente_local_id")
    private String pacienteLocalId;
    
    @JsonProperty("especialidade_id")
    private Integer especialidadeId;
    
    @JsonProperty("data_atendimento")
    private LocalDate dataAtendimento;
    
    @JsonProperty("sync_status")
    private String syncStatus;
    
    @JsonProperty("created_at")
    private Long createdAt;
    
    @JsonProperty("updated_at")
    private Long updatedAt;
    
    // Construtores
    public AtendimentoDTO() {}
    
    // Getters e Setters
    public Integer getPacienteId() {
        return this.pacienteId;
    }
    
    public void setPacienteId(Integer pacienteId) {
        this.pacienteId = pacienteId;
    }
    
    public String getPacienteLocalId() {
        return this.pacienteLocalId;
    }
    
    public void setPacienteLocalId(String pacienteLocalId) {
        this.pacienteLocalId = pacienteLocalId;
    }
    
    public Integer getEspecialidadeId() {
        return this.especialidadeId;
    }
    
    public void setEspecialidadeId(Integer especialidadeId) {
        this.especialidadeId = especialidadeId;
    }
    
    public LocalDate getDataAtendimento() {
        return this.dataAtendimento;
    }
    
    public void setDataAtendimento(LocalDate dataAtendimento) {
        this.dataAtendimento = dataAtendimento;
    }
    
    public String getSyncStatus() {
        return this.syncStatus;
    }
    
    public void setSyncStatus(String syncStatus) {
        this.syncStatus = syncStatus;
    }
    
    public Long getCreatedAt() {
        return this.createdAt;
    }
    
    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }
    
    public Long getUpdatedAt() {
        return this.updatedAt;
    }
    
    public void setUpdatedAt(Long updatedAt) {
        this.updatedAt = updatedAt;
    }
}