package br.edu.fiap.territorio.dominio;

import br.edu.fiap.territorio.excecao.FonteIndisponivelException;
import java.util.Random;

public abstract class SensorSimulado implements Sensor {
    private final String id;
    private final String nome;
    private final String regiao;
    private final Random random;
    private boolean disponivel = true;

    protected SensorSimulado(String id, String nome, String regiao) {
        this(id, nome, regiao, new Random());
    }

    protected SensorSimulado(String id, String nome, String regiao, Random random) {
        this.id = id;
        this.nome = nome;
        this.regiao = regiao;
        this.random = random;
    }

    protected double gerarEntre(double minimo, double maximo) {
        if (!disponivel) {
            throw new FonteIndisponivelException("Fonte indisponível: sensor " + id + " sem comunicação.");
        }
        return minimo + random.nextDouble() * (maximo - minimo);
    }

    public void setDisponivel(boolean disponivel) { this.disponivel = disponivel; }
    @Override public String getId() { return id; }
    @Override public String getNome() { return nome; }
    @Override public String getRegiao() { return regiao; }
    @Override public boolean verificarFuncionamento() { return disponivel; }
}
