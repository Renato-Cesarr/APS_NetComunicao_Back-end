package com.unip.projeto.Swing;

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import com.unip.projeto.model.Usuario;
import com.unip.projeto.service.UsuarioService;

public class LoginFrame extends JFrame {
    private final UsuarioService usuarioService;

    public LoginFrame() {
        usuarioService = new UsuarioService();
        setTitle("Login");
        setSize(600, 450); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); 
        setLayout(new BorderLayout());

        JLabel backgroundLabel = new JLabel();
        ImageIcon imageIcon = new ImageIcon("\\Users\\renat\\Downloads\\fundologin.jpg");
        
        Image scaledImage = imageIcon.getImage().getScaledInstance(600, 450, Image.SCALE_SMOOTH);
        backgroundLabel.setIcon(new ImageIcon(scaledImage));
        backgroundLabel.setLayout(new GridBagLayout());

        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new GridLayout(4, 1, 15, 15)); 
        loginPanel.setPreferredSize(new Dimension(350, 250)); 
        loginPanel.setBackground(new Color(255, 255, 255, 200));
        loginPanel.setBorder(new EmptyBorder(20, 30, 20, 30)); 

        JLabel titleLabel = new JLabel("Bem-vindo!");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(33, 150, 243)); // Azul
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JTextField emailField = new JTextField();
        emailField.setFont(new Font("Arial", Font.PLAIN, 14));
        emailField.setBorder(BorderFactory.createTitledBorder("Email"));

        JPasswordField senhaField = new JPasswordField();
        senhaField.setFont(new Font("Arial", Font.PLAIN, 14));
        senhaField.setBorder(BorderFactory.createTitledBorder("Senha"));

        JButton loginButton = new JButton("Entrar");
        loginButton.setFont(new Font("Arial", Font.BOLD, 16));
        loginButton.setBackground(new Color(33, 150, 243)); 
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false); 
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        
        loginPanel.add(titleLabel);
        loginPanel.add(emailField);
        loginPanel.add(senhaField);
        loginPanel.add(loginButton);

        backgroundLabel.add(loginPanel);

        add(backgroundLabel, BorderLayout.CENTER);

        loginButton.addActionListener((ActionEvent e) -> {
            String email = emailField.getText();
            String senha = new String(senhaField.getPassword());

            if (usuarioService.autenticar(email, senha)) {
                Usuario usuario = usuarioService.buscarUsuarioPorEmail(email);
                JOptionPane.showMessageDialog(this, "Bem-vindo, " + usuario.getNome());
                new ChatFrame(usuario).setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Credenciais inválidas!");
            }
        });

        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent evt) {
                Dimension newSize = getSize();
                Image resizedImage = imageIcon.getImage().getScaledInstance(newSize.width, newSize.height, Image.SCALE_SMOOTH);
                backgroundLabel.setIcon(new ImageIcon(resizedImage));
            }
        });
    }
}
