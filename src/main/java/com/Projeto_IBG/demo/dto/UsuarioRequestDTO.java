package com.Projeto_IBG.demo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.util.List;

@Data
public class UsuarioRequestDTO {
    @NotBlank
    private String nome;
    @NotBlank
    private String email;
    @NotBlank
    private String senha;
    @NotBlank
    private String role;
    private List<Integer> especialidadeIds;
}
