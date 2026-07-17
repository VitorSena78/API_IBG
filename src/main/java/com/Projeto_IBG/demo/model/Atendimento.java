package com.Projeto_IBG.demo.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "atendimentos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Atendimento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "INTEGER UNSIGNED")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "especialidade_id", nullable = false)
    private Especialidade especialidade;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusAtendimento status = StatusAtendimento.AGUARDANDO_TRIAGEM;

    @Column(name = "data_atendimento", nullable = false)
    private LocalDate dataAtendimento = LocalDate.now();

    @Column(name = "pa_x_mmhg", length = 20)
    private String paXMmhg;

    @Column(name = "fc_bpm")
    private Float fcBpm;

    @Column(name = "fr_ibpm")
    private Float frIbpm;

    @Column(name = "temperatura_c")
    private Float temperaturaC;

    @Column(name = "hgt_mgld")
    private Float hgtMgld;

    private Float spo2;
    private Float peso;
    private Float altura;
    private Float imc;

    @Column(columnDefinition = "TEXT")
    private String observacoesEnfermagem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enfermeira_id")
    private Usuario enfermeira;

    @Column(name = "triagem_realizada_em")
    private LocalDateTime triagemRealizadaEm;

    @Column(columnDefinition = "TEXT")
    private String avaliacaoMedica;

    @Column(columnDefinition = "TEXT")
    private String diagnostico;

    @Column(columnDefinition = "TEXT")
    private String condutas;

    @Column(columnDefinition = "TEXT")
    private String observacoesMedicas;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medico_id")
    private Usuario medico;

    @Column(name = "consulta_realizada_em")
    private LocalDateTime consultaRealizadaEm;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recepcionista_id")
    private Usuario recepcionista;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (dataAtendimento == null) {
            dataAtendimento = LocalDate.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum StatusAtendimento {
        AGUARDANDO_TRIAGEM,
        EM_TRIAGEM,
        AGUARDANDO_CONSULTA,
        EM_CONSULTA,
        FINALIZADO,
        CANCELADO
    }
}
