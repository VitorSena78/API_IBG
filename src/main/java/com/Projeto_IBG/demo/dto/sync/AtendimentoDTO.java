package com.Projeto_IBG.demo.dto.sync;

import java.time.LocalDate;

public class AtendimentoDTO {
    private Integer pacienteId;
    private String pacienteLocalId;
    private Integer especialidadeId;
    private LocalDate dataAtendimento;
    private String syncStatus;
    private Long createdAt;
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
