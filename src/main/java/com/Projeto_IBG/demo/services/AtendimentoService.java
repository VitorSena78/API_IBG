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
import java.util.List;
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

        atendimento.setPaXMmhg(request.getPaXMmhg());
        atendimento.setFcBpm(request.getFcBpm());
        atendimento.setFrIbpm(request.getFrIbpm());
        atendimento.setTemperaturaC(request.getTemperaturaC());
        atendimento.setHgtMgld(request.getHgtMgld());
        atendimento.setSpo2(request.getSpo2());
        atendimento.setPeso(request.getPeso());
        atendimento.setAltura(request.getAltura());
        atendimento.setImc(request.getImc());
        atendimento.setObservacoesEnfermagem(request.getObservacoesEnfermagem());
        atendimento.setEnfermeira(enfermeira);
        atendimento.setTriagemRealizadaEm(LocalDateTime.now());
        atendimento.setStatus(Atendimento.StatusAtendimento.AGUARDANDO_CONSULTA);

        atendimento = atendimentoRepository.save(atendimento);
        return toDTO(atendimento);
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

        atendimento.setAvaliacaoMedica(request.getAvaliacaoMedica());
        atendimento.setDiagnostico(request.getDiagnostico());
        atendimento.setCondutas(request.getCondutas());
        atendimento.setObservacoesMedicas(request.getObservacoesMedicas());
        atendimento.setMedico(medico);
        atendimento.setConsultaRealizadaEm(LocalDateTime.now());
        atendimento.setStatus(Atendimento.StatusAtendimento.FINALIZADO);

        atendimento = atendimentoRepository.save(atendimento);
        return toDTO(atendimento);
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

        atendimento.setPaXMmhg(request.getPaXMmhg());
        atendimento.setFcBpm(request.getFcBpm());
        atendimento.setFrIbpm(request.getFrIbpm());
        atendimento.setTemperaturaC(request.getTemperaturaC());
        atendimento.setHgtMgld(request.getHgtMgld());
        atendimento.setSpo2(request.getSpo2());
        atendimento.setPeso(request.getPeso());
        atendimento.setAltura(request.getAltura());
        atendimento.setImc(request.getImc());
        atendimento.setObservacoesEnfermagem(request.getObservacoesEnfermagem());
        atendimento.setEnfermeira(enfermeira);
        atendimento.setTriagemRealizadaEm(LocalDateTime.now());

        atendimento = atendimentoRepository.save(atendimento);
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

        atendimento.setAvaliacaoMedica(request.getAvaliacaoMedica());
        atendimento.setDiagnostico(request.getDiagnostico());
        atendimento.setCondutas(request.getCondutas());
        atendimento.setObservacoesMedicas(request.getObservacoesMedicas());
        atendimento.setMedico(medico);
        atendimento.setConsultaRealizadaEm(LocalDateTime.now());

        atendimento = atendimentoRepository.save(atendimento);
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

    public List<AtendimentoResponseDTO> listarPorStatus(String status) {
        List<Atendimento> atendimentos;
        if (status == null || status.isEmpty()) {
            atendimentos = atendimentoRepository.findByStatusIn(List.of(
                    Atendimento.StatusAtendimento.AGUARDANDO_TRIAGEM,
                    Atendimento.StatusAtendimento.EM_TRIAGEM,
                    Atendimento.StatusAtendimento.AGUARDANDO_CONSULTA,
                    Atendimento.StatusAtendimento.EM_CONSULTA));
        } else {
            atendimentos = atendimentoRepository.findByStatusOrderByCreatedAtAsc(
                    Atendimento.StatusAtendimento.valueOf(status));
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
