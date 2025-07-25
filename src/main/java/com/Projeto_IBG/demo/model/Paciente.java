package com.Projeto_IBG.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import com.Projeto_IBG.demo.dto.sync.SyncStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Paciente")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Paciente {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(nullable = false)
    private String nome;
    
    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;
    
    @Column(name = "idade")
    private Integer idade;
    
    @Column(name = "nome_da_mae")
    private String nomeDaMae;
    
    @Column(length = 45)
    private String cpf;
    
    @Column(length = 45)
    private String sus;
    
    @Column(length = 45)
    private String telefone;
    
    @Column(columnDefinition = "TEXT")
    private String endereço;
    
    @Column(name = "pa_x_mmhg", length = 20)
    private String paXMmhg;
    
    @Column(name = "fc_bpm")
    private Float fcBpm;
    
    @Column(name = "fr_ibpm")
    private Float frIbpm;
    
    @Column(name = "temperatura_c")
    private Float temperaturaC;
    
    @Column(name = "hgt_mgld")
    private Float hgtMgld;
    
    private Float spo2;
    
    private Float peso;
    
    private Float altura;
    
    private Float imc;
    
    @Column(name = "created_at")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "sync_status")
    private SyncStatus syncStatus = SyncStatus.PENDING;
    
    @Column(name = "last_sync_at")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime lastSyncAt;
    
    @Column(name = "device_id", length = 100)
    private String deviceId;
    
    @Column(name = "local_id", length = 100)
    private String localId;
    
    @OneToMany(mappedBy = "paciente", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<PacienteEspecialidade> especialidades;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (syncStatus == null) {
            syncStatus = SyncStatus.PENDING;
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
        // Marcar como PENDING quando houver mudanças (exceto quando está sendo sincronizado)
        if (syncStatus == SyncStatus.SYNCED) {
            syncStatus = SyncStatus.PENDING;
        }
    }
    
    // Método utilitário para marcar como sincronizado
    public void markAsSynced() {
        this.syncStatus = SyncStatus.SYNCED;
        this.lastSyncAt = LocalDateTime.now();
    }
    
    // Método utilitário para verificar se precisa sincronizar
    public boolean needsSync() {
        return this.syncStatus == SyncStatus.PENDING || this.syncStatus == SyncStatus.CONFLICT;
    }
    
    // Método utilitário para marcar conflito
    public void markAsConflict() {
        this.syncStatus = SyncStatus.CONFLICT;
    }
}