import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        ClienteSocket cliente = new ClienteSocket();
        Scanner scanner = new Scanner(System.in);
        boolean flag = true;

        while (flag) {
            System.out.println("\n1 - Adicionar");
            System.out.println("2 - Concluir");
            System.out.println("3 - Listar todas");
            System.out.println("4 - Listar pendentes");
            System.out.println("5 - Buscar por título");
            System.out.println("6 - Deletar");
            System.out.println("0 - Sair");

            System.out.print("Escolha: ");
            int op = scanner.nextInt();
            scanner.nextLine();

            switch (op) {
                case 1:
                    System.out.print("Título: ");
                    String titulo = scanner.nextLine();
                    System.out.print("Descrição: ");
                    String desc = scanner.nextLine();
                    cliente.enviar("adicionar\\" + titulo + "\\" + desc);
                    break;

                case 2:
                    System.out.print("ID: ");
                    int idConcluir = scanner.nextInt();
                    scanner.nextLine();
                    cliente.enviar("concluir\\" + idConcluir);
                    break;

                case 3:
                    cliente.enviar("listarTodas");
                    break;

                case 4:
                    cliente.enviar("listarPendentes");
                    break;

                case 5:
                    System.out.print("Título: ");
                    String tituloBusca = scanner.nextLine();
                    cliente.enviar("buscarPorTitulo\\" + tituloBusca);
                    break;

                case 6:
                    System.out.print("ID: ");
                    int idDeletar = scanner.nextInt();
                    scanner.nextLine();
                    cliente.enviar("deletar\\" + idDeletar);
                    break;

                case 0:
                    cliente.fechar();
                    flag = false;
                    break;

                default:
                    System.out.println("Opção inválida!");
            }
        }
    }
}