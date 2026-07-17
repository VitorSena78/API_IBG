package com.Projeto_IBG.demo.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class AtendimentoRequestDTO {
    private Integer pacienteId;
    private Integer especialidadeId;
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

    private String avaliacaoMedica;
    private String diagnostico;
    private String condutas;
    private String observacoesMedicas;
}
