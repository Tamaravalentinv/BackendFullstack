package com.example.backend_evaluacion.controller;

import com.example.backend_evaluacion.entity.Perfume;
import com.example.backend_evaluacion.service.PerfumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/perfumes")
@RequiredArgsConstructor
public class PerfumeController {

    private final PerfumeService service;

    @GetMapping
    // Público para catálogo
    public List<Perfume> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    // Público para detalle de catálogo
    public Perfume obtener(@PathVariable Long id) {
        return service.obtener(id);
    }

    @PostMapping
    public Perfume guardar(@RequestBody Perfume p) {
        return service.guardar(p);
    }

    @PutMapping("/{id}")
    public Perfume actualizar(@PathVariable Long id, @RequestBody Perfume p) {
        return service.actualizar(id, p);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }
}
