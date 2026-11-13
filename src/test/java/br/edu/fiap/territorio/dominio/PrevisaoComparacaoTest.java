package br.edu.fiap.territorio.dominio;

import org.junit.jupiter.api.Test;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class PrevisaoComparacaoTest {
    @Test
    void deveProjetarTendenciaEntreDoisPeriodos() {
        // Arrange: período antigo com média 10 e recente com média 30 => projeção = 50.
        List<Medicao> medicoes = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            medicoes.add(new Medicao(i + 1, "CH-01", TipoSensor.CHUVA,
                    "Bairro X", i < 5 ? 10 : 30, Instant.EPOCH.plusSeconds(i * 60L)));
        }
        // Act
        ResultadoPrevisao resultado = new PrevisaoComparacao().prever(medicoes);
        // Assert
        assertEquals(50.0, resultado.valorPrevisto(), 0.0001);
    }
}
