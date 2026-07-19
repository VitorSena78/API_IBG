package com.Projeto_IBG.demo.repositories;

import com.Projeto_IBG.demo.model.Atendimento;
import com.Projeto_IBG.demo.model.Atendimento.StatusAtendimento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AtendimentoRepository extends JpaRepository<Atendimento, Integer> {

    List<Atendimento> findByStatusOrderByCreatedAtAsc(StatusAtendimento status);

    List<Atendimento> findByPacienteIdOrderByCreatedAtDesc(Integer pacienteId);

    List<Atendimento> findByDataAtendimentoOrderByCreatedAtAsc(LocalDate data);

    List<Atendimento> findByEspecialidadeIdAndDataAtendimento(Integer especialidadeId, LocalDate data);

    List<Atendimento> findByPacienteIdAndStatusNot(Integer pacienteId, StatusAtendimento status);

    long countByStatus(StatusAtendimento status);

    long countByDataAtendimento(LocalDate data);

    @Query("SELECT a FROM Atendimento a WHERE a.status IN :statuses ORDER BY a.createdAt ASC")
    List<Atendimento> findByStatusIn(@Param("statuses") List<StatusAtendimento> statuses);

    List<Atendimento> findByStatusAndEspecialidadeIdOrderByCreatedAtAsc(
            StatusAtendimento status, Integer especialidadeId);

    @Query("SELECT a FROM Atendimento a WHERE a.status IN :statuses AND a.especialidade.id = :especialidadeId ORDER BY a.createdAt ASC")
    List<Atendimento> findByStatusInAndEspecialidadeId(
            @Param("statuses") List<StatusAtendimento> statuses,
            @Param("especialidadeId") Integer especialidadeId);

    @Query("SELECT a.especialidade.nome, COUNT(a) FROM Atendimento a WHERE a.dataAtendimento = :data GROUP BY a.especialidade.nome")
    List<Object[]> countByEspecialidade(@Param("data") LocalDate data);

    @Query("SELECT FUNCTION('YEAR', a.dataAtendimento), FUNCTION('MONTH', a.dataAtendimento), COUNT(a) " +
           "FROM Atendimento a WHERE a.dataAtendimento BETWEEN :inicio AND :fim GROUP BY FUNCTION('YEAR', a.dataAtendimento), FUNCTION('MONTH', a.dataAtendimento)")
    List<Object[]> countByMes(@Param("inicio") LocalDate inicio, @Param("fim") LocalDate fim);
}
