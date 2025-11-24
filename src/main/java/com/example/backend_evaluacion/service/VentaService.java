package com.tienda.backend.service;

import com.tienda.backend.entity.Venta;
import com.tienda.backend.repository.VentaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VentaService {

    private final VentaRepository repo;

    public List<Venta> listar() {
        return repo.findAll();
    }

    public Venta registrar(Venta v) {
        v.setFecha(LocalDate.now());
        return repo.save(v);
    }
}
