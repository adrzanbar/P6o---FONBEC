package com.fonbec.p6o.security.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fonbec.p6o.util.validator.ValidRoles;


@Getter
@Setter
public class UsuarioDTO {

    @NotNull(message = "El ID es obligatorio", groups = { Actualizar.class, Eliminar.class })
    private String id;

    @NotBlank(message = "El nombre es obligatorio", groups = { Guardar.class, Actualizar.class })
    @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio", groups = { Guardar.class, Actualizar.class })
    @Size(min = 2, max = 50, message = "El apellido debe tener entre 2 y 50 caracteres")
    private String apellido;

    @NotBlank(message = "El email es obligatorio", groups = { Guardar.class, Actualizar.class })
    @Size(max = 255, message = "El email debe tener menos de 255 caracteres", groups = { Guardar.class, Actualizar.class })
    @Email(message = "Debe proporcionar un email valido", groups = { Guardar.class, Actualizar.class })
    private String email;

    @NotBlank(message = "La contraseña es obligatoria" , groups = { Guardar.class, Actualizar.class })
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres", groups = { Guardar.class, Actualizar.class })
    private String password;

    @NotBlank(message = "Repita la contraseña", groups = { Guardar.class, Actualizar.class })
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres", groups = { Guardar.class, Actualizar.class })
    private String password2;

    @NotNull(message = "El estado habilitado es obligatorio", groups = { Actualizar.class })
    private Boolean habilitado;

    @Past(message = "La fecha de nacimiento no puede ser futura", groups = { Guardar.class, Actualizar.class })
    @NotNull(message = "La fecha de nacimiento es obligatoria", groups = { Guardar.class, Actualizar.class })
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate fechaNacimiento;

    @NotBlank(message = "El telefono es obligatorio", groups = { Guardar.class, Actualizar.class }) 
    @Pattern(regexp = "\\d+", message = "El telefono debe contener solo numeros", groups = { Guardar.class, Actualizar.class })
    @Size(min = 10, max = 15, message = "El telefono debe tener entre 10 y 15 caracteres", groups = { Guardar.class, Actualizar.class })    
    private String telefono;

    @NotBlank(message = "La direccion es obligatoria", groups = { Guardar.class, Actualizar.class })    
    @Size(max = 255, message = "La direccion no puede superar los 255 caracteres", groups = { Guardar.class, Actualizar.class })
    private String direccion;

    @NotBlank(message = "El DNI es obligatorio", groups = { Guardar.class, Actualizar.class })
    @Pattern(regexp = "\\d+", message = "El DNI debe contener solo numeros", groups = { Guardar.class, Actualizar.class })
    private String dni;

    private LocalDate fechaRegistro;
    private LocalDate fechaActualizacion;

    @NotEmpty(message = "Debe asignar al menos un rol", groups = { Guardar.class, Actualizar.class })
    @ValidRoles(message = "Uno o mas roles son invalidos", groups = { Guardar.class, Actualizar.class })
    private List<String> roles;

    @AssertTrue(message = "Las contraseñas no coinciden", groups = { Guardar.class, Actualizar.class })
    public boolean isPasswordsMatching() {
        if (password == null || password2 == null) {
            return false;
        }
        return password.equals(password2);
    }
    
    public interface Guardar {
    }

    public interface Actualizar {
    }

    public interface Eliminar {
    }

    public interface Listar {
    }
}
