package com.example.backend_evaluacion.service;

import com.example.backend_evaluacion.entity.Venta;
import com.example.backend_evaluacion.entity.Usuario;
import com.example.backend_evaluacion.entity.Cliente;
import com.example.backend_evaluacion.repository.VentaRepository;
import com.example.backend_evaluacion.repository.UsuarioRepository;
import com.example.backend_evaluacion.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VentaService {

    private final VentaRepository repo;
    private final UsuarioRepository usuarioRepository;
    private final ClienteRepository clienteRepository;

    /**
     * Lista todas las ventas registradas
     */
    public List<Venta> listar() {
        return repo.findAll();
    }

    /**
     * Registra una nueva venta con validaciones de negocio
     * - Cliente requerido
     * - Vendedor requerido
     * - Monto total > 0
     * - Estado requerido (PENDIENTE, COMPLETADA, CANCELADA)
     */
    public Venta registrar(Venta v) {
        // Completar datos faltantes desde el usuario autenticado, si aplica
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            String username = auth.getName();
            usuarioRepository.findByUsername(username).ifPresent(u -> {
                if (v.getVendedor() == null) {
                    v.setVendedor(u);
                }
                if (v.getCliente() == null) {
                    // Intentar por email
                    clienteRepository.findByEmail(u.getEmail()).ifPresent(v::setCliente);
                    // Fallback: crear cliente si no existe
                    if (v.getCliente() == null) {
                        Cliente nuevo = new Cliente();
                        String nombreCompleto = u.getNombre() != null ? u.getNombre() : u.getUsername();
                        String nombre = nombreCompleto;
                        String apellido = "N/A";
                        if (nombreCompleto.contains(" ")) {
                            String[] partes = nombreCompleto.trim().split(" ", 2);
                            nombre = partes[0];
                            apellido = partes.length > 1 ? partes[1] : "N/A";
                        }
                        nuevo.setNombre(nombre);
                        nuevo.setApellido(apellido);
                        nuevo.setEmail(u.getEmail());
                        Cliente guardado = clienteRepository.save(nuevo);
                        v.setCliente(guardado);
                    }
                }
            });
        }
        validarVenta(v);
        v.setFechaCreacion(LocalDateTime.now());
        v.setFechaActualizacion(LocalDateTime.now());
        if (v.getEstado() == null || v.getEstado().isEmpty()) {
            v.setEstado("PENDIENTE");
        }
        return repo.save(v);
    }

    /**
     * Obtiene una venta por su ID
     */
    public Venta obtener(Long id) {
        if (id == null || id <= 0) {
            return null;
        }
        return repo.findById(id).orElse(null);
    }

    /**
     * Actualiza los datos de una venta
     */
    public Venta actualizar(Long id, Venta nuevo) {
        Venta v = obtener(id);
        if (v == null) {
            return null;
        }

        if (nuevo.getCliente() != null) {
            v.setCliente(nuevo.getCliente());
        }

        if (nuevo.getVendedor() != null) {
            v.setVendedor(nuevo.getVendedor());
        }

        if (nuevo.getMontoTotal() != null && nuevo.getMontoTotal().compareTo(BigDecimal.ZERO) > 0) {
            v.setMontoTotal(nuevo.getMontoTotal());
        } else if (nuevo.getMontoTotal() != null) {
            throw new IllegalArgumentException("El monto total debe ser mayor a 0");
        }

        if (nuevo.getEstado() != null && !nuevo.getEstado().isEmpty()) {
            String estado = nuevo.getEstado().toUpperCase();
            if (!esEstadoValido(estado)) {
                throw new IllegalArgumentException("El estado debe ser PENDIENTE, COMPLETADA o CANCELADA");
            }
            v.setEstado(estado);
        }

        if (nuevo.getObservaciones() != null) {
            v.setObservaciones(nuevo.getObservaciones());
        }

        v.setFechaActualizacion(LocalDateTime.now());
        return repo.save(v);
    }

    /**
     * Elimina una venta por su ID
     */
    public void eliminar(Long id) {
        if (repo.existsById(id)) {
            repo.deleteById(id);
        }
    }

    /**
     * Valida los datos básicos de una venta
     */
    private void validarVenta(Venta v) {
        if (v == null) {
            throw new IllegalArgumentException("La venta no puede ser nula");
        }

        if (v.getCliente() == null) {
            throw new IllegalArgumentException("El cliente es requerido");
        }

        if (v.getVendedor() == null) {
            throw new IllegalArgumentException("El vendedor es requerido");
        }

        if (v.getMontoTotal() == null || v.getMontoTotal().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto total debe ser mayor a 0");
        }
    }

    /**
     * Valida que el estado sea uno de los permitidos
     */
    private boolean esEstadoValido(String estado) {
        return estado.equals("PENDIENTE") || estado.equals("COMPLETADA") || estado.equals("CANCELADA");
    }
}
