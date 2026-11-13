package br.edu.fiap.territorio.dominio;

public enum NivelAlerta {
    ATENCAO("ATENÇÃO"),
    ALERTA("ALERTA"),
    CRITICO("CRÍTICO");

    private final String rotulo;

    NivelAlerta(String rotulo) { this.rotulo = rotulo; }

    @Override public String toString() { return rotulo; }
}
