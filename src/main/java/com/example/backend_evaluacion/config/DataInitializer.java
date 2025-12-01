package com.example.backend_evaluacion.config;

import com.example.backend_evaluacion.entity.Rol;
import com.example.backend_evaluacion.entity.Usuario;
import com.example.backend_evaluacion.repository.RolRepository;
import com.example.backend_evaluacion.repository.UsuarioRepository;
import com.example.backend_evaluacion.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RolRepository rolRepository;
    private final UsuarioRepository usuarioRepository;
    private final UsuarioService usuarioService;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // Ensure roles exist
        Rol adminRole = rolRepository.findByNombre("ADMIN").orElseGet(() -> {
            Rol r = new Rol();
            r.setNombre("ADMIN");
            r.setDescripcion("Acceso total");
            r.setFechaCreacion(LocalDateTime.now());
            return rolRepository.save(r);
        });

        Rol vendedorRole = rolRepository.findByNombre("VENDEDOR").orElseGet(() -> {
            Rol r = new Rol();
            r.setNombre("VENDEDOR");
            r.setDescripcion("Gestión de ventas y catálogo");
            r.setFechaCreacion(LocalDateTime.now());
            return rolRepository.save(r);
        });

        Rol clienteRole = rolRepository.findByNombre("CLIENTE").orElseGet(() -> {
            Rol r = new Rol();
            r.setNombre("CLIENTE");
            r.setDescripcion("Cliente estándar");
            r.setFechaCreacion(LocalDateTime.now());
            return rolRepository.save(r);
        });

        // Create users if missing
        createUserIfMissing("admin", "admin123", "Administrador", "admin@example.com", adminRole);
        createUserIfMissing("vendedor", "vendedor123", "Vendedor", "vendedor@example.com", vendedorRole);
        createUserIfMissing("cliente", "cliente123", "Cliente", "cliente@example.com", clienteRole);
    }

    private void createUserIfMissing(String username, String password, String nombre, String email, Rol rol) {
        if (usuarioRepository.findByUsername(username).isEmpty()) {
            Usuario u = new Usuario();
            u.setUsername(username);
            u.setPassword(password); // will be encoded by usuarioService.guardar
            u.setNombre(nombre);
            u.setEmail(email);
            u.setRol(rol);
            u.setActivo(true);
            u.setFechaCreacion(LocalDateTime.now());
            u.setFechaActualizacion(LocalDateTime.now());
            try {
                usuarioService.guardar(u);
                System.out.println("Created user: " + username);
            } catch (Exception ex) {
                System.err.println("Could not create user " + username + ": " + ex.getMessage());
            }
        }
    }
}
