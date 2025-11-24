package com.example.backend_evaluacion.service;

import com.example.backend_evaluacion.entity.Venta;
import com.example.backend_evaluacion.repository.VentaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VentaService {

    private final VentaRepository repo;

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
