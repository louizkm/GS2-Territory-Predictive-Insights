package br.edu.fiap.territorio.servico;

import br.edu.fiap.territorio.dominio.PrevisaoComparacao;
import br.edu.fiap.territorio.dominio.PrevisaoMediaMovel;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FabricaPrevisaoTest {
    @Test
    void deveCriarEstrategiasDiferentesSemCondicionaisDeTipo() {
        // Arrange
        FabricaPrevisao fabrica = new FabricaPrevisao();
        // Act
        Object media = fabrica.criar(TipoEstrategia.MEDIA_MOVEL);
        Object comparacao = fabrica.criar(TipoEstrategia.COMPARACAO);
        // Assert
        assertInstanceOf(PrevisaoMediaMovel.class, media);
        assertInstanceOf(PrevisaoComparacao.class, comparacao);
    }
}
