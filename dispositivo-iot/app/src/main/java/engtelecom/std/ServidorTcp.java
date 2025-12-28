package engtelecom.std;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Classe para representar um servidor TCP.
 * Deve receber mensagens do gerenciador (cliente) para atualização de software
 * de dispositivos IOT.
 */
public class ServidorTcp implements Runnable {
    private final int porta;
    private final Dispositivo dispositivo;

    public ServidorTcp(int porta, Dispositivo dispositivo) {
        this.porta = porta;
        this.dispositivo = dispositivo;
    }

    /**
     * Define a tarefa executada pela thread: Escuta a porta TCP e trata cada
     * cliente que se conecta.
     */
    @Override
    public void run() {
        // System.out.println("\u2591\u2592\u2592 Servidor TCP iniciando
        // \u2592\u2592\u2591");
        try (ServerSocket server = new ServerSocket(porta)) {
            while (true) {
                Socket cliente = server.accept();
                atender(cliente);
            }
        } catch (IOException e) {
            System.err.println("Erro no TCP: " + e.getMessage());
        }
        // System.out.println("\u2591\u2592\u2592 Servidor TCP finalizado
        // \u2592\u2592\u2591");
    }

    /**
     * Recebe uma nova versão de software e solicita ao dispositivo IOT que aplique
     * a atualização.
     * 
     * @param sock socket da conexão TCP
     */
    private void atender(Socket sock) {
        try (Socket s = sock;
                BufferedReader entrada = new BufferedReader(new InputStreamReader(s.getInputStream()));
                PrintWriter saida = new PrintWriter(s.getOutputStream())) {

            String recebido = entrada.readLine();
            dispositivo.setVersao(Integer.parseInt(recebido));
            System.out.println("Gerenciador > Atualizando para v" + recebido);

        } catch (IOException e) {
            System.err.println("Erro atendendo cliente TCP: " + e.getMessage());
        }
    }

}
