package com.Projeto_IBG.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.Projeto_IBG.demo.dto.sync.SyncStatus;
import com.Projeto_IBG.demo.model.PacienteEspecialidade;
import com.Projeto_IBG.demo.model.PacienteEspecialidadeId;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PacienteEspecialidadeRepository extends JpaRepository<PacienteEspecialidade, PacienteEspecialidadeId> {

    // ---- Consultas básicas ----
    // ALTERAÇÃO: Adicionar @Query para acessar via EmbeddedId
    @Query("SELECT pe FROM PacienteEspecialidade pe WHERE pe.id.pacienteId = :pacienteId")
    List<PacienteEspecialidade> findByPacienteId(@Param("pacienteId") Integer pacienteId);
    
    // ALTERAÇÃO: Adicionar @Query para acessar via EmbeddedId
    @Query("SELECT pe FROM PacienteEspecialidade pe WHERE pe.id.especialidadeId = :especialidadeId")
    List<PacienteEspecialidade> findByEspecialidadeId(@Param("especialidadeId") Integer especialidadeId);
    
    // Estes métodos podem permanecer como estão (acessam atributos diretos)
    List<PacienteEspecialidade> findByDataAtendimento(LocalDate dataAtendimento);
    List<PacienteEspecialidade> findByDataAtendimentoBetween(LocalDate dataInicio, LocalDate dataFim);
    
    // ALTERAÇÃO: Adicionar @Query para acessar via EmbeddedId
    @Query("SELECT pe FROM PacienteEspecialidade pe WHERE pe.id.pacienteId = :pacienteId AND pe.id.especialidadeId = :especialidadeId")
    Optional<PacienteEspecialidade> findByPacienteIdAndEspecialidadeId(
        @Param("pacienteId") Integer pacienteId, 
        @Param("especialidadeId") Integer especialidadeId
    );

    // ---- Relatórios ----
    @Query("SELECT pe.especialidade.nome, COUNT(pe) FROM PacienteEspecialidade pe GROUP BY pe.especialidade.nome")
    List<Object[]> findAtendimentosPorEspecialidade();

    @Query("SELECT FUNCTION('MONTH', pe.dataAtendimento), FUNCTION('YEAR', pe.dataAtendimento), COUNT(pe) " +
           "FROM PacienteEspecialidade pe GROUP BY FUNCTION('YEAR', pe.dataAtendimento), FUNCTION('MONTH', pe.dataAtendimento)")
    List<Object[]> findAtendimentosPorMes();

    // ---- Sincronização ----
    @Query("SELECT pe FROM PacienteEspecialidade pe WHERE pe.syncStatus = :status")
    List<PacienteEspecialidade> findBySyncStatus(@Param("status") SyncStatus status);

    // ---- Exclusão ----
    @Modifying
    @Transactional
    @Query("DELETE FROM PacienteEspecialidade pe WHERE pe.id.pacienteId = :pacienteId")
    void deleteByPacienteId(@Param("pacienteId") Integer pacienteId);

    @Query("SELECT pe FROM PacienteEspecialidade pe WHERE pe.updatedAt > :since ORDER BY pe.updatedAt ASC")
    List<PacienteEspecialidade> findUpdatedSince(@Param("since") LocalDateTime since);

}