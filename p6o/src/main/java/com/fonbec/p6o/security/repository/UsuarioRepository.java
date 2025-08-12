package com.fonbec.p6o.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fonbec.p6o.security.entity.Usuario;


import java.util.Optional;


public interface UsuarioRepository extends JpaRepository<Usuario,String> {

    Optional<Usuario> findByEmail(String email);
    Boolean existsByEmail(String email);
    boolean existsByDni(String email);
    boolean existsByTelefono(String telefono);
    
}