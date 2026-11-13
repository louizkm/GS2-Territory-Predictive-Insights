package br.edu.fiap.territorio.servico;

import br.edu.fiap.territorio.dominio.DadosTerritorio;
import br.edu.fiap.territorio.dominio.Limiares;
import br.edu.fiap.territorio.dominio.TipoSensor;

public final class ConfiguracaoSistema {
    private static final ConfiguracaoSistema INSTANCIA = new ConfiguracaoSistema();
    private final DadosTerritorio dados;

    private ConfiguracaoSistema() {
        String codigo = System.getenv("TERRITORIO_CODIGO_ACESSO");
        if (codigo == null || codigo.isBlank()) {
            codigo = "DEMO2026"; // Apenas para demonstração local; ver README.
        }
        dados = new DadosTerritorio("Bairro Exemplo - Zona Norte", 8500, codigo);
    }

    public static ConfiguracaoSistema getInstancia() { return INSTANCIA; }
    public DadosTerritorio getDadosTerritorio() { return dados; }
    public Limiares getLimiares(TipoSensor tipo) { return dados.getLimiares(tipo); }
}
