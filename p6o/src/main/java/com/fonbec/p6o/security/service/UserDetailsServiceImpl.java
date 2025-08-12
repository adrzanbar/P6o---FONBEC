package com.fonbec.p6o.security.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fonbec.p6o.security.entity.Role;
import com.fonbec.p6o.security.entity.Usuario;
import com.fonbec.p6o.security.exception.UsuarioException;
import com.fonbec.p6o.security.repository.RoleRepository;
import com.fonbec.p6o.security.repository.UsuarioRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public UserDetailsServiceImpl(UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder,
            RoleRepository roleRepository) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
    }

    public Usuario guardar(Usuario usuario) {
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new UsuarioException(HttpStatus.BAD_REQUEST, "El email ya esta registrado");
        }

        if (usuarioRepository.existsByDni(usuario.getDni())) {
            throw new UsuarioException(HttpStatus.BAD_REQUEST, "El DNI ya esta registrado");
        }

        if (usuarioRepository.existsByTelefono(usuario.getTelefono())) {
            throw new UsuarioException(HttpStatus.BAD_REQUEST, "El telefono ya esta registrado");
        }

        // Vincular roles existentes
        List<Role> rolesValidos = usuario.getRoles().stream()
                .map(role -> {
                    return roleRepository.findByNombre(role.getNombre())
                            .orElseThrow(() -> new UsuarioException(HttpStatus.BAD_REQUEST,
                                    "Rol invalido: " + role.getNombre()));
                })
                .collect(Collectors.toList());

        usuario.setRoles(rolesValidos);

        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        usuario.setHabilitado(true);
        usuario.setFechaRegistro(LocalDate.now());

        return usuarioRepository.save(usuario);
    }
}
