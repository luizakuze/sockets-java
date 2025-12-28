package engtelecom.std;

import java.net.InetAddress;

/**
 * Classe principal
 */
public class App {
    private static final String IP_MULTICAST = "231.0.0.1";
    private static final int PORTA_MULTICAST = 1290;
    private static final int PORTA_TCP = 1234;

    /**
     * Obtém a versão inicial do dispositivo IoT.
     * Regras:
     * - Se o usuário informar um argumento numérico, usa esse valor.
     * - Se o argumento for inválido, usa o último octeto do IP.
     * - Se nenhum argumento for informado, usa o último octeto do IP.
     * 
     * @param args          argumentos recebidos via linha de comando
     * @param enderecoLocal IP que será utilizado para extrair o último octeto
     * @return versão inicial do dispositivo IOT
     */
    private static int obterVersaoInicial(String[] args, InetAddress enderecoLocal) {
        String ipLocal = enderecoLocal.getHostAddress();

        // Caso o usuário passe um argumento válido, usa esse valor como versão inicial
        if (args.length == 1) {
            try {
                return Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                int fallback = ultimoOcteto(ipLocal);
                System.out.println("Argumento inválido. Usando versão baseada no IP: v" + fallback);
                return fallback;
            }
        }

        // Se não passar argumento, usa o último octeto do IP como versão inicial
        return ultimoOcteto(ipLocal);
    }

    /**
     * Extrai o último octeto do IP (ex.: 172.18.0.5 -> 5)
     * 
     * @param ip IP que será utilizado
     * @return último octeto
     */
    private static int ultimoOcteto(String ip) {
        String[] partes = ip.split("\\.");
        return Integer.parseInt(partes[partes.length - 1]);
    }

    public static void main(String[] args) throws Exception {

        // Obtém o endereço e a versão inicial do dispositivo IOT
        InetAddress enderecoLocal = InetAddress.getLocalHost();
        int versaoInicial = obterVersaoInicial(args, enderecoLocal);

        // Mostra qual versão o dispositivo vai iniciar
        System.out.println("Dispositivo " + enderecoLocal + " iniciado em v" + versaoInicial);

        // Criando um dispositivo IOT que contém um endereço IP e uma versão de software
        // inicial
        Dispositivo d = new Dispositivo(enderecoLocal, versaoInicial);

        // T1: Multicast
        new Thread(new ServidorMulticast(IP_MULTICAST, PORTA_MULTICAST, d)).start();

        // T2: TCP
        new Thread(new ServidorTcp(PORTA_TCP, d)).start();
    }

}
