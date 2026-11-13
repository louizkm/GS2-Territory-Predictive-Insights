package br.edu.fiap.territorio.dominio;

public class SensorNivelRio extends SensorSimulado {
    public SensorNivelRio(String id, String nome, String regiao) {
        super(id, nome, regiao);
    }

    @Override public TipoSensor getTipo() { return TipoSensor.NIVEL_RIO; }
    @Override public double lerValor() { return gerarEntre(80, 450); }
}
