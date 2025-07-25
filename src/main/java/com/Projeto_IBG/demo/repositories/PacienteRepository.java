package com.Projeto_IBG.demo.repositories;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.Projeto_IBG.demo.dto.sync.SyncStatus;
import com.Projeto_IBG.demo.model.Paciente;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Integer> {
    
     // ---- Busca básica ----
    List<Paciente> findByNomeContainingIgnoreCase(String nome);
    Optional<Paciente> findByCpf(String cpf);
    Optional<Paciente> findBySus(String sus);
    List<Paciente> findByTelefone(String telefone);
    List<Paciente> findByDataNascimento(LocalDate dataNascimento);
    List<Paciente> findByIdadeBetween(Integer idadeMin, Integer idadeMax);
    Page<Paciente> findByNomeContainingIgnoreCase(String nome, Pageable pageable);

    // ---- Validações ----
    boolean existsByCpf(String cpf);
    boolean existsBySus(String sus);

    // ---- Sincronização ----
    List<Paciente> findByUpdatedAtAfter(LocalDateTime lastSync);

    @Query("SELECT p FROM Paciente p WHERE p.updatedAt > :timestamp AND p.deviceId != :deviceId")
    List<Paciente> findByUpdatedAtAfterAndDeviceIdNot(@Param("timestamp") Timestamp timestamp, @Param("deviceId") String deviceId);

    @Query("SELECT p FROM Paciente p WHERE p.syncStatus = :status")
    List<Paciente> findBySyncStatus(@Param("status") SyncStatus status);

    @Query("SELECT p FROM Paciente p WHERE p.createdAt >= :since OR p.updatedAt >= :since ORDER BY p.updatedAt DESC")
    List<Paciente> findUpdatedSince(@Param("since") LocalDateTime since);

    Optional<Paciente> findByLocalIdAndDeviceId(String localId, String deviceId);

    // ---- Relacionamentos com Especialidade ----
    @Query("SELECT p FROM Paciente p JOIN p.especialidades pe WHERE pe.especialidade.id = :especialidadeId")
    List<Paciente> findByEspecialidadeId(@Param("especialidadeId") Integer especialidadeId);

    @Query("SELECT p FROM Paciente p JOIN p.especialidades pe WHERE pe.dataAtendimento = :dataAtendimento")
    List<Paciente> findByDataAtendimento(@Param("dataAtendimento") LocalDate dataAtendimento);

    @Query("SELECT p FROM Paciente p JOIN p.especialidades pe WHERE pe.dataAtendimento BETWEEN :dataInicio AND :dataFim")
    List<Paciente> findByDataAtendimentoBetween(@Param("dataInicio") LocalDate dataInicio, @Param("dataFim") LocalDate dataFim);
}
