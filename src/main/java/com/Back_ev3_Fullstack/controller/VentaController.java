package com.Back_ev3_Fullstack.controller;

import com.Back_ev3_Fullstack.dto.VentaRequest;
import com.Back_ev3_Fullstack.dto.VentaResponse;
import com.Back_ev3_Fullstack.service.VentaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ventas")
@Tag(name = "Venta Controller", description = "Gestión de ventas")
public class VentaController {

    private final VentaService ventaService;

    public VentaController(VentaService ventaService) {
        this.ventaService = ventaService;
    }

    @PostMapping
    @Operation(
            summary = "Crear una nueva venta",
            description = "Registra una nueva venta con sus detalles y actualiza el stock"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Venta creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o stock insuficiente"),
            @ApiResponse(responseCode = "404", description = "Libro no encontrado")
    })
    public ResponseEntity<VentaResponse> crearVenta(@RequestBody VentaRequest ventaRequest) {
        VentaResponse venta = ventaService.crearVenta(ventaRequest);
        return ResponseEntity.ok(venta);
    }

    @GetMapping
    @Operation(
            summary = "Obtener todas las ventas",
            description = "Devuelve una lista con todas las ventas registradas"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente")
    })
    public ResponseEntity<List<VentaResponse>> obtenerTodasVentas() {
        return ResponseEntity.ok(ventaService.obtenerTodasVentas());
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Obtener venta por ID",
            description = "Devuelve una venta específica según su ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Venta encontrada"),
            @ApiResponse(responseCode = "404", description = "Venta no encontrada")
    })
    public ResponseEntity<VentaResponse> obtenerVentaPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ventaService.obtenerVentaPorId(id));
    }

    @GetMapping("/hoy")
    @Operation(
            summary = "Obtener ventas del día",
            description = "Devuelve todas las ventas realizadas el día de hoy"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ventas del día obtenidas correctamente")
    })
    public ResponseEntity<List<VentaResponse>> obtenerVentasDelDia() {
        return ResponseEntity.ok(ventaService.obtenerVentasDelDia());
    }
}