package com.Projeto_IBG.demo.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PacienteDTO {

    @JsonProperty("server_id")
    private Integer serverId;

    @JsonProperty("local_id")
    private String localId;

    @JsonProperty("nome")
    private String nome;

    @JsonProperty("data_nascimento")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataNascimento;

    @JsonProperty("idade")
    private Integer idade;

    @JsonProperty("nome_da_mae")
    private String nomeDaMae;

    @JsonProperty("cpf")
    private String cpf;

    @JsonProperty("sus")
    private String sus;

    @JsonProperty("telefone")
    private String telefone;

    @JsonProperty("endereço")
    private String endereco;

    @JsonProperty("pa_x_mmhg")
    private String paXMmhg;

    @JsonProperty("fc_bpm")
    private Float fcBpm;

    @JsonProperty("fr_ibpm")
    private Float frIbpm;

    @JsonProperty("temperatura_c")
    private Float temperaturaC;

    @JsonProperty("hgt_mgld")
    private Float hgtMgld;

    @JsonProperty("spo2")
    private Float spo2;

    @JsonProperty("peso")
    private Float peso;

    @JsonProperty("altura")
    private Float altura;

    @JsonProperty("imc")
    private Float imc;

    @JsonProperty("sync_status")
    private String syncStatus;

    @JsonProperty("last_sync_timestamp")
    private Long lastSyncTimestamp;

    @JsonProperty("created_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonProperty("updated_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    public PacienteDTO() {}

    // Getters e setters (inalterados)

    public Integer getServerId() {
        return serverId;
    }

    public void setServerId(Integer serverId) {
        this.serverId = serverId;
    }

    public String getLocalId() {
        return localId;
    }

    public void setLocalId(String localId) {
        this.localId = localId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public Integer getIdade() {
        return idade;
    }

    public void setIdade(Integer idade) {
        this.idade = idade;
    }

    public String getNomeDaMae() {
        return nomeDaMae;
    }

    public void setNomeDaMae(String nomeDaMae) {
        this.nomeDaMae = nomeDaMae;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getSus() {
        return sus;
    }

    public void setSus(String sus) {
        this.sus = sus;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getPaXMmhg() {
        return paXMmhg;
    }

    public void setPaXMmhg(String paXMmhg) {
        this.paXMmhg = paXMmhg;
    }

    public Float getFcBpm() {
        return fcBpm;
    }

    public void setFcBpm(Float fcBpm) {
        this.fcBpm = fcBpm;
    }

    public Float getFrIbpm() {
        return frIbpm;
    }

    public void setFrIbpm(Float frIbpm) {
        this.frIbpm = frIbpm;
    }

    public Float getTemperaturaC() {
        return temperaturaC;
    }

    public void setTemperaturaC(Float temperaturaC) {
        this.temperaturaC = temperaturaC;
    }

    public Float getHgtMgld() {
        return hgtMgld;
    }

    public void setHgtMgld(Float hgtMgld) {
        this.hgtMgld = hgtMgld;
    }

    public Float getSpo2() {
        return spo2;
    }

    public void setSpo2(Float spo2) {
        this.spo2 = spo2;
    }

    public Float getPeso() {
        return peso;
    }

    public void setPeso(Float peso) {
        this.peso = peso;
    }

    public Float getAltura() {
        return altura;
    }

    public void setAltura(Float altura) {
        this.altura = altura;
    }

    public Float getImc() {
        return imc;
    }

    public void setImc(Float imc) {
        this.imc = imc;
    }

    public String getSyncStatus() {
        return syncStatus;
    }

    public void setSyncStatus(String syncStatus) {
        this.syncStatus = syncStatus;
    }

    public Long getLastSyncTimestamp() {
        return lastSyncTimestamp;
    }

    public void setLastSyncTimestamp(Long lastSyncTimestamp) {
        this.lastSyncTimestamp = lastSyncTimestamp;
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
