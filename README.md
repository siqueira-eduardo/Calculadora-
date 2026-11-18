# Calculadora em Java

Projeto introdutório desenvolvido para praticar lógica de programação e estruturas condicionais em Java.

## Funcionalidades

- Leitura de dois números informados pelo usuário, com validação de entradas não numéricas.
- Escolha da operação matemática (1 a 4), repetindo a pergunta se a opção for inválida.
- Adição, subtração, multiplicação e divisão.
- Tratamento específico para divisão por zero.
- Possibilidade de fazer várias operações sem reiniciar o programa.
- Lógica de cálculo separada em um método (`calcular`), coberta por testes.

## Tecnologia

- Java
- Classe `Scanner` para entrada de dados.
- Estrutura `switch` para seleção da operação.
- Testes simples em Java puro, sem bibliotecas externas.

## Como executar

É necessário ter o JDK instalado e disponível no PATH. Na pasta do projeto, execute:

```bash
javac -encoding UTF-8 Calculadora.java
java Calculadora
```

## Testes

```bash
javac -encoding UTF-8 Calculadora.java CalculadoraTest.java
java CalculadoraTest
```

O mesmo teste roda automaticamente no GitHub Actions a cada push.

## Exemplo de uso

O programa solicita dois números e, em seguida, permite escolher uma operação:

1. Adição
2. Subtração
3. Multiplicação
4. Divisão

## Objetivo de aprendizagem

Praticar entrada e saída de dados, variáveis do tipo `double`, estruturas condicionais e tratamento de uma situação matemática inválida (divisão por zero).

## Próximas melhorias possíveis

- Adicionar novas operações (potência, raiz quadrada, resto).
- Migrar os testes para JUnit com Maven ou Gradle.
- Criar uma interface gráfica.

---
Projeto de estudo. A documentação será atualizada conforme o código evoluir.
