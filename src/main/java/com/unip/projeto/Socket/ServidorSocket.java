package com.unip.projeto.Socket;

import java.io.*;
import java.net.*;
import java.util.*;

public class ServidorSocket {
    private static final int PORT = 12345;
    private static Map<String, PrintWriter> usuariosConectados = new HashMap<>();

    public static void main(String[] args) {
        System.out.println("Servidor iniciado na porta " + PORT);
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket socket = serverSocket.accept();
                new Thread(new ManipuladorCliente(socket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ManipuladorCliente implements Runnable {
        private Socket socket;
        private String nomeUsuario;
        private PrintWriter saida;

        public ManipuladorCliente(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (
                InputStreamReader isr = new InputStreamReader(socket.getInputStream());
                BufferedReader entrada = new BufferedReader(isr);
                OutputStreamWriter osw = new OutputStreamWriter(socket.getOutputStream());
                PrintWriter saida = new PrintWriter(osw, true);
            ) {
                this.saida = saida;
                saida.println("Digite seu nome de usuário:");
                nomeUsuario = entrada.readLine();

                synchronized (usuariosConectados) {
                    if (usuariosConectados.containsKey(nomeUsuario)) {
                        saida.println("Nome de usuário já em uso. Conexão encerrada.");
                        return;
                    }
                    usuariosConectados.put(nomeUsuario, saida);
                }

                saida.println("Bem-vindo, " + nomeUsuario + "!");
                broadcast(nomeUsuario + " entrou no chat.");

                String mensagem;
                while ((mensagem = entrada.readLine()) != null) {
                    if (mensagem.equalsIgnoreCase("/sair")) {
                        break;
                    }
                    broadcast(nomeUsuario + ": " + mensagem);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                sair();
            }
        }

        private void broadcast(String mensagem) {
            synchronized (usuariosConectados) {
                for (PrintWriter writer : usuariosConectados.values()) {
                    writer.println(mensagem);
                }
            }
        }

        private void sair() {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            synchronized (usuariosConectados) {
                if (nomeUsuario != null) {
                    usuariosConectados.remove(nomeUsuario);
                    broadcast(nomeUsuario + " saiu do chat.");
                }
            }
        }
    }
}
