package com.Projeto_IBG.demo.dto;

import lombok.Data;

@Data
public class UsuarioResponseDTO {
    private Integer id;
    private String nome;
    private String email;
    private String role;
    private Boolean ativo;
    private Integer especialidadeId;
    private String especialidadeNome;
}
