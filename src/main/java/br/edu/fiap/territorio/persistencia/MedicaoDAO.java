package br.edu.fiap.territorio.persistencia;

import br.edu.fiap.territorio.dominio.Medicao;
import br.edu.fiap.territorio.dominio.TipoSensor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class MedicaoDAO {
    private final BancoDados banco;

    public MedicaoDAO(BancoDados banco) { this.banco = banco; }

    public void salvar(Medicao medicao) throws SQLException {
        String sql = "INSERT INTO medicao (sensor_id, tipo, regiao, valor, instante) VALUES (?, ?, ?, ?, ?)";
        try (Connection conexao = banco.conectar(); PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setString(1, medicao.getSensorId());
            ps.setString(2, medicao.getTipo().name());
            ps.setString(3, medicao.getRegiao());
            ps.setDouble(4, medicao.getValor());
            ps.setTimestamp(5, Timestamp.from(medicao.getInstante()));
            ps.executeUpdate();
        }
    }

    public List<Medicao> listarPorSensor(String sensorId) throws SQLException {
        String sql = "SELECT * FROM medicao WHERE sensor_id = ? ORDER BY instante ASC, id ASC";
        try (Connection conexao = banco.conectar(); PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setString(1, sensorId);
            try (ResultSet resultado = ps.executeQuery()) {
                return coletar(resultado);
            }
        }
    }

    public List<Medicao> listarTudo() throws SQLException {
        String sql = "SELECT * FROM medicao ORDER BY instante DESC, id DESC";
        try (Connection conexao = banco.conectar(); PreparedStatement ps = conexao.prepareStatement(sql);
             ResultSet resultado = ps.executeQuery()) {
            return coletar(resultado);
        }
    }

    private List<Medicao> coletar(ResultSet resultado) throws SQLException {
        List<Medicao> historico = new ArrayList<>();
        while (resultado.next()) {
            historico.add(new Medicao(
                    resultado.getLong("id"), resultado.getString("sensor_id"),
                    TipoSensor.valueOf(resultado.getString("tipo")), resultado.getString("regiao"),
                    resultado.getDouble("valor"), resultado.getTimestamp("instante").toInstant()));
        }
        return historico;
    }
}
