package com.Back_ev3_Fullstack.dto;

import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VentaResponse {

    private Long id;
    private LocalDateTime fechaVenta;
    private Double total;
    private List<DetalleResponse> detalles;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class DetalleResponse {
        private Long id;
        private LibroInfo libro;
        private Integer cantidad;
        private Double precioUnitario;
        private Double subtotal;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class LibroInfo {
        private Long id;
        private String titulo;
        private String autor;
    }
}