package com.Projeto_IBG.demo.controllers;

import com.Projeto_IBG.demo.dto.ApiResponse;
import com.Projeto_IBG.demo.dto.UsuarioRequestDTO;
import com.Projeto_IBG.demo.dto.UsuarioResponseDTO;
import com.Projeto_IBG.demo.model.Especialidade;
import com.Projeto_IBG.demo.model.Usuario;
import com.Projeto_IBG.demo.repositories.EspecialidadeRepository;
import com.Projeto_IBG.demo.repositories.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;
    private final EspecialidadeRepository especialidadeRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioController(UsuarioRepository usuarioRepository,
                             EspecialidadeRepository especialidadeRepository,
                             PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.especialidadeRepository = especialidadeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    private UsuarioResponseDTO toDTO(Usuario u) {
        UsuarioResponseDTO dto = new UsuarioResponseDTO();
        dto.setId(u.getId());
        dto.setNome(u.getNome());
        dto.setEmail(u.getEmail());
        dto.setRole(u.getRole().name());
        dto.setAtivo(u.getAtivo());
        if (u.getEspecialidade() != null) {
            dto.setEspecialidadeId(u.getEspecialidade().getId());
            dto.setEspecialidadeNome(u.getEspecialidade().getNome());
        }
        return dto;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<UsuarioResponseDTO>>> listarTodos() {
        try {
            List<UsuarioResponseDTO> usuarios = usuarioRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
            return ResponseEntity.ok(ApiResponse.success(usuarios, "Usuários encontrados"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Erro ao listar usuários", e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UsuarioResponseDTO>> buscarPorId(@PathVariable Integer id) {
        try {
            Usuario u = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
            return ResponseEntity.ok(ApiResponse.success(toDTO(u), "Usuário encontrado"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error("Usuário não encontrado", e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<UsuarioResponseDTO>> criar(@Valid @RequestBody UsuarioRequestDTO request) {
        try {
            if (usuarioRepository.existsByEmail(request.getEmail())) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ApiResponse.error("Email já cadastrado"));
            }
            Usuario usuario = new Usuario();
            usuario.setNome(request.getNome());
            usuario.setEmail(request.getEmail());
            usuario.setSenha(passwordEncoder.encode(request.getSenha()));
            usuario.setRole(Usuario.Role.valueOf(request.getRole()));
            usuario.setAtivo(true);
            if (request.getEspecialidadeId() != null) {
                Especialidade esp = especialidadeRepository.findById(request.getEspecialidadeId())
                    .orElseThrow(() -> new RuntimeException("Especialidade não encontrada"));
                usuario.setEspecialidade(esp);
            }
            Usuario saved = usuarioRepository.save(usuario);
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(toDTO(saved), "Usuário criado com sucesso"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("Erro ao criar usuário", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UsuarioResponseDTO>> atualizar(
            @PathVariable Integer id,
            @RequestBody UsuarioRequestDTO request) {
        try {
            Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
            usuario.setNome(request.getNome());
            usuario.setEmail(request.getEmail());
            if (request.getSenha() != null && !request.getSenha().isEmpty()) {
                usuario.setSenha(passwordEncoder.encode(request.getSenha()));
            }
            usuario.setRole(Usuario.Role.valueOf(request.getRole()));
            if (request.getEspecialidadeId() != null) {
                Especialidade esp = especialidadeRepository.findById(request.getEspecialidadeId())
                    .orElseThrow(() -> new RuntimeException("Especialidade não encontrada"));
                usuario.setEspecialidade(esp);
            } else {
                usuario.setEspecialidade(null);
            }
            Usuario saved = usuarioRepository.save(usuario);
            return ResponseEntity.ok(ApiResponse.success(toDTO(saved), "Usuário atualizado"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("Erro ao atualizar usuário", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletar(@PathVariable Integer id) {
        try {
            usuarioRepository.deleteById(id);
            return ResponseEntity.ok(ApiResponse.success("Usuário removido"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Erro ao remover usuário", e.getMessage()));
        }
    }

    @PatchMapping("/{id}/ativo")
    public ResponseEntity<ApiResponse<UsuarioResponseDTO>> toggleAtivo(@PathVariable Integer id) {
        try {
            Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
            usuario.setAtivo(!usuario.getAtivo());
            Usuario saved = usuarioRepository.save(usuario);
            return ResponseEntity.ok(ApiResponse.success(toDTO(saved), "Status do usuário alterado"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("Erro ao alterar status", e.getMessage()));
        }
    }
}
