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

    /**
     * Lista todas las categorías registradas
     */
    public List<Categoria> listar() {
        return repo.findAll();
    }

    /**
     * Registra una nueva categoría con validaciones
     * - Nombre requerido y no vacío
     */
    public Categoria guardar(Categoria categoria) {
        validarCategoria(categoria);
        categoria.setFechaCreacion(LocalDateTime.now());
        categoria.setFechaActualizacion(LocalDateTime.now());
        return repo.save(categoria);
    }

    /**
     * Obtiene una categoría por su ID
     */
    public Categoria obtener(Long id) {
        if (id == null || id <= 0) {
            return null;
        }
        return repo.findById(id).orElse(null);
    }

    /**
     * Actualiza una categoría existente
     */
    public Categoria actualizar(Long id, Categoria nuevo) {
        Categoria c = obtener(id);
        if (c == null) {
            return null;
        }

        if (nuevo.getNombre() != null && !nuevo.getNombre().trim().isEmpty()) {
            c.setNombre(nuevo.getNombre().trim());
        }

        if (nuevo.getDescripcion() != null) {
            c.setDescripcion(nuevo.getDescripcion());
        }

        c.setFechaActualizacion(LocalDateTime.now());
        return repo.save(c);
    }

    /**
     * Elimina una categoría por su ID
     */
    public void eliminar(Long id) {
        if (repo.existsById(id)) {
            repo.deleteById(id);
        }
    }

    /**
     * Valida los datos básicos de una categoría
     */
    private void validarCategoria(Categoria categoria) {
        if (categoria == null) {
            throw new IllegalArgumentException("La categoría no puede ser nula");
        }

        if (categoria.getNombre() == null || categoria.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la categoría es requerido");
        }
    }
}
