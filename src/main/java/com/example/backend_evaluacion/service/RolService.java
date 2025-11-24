package com.example.backend_evaluacion.service;

import com.example.backend_evaluacion.entity.Rol;
import com.example.backend_evaluacion.repository.RolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RolService {

    private final RolRepository repo;

    public List<Rol> listar() {
        return repo.findAll();
    }

    public Rol guardar(Rol rol) {
        if (rol.getNombre() == null || rol.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del rol es requerido");
        }
        
        // Validar que el nombre sea uno de los roles permitidos
        String nombre = rol.getNombre().toUpperCase();
        if (!nombre.equals("ADMIN") && !nombre.equals("VENDEDOR")) {
            throw new IllegalArgumentException("El rol solo puede ser ADMIN o VENDEDOR");
        }
        
        return repo.save(rol);
    }

    public Rol obtener(Long id) {
        if (id == null || id <= 0) {
            return null;
        }
        return repo.findById(id).orElse(null);
    }

    public Rol actualizar(Long id, Rol nuevo) {
        Rol rol = obtener(id);
        if (rol == null) {
            return null;
        }

        if (nuevo.getNombre() != null && !nuevo.getNombre().trim().isEmpty()) {
            String nombre = nuevo.getNombre().toUpperCase();
            if (!nombre.equals("ADMIN") && !nombre.equals("VENDEDOR")) {
                throw new IllegalArgumentException("El rol solo puede ser ADMIN o VENDEDOR");
            }
            rol.setNombre(nombre);
        }

        if (nuevo.getDescripcion() != null) {
            rol.setDescripcion(nuevo.getDescripcion());
        }

        return repo.save(rol);
    }

    public void eliminar(Long id) {
        if (repo.existsById(id)) {
            repo.deleteById(id);
        }
    }
}
