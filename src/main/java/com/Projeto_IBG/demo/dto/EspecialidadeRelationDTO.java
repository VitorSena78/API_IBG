package com.Projeto_IBG.demo.dto;

public class EspecialidadeRelationDTO {
    private String pacienteLocalId;
    private Integer pacienteServerId;
    private Integer especialidadeId;
    private String localRelationId;
    private Integer serverRelationId;
    private String action; // "CREATE", "DELETE"
    private Long lastSyncTimestamp;


    public EspecialidadeRelationDTO() {
    }

    // getters e setters

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

    public Integer getEspecialidadeId() {
        return this.especialidadeId;
    }

    public void setEspecialidadeId(Integer especialidadeId) {
        this.especialidadeId = especialidadeId;
    }

    public String getLocalRelationId() {
        return this.localRelationId;
    }

    public void setLocalRelationId(String localRelationId) {
        this.localRelationId = localRelationId;
    }

    public Integer getServerRelationId() {
        return this.serverRelationId;
    }

    public void setServerRelationId(Integer serverRelationId) {
        this.serverRelationId = serverRelationId;
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

}
