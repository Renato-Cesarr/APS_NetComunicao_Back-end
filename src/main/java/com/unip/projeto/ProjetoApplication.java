package com.unip.projeto;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import javax.swing.SwingUtilities;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.unip.projeto.Swing.LoginFrame;

@SpringBootApplication
public class ProjetoApplication {

	private static final Set<PrintWriter> clients = new HashSet<>();

	public static void main(String[] args) throws IOException {
	    new Thread(() -> {
	        try {
	            System.out.println("Servidor iniciado na porta 8080.");
	            ServerSocket serverSocket = new ServerSocket(8080);

	            while (true) {
	                Socket clientSocket = serverSocket.accept();
	                PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
	                clients.add(writer);

	                new Thread(() -> {
	                    try (Scanner scanner = new Scanner(clientSocket.getInputStream())) {
	                        while (scanner.hasNextLine()) {
	                            String mensagem = scanner.nextLine();
	                            synchronized (clients) {
	                                for (PrintWriter client : clients) {
	                                    client.println(mensagem);
	                                }
	                            }
	                        }
	                    } catch (Exception ex) {
	                        ex.printStackTrace();
	                    } finally {
	                        clients.remove(writer);
	                    }
	                }).start();
	            }
	        } catch (IOException ex) {
	            ex.printStackTrace();
	        }
	    }).start();

	    SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
	}

}
