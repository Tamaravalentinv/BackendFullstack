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

    /**
     * Lista todas las marcas registradas
     */
    public List<Marca> listar() {
        return repo.findAll();
    }

    /**
     * Registra una nueva marca con validaciones
     * - Nombre requerido y no vacío
     * - País requerido
     */
    public Marca guardar(Marca marca) {
        validarMarca(marca);
        marca.setFechaCreacion(LocalDateTime.now());
        marca.setFechaActualizacion(LocalDateTime.now());
        return repo.save(marca);
    }

    /**
     * Obtiene una marca por su ID
     */
    public Marca obtener(Long id) {
        if (id == null || id <= 0) {
            return null;
        }
        return repo.findById(id).orElse(null);
    }

    /**
     * Actualiza una marca existente
     */
    public Marca actualizar(Long id, Marca nuevo) {
        Marca m = obtener(id);
        if (m == null) {
            return null;
        }

        if (nuevo.getNombre() != null && !nuevo.getNombre().trim().isEmpty()) {
            m.setNombre(nuevo.getNombre().trim());
        }

        if (nuevo.getDescripcion() != null) {
            m.setDescripcion(nuevo.getDescripcion());
        }

        if (nuevo.getPais() != null && !nuevo.getPais().trim().isEmpty()) {
            m.setPais(nuevo.getPais().trim());
        }

        m.setFechaActualizacion(LocalDateTime.now());
        return repo.save(m);
    }

    /**
     * Elimina una marca por su ID
     */
    public void eliminar(Long id) {
        if (repo.existsById(id)) {
            repo.deleteById(id);
        }
    }

    /**
     * Valida los datos básicos de una marca
     */
    private void validarMarca(Marca marca) {
        if (marca == null) {
            throw new IllegalArgumentException("La marca no puede ser nula");
        }

        if (marca.getNombre() == null || marca.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la marca es requerido");
        }

        if (marca.getPais() == null || marca.getPais().trim().isEmpty()) {
            throw new IllegalArgumentException("El país es requerido");
        }
    }
}
