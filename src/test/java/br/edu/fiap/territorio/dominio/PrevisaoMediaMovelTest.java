package br.edu.fiap.territorio.dominio;

import br.edu.fiap.territorio.excecao.DadosInvalidosException;
import org.junit.jupiter.api.Test;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class PrevisaoMediaMovelTest {
    private List<Medicao> historico(double... valores) {
        List<Medicao> medicoes = new ArrayList<>();
        for (int i = 0; i < valores.length; i++) {
            medicoes.add(new Medicao(i + 1, "CH-01", TipoSensor.CHUVA,
                    "Bairro X", valores[i], Instant.EPOCH.plusSeconds(i * 60L)));
        }
        return medicoes;
    }

    @Test
    void deveCalcularMediaDasUltimasCincoMedicoes() {
        // Arrange: cinco valores mais recentes = 10, 20, 30, 40, 50.
        List<Medicao> medicoes = historico(110, 10, 20, 30, 40, 50);
        // Act
        ResultadoPrevisao resultado = new PrevisaoMediaMovel().prever(medicoes);
        // Assert
        assertEquals(30.0, resultado.valorPrevisto(), 0.0001);
    }

    @Test
    void deveRejeitarHistoricoComMenosDeCincoLeituras() {
        // Arrange
        List<Medicao> medicoes = historico(10, 20, 30, 40);
        // Act + Assert
        assertThrows(DadosInvalidosException.class, () -> new PrevisaoMediaMovel().prever(medicoes));
    }
}
