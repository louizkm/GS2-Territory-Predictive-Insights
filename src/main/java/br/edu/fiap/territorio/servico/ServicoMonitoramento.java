package br.edu.fiap.territorio.servico;

import br.edu.fiap.territorio.dominio.Alerta;
import br.edu.fiap.territorio.dominio.EstrategiaPrevisao;
import br.edu.fiap.territorio.dominio.Limiares;
import br.edu.fiap.territorio.dominio.Medicao;
import br.edu.fiap.territorio.dominio.NivelAlerta;
import br.edu.fiap.territorio.dominio.ResultadoPrevisao;
import br.edu.fiap.territorio.dominio.Sensor;
import br.edu.fiap.territorio.excecao.DadosInvalidosException;
import br.edu.fiap.territorio.excecao.FonteIndisponivelException;
import br.edu.fiap.territorio.persistencia.AlertaDAO;
import br.edu.fiap.territorio.persistencia.MedicaoDAO;
import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ServicoMonitoramento {
    private final MedicaoDAO medicaoDAO;
    private final AlertaDAO alertaDAO;
    private final List<Sensor> sensores;
    private final FabricaPrevisao fabrica;
    private final ConfiguracaoSistema configuracao;

    public ServicoMonitoramento(MedicaoDAO medicaoDAO, AlertaDAO alertaDAO,
                               List<Sensor> sensores, FabricaPrevisao fabrica,
                               ConfiguracaoSistema configuracao) {
        this.medicaoDAO = medicaoDAO;
        this.alertaDAO = alertaDAO;
        this.sensores = List.copyOf(sensores);
        this.fabrica = fabrica;
        this.configuracao = configuracao;
    }

    public List<Medicao> coletarLeituras() throws SQLException {
        List<Medicao> coletadas = new ArrayList<>();
        for (Sensor sensor : sensores) {
            if (!sensor.verificarFuncionamento()) {
                throw new FonteIndisponivelException("Sensor " + sensor.getId() + " indisponível.");
            }
            double valor = sensor.lerValor();
            Medicao medicao = new Medicao(0, sensor.getId(), sensor.getTipo(),
                    sensor.getRegiao(), valor, Instant.now());
            medicaoDAO.salvar(medicao);
            coletadas.add(medicao);
        }
        return coletadas;
    }

    public List<ResultadoMonitoramento> executarPrevisoes() throws SQLException {
        List<ResultadoMonitoramento> resultados = new ArrayList<>();
        for (Sensor sensor : sensores) {
            List<Medicao> historico = medicaoDAO.listarPorSensor(sensor.getId());
            for (TipoEstrategia tipoEstrategia : TipoEstrategia.values()) {
                EstrategiaPrevisao estrategia = fabrica.criar(tipoEstrategia);
                if (historico.size() < estrategia.getMinimoLeituras()) {
                    continue; // Falta de dados: não prever ou emitir alerta.
                }
                ResultadoPrevisao previsao = estrategia.prever(historico);
                Optional<Alerta> alerta = avaliarERegistrarAlerta(sensor, previsao);
                resultados.add(new ResultadoMonitoramento(sensor.getId(), sensor.getTipo().getDescricao(),
                        previsao, alerta));
            }
        }
        return resultados;
    }

    public Optional<Alerta> avaliarERegistrarAlerta(Sensor sensor, ResultadoPrevisao previsao)
            throws SQLException {
        if (!Double.isFinite(previsao.valorPrevisto()) || previsao.valorPrevisto() < 0) {
            throw new DadosInvalidosException("A previsão produziu um valor inválido.");
        }
        Limiares limites = configuracao.getLimiares(sensor.getTipo());
        double previsto = previsao.valorPrevisto();
        NivelAlerta nivel;
        double limite;
        if (previsto >= limites.critico()) {
            nivel = NivelAlerta.CRITICO;
            limite = limites.critico();
        } else if (previsto >= limites.alerta()) {
            nivel = NivelAlerta.ALERTA;
            limite = limites.alerta();
        } else if (previsto >= limites.atencao()) {
            nivel = NivelAlerta.ATENCAO;
            limite = limites.atencao();
        } else {
            return Optional.empty();
        }
        String justificativa = String.format(java.util.Locale.ROOT,
                "Sensor %s (%s): previsão %s = %.2f %s, atingiu limiar %s = %.2f %s.",
                sensor.getId(), sensor.getTipo().getDescricao(), previsao.estrategia(), previsto,
                sensor.getTipo().getUnidade(), nivel, limite, sensor.getTipo().getUnidade());
        Alerta alerta = new Alerta(0, sensor.getId(), sensor.getTipo(),
                nivel, previsto, limite, justificativa, Instant.now());
        alertaDAO.salvar(alerta);
        return Optional.of(alerta);
    }

    public List<Sensor> getSensores() { return sensores; }
}
