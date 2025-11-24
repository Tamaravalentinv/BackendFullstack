package com.example.backend_evaluacion.service;

import com.example.backend_evaluacion.entity.Perfume;
import com.example.backend_evaluacion.repository.PerfumeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PerfumeService {

    private final PerfumeRepository repo;

    public List<Perfume> listar() {
        return repo.findAll();
    }

    public Perfume guardar(Perfume p) {
        p.setFechaCreacion(LocalDateTime.now());
        p.setFechaActualizacion(LocalDateTime.now());
        return repo.save(p);
    }

    public Perfume obtener(Long id) {
        return repo.findById(id).orElse(null);
    }

    public Perfume actualizar(Long id, Perfume nuevo) {
        Perfume p = obtener(id);
        if (p == null) return null;

        p.setNombre(nuevo.getNombre());
        p.setPrecio(nuevo.getPrecio());
        p.setStock(nuevo.getStock());
        p.setDescripcion(nuevo.getDescripcion());
        p.setTamanio(nuevo.getTamanio());
        p.setTipo(nuevo.getTipo());
        p.setMarca(nuevo.getMarca());
        p.setCategoria(nuevo.getCategoria());
        p.setFechaActualizacion(LocalDateTime.now());
        return repo.save(p);
    }

    public void eliminar(Long id) {
        if (repo.existsById(id)) {
            repo.deleteById(id);
        }
    }
}
