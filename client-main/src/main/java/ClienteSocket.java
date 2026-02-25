import java.io.*;
import java.net.Socket;

public class ClienteSocket {

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public ClienteSocket() {
        try {
            socket = new Socket("localhost", 12345);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            System.out.println("Conectado ao servidor!");
        } catch (IOException e) {
            throw new RuntimeException("Erro ao conectar ao servidor", e);
        }
    }

    public void enviar(String comando) {
        out.println(comando);
        receberResposta();
    }

    private void receberResposta() {
        try {
            String linha;
            while (!(linha = in.readLine()).equals("FIM")) {
                System.out.println(linha);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void fechar() {
        try {
            socket.close();
        } catch (IOException ignored) {}
    }
}