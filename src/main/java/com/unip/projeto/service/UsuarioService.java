package com.unip.projeto.service;

import com.unip.projeto.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public boolean autenticar(String email, String senha) {
        return usuarioRepository.findByEmail(email)
                .map(usuario -> usuario.getSenha().equals(senha))
                .orElse(false);
    }
}
