package com.fonbec.p6o.security.enums;

import lombok.Getter;

@Getter
public enum RoleEnum {
    ADMINISTRADOR("Administrador"), 
    VOLUNTARIO("Voluntario"), 
    MEDIADOR("Mediador"),
    BECARIO_UNIVERSITARIO("Becario Universitario"), 
    SISTEMA("Sistema");

    private final String name;

    RoleEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
