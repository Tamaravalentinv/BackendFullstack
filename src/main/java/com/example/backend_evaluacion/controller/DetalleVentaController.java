package com.example.backend_evaluacion.controller;

import com.example.backend_evaluacion.entity.DetalleVenta;
import com.example.backend_evaluacion.service.DetalleVentaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/detalles-ventas")
@RequiredArgsConstructor
public class DetalleVentaController {

    private final DetalleVentaService service;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'VENDEDOR')")
    public ResponseEntity<List<DetalleVenta>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'VENDEDOR')")
    public ResponseEntity<DetalleVenta> obtener(@PathVariable Long id) {
        DetalleVenta detalle = service.obtener(id);
        if (detalle == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(detalle);
    }

    @GetMapping("/venta/{ventaId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'VENDEDOR')")
    public ResponseEntity<List<DetalleVenta>> obtenerPorVenta(@PathVariable Long ventaId) {
        return ResponseEntity.ok(service.obtenerPorVenta(ventaId));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'VENDEDOR')")
    public ResponseEntity<?> guardar(@RequestBody DetalleVenta detalle) {
        try {
            DetalleVenta detalleGuardado = service.guardar(detalle);
            return ResponseEntity.status(HttpStatus.CREATED).body(detalleGuardado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'VENDEDOR')")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody DetalleVenta detalle) {
        try {
            DetalleVenta detalleActualizado = service.actualizar(id, detalle);
            if (detalleActualizado == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(detalleActualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            service.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}
