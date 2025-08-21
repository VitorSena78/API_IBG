package com.Projeto_IBG.demo.dto;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

public class EspecialidadeDTO {
    
    @JsonProperty("id")
    private Integer id;
    
    @JsonProperty("nome")
    private String nome;
    
    @JsonProperty("fichas") // Campo que estava ausente
    private Integer fichas;
    
    @JsonProperty("atendimentos_restantes_hoje") // Campo que estava ausente
    private Integer atendimentosRestantesHoje;
    
    @JsonProperty("atendimentos_totais_hoje") // Campo que estava ausente
    private Integer atendimentosTotaisHoje;
    
    @JsonProperty("created_at") // Campo que estava ausente
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    
    @JsonProperty("updated_at") // Campo que estava ausente
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    // Construtores
    public EspecialidadeDTO() {}

    public EspecialidadeDTO(Integer id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public EspecialidadeDTO(Integer id, String nome, Integer fichas, Integer atendimentosRestantesHoje, 
                           Integer atendimentosTotaisHoje, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.nome = nome;
        this.fichas = fichas;
        this.atendimentosRestantesHoje = atendimentosRestantesHoje;
        this.atendimentosTotaisHoje = atendimentosTotaisHoje;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters e Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getFichas() {
        return fichas;
    }

    public void setFichas(Integer fichas) {
        this.fichas = fichas;
    }

    public Integer getAtendimentosRestantesHoje() {
        return atendimentosRestantesHoje;
    }

    public void setAtendimentosRestantesHoje(Integer atendimentosRestantesHoje) {
        this.atendimentosRestantesHoje = atendimentosRestantesHoje;
    }

    public Integer getAtendimentosTotaisHoje() {
        return atendimentosTotaisHoje;
    }

    public void setAtendimentosTotaisHoje(Integer atendimentosTotaisHoje) {
        this.atendimentosTotaisHoje = atendimentosTotaisHoje;
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
}