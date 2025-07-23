package com.Projeto_IBG.demo.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.Projeto_IBG.demo.dto.sync.SyncStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "Paciente_has_Especialidade")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PacienteEspecialidade {
    
    @EmbeddedId
    private PacienteEspecialidadeId id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("pacienteId")
    @JoinColumn(name = "Paciente_id")
    @JsonBackReference
    private Paciente paciente;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("especialidadeId")
    @JoinColumn(name = "Especialidade_id")
    @JsonBackReference
    private Especialidade especialidade;
    
    @Column(name = "data_atendimento")
    private LocalDate dataAtendimento;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "sync_status")
    private SyncStatus syncStatus = SyncStatus.PENDING;
    
    @Column(name = "device_id")
    private String deviceId;
    
    // Métodos de conveniência para acessar os IDs
    public Integer getPacienteId() {
        return id != null ? id.getPacienteId() : null;
    }
    
    public void setPacienteId(Integer pacienteId) {
        if (id == null) {
            id = new PacienteEspecialidadeId();
        }
        id.setPacienteId(pacienteId);
    }
    
    public Integer getEspecialidadeId() {
        return id != null ? id.getEspecialidadeId() : null;
    }
    
    public void setEspecialidadeId(Integer especialidadeId) {
        if (id == null) {
            id = new PacienteEspecialidadeId();
        }
        id.setEspecialidadeId(especialidadeId);
    }
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (dataAtendimento == null) {
            dataAtendimento = LocalDate.now();
        }
    }
}