package com.unip.projeto.Swing;

import java.awt.*;
import java.awt.event.*;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import javax.swing.*;

import com.unip.projeto.model.Usuario;
import com.unip.projeto.service.EmailService;

public class ChatFrame extends JFrame {
    private final Usuario usuario;
    private PrintWriter writer;
    private boolean darkMode = false;

    private JPanel userPanel, inputPanel, topBar;
    private JTextArea chatArea;
    private JTextField inputField;
    private JButton darkModeButton;

    public ChatFrame(Usuario usuario) {
        this.usuario = usuario;
        initComponents();
        configureNetwork();
    }

    private void initComponents() {
        setTitle("Chat - " + usuario.getNome());
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        createTopBar();
        createUserPanel();
        createChatArea();
        createInputPanel();
    }

    private void createTopBar() {
        topBar = new JPanel(new BorderLayout());
        topBar.setBackground(new Color(33, 150, 243));
        topBar.setPreferredSize(new Dimension(0, 50));

        ImageIcon icon = new ImageIcon("C:/Users/renat/Downloads/image-removebg-preview.png");
        JLabel logoLabel = new JLabel();
        logoLabel.setPreferredSize(new Dimension(50, 50));
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        logoLabel.setVerticalAlignment(SwingConstants.CENTER);

        logoLabel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int height = logoLabel.getHeight();
                int width = logoLabel.getWidth();
                Image scaledImage = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
                logoLabel.setIcon(new ImageIcon(scaledImage));
            }
        });

        topBar.add(logoLabel, BorderLayout.WEST);

        darkModeButton = new JButton("🌙");
        darkModeButton.setFont(new Font("Arial", Font.BOLD, 16));
        darkModeButton.setFocusPainted(false);
        darkModeButton.setBackground(new Color(33, 150, 243));
        darkModeButton.setForeground(Color.WHITE);
        darkModeButton.setBorderPainted(false);
        darkModeButton.addActionListener(e -> toggleDarkMode());

        topBar.add(darkModeButton, BorderLayout.EAST);
        add(topBar, BorderLayout.NORTH);
    }

    private void createUserPanel() {
        userPanel = new JPanel();
        userPanel.setLayout(new BoxLayout(userPanel, BoxLayout.Y_AXIS));
        userPanel.setBackground(new Color(245, 245, 245));
        userPanel.setPreferredSize(new Dimension(200, 0));
        userPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel userLabel = new JLabel("Usuários:");
        userLabel.setFont(new Font("Arial", Font.BOLD, 16));
        userLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        userPanel.add(userLabel);

        DefaultListModel<String> userListModel = new DefaultListModel<>();
        userListModel.addElement("Admin");
        userListModel.addElement("Admin2");

        JList<String> userList = new JList<>(userListModel);
        userList.setFont(new Font("Arial", Font.PLAIN, 14));
        userList.setBackground(Color.WHITE);
        userList.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        JScrollPane scrollPane = new JScrollPane(userList);
        userPanel.add(scrollPane);

        add(userPanel, BorderLayout.WEST);
    }

    private void createChatArea() {
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setFont(new Font("Arial", Font.PLAIN, 14));
        chatArea.setBackground(Color.WHITE);
        chatArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(chatArea);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void createInputPanel() {
        inputPanel = new JPanel(new BorderLayout());
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        inputPanel.setBackground(new Color(245, 245, 245));

        inputField = new JTextField();
        inputField.setFont(new Font("Arial", Font.PLAIN, 14));
        inputField.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        JButton sendButton = new JButton("Enviar");
        sendButton.setFont(new Font("Arial", Font.BOLD, 14));
        sendButton.setBackground(new Color(33, 150, 243));
        sendButton.setForeground(Color.WHITE);
        sendButton.setFocusPainted(false);
        sendButton.addActionListener(this::sendMessage);

        JButton emailButton = new JButton("Enviar E-mail");
        emailButton.setFont(new Font("Arial", Font.BOLD, 14));
        emailButton.setBackground(new Color(76, 175, 80));
        emailButton.setForeground(Color.WHITE);
        emailButton.setFocusPainted(false);
        emailButton.addActionListener(this::sendEmail);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        buttonPanel.setBackground(inputPanel.getBackground());
        buttonPanel.add(emailButton);
        buttonPanel.add(sendButton);

        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(buttonPanel, BorderLayout.EAST);

        add(inputPanel, BorderLayout.SOUTH);
    }

    private void configureNetwork() {
        try {
            Socket socket = new Socket("localhost", 8080);
            writer = new PrintWriter(socket.getOutputStream(), true);

            new Thread(() -> {
                try (Scanner scanner = new Scanner(socket.getInputStream())) {
                    while (scanner.hasNextLine()) {
                        chatArea.append(scanner.nextLine() + "\n");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }).start();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void sendMessage(ActionEvent e) {
        String message = inputField.getText().trim();
        if (!message.isEmpty() && writer != null) {
            writer.println(usuario.getNome() + ": " + message);
            inputField.setText("");
        }
    }

    private void sendEmail(ActionEvent e) {
        String to = JOptionPane.showInputDialog(this, "Para:");
        String subject = JOptionPane.showInputDialog(this, "Assunto:");
        String body = JOptionPane.showInputDialog(this, "Mensagem:");

        if (to != null && subject != null && body != null) {
            new EmailService().sendEmail(to, subject, body);
            JOptionPane.showMessageDialog(this, "E-mail enviado com sucesso!");
        }
    }

    private void toggleDarkMode() {
        darkMode = !darkMode;

        Color topColor = darkMode ? new Color(64, 64, 64) : new Color(33, 150, 243);
        Color userBg = darkMode ? new Color(192, 192, 192) : new Color(245, 245, 245);
        Color inputBg = darkMode ? new Color(96, 96, 96) : new Color(245, 245, 245);
        Color chatBg = darkMode ? new Color(128, 128, 128) : Color.WHITE;
        Color chatFg = darkMode ? Color.WHITE : Color.BLACK;
        Color inputFieldBg = darkMode ? new Color(224, 224, 224) : Color.WHITE;

        topBar.setBackground(topColor);
        userPanel.setBackground(userBg);
        inputPanel.setBackground(inputBg);
        chatArea.setBackground(chatBg);
        chatArea.setForeground(chatFg);
        inputField.setBackground(inputFieldBg);
        inputField.setForeground(Color.BLACK);
        darkModeButton.setBackground(topColor);
        darkModeButton.setForeground(Color.WHITE);

        repaint();
    }
}
