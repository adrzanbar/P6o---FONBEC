package com.fonbec.p6o.security.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fonbec.p6o.security.entity.Role;
import com.fonbec.p6o.security.enums.RoleEnum;

public interface RoleRepository extends JpaRepository<Role,Long>{
    Optional<Role> findByNombre(RoleEnum nombre);
    
}
