package com.Back_ev3_Fullstack.config;

import com.Back_ev3_Fullstack.entity.Usuario;
import com.Back_ev3_Fullstack.entity.Role;
import com.Back_ev3_Fullstack.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataLoader {

    @Bean
    public CommandLineRunner initAdmin(
            UsuarioRepository repo,
            PasswordEncoder encoder
    ) {
        return args -> {

            String adminCorreo = "admin@local";
            String adminPass = "admin123";

            // Si ya existe un admin, no hacemos nada
            if (repo.existsByCorreo(adminCorreo)) return;

            Usuario admin = new Usuario();
            admin.setCorreo(adminCorreo);
            admin.setContrasenia(encoder.encode(adminPass));

            // asignar roles
            admin.getRoles().add(Role.ADMIN);
            admin.getRoles().add(Role.USER);

            repo.save(admin);

            System.out.println("✔ Admin inicial creado: " + adminCorreo);
        };
    }
}
