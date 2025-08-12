package com.fonbec.p6o.security.mapper;

import java.util.stream.Collectors;


import com.fonbec.p6o.security.dto.UsuarioDTO;
import com.fonbec.p6o.security.entity.Role;
import com.fonbec.p6o.security.entity.Usuario;
import com.fonbec.p6o.security.enums.RoleEnum;


public class UsuarioMapper {
    private UsuarioMapper() {}

    // Convierte de Usuario a UsuarioDTO
    public static UsuarioDTO toDTO(Usuario usuario) {
        if (usuario == null) {
            return null;
        }
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(usuario.getId());
        dto.setNombre(usuario.getNombre());
        dto.setApellido(usuario.getApellido());
        dto.setEmail(usuario.getEmail());
        dto.setHabilitado(usuario.getHabilitado());
        dto.setFechaNacimiento(usuario.getFechaNacimiento());
        dto.setTelefono(usuario.getTelefono());
        dto.setDireccion(usuario.getDireccion());
        dto.setDni(usuario.getDni());
        dto.setFechaRegistro(usuario.getFechaRegistro());
        dto.setFechaActualizacion(usuario.getFechaActualizacion());

        // Convertir List<Role> a List<String> (nombre de enum)
        dto.setRoles(
            usuario.getRoles().stream()
                .map(role -> role.getNombre().name()) // RoleEnum.name() devuelve el nombre del enum como String
                .collect(Collectors.toList())
        );

        return dto;
    }

    // Convierte de UsuarioDTO a Usuario
    public static Usuario toEntity(UsuarioDTO dto) {
        if (dto == null) {
            return null;
        }
        Usuario usuario = new Usuario();
        usuario.setId(dto.getId());
        usuario.setNombre(dto.getNombre());
        usuario.setApellido(dto.getApellido());
        usuario.setEmail(dto.getEmail());
        usuario.setPassword(dto.getPassword());
        usuario.setHabilitado(dto.getHabilitado());
        usuario.setFechaNacimiento(dto.getFechaNacimiento());
        usuario.setTelefono(dto.getTelefono());
        usuario.setDireccion(dto.getDireccion());
        usuario.setDni(dto.getDni());
        usuario.setFechaRegistro(dto.getFechaRegistro());
        usuario.setFechaActualizacion(dto.getFechaActualizacion());

        
        usuario.setRoles(
            dto.getRoles().stream()
                .map(roleStr -> {
                    Role role = new Role();
                    try {
                        role.setNombre(RoleEnum.valueOf(roleStr)); 
                    } catch (IllegalArgumentException e) {
                        throw new RuntimeException("Rol inválido: " + roleStr);
                    }
                    return role;
                })
                .collect(Collectors.toList())
        );

        return usuario;
    }
}
