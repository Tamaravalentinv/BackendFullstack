package com.example.backend_evaluacion.service;

import com.example.backend_evaluacion.entity.Cliente;
import com.example.backend_evaluacion.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository repo;
    private static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@(.+)$";

    /**
     * Lista todos los clientes registrados
     */
    public List<Cliente> listar() {
        return repo.findAll();
    }

    /**
     * Registra un nuevo cliente con validaciones
     * - Nombre y apellido requeridos
     * - Email válido
     * - Teléfono requerido
     * - Dirección y ciudad requeridas
     */
    public Cliente guardar(Cliente cliente) {
        validarCliente(cliente);
        cliente.setFechaRegistro(LocalDateTime.now());
        cliente.setFechaActualizacion(LocalDateTime.now());
        return repo.save(cliente);
    }

    /**
     * Obtiene un cliente por su ID
     */
    public Cliente obtener(Long id) {
        if (id == null || id <= 0) {
            return null;
        }
        return repo.findById(id).orElse(null);
    }

    /**
     * Actualiza los datos de un cliente
     */
    public Cliente actualizar(Long id, Cliente nuevo) {
        Cliente c = obtener(id);
        if (c == null) {
            return null;
        }

        if (nuevo.getNombre() != null && !nuevo.getNombre().trim().isEmpty()) {
            c.setNombre(nuevo.getNombre().trim());
        }

        if (nuevo.getApellido() != null && !nuevo.getApellido().trim().isEmpty()) {
            c.setApellido(nuevo.getApellido().trim());
        }

        if (nuevo.getEmail() != null && !nuevo.getEmail().trim().isEmpty()) {
            if (!esEmailValido(nuevo.getEmail())) {
                throw new IllegalArgumentException("El email no es válido");
            }
            c.setEmail(nuevo.getEmail().trim());
        }

        if (nuevo.getTelefono() != null && !nuevo.getTelefono().trim().isEmpty()) {
            c.setTelefono(nuevo.getTelefono().trim());
        }

        if (nuevo.getDireccion() != null && !nuevo.getDireccion().trim().isEmpty()) {
            c.setDireccion(nuevo.getDireccion().trim());
        }

        if (nuevo.getCiudad() != null && !nuevo.getCiudad().trim().isEmpty()) {
            c.setCiudad(nuevo.getCiudad().trim());
        }

        c.setFechaActualizacion(LocalDateTime.now());
        return repo.save(c);
    }

    /**
     * Elimina un cliente por su ID
     */
    public void eliminar(Long id) {
        if (repo.existsById(id)) {
            repo.deleteById(id);
        }
    }

    /**
     * Valida los datos básicos de un cliente
     */
    private void validarCliente(Cliente cliente) {
        if (cliente == null) {
            throw new IllegalArgumentException("El cliente no puede ser nulo");
        }

        if (cliente.getNombre() == null || cliente.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del cliente es requerido");
        }

        if (cliente.getApellido() == null || cliente.getApellido().trim().isEmpty()) {
            throw new IllegalArgumentException("El apellido del cliente es requerido");
        }

        if (cliente.getEmail() == null || cliente.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("El email es requerido");
        }

        if (!esEmailValido(cliente.getEmail())) {
            throw new IllegalArgumentException("El email no es válido");
        }

        if (cliente.getTelefono() == null || cliente.getTelefono().trim().isEmpty()) {
            throw new IllegalArgumentException("El teléfono es requerido");
        }

        if (cliente.getDireccion() == null || cliente.getDireccion().trim().isEmpty()) {
            throw new IllegalArgumentException("La dirección es requerida");
        }

        if (cliente.getCiudad() == null || cliente.getCiudad().trim().isEmpty()) {
            throw new IllegalArgumentException("La ciudad es requerida");
        }
    }

    /**
     * Valida el formato del email
     */
    private boolean esEmailValido(String email) {
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        return pattern.matcher(email).matches();
    }
}
