package com.Projeto_IBG.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PacienteEspecialidadeId implements Serializable {
    
    @Column(name = "Paciente_id")
    private Integer pacienteId;
    
    @Column(name = "Especialidade_id")
    private Integer especialidadeId;
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PacienteEspecialidadeId that = (PacienteEspecialidadeId) o;
        return Objects.equals(pacienteId, that.pacienteId) &&
               Objects.equals(especialidadeId, that.especialidadeId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(pacienteId, especialidadeId);
    }
}
