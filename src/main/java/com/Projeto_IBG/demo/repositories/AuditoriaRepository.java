package com.Projeto_IBG.demo.repositories;

import com.Projeto_IBG.demo.model.Auditoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AuditoriaRepository extends JpaRepository<Auditoria, Long> {
    List<Auditoria> findByEntidadeAndEntidadeIdOrderByCreatedAtDesc(String entidade, Integer entidadeId);
    List<Auditoria> findByCreatedAtBetweenOrderByCreatedAtDesc(LocalDateTime inicio, LocalDateTime fim);
    List<Auditoria> findByUsuarioIdOrderByCreatedAtDesc(Integer usuarioId);
}
