# Verificação técnica realizada ao preparar os arquivos

## Validado neste ambiente

- [x] Compilação de **todos os arquivos `src/main/java`** com JDK 21 e `javac --release 17`, sem erro de sintaxe ou referência interna.
- [x] Teste de fumaça independente usando as classes reais do projeto: média móvel, comparação de períodos, validação de medição negativa, proteção do código de limiar, criação de alerta crítico e escrita de CSV.
- [x] Presença de 12 métodos de teste automatizado nos 7 arquivos JUnit.

## Ainda exige validação da equipe

- [ ] `mvn clean test` em computador com Maven e acesso às dependências H2, JUnit e Mockito.
- [ ] `mvn exec:java`, navegação no menu, conexão H2 e consultas JDBC reais.
- [ ] Fechar e abrir o aplicativo para comprovar persistência do H2 em arquivo.
- [ ] Inspecionar saída CSV e verificar se o encoding está correto na máquina de entrega.

**Limitação do ambiente utilizado:** há JDK, porém não há Maven nem os JARs H2/JUnit/Mockito disponíveis localmente, e o acesso ao repositório Maven não funcionou. Por isso, **não** afirmamos que os testes Maven ou o fluxo JDBC foram executados aqui. O grupo deve validar antes de submeter o trabalho.
