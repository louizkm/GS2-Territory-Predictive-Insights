package br.edu.fiap.territorio.dominio;

import java.time.Instant;
import java.util.Objects;

public class Alerta {
    private final long id;
    private final String sensorId;
    private final TipoSensor tipo;
    private final NivelAlerta nivel;
    private final double valorPrevisto;
    private final double limiteSuperado;
    private final String justificativa;
    private final Instant instante;

    public Alerta(long id, String sensorId, TipoSensor tipo, NivelAlerta nivel,
                  double valorPrevisto, double limiteSuperado, String justificativa, Instant instante) {
        this.id = id;
        this.sensorId = Objects.requireNonNull(sensorId);
        this.tipo = Objects.requireNonNull(tipo);
        this.nivel = Objects.requireNonNull(nivel);
        this.valorPrevisto = valorPrevisto;
        this.limiteSuperado = limiteSuperado;
        this.justificativa = Objects.requireNonNull(justificativa);
        this.instante = Objects.requireNonNull(instante);
    }

    public long getId() { return id; }
    public String getSensorId() { return sensorId; }
    public TipoSensor getTipo() { return tipo; }
    public NivelAlerta getNivel() { return nivel; }
    public double getValorPrevisto() { return valorPrevisto; }
    public double getLimiteSuperado() { return limiteSuperado; }
    public String getJustificativa() { return justificativa; }
    public Instant getInstante() { return instante; }
}
