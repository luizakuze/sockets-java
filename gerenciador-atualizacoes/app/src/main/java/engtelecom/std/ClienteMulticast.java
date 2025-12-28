package engtelecom.std;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.Inet6Address;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

/**
 * Classe que representa um cliente multicast.
 * Escuta mensagens enviadas por dispositivos IoT e registra o
 * dispositivo no gerenciador
 */
public class ClienteMulticast implements Runnable {

    private static final Logger logger = Logger.getLogger(ClienteMulticast.class.getName());
    private final int BUFFER_SIZE = 256;
    private final String endereco;
    private final int porta;
    private final Gerenciador gerenciador;
    private List<InetAddress> interfacesDeRede;

    public ClienteMulticast(String endereco, int porta, Gerenciador gerenciador) {
        this.endereco = endereco;
        this.porta = porta;
        this.gerenciador = gerenciador;
        this.interfacesDeRede = new ArrayList<>();
    }

    /**
     * Obtém as interfaces de rede conectadas, que representam os dispositivos IoT.
     * 
     * @return Lista de interfaces disponíveis.
     */
    private List<InetAddress> obterInterfacesDeRede() {
        List<InetAddress> interfaces = new ArrayList<>();
        try {
            for (NetworkInterface networkInterface : Collections.list(NetworkInterface.getNetworkInterfaces())) {
                for (InetAddress inetAddress : Collections.list(networkInterface.getInetAddresses())) {
                    if (inetAddress.isLoopbackAddress() || inetAddress instanceof Inet6Address) {
                        continue;
                    }
                    interfaces.add(inetAddress);
                }
            }
        } catch (Exception e) {
            logger.severe("Erro ao obter interfaces: " + e.getMessage());
        }
        return interfaces;
    }

    /**
     * Define a tarefa executada pela thread: escuta mensagens no endereço
     * multicast, recebe a versão do software e registra novos dispositivos IoT na
     * rede.
     */
    @Override
    public void run() {
        // System.out.println("\u2591\u2592\u2592 Cliente Multicast iniciado
        // \u2592\u2592\u2591");
        this.interfacesDeRede = obterInterfacesDeRede();

        try (MulticastSocket multicastSocket = new MulticastSocket(this.porta)) {
            InetAddress enderecoMulticast = InetAddress.getByName(this.endereco);
            InetSocketAddress grupo = new InetSocketAddress(enderecoMulticast, this.porta);

            // Entra no grupo multicast
            for (InetAddress inetAddress : this.interfacesDeRede) {
                multicastSocket.joinGroup(grupo, NetworkInterface.getByInetAddress(inetAddress));
            }

            byte[] buffer = new byte[this.BUFFER_SIZE];
            DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length);

            // Comunicação
            while (true) { // TODO: running
                multicastSocket.receive(datagramPacket);
                // Recebe a versão do software do dispositvo
                String mensagemRecebida = new String(datagramPacket.getData()).trim();
                int versao = Integer.parseInt(mensagemRecebida);
                
                // Registra o dispositivo no gerenciador
                Dispositivo d = new Dispositivo(datagramPacket.getAddress(), versao);
                gerenciador.registrarDispositivo(d);
            }

        } catch (Exception e) {
            logger.severe("Erro no cliente multicast: " + e.getMessage());
        }
    }
}
