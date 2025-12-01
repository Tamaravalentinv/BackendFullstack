package com.example.backend_evaluacion.controller;

import com.example.backend_evaluacion.dto.LoginDTO;
import com.example.backend_evaluacion.entity.Usuario;
import com.example.backend_evaluacion.security.JwtUtil;
import com.example.backend_evaluacion.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final UsuarioService usuarioService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody LoginDTO login) {

        log.info("Login attempt for username={}", login.getUsername());

        Usuario usuario = usuarioService.getByUsername(login.getUsername());

        if (usuario == null) {
            log.warn("Login failed: usuario no encontrado -> {}", login.getUsername());
            return Map.of("error", "Usuario no encontrado");
        }

        if (!passwordEncoder.matches(login.getPassword(), usuario.getPassword())) {
            log.warn("Login failed: contraseña incorrecta -> {}", login.getUsername());
            return Map.of("error", "Contraseña incorrecta");
        }

        String token = jwtUtil.generarToken(
                usuario.getUsername(),
                usuario.getRol().getNombre()
        );

        return Map.of(
                "token", token,
                "usuario", usuario.getUsername(),
                "rol", usuario.getRol().getNombre()
        );
    }

    @GetMapping("/validate")
    public Map<String, Object> validateToken(@RequestHeader(name = "Authorization", required = false) String auth) {
        if (auth == null || !auth.startsWith("Bearer ")) {
            return Map.of("valid", false, "error", "Authorization header missing or malformed");
        }
        String token = auth.substring(7);
        try {
            var claims = jwtUtil.obtenerClaims(token);
            return Map.of("valid", true, "usuario", claims.getSubject(), "rol", claims.get("rol"));
        } catch (Exception ex) {
            log.warn("Token validation failed: {}", ex.getMessage());
            return Map.of("valid", false, "error", "Token inválido");
        }
    }

    @GetMapping("/validate-token")
    public Map<String, Object> validateTokenParam(@RequestParam(name = "token", required = false) String token) {
        if (token == null || token.trim().isEmpty()) {
            return Map.of("valid", false, "error", "token query param missing");
        }
        try {
            var claims = jwtUtil.obtenerClaims(token);
            return Map.of("valid", true, "usuario", claims.getSubject(), "rol", claims.get("rol"));
        } catch (Exception ex) {
            log.warn("Token validation failed (param): {}", ex.getMessage());
            return Map.of("valid", false, "error", "Token inválido");
        }
    }

    @PostMapping("/validate-token")
    public Map<String, Object> validateTokenBody(@RequestBody(required = false) java.util.Map<String, String> body) {
        if (body == null || !body.containsKey("token") || body.get("token").trim().isEmpty()) {
            return Map.of("valid", false, "error", "token missing in body");
        }
        String token = body.get("token");
        try {
            var claims = jwtUtil.obtenerClaims(token);
            return Map.of("valid", true, "usuario", claims.getSubject(), "rol", claims.get("rol"));
        } catch (Exception ex) {
            log.warn("Token validation failed (body): {}", ex.getMessage());
            return Map.of("valid", false, "error", "Token inválido");
        }
    }
}
