package com.fonbec.p6o.controller;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fonbec.p6o.security.dto.UsuarioDTO;
import com.fonbec.p6o.security.entity.Usuario;
import com.fonbec.p6o.security.mapper.UsuarioMapper;
import com.fonbec.p6o.security.service.UserDetailsServiceImpl;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/usuario")
@AllArgsConstructor
public class UsuarioController {

    private final UserDetailsServiceImpl userDetailsServiceImpl;
    
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @GetMapping(value = "/listar", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Usuario>> listar(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestBody(required = false) Map<String, Object> filtros) {

        Pageable pageable = PageRequest.of(page, size);

        if (filtros == null) {
            filtros = Map.of();
        }

        Page<Usuario> usuarios = userDetailsServiceImpl.listar(filtros, pageable);

        return ResponseEntity.ok(usuarios.getContent());
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PostMapping(value = "/borrar")
    public void borrar(@RequestParam String id) {
        this.userDetailsServiceImpl.borrar(id);
       
    }


    @PutMapping("/actualizar")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Usuario> actualizar(
            @RequestParam String id,
            @Validated(UsuarioDTO.Actualizar.class) @RequestBody UsuarioDTO usuarioDTO) {
        usuarioDTO.setId(id);
        Usuario usuario = UsuarioMapper.toEntity(usuarioDTO);
        usuario = userDetailsServiceImpl.actualizar(id, usuario);
        return ResponseEntity.ok(usuario);
    }


    
}
