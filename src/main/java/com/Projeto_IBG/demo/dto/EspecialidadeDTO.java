package com.Projeto_IBG.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EspecialidadeDTO {
    
    @JsonProperty("id")
    private Integer id;
    
    @JsonProperty("nome")
    private String nome;
    
    // Construtores
    public EspecialidadeDTO() {
    }
    
    public EspecialidadeDTO(Integer id, String nome) {
        this.id = id;
        this.nome = nome;
    }
    
    // getters e setters
    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getNome() {
        return this.nome;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }
}