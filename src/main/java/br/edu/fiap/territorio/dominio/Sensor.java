package br.edu.fiap.territorio.dominio;

public interface Sensor {
    String getId();
    String getNome();
    String getRegiao();
    TipoSensor getTipo();
    double lerValor();
    boolean verificarFuncionamento();
}
