package test;

import code.CadeiaCaracteres;
import org.junit.Before;
import org.junit.Test;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Scanner;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.*;

/**
 * Created by Thales on 4/1/2015.
 */
public class CadeiaCaracteresTest {

    @Before
    public void cadeia(){
        String input = "5\n" +
                "abcde\n";
        CadeiaCaracteres.defineCadeia(new Scanner(input));
    }

    @Test
    public void testDefineCadeia() throws Exception {
        String input =  "34\n" +    // cadeia > 20  - incorreto
                        "0\n" +     // cadeia < 1   - incorreto
                        "5\n" +     // cadeia = 5   - correto
                        "abc\n" +   // cadeia menor que tamanho definido
                        "abcdefg\n" + // cadeia maior que tamanho definido
                        "abcde\n";    // cadeia com tamanho definido

        CadeiaCaracteres.defineCadeia(new Scanner(input));

        assertEquals(5, CadeiaCaracteres.cadeia.length(), 0);   // tamanho = 5
        assertEquals("abcde", CadeiaCaracteres.cadeia);         // cadeia = "abcde"
    }

    @Test
    public void testBuscaCaractereExistente() throws Exception {
        StringWriter output = new StringWriter();
        String input = "c\n" +  // caractere c existe na cadeia "abcde"
                "nao\n";        // encerra

        CadeiaCaracteres.buscaCaractere(new Scanner(input), new PrintWriter(output));
        assertThat(output.toString(), containsString("Caracter aparece na posicao 2 da cadeia"));
    }

    @Test
    public void testBuscaCaractereInexistente() throws Exception {
        StringWriter output = new StringWriter();
        String input = "z\n" +  // caractere z nao existe na cadeia "abcde"
                "nao\n";        // encerra

        CadeiaCaracteres.buscaCaractere(new Scanner(input), new PrintWriter(output));
        assertThat(output.toString(), containsString("Caracter nao pertence a cadeia"));
    }

    @Test
    public void testBuscaCaracteres() throws Exception {
        StringWriter output = new StringWriter();
        String input = "z\n" +  // caractere z nao existe na cadeia "abcde"
                "sim\n" +       // continua
                "a\n" +         // caractere a na posicao 0 da cadeia
                "sim\n" +       // continua
                "c\n" +         // caractere c na posicao 2 da cadeia
                "nao\n";        // encerra

        CadeiaCaracteres.buscaCaractere(new Scanner(input), new PrintWriter(output));

        assertThat(output.toString(), containsString("Caracter nao pertence a cadeia"));
        assertThat(output.toString(), containsString("Caracter aparece na posicao 0 da cadeia"));
        assertThat(output.toString(), containsString("Caracter aparece na posicao 2 da cadeia"));
    }

    @Test
    public void testBuscaCaracteresInvalidos() throws Exception {
        StringWriter output = new StringWriter();
        String input = "abc\n" +    // mais de um caractere
                "\n" +              // nenhum caractere
                "z\n" +             // caractere a na posicao 0 da cadeia
                // loop para saber se continua a busca
                "s\n" +             // s - invalido
                "n\n" +             // n - invalido
                "\n" +              // vazio - invalido
                "nao";              // encerra

        CadeiaCaracteres.buscaCaractere(new Scanner(input), new PrintWriter(output));

        assertThat(output.toString(), containsString("Caracter nao pertence a cadeia"));
    }
}