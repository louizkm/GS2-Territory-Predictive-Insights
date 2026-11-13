package br.edu.fiap.territorio.dominio;

import br.edu.fiap.territorio.excecao.DadosInvalidosException;
import java.util.Comparator;
import java.util.List;

public abstract class EstrategiaPrevisao {
    private final String nome;
    private final int minimoLeituras;

    protected EstrategiaPrevisao(String nome, int minimoLeituras) {
        this.nome = nome;
        this.minimoLeituras = minimoLeituras;
    }

    public abstract ResultadoPrevisao prever(List<Medicao> historico);

    protected List<Medicao> ultimasPrimeiro(List<Medicao> historico) {
        if (historico == null || historico.size() < minimoLeituras) {
            throw new DadosInvalidosException(nome + ": histórico insuficiente (mínimo " + minimoLeituras + " leituras).");
        }
        return historico.stream().sorted(Comparator.comparing(Medicao::getInstante).thenComparingLong(Medicao::getId).reversed()).toList();
    }

    protected double media(List<Medicao> medicoes) {
        return medicoes.stream().mapToDouble(Medicao::getValor).average().orElseThrow();
    }

    public String getNome() { return nome; }
    public int getMinimoLeituras() { return minimoLeituras; }
}
