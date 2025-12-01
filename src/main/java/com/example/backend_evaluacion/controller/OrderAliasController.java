package com.example.backend_evaluacion.controller;

import com.example.backend_evaluacion.entity.Venta;
import com.example.backend_evaluacion.entity.DetalleVenta;
import com.example.backend_evaluacion.entity.Perfume;
import com.example.backend_evaluacion.entity.Cliente;
import com.example.backend_evaluacion.dto.OrderRequest;
import com.example.backend_evaluacion.dto.DetalleRequest;
import com.example.backend_evaluacion.dto.VentaDTO;
import com.example.backend_evaluacion.dto.DetalleVentaDTO;
import com.example.backend_evaluacion.service.VentaService;
import com.example.backend_evaluacion.repository.UsuarioRepository;
import com.example.backend_evaluacion.repository.ClienteRepository;
import com.example.backend_evaluacion.repository.PerfumeRepository;
import com.example.backend_evaluacion.repository.DetalleVentaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Alias controller to map generic order endpoints expected by frontend
 * to underlying Venta domain.
 */
@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderAliasController {

    private final VentaService ventaService;
    private final UsuarioRepository usuarioRepository;
    private final ClienteRepository clienteRepository;
    private final PerfumeRepository perfumeRepository;
    private final DetalleVentaRepository detalleVentaRepository;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','VENDEDOR','CLIENTE')")
    public List<VentaDTO> listar() {
        return ventaService.listar().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private VentaDTO convertToDTO(Venta venta) {
        VentaDTO dto = new VentaDTO();
        dto.setId(venta.getId());
        dto.setMontoTotal(venta.getMontoTotal());
        dto.setFecha(venta.getFecha());
        dto.setEstado(venta.getEstado());
        dto.setObservaciones(venta.getObservaciones());
        dto.setFechaCreacion(venta.getFechaCreacion());

        if (venta.getCliente() != null) {
            dto.setClienteId(venta.getCliente().getId());
            dto.setClienteNombre(venta.getCliente().getNombre() + " " + venta.getCliente().getApellido());
        }

        if (venta.getVendedor() != null) {
            dto.setVendedorId(venta.getVendedor().getId());
            dto.setVendedorNombre(venta.getVendedor().getNombre());
        }

        return dto;
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','VENDEDOR','CLIENTE')")
    public VentaDTO obtener(@PathVariable Long id) {
        Venta venta = ventaService.obtener(id);
        return convertToDTO(venta);
    }

    @GetMapping("/{id}/details")
    @PreAuthorize("hasAnyRole('ADMIN','VENDEDOR','CLIENTE')")
    public List<DetalleVentaDTO> obtenerDetalles(@PathVariable Long id) {
        List<DetalleVenta> detalles = detalleVentaRepository.findAll().stream()
                .filter(d -> d.getVenta().getId().equals(id))
                .collect(Collectors.toList());

        return detalles.stream()
                .map(this::convertDetalleToDTO)
                .collect(Collectors.toList());
    }

    private DetalleVentaDTO convertDetalleToDTO(DetalleVenta detalle) {
        DetalleVentaDTO dto = new DetalleVentaDTO();
        dto.setId(detalle.getId());
        dto.setCantidad(detalle.getCantidad());
        dto.setPrecioUnitario(detalle.getPrecioUnitario());
        dto.setSubtotal(detalle.getSubtotal());

        if (detalle.getPerfume() != null) {
            dto.setPerfumeId(detalle.getPerfume().getId());
            dto.setPerfumeNombre(detalle.getPerfume().getNombre());
        }

        return dto;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('VENDEDOR','CLIENTE')")
    public Venta registrar(@RequestBody OrderRequest req) {
        Venta v = new Venta();
        v.setMontoTotal(req.getMontoTotal());
        v.setEstado(req.getEstado() != null ? req.getEstado().toUpperCase() : "PENDIENTE");

        if (req.getVendedorId() != null) {
            usuarioRepository.findById(req.getVendedorId()).ifPresent(v::setVendedor);
        }
        if (req.getClienteId() != null) {
            clienteRepository.findById(req.getClienteId()).ifPresent(v::setCliente);
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            String username = auth.getName();
            usuarioRepository.findByUsername(username).ifPresent(u -> {
                // If user is VENDEDOR or ADMIN and no vendedor set, use them
                String rol = u.getRol().getNombre();
                if (v.getVendedor() == null && ("VENDEDOR".equals(rol) || "ADMIN".equals(rol))) {
                    v.setVendedor(u);
                }
                // If cliente not set, try to find or create one
                if (v.getCliente() == null) {
                    // First try to find by email
                    Cliente clienteExistente = clienteRepository.findByEmail(u.getEmail()).orElse(null);
                    if (clienteExistente != null) {
                        v.setCliente(clienteExistente);
                    } else {
                        // Create a new cliente based on user info
                        Cliente nuevo = new Cliente();
                        String nombreCompleto = u.getNombre() != null ? u.getNombre() : u.getUsername();
                        String nombre = nombreCompleto;
                        String apellido = "";
                        if (nombreCompleto.contains(" ")) {
                            String[] partes = nombreCompleto.trim().split(" ", 2);
                            nombre = partes[0];
                            apellido = partes.length > 1 ? partes[1] : "";
                        }
                        if (apellido.isEmpty()) {
                            apellido = "N/A";
                        }
                        nuevo.setNombre(nombre);
                        nuevo.setApellido(apellido);
                        nuevo.setEmail(u.getEmail());
                        nuevo.setTelefono("");
                        nuevo.setDireccion("");
                        nuevo.setCiudad("");
                        Cliente guardado = clienteRepository.save(nuevo);
                        v.setCliente(guardado);
                    }
                }
            });
        }

        // Guardar la venta primero
        Venta ventaGuardada = ventaService.registrar(v);

        // Procesar los detalles si existen
        if (req.getDetalle() != null && !req.getDetalle().isEmpty()) {
            for (DetalleRequest detReq : req.getDetalle()) {
                if (detReq.getProductId() == null || detReq.getCantidad() == null) {
                    continue; // Saltar detalles incompletos
                }

                Perfume perfume = perfumeRepository.findById(detReq.getProductId()).orElse(null);
                if (perfume == null) {
                    continue; // Saltar si el producto no existe
                }

                DetalleVenta detalle = new DetalleVenta();
                detalle.setVenta(ventaGuardada);
                detalle.setPerfume(perfume);
                detalle.setCantidad(detReq.getCantidad());

                // Usar precio del request o del producto
                BigDecimal precioUnitario = detReq.getPrecio_unitario() != null
                        ? detReq.getPrecio_unitario()
                        : BigDecimal.valueOf(perfume.getPrecio());
                detalle.setPrecioUnitario(precioUnitario);

                // Calcular subtotal
                BigDecimal subtotal = precioUnitario.multiply(BigDecimal.valueOf(detReq.getCantidad()));
                detalle.setSubtotal(subtotal);

                detalleVentaRepository.save(detalle);
            }
        }

        return ventaGuardada;
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Venta actualizar(@PathVariable Long id, @RequestBody Venta v) {
        return ventaService.actualizar(id, v);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void eliminar(@PathVariable Long id) {
        ventaService.eliminar(id);
    }
}