package com.example.backend_evaluacion.service;

import com.example.backend_evaluacion.entity.Usuario;
import com.example.backend_evaluacion.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.Collections;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UsuarioService implements UserDetailsService {

    private final UsuarioRepository repo;
    private final PasswordEncoder passwordEncoder;
    private static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@(.+)$";

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario user = repo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        // Build UserDetails with explicit authorities to avoid role-mapping inconsistencies
        var authority = new SimpleGrantedAuthority("ROLE_" + user.getRol().getNombre());
        return User.builder()
            .username(user.getUsername())
            .password(user.getPassword())
            .authorities(Collections.singletonList(authority))
            .build();
    }

    public Usuario getByUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            return null;
        }
        return repo.findByUsername(username).orElse(null);
    }

    /**
     * Lista todos los usuarios del sistema
     */
    public List<Usuario> listar() {
        return repo.findAll();
    }

    /**
     * Registra un nuevo usuario con validaciones de seguridad
     * - Username requerido y único
     * - Password requerido y codificado con BCrypt
     * - Email válido
     * - Rol requerido
     */
    public Usuario guardar(Usuario usuario) {
        validarUsuario(usuario);
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        usuario.setActivo(true);
        usuario.setFechaCreacion(LocalDateTime.now());
        usuario.setFechaActualizacion(LocalDateTime.now());
        return repo.save(usuario);
    }

    /**
     * Obtiene un usuario por su ID
     */
    public Usuario obtener(Long id) {
        if (id == null || id <= 0) {
            return null;
        }
        return repo.findById(id).orElse(null);
    }

    /**
     * Actualiza los datos de un usuario (sin cambiar contraseña)
     */
    public Usuario actualizar(Long id, Usuario nuevo) {
        Usuario u = obtener(id);
        if (u == null) {
            return null;
        }

        if (nuevo.getNombre() != null && !nuevo.getNombre().trim().isEmpty()) {
            u.setNombre(nuevo.getNombre().trim());
        }

        if (nuevo.getEmail() != null && !nuevo.getEmail().trim().isEmpty()) {
            if (!esEmailValido(nuevo.getEmail())) {
                throw new IllegalArgumentException("El email no es válido");
            }
            u.setEmail(nuevo.getEmail().trim());
        }

        if (nuevo.getRol() != null) {
            u.setRol(nuevo.getRol());
        }

        if (nuevo.getActivo() != null) {
            u.setActivo(nuevo.getActivo());
        }

        u.setFechaActualizacion(LocalDateTime.now());
        return repo.save(u);
    }

    /**
     * Cambia la contraseña de un usuario
     */
    public Usuario cambiarPassword(Long id, String passwordActual, String passwordNueva) {
        Usuario u = obtener(id);
        if (u == null) {
            throw new IllegalArgumentException("Usuario no encontrado");
        }

        if (!passwordEncoder.matches(passwordActual, u.getPassword())) {
            throw new IllegalArgumentException("La contraseña actual es incorrecta");
        }

        if (passwordNueva == null || passwordNueva.trim().length() < 6) {
            throw new IllegalArgumentException("La nueva contraseña debe tener al menos 6 caracteres");
        }

        u.setPassword(passwordEncoder.encode(passwordNueva));
        u.setFechaActualizacion(LocalDateTime.now());
        return repo.save(u);
    }

    /**
     * Elimina un usuario por su ID
     */
    public void eliminar(Long id) {
        if (repo.existsById(id)) {
            repo.deleteById(id);
        }
    }

    /**
     * Valida los datos básicos de un usuario
     */
    private void validarUsuario(Usuario usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException("El usuario no puede ser nulo");
        }

        if (usuario.getUsername() == null || usuario.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("El username es requerido");
        }

        if (usuario.getPassword() == null || usuario.getPassword().trim().length() < 6) {
            throw new IllegalArgumentException("La contraseña debe tener al menos 6 caracteres");
        }

        if (usuario.getNombre() == null || usuario.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es requerido");
        }

        if (usuario.getEmail() == null || usuario.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("El email es requerido");
        }

        if (!esEmailValido(usuario.getEmail())) {
            throw new IllegalArgumentException("El email no es válido");
        }

        if (usuario.getRol() == null) {
            throw new IllegalArgumentException("El rol es requerido");
        }
    }

    /**
     * Valida el formato del email
     */
    private boolean esEmailValido(String email) {
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        return pattern.matcher(email).matches();
    }
}
