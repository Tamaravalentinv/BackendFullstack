package com.example.backend_evaluacion.service;

import com.example.backend_evaluacion.entity.Categoria;
import com.example.backend_evaluacion.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository repo;

    public List<Categoria> listar() {
        return repo.findAll();
    }

    public Categoria guardar(Categoria categoria) {
        categoria.setFechaCreacion(LocalDateTime.now());
        categoria.setFechaActualizacion(LocalDateTime.now());
        return repo.save(categoria);
    }

    public Categoria obtener(Long id) {
        return repo.findById(id).orElse(null);
    }

    public Categoria actualizar(Long id, Categoria nuevo) {
        Categoria c = obtener(id);
        if (c == null) return null;

        c.setNombre(nuevo.getNombre());
        c.setDescripcion(nuevo.getDescripcion());
        c.setFechaActualizacion(LocalDateTime.now());
        return repo.save(c);
    }

    public void eliminar(Long id) {
        if (repo.existsById(id)) {
            repo.deleteById(id);
        }
    }
}
