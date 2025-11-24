package com.example.backend_evaluacion.service;

import com.example.backend_evaluacion.entity.DetalleVenta;
import com.example.backend_evaluacion.repository.DetalleVentaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DetalleVentaService {

    private final DetalleVentaRepository repo;

    /**
     * Lista todos los detalles de ventas registrados
     */
    public List<DetalleVenta> listar() {
        return repo.findAll();
    }

    /**
     * Registra un nuevo detalle de venta con validaciones de negocio
     * - Validar que cantidad > 0
     * - Validar que precioUnitario > 0
     * - Calcular subtotal (cantidad * precioUnitario)
     */
    public DetalleVenta guardar(DetalleVenta detalle) {
        validarDetalle(detalle);
        
        // Calcular subtotal si no está establecido
        if (detalle.getSubtotal() == null || detalle.getSubtotal().compareTo(BigDecimal.ZERO) == 0) {
            BigDecimal cantidad = new BigDecimal(detalle.getCantidad());
            BigDecimal subtotal = cantidad.multiply(detalle.getPrecioUnitario());
            detalle.setSubtotal(subtotal);
        }
        
        detalle.setFechaCreacion(LocalDateTime.now());
        return repo.save(detalle);
    }

    /**
     * Obtiene un detalle de venta por su ID
     */
    public DetalleVenta obtener(Long id) {
        if (id == null || id <= 0) {
            return null;
        }
        return repo.findById(id).orElse(null);
    }

    /**
     * Actualiza un detalle de venta con validaciones
     */
    public DetalleVenta actualizar(Long id, DetalleVenta nuevo) {
        DetalleVenta detalle = obtener(id);
        if (detalle == null) {
            return null;
        }

        // Validar cantidad
        if (nuevo.getCantidad() != null) {
            if (nuevo.getCantidad() <= 0) {
                throw new IllegalArgumentException("La cantidad debe ser mayor a 0");
            }
            detalle.setCantidad(nuevo.getCantidad());
        }

        // Validar precio unitario
        if (nuevo.getPrecioUnitario() != null) {
            if (nuevo.getPrecioUnitario().compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("El precio unitario debe ser mayor a 0");
            }
            detalle.setPrecioUnitario(nuevo.getPrecioUnitario());
        }

        // Actualizar perfume si es necesario
        if (nuevo.getPerfume() != null) {
            detalle.setPerfume(nuevo.getPerfume());
        }

        // Recalcular subtotal
        BigDecimal cantidad = new BigDecimal(detalle.getCantidad());
        BigDecimal subtotal = cantidad.multiply(detalle.getPrecioUnitario());
        detalle.setSubtotal(subtotal);

        return repo.save(detalle);
    }

    /**
     * Elimina un detalle de venta por su ID
     */
    public void eliminar(Long id) {
        if (repo.existsById(id)) {
            repo.deleteById(id);
        }
    }

    /**
     * Obtiene todos los detalles de una venta específica
     */
    public List<DetalleVenta> obtenerPorVenta(Long ventaId) {
        if (ventaId == null || ventaId <= 0) {
            return List.of();
        }
        return repo.findByVentaId(ventaId);
    }

    /**
     * Valida los datos básicos de un detalle de venta
     */
    private void validarDetalle(DetalleVenta detalle) {
        if (detalle == null) {
            throw new IllegalArgumentException("El detalle de venta no puede ser nulo");
        }

        if (detalle.getVenta() == null) {
            throw new IllegalArgumentException("La venta es requerida");
        }

        if (detalle.getPerfume() == null) {
            throw new IllegalArgumentException("El perfume es requerido");
        }

        if (detalle.getCantidad() == null || detalle.getCantidad() <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0");
        }

        if (detalle.getPrecioUnitario() == null || detalle.getPrecioUnitario().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El precio unitario debe ser mayor a 0");
        }
    }
}
