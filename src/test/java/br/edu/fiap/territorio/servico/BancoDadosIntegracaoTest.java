package br.edu.fiap.territorio.servico;

import br.edu.fiap.territorio.dominio.Alerta;
import br.edu.fiap.territorio.dominio.Medicao;
import br.edu.fiap.territorio.dominio.NivelAlerta;
import br.edu.fiap.territorio.dominio.TipoSensor;
import br.edu.fiap.territorio.persistencia.AlertaDAO;
import br.edu.fiap.territorio.persistencia.BancoDados;
import br.edu.fiap.territorio.persistencia.MedicaoDAO;
import org.junit.jupiter.api.Test;
import java.sql.SQLException;
import java.time.Instant;
import static org.junit.jupiter.api.Assertions.*;

class BancoDadosIntegracaoTest {
    @Test
    void deveSalvarEConsultarMedicoesEAlertasNoH2() throws SQLException {
        // Arrange: banco de teste isolado, sem arquivo e sem digitação no console.
        BancoDados banco = new BancoDados("jdbc:h2:mem:testePersistencia;DB_CLOSE_DELAY=-1");
        banco.inicializar();
        MedicaoDAO medicoes = new MedicaoDAO(banco);
        AlertaDAO alertas = new AlertaDAO(banco);
        // Act
        medicoes.salvar(new Medicao(0, "CH-01", TipoSensor.CHUVA,
                "Bairro X", 45, Instant.now()));
        alertas.salvar(new Alerta(0, "CH-01", TipoSensor.CHUVA,
                NivelAlerta.ALERTA, 45, 40, "Chuva superou o limite", Instant.now()));
        // Assert
        assertEquals(1, medicoes.listarPorSensor("CH-01").size());
        assertEquals(1, alertas.listarTodos().size());
    }
}
