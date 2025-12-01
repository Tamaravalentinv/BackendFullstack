package com.example.backend_evaluacion.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "perfumes")
@Data
public class Perfume {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @ManyToOne(optional = false)
    @JoinColumn(name = "marca_id")
    private Marca marca;

    @ManyToOne(optional = false)
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    @Column(nullable = false, columnDefinition = "DECIMAL(10,2)")
    private Double precio;

    @Column(nullable = false)
    private Integer stock;

    @Column(length = 500)
    private String descripcion;

    @Column(length = 50)
    private String tamanio; // ml

    @Column(length = 20)
    private String tipo; // EDP, EDT, Eau de Cologne, etc.

    @Column(length = 1000)
    private String imagen; // URL de la imagen del perfume

    private LocalDateTime fechaCreacion = LocalDateTime.now();
    private LocalDateTime fechaActualizacion = LocalDateTime.now();
}
