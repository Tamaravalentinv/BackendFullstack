package com.example.backend_evaluacion.controller;

import com.example.backend_evaluacion.entity.Perfume;
import com.example.backend_evaluacion.service.PerfumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Alias controller to map generic product endpoints expected by frontend
 * to underlying Perfume domain.
 */
@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductAliasController {

    private final PerfumeService perfumeService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','VENDEDOR')")
    public List<Perfume> listar() {
        return perfumeService.listar();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','VENDEDOR')")
    public Perfume obtener(@PathVariable Long id) {
        return perfumeService.obtener(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Perfume guardar(@RequestBody Perfume p) {
        return perfumeService.guardar(p);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Perfume actualizar(@PathVariable Long id, @RequestBody Perfume p) {
        return perfumeService.actualizar(id, p);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void eliminar(@PathVariable Long id) {
        perfumeService.eliminar(id);
    }
}