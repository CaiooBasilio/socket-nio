package server;

import parser.ISO8583Parser;
import service.ServicoTransacao;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class ServidorNIO {

    private static final int PORTA = 8081; 
    private static final ServicoTransacao servico = new ServicoTransacao();

    public static void main(String[] args) {
        System.out.println("SERVIDOR JAVAA NIO ATIVO (PORTA: " + PORTA+ ")");

        try (Selector selector = Selector.open();
             ServerSocketChannel serverChannel = ServerSocketChannel.open()) {

           
            serverChannel.bind(new InetSocketAddress(PORTA));
            serverChannel.configureBlocking(false);

           
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);

            while (true) {
              
                selector.select();

                
                Iterator<SelectionKey> keys = selector.selectedKeys().iterator();

                while (keys.hasNext()) {
                    SelectionKey key = keys.next();
                    keys.remove(); 

                    if (!key.isValid()) continue;

                   
                    if (key.isAcceptable()) {
                        aceitarConexao(key, selector);
                    } 
                    
                    else if (key.isReadable()) {
                        lerEProcessarMensagem(key);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Erro no Servidor: " + e.getMessage());
        }
    }

    private static void aceitarConexao(SelectionKey key, Selector selector) throws IOException {
        ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
        SocketChannel clientChannel = serverChannel.accept();

        if (clientChannel != null) {
            clientChannel.configureBlocking(false); 
            System.out.println("Cliente novo conectado via NIO: " + clientChannel.getRemoteAddress());
            
           
            clientChannel.register(selector, SelectionKey.OP_READ);
        }
    }

    private static void lerEProcessarMensagem(SelectionKey key) throws IOException {
        SocketChannel clientChannel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        int bytesLidos = clientChannel.read(buffer);

        if (bytesLidos > 0) {
            
            buffer.flip();
            byte[] requisicao = new byte[bytesLidos];
            buffer.get(requisicao);

            
            byte[] resposta = ISO8583Parser.processarMensagem(requisicao, servico);

           
            clientChannel.write(ByteBuffer.wrap(resposta));

           
            clientChannel.close();
        } else if (bytesLidos == -1) {
            clientChannel.close();
        }
    }
}