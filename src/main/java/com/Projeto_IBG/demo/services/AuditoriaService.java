package com.Projeto_IBG.demo.services;

import com.Projeto_IBG.demo.model.Auditoria;
import com.Projeto_IBG.demo.model.Usuario;
import com.Projeto_IBG.demo.repositories.AuditoriaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuditoriaService {

    private final AuditoriaRepository auditoriaRepository;

    public AuditoriaService(AuditoriaRepository auditoriaRepository) {
        this.auditoriaRepository = auditoriaRepository;
    }

    public void registrar(Usuario usuario, String acao, String entidade, Integer entidadeId,
                          String valoresAntigos, String valoresNovos, String ip) {
        Auditoria auditoria = new Auditoria();
        auditoria.setUsuario(usuario);
        auditoria.setAcao(acao);
        auditoria.setEntidade(entidade);
        auditoria.setEntidadeId(entidadeId);
        auditoria.setValoresAntigos(valoresAntigos);
        auditoria.setValoresNovos(valoresNovos);
        auditoria.setIp(ip);
        auditoriaRepository.save(auditoria);
    }

    public List<Auditoria> listarTodas() {
        return auditoriaRepository.findAll();
    }

    public List<Auditoria> listarPorEntidade(String entidade, Integer entidadeId) {
        return auditoriaRepository.findByEntidadeAndEntidadeIdOrderByCreatedAtDesc(entidade, entidadeId);
    }
}
