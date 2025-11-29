package com.Back_ev3_Fullstack.security;

import com.Back_ev3_Fullstack.entity.Usuario;
import com.Back_ev3_Fullstack.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {

        Usuario usuario = usuarioRepository.findByCorreo(correo);

        if (usuario == null) {
            throw new UsernameNotFoundException("Usuario no encontrado: " + correo);
        }

        // Convertimos los roles de la entidad a un arreglo de Strings
        String[] roles = usuario.getRoles()
                .stream()
                .map(Enum::name)   // ADMIN → "ADMIN"
                .toArray(String[]::new);

        return User.builder()
                .username(usuario.getCorreo())
                .password(usuario.getContrasenia())
                .roles(roles)   // ← Ahora usa los roles reales del usuario
                .build();
    }
}
