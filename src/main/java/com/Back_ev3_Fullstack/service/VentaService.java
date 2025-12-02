package com.Back_ev3_Fullstack.service;

import com.Back_ev3_Fullstack.dto.VentaRequest;
import com.Back_ev3_Fullstack.dto.VentaResponse;
import com.Back_ev3_Fullstack.entity.DetalleVenta;
import com.Back_ev3_Fullstack.entity.Libro;
import com.Back_ev3_Fullstack.entity.Venta;
import com.Back_ev3_Fullstack.repository.LibroRepository;
import com.Back_ev3_Fullstack.repository.VentaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VentaService {

    private final VentaRepository ventaRepository;
    private final LibroRepository libroRepository;

    public VentaService(VentaRepository ventaRepository, LibroRepository libroRepository) {
        this.ventaRepository = ventaRepository;
        this.libroRepository = libroRepository;
    }

    @Transactional
    public VentaResponse crearVenta(VentaRequest request) {
        // crear la venta
        Venta venta = Venta.builder()
                .total(request.getTotal())
                .build();

        // procesar cada item
        for (VentaRequest.ItemVenta item : request.getItems()) {
            // buscar el libro
            Libro libro = libroRepository.findById(item.getLibroId())
                    .orElseThrow(() -> new RuntimeException("Libro no encontrado con ID: " + item.getLibroId()));

            // validar stock
            if (libro.getStock() < item.getCantidad()) {
                throw new RuntimeException("Stock insuficiente para el libro: " + libro.getTitulo());
            }

            // actualizar stock
            libro.setStock(libro.getStock() - item.getCantidad());
            libroRepository.save(libro);

            // crear detalle de venta
            DetalleVenta detalle = DetalleVenta.builder()
                    .venta(venta)
                    .libro(libro)
                    .cantidad(item.getCantidad())
                    .precioUnitario(item.getPrecio())
                    .subtotal(item.getCantidad() * item.getPrecio())
                    .build();

            venta.getDetalles().add(detalle);
        }

        // guardar la venta con todos sus detalles
        Venta ventaGuardada = ventaRepository.save(venta);

        return convertirAResponse(ventaGuardada);
    }

    public List<VentaResponse> obtenerTodasVentas() {
        return ventaRepository.findAll().stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    public VentaResponse obtenerVentaPorId(Long id) {
        Venta venta = ventaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada con ID: " + id));
        return convertirAResponse(venta);
    }

    public List<VentaResponse> obtenerVentasDelDia() {
        LocalDateTime inicioDia = LocalDate.now().atStartOfDay();
        LocalDateTime finDia = LocalDate.now().atTime(LocalTime.MAX);

        return ventaRepository.findByFechaVentaBetween(inicioDia, finDia).stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    private VentaResponse convertirAResponse(Venta venta) {
        List<VentaResponse.DetalleResponse> detalles = venta.getDetalles().stream()
                .map(detalle -> VentaResponse.DetalleResponse.builder()
                        .id(detalle.getId())
                        .libro(VentaResponse.LibroInfo.builder()
                                .id(detalle.getLibro().getId())
                                .titulo(detalle.getLibro().getTitulo())
                                .autor(detalle.getLibro().getAutor())
                                .build())
                        .cantidad(detalle.getCantidad())
                        .precioUnitario(detalle.getPrecioUnitario())
                        .subtotal(detalle.getSubtotal())
                        .build())
                .collect(Collectors.toList());

        return VentaResponse.builder()
                .id(venta.getId())
                .fechaVenta(venta.getFechaVenta())
                .total(venta.getTotal())
                .detalles(detalles)
                .build();
    }
}