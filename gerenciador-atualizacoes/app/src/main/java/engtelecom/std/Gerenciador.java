package engtelecom.std;

import java.net.InetAddress;
import java.util.ArrayList;

/**
 * Classe representa um gerenciador de atualizações.
 * É responsável por manter uma lista de dispositvios iot e
 * guardar a versão do gerenciador.
 */
public class Gerenciador {
    private static final int VERSAO_GERENCIADOR = 1; // Gerenciador, por padrão, inicia nessa versão
    private final ArrayList<Dispositivo> dispositivos;
    private int versao;

    public Gerenciador() {
        this.dispositivos = new ArrayList<>();
        this.versao = VERSAO_GERENCIADOR;
    }

    /**
     * Verifica se um dispositivo pertence a lista de dispositivos existentes do
     * gerenciador.
     * 
     * @param d Dispositivo a ser verificado.
     * @return "true" se conter, "false" caso contrário.
     */
    public boolean contemDispositivo(Dispositivo d) {
        if (dispositivos.contains(d)) {
            return true;
        }
        return false;
    }

    /**
     * Registra um dispositivo na lista.
     * 
     * @return true se o dispositivo foi registrado ou reativado, false caso
     *         contrário.
     */
    public boolean registrarDispositivo(Dispositivo d) {
        boolean existe = contemDispositivo(d);

        if (!existe) {
            // se não existe, adiciona
            dispositivos.add(d);
            return true;
        }
        // Se existe e já está ativo, não faz nada
        return false;
    }

    /**
     * Atualiza a versão do dispositivo iot para a versão do gerenciador.
     * 
     * @param ip endereço do dispositvo a ser atualizado.
     * @return "true" caso encontre o dispositivo e "false" caso contrário.
     */
    public boolean atualizarDispositivo(InetAddress ip) {
        Dispositivo d = findByEndereco(ip);
        if (d == null) {
            return false; // não existe ainda
        }
        d.setVersao(this.versao);
        return true;
    }

    /**
     * Incrementa a versão do gerenciador.
     */
    public void incrementarVersao() {
        this.versao++;
    }

    /**
     * Retorna um dispositivo da lista do gerenciador a partir do IP.
     * 
     * @param endereco endereço IP do dispositivo.
     * @return o dispositivo caso encontre e "null" caso contrário.
     */
    private Dispositivo findByEndereco(InetAddress endereco) {
        for (Dispositivo d : dispositivos) {
            if (d.getEndereco().equals(endereco)) {
                return d;
            }
        }
        return null;
    }

/**
 * Atualiza a lista de dispositivos IoT que possuem uma versão de software
 * inferior à versão atual do gerenciador, enviando a atualização via TCP.
 *
 * @param portaTcp porta TCP para conexão
 */
public void atualizarDispositivos(int portaTcp) {
    ClienteTcp tcp = new ClienteTcp();

    for (Dispositivo d : new ArrayList<>(this.dispositivos)) {
        // se a versão do dispositivo iot for inferior a versão do gerenciador 
        if (d.getVersao() < this.versao) {
            // e se o dispositivo estiver ativo...
            if (tcp.executarAtualizacao(d, this.versao, portaTcp)) {
                // ... atualiza o sistema
                d.setVersao(this.versao);
            } else {
                // ... se não, remove da lista de dispositivos conectados
                this.dispositivos.remove(d);
            }
        }
    }
}


    public int getVersao() {
        return versao;
    }

    public ArrayList<Dispositivo> getDispositivos() {
        return dispositivos;
    }
}
