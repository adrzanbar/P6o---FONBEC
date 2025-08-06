package com.fonbec.p6o.security.service;

import java.util.stream.Collectors;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.fonbec.p6o.security.entity.Role;
import com.fonbec.p6o.security.entity.Usuario;
import com.fonbec.p6o.security.repository.UsuarioRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public UserDetailsServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Usuario user = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        String[] authorities = user.getRoles().stream()
                .map(role -> role.getNombre().name()) // si usás enums
                .toArray(String[]::new);

        return User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .roles(authorities)
                .build();
    }

}