package com.Projeto_IBG.demo.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
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
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "data_atendimento")
    private LocalDate dataAtendimento;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        if (dataAtendimento == null) {
            dataAtendimento = LocalDate.now();
        }
    }
}
