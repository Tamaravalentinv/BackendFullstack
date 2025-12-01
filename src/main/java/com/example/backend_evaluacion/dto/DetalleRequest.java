package com.example.backend_evaluacion.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class DetalleRequest {
    private Long productId;
    private Integer cantidad;
    private BigDecimal precio_unitario;
}
