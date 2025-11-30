package com.Back_ev3_Fullstack.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Tag(name = "Pruebas de Roles", description = "Endpoints de prueba para verificar roles USER y ADMIN")
@SecurityRequirement(name = "Bearer Authentication")
public class UserAdminTestController {

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER')")
    @Operation(
            summary = "Endpoint de prueba USER",
            description = "Requiere rol USER para acceder"
    )
    public String userEndpoint() {
        return "Acceso USER permitido";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Endpoint de prueba ADMIN",
            description = "Requiere rol ADMIN para acceder"
    )
    public String adminEndpoint() {
        return "Acceso ADMIN permitido";
    }
}