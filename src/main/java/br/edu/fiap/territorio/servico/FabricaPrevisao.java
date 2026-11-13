package br.edu.fiap.territorio.servico;

import br.edu.fiap.territorio.dominio.EstrategiaPrevisao;
import br.edu.fiap.territorio.dominio.PrevisaoComparacao;
import br.edu.fiap.territorio.dominio.PrevisaoMediaMovel;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.Supplier;

public class FabricaPrevisao {
    private final Map<TipoEstrategia, Supplier<EstrategiaPrevisao>> criadores =
            new EnumMap<>(TipoEstrategia.class);

    public FabricaPrevisao() {
        criadores.put(TipoEstrategia.MEDIA_MOVEL, PrevisaoMediaMovel::new);
        criadores.put(TipoEstrategia.COMPARACAO, PrevisaoComparacao::new);
    }

    public EstrategiaPrevisao criar(TipoEstrategia tipo) {
        Supplier<EstrategiaPrevisao> fornecedor = criadores.get(tipo);
        if (fornecedor == null) {
            throw new IllegalArgumentException("Estratégia não suportada: " + tipo);
        }
        return fornecedor.get();
    }
}
