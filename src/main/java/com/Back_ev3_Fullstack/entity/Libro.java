package com.Back_ev3_Fullstack.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "libros")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false)
    private String autor;

    private Integer anio;

    @Column(nullable = false)
    private Integer precio;

    @Column(nullable = false)
    private Integer stock;


    private String categoria;

    @Column(unique = true)
    private String isbn;
}
