package com.Projeto_IBG.demo.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.time.LocalDateTime;
import java.util.List;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "Especialidade")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Especialidade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 45)
    private String nome;

    @Column(name = "fichas", columnDefinition = "INT UNSIGNED DEFAULT 0")
    private Integer fichas = 0;

    @Column(name = "atendimentos_restantes_hoje", columnDefinition = "INT UNSIGNED DEFAULT 0")
    private Integer atendimentosRestantesHoje = 0;

    @Column(name = "atendimentos_totais_hoje", columnDefinition = "INT UNSIGNED DEFAULT 0")
    private Integer atendimentosTotaisHoje = 0;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "especialidade", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    @JsonManagedReference("especialidade-pacientes")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private List<PacienteEspecialidade> pacientes;

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;
        
        // Inicializar valores padrão se necessário
        if (fichas == null) fichas = 0;
        if (atendimentosRestantesHoje == null) atendimentosRestantesHoje = 0;
        if (atendimentosTotaisHoje == null) atendimentosTotaisHoje = 0;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Getters e Setters adicionais para os novos campos
    public Integer getFichas() { return fichas; }
    public void setFichas(Integer fichas) { this.fichas = fichas; }

    public Integer getAtendimentosRestantesHoje() { return atendimentosRestantesHoje; }
    public void setAtendimentosRestantesHoje(Integer atendimentosRestantesHoje) { 
        this.atendimentosRestantesHoje = atendimentosRestantesHoje; 
    }

    public Integer getAtendimentosTotaisHoje() { return atendimentosTotaisHoje; }
    public void setAtendimentosTotaisHoje(Integer atendimentosTotaisHoje) { 
        this.atendimentosTotaisHoje = atendimentosTotaisHoje; 
    }

    // Getters e Setters existentes
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}