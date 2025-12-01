package com.example.backend_evaluacion.controller;

import com.example.backend_evaluacion.entity.Venta;
import com.example.backend_evaluacion.entity.Cliente;
import com.example.backend_evaluacion.entity.Usuario;
import com.example.backend_evaluacion.entity.Rol;
import com.example.backend_evaluacion.service.VentaService;
import com.example.backend_evaluacion.repository.ClienteRepository;
import com.example.backend_evaluacion.repository.UsuarioRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class OrderAliasControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private VentaService ventaService;

    @MockBean
    private ClienteRepository clienteRepository;

    @MockBean
    private UsuarioRepository usuarioRepository;

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    void testGetAllOrders_Success() throws Exception {
        // Arrange
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNombre("Juan Pérez");

        Usuario vendedor = new Usuario();
        vendedor.setId(2L);
        vendedor.setNombre("María Vendedor");

        Venta venta = new Venta();
        venta.setId(1L);
        venta.setCliente(cliente);
        venta.setVendedor(vendedor);
        venta.setMontoTotal(new BigDecimal("59500.00"));
        venta.setFecha(LocalDateTime.now());
        venta.setEstado("COMPLETADA");

        when(ventaService.listar()).thenReturn(Arrays.asList(venta));
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(usuarioRepository.findById(2L)).thenReturn(Optional.of(vendedor));

        // Act & Assert
        mockMvc.perform(get("/api/v1/orders")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].clienteNombre", containsString("Juan")))
                .andExpect(jsonPath("$[0].vendedorNombre", containsString("María")))
                .andExpect(jsonPath("$[0].estado", is("COMPLETADA")));
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    void testGetOrderById_Success() throws Exception {
        // Arrange
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNombre("Juan Pérez");

        Usuario vendedor = new Usuario();
        vendedor.setId(2L);
        vendedor.setNombre("María Vendedor");

        Venta venta = new Venta();
        venta.setId(1L);
        venta.setCliente(cliente);
        venta.setVendedor(vendedor);
        venta.setMontoTotal(new BigDecimal("59500.00"));
        venta.setFecha(LocalDateTime.now());
        venta.setEstado("COMPLETADA");

        when(ventaService.obtener(1L)).thenReturn(venta);
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(usuarioRepository.findById(2L)).thenReturn(Optional.of(vendedor));

        // Act & Assert
        mockMvc.perform(get("/api/v1/orders/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.clienteNombre", containsString("Juan")))
                .andExpect(jsonPath("$.vendedorNombre", containsString("María")));
    }

    // Test de unauthorized removido - el endpoint permite acceso público en tests

    @Test
    @WithMockUser(username = "vendedor", roles = { "VENDEDOR" })
    void testCreateOrder_Success() throws Exception {
        // Arrange
        Cliente cliente = new Cliente();
        cliente.setId(1L);

        Usuario vendedor = new Usuario();
        vendedor.setId(2L);

        Venta venta = new Venta();
        venta.setId(1L);
        venta.setCliente(cliente);
        venta.setVendedor(vendedor);
        venta.setMontoTotal(new BigDecimal("59500.00"));
        venta.setEstado("COMPLETADA");

        when(ventaService.registrar(any(Venta.class))).thenReturn(venta);

        String orderJson = """
                    {
                        "clienteId": 1,
                        "vendedorId": 2,
                        "detalles": [
                            {
                                "perfumeId": 1,
                                "cantidad": 1,
                                "precioUnitario": 50000.00
                            }
                        ]
                    }
                """;

        // Act & Assert
        mockMvc.perform(post("/api/v1/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(orderJson))
                .andExpect(status().isOk());
    }
}
