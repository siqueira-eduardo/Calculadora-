import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class CalculadoraTest {

    private final Calculadora calc = new Calculadora();

    @Test
    void operacoesBasicas() {
        assertEquals(5, calc.calcular(Operacao.ADICAO, 2, 3));
        assertEquals(-1, calc.calcular(Operacao.SUBTRACAO, 2, 3));
        assertEquals(6, calc.calcular(Operacao.MULTIPLICACAO, 2, 3));
        assertEquals(2, calc.calcular(Operacao.DIVISAO, 6, 3));
    }

    @Test
    void operacoesNovas() {
        assertEquals(1024, calc.calcular(Operacao.POTENCIA, 2, 10));
        assertEquals(9, calc.calcular(Operacao.RAIZ_QUADRADA, 81, 0));
        assertEquals(1, calc.calcular(Operacao.RESTO, 10, 3));
        assertEquals(30, calc.calcular(Operacao.PORCENTAGEM, 15, 200));
    }

    @Test
    void divisaoPorZeroLancaErro() {
        assertThrows(ArithmeticException.class, () -> calc.calcular(Operacao.DIVISAO, 1, 0));
        assertThrows(ArithmeticException.class, () -> calc.calcular(Operacao.RESTO, 1, 0));
    }

    @Test
    void resultadosInvalidosLancamErro() {
        assertThrows(ArithmeticException.class, () -> calc.calcular(Operacao.RAIZ_QUADRADA, -4, 0));
        assertThrows(ArithmeticException.class, () -> calc.calcular(Operacao.POTENCIA, 0, -1));
        assertThrows(ArithmeticException.class, () -> calc.calcular(Operacao.POTENCIA, -8, 0.5));
    }

    @Test
    void erroNaoEntraNoHistorico() {
        assertThrows(ArithmeticException.class, () -> calc.calcular(Operacao.DIVISAO, 1, 0));
        assertTrue(calc.getHistorico().isEmpty());
    }

    @Test
    void historicoGuardaAsContasEmOrdem() {
        calc.calcular(Operacao.ADICAO, 2, 3);
        calc.calcular(Operacao.RAIZ_QUADRADA, 81, 0);
        calc.calcular(Operacao.PORCENTAGEM, 15, 200);

        assertEquals(3, calc.getHistorico().size());
        assertEquals("2 + 3 = 5", calc.getHistorico().get(0));
        assertEquals("√81 = 9", calc.getHistorico().get(1));
        assertEquals("15% de 200 = 30", calc.getHistorico().get(2));

        calc.limparHistorico();
        assertTrue(calc.getHistorico().isEmpty());
    }

    @Test
    void historicoNaoPodeSerAlteradoPorFora() {
        calc.calcular(Operacao.ADICAO, 1, 1);
        assertThrows(UnsupportedOperationException.class, () -> calc.getHistorico().clear());
    }

    @Test
    void formatarRemoveZeroDecimal() {
        assertEquals("15", Calculadora.formatar(15.0));
        assertEquals("2.5", Calculadora.formatar(2.5));
        assertEquals("-3", Calculadora.formatar(-3.0));
        assertEquals("0", Calculadora.formatar(-0.0));
    }

    @Test
    void operacaoPorCodigo() {
        assertEquals(Operacao.DIVISAO, Operacao.deCodigo(4));
        assertEquals(Operacao.PORCENTAGEM, Operacao.deCodigo(8));
        assertNull(Operacao.deCodigo(0));
        assertNull(Operacao.deCodigo(9));
        assertEquals(1, Operacao.RAIZ_QUADRADA.getOperandos());
    }
}
