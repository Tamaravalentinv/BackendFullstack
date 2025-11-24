public package com.tienda.backend.controller;

import com.tienda.backend.entity.Venta;
import com.tienda.backend.service.VentaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ventas")
@RequiredArgsConstructor
public class VentaController {

    private final VentaService service;

    @GetMapping
    public List<Venta> listar() {
        return service.listar();
    }

    @PostMapping
    public Venta registrar(@RequestBody Venta v) {
        return service.registrar(v);
    }
}

