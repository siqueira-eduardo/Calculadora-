import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class CalculadoraPanelTest {

    @BeforeAll
    static void semJanela() {
        System.setProperty("java.awt.headless", "true");
    }

    private static CalculadoraPanel painel(String... teclas) {
        CalculadoraPanel p = new CalculadoraPanel();
        for (String t : teclas) {
            p.pressionar(t);
        }
        return p;
    }

    @Test
    void somaSimples() {
        assertEquals("5", painel("2", "+", "3", "=").getTextoVisor());
    }

    @Test
    void encadeiaContas() {
        // 2 + 3 × 4 resolve da esquerda para a direita: (2 + 3) × 4 = 20
        assertEquals("20", painel("2", "+", "3", "×", "4", "=").getTextoVisor());
    }

    @Test
    void numerosDecimaisUsamVirgula() {
        assertEquals("3,75", painel("7", ",", "5", "÷", "2", "=").getTextoVisor());
        assertEquals("0,5", painel(",", "5").getTextoVisor());
    }

    @Test
    void continuaDoResultado() {
        assertEquals("15", painel("5", "+", "5", "=", "+", "5", "=").getTextoVisor());
    }

    @Test
    void divisaoPorZeroMostraErroEPermiteContinuar() {
        CalculadoraPanel p = painel("8", "÷", "0", "=");
        assertEquals("Erro", p.getTextoVisor());
        assertTrue(p.getTextoExpressao().contains("zero"));

        p.pressionar("4");
        assertEquals("4", p.getTextoVisor());
    }

    @Test
    void raizQuadradaEImediata() {
        assertEquals("9", painel("8", "1", "√").getTextoVisor());
        assertEquals("Erro", painel("4", "±", "√").getTextoVisor());
    }

    @Test
    void potenciaRestoEPorcentagem() {
        assertEquals("1024", painel("2", "xʸ", "1", "0", "=").getTextoVisor());
        assertEquals("1", painel("1", "0", "mod", "3", "=").getTextoVisor());
        assertEquals("30", painel("1", "5", "% de", "2", "0", "0", "=").getTextoVisor());
    }

    @Test
    void apagarEInverterSinal() {
        assertEquals("12", painel("1", "2", "3", "←").getTextoVisor());
        assertEquals("0", painel("5", "←").getTextoVisor());
        assertEquals("-7", painel("7", "±").getTextoVisor());
        assertEquals("7", painel("7", "±", "±").getTextoVisor());
    }

    @Test
    void naoPermiteDoisZerosNemDuasVirgulas() {
        assertEquals("0", painel("0", "0").getTextoVisor());
        assertEquals("1,5", painel("1", ",", "5", ",").getTextoVisor());
    }

    @Test
    void historicoRegistraEPodeSerLimpo() {
        CalculadoraPanel p = painel("2", "+", "3", "=");
        assertEquals(1, p.getCalculadora().getHistorico().size());
        p.pressionar("Limpar histórico");
        assertTrue(p.getCalculadora().getHistorico().isEmpty());
    }

    @Test
    void limparZeraTudo() {
        CalculadoraPanel p = painel("9", "+", "9", "C");
        assertEquals("0", p.getTextoVisor());
        p.pressionar("3");
        p.pressionar("=");
        assertEquals("3", p.getTextoVisor());
    }
}
