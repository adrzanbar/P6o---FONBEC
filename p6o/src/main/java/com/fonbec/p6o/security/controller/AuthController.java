package com.fonbec.p6o.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.fonbec.p6o.security.dto.JwtDTO;
import com.fonbec.p6o.security.dto.LoginRequestDTO;
import com.fonbec.p6o.security.dto.UsuarioDTO;
import com.fonbec.p6o.security.entity.Usuario;
import com.fonbec.p6o.security.exception.AuthenticationException;
import com.fonbec.p6o.security.mapper.UsuarioMapper;
import com.fonbec.p6o.security.service.JwtService;
import com.fonbec.p6o.security.service.UserDetailsServiceImpl;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserDetailsServiceImpl userDetailsServiceImpl;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, JwtService jwtService,
            UserDetailsServiceImpl userDetailsServiceImpl) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userDetailsServiceImpl = userDetailsServiceImpl;
    }

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JwtDTO> login(
            @Validated(LoginRequestDTO.Autenticar.class) @RequestBody LoginRequestDTO request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String jwt = jwtService.generateToken(userDetails);

            return ResponseEntity.ok(new JwtDTO(jwt));

        } catch (DisabledException e) {
            throw new AuthenticationException(HttpStatus.UNAUTHORIZED, "Cuenta deshabilitada");
        } catch (LockedException e) {
            throw new AuthenticationException(HttpStatus.UNAUTHORIZED, "Cuenta bloqueada");
        } catch (AccountExpiredException e) {
            throw new AuthenticationException(HttpStatus.UNAUTHORIZED, "Cuenta expirada");
        } catch (CredentialsExpiredException e) {
            throw new AuthenticationException(HttpStatus.UNAUTHORIZED, "Credenciales expiradas");
        } catch (BadCredentialsException e) {
            throw new AuthenticationException(HttpStatus.UNAUTHORIZED, "Credenciales incorrectas");
        } catch (Exception e) {
            throw new AuthenticationException(HttpStatus.UNAUTHORIZED, "Ocurrio un error al autenticar", e);
        }
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PostMapping(value = "/registrar", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Usuario> register(
            @Validated(UsuarioDTO.Guardar.class) @RequestBody UsuarioDTO usuarioDTO) {
        Usuario usuario = UsuarioMapper.toEntity(usuarioDTO);
        if (usuario != null) {
            usuario = userDetailsServiceImpl.guardar(usuario);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(usuario);

    }

}