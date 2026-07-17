package com.Projeto_IBG.demo.controllers;

import com.Projeto_IBG.demo.dto.*;
import com.Projeto_IBG.demo.model.Usuario;
import com.Projeto_IBG.demo.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponseDTO>> login(@Valid @RequestBody LoginRequestDTO request) {
        try {
            LoginResponseDTO response = authService.login(request);
            return ResponseEntity.ok(ApiResponse.success(response, "Login realizado com sucesso"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error("Erro de autenticação", e.getMessage()));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UsuarioResponseDTO>> register(@Valid @RequestBody UsuarioRequestDTO request) {
        try {
            Usuario usuario = new Usuario();
            usuario.setNome(request.getNome());
            usuario.setEmail(request.getEmail());
            usuario.setSenha(request.getSenha());
            usuario.setRole(Usuario.Role.valueOf(request.getRole()));

            Usuario saved = authService.criarUsuario(usuario);

            UsuarioResponseDTO response = new UsuarioResponseDTO();
            response.setId(saved.getId());
            response.setNome(saved.getNome());
            response.setEmail(saved.getEmail());
            response.setRole(saved.getRole().name());
            response.setAtivo(saved.getAtivo());

            return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Usuário criado com sucesso"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("Erro ao criar usuário", e.getMessage()));
        }
    }
}
