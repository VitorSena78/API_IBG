package com.Projeto_IBG.demo.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.Projeto_IBG.demo.dto.sync.SyncStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "pacientes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "INTEGER UNSIGNED")
    private Integer id;

    @Column(nullable = false)
    private String nome;

    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;

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
    private String endereco;

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
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
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
        if (syncStatus == SyncStatus.SYNCED) {
            syncStatus = SyncStatus.PENDING;
        }
    }

    public void markAsSynced() {
        this.syncStatus = SyncStatus.SYNCED;
        this.lastSyncAt = LocalDateTime.now();
    }

    public boolean needsSync() {
        return this.syncStatus == SyncStatus.PENDING || this.syncStatus == SyncStatus.CONFLICT;
    }

    public void markAsConflict() {
        this.syncStatus = SyncStatus.CONFLICT;
    }
}
