package com.example.backend_evaluacion.service;

import com.example.backend_evaluacion.entity.Perfume;
import com.example.backend_evaluacion.entity.Marca;
import com.example.backend_evaluacion.entity.Categoria;
import com.example.backend_evaluacion.repository.PerfumeRepository;
import com.example.backend_evaluacion.repository.MarcaRepository;
import com.example.backend_evaluacion.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PerfumeService {

    private final PerfumeRepository repo;
    private final MarcaRepository marcaRepo;
    private final CategoriaRepository categoriaRepo;

    /**
     * Lista todos los perfumes disponibles
     */
    public List<Perfume> listar() {
        return repo.findAll();
    }

    /**
     * Crea un nuevo perfume con validaciones de negocio
     * - Nombre requerido y no vacio
     * - Precio > 0
     * - Stock >= 0
     * - Marca requerida
     * - Categoria requerida
     */
    public Perfume guardar(Perfume p) {
        validarPerfume(p);
        
        // Si marca solo tiene ID, buscarla en la BD
        if (p.getMarca() != null && p.getMarca().getId() != null && p.getMarca().getNombre() == null) {
            p.setMarca(marcaRepo.findById(p.getMarca().getId()).orElseThrow(() -> 
                new IllegalArgumentException("Marca no encontrada")));
        }
        
        // Si categoría solo tiene ID, buscarla en la BD
        if (p.getCategoria() != null && p.getCategoria().getId() != null && p.getCategoria().getNombre() == null) {
            p.setCategoria(categoriaRepo.findById(p.getCategoria().getId()).orElseThrow(() -> 
                new IllegalArgumentException("Categoría no encontrada")));
        }
        
        p.setFechaCreacion(LocalDateTime.now());
        p.setFechaActualizacion(LocalDateTime.now());
        return repo.save(p);
    }

    /**
     * Obtiene un perfume por su ID
     */
    public Perfume obtener(Long id) {
        if (id == null || id <= 0) {
            return null;
        }
        return repo.findById(id).orElse(null);
    }

    /**
     * Actualiza un perfume existente
     */
    public Perfume actualizar(Long id, Perfume nuevo) {
        Perfume p = obtener(id);
        if (p == null) {
            return null;
        }

        // Validar nombre si viene en la actualización
        if (nuevo.getNombre() != null && !nuevo.getNombre().trim().isEmpty()) {
            p.setNombre(nuevo.getNombre().trim());
        }

        // Validar precio
        if (nuevo.getPrecio() != null && nuevo.getPrecio() > 0) {
            p.setPrecio(nuevo.getPrecio());
        } else if (nuevo.getPrecio() != null) {
            throw new IllegalArgumentException("El precio debe ser mayor a 0");
        }

        // Validar stock
        if (nuevo.getStock() != null && nuevo.getStock() >= 0) {
            p.setStock(nuevo.getStock());
        } else if (nuevo.getStock() != null) {
            throw new IllegalArgumentException("El stock no puede ser negativo");
        }

        if (nuevo.getDescripcion() != null) {
            p.setDescripcion(nuevo.getDescripcion());
        }

        if (nuevo.getTamanio() != null) {
            p.setTamanio(nuevo.getTamanio());
        }

        if (nuevo.getTipo() != null) {
            p.setTipo(nuevo.getTipo());
        }

        if (nuevo.getMarca() != null) {
            // Si marca solo tiene ID, buscarla en la BD
            if (nuevo.getMarca().getId() != null && nuevo.getMarca().getNombre() == null) {
                p.setMarca(marcaRepo.findById(nuevo.getMarca().getId()).orElseThrow(() -> 
                    new IllegalArgumentException("Marca no encontrada")));
            } else {
                p.setMarca(nuevo.getMarca());
            }
        }

        if (nuevo.getCategoria() != null) {
            // Si categoría solo tiene ID, buscarla en la BD
            if (nuevo.getCategoria().getId() != null && nuevo.getCategoria().getNombre() == null) {
                p.setCategoria(categoriaRepo.findById(nuevo.getCategoria().getId()).orElseThrow(() -> 
                    new IllegalArgumentException("Categoría no encontrada")));
            } else {
                p.setCategoria(nuevo.getCategoria());
            }
        }

        p.setFechaActualizacion(LocalDateTime.now());
        return repo.save(p);
    }

    /**
     * Elimina un perfume por su ID
     */
    public void eliminar(Long id) {
        if (repo.existsById(id)) {
            repo.deleteById(id);
        }
    }

    /**
     * Valida los datos básicos de un perfume
     */
    private void validarPerfume(Perfume p) {
        if (p == null) {
            throw new IllegalArgumentException("El perfume no puede ser nulo");
        }

        if (p.getNombre() == null || p.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del perfume es requerido");
        }

        if (p.getPrecio() == null || p.getPrecio() <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor a 0");
        }

        if (p.getStock() == null || p.getStock() < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo");
        }

        if (p.getMarca() == null) {
            throw new IllegalArgumentException("La marca es requerida");
        }

        if (p.getCategoria() == null) {
            throw new IllegalArgumentException("La categoria es requerida");
        }
    }
}
