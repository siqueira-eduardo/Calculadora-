# Calculadora em Java

Projeto de estudo em Java com a mesma lógica em duas versões: **terminal** e **interface gráfica (Swing)**.

## Funcionalidades

- 8 operações: adição, subtração, multiplicação, divisão, potência, raiz quadrada, resto da divisão e porcentagem.
- Histórico das contas feitas na sessão (no terminal, opção 9; na janela, painel à direita).
- Tratamento de erros: divisão por zero, resto por zero, raiz de número negativo e resultados indefinidos.
- Terminal: valida entradas que não sejam números e repete a pergunta quando a opção é inválida.
- Interface gráfica: teclado com apagar, inverter sinal, vírgula decimal e contas encadeadas.

## Estrutura

| Arquivo | Função |
| --- | --- |
| `Operacao.java` | Enum com as 8 operações e o cálculo de cada uma |
| `Calculadora.java` | Histórico, formatação e a versão de terminal (`main`) |
| `CalculadoraPanel.java` | Painel Swing com a lógica do teclado |
| `CalculadoraGUI.java` | Abre a janela da calculadora (`main`) |
| `CalculadoraTest.java`, `CalculadoraPanelTest.java` | Testes JUnit 5 |

Os arquivos ficam na raiz de propósito, para o projeto continuar simples de estudar. O `pom.xml` está configurado para esse layout.

## Como executar

É necessário ter o JDK 17 ou superior no PATH. Na pasta do projeto:

```bash
javac -encoding UTF-8 Operacao.java Calculadora.java CalculadoraPanel.java CalculadoraGUI.java

java Calculadora      # versão de terminal
java CalculadoraGUI   # versão com janela
```

## Testes

Com o Maven instalado:

```bash
mvn test
```

Os mesmos testes rodam automaticamente no GitHub Actions a cada push.

## Exemplo no terminal

```
Escolha a operação:
1. Adição
2. Subtração
3. Multiplicação
4. Divisão
5. Potência
6. Raiz quadrada
7. Resto da divisão
8. Porcentagem
9. Ver histórico
0. Sair
```

## Próximas melhorias possíveis

- Aceitar expressões completas (por exemplo, `2 + 3 * 4`) respeitando a precedência.
- Atalhos de teclado na interface gráfica.
- Mais operações (logaritmo, fatorial, funções trigonométricas).

---
Projeto de estudo. A documentação será atualizada conforme o código evoluir.
