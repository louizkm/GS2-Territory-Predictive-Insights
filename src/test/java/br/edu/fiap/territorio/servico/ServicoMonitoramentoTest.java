package br.edu.fiap.territorio.servico;

import br.edu.fiap.territorio.dominio.Alerta;
import br.edu.fiap.territorio.dominio.NivelAlerta;
import br.edu.fiap.territorio.dominio.ResultadoPrevisao;
import br.edu.fiap.territorio.dominio.Sensor;
import br.edu.fiap.territorio.dominio.SensorPluviometrico;
import br.edu.fiap.territorio.persistencia.AlertaDAO;
import br.edu.fiap.territorio.persistencia.MedicaoDAO;
import org.junit.jupiter.api.Test;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ServicoMonitoramentoTest {
    @Test
    void devePersistirAlertaCriticoSemAcessarBancoReal() throws SQLException {
        // Arrange: DAOs mockados com Mockito; nenhuma conexão real.
        MedicaoDAO medicaoDAO = mock(MedicaoDAO.class);
        AlertaDAO alertaDAO = mock(AlertaDAO.class);
        Sensor sensor = new SensorPluviometrico("CH-01", "Pluviômetro", "Zona Norte");
        ServicoMonitoramento servico = new ServicoMonitoramento(medicaoDAO, alertaDAO,
                List.of(sensor), new FabricaPrevisao(), ConfiguracaoSistema.getInstancia());
        // Act
        Optional<Alerta> alerta = servico.avaliarERegistrarAlerta(sensor,
                new ResultadoPrevisao("Média móvel", 85, "chuva forte"));
        // Assert
        assertTrue(alerta.isPresent());
        assertEquals(NivelAlerta.CRITICO, alerta.orElseThrow().getNivel());
        verify(alertaDAO, times(1)).salvar(any(Alerta.class));
        verifyNoInteractions(medicaoDAO);
    }

    @Test
    void naoDevePersistirAlertaAbaixoDaAtencao() throws SQLException {
        // Arrange
        AlertaDAO alertaDAO = mock(AlertaDAO.class);
        Sensor sensor = new SensorPluviometrico("CH-01", "Pluviômetro", "Zona Norte");
        ServicoMonitoramento servico = new ServicoMonitoramento(mock(MedicaoDAO.class), alertaDAO,
                List.of(sensor), new FabricaPrevisao(), ConfiguracaoSistema.getInstancia());
        // Act
        Optional<Alerta> alerta = servico.avaliarERegistrarAlerta(sensor,
                new ResultadoPrevisao("Média móvel", 5, "sem risco"));
        // Assert
        assertTrue(alerta.isEmpty());
        verify(alertaDAO, never()).salvar(any(Alerta.class));
    }
}
