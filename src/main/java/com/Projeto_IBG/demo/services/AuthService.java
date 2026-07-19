package com.Projeto_IBG.demo.services;

import com.Projeto_IBG.demo.dto.LoginRequestDTO;
import com.Projeto_IBG.demo.dto.LoginResponseDTO;
import com.Projeto_IBG.demo.model.Usuario;
import com.Projeto_IBG.demo.repositories.UsuarioRepository;
import com.Projeto_IBG.demo.security.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository usuarioRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public AuthService(AuthenticationManager authenticationManager,
                       UsuarioRepository usuarioRepository,
                       JwtUtil jwtUtil,
                       PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.usuarioRepository = usuarioRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    public LoginResponseDTO login(LoginRequestDTO request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getSenha()));

        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        String token = jwtUtil.generateToken(usuario.getEmail(), usuario.getRole().name(), usuario.getId());

        List<LoginResponseDTO.EspecialidadeInfo> especialidades;
        if (usuario.getEspecialidades() != null) {
            especialidades = usuario.getEspecialidades().stream()
                .map(esp -> new LoginResponseDTO.EspecialidadeInfo(esp.getId(), esp.getNome()))
                .collect(Collectors.toList());
        } else {
            especialidades = Collections.emptyList();
        }

        return new LoginResponseDTO(token, "Bearer", usuario.getId(), usuario.getNome(),
                usuario.getEmail(), usuario.getRole().name(), especialidades);
    }

    public Usuario criarUsuario(Usuario usuario) {
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new RuntimeException("Email já cadastrado");
        }
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        usuario.setAtivo(true);
        return usuarioRepository.save(usuario);
    }
}
