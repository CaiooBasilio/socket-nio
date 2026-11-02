package parser;

import model.Transacao;
import service.ServicoTransacao;
import java.nio.charset.StandardCharsets;

public class ISO8583Parser {
    
    public static byte[] processarMensagem(byte[] requisicaoBytes, ServicoTransacao servico) {
       String msg = new String(requisicaoBytes, StandardCharsets.UTF_8);
       
       String mti = msg.substring(0, 4);
       String valorStr = msg.substring(4, 16);
       String hora = msg.substring(16, 22);
       String data = msg.substring(22, 26);
       String rede = msg.substring(26, 32);
       String numeroCartao = msg.substring(32, 44);
       String formaPagamento = msg.substring(44, 45);

       double valorConvertido = Double.parseDouble(valorStr) / 100.0;
       Transacao ts = servico.processar(numeroCartao, valorConvertido, data, hora, rede, formaPagamento);
       StringBuilder resposta = new StringBuilder();
        resposta.append("0210");                
        resposta.append(valorStr);               
        resposta.append(hora);                   
        resposta.append(data);                  
        resposta.append(rede);                  
        resposta.append(ts.getCodigoResposta()); 
        resposta.append(ts.getNsu());           

        
        return resposta.toString().getBytes(StandardCharsets.UTF_8);


    }
}
