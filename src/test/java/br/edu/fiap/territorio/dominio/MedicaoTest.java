package br.edu.fiap.territorio.dominio;

import br.edu.fiap.territorio.excecao.DadosInvalidosException;
import org.junit.jupiter.api.Test;
import java.time.Instant;
import static org.junit.jupiter.api.Assertions.*;

class MedicaoTest {
    @Test
    void deveAceitarMedicaoDentroDaFaixa() {
        // Arrange
        double valor = 35.0;
        // Act
        Medicao medicao = new Medicao(1, "CH-01", TipoSensor.CHUVA, "Zona Norte", valor, Instant.EPOCH);
        // Assert
        assertEquals(35.0, medicao.getValor());
    }

    @Test
    void deveLancarExcecaoParaChuvaNegativa() {
        // Arrange
        double valorInvalido = -1;
        // Act + Assert
        assertThrows(DadosInvalidosException.class,
                () -> new Medicao(1, "CH-01", TipoSensor.CHUVA, "Zona Norte", valorInvalido, Instant.EPOCH));
    }
}
