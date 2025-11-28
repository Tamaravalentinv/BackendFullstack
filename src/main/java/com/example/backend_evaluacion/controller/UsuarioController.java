package com.example.backend_evaluacion.controller;

import com.example.backend_evaluacion.entity.Usuario;
import com.example.backend_evaluacion.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<Usuario> listar() {
        return usuarioService.listar();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Usuario crear(@RequestBody Usuario usuario) {
        return usuarioService.guardar(usuario);
    }

    @GetMapping("/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public Usuario obtener(@PathVariable String username) {
        return usuarioService.getByUsername(username);
    }
}