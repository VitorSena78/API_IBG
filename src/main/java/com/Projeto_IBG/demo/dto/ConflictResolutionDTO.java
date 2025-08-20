package com.Projeto_IBG.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ConflictResolutionDTO {
    
    @JsonProperty("entity_id")
    private Integer entityId;
    
    @JsonProperty("entity_type") // "PACIENTE", "ESPECIALIDADE_RELATION"
    private String entityType;
    
    @JsonProperty("resolution_strategy") // "USE_SERVER", "USE_APP", "MERGE"
    private String resolutionStrategy;
    
    @JsonProperty("app_data")
    private PacienteDTO appData;
    
    @JsonProperty("status")
    private String status;
    
    @JsonProperty("message")
    private String message;
    
    public ConflictResolutionDTO() {
    }
    
    public ConflictResolutionDTO(String status, String message) {
        this.status = status;
        this.message = message;
    }
    
    public ConflictResolutionDTO(Integer entityId, String entityType, String resolutionStrategy, 
                                PacienteDTO appData, String status, String message) {
        this.entityId = entityId;
        this.entityType = entityType;
        this.resolutionStrategy = resolutionStrategy;
        this.appData = appData;
        this.status = status;
        this.message = message;
    }
    
    // getters e setters
    public Integer getEntityId() {
        return this.entityId;
    }
    
    public void setEntityId(Integer entityId) {
        this.entityId = entityId;
    }
    
    public String getEntityType() {
        return this.entityType;
    }
    
    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }
    
    public String getResolutionStrategy() {
        return this.resolutionStrategy;
    }
    
    public void setResolutionStrategy(String resolutionStrategy) {
        this.resolutionStrategy = resolutionStrategy;
    }
    
    public PacienteDTO getAppData() {
        return this.appData;
    }
    
    public void setAppData(PacienteDTO appData) {
        this.appData = appData;
    }
    
    public String getStatus() {
        return this.status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getMessage() {
        return this.message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
}