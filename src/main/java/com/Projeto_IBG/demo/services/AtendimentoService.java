package com.Projeto_IBG.demo.services;

import com.Projeto_IBG.demo.dto.AtendimentoRequestDTO;
import com.Projeto_IBG.demo.dto.AtendimentoResponseDTO;
import com.Projeto_IBG.demo.model.*;
import com.Projeto_IBG.demo.repositories.AtendimentoRepository;
import com.Projeto_IBG.demo.repositories.EspecialidadeRepository;
import com.Projeto_IBG.demo.repositories.PacienteRepository;
import com.Projeto_IBG.demo.repositories.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AtendimentoService {

    private final AtendimentoRepository atendimentoRepository;
    private final PacienteRepository pacienteRepository;
    private final EspecialidadeRepository especialidadeRepository;
    private final UsuarioRepository usuarioRepository;

    public AtendimentoService(AtendimentoRepository atendimentoRepository,
                              PacienteRepository pacienteRepository,
                              EspecialidadeRepository especialidadeRepository,
                              UsuarioRepository usuarioRepository) {
        this.atendimentoRepository = atendimentoRepository;
        this.pacienteRepository = pacienteRepository;
        this.especialidadeRepository = especialidadeRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public AtendimentoResponseDTO iniciarAtendimento(AtendimentoRequestDTO request, Integer recepcionistaId) {
        Paciente paciente = pacienteRepository.findById(request.getPacienteId())
                .orElseThrow(() -> new RuntimeException("Paciente não encontrado"));
        Especialidade especialidade = especialidadeRepository.findById(request.getEspecialidadeId())
                .orElseThrow(() -> new RuntimeException("Especialidade não encontrada"));
        Usuario recepcionista = usuarioRepository.findById(recepcionistaId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Atendimento atendimento = new Atendimento();
        atendimento.setPaciente(paciente);
        atendimento.setEspecialidade(especialidade);
        atendimento.setRecepcionista(recepcionista);
        atendimento.setDataAtendimento(request.getDataAtendimento() != null ? request.getDataAtendimento() : LocalDate.now());

        if (especialidade.getTriagemObrigatoria()) {
            atendimento.setStatus(Atendimento.StatusAtendimento.AGUARDANDO_TRIAGEM);
        } else {
            atendimento.setStatus(Atendimento.StatusAtendimento.AGUARDANDO_CONSULTA);
        }

        atendimento = atendimentoRepository.save(atendimento);
        return toDTO(atendimento);
    }

    @Transactional
    public AtendimentoResponseDTO realizarTriagem(Integer atendimentoId, AtendimentoRequestDTO request, Integer enfermeiraId) {
        Atendimento atendimento = atendimentoRepository.findById(atendimentoId)
                .orElseThrow(() -> new RuntimeException("Atendimento não encontrado"));

        if (atendimento.getStatus() != Atendimento.StatusAtendimento.AGUARDANDO_TRIAGEM &&
            atendimento.getStatus() != Atendimento.StatusAtendimento.EM_TRIAGEM) {
            throw new RuntimeException("Atendimento não está aguardando triagem");
        }

        Usuario enfermeira = usuarioRepository.findById(enfermeiraId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Integer pacienteId = atendimento.getPaciente().getId();

        // Aplica os dados de triagem ao atendimento atual
        aplicarDadosTriagem(atendimento, request, enfermeira);
        atendimento.setStatus(Atendimento.StatusAtendimento.AGUARDANDO_CONSULTA);
        atendimentoRepository.save(atendimento);

        // Propaga os mesmos dados para todos os outros atendimentos do mesmo
        // paciente que ainda estão AGUARDANDO_TRIAGEM (mesmo dia)
        List<Atendimento> outros = atendimentoRepository
                .findByPacienteIdAndStatusOrderByCreatedAtAsc(pacienteId,
                        Atendimento.StatusAtendimento.AGUARDANDO_TRIAGEM);
        for (Atendimento outro : outros) {
            if (outro.getId().equals(atendimentoId)) continue;
            aplicarDadosTriagem(outro, request, enfermeira);
            outro.setStatus(Atendimento.StatusAtendimento.AGUARDANDO_CONSULTA);
            atendimentoRepository.save(outro);
        }

        return toDTO(atendimento);
    }

    private void aplicarDadosTriagem(Atendimento a, AtendimentoRequestDTO r, Usuario enfermeira) {
        a.setPaXMmhg(r.getPaXMmhg());
        a.setFcBpm(r.getFcBpm());
        a.setFrIbpm(r.getFrIbpm());
        a.setTemperaturaC(r.getTemperaturaC());
        a.setHgtMgld(r.getHgtMgld());
        a.setSpo2(r.getSpo2());
        a.setPeso(r.getPeso());
        a.setAltura(r.getAltura());
        a.setImc(r.getImc());
        a.setObservacoesEnfermagem(r.getObservacoesEnfermagem());
        a.setEnfermeira(enfermeira);
        a.setTriagemRealizadaEm(LocalDateTime.now());
    }

    @Transactional
    public AtendimentoResponseDTO realizarConsulta(Integer atendimentoId, AtendimentoRequestDTO request, Integer medicoId) {
        Atendimento atendimento = atendimentoRepository.findById(atendimentoId)
                .orElseThrow(() -> new RuntimeException("Atendimento não encontrado"));

        if (atendimento.getStatus() != Atendimento.StatusAtendimento.AGUARDANDO_CONSULTA &&
            atendimento.getStatus() != Atendimento.StatusAtendimento.EM_CONSULTA) {
            throw new RuntimeException("Atendimento não está aguardando consulta");
        }

        Usuario medico = usuarioRepository.findById(medicoId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Integer pacienteId = atendimento.getPaciente().getId();

        aplicarDadosConsulta(atendimento, request, medico);
        atendimento.setStatus(Atendimento.StatusAtendimento.FINALIZADO);
        atendimentoRepository.save(atendimento);

        // Propaga para outros atendimentos do mesmo paciente que estão
        // nas especialidades deste médico e ainda aguardam consulta
        Set<Integer> espIdsMedico = medico.getEspecialidades() != null
                ? medico.getEspecialidades().stream().map(Especialidade::getId).collect(Collectors.toSet())
                : Collections.emptySet();

        List<Atendimento.StatusAtendimento> statuses = List.of(
                Atendimento.StatusAtendimento.AGUARDANDO_CONSULTA,
                Atendimento.StatusAtendimento.EM_CONSULTA);
        List<Atendimento> outros = atendimentoRepository
                .findByPacienteIdAndStatusInOrderByCreatedAtAsc(pacienteId, statuses);
        for (Atendimento outro : outros) {
            if (outro.getId().equals(atendimentoId)) continue;
            if (!espIdsMedico.contains(outro.getEspecialidade().getId())) continue;
            aplicarDadosConsulta(outro, request, medico);
            outro.setStatus(Atendimento.StatusAtendimento.FINALIZADO);
            atendimentoRepository.save(outro);
        }

        return toDTO(atendimento);
    }

    private void aplicarDadosConsulta(Atendimento a, AtendimentoRequestDTO r, Usuario medico) {
        a.setAvaliacaoMedica(r.getAvaliacaoMedica());
        a.setDiagnostico(r.getDiagnostico());
        a.setCondutas(r.getCondutas());
        a.setObservacoesMedicas(r.getObservacoesMedicas());
        a.setMedico(medico);
        a.setConsultaRealizadaEm(LocalDateTime.now());
    }

    @Transactional
    public AtendimentoResponseDTO editarTriagem(Integer atendimentoId, AtendimentoRequestDTO request, Integer enfermeiraId) {
        Atendimento atendimento = atendimentoRepository.findById(atendimentoId)
                .orElseThrow(() -> new RuntimeException("Atendimento não encontrado"));

        if (atendimento.getStatus() == Atendimento.StatusAtendimento.AGUARDANDO_TRIAGEM) {
            throw new RuntimeException("Paciente ainda não foi triado");
        }

        Usuario enfermeira = usuarioRepository.findById(enfermeiraId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Integer pacienteId = atendimento.getPaciente().getId();

        aplicarDadosTriagem(atendimento, request, enfermeira);
        atendimentoRepository.save(atendimento);

        // Propaga edição para todos os atendimentos do mesmo paciente que já
        // passaram pela triagem (status AGUARDANDO_CONSULTA ou FINALIZADO)
        List<Atendimento.StatusAtendimento> statuses = List.of(
                Atendimento.StatusAtendimento.AGUARDANDO_CONSULTA,
                Atendimento.StatusAtendimento.FINALIZADO);
        List<Atendimento> outros = atendimentoRepository
                .findByPacienteIdAndStatusInOrderByCreatedAtAsc(pacienteId, statuses);
        for (Atendimento outro : outros) {
            if (outro.getId().equals(atendimentoId)) continue;
            aplicarDadosTriagem(outro, request, enfermeira);
            atendimentoRepository.save(outro);
        }

        return toDTO(atendimento);
    }

    @Transactional
    public AtendimentoResponseDTO editarConsulta(Integer atendimentoId, AtendimentoRequestDTO request, Integer medicoId) {
        Atendimento atendimento = atendimentoRepository.findById(atendimentoId)
                .orElseThrow(() -> new RuntimeException("Atendimento não encontrado"));

        if (atendimento.getStatus() != Atendimento.StatusAtendimento.FINALIZADO) {
            throw new RuntimeException("Consulta ainda não foi finalizada");
        }

        Usuario medico = usuarioRepository.findById(medicoId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Integer pacienteId = atendimento.getPaciente().getId();

        aplicarDadosConsulta(atendimento, request, medico);
        atendimentoRepository.save(atendimento);

        // Propaga edição para atendimentos do mesmo paciente que estão nas
        // especialidades do médico (já finalizados)
        Set<Integer> espIdsMedico = medico.getEspecialidades() != null
                ? medico.getEspecialidades().stream().map(Especialidade::getId).collect(Collectors.toSet())
                : Collections.emptySet();

        List<Atendimento> outros = atendimentoRepository
                .findByPacienteIdAndStatusOrderByCreatedAtAsc(pacienteId,
                        Atendimento.StatusAtendimento.FINALIZADO);
        for (Atendimento outro : outros) {
            if (outro.getId().equals(atendimentoId)) continue;
            if (!espIdsMedico.contains(outro.getEspecialidade().getId())) continue;
            aplicarDadosConsulta(outro, request, medico);
            atendimentoRepository.save(outro);
        }

        return toDTO(atendimento);
    }

    @Transactional
    public AtendimentoResponseDTO cancelarAtendimento(Integer atendimentoId) {
        Atendimento atendimento = atendimentoRepository.findById(atendimentoId)
                .orElseThrow(() -> new RuntimeException("Atendimento não encontrado"));

        if (atendimento.getStatus() == Atendimento.StatusAtendimento.FINALIZADO) {
            throw new RuntimeException("Não é possível cancelar um atendimento finalizado");
        }

        atendimento.setStatus(Atendimento.StatusAtendimento.CANCELADO);
        atendimento = atendimentoRepository.save(atendimento);
        return toDTO(atendimento);
    }

    public List<AtendimentoResponseDTO> listarMeus(Integer userId, String role) {
        List<Atendimento> atendimentos;
        LocalDate hoje = LocalDate.now();

        switch (role) {
            case "MEDICO": {
                Set<Integer> espIds = getEspecialidadeIds(userId);
                List<Atendimento.StatusAtendimento> statusesMedico = List.of(
                        Atendimento.StatusAtendimento.AGUARDANDO_CONSULTA,
                        Atendimento.StatusAtendimento.EM_CONSULTA,
                        Atendimento.StatusAtendimento.FINALIZADO);
                List<Atendimento> meus = atendimentoRepository.findByMedicoIdAndStatusIn(userId, statusesMedico);
                List<Atendimento> pendentes = new ArrayList<>();
                if (!espIds.isEmpty()) {
                    for (Integer espId : espIds) {
                        pendentes.addAll(atendimentoRepository
                                .findByStatusAndEspecialidadeIdOrderByCreatedAtAsc(
                                        Atendimento.StatusAtendimento.AGUARDANDO_CONSULTA, espId));
                    }
                }
                atendimentos = new ArrayList<>();
                atendimentos.addAll(meus);
                for (Atendimento a : pendentes) {
                    if (atendimentos.stream().noneMatch(at -> at.getId().equals(a.getId()))) {
                        atendimentos.add(a);
                    }
                }
                atendimentos.sort((a, b) -> a.getCreatedAt().compareTo(b.getCreatedAt()));
                break;
            }
            case "ENFERMEIRA": {
                List<Atendimento> aguardandoTriagem = atendimentoRepository
                        .findByStatusOrderByCreatedAtAsc(Atendimento.StatusAtendimento.AGUARDANDO_TRIAGEM);
                List<Atendimento> minhasTriagens = atendimentoRepository
                        .findByEnfermeiraIdAndTriagemRealizadaEmBetweenOrderByTriagemRealizadaEmDesc(
                                userId, hoje.atStartOfDay(), hoje.atTime(LocalTime.MAX));
                atendimentos = new ArrayList<>();
                atendimentos.addAll(aguardandoTriagem);
                for (Atendimento a : minhasTriagens) {
                    if (atendimentos.stream().noneMatch(at -> at.getId().equals(a.getId()))) {
                        atendimentos.add(a);
                    }
                }
                atendimentos.sort((a, b) -> a.getCreatedAt().compareTo(b.getCreatedAt()));
                break;
            }
            case "RECEPCIONISTA":
                atendimentos = atendimentoRepository.findByDataAtendimentoOrderByCreatedAtAsc(hoje);
                break;
            default:
                atendimentos = atendimentoRepository.findByStatusIn(List.of(Atendimento.StatusAtendimento.values()));
                break;
        }

        return atendimentos.stream().map(this::toDTO).collect(Collectors.toList());
    }

    public Map<String, Object> resumoMeu(Integer userId, String role) {
        Map<String, Object> resumo = new LinkedHashMap<>();
        LocalDate hoje = LocalDate.now();

        long totalHoje = atendimentoRepository.countByDataAtendimento(hoje);
        long aguardandoTriagem = atendimentoRepository.countByStatus(Atendimento.StatusAtendimento.AGUARDANDO_TRIAGEM);
        long aguardandoConsulta = atendimentoRepository.countByStatus(Atendimento.StatusAtendimento.AGUARDANDO_CONSULTA);
        long emAtendimento = atendimentoRepository.countByStatus(Atendimento.StatusAtendimento.EM_CONSULTA);

        resumo.put("total_hoje", totalHoje);
        resumo.put("aguardando_triagem", aguardandoTriagem);
        resumo.put("aguardando_consulta", aguardandoConsulta);
        resumo.put("em_atendimento", emAtendimento);

        switch (role) {
            case "MEDICO": {
                Set<Integer> espIds = getEspecialidadeIds(userId);
                long meusFinalizadosHoje = atendimentoRepository
                        .countByMedicoIdAndConsultaRealizadaEmBetween(
                                userId, hoje.atStartOfDay(), hoje.atTime(LocalTime.MAX));
                long minhasPendentes = 0;
                if (!espIds.isEmpty()) {
                    for (Integer espId : espIds) {
                        minhasPendentes += atendimentoRepository
                                .findByStatusAndEspecialidadeIdOrderByCreatedAtAsc(
                                        Atendimento.StatusAtendimento.AGUARDANDO_CONSULTA, espId)
                                .size();
                    }
                }
                resumo.put("meus_finalizados_hoje", meusFinalizadosHoje);
                resumo.put("minhas_pendentes", minhasPendentes);
                break;
            }
            case "ENFERMEIRA": {
                long minhasTriagensHoje = atendimentoRepository
                        .countByEnfermeiraIdAndTriagemRealizadaEmBetween(
                                userId, hoje.atStartOfDay(), hoje.atTime(LocalTime.MAX));
                resumo.put("minhas_triagens_hoje", minhasTriagensHoje);
                break;
            }
            case "RECEPCIONISTA": {
                long meusRegistrosHoje = atendimentoRepository
                        .findByDataAtendimentoOrderByCreatedAtAsc(hoje).size();
                resumo.put("meus_registros_hoje", meusRegistrosHoje);
                break;
            }
        }

        return resumo;
    }

    private Set<Integer> getEspecialidadeIds(Integer userId) {
        java.util.Optional<Usuario> opt = usuarioRepository.findById(userId);
        if (opt.isPresent()) {
            Usuario u = opt.get();
            if (u.getEspecialidades() != null) {
                return u.getEspecialidades().stream()
                        .map(esp -> esp.getId())
                        .collect(Collectors.toSet());
            }
        }
        return Collections.emptySet();
    }

    public List<AtendimentoResponseDTO> listarPorStatus(String status, Integer especialidadeId) {
        List<Atendimento> atendimentos;
        List<Atendimento.StatusAtendimento> statuses;

        if (status == null || status.isEmpty()) {
            statuses = List.of(
                    Atendimento.StatusAtendimento.AGUARDANDO_TRIAGEM,
                    Atendimento.StatusAtendimento.EM_TRIAGEM,
                    Atendimento.StatusAtendimento.AGUARDANDO_CONSULTA,
                    Atendimento.StatusAtendimento.EM_CONSULTA,
                    Atendimento.StatusAtendimento.FINALIZADO,
                    Atendimento.StatusAtendimento.CANCELADO);
        } else {
            statuses = List.of(Atendimento.StatusAtendimento.valueOf(status));
        }

        if (especialidadeId != null) {
            atendimentos = atendimentoRepository.findByStatusInAndEspecialidadeId(statuses, especialidadeId);
        } else {
            atendimentos = atendimentoRepository.findByStatusIn(statuses);
        }
        return atendimentos.stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<AtendimentoResponseDTO> listarPorData(LocalDate data) {
        return atendimentoRepository.findByDataAtendimentoOrderByCreatedAtAsc(data)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<AtendimentoResponseDTO> historicoPaciente(Integer pacienteId) {
        return atendimentoRepository.findByPacienteIdOrderByCreatedAtDesc(pacienteId)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public AtendimentoResponseDTO buscarPorId(Integer id) {
        Atendimento atendimento = atendimentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Atendimento não encontrado"));
        return toDTO(atendimento);
    }

    private AtendimentoResponseDTO toDTO(Atendimento a) {
        AtendimentoResponseDTO dto = new AtendimentoResponseDTO();
        dto.setId(a.getId());
        dto.setPacienteId(a.getPaciente().getId());
        dto.setPacienteNome(a.getPaciente().getNome());
        dto.setPacienteCpf(a.getPaciente().getCpf());
        dto.setPacienteSus(a.getPaciente().getSus());
        dto.setEspecialidadeId(a.getEspecialidade().getId());
        dto.setEspecialidadeNome(a.getEspecialidade().getNome());
        dto.setTriagemObrigatoria(a.getEspecialidade().getTriagemObrigatoria());
        dto.setStatus(a.getStatus().name());
        dto.setDataAtendimento(a.getDataAtendimento());
        dto.setPaXMmhg(a.getPaXMmhg());
        dto.setFcBpm(a.getFcBpm());
        dto.setFrIbpm(a.getFrIbpm());
        dto.setTemperaturaC(a.getTemperaturaC());
        dto.setHgtMgld(a.getHgtMgld());
        dto.setSpo2(a.getSpo2());
        dto.setPeso(a.getPeso());
        dto.setAltura(a.getAltura());
        dto.setImc(a.getImc());
        dto.setObservacoesEnfermagem(a.getObservacoesEnfermagem());
        dto.setEnfermeiraNome(a.getEnfermeira() != null ? a.getEnfermeira().getNome() : null);
        dto.setTriagemRealizadaEm(a.getTriagemRealizadaEm());
        dto.setAvaliacaoMedica(a.getAvaliacaoMedica());
        dto.setDiagnostico(a.getDiagnostico());
        dto.setCondutas(a.getCondutas());
        dto.setObservacoesMedicas(a.getObservacoesMedicas());
        dto.setMedicoNome(a.getMedico() != null ? a.getMedico().getNome() : null);
        dto.setConsultaRealizadaEm(a.getConsultaRealizadaEm());
        dto.setRecepcionistaNome(a.getRecepcionista() != null ? a.getRecepcionista().getNome() : null);
        dto.setCreatedAt(a.getCreatedAt());
        dto.setUpdatedAt(a.getUpdatedAt());
        return dto;
    }
}
