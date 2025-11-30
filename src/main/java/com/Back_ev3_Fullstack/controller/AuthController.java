package com.Back_ev3_Fullstack.controller;

import com.Back_ev3_Fullstack.dto.LoginRequest;
import com.Back_ev3_Fullstack.dto.RegistroRequest;
import com.Back_ev3_Fullstack.entity.Role;
import com.Back_ev3_Fullstack.entity.Usuario;
import com.Back_ev3_Fullstack.repository.UsuarioRepository;
import com.Back_ev3_Fullstack.security.CustomUserDetailsService;
import com.Back_ev3_Fullstack.security.JwtUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticación", description = "Endpoints para login, registro y gestión de usuarios")
public class AuthController {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    // LOGIN
    @PostMapping("/login")
    @Operation(
            summary = "Iniciar sesión",
            description = "Autentica un usuario y devuelve un token JWT"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login exitoso, token JWT generado"),
            @ApiResponse(responseCode = "401", description = "Credenciales inválidas")
    })
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(req.getCorreo(), req.getContrasenia())
            );
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
        }

        // Obtener el usuario real desde DB
        Usuario usuario = usuarioRepository.findByCorreo(req.getCorreo());

        List<String> rolesConPrefijo = usuario.getRoles()
                .stream()
                .map(r -> "ROLE_" + r.name())
                .toList();

        // Crear token CON roles convertidos
        String token = jwtUtil.generateToken(usuario.getCorreo(), rolesConPrefijo);

        return ResponseEntity.ok(Map.of("token", token));
    }

    @PostMapping("/register")
    @Operation(
            summary = "Registrar nuevo usuario",
            description = "Crea un nuevo usuario con rol USER por defecto"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario registrado correctamente"),
            @ApiResponse(responseCode = "400", description = "El correo ya está registrado")
    })
    public ResponseEntity<String> registro(@RequestBody RegistroRequest request) {

        if (usuarioRepository.findByCorreo(request.getCorreo()) != null) {
            return ResponseEntity.badRequest().body("El correo ya está registrado");
        }

        Usuario usuario = new Usuario();
        usuario.setCorreo(request.getCorreo());
        usuario.setContrasenia(passwordEncoder.encode(request.getContrasenia()));
        usuario.setRoles(new HashSet<>());
        usuario.getRoles().add(Role.USER);

        usuarioRepository.save(usuario);

        return ResponseEntity.ok("Usuario registrado correctamente");
    }

    // ENDPOINT para frontend devuelve usuario actual y roles
    @GetMapping("/me")
    @Operation(
            summary = "Obtener información del usuario actual",
            description = "Devuelve el correo y roles del usuario autenticado"
    )
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Información del usuario obtenida correctamente"),
            @ApiResponse(responseCode = "401", description = "Usuario no autenticado")
    })
    public ResponseEntity<?> me() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No autenticado");
        }

        Object principal = auth.getPrincipal();
        String correo;
        if (principal instanceof UserDetails) {
            correo = ((UserDetails) principal).getUsername();
        } else {
            correo = principal.toString();
        }

        var roles = auth.getAuthorities()
                .stream()
                .map(a -> a.getAuthority())
                .toList();

        return ResponseEntity.ok(Map.of("correo", correo, "roles", roles));
    }

    // Prueba de endpoints protegidos
    @GetMapping("/user")
    @PreAuthorize("hasRole('USER')")
    @Operation(
            summary = "Endpoint de prueba para USER",
            description = "Verifica que un usuario con rol USER puede acceder"
    )
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponse(responseCode = "200", description = "Acceso permitido para rol USER")
    public String userOnly() {
        return "Usuario";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Endpoint de prueba para ADMIN",
            description = "Verifica que un usuario con rol ADMIN puede acceder"
    )
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponse(responseCode = "200", description = "Acceso permitido para rol ADMIN")
    public String adminOnly() {
        return "Admin";
    }
}