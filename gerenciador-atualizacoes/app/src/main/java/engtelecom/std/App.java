package engtelecom.std;

/**
 * Classe principal
 */
public class App {
    private static final String IP_MULTICAST = "231.0.0.1";
    private static final int PORTA_MULTICAST = 1290;
    private static final int PORTA_TCP = 1234;

    public static void main(String[] args) {

        // Cria o gerenciador de atualizações, que mantém os dispositivos IOT 
        Gerenciador g = new Gerenciador();

        // T1: Cliente Multicast
        ClienteMulticast c = new ClienteMulticast(IP_MULTICAST, PORTA_MULTICAST, g);
        new Thread(c).start();

        // T2: CLI
        PromptComandos p = new PromptComandos(PORTA_TCP, g);
        new Thread(p).start();
    }

}
