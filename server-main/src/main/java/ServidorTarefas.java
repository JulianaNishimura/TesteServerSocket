import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class ServidorTarefas extends Thread {

    private ServerSocket serverSocket;

    public ServidorTarefas() {
        try {
            serverSocket = new ServerSocket(12345);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        System.out.println("Servidor iniciado...");

        while (true) {
            try {
                Socket socket = serverSocket.accept();
                System.out.println("Nova conexão realizada");

                new Thread(() -> tratarCliente(socket)).start();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void tratarCliente(Socket socket) {
        GerenciadorTarefas gerenciadorTarefas = new GerenciadorTarefas();

        try (
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream())
                );
                PrintWriter out = new PrintWriter(
                        socket.getOutputStream(), true
                )
        ) {

            String linha;

            while ((linha = in.readLine()) != null) {

                String[] partes = linha.split("\\\\");
                String comando = partes[0];

                switch (comando) {

                    case "adicionar":
                        if (partes.length < 3) {
                            out.println("Formato inválido.");
                            out.println("FIM");
                            break;
                        }

                        Tarefa tarefa = new Tarefa(partes[1], partes[2]);
                        if (gerenciadorTarefas.adicionarTarefa(tarefa)) {
                            out.println("Tarefa adicionada com sucesso.");
                        } else {
                            out.println("Erro ao adicionar tarefa.");
                        }
                        out.println("FIM");
                        break;

                    case "concluir":
                        int idConcluir = Integer.parseInt(partes[1]);

                        if (gerenciadorTarefas.concluirTarefa(idConcluir)) {
                            out.println("Tarefa concluída.");
                        } else {
                            out.println("Tarefa não encontrada.");
                        }
                        out.println("FIM");
                        break;

                    case "listarTodas":
                        List<Tarefa> tarefas = gerenciadorTarefas.listarTarefas();

                        if (tarefas.isEmpty()) {
                            out.println("Nenhuma tarefa cadastrada.");
                        } else {
                            for (Tarefa t : tarefas) {
                                out.println(t);
                            }
                        }
                        out.println("FIM");
                        break;

                    case "listarPendentes":
                        List<Tarefa> pendentes = gerenciadorTarefas.listarTarefasPendentes();

                        if (pendentes.isEmpty()) {
                            out.println("Nenhuma tarefa pendente.");
                        } else {
                            for (Tarefa t : pendentes) {
                                out.println(t);
                            }
                        }
                        out.println("FIM");
                        break;

                    case "buscarPorTitulo":
                        if (partes.length < 2) {
                            out.println("Uso: buscarPorTitulo\\titulo");
                            out.println("FIM");
                            break;
                        }

                        List<Tarefa> encontradas =
                                gerenciadorTarefas.buscarPorTitulo(partes[1]);

                        if (encontradas.isEmpty()) {
                            out.println("Nenhuma tarefa encontrada.");
                        } else {
                            for (Tarefa t : encontradas) {
                                out.println(t);
                            }
                        }
                        out.println("FIM");
                        break;

                    case "deletar":
                        int idDeletar = Integer.parseInt(partes[1]);

                        if (gerenciadorTarefas.deletarTarefa(idDeletar)) {
                            out.println("Tarefa deletada.");
                        } else {
                            out.println("Tarefa não encontrada.");
                        }
                        out.println("FIM");
                        break;

                    default:
                        out.println("Comando não reconhecido.");
                        out.println("FIM");
                }
            }

        } catch (Exception e) {
            System.out.println("Cliente desconectado.");
        }
    }
}