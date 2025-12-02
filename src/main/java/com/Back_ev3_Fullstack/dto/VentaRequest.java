package com.Back_ev3_Fullstack.dto;

import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VentaRequest {

    private List<ItemVenta> items;
    private Double total;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ItemVenta {
        private Long libroId;
        private Integer cantidad;
        private Double precio;
    }
}