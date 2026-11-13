package br.edu.fiap.territorio.dominio;

import br.edu.fiap.territorio.excecao.DadosInvalidosException;
import java.util.EnumMap;
import java.util.Map;

public class DadosTerritorio {
    private String regiao;
    private int populacaoEstimada;
    private final String codigoAcesso;
    private final Map<TipoSensor, Limiares> limiares = new EnumMap<>(TipoSensor.class);

    public DadosTerritorio(String regiao, int populacaoEstimada, String codigoAcesso) {
        if (codigoAcesso == null || codigoAcesso.isBlank()) {
            throw new DadosInvalidosException("Código de acesso não pode estar em branco.");
        }
        this.codigoAcesso = codigoAcesso;
        definirRegiao(regiao);
        definirPopulacaoEstimada(populacaoEstimada);
        limiares.put(TipoSensor.CHUVA, new Limiares(20, 40, 70));
        limiares.put(TipoSensor.NIVEL_RIO, new Limiares(150, 250, 350));
        limiares.put(TipoSensor.UMIDADE_SOLO, new Limiares(60, 75, 90));
    }

    public void definirRegiao(String novaRegiao) {
        if (novaRegiao == null || novaRegiao.isBlank()) {
            throw new DadosInvalidosException("Região não pode estar vazia.");
        }
        this.regiao = novaRegiao.strip();
    }

    public void definirPopulacaoEstimada(int novaPopulacao) {
        if (novaPopulacao < 0) {
            throw new DadosInvalidosException("População estimada não pode ser negativa.");
        }
        this.populacaoEstimada = novaPopulacao;
    }

    public void alterarLimiares(TipoSensor tipo, Limiares novos, String codigoInformado) {
        if (!codigoAcesso.equals(codigoInformado)) {
            throw new DadosInvalidosException("Código de acesso incorreto: limiares não alterados.");
        }
        if (novos.critico() > tipo.getMaximo()) {
            throw new DadosInvalidosException("Limiar crítico acima da faixa do sensor (" + tipo.getMaximo() + ").");
        }
        limiares.put(tipo, novos);
    }

    public Limiares getLimiares(TipoSensor tipo) { return limiares.get(tipo); }
    public String getRegiao() { return regiao; }
    public int getPopulacaoEstimada() { return populacaoEstimada; }
}
