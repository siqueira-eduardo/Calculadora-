/**
 * Operações disponíveis na calculadora.
 * Cada operação sabe o seu código (usado no menu), o símbolo e quantos números precisa.
 */
public enum Operacao {
    ADICAO(1, "Adição", "+", 2),
    SUBTRACAO(2, "Subtração", "-", 2),
    MULTIPLICACAO(3, "Multiplicação", "*", 2),
    DIVISAO(4, "Divisão", "/", 2),
    POTENCIA(5, "Potência", "^", 2),
    RAIZ_QUADRADA(6, "Raiz quadrada", "√", 1),
    RESTO(7, "Resto da divisão", "mod", 2),
    PORCENTAGEM(8, "Porcentagem", "% de", 2);

    private final int codigo;
    private final String nome;
    private final String simbolo;
    private final int operandos;

    Operacao(int codigo, String nome, String simbolo, int operandos) {
        this.codigo = codigo;
        this.nome = nome;
        this.simbolo = simbolo;
        this.operandos = operandos;
    }

    public int getCodigo() {
        return codigo;
    }

    public String getNome() {
        return nome;
    }

    public String getSimbolo() {
        return simbolo;
    }

    /** Quantidade de números necessários: 1 (raiz quadrada) ou 2 (demais). */
    public int getOperandos() {
        return operandos;
    }

    /** Busca a operação pelo código do menu; devolve null se não existir. */
    public static Operacao deCodigo(int codigo) {
        for (Operacao op : values()) {
            if (op.codigo == codigo) {
                return op;
            }
        }
        return null;
    }

    /**
     * Calcula o resultado. Nas operações de um número só, {@code b} é ignorado.
     *
     * @throws ArithmeticException se a conta não tiver resultado válido
     *                             (divisão por zero, raiz de negativo, etc.)
     */
    public double aplicar(double a, double b) {
        double resultado;
        switch (this) {
            case ADICAO:
                resultado = a + b;
                break;
            case SUBTRACAO:
                resultado = a - b;
                break;
            case MULTIPLICACAO:
                resultado = a * b;
                break;
            case DIVISAO:
                if (b == 0) {
                    throw new ArithmeticException("Divisão por zero.");
                }
                resultado = a / b;
                break;
            case POTENCIA:
                resultado = Math.pow(a, b);
                break;
            case RAIZ_QUADRADA:
                if (a < 0) {
                    throw new ArithmeticException("Não existe raiz quadrada real de número negativo.");
                }
                resultado = Math.sqrt(a);
                break;
            case RESTO:
                if (b == 0) {
                    throw new ArithmeticException("Resto de divisão por zero.");
                }
                resultado = a % b;
                break;
            case PORCENTAGEM:
                resultado = a * b / 100.0;
                break;
            default:
                throw new IllegalStateException("Operação não tratada: " + this);
        }

        if (Double.isNaN(resultado) || Double.isInfinite(resultado)) {
            throw new ArithmeticException("Resultado indefinido.");
        }
        return resultado;
    }
}
