package br.edu.fiap.territorio.dominio;

import br.edu.fiap.territorio.excecao.DadosInvalidosException;
import java.time.Instant;
import java.util.Objects;

public class Medicao {
    private final long id;
    private final String sensorId;
    private final TipoSensor tipo;
    private final String regiao;
    private final double valor;
    private final Instant instante;

    public Medicao(long id, String sensorId, TipoSensor tipo, String regiao, double valor, Instant instante) {
        Objects.requireNonNull(sensorId, "sensorId");
        Objects.requireNonNull(tipo, "tipo");
        Objects.requireNonNull(regiao, "regiao");
        this.instante = Objects.requireNonNull(instante, "instante");
        if (sensorId.isBlank() || regiao.isBlank() || !Double.isFinite(valor)
                || valor < tipo.getMinimo() || valor > tipo.getMaximo()) {
            throw new DadosInvalidosException("Medição inválida para " + tipo.getDescricao()
                    + ": valor " + valor + " fora de [" + tipo.getMinimo() + ", " + tipo.getMaximo() + "].");
        }
        this.id = id;
        this.sensorId = sensorId;
        this.tipo = tipo;
        this.regiao = regiao;
        this.valor = valor;
    }

    public long getId() { return id; }
    public String getSensorId() { return sensorId; }
    public TipoSensor getTipo() { return tipo; }
    public String getRegiao() { return regiao; }
    public double getValor() { return valor; }
    public Instant getInstante() { return instante; }
}
