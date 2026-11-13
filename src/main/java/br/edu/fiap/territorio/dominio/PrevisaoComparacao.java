package br.edu.fiap.territorio.dominio;

import java.util.List;

public class PrevisaoComparacao extends EstrategiaPrevisao {
    public PrevisaoComparacao() {
        super("Comparação de períodos", 10);
    }

    @Override
    public ResultadoPrevisao prever(List<Medicao> historico) {
        List<Medicao> ordenadas = ultimasPrimeiro(historico);
        double atual = media(ordenadas.subList(0, 5));
        double anterior = media(ordenadas.subList(5, 10));
        double tendencia = atual - anterior;
        double estimativa = Math.max(0, atual + tendencia);
        TipoSensor tipo = ordenadas.get(0).getTipo();
        estimativa = Math.min(tipo.getMaximo(), estimativa);
        String descricao = String.format(java.util.Locale.ROOT,
                "Média recente %.2f versus período anterior %.2f; tendência %+.2f %s.",
                atual, anterior, tendencia, tipo.getUnidade());
        return new ResultadoPrevisao(getNome(), estimativa, descricao);
    }
}
