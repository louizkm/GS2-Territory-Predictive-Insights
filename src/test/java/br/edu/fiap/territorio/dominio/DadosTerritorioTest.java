package br.edu.fiap.territorio.dominio;

import br.edu.fiap.territorio.excecao.DadosInvalidosException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DadosTerritorioTest {
    @Test
    void deveBloquearMudancaDeLimiaresSemCodigoCorreto() {
        // Arrange
        DadosTerritorio dados = new DadosTerritorio("Bairro X", 1000, "SECRETO");
        // Act + Assert
        assertThrows(DadosInvalidosException.class,
                () -> dados.alterarLimiares(TipoSensor.CHUVA, new Limiares(10, 20, 50), "ERRADO"));
        assertEquals(20, dados.getLimiares(TipoSensor.CHUVA).atencao());
    }

    @Test
    void deveRejeitarPopulacaoNegativa() {
        // Arrange
        DadosTerritorio dados = new DadosTerritorio("Bairro X", 1000, "SECRETO");
        // Act + Assert
        assertThrows(DadosInvalidosException.class, () -> dados.definirPopulacaoEstimada(-50));
    }

    @Test
    void deveAtualizarLimiaresComCodigoCorreto() {
        // Arrange
        DadosTerritorio dados = new DadosTerritorio("Bairro X", 1000, "SECRETO");
        // Act
        dados.alterarLimiares(TipoSensor.CHUVA, new Limiares(10, 30, 60), "SECRETO");
        // Assert
        assertEquals(60, dados.getLimiares(TipoSensor.CHUVA).critico());
    }
}
