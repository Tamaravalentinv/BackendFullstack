package com.example.backend_evaluacion.service;

import com.example.backend_evaluacion.entity.Cliente;
import com.example.backend_evaluacion.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository repo;

    public List<Cliente> listar() {
        return repo.findAll();
    }

    public Cliente guardar(Cliente cliente) {
        cliente.setFechaRegistro(LocalDateTime.now());
        cliente.setFechaActualizacion(LocalDateTime.now());
        return repo.save(cliente);
    }

    public Cliente obtener(Long id) {
        return repo.findById(id).orElse(null);
    }

    public Cliente actualizar(Long id, Cliente nuevo) {
        Cliente c = obtener(id);
        if (c == null) return null;

        c.setNombre(nuevo.getNombre());
        c.setApellido(nuevo.getApellido());
        c.setEmail(nuevo.getEmail());
        c.setTelefono(nuevo.getTelefono());
        c.setDireccion(nuevo.getDireccion());
        c.setCiudad(nuevo.getCiudad());
        c.setFechaActualizacion(LocalDateTime.now());
        return repo.save(c);
    }

    public void eliminar(Long id) {
        if (repo.existsById(id)) {
            repo.deleteById(id);
        }
    }
}
