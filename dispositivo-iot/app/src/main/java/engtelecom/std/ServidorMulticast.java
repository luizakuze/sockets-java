package engtelecom.std;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Logger;

/**
 * Classe para representar um servidor multicast.
 * Ele envia uma mensagem contendo a versão de software de um dispositivo IOT.
 */
public class ServidorMulticast implements Runnable {
    private static final Logger logger = Logger.getLogger(ServidorMulticast.class.getName());
    private static final int INTERVALO = 10_000; // 10 segundos

    private final int porta;
    private final InetAddress enderecoMulticast;
    private final Dispositivo dispositivo;

    public ServidorMulticast(String enderecoMulticast, int porta, Dispositivo dispositivo)
            throws UnknownHostException {
        this.enderecoMulticast = InetAddress.getByName(enderecoMulticast);
        this.porta = porta;
        this.dispositivo = dispositivo;
    }

    /**
     * Define a tarefa executada pela thread: envia mensagem para endereço multicast
     * contendo a versão de software atualizada pelo dispositivo IOT
     */
    @Override
    public void run() {
        // System.out.println("\u2591\u2592\u2592 Servidor Multicast iniciando
        // \u2592\u2592\u2591");

        try (DatagramSocket datagramSocket = new DatagramSocket()) {

            // Comunicação
            while (true) {
                // Cria a mensagem no formato vx, onde x é a versão do dispositivo
                String mensagem = String.valueOf(dispositivo.getVersao()); // envia só o número
                byte[] buffer = mensagem.getBytes();

                // Envia o pacote multicast
                DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length, enderecoMulticast, porta);
                datagramSocket.send(datagramPacket);

                System.out.println("Dispositivo " + dispositivo.getEndereco().getHostAddress() + " > v" + mensagem);
                Thread.sleep(INTERVALO);
            }

        } catch (Exception e) {
            logger.severe("Erro no servidor multicast: " + e.getMessage());
        }
    }
}
