package com.Back_ev3_Fullstack.service;

import com.Back_ev3_Fullstack.entity.Usuario;
import com.Back_ev3_Fullstack.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Usuario registrar(Usuario usuarioPlain) {
        if (usuarioRepository.existsByCorreo(usuarioPlain.getCorreo())) {
            throw new RuntimeException("El correo ya está registrado");
        }
        usuarioPlain.setContrasenia(passwordEncoder.encode(usuarioPlain.getContrasenia()));
        return usuarioRepository.save(usuarioPlain);
    }

    public Usuario findByCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo);
    }

}
