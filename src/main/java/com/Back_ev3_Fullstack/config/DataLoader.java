package com.Back_ev3_Fullstack.config;

import com.Back_ev3_Fullstack.entity.Libro;
import com.Back_ev3_Fullstack.entity.Usuario;
import com.Back_ev3_Fullstack.entity.Role;
import com.Back_ev3_Fullstack.repository.LibroRepository;
import com.Back_ev3_Fullstack.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

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

            if (repo.existsByCorreo(adminCorreo)) {
                System.out.println("✔ Admin ya existe: " + adminCorreo);
                return;
            }

            Usuario admin = new Usuario();
            admin.setCorreo(adminCorreo);
            admin.setContrasenia(encoder.encode(adminPass));

            admin.getRoles().add(Role.ADMIN);
            admin.getRoles().add(Role.USER);

            repo.save(admin);

            System.out.println("✔ Admin inicial creado: " + adminCorreo);
        };
    }

    @Bean
    public CommandLineRunner initLibros(LibroRepository libroRepo) {
        return args -> {

            // solo crear si no hay libros
            if (libroRepo.count() > 0) {
                System.out.println("✔ Ya existen " + libroRepo.count() + " libros en la base de datos");
                return;
            }

            System.out.println("Creando libros iniciales con catalogo vacio ");

            Libro libro1 = Libro.builder()
                    .titulo("1984")
                    .autor("George Orwell")
                    .precio(15990)
                    .stock(10)
                    .imagen("https://covers.openlibrary.org/b/id/7222246-L.jpg")
                    .anio(1949)
                    .categoria("Distopía")
                    .isbn("9780451524935")
                    .build();

            Libro libro2 = Libro.builder()
                    .titulo("Orgullo y Prejuicio")
                    .autor("Jane Austen")
                    .precio(14500)
                    .stock(10)
                    .imagen("https://covers.openlibrary.org/b/id/12604738-M.jpg")
                    .anio(1813)
                    .categoria("Romance")
                    .isbn("9780141439518")
                    .build();

            Libro libro3 = Libro.builder()
                    .titulo("El Gran Gatsby")
                    .autor("F. Scott Fitzgerald")
                    .precio(13990)
                    .stock(10)
                    .imagen("https://covers.openlibrary.org/b/id/9367463-M.jpg")
                    .anio(1925)
                    .categoria("Clásicos")
                    .isbn("9780743273565")
                    .build();

            Libro libro4 = Libro.builder()
                    .titulo("Matar a un Ruiseñor")
                    .autor("Harper Lee")
                    .precio(16500)
                    .stock(10)
                    .imagen("https://covers.openlibrary.org/b/id/14351066-L.jpg")
                    .anio(1960)
                    .categoria("Clásicos")
                    .isbn("9780061120084")
                    .build();

            Libro libro5 = Libro.builder()
                    .titulo("El Principito")
                    .autor("Antoine de Saint-Exupéry")
                    .precio(12000)
                    .stock(10)
                    .imagen("https://covers.openlibrary.org/b/id/2295636-M.jpg")
                    .anio(1943)
                    .categoria("Infantil")
                    .isbn("9780156012195")
                    .build();

            try {
                libroRepo.saveAll(List.of(libro1, libro2, libro3, libro4, libro5));
                System.out.println("5 libros clásicos creados exitosamente");
            } catch (Exception e) {
                System.err.println("Error al crear libros iniciales: " + e.getMessage());
                e.printStackTrace();
            }
        };
    }
}