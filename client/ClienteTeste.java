package client;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ClienteTeste {

    // Altere para 8080 para testar o Servidor Multithread ou 8081 para o Servidor NIO
    private static final String HOST = "localhost";
    private static final int PORTA = 8081; 

    public static void main(String[] args) {
        System.out.println("=== COMEÇANDO TESTES NO SERVIDOR (" + PORTA + ") ===\n");

        
        System.out.println("--- TESTE DE TRANSAÇÃO APROVADA ---");
        String msgAprovada = "020000000000201010444605120401044012310218451";
        enviarEExibirResposta(msgAprovada);

      
        System.out.println("\n--- TESTE DE SALDO INSUFICIENTE ---");
        String msgSaldoInsuficiente = "020000000005000010444605120401045500000000001";
        enviarEExibirResposta(msgSaldoInsuficiente);

      
        System.out.println("\n--- TSTE DE CARTÃO QUE NÃO EXISTE---");
        String msgCartaoInexistente = "020000000000100010444605120401049999999999991";
        enviarEExibirResposta(msgCartaoInexistente);
    }

    private static void enviarEExibirResposta(String mensagemIso) {
        try (Socket socket = new Socket(HOST, PORTA);
             OutputStream out = socket.getOutputStream();
             InputStream in = socket.getInputStream()) {

          
            byte[] envialBytes = mensagemIso.getBytes(StandardCharsets.UTF_8);
            out.write(envialBytes);
            out.flush();

           
            byte[] buffer = new byte[1024];
            int bytesLidos = in.read(buffer);

            if (bytesLidos > 0) {
                String respostaStr = new String(buffer, 0, bytesLidos, StandardCharsets.UTF_8);
                System.out.println("Enviado (0200) : " + mensagemIso);
                System.out.println("Recebido (0210): " + respostaStr);

                
                if (respostaStr.length() >= 14) {
                    String codResposta = respostaStr.substring(respostaStr.length() - 14, respostaStr.length() - 12);
                    String nsu = respostaStr.substring(respostaStr.length() - 12);
                    System.out.println("-> Código de Resposta: " + codResposta);
                    System.out.println("-> NSU Gerado: " + nsu);
                }
            }

        } catch (IOException e) {
            System.err.println("Erro na comunicação com o servidor: " + e.getMessage());
        }
    }
}