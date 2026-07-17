package com.Projeto_IBG.demo.configs;

import com.Projeto_IBG.demo.security.AuthEntryPointJwt;
import com.Projeto_IBG.demo.security.AuthTokenFilter;
import com.Projeto_IBG.demo.security.UserDetailsServiceImpl;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final AuthEntryPointJwt authEntryPoint;
    private final AuthTokenFilter authTokenFilter;
    private final UserDetailsServiceImpl userDetailsService;

    public SecurityConfig(AuthEntryPointJwt authEntryPoint, AuthTokenFilter authTokenFilter,
                          UserDetailsServiceImpl userDetailsService) {
        this.authEntryPoint = authEntryPoint;
        this.authTokenFilter = authTokenFilter;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(request -> {
                var corsConfig = new org.springframework.web.cors.CorsConfiguration();
                corsConfig.addAllowedOrigin("*");
                corsConfig.addAllowedMethod("*");
                corsConfig.addAllowedHeader("*");
                return corsConfig;
            }))
            .csrf(csrf -> csrf.disable())
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint(authEntryPoint)
                .accessDeniedHandler(accessDeniedHandler())
            )
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/health").permitAll()
                .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                .requestMatchers("/ws/**").permitAll()
                .requestMatchers("/h2-console/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/pacientes/**").hasAnyRole("ADMIN", "RECEPCIONISTA", "ENFERMEIRA", "MEDICO")
                .requestMatchers(HttpMethod.POST, "/api/pacientes/**").hasAnyRole("ADMIN", "RECEPCIONISTA")
                .requestMatchers(HttpMethod.PUT, "/api/pacientes/**").hasAnyRole("ADMIN", "RECEPCIONISTA")
                .requestMatchers(HttpMethod.DELETE, "/api/pacientes/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/especialidades/**").hasAnyRole("ADMIN", "RECEPCIONISTA", "ENFERMEIRA", "MEDICO")
                .requestMatchers(HttpMethod.POST, "/api/especialidades/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/especialidades/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/especialidades/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/atendimentos/*/triagem").hasAnyRole("ADMIN", "ENFERMEIRA")
                .requestMatchers(HttpMethod.GET, "/api/atendimentos/*/triagem").hasAnyRole("ADMIN", "ENFERMEIRA")
                .requestMatchers(HttpMethod.PUT, "/api/atendimentos/*/consulta").hasAnyRole("ADMIN", "MEDICO")
                .requestMatchers(HttpMethod.GET, "/api/atendimentos/*/consulta").hasAnyRole("ADMIN", "MEDICO")
                .requestMatchers("/api/atendimentos/**").authenticated()
                .requestMatchers("/api/usuarios/**").hasRole("ADMIN")
                .requestMatchers("/api/relatorios/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .headers(headers -> headers.frameOptions(fo -> fo.disable()))
            .addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            var mapper = new ObjectMapper();
            mapper.writeValue(response.getOutputStream(), Map.of(
                "success", false,
                "error", "Acesso negado",
                "message", accessDeniedException.getMessage()
            ));
        };
    }
}
