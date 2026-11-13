package br.edu.fiap.territorio.dominio;

public class SensorPluviometrico extends SensorSimulado {
    public SensorPluviometrico(String id, String nome, String regiao) {
        super(id, nome, regiao);
    }

    @Override public TipoSensor getTipo() { return TipoSensor.CHUVA; }
    @Override public double lerValor() { return gerarEntre(0, 95); }
}
