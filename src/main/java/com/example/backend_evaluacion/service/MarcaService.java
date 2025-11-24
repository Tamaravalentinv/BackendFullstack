package com.example.backend_evaluacion.service;

import com.example.backend_evaluacion.entity.Marca;
import com.example.backend_evaluacion.repository.MarcaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MarcaService {

    private final MarcaRepository repo;

    public List<Marca> listar() {
        return repo.findAll();
    }

    public Marca guardar(Marca marca) {
        marca.setFechaCreacion(LocalDateTime.now());
        marca.setFechaActualizacion(LocalDateTime.now());
        return repo.save(marca);
    }

    public Marca obtener(Long id) {
        return repo.findById(id).orElse(null);
    }

    public Marca actualizar(Long id, Marca nuevo) {
        Marca m = obtener(id);
        if (m == null) return null;

        m.setNombre(nuevo.getNombre());
        m.setDescripcion(nuevo.getDescripcion());
        m.setPais(nuevo.getPais());
        m.setFechaActualizacion(LocalDateTime.now());
        return repo.save(m);
    }

    public void eliminar(Long id) {
        if (repo.existsById(id)) {
            repo.deleteById(id);
        }
    }
}
