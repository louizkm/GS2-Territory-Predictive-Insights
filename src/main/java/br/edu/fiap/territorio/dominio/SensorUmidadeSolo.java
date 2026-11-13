package br.edu.fiap.territorio.dominio;

public class SensorUmidadeSolo extends SensorSimulado {
    public SensorUmidadeSolo(String id, String nome, String regiao) {
        super(id, nome, regiao);
    }

    @Override public TipoSensor getTipo() { return TipoSensor.UMIDADE_SOLO; }
    @Override public double lerValor() { return gerarEntre(35, 98); }
}
