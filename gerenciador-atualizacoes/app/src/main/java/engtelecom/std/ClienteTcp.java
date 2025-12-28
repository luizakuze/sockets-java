package engtelecom.std;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Classe para representa um cliente TCP.
 * Envia a versão do gerenciador para um dispoitivo iot.
 */
public class ClienteTcp {

    /**
     * Envia uma mensagem de atualização a um dispositivo IoT.
     * 
     * @param dispositivo       Dispositivo a ser atualizado
     * @param versaoGerenciador versão atual do gerenciador
     * @param portaTcp          porta TCP da conexão
     */
    public boolean executarAtualizacao(Dispositivo dispositivo, int versaoGerenciador, int portaTcp) {

        // System.out.println("\u2591\u2592\u2592 Inciando cliente TCP
        // \u2592\u2592\u2591");
        InetAddress ipServidor = dispositivo.getEndereco();

        try (Socket conexao = new Socket()) {

            // Timeout para evitar travar caso o dispositivo esteja offline
            conexao.connect(new InetSocketAddress(ipServidor, portaTcp), 800);

            // Envia a versão
            PrintWriter saida = new PrintWriter(conexao.getOutputStream());
            saida.write(versaoGerenciador + "\n");
            saida.flush();

            System.out.println("Dispositivo " + dispositivo.getEndereco() + " atualizado.");
            return true;
        } catch (IOException e) {
            // Foi removido da lista de dispositivos IOT
            System.out.println("Dispositivo " + dispositivo.getEndereco() + " removido.");
            return false;
        }
    }
}