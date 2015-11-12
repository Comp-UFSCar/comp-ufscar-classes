package test;

import code.SillyPascal;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Thales on 4/1/2015.
 */
public class SillyPascalTest {

    @Test
    public void cadeiaVazia() throws Exception {
        // Cadeia vazia : retorna False
        assertFalse(SillyPascal.identificador(""));
    }

    @Test
    public void cadeiaMaior() throws Exception {
        // Cadeia > 6 : retorna False
        assertFalse(SillyPascal.identificador("abcdefg"));
    }

    @Test
    public void cadeiaComecaNumero() throws Exception {
        // Cadeia comeca com numero: retorna False
        assertFalse(SillyPascal.identificador("4abc"));
    }

    @Test
    public void cadeiaDigitoInvalido() throws Exception {
        // Cadeia com digito invalido: retorna False
        assertFalse(SillyPascal.identificador("abc*"));
    }

    @Test
    public void cadeiaCorreta() throws Exception {
        // Cadeia correta: retorna True
        assertTrue(SillyPascal.identificador("abc"));
        assertTrue(SillyPascal.identificador("a12c"));
        assertTrue(SillyPascal.identificador("a1bc23"));
    }
}