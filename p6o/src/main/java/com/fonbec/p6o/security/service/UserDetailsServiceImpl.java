package com.fonbec.p6o.security.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fonbec.p6o.exception.UsuarioException;
import com.fonbec.p6o.security.entity.Role;
import com.fonbec.p6o.security.entity.Usuario;
import com.fonbec.p6o.security.enums.RoleEnum;
import com.fonbec.p6o.security.repository.RoleRepository;
import com.fonbec.p6o.security.repository.UsuarioRepository;
import com.fonbec.p6o.service.BaseService;

@Service
public class UserDetailsServiceImpl extends BaseService<Usuario, String> implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public UserDetailsServiceImpl(UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder,
            RoleRepository roleRepository) {
        super(usuarioRepository);
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
    }

    @Override
    public Usuario guardar(Usuario usuario) throws UsernameNotFoundException {
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new UsuarioException(HttpStatus.BAD_REQUEST, "El email ya esta registrado");
        }

        if (usuarioRepository.existsByDni(usuario.getDni())) {
            throw new UsuarioException(HttpStatus.BAD_REQUEST, "El DNI ya esta registrado");
        }

        if (usuarioRepository.existsByTelefono(usuario.getTelefono())) {
            throw new UsuarioException(HttpStatus.BAD_REQUEST, "El telefono ya esta registrado");
        }

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

    @Override
    public void borrar(String id) throws UsernameNotFoundException {
        Usuario usuario = this.obtener(id);
        if (usuario != null) {
            usuario.setHabilitado(false);
            this.actualizar(id,usuario);
        }
    }

    @Override
    public Usuario obtener(String id) throws UsernameNotFoundException {
        return usuarioRepository.findById(id).orElse(null);
    }

    @Override
    public Usuario actualizar(String id, Usuario usuarioActualizado) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String emailAuth = auth.getName();
        Usuario usuarioAuth = usuarioRepository.findByEmail(emailAuth)
                .orElseThrow(() -> new UsuarioException(HttpStatus.UNAUTHORIZED, "Usuario no autenticado"));

        
        if (!usuarioAuth.getId().equals(id) &&
            usuarioAuth.getRoles().stream().noneMatch(r -> r.getNombre().equals(RoleEnum.ADMINISTRADOR))) {
            throw new UsuarioException(HttpStatus.FORBIDDEN, "No puedes modificar a otro usuario");
        }

        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        if (!usuarioExistente.getEmail().equals(usuarioActualizado.getEmail())
                && usuarioRepository.existsByEmail(usuarioActualizado.getEmail())) {
            throw new UsuarioException(HttpStatus.BAD_REQUEST, "El email ya esta registrado");
        }

        if (!usuarioExistente.getDni().equals(usuarioActualizado.getDni())
                && usuarioRepository.existsByDni(usuarioActualizado.getDni())) {
            throw new UsuarioException(HttpStatus.BAD_REQUEST, "El DNI ya esta registrado");
        }

        if (!usuarioExistente.getTelefono().equals(usuarioActualizado.getTelefono())
                && usuarioRepository.existsByTelefono(usuarioActualizado.getTelefono())) {
            throw new UsuarioException(HttpStatus.BAD_REQUEST, "El telefono ya esta registrado");
        }

        usuarioExistente.setNombre(usuarioActualizado.getNombre());
        usuarioExistente.setApellido(usuarioActualizado.getApellido());
        usuarioExistente.setEmail(usuarioActualizado.getEmail());
        usuarioExistente.setTelefono(usuarioActualizado.getTelefono());
        usuarioExistente.setDireccion(usuarioActualizado.getDireccion());
        usuarioExistente.setDni(usuarioActualizado.getDni());
        usuarioExistente.setFechaNacimiento(usuarioActualizado.getFechaNacimiento());

        if (usuarioActualizado.getPassword() != null && !usuarioActualizado.getPassword().isBlank()) {
            usuarioExistente.setPassword(passwordEncoder.encode(usuarioActualizado.getPassword()));
            usuarioExistente.setFechaActualizacion(LocalDate.now());
        }

        if (usuarioActualizado.getRoles() != null && !usuarioActualizado.getRoles().isEmpty()) {
            if (usuarioAuth.getRoles().stream().noneMatch(r -> r.getNombre().equals(RoleEnum.ADMINISTRADOR))) {
                throw new UsuarioException(HttpStatus.FORBIDDEN, "No puedes modificar roles");
            }

            List<Role> rolesValidos = usuarioActualizado.getRoles().stream()
                    .map(role -> roleRepository.findByNombre(role.getNombre())
                            .orElseThrow(() -> new UsuarioException(HttpStatus.BAD_REQUEST,
                                    "Rol invalido: " + role.getNombre())))
                    .collect(Collectors.toList());
            usuarioExistente.setRoles(rolesValidos);
        }

        return usuarioRepository.save(usuarioExistente);
    }

}
