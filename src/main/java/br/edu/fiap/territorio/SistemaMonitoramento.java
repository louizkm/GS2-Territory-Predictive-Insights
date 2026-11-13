package br.edu.fiap.territorio;

import br.edu.fiap.territorio.dominio.Alerta;
import br.edu.fiap.territorio.dominio.DadosTerritorio;
import br.edu.fiap.territorio.dominio.Limiares;
import br.edu.fiap.territorio.dominio.Medicao;
import br.edu.fiap.territorio.dominio.PontoMonitoramento;
import br.edu.fiap.territorio.dominio.Sensor;
import br.edu.fiap.territorio.dominio.SensorNivelRio;
import br.edu.fiap.territorio.dominio.SensorPluviometrico;
import br.edu.fiap.territorio.dominio.SensorUmidadeSolo;
import br.edu.fiap.territorio.dominio.TipoSensor;
import br.edu.fiap.territorio.excecao.DadosInvalidosException;
import br.edu.fiap.territorio.excecao.FonteIndisponivelException;
import br.edu.fiap.territorio.persistencia.AlertaDAO;
import br.edu.fiap.territorio.persistencia.BancoDados;
import br.edu.fiap.territorio.persistencia.MedicaoDAO;
import br.edu.fiap.territorio.servico.ConfiguracaoSistema;
import br.edu.fiap.territorio.servico.FabricaPrevisao;
import br.edu.fiap.territorio.servico.ResultadoMonitoramento;
import br.edu.fiap.territorio.servico.ServicoMonitoramento;
import br.edu.fiap.territorio.util.ExportadorCsv;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.sql.SQLException;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class SistemaMonitoramento {
    private static final DateTimeFormatter DATA_FORMATADA = DateTimeFormatter
            .ofPattern("dd/MM/yyyy HH:mm:ss")
            .withZone(ZoneId.of("America/Sao_Paulo"));

    private final Scanner teclado;
    private final ConfiguracaoSistema configuracao;
    private final ServicoMonitoramento monitoramento;
    private final MedicaoDAO medicaoDAO;
    private final AlertaDAO alertaDAO;
    private final PontoMonitoramento ponto;

    private SistemaMonitoramento(Scanner teclado, ConfiguracaoSistema configuracao,
                                ServicoMonitoramento monitoramento, MedicaoDAO medicaoDAO,
                                AlertaDAO alertaDAO, PontoMonitoramento ponto) {
        this.teclado = teclado;
        this.configuracao = configuracao;
        this.monitoramento = monitoramento;
        this.medicaoDAO = medicaoDAO;
        this.alertaDAO = alertaDAO;
        this.ponto = ponto;
    }

    public static void main(String[] args) {
        // H2 gravado em disco: o histórico continua após fechar e reabrir.
        String url = System.getenv("TERRITORIO_DB_URL");
        if (url == null || url.isBlank()) {
            url = "jdbc:h2:file:./data/territorio";
        }
        BancoDados banco = new BancoDados(url);
        try {
            banco.inicializar();
            MedicaoDAO medicaoDAO = new MedicaoDAO(banco);
            AlertaDAO alertaDAO = new AlertaDAO(banco);
            List<Sensor> sensores = List.of(
                    new SensorPluviometrico("CH-01", "Pluviômetro da praça", "Zona Norte"),
                    new SensorNivelRio("RI-01", "Nível do córrego", "Zona Norte"),
                    new SensorUmidadeSolo("SO-01", "Umidade da encosta", "Zona Norte"));
            PontoMonitoramento ponto = new PontoMonitoramento("PT-01", "Núcleo comunitário", "Zona Norte", sensores);
            ConfiguracaoSistema configuracao = ConfiguracaoSistema.getInstancia();
            ServicoMonitoramento servico = new ServicoMonitoramento(medicaoDAO, alertaDAO,
                    sensores, new FabricaPrevisao(), configuracao);
            try (Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8)) {
                new SistemaMonitoramento(scanner, configuracao, servico, medicaoDAO, alertaDAO, ponto).executar();
            }
        } catch (SQLException erro) {
            System.err.println("Não foi possível acessar o banco H2: " + erro.getMessage());
            System.err.println("Confirme a dependência no Maven e a permissão da pasta data/.");
        }
    }

    private void executar() {
        System.out.println("\n=== TERRITORY PREDICTIVE INSIGHTS | CITIZENTECH ===");
        System.out.println("Simulação didática: NÃO utilizar para alertas de defesa civil.");
        boolean aberto = true;
        while (aberto) {
            mostrarMenu();
            if (!teclado.hasNextLine()) {
                System.out.println("Entrada encerrada. Finalizando programa.");
                break;
            }
            String opcao = teclado.nextLine().strip();
            try {
                switch (opcao) {
                    case "1" -> verificarESimularLeitura();
                    case "2" -> executarPrevisoes();
                    case "3" -> mostrarHistorico();
                    case "4" -> gerenciarTerritorio();
                    case "5" -> statusCompleto();
                    case "6" -> gerarDezCiclos();
                    case "7" -> exportarCsv();
                    case "0" -> { aberto = false; System.out.println("Encerrado. Histórico preservado no banco."); }
                    default -> System.out.println("Opção inválida. Escolha um número do menu.");
                }
            } catch (DadosInvalidosException | FonteIndisponivelException erro) {
                System.out.println("Não foi possível concluir a operação: " + erro.getMessage());
            } catch (SQLException erro) {
                System.out.println("Erro de persistência: " + erro.getMessage());
            } catch (IOException erro) {
                System.out.println("Erro de gravação do CSV: " + erro.getMessage());
            } catch (NumberFormatException erro) {
                System.out.println("Entrada numérica inválida. Informe apenas números válidos.");
            }
        }
    }

    private void mostrarMenu() {
        System.out.println("\n1 - Verificar sensores e registrar leituras");
        System.out.println("2 - Rodar previsões e gerar alertas");
        System.out.println("3 - Ver histórico de medições e alertas");
        System.out.println("4 - Gerenciar dados do território e limiares");
        System.out.println("5 - Status completo");
        System.out.println("6 - Simular 10 ciclos de leitura (demonstração)");
        System.out.println("7 - Exportar medições para CSV (bônus)");
        System.out.println("0 - Encerrar programa");
        System.out.print("Escolha: ");
    }

    private void verificarESimularLeitura() throws SQLException {
        System.out.println("Ponto " + ponto.getNome() + ": " + ponto.verificarStatus());
        for (Medicao m : monitoramento.coletarLeituras()) {
            System.out.printf("Leitura registrada: %s = %.2f %s%n",
                    m.getSensorId(), m.getValor(), m.getTipo().getUnidade());
        }
    }

    private void executarPrevisoes() throws SQLException {
        List<ResultadoMonitoramento> resultados = monitoramento.executarPrevisoes();
        if (resultados.isEmpty()) {
            System.out.println("Ainda não há histórico suficiente. São necessárias 5 leituras "
                    + "por sensor para média móvel e 10 para comparação.");
            return;
        }
        for (ResultadoMonitoramento resultado : resultados) {
            System.out.printf("%s | %s | %s: %.2f%n",
                    resultado.sensorId(), resultado.tipo(), resultado.previsao().estrategia(),
                    resultado.previsao().valorPrevisto());
            System.out.println("  Interpretação: " + resultado.previsao().explicacao());
            if (resultado.alerta().isPresent()) {
                System.out.println("  ALERTA: " + resultado.alerta().get().getJustificativa());
            } else {
                System.out.println("  Situação: abaixo do limiar de atenção.");
            }
        }
    }

    private void mostrarHistorico() throws SQLException {
        System.out.println("1 - Medições por sensor | 2 - Todos os alertas | 3 - Todas as medições");
        System.out.print("Escolha: ");
        String escolha = lerLinha();
        switch (escolha) {
            case "1" -> {
                System.out.print("ID do sensor (CH-01, RI-01, SO-01): ");
                mostrarMedicoes(medicaoDAO.listarPorSensor(lerLinha()));
            }
            case "2" -> mostrarAlertas(alertaDAO.listarTodos());
            case "3" -> mostrarMedicoes(medicaoDAO.listarTudo());
            default -> System.out.println("Opção de histórico inválida.");
        }
    }

    private void mostrarMedicoes(List<Medicao> medicoes) {
        if (medicoes.isEmpty()) { System.out.println("Nenhuma medição encontrada."); return; }
        for (Medicao m : medicoes) {
            System.out.printf("#%d | %s | %s | %.2f %s | %s%n", m.getId(), m.getSensorId(),
                    m.getTipo().getDescricao(), m.getValor(), m.getTipo().getUnidade(),
                    DATA_FORMATADA.format(m.getInstante()));
        }
        System.out.println("Total: " + medicoes.size());
    }

    private void mostrarAlertas(List<Alerta> alertas) {
        if (alertas.isEmpty()) { System.out.println("Nenhum alerta emitido."); return; }
        for (Alerta alerta : alertas) {
            System.out.printf("#%d | %s | %s | %s%n", alerta.getId(), alerta.getNivel(),
                    DATA_FORMATADA.format(alerta.getInstante()), alerta.getJustificativa());
        }
        System.out.println("Total: " + alertas.size());
    }

    private void gerenciarTerritorio() {
        DadosTerritorio dados = configuracao.getDadosTerritorio();
        System.out.println("Região: " + dados.getRegiao());
        System.out.println("População estimada: " + dados.getPopulacaoEstimada());
        for (TipoSensor tipo : TipoSensor.values()) {
            Limiares l = dados.getLimiares(tipo);
            System.out.printf("%s (%s): atenção %.2f | alerta %.2f | crítico %.2f%n",
                    tipo.getDescricao(), tipo.getUnidade(), l.atencao(), l.alerta(), l.critico());
        }
        System.out.println("1 - Alterar região | 2 - Alterar população | 3 - Alterar limiares | 0 - Voltar");
        System.out.print("Escolha: ");
        String escolha = lerLinha();
        switch (escolha) {
            case "1" -> {
                System.out.print("Nova região: ");
                dados.definirRegiao(lerLinha());
                System.out.println("Região alterada (válida apenas nesta execução).");
            }
            case "2" -> {
                System.out.print("População estimada: ");
                dados.definirPopulacaoEstimada(Integer.parseInt(lerLinha()));
                System.out.println("População alterada (válida apenas nesta execução).");
            }
            case "3" -> {
                System.out.print("Tipo (CHUVA, NIVEL_RIO, UMIDADE_SOLO): ");
                TipoSensor tipo;
                try {
                    tipo = TipoSensor.valueOf(lerLinha().toUpperCase(java.util.Locale.ROOT));
                } catch (IllegalArgumentException erro) {
                    System.out.println("Tipo de sensor desconhecido.");
                    return;
                }
                System.out.print("Limiar de ATENÇÃO: ");
                double atencao = Double.parseDouble(lerLinha());
                System.out.print("Limiar de ALERTA: ");
                double alerta = Double.parseDouble(lerLinha());
                System.out.print("Limiar CRÍTICO: ");
                double critico = Double.parseDouble(lerLinha());
                System.out.print("Código de acesso: ");
                String codigo = lerLinha();
                dados.alterarLimiares(tipo, new Limiares(atencao, alerta, critico), codigo);
                System.out.println("Limiares atualizados nesta execução.");
            }
            case "0" -> System.out.println("Retornando ao menu.");
            default -> System.out.println("Opção inválida.");
        }
    }

    private void statusCompleto() throws SQLException {
        System.out.println("Ponto monitorado: " + ponto.getNome() + " | status: " + ponto.verificarStatus());
        for (Sensor sensor : monitoramento.getSensores()) {
            List<Medicao> historico = medicaoDAO.listarPorSensor(sensor.getId());
            System.out.printf("Sensor: %s | tipo: %s | ativo: %s | leituras: %d%n",
                    sensor.getId(), sensor.getTipo().getDescricao(), sensor.verificarFuncionamento(), historico.size());
            if (!historico.isEmpty()) {
                Medicao ultima = historico.get(historico.size() - 1);
                System.out.printf("  Última leitura: %.2f %s (%s)%n", ultima.getValor(),
                        ultima.getTipo().getUnidade(), DATA_FORMATADA.format(ultima.getInstante()));
            }
        }
        System.out.println("Alertas emitidos no total: " + alertaDAO.listarTodos().size());
    }

    private void gerarDezCiclos() throws SQLException {
        int total = 0;
        for (int ciclo = 0; ciclo < 10; ciclo++) {
            total += monitoramento.coletarLeituras().size();
        }
        System.out.println("Simulação concluída: " + total + " medições gravadas (10 ciclos x 3 sensores).");
        System.out.println("Agora selecione 2 para conferir as duas estratégias de previsão.");
    }

    private void exportarCsv() throws SQLException, IOException {
        Path caminho = ExportadorCsv.exportarMedicoes(medicaoDAO.listarTudo(),
                Path.of("relatorios", "medicoes.csv"));
        System.out.println("CSV exportado em: " + caminho.toAbsolutePath());
    }

    private String lerLinha() {
        if (!teclado.hasNextLine()) {
            throw new DadosInvalidosException("Entrada finalizada no console.");
        }
        return teclado.nextLine().strip();
    }
}
