package com.tienda.backend.service;

import com.tienda.backend.entity.Producto;
import com.tienda.backend.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository repo;

    public List<Producto> listar() {
        return repo.findAll();
    }

    public Producto guardar(Producto p) {
        return repo.save(p);
    }

    public Producto obtener(Long id) {
        return repo.findById(id).orElse(null);
    }

    public Producto actualizar(Long id, Producto nuevo) {
        Producto p = obtener(id);
        if (p == null) return null;

        p.setNombre(nuevo.getNombre());
        p.setPrecio(nuevo.getPrecio());
        p.setStock(nuevo.getStock());
        return repo.save(p);
    }

    public void eliminar(Long id) {
        if (repo.existsById(id)) {
            repo.deleteById(id);
        }
    }
}
