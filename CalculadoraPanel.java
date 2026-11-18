import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

/**
 * Painel da calculadora gráfica. Toda a lógica passa por {@link #pressionar(String)},
 * o que permite testar sem abrir janela.
 */
public class CalculadoraPanel extends JPanel {

    private static final String[][] TECLAS = {
        {"C", "←", "±", "÷"},
        {"7", "8", "9", "×"},
        {"4", "5", "6", "-"},
        {"1", "2", "3", "+"},
        {"0", ",", "√", "="},
        {"xʸ", "mod", "% de", "Limpar histórico"},
    };

    private final Calculadora calculadora = new Calculadora();

    private final JLabel visor = new JLabel("0", SwingConstants.RIGHT);
    private final JLabel expressao = new JLabel(" ", SwingConstants.RIGHT);
    private final JTextArea areaHistorico = new JTextArea();

    private String entrada = "0";          // número que está sendo digitado (com ponto decimal)
    private Double acumulado = null;       // primeiro número da conta
    private Operacao pendente = null;      // operação aguardando o segundo número
    private boolean novaEntrada = true;    // próximo dígito começa um número novo
    private boolean mostrandoErro = false;

    public CalculadoraPanel() {
        super(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        visor.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 32));
        expressao.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 13));
        expressao.setForeground(Color.GRAY);

        JPanel topo = new JPanel(new GridLayout(2, 1));
        topo.add(expressao);
        topo.add(visor);
        topo.setBorder(BorderFactory.createEmptyBorder(4, 8, 8, 8));

        JPanel teclado = new JPanel(new GridLayout(TECLAS.length, 4, 6, 6));
        for (String[] linha : TECLAS) {
            for (String tecla : linha) {
                JButton botao = new JButton(tecla);
                botao.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));
                botao.setName(tecla);
                botao.addActionListener(e -> pressionar(tecla));
                teclado.add(botao);
            }
        }

        JPanel calculo = new JPanel(new BorderLayout(0, 8));
        calculo.add(topo, BorderLayout.NORTH);
        calculo.add(teclado, BorderLayout.CENTER);

        areaHistorico.setEditable(false);
        areaHistorico.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        areaHistorico.setText("Histórico vazio");
        JScrollPane rolagem = new JScrollPane(areaHistorico);
        rolagem.setPreferredSize(new Dimension(220, 0));
        rolagem.setBorder(BorderFactory.createTitledBorder("Histórico"));

        add(calculo, BorderLayout.CENTER);
        add(rolagem, BorderLayout.EAST);
        atualizarTela();
    }

    // ------------------------------------------------------------------ lógica

    /** Trata o clique em uma tecla (o texto é o mesmo que aparece no botão). */
    public void pressionar(String tecla) {
        if (mostrandoErro && !tecla.equals("Limpar histórico")) {
            limparTudo();
        }

        switch (tecla) {
            case "C":
                limparTudo();
                break;
            case "←":
                apagarUltimo();
                break;
            case "±":
                inverterSinal();
                break;
            case ",":
                digitarVirgula();
                break;
            case "+":
                escolherOperacao(Operacao.ADICAO);
                break;
            case "-":
                escolherOperacao(Operacao.SUBTRACAO);
                break;
            case "×":
                escolherOperacao(Operacao.MULTIPLICACAO);
                break;
            case "÷":
                escolherOperacao(Operacao.DIVISAO);
                break;
            case "xʸ":
                escolherOperacao(Operacao.POTENCIA);
                break;
            case "mod":
                escolherOperacao(Operacao.RESTO);
                break;
            case "% de":
                escolherOperacao(Operacao.PORCENTAGEM);
                break;
            case "√":
                aplicarRaiz();
                break;
            case "=":
                igual();
                break;
            case "Limpar histórico":
                calculadora.limparHistorico();
                break;
            default:
                if (tecla.length() == 1 && Character.isDigit(tecla.charAt(0))) {
                    digitar(tecla);
                }
        }
        atualizarTela();
    }

    private void digitar(String digito) {
        if (novaEntrada) {
            entrada = digito;
            novaEntrada = false;
        } else if (entrada.equals("0")) {
            entrada = digito;
        } else if (entrada.replace("-", "").replace(".", "").length() < 15) {
            entrada += digito;
        }
    }

    private void digitarVirgula() {
        if (novaEntrada) {
            entrada = "0.";
            novaEntrada = false;
        } else if (!entrada.contains(".")) {
            entrada += ".";
        }
    }

    private void apagarUltimo() {
        if (novaEntrada) {
            return;
        }
        entrada = entrada.substring(0, entrada.length() - 1);
        if (entrada.isEmpty() || entrada.equals("-")) {
            entrada = "0";
            novaEntrada = true;
        }
    }

    private void inverterSinal() {
        if (entrada.equals("0")) {
            return;
        }
        entrada = entrada.startsWith("-") ? entrada.substring(1) : "-" + entrada;
    }

    private void escolherOperacao(Operacao operacao) {
        // Encadeia contas: 2 + 3 × ... resolve 2 + 3 antes de aplicar ×.
        if (pendente != null && !novaEntrada) {
            if (!resolverPendente()) {
                return;
            }
        }
        acumulado = valorEntrada();
        pendente = operacao;
        novaEntrada = true;
    }

    private void aplicarRaiz() {
        try {
            double a = valorEntrada();
            double resultado = calculadora.calcular(Operacao.RAIZ_QUADRADA, a, 0);
            entrada = textoDe(resultado);
            novaEntrada = true;
        } catch (ArithmeticException e) {
            mostrarErro(e.getMessage());
        }
    }

    private void igual() {
        if (pendente != null) {
            resolverPendente();
            pendente = null;
            acumulado = null;
            novaEntrada = true;
        }
    }

    /** Calcula acumulado (operação) entrada. Devolve false se der erro. */
    private boolean resolverPendente() {
        try {
            double resultado = calculadora.calcular(pendente, acumulado, valorEntrada());
            entrada = textoDe(resultado);
            acumulado = null;
            pendente = null;
            novaEntrada = true;
            return true;
        } catch (ArithmeticException e) {
            mostrarErro(e.getMessage());
            return false;
        }
    }

    private void limparTudo() {
        entrada = "0";
        acumulado = null;
        pendente = null;
        novaEntrada = true;
        mostrandoErro = false;
    }

    private void mostrarErro(String mensagem) {
        limparTudo();
        mostrandoErro = true;
        visor.setText("Erro");
        expressao.setText(mensagem);
    }

    // ------------------------------------------------------------------ tela

    private double valorEntrada() {
        return Double.parseDouble(entrada);
    }

    /** Número como texto interno (com ponto), sem ".0" quando inteiro. */
    private static String textoDe(double valor) {
        return Calculadora.formatar(valor);
    }

    private void atualizarTela() {
        if (mostrandoErro) {
            // visor e expressão já foram preenchidos por mostrarErro
        } else {
            visor.setText(entrada.replace('.', ','));
            expressao.setText(pendente != null && acumulado != null
                    ? Calculadora.formatar(acumulado).replace('.', ',') + " " + pendente.getSimbolo()
                    : " ");
        }

        if (calculadora.getHistorico().isEmpty()) {
            areaHistorico.setText("Histórico vazio");
        } else {
            StringBuilder texto = new StringBuilder();
            // Mais recente primeiro.
            for (int i = calculadora.getHistorico().size() - 1; i >= 0; i--) {
                texto.append(calculadora.getHistorico().get(i).replace('.', ',')).append('\n');
            }
            areaHistorico.setText(texto.toString());
            areaHistorico.setCaretPosition(0);
        }
    }

    // ------------------------------------------------------------------ acesso (testes)

    public String getTextoVisor() {
        return visor.getText();
    }

    public String getTextoExpressao() {
        return expressao.getText();
    }

    public Calculadora getCalculadora() {
        return calculadora;
    }
}
