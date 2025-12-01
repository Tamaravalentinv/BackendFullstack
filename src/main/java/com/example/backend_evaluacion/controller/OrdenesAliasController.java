package com.example.backend_evaluacion.controller;

import com.example.backend_evaluacion.entity.Venta;
import com.example.backend_evaluacion.service.VentaService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ordenes")
@RequiredArgsConstructor
public class OrdenesAliasController {

    private final VentaService ventaService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','VENDEDOR')")
    public List<Venta> listar() {
        return ventaService.listar();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','VENDEDOR')")
    public Venta obtener(@PathVariable Long id) {
        return ventaService.obtener(id);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','VENDEDOR')")
    public Venta registrar(@RequestBody Venta v) {
        return ventaService.registrar(v);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Venta actualizar(@PathVariable Long id, @RequestBody Venta v) {
        return ventaService.actualizar(id, v);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void eliminar(@PathVariable Long id) {
        ventaService.eliminar(id);
    }
}
