package com.Projeto_IBG.demo.repositories;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.Projeto_IBG.demo.model.Paciente;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Integer> {
    
    // Buscar por nome (ignora maiúsculas/minúsculas)
    List<Paciente> findByNomeContainingIgnoreCase(String nome);
    
    // Buscar por CPF
    Optional<Paciente> findByCpf(String cpf);
    
    // Buscar por SUS
    Optional<Paciente> findBySus(String sus);
    
    // Buscar por telefone
    List<Paciente> findByTelefone(String telefone);
    
    // Buscar por data de nascimento
    List<Paciente> findByDataNascimento(LocalDate dataNascimento);
    
    // Buscar por faixa etária
    List<Paciente> findByIdadeBetween(Integer idadeMin, Integer idadeMax);
    
    // Busca paginada por nome
    Page<Paciente> findByNomeContainingIgnoreCase(String nome, Pageable pageable);
    
    // Buscar pacientes com determinada especialidade
    @Query("SELECT p FROM Paciente p JOIN p.especialidades pe WHERE pe.especialidade.id = :especialidadeId")
    List<Paciente> findByEspecialidadeId(@Param("especialidadeId") Long especialidadeId);
    
    // Buscar pacientes atendidos em uma data específica
    @Query("SELECT p FROM Paciente p JOIN p.especialidades pe WHERE pe.dataAtendimento = :dataAtendimento")
    List<Paciente> findByDataAtendimento(@Param("dataAtendimento") LocalDate dataAtendimento);
    
    // Buscar pacientes atendidos em um período
    @Query("SELECT p FROM Paciente p JOIN p.especialidades pe WHERE pe.dataAtendimento BETWEEN :dataInicio AND :dataFim")
    List<Paciente> findByDataAtendimentoBetween(@Param("dataInicio") LocalDate dataInicio, @Param("dataFim") LocalDate dataFim);
    
    // Verificar se CPF já existe
    boolean existsByCpf(String cpf);
    
    // Verificar se SUS já existe
    boolean existsBySus(String sus);
}
