/**
 * Testes simples, sem bibliotecas externas.
 * Execute com: javac Calculadora.java CalculadoraTest.java && java CalculadoraTest
 */
public class CalculadoraTest {

    private static int falhas = 0;

    private static void verificar(String nome, boolean condicao) {
        if (condicao) {
            System.out.println("OK    " + nome);
        } else {
            System.out.println("FALHA " + nome);
            falhas++;
        }
    }

    private static boolean lancou(Class<? extends Exception> tipo, Runnable codigo) {
        try {
            codigo.run();
            return false;
        } catch (Exception e) {
            return tipo.isInstance(e);
        }
    }

    public static void main(String[] args) {
        verificar("adição", Calculadora.calcular(2, 3, 1) == 5);
        verificar("subtração", Calculadora.calcular(2, 3, 2) == -1);
        verificar("multiplicação", Calculadora.calcular(2, 3, 3) == 6);
        verificar("divisão", Calculadora.calcular(6, 3, 4) == 2);
        verificar("divisão por zero",
                lancou(ArithmeticException.class, () -> Calculadora.calcular(1, 0, 4)));
        verificar("operação inválida",
                lancou(IllegalArgumentException.class, () -> Calculadora.calcular(1, 1, 9)));

        if (falhas > 0) {
            System.out.println(falhas + " teste(s) falharam.");
            System.exit(1);
        }
        System.out.println("Todos os testes passaram.");
    }
}
