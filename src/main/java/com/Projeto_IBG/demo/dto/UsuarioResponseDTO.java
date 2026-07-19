package com.Projeto_IBG.demo.dto;

import lombok.Data;
import java.util.List;

@Data
public class UsuarioResponseDTO {
    private Integer id;
    private String nome;
    private String email;
    private String role;
    private Boolean ativo;
    private List<EspecialidadeInfo> especialidades;

    @Data
    @lombok.AllArgsConstructor
    public static class EspecialidadeInfo {
        private Integer id;
        private String nome;
    }
}
