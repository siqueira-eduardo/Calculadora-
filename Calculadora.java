import java.util.Scanner;

public class Calculadora {

    /**
     * Executa a operação escolhida (1 a 4).
     *
     * @throws ArithmeticException      se for uma divisão por zero
     * @throws IllegalArgumentException se a operação não existir
     */
    public static double calcular(double num1, double num2, int operacao) {
        switch (operacao) {
            case 1:
                return num1 + num2;
            case 2:
                return num1 - num2;
            case 3:
                return num1 * num2;
            case 4:
                if (num2 == 0) {
                    throw new ArithmeticException("Divisão por zero.");
                }
                return num1 / num2;
            default:
                throw new IllegalArgumentException("Operação inválida: " + operacao);
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

    private static int lerOperacao(Scanner scanner) {
        while (true) {
            System.out.print("Digite o número da operação desejada: ");
            if (scanner.hasNextInt()) {
                int escolha = scanner.nextInt();
                if (escolha >= 1 && escolha <= 4) {
                    return escolha;
                }
            } else {
                scanner.next();
            }
            System.out.println("Escolha inválida. Digite um número de 1 a 4.");
        }
    }

    private static boolean desejaContinuar(Scanner scanner) {
        while (true) {
            System.out.print("Deseja fazer outra operação? (s/n): ");
            String resposta = scanner.next().trim().toLowerCase();
            if (resposta.equals("s")) {
                return true;
            }
            if (resposta.equals("n")) {
                return false;
            }
            System.out.println("Digite apenas s ou n.");
        }
    }

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Bem-vindo à Calculadora em Java!");

            do {
                double num1 = lerNumero(scanner, "Digite o primeiro número: ");
                double num2 = lerNumero(scanner, "Digite o segundo número: ");

                System.out.println("Escolha a operação:");
                System.out.println("1. Adição");
                System.out.println("2. Subtração");
                System.out.println("3. Multiplicação");
                System.out.println("4. Divisão");
                int escolha = lerOperacao(scanner);

                try {
                    System.out.println("Resultado: " + calcular(num1, num2, escolha));
                } catch (ArithmeticException e) {
                    System.out.println("Erro: " + e.getMessage());
                }
            } while (desejaContinuar(scanner));

            System.out.println("Até logo!");
        }
    }
}
