package br.edu.fiap.territorio.servico;

import br.edu.fiap.territorio.dominio.Alerta;
import br.edu.fiap.territorio.dominio.ResultadoPrevisao;
import java.util.Optional;

public record ResultadoMonitoramento(String sensorId, String tipo, ResultadoPrevisao previsao,
                                     Optional<Alerta> alerta) { }
