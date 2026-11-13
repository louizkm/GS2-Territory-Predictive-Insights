package br.edu.fiap.territorio.dominio;

import java.util.List;

public class PontoMonitoramento extends ComponenteTerritorial {
    private final List<Sensor> sensores;

    public PontoMonitoramento(String id, String nome, String regiao, List<Sensor> sensores) {
        super(id, nome, regiao);
        this.sensores = List.copyOf(sensores);
    }

    @Override
    public StatusComponente verificarStatus() {
        boolean todosOperacionais = sensores.stream().allMatch(Sensor::verificarFuncionamento);
        setStatus(todosOperacionais ? StatusComponente.OPERACIONAL : StatusComponente.INDISPONIVEL);
        return getStatus();
    }
}
