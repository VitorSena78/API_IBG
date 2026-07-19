package com.Projeto_IBG.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class LoginResponseDTO {
    private String token;
    private String tipo = "Bearer";
    private Integer id;
    private String nome;
    private String email;
    private String role;
    private List<EspecialidadeInfo> especialidades;

    @Data
    @AllArgsConstructor
    public static class EspecialidadeInfo {
        private Integer id;
        private String nome;
    }
}
