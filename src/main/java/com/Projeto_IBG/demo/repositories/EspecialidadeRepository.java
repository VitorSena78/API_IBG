package com.Projeto_IBG.demo.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.Projeto_IBG.demo.model.Especialidade;

import java.util.List;
import java.util.Optional;

@Repository
public interface EspecialidadeRepository extends JpaRepository<Especialidade, Integer> {
    
    // Buscar por nome (ignora maiúsculas/minúsculas)
    List<Especialidade> findByNomeContainingIgnoreCase(String nome);
    
    // Buscar por nome exato
    Optional<Especialidade> findByNome(String nome);
    
    // Verificar se especialidade existe
    boolean existsByNome(String nome);
    
    // Buscar especialidades com pacientes
    @Query("SELECT e FROM Especialidade e WHERE SIZE(e.pacientes) > 0")
    List<Especialidade> findEspecialidadesComPacientes();
    
    // Contar pacientes por especialidade
    @Query("SELECT COUNT(pe) FROM PacienteEspecialidade pe WHERE pe.especialidade.id = :especialidadeId")
    Long countPacientesByEspecialidadeId(@Param("especialidadeId") Integer especialidadeId);
}
