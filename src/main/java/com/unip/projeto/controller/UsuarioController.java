package com.unip.projeto.controller;

import com.unip.projeto.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Map<String, String> credentials) {
        String email = credentials.get("email");
        String senha = credentials.get("senha");

        boolean autenticado = usuarioService.autenticar(email, senha);

        Map<String, String> response = new HashMap<>();
        if (autenticado) {
            response.put("mensagem", "Login realizado com sucesso");
        } else {
            response.put("mensagem", "Credenciais inválidas");
        }

        return response;
    }
}
