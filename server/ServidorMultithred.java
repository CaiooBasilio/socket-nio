package server;

import parser.ISO8583Parser;
import service.ServicoTransacao;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
public class ServidorMultithred {
    private static final int PORTA = 8080;
    private static final ServicoTransacao servico = new ServicoTransacao();

    public static void main(String[] args) {
        System.out.println("SERVIDOR SOCKET MUILTITHREAD ATIVO (PORTA: " + PORTA +")");

        try (ServerSocket serverSocket = new ServerSocket(PORTA)) {
            while (true) {
               
                Socket socketCliente = serverSocket.accept();
                System.out.println("Novo cliente conectado: " + socketCliente.getInetAddress());

             
                Thread threadCliente = new Thread(new ClientHandler(socketCliente));
                threadCliente.start();
            }
        } catch (IOException e) {
            System.err.println("Erro no servidor: " + e.getMessage());
        }
    }

      private static class ClientHandler implements Runnable {
        private final Socket socket;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (InputStream in = socket.getInputStream();
                 OutputStream out = socket.getOutputStream()) {

                byte[] buffer = new byte[1024];
                int bytesLidos = in.read(buffer);

                if (bytesLidos > 0) {
                   
                    byte[] requisicao = new byte[bytesLidos];
                    System.arraycopy(buffer, 0, requisicao, 0, bytesLidos);

                   
                    byte[] resposta = ISO8583Parser.processarMensagem(requisicao, servico);

                  
                    out.write(resposta);
                    out.flush();
                }

            } catch (IOException e) {
                System.err.println("Erro ao comunicar com o cliente: " + e.getMessage());
            } finally {
                try {
                    socket.close(); 
                } catch (IOException e) {
                    System.err.println("Erro ao fechar o socket: " + e.getMessage());
                }
            }
        }
    }
}