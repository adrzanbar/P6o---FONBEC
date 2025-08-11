package com.fonbec.p6o.security.service;

import javax.security.auth.login.AccountExpiredException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.fonbec.p6o.security.dto.LoginRequestDTO;
import com.fonbec.p6o.security.entity.Usuario;
import com.fonbec.p6o.security.repository.UsuarioRepository;
import com.fonbec.p6o.security.dto.JwtDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final UsuarioRepository usuarioRepository;

    public ResponseEntity<JwtDTO> authenticateAndGenerateToken(LoginRequestDTO authRequest)
            throws AccountExpiredException {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));

        Usuario usuario = usuarioRepository.findByEmail(authRequest.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));


        if (usuario.isAccountNonLocked() == false) {
            throw new LockedException("Cuenta bloqueada");
        }
        if (usuario.isAccountNonExpired() == false) {
            throw new AccountExpiredException("Cuenta expirada");
        }
        if (usuario.isCredentialsNonExpired() == false) {
            throw new CredentialsExpiredException("Credenciales expiradas");
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getEmail());
        final String jwtToken = jwtService.generateToken(userDetails);

        return ResponseEntity.ok(new JwtDTO(jwtToken));
    }

}