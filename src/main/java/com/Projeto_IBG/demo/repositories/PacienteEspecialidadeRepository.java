package com.Projeto_IBG.demo.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.Projeto_IBG.demo.model.PacienteEspecialidade;
import com.Projeto_IBG.demo.model.PacienteEspecialidadeId;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PacienteEspecialidadeRepository extends JpaRepository<PacienteEspecialidade, PacienteEspecialidadeId> {
    
    // Buscar por paciente
    List<PacienteEspecialidade> findByPacienteId(Long pacienteId);
    
    // Buscar por especialidade
    List<PacienteEspecialidade> findByEspecialidadeId(Long especialidadeId);
    
    // Buscar por data de atendimento
    List<PacienteEspecialidade> findByDataAtendimento(LocalDate dataAtendimento);
    
    // Buscar por período
    List<PacienteEspecialidade> findByDataAtendimentoBetween(LocalDate dataInicio, LocalDate dataFim);
    
    // Buscar atendimentos de um paciente em uma especialidade
    List<PacienteEspecialidade> findByPacienteIdAndEspecialidadeId(Long pacienteId, Long especialidadeId);
    
    // Relatório de atendimentos por especialidade
    @Query("SELECT pe.especialidade.nome, COUNT(pe) FROM PacienteEspecialidade pe GROUP BY pe.especialidade.nome")
    List<Object[]> findAtendimentosPorEspecialidade();
    
    // Relatório de atendimentos por mês
    @Query("SELECT FUNCTION('MONTH', pe.dataAtendimento), FUNCTION('YEAR', pe.dataAtendimento), COUNT(pe) " +
           "FROM PacienteEspecialidade pe GROUP BY FUNCTION('MONTH', pe.dataAtendimento), FUNCTION('YEAR', pe.dataAtendimento)")
    List<Object[]> findAtendimentosPorMes();
}
