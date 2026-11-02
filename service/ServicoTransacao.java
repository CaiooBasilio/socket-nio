package service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import model.Cartao;
import model.Transacao;
public class ServicoTransacao {
    private final Map<String, Cartao> bancoCartoes = new ConcurrentHashMap<>();

    private long contadorNSU = 1;

    public ServicoTransacao() {
        bancoCartoes.put("401231021845", new Cartao("401231021845", "João Silva", 100.00));
        bancoCartoes.put("550000000000", new Cartao("550000000000", "Maria Souza", 10.00));
    }


    public synchronized Transacao processar(String numeroCartao, double valor, String data, String hora, String rede, String formaPagamento) {
        Transacao ts = new Transacao();
        ts.setValor(valor);
        ts.setData(data);
        ts.setHora(hora);
        ts.setRedeTransmissora(rede);
        ts.setFormaPagamento(formaPagamento);
        
        Cartao cartao = bancoCartoes.get(numeroCartao);

        if (cartao == null) {
            ts.setCodigoResposta("05"); 
            ts.setNsu("000000000000");  
            return ts;                 
        }

        if (cartao.getSaldo() < valor) {
            ts.setCodigoResposta("51"); 
            ts.setNsu("000000000000");  
            return ts;
        }

        cartao.setSaldo(cartao.getSaldo() - valor);

        String nsuGerado = String.format("%012d", contadorNSU);
        contadorNSU++; 
        
        ts.setCodigoResposta("00"); 
        ts.setNsu(nsuGerado);       
        
        return ts;
    }
}
