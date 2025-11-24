package com.example.backend_evaluacion.service;

import com.example.backend_evaluacion.entity.Venta;
import com.example.backend_evaluacion.repository.VentaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VentaService {

    private final VentaRepository repo;

    public List<Venta> listar() {
        return repo.findAll();
    }

    public Venta registrar(Venta v) {
        v.setFecha(LocalDateTime.now());
        v.setFechaCreacion(LocalDateTime.now());
        v.setFechaActualizacion(LocalDateTime.now());
        return repo.save(v);
    }

    public Venta obtener(Long id) {
        return repo.findById(id).orElse(null);
    }

    public Venta actualizar(Long id, Venta nuevo) {
        Venta v = obtener(id);
        if (v == null) return null;

        v.setCliente(nuevo.getCliente());
        v.setVendedor(nuevo.getVendedor());
        v.setMontoTotal(nuevo.getMontoTotal());
        v.setEstado(nuevo.getEstado());
        v.setObservaciones(nuevo.getObservaciones());
        v.setFechaActualizacion(LocalDateTime.now());
        return repo.save(v);
    }

    public void eliminar(Long id) {
        if (repo.existsById(id)) {
            repo.deleteById(id);
        }
    }
}
