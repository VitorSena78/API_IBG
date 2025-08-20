package com.Projeto_IBG.demo.dto.sync;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ConflictDTO {
    
    @JsonProperty("entity_id")
    private Integer entityId;
    
    @JsonProperty("entity_type") // "PACIENTE", "ESPECIALIDADE_RELATION"
    private String entityType;
    
    @JsonProperty("server_data")
    private Object serverData;
    
    @JsonProperty("app_data")
    private Object appData;
    
    @JsonProperty("description")
    private String description;
    
    @JsonProperty("conflict_type") // "UPDATE_CONFLICT", "DELETE_CONFLICT"
    private String conflictType;
    
    @JsonProperty("server_timestamp")
    private Long serverTimestamp;
    
    @JsonProperty("app_timestamp")
    private Long appTimestamp;
    
    // Construtores, getters e setters
    public ConflictDTO() {}
    
    public ConflictDTO(Integer entityId, String entityType, Object serverData, Object appData, String description) {
        this.entityId = entityId;
        this.entityType = entityType;
        this.serverData = serverData;
        this.appData = appData;
        this.description = description;
        this.conflictType = "UPDATE_CONFLICT";
    }
    
    public ConflictDTO(Integer entityId, String entityType, Object serverData, Object appData, 
                      String description, String conflictType, Long serverTimestamp, Long appTimestamp) {
        this.entityId = entityId;
        this.entityType = entityType;
        this.serverData = serverData;
        this.appData = appData;
        this.description = description;
        this.conflictType = conflictType;
        this.serverTimestamp = serverTimestamp;
        this.appTimestamp = appTimestamp;
    }
    
    // Getters e Setters
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
    
    public Object getServerData() {
        return this.serverData;
    }
    
    public void setServerData(Object serverData) {
        this.serverData = serverData;
    }
    
    public Object getAppData() {
        return this.appData;
    }
    
    public void setAppData(Object appData) {
        this.appData = appData;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getConflictType() {
        return this.conflictType;
    }
    
    public void setConflictType(String conflictType) {
        this.conflictType = conflictType;
    }
    
    public Long getServerTimestamp() {
        return this.serverTimestamp;
    }
    
    public void setServerTimestamp(Long serverTimestamp) {
        this.serverTimestamp = serverTimestamp;
    }
    
    public Long getAppTimestamp() {
        return this.appTimestamp;
    }
    
    public void setAppTimestamp(Long appTimestamp) {
        this.appTimestamp = appTimestamp;
    }
}