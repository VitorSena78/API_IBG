package com.Projeto_IBG.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PacienteEspecialidadeId implements Serializable {
    
    @Column(name = "Paciente_id")
    private Integer pacienteId;
    
    @Column(name = "Especialidade_id")
    private Integer especialidadeId;
}
