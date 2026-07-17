package com.Projeto_IBG.demo.dto;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class AtendimentoResponseDTO {
    private Integer id;
    private Integer pacienteId;
    private String pacienteNome;
    private String pacienteCpf;
    private String pacienteSus;
    private Integer especialidadeId;
    private String especialidadeNome;
    private Boolean triagemObrigatoria;
    private String status;
    private LocalDate dataAtendimento;

    private String paXMmhg;
    private Float fcBpm;
    private Float frIbpm;
    private Float temperaturaC;
    private Float hgtMgld;
    private Float spo2;
    private Float peso;
    private Float altura;
    private Float imc;
    private String observacoesEnfermagem;
    private String enfermeiraNome;
    private LocalDateTime triagemRealizadaEm;

    private String avaliacaoMedica;
    private String diagnostico;
    private String condutas;
    private String observacoesMedicas;
    private String medicoNome;
    private LocalDateTime consultaRealizadaEm;

    private String recepcionistaNome;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
