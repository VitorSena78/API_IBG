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
    @Query("SELECT pe FROM PacienteEspecialidade pe WHERE pe.id.pacienteId = :pacienteId")
    List<PacienteEspecialidade> findByPacienteId(@Param("pacienteId") Integer pacienteId);

    @Query("SELECT pe FROM PacienteEspecialidade pe WHERE pe.id.especialidadeId = :especialidadeId")
    List<PacienteEspecialidade> findByEspecialidadeId(@Param("especialidadeId") Integer especialidadeId);

    // Métodos que acessam atributos diretos da Entity
    List<PacienteEspecialidade> findByDataAtendimento(LocalDate dataAtendimento);
    List<PacienteEspecialidade> findByDataAtendimentoBetween(LocalDate dataInicio, LocalDate dataFim);

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

    // ---- Sincronização via Paciente ou Especialidade ----
    // Se você precisar de controle de sincronização, use os status dos Pacientes
    @Query("SELECT pe FROM PacienteEspecialidade pe WHERE pe.paciente.syncStatus = :status")
    List<PacienteEspecialidade> findByPacienteSyncStatus(@Param("status") SyncStatus status);

    // Ou busque relacionamentos onde o paciente precisa ser sincronizado
    @Query("SELECT pe FROM PacienteEspecialidade pe WHERE pe.paciente.syncStatus = 'PENDING'")
    List<PacienteEspecialidade> findPendingSync();

    // ---- Exclusão ----
    @Modifying
    @Transactional
    @Query("DELETE FROM PacienteEspecialidade pe WHERE pe.id.pacienteId = :pacienteId")
    void deleteByPacienteId(@Param("pacienteId") Integer pacienteId);

    // Buscar atualizações recentes
    @Query("SELECT pe FROM PacienteEspecialidade pe WHERE pe.updatedAt > :since ORDER BY pe.updatedAt ASC")
    List<PacienteEspecialidade> findUpdatedSince(@Param("since") LocalDateTime since);

    // ---- Métodos adicionais úteis ----
    // Buscar por data de criação
    @Query("SELECT pe FROM PacienteEspecialidade pe WHERE pe.createdAt >= :dataInicio AND pe.createdAt <= :dataFim")
    List<PacienteEspecialidade> findByCreatedAtBetween(@Param("dataInicio") LocalDateTime dataInicio, @Param("dataFim") LocalDateTime dataFim);

    // Buscar atendimentos de hoje
    @Query("SELECT pe FROM PacienteEspecialidade pe WHERE pe.dataAtendimento = CURRENT_DATE")
    List<PacienteEspecialidade> findAtendimentosHoje();

    // Contar atendimentos por especialidade
    @Query("SELECT COUNT(pe) FROM PacienteEspecialidade pe WHERE pe.id.especialidadeId = :especialidadeId")
    Long countByEspecialidadeId(@Param("especialidadeId") Integer especialidadeId);
}