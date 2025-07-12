package com.Projeto_IBG.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Paciente")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Paciente {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 255, message = "Nome deve ter no máximo 255 caracteres")
    private String nome;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;
    
    @Min(value = 0, message = "Idade deve ser positiva")
    @Max(value = 150, message = "Idade deve ser menor que 150")
    private Integer idade;
    
    @Size(max = 255, message = "Nome da mãe deve ter no máximo 255 caracteres")
    @Column(name = "nome_da_mae")
    private String nomeDaMae;
    
    @Size(max = 45, message = "CPF deve ter no máximo 45 caracteres")
    private String cpf;
    
    @Size(max = 45, message = "SUS deve ter no máximo 45 caracteres")
    private String sus;
    
    @Size(max = 45, message = "Telefone deve ter no máximo 45 caracteres")
    private String telefone;
    
    @Column(columnDefinition = "TEXT")
    private String endereço;
    
    @Size(max = 20, message = "PA deve ter no máximo 20 caracteres")
    @Column(name = "pa_x_mmhg")
    private String paXMmhg;
    
    @DecimalMin(value = "0.0", message = "FC deve ser positiva")
    @Column(name = "fc_bpm")
    private Float fcBpm;
    
    @DecimalMin(value = "0.0", message = "FR deve ser positiva")
    @Column(name = "fr_ibpm")
    private Float frIbpm;
    
    @DecimalMin(value = "0.0", message = "Temperatura deve ser positiva")
    @Column(name = "temperatura_c")
    private Float temperaturaC;
    
    @DecimalMin(value = "0.0", message = "HGT deve ser positiva")
    @Column(name = "hgt_mgld")
    private Float hgtMgld;
    
    @DecimalMin(value = "0.0", message = "SPO2 deve ser positiva")
    @DecimalMax(value = "100.0", message = "SPO2 deve ser menor ou igual a 100")
    private Float spo2;
    
    @DecimalMin(value = "0.0", message = "Peso deve ser positivo")
    private Float peso;
    
    @DecimalMin(value = "0.0", message = "Altura deve ser positiva")
    private Float altura;
    
    @DecimalMin(value = "0.0", message = "IMC deve ser positivo")
    private Float imc;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @OneToMany(mappedBy = "paciente", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<PacienteEspecialidade> especialidades;
    
    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        calcularImc();
    }
    
    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
        calcularImc();
    }
    
    private void calcularImc() {
        if (peso != null && altura != null && altura > 0) {
            imc = peso / (altura * altura);
        }
    }
}
