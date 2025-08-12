package com.fonbec.p6o.security.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestDTO {

    @NotBlank(message = "El email es obligatorio", groups = { Autenticar.class })
    @Size(max = 255, message = "El email debe tener menos de 255 caracteres", groups = { Autenticar.class })
    @Email(message = "Debe ser un email valido", groups = { Autenticar.class })
    private String email;

    @NotBlank(message = "La contraseña es obligatoria", groups = { Autenticar.class })
    @Size(min = 5, max = 100, message = "La contraseña debe tener entre 5 y 100 caracteres", groups = { Autenticar.class })
    private String password;

    public interface Autenticar {}
}
