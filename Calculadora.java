import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

/**
 * Calculadora com histórico. Esta classe guarda a lógica e o histórico;
 * o método {@code main} é a versão de terminal.
 */
public class Calculadora {

    private final List<String> historico = new ArrayList<>();

    /** Executa a operação e registra a conta no histórico. */
    public double calcular(Operacao operacao, double a, double b) {
        double resultado = operacao.aplicar(a, b);
        historico.add(descrever(operacao, a, b, resultado));
        return resultado;
    }

    /** Contas feitas, da mais antiga para a mais recente (lista somente leitura). */
    public List<String> getHistorico() {
        return Collections.unmodifiableList(historico);
    }

    public void limparHistorico() {
        historico.clear();
    }

    /** Escreve o número sem ".0" quando ele é inteiro (15.0 vira 15). */
    public static String formatar(double valor) {
        if (valor == Math.rint(valor) && Math.abs(valor) < 1e15) {
            return String.format(Locale.ROOT, "%d", (long) valor);
        }
        return String.valueOf(valor);
    }

    public static String descrever(Operacao operacao, double a, double b, double resultado) {
        String conta;
        switch (operacao) {
            case RAIZ_QUADRADA:
                conta = "√" + formatar(a);
                break;
            case PORCENTAGEM:
                conta = formatar(a) + "% de " + formatar(b);
                break;
            default:
                conta = formatar(a) + " " + operacao.getSimbolo() + " " + formatar(b);
        }
        return conta + " = " + formatar(resultado);
    }

    // ---------------------------------------------------------------- terminal

    private static final int OPCAO_HISTORICO = 9;
    private static final int OPCAO_SAIR = 0;

    private static void mostrarMenu() {
        System.out.println();
        System.out.println("Escolha a operação:");
        for (Operacao op : Operacao.values()) {
            System.out.println(op.getCodigo() + ". " + op.getNome());
        }
        System.out.println(OPCAO_HISTORICO + ". Ver histórico");
        System.out.println(OPCAO_SAIR + ". Sair");
    }

    private static int lerOpcao(Scanner scanner) {
        while (true) {
            System.out.print("Digite o número da opção desejada: ");
            if (scanner.hasNextInt()) {
                int opcao = scanner.nextInt();
                if (opcao == OPCAO_SAIR || opcao == OPCAO_HISTORICO || Operacao.deCodigo(opcao) != null) {
                    return opcao;
                }
            } else {
                scanner.next();
            }
            System.out.println("Opção inválida. Escolha uma das opções do menu.");
        }
    }

    private static double lerNumero(Scanner scanner, String mensagem) {
        while (true) {
            System.out.print(mensagem);
            if (scanner.hasNextDouble()) {
                return scanner.nextDouble();
            }
            System.out.println("Entrada inválida. Digite um número.");
            scanner.next();
        }
    }

    private static void mostrarHistorico(Calculadora calc) {
        if (calc.getHistorico().isEmpty()) {
            System.out.println("O histórico está vazio.");
            return;
        }
        System.out.println("Histórico:");
        int i = 1;
        for (String conta : calc.getHistorico()) {
            System.out.println(i++ + ". " + conta);
        }
    }

    public static void main(String[] args) {
        Calculadora calc = new Calculadora();

        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Bem-vindo à Calculadora em Java!");

            while (true) {
                mostrarMenu();
                int opcao = lerOpcao(scanner);

                if (opcao == OPCAO_SAIR) {
                    break;
                }
                if (opcao == OPCAO_HISTORICO) {
                    mostrarHistorico(calc);
                    continue;
                }

                Operacao operacao = Operacao.deCodigo(opcao);
                double a = lerNumero(scanner, operacao.getOperandos() == 1
                        ? "Digite o número: "
                        : "Digite o primeiro número: ");
                double b = 0;
                if (operacao.getOperandos() == 2) {
                    b = lerNumero(scanner, "Digite o segundo número: ");
                }

                try {
                    double resultado = calc.calcular(operacao, a, b);
                    System.out.println("Resultado: " + formatar(resultado));
                } catch (ArithmeticException e) {
                    System.out.println("Erro: " + e.getMessage());
                }
            }

            System.out.println("Até logo!");
        }
    }
}
