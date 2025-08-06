package com.fonbec.p6o.security.batch;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.fonbec.p6o.security.entity.Role;
import com.fonbec.p6o.security.entity.Usuario;
import com.fonbec.p6o.security.enums.RoleEnum;
import com.fonbec.p6o.security.repository.RoleRepository;
import com.fonbec.p6o.security.repository.UsuarioRepository;

@Component
public class Initializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public Initializer(
            UsuarioRepository usuarioRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.usuarioRepository = usuarioRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        // Pre-cargar todos los roles si faltan
        Arrays.stream(RoleEnum.values()).forEach(roleEnum -> {
            roleRepository.findByNombre(roleEnum).orElseGet(() -> {
                Role role = new Role();
                role.setNombre(roleEnum);
                return roleRepository.save(role);
            });
        });
        
        // Pre-cargar un usuario administrador si no existe
        if (usuarioRepository.findByEmail("admin@fonbec.org").isEmpty()) {
            Role adminRole = roleRepository.findByNombre(RoleEnum.ADMINISTRADOR)
                .orElseThrow(() -> new IllegalStateException("Rol ADMINISTRADOR no encontrado"));

            Usuario admin = new Usuario();
            admin.setNombre("Admin");
            admin.setApellido("Fonbec");
            admin.setEmail("admin@fonbec.org");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setHabilitado(true);
            admin.setFechaNacimiento(LocalDate.of(1990, 1, 1));
            admin.setTelefono("1234567890");
            admin.setDireccion("Oficina Central");
            admin.setDni("00000000");
            admin.setFechaRegistro(LocalDate.now());
            admin.setRoles(List.of(adminRole));

            usuarioRepository.save(admin);
        }
    }
}
