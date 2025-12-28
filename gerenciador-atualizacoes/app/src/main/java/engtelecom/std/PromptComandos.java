package engtelecom.std;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Classe para representar um prompt de comando para o usuário.
 * Ela chama a classe Gerenciador para executar as ações selecionadas.
 */
public class PromptComandos implements Runnable {

    private Gerenciador gerenciador;
    private int portaTcp;

    public PromptComandos(int portaTcp, Gerenciador g) {
        this.gerenciador = g;
        this.portaTcp = portaTcp;
    }

    /**
     * Mostra na tela a lista de dispositivos IOT que o gerenciador tem
     * conhecimento.
     */
    private void listarDispositivos() {
        System.out.println("Dispositivos IOT ativos");

        ArrayList<Dispositivo> dispositivos = gerenciador.getDispositivos();

        for (var d : dispositivos) {
            System.out.println("- " + d.getEndereco() + " v" + d.getVersao() + "\n");
        }

        if (dispositivos.size() == 0) {
            System.out.println(" - Nenhum");
        }
    }

    /**
     * Incrementa a versão de software do gerenciador.
     */
    private void incrementarVersao() {
        this.gerenciador.incrementarVersao();
        System.out.println("Gerenciador v" + this.gerenciador.getVersao());
    }

    /**
     * Atualiza dispositivos IOT com a versão do gerenciador.
     */
    private void atualizarTodos() {
        this.gerenciador.atualizarDispositivos(this.portaTcp);
    }

    /**
     * Define a tarefa executada pela thread: interação com o usuário
     */
    @Override
    public void run() {
        @SuppressWarnings("resource")
        Scanner teclado = new Scanner(System.in);
        int opcao = 0;

        do {
            System.out.println("\n\u2591\u2592\u2592 Menu \u2592\u2592\u2591");
            System.out.println("1) Listar dispositivos");
            System.out.println("2) Incrementar versão do gerenciador");
            System.out.println("3) Atualizar dispositivos com versão antiga");
            System.out.println("4) Sair");
            System.out.print("> ");

            String entrada = teclado.nextLine().trim();

            if (!entrada.isEmpty()) {
                try {
                    opcao = Integer.parseInt(entrada);
                } catch (NumberFormatException e) {
                    opcao = -1; // coloca uma opção inválida
                }
            }

            System.out.println("");

            switch (opcao) {
                case 1 -> listarDispositivos();
                case 2 -> incrementarVersao();
                case 3 -> atualizarTodos();
                case 4 -> {
                    System.out.println("Encerrando operação...");
                    System.exit(0);
                }
                default -> System.out.println("Opção inválida. Tente novamente");
            }
        } while (opcao != 4);
    }
}
