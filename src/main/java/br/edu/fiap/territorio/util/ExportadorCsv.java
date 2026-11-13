package br.edu.fiap.territorio.util;

import br.edu.fiap.territorio.dominio.Medicao;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;

public final class ExportadorCsv {
    private ExportadorCsv() { }

    public static Path exportarMedicoes(List<Medicao> medicoes, Path caminho) throws IOException {
        Files.createDirectories(caminho.toAbsolutePath().getParent());
        StringBuilder csv = new StringBuilder("id;sensor_id;tipo;regiao;valor;instante_utc\n");
        for (Medicao m : medicoes) {
            csv.append(m.getId()).append(';')
                    .append(escapar(m.getSensorId())).append(';')
                    .append(m.getTipo().name()).append(';')
                    .append(escapar(m.getRegiao())).append(';')
                    .append(String.format(Locale.ROOT, "%.2f", m.getValor())).append(';')
                    .append(m.getInstante()).append('\n');
        }
        return Files.writeString(caminho, csv, StandardCharsets.UTF_8);
    }

    private static String escapar(String campo) {
        return "\"" + campo.replace("\"", "\"\"") + "\"";
    }
}
