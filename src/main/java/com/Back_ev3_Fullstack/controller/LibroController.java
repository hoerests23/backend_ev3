package com.Back_ev3_Fullstack.controller;

import com.Back_ev3_Fullstack.entity.Libro;
import com.Back_ev3_Fullstack.service.LibroService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/libros")
@Tag(name = "Libro Controller", description = "CRUD de libros (solo rol ADMIN)")
@SecurityRequirement(name = "Bearer Authentication")
public class LibroController {

    private final LibroService libroService;

    public LibroController(LibroService libroService) {
        this.libroService = libroService;
    }

    // GET total
    @GetMapping
    @Operation(
            summary = "Obtener todos los libros",
            description = "Devuelve una lista con todos los libros registrados"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente"),
            @ApiResponse(responseCode = "401", description = "No autorizado — requiere token JWT"),
            @ApiResponse(responseCode = "403", description = "No tiene el rol necesario")
    })
    public List<Libro> obtenerTodos() {
        return libroService.obtenerTodos();
    }

    // GET por id
    @GetMapping("/{id}")
    @Operation(
            summary = "Obtener libro por ID",
            description = "Devuelve un libro según su ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Libro encontrado"),
            @ApiResponse(responseCode = "404", description = "Libro no encontrado"),
            @ApiResponse(responseCode = "401", description = "No autorizado")
    })
    public ResponseEntity<Libro> obtenerPorId(@PathVariable Long id) {
        return libroService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // CREAR
    @PostMapping
    @Operation(
            summary = "Crear un nuevo libro",
            description = "Registra un nuevo libro en el sistema"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Libro creado con éxito"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "401", description = "No autorizado")
    })
    public Libro crear(@RequestBody Libro libro) {
        return libroService.crear(libro);
    }

    // ACTUALIZAR
    @PutMapping("/{id}")
    @Operation(
            summary = "Actualizar libro",
            description = "Actualiza la información de un libro específico"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Libro actualizado"),
            @ApiResponse(responseCode = "404", description = "Libro no encontrado"),
            @ApiResponse(responseCode = "401", description = "No autorizado")
    })
    public ResponseEntity<Libro> actualizar(@PathVariable Long id, @RequestBody Libro libro) {
        Libro actualizado = libroService.actualizar(id, libro);
        if (actualizado == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(actualizado);
    }

    // ELIMINAR
    @DeleteMapping("/{id}")
    @Operation(
            summary = "Eliminar libro",
            description = "Elimina un libro según su ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Libro eliminado"),
            @ApiResponse(responseCode = "404", description = "Libro no encontrado"),
            @ApiResponse(responseCode = "401", description = "No autorizado")
    })
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        libroService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
