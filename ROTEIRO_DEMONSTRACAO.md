# Roteiro de demonstração / defesa individual

## Antes de iniciar

Na raiz do projeto, instalar JDK 17 e Maven. Executar `mvn clean test` e mostrar o resultado.
Não compartilhe o arquivo do banco `data/territorio.mv.db` com dados aleatórios de ensaios, a menos que a equipe deseje demonstrar histórico pré-gravado.

## Passo a passo no menu

1. `mvn exec:java`; confirmar mensagem inicial e opções.
2. Opção `5`: ver o ponto territorial e os três sensores, com contagem inicial de medições.
3. Opção `6`: simular 10 ciclos, gravando 30 medições para os três tipos.
4. Opção `2`: demonstrar **duas previsões polimórficas** e alertas classificados com justificativas explícitas. Se todos ficarem abaixo do limiar, ajuste limiares no menu 4 para a demonstração; jamais fabrique um dado alegando ser real.
5. Opção `3`, subopção `1`: listar histórico do sensor `CH-01`; depois subopção `2`: listar alertas persistidos.
6. Opção `4`, subopção `3`: tentar código inválido, confirmar exceção com mensagem; depois executar com código demonstrativo (`DEMO2026`, se variável de ambiente não alterada).
7. Opção `7`: exportar para `relatorios/medicoes.csv`; abrir arquivo e mostrar medições.
8. Opção `0`: encerrar. Abrir o programa novamente e repetir opção `3` para provar que os registros permaneceram no banco.

## Perguntas que cada integrante deve saber responder

- Por que `ComponenteTerritorial` é abstrata e qual classe a estende?
- Qual a diferença entre interface `Sensor` e a classe abstrata `SensorSimulado`?
- Como o encapsulamento protege os limiares de alertas?
- Em que ponto uma medição negativa lança `DadosInvalidosException`?
- Como uma falha de comunicação dispara `FonteIndisponivelException`?
- Onde se usa `PreparedStatement` e onde se fecham as conexões JDBC?
- Qual a diferença entre a média das últimas 5 medições e a comparação 5 versus 5?
- Onde há polimorfismo (sem `instanceof`) ao executar a previsão?
- Por que `FabricaPrevisao` é Factory? Por que `ConfiguracaoSistema` é Singleton?
- Qual teste usa `assertThrows`? Qual usa Mockito? Como um teste numérico falha ao errar a média?
- O que seria necessário para transformar esta simulação em sistema seguro para uso real?

## Limitações que não devem ser escondidas

- Simulador não usa previsão real nem amostragem em intervalos de tempo reais.
- Limiares são de demonstração, não parâmetros oficiais.
- Cadastro e limiares são atualizados apenas durante a execução; medições/alertas são persistidos.
- Este protótipo foi preparado com apoio de IA: revisar, compreender e adaptar antes da avaliação.
