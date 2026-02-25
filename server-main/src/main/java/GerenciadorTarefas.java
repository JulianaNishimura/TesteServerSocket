import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GerenciadorTarefas {

    public boolean adicionarTarefa(Tarefa tarefa) {
        String sqlComando = "INSERT INTO tarefas (titulo, descricao, concluida) VALUES (?, ?, ?)";

        try (Connection conn = ConexaoBanco.getConnection();
             PreparedStatement statement = conn.prepareStatement(sqlComando)) {

            statement.setString(1, tarefa.getTitulo());
            statement.setString(2, tarefa.getDescricao());
            statement.setBoolean(3, tarefa.isConcluida());
            int linhasAfetadas = statement.executeUpdate();
            if(linhasAfetadas > 0){
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean concluirTarefa(int id) {
        String sqlComando = "UPDATE tarefas SET concluida = true WHERE id = ?";

        try (Connection conn = ConexaoBanco.getConnection();
             PreparedStatement statement = conn.prepareStatement(sqlComando)) {

            statement.setInt(1, id);
            int linhasAfetadas = statement.executeUpdate();
            return linhasAfetadas > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public List<Tarefa> listarTarefas() {
        List<Tarefa> tarefas = new ArrayList<>();
        String sqlComando = "SELECT * FROM tarefas";

        try (Connection conn = ConexaoBanco.getConnection();
             PreparedStatement statement = conn.prepareStatement(sqlComando);
             ResultSet resultado = statement.executeQuery()) {

            while (resultado.next()) {
                tarefas.add(mapearTarefa(resultado));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return tarefas;
    }

    public List<Tarefa> listarTarefasPendentes() {
        List<Tarefa> tarefas = new ArrayList<>();
        String sqlComando = "SELECT * FROM tarefas WHERE concluida = false";

        try (Connection conn = ConexaoBanco.getConnection();
             PreparedStatement statement = conn.prepareStatement(sqlComando);
             ResultSet resultado = statement.executeQuery()) {

            while (resultado.next()) {
                tarefas.add(mapearTarefa(resultado));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return tarefas;
    }

    public List<Tarefa> buscarPorTitulo(String titulo) {
        List<Tarefa> tarefas = new ArrayList<>();
        String sqlComando = "SELECT * FROM tarefas WHERE titulo LIKE ?";

        try (Connection conn = ConexaoBanco.getConnection();
             PreparedStatement statement = conn.prepareStatement(sqlComando)) {

            statement.setString(1, "%" + titulo + "%");

            try (ResultSet resultado = statement.executeQuery()) {
                while (resultado.next()) {
                    tarefas.add(mapearTarefa(resultado));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return tarefas;
    }

    public boolean deletarTarefa(int id){
        String sqlString = "DELETE FROM tarefas WHERE id = ?";

        try(Connection conn = ConexaoBanco.getConnection();
            PreparedStatement statement = conn.prepareStatement(sqlString)) {
            statement.setInt(1,id);
            int linhasAfetadas = statement.executeUpdate();
            if(linhasAfetadas > 0){
                return true;
            } else {
                return false;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return false;
    };

    private Tarefa mapearTarefa(ResultSet rs) throws SQLException {
        Tarefa tarefa = new Tarefa(
                rs.getString("titulo"),
                rs.getString("descricao"),
                rs.getInt("id")
        );
        tarefa.setConcluida(rs.getBoolean("concluida"));
        return tarefa;
    }
}