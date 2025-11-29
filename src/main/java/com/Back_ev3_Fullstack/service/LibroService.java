package com.Back_ev3_Fullstack.service;

import com.Back_ev3_Fullstack.entity.Libro;
import com.Back_ev3_Fullstack.repository.LibroRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LibroService {

    private final LibroRepository libroRepository;

    public LibroService(LibroRepository libroRepository) {
        this.libroRepository = libroRepository;
    }

    public List<Libro> obtenerTodos() {
        return libroRepository.findAll();
    }

    public Optional<Libro> obtenerPorId(Long id) {
        return libroRepository.findById(id);
    }

    public Libro crear(Libro libro) {
        return libroRepository.save(libro);
    }

    public Libro actualizar(Long id, Libro libroActualizado) {
        return libroRepository.findById(id).map(libro -> {
            libro.setTitulo(libroActualizado.getTitulo());
            libro.setAutor(libroActualizado.getAutor());
            libro.setAnio(libroActualizado.getAnio());
            libro.setPrecio(libroActualizado.getPrecio());
            libro.setStock(libroActualizado.getStock());
            libro.setCategoria(libroActualizado.getCategoria());
            libro.setIsbn(libroActualizado.getIsbn());
            return libroRepository.save(libro);

        }).orElse(null);
    }

    public void eliminar(Long id) {
        libroRepository.deleteById(id);
    }
}
