package br.edu.fiap.territorio.dominio;

import br.edu.fiap.territorio.excecao.DadosInvalidosException;

public record Limiares(double atencao, double alerta, double critico) {
    public Limiares {
        if (!Double.isFinite(atencao) || !Double.isFinite(alerta) || !Double.isFinite(critico)
                || atencao < 0 || atencao >= alerta || alerta >= critico) {
            throw new DadosInvalidosException("Limiares inválidos: informe 0 <= atenção < alerta < crítico.");
        }
    }
}
