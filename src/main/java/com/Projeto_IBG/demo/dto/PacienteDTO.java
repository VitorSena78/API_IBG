package com.Projeto_IBG.demo.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


public class PacienteDTO {
    private Integer serverId; // ID do servidor
    private String localId; // ID temporário do app
    private String nome;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataNascimento;
    
    private Integer idade;
    private String nomeDaMae;
    private String cpf;
    private String sus;
    private String telefone;
    private String endereço;
    private List<Integer> especialidadeIds;
    private String paXMmhg;
    private Float fcBpm;
    private Float frIbpm;
    private Float temperaturaC;
    private Float hgtMgld;
    private Float spo2;
    private Float peso;
    private Float altura;
    private Float imc;
    private String syncStatus;
    private Long lastSyncTimestamp;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
   

    public PacienteDTO() {
    }

    // getters e setters
    public Integer getServerId() {
        return this.serverId;
    }

    public void setServerId(Integer serverId) {
        this.serverId = serverId;
    }

    public String getLocalId() {
        return this.localId;
    }

    public void setLocalId(String localId) {
        this.localId = localId;
    }

    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public LocalDate getDataNascimento() {
        return this.dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public Integer getIdade() {
        return this.idade;
    }

    public void setIdade(Integer idade) {
        this.idade = idade;
    }

    public String getNomeDaMae() {
        return this.nomeDaMae;
    }

    public void setNomeDaMae(String nomeDaMae) {
        this.nomeDaMae = nomeDaMae;
    }

    public String getCpf() {
        return this.cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getSus() {
        return this.sus;
    }

    public void setSus(String sus) {
        this.sus = sus;
    }

    public String getTelefone() {
        return this.telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEndereço() {
        return this.endereço;
    }

    public void setEndereço(String endereço) {
        this.endereço = endereço;
    }

    public List<Integer> getEspecialidadeIds() {
        return this.especialidadeIds;
    }

    public void setEspecialidadeIds(List<Integer> especialidadeIds) {
        this.especialidadeIds = especialidadeIds;
    }

    public String getPaXMmhg() {
        return this.paXMmhg;
    }

    public void setPaXMmhg(String paXMmhg) {
        this.paXMmhg = paXMmhg;
    }

    public Float getFcBpm() {
        return this.fcBpm;
    }

    public void setFcBpm(Float fcBpm) {
        this.fcBpm = fcBpm;
    }

    public Float getFrIbpm() {
        return this.frIbpm;
    }

    public void setFrIbpm(Float frIbpm) {
        this.frIbpm = frIbpm;
    }

    public Float getTemperaturaC() {
        return this.temperaturaC;
    }

    public void setTemperaturaC(Float temperaturaC) {
        this.temperaturaC = temperaturaC;
    }

    public Float getHgtMgld() {
        return this.hgtMgld;
    }

    public void setHgtMgld(Float hgtMgld) {
        this.hgtMgld = hgtMgld;
    }

    public Float getSpo2() {
        return this.spo2;
    }

    public void setSpo2(Float spo2) {
        this.spo2 = spo2;
    }

    public Float getPeso() {
        return this.peso;
    }

    public void setPeso(Float peso) {
        this.peso = peso;
    }

    public Float getAltura() {
        return this.altura;
    }

    public void setAltura(Float altura) {
        this.altura = altura;
    }

    public Float getImc() {
        return this.imc;
    }

    public void setImc(Float imc) {
        this.imc = imc;
    }

    public String getSyncStatus() {
        return this.syncStatus;
    }

    public void setSyncStatus(String syncStatus) {
        this.syncStatus = syncStatus;
    }

    public Long getLastSyncTimestamp() {
        return this.lastSyncTimestamp;
    }

    public void setLastSyncTimestamp(Long lastSyncTimestamp) {
        this.lastSyncTimestamp = lastSyncTimestamp;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

}
