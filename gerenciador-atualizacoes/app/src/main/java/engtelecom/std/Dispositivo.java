package engtelecom.std;

import java.net.InetAddress;

/**
 * Classe para rerpresentar um dispositivo iot
 */
public class Dispositivo {
    private int versao;
    private InetAddress endereco;

    public Dispositivo(InetAddress endereco, int versao) {
        this.versao = versao;
        this.endereco = endereco;
    }

    /**
     * Extrai uma versão de software recebida em string no formato "vx" para um
     * formato int.
     */
    // private int extrairVersao(String texto) {
    // if (texto == null)
    // return 0;
    // texto = texto.trim().toLowerCase();
    // // remove o "v" no início
    // texto = texto.substring(1);
    // try {
    // return Integer.parseInt(texto);
    // } catch (NumberFormatException e) {
    // System.err.println("Versão inválida: " + texto);
    // return 0;
    // }
    // }

    /**
     * Compara este dispositivo com outro dipositivo (objeto) para verificar igualdade.
     * Dois dispositivos são considerados iguais quando possuem o mesmo endereço IP.
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Dispositivo outro))
            return false;
        return this.endereco.equals(outro.endereco);
    }

    public InetAddress getEndereco() {
        return this.endereco;
    }

    public void setVersao(int versao) {
        this.versao = versao;
    }

    public int getVersao() {
        return this.versao;
    }

}
