package com.example.backend_evaluacion.service;

import com.example.backend_evaluacion.entity.Usuario;
import com.example.backend_evaluacion.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService implements UserDetailsService {

    private final UsuarioRepository repo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Usuario user = repo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        return User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRol().getNombre()) // ADMIN o VENDEDOR
                .build();
    }

    public Usuario getByUsername(String username) {
        return repo.findByUsername(username).orElse(null);
    }

    public List<Usuario> listar() {
        return repo.findAll();
    }

    public Usuario guardar(Usuario usuario) {
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        usuario.setFechaCreacion(LocalDateTime.now());
        usuario.setFechaActualizacion(LocalDateTime.now());
        return repo.save(usuario);
    }

    public Usuario obtener(Long id) {
        return repo.findById(id).orElse(null);
    }

    public Usuario actualizar(Long id, Usuario nuevo) {
        Usuario u = obtener(id);
        if (u == null) return null;

        u.setUsername(nuevo.getUsername());
        u.setNombre(nuevo.getNombre());
        u.setEmail(nuevo.getEmail());
        u.setRol(nuevo.getRol());
        u.setActivo(nuevo.getActivo());
        u.setFechaActualizacion(LocalDateTime.now());
        return repo.save(u);
    }

    public void eliminar(Long id) {
        if (repo.existsById(id)) {
            repo.deleteById(id);
        }
    }
}
