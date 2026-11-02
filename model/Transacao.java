package model;

public class Transacao {
    private double valor;
    private String data;
    private String hora;
    private String redeTransmissora;
    private String formaPagamento;

    private String nsu;
    private String codigoResposta;

    public double getValor() {
        return valor;
    }
    public void setValor(double valor) {
        this.valor = valor;
    }
    
    public String getData() {
        return data;
    }
    public void setData(String data) {
        this.data = data;
    }

    public String getHora() {
        return hora;
    }
    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getRedeTransmissora() {
        return redeTransmissora;
    }
    public void setRedeTransmissora(String redeTransmissora) {
        this.redeTransmissora = redeTransmissora;
    }
    public String getFormaPagamento() {
        return formaPagamento;
    }
    public void setFormaPagamento(String formaPagamento) {
        this.formaPagamento = formaPagamento;
    }
    public String getNsu() {
        return nsu;
    }
    public void setNsu(String nsu) {
        this.nsu = nsu;
    }
    public String getCodigoResposta() {
        return codigoResposta;
    }
    public void setCodigoResposta(String codigoResposta) {
        this.codigoResposta = codigoResposta;
    }
}
