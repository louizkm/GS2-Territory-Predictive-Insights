package br.edu.fiap.territorio.dominio;

import java.util.List;

public class PrevisaoMediaMovel extends EstrategiaPrevisao {
    public PrevisaoMediaMovel() {
        super("Média móvel", 5);
    }

    @Override
    public ResultadoPrevisao prever(List<Medicao> historico) {
        List<Medicao> ultimas = ultimasPrimeiro(historico).subList(0, 5);
        double valor = media(ultimas);
        return new ResultadoPrevisao(getNome(), valor,
                "Estimativa baseada na média das 5 leituras mais recentes; não representa previsão meteorológica profissional.");
    }
}
