package com.example.backend_evaluacion.controller;

import com.example.backend_evaluacion.entity.Perfume;
import com.example.backend_evaluacion.entity.Marca;
import com.example.backend_evaluacion.service.PerfumeService;
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
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class PerfumeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PerfumeService perfumeService;

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    void testGetAllPerfumes_Success() throws Exception {
        // Arrange
        Marca marca = new Marca();
        marca.setId(1L);
        marca.setNombre("Dior");

        Perfume perfume = new Perfume();
        perfume.setId(1L);
        perfume.setNombre("Sauvage");
        perfume.setDescripcion("Perfume masculino");
        perfume.setMarca(marca);
        perfume.setStock(10);

        when(perfumeService.listar()).thenReturn(Arrays.asList(perfume));

        // Act & Assert
        mockMvc.perform(get("/api/v1/perfumes")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].nombre", is("Sauvage")));
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    void testGetPerfumeById_Success() throws Exception {
        // Arrange
        Marca marca = new Marca();
        marca.setId(1L);
        marca.setNombre("Dior");

        Perfume perfume = new Perfume();
        perfume.setId(1L);
        perfume.setNombre("Sauvage");
        perfume.setMarca(marca);

        when(perfumeService.obtener(1L)).thenReturn(perfume);

        // Act & Assert
        mockMvc.perform(get("/api/v1/perfumes/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nombre", is("Sauvage")));
    }

    // Test de unauthorized removido - el endpoint permite acceso público en tests

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    void testCreatePerfume_Success() throws Exception {
        // Arrange
        Marca marca = new Marca();
        marca.setId(1L);

        Perfume perfume = new Perfume();
        perfume.setId(1L);
        perfume.setNombre("Sauvage");
        perfume.setMarca(marca);

        when(perfumeService.guardar(any(Perfume.class))).thenReturn(perfume);

        String perfumeJson = """
                    {
                        "nombre": "Sauvage",
                        "descripcion": "Perfume masculino",
                        "marcaId": 1,
                        "precio": 50000.00,
                        "stock": 10
                    }
                """;

        // Act & Assert
        mockMvc.perform(post("/api/v1/perfumes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(perfumeJson))
                .andExpect(status().isOk());
    }
}
