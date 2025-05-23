package com.unip.projeto.service;

import java.util.ArrayList;
import java.util.List;
import com.unip.projeto.model.Usuario;

public class UsuarioService {
	private final List<Usuario> usuarios;

	public UsuarioService() {
		usuarios = new ArrayList<>();
		usuarios.add(new Usuario("Admin", "admin@admin.com", "admin"));
		usuarios.add(new Usuario("Admin2", "admin2@admin.com", "admin2"));
	}

	public boolean autenticar(String email, String senha) {
		return usuarios.stream()
				.anyMatch(usuario -> usuario.getEmail().equals(email) && usuario.getSenha().equals(senha));
	}

	public Usuario buscarUsuarioPorEmail(String email) {
		return usuarios.stream().filter(usuario -> usuario.getEmail().equals(email)).findFirst().orElse(null);
	}

	public List<Usuario> getUsuarios() {
		return usuarios;
	}

}
