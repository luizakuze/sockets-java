package engtelecom.std;

import java.net.InetAddress;

/**
 * Classe para representar um dispositivo IOT.
 */
public class Dispositivo {
    private int versao;
    private InetAddress endereco;

    public Dispositivo(InetAddress endereco, int versao) {
        this.versao = versao;
        this.endereco = endereco;
    }

    // /**
    // * Atualiza a versão atual do dispositivo com uma nova versão.
    // * @param novaVersao nova versão a ser atualizada.
    // * @return "true" se atualizou a versão, "false" caso contrário.
    // */
    // public boolean aplicarAtualizacao(int novaVersao) {
    // if (novaVersao != versao) {
    // this.versao = novaVersao;
    // return true;
    // }
    // return false;
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
