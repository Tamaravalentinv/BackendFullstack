package com.example.backend_evaluacion.controller;

import com.example.backend_evaluacion.dto.LoginDTO;
import com.example.backend_evaluacion.entity.Usuario;
import com.example.backend_evaluacion.security.JwtUtil;
import com.example.backend_evaluacion.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioService usuarioService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody LoginDTO login) {

        Usuario usuario = usuarioService.getByUsername(login.getUsername());

        if (usuario == null) {
            return Map.of("error", "Usuario no encontrado");
        }

        if (!passwordEncoder.matches(login.getPassword(), usuario.getPassword())) {
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
}
