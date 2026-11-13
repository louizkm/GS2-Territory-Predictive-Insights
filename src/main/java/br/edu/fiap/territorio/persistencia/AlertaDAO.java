package br.edu.fiap.territorio.persistencia;

import br.edu.fiap.territorio.dominio.Alerta;
import br.edu.fiap.territorio.dominio.NivelAlerta;
import br.edu.fiap.territorio.dominio.TipoSensor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class AlertaDAO {
    private final BancoDados banco;

    public AlertaDAO(BancoDados banco) { this.banco = banco; }

    public void salvar(Alerta alerta) throws SQLException {
        String sql = """
            INSERT INTO alerta (sensor_id, tipo, nivel, valor_previsto, limite_superado, justificativa, instante)
            VALUES (?, ?, ?, ?, ?, ?, ?)
            """;
        try (Connection conexao = banco.conectar(); PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setString(1, alerta.getSensorId());
            ps.setString(2, alerta.getTipo().name());
            ps.setString(3, alerta.getNivel().name());
            ps.setDouble(4, alerta.getValorPrevisto());
            ps.setDouble(5, alerta.getLimiteSuperado());
            ps.setString(6, alerta.getJustificativa());
            ps.setTimestamp(7, Timestamp.from(alerta.getInstante()));
            ps.executeUpdate();
        }
    }

    public List<Alerta> listarTodos() throws SQLException {
        String sql = "SELECT * FROM alerta ORDER BY instante DESC, id DESC";
        try (Connection conexao = banco.conectar(); PreparedStatement ps = conexao.prepareStatement(sql);
             ResultSet resultado = ps.executeQuery()) {
            List<Alerta> alertas = new ArrayList<>();
            while (resultado.next()) {
                alertas.add(new Alerta(
                        resultado.getLong("id"), resultado.getString("sensor_id"),
                        TipoSensor.valueOf(resultado.getString("tipo")),
                        NivelAlerta.valueOf(resultado.getString("nivel")),
                        resultado.getDouble("valor_previsto"), resultado.getDouble("limite_superado"),
                        resultado.getString("justificativa"), resultado.getTimestamp("instante").toInstant()));
            }
            return alertas;
        }
    }
}
