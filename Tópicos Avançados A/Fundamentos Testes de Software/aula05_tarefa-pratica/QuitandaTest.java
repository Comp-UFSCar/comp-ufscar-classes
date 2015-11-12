import org.junit.Test;

import java.util.StringTokenizer;

import static org.junit.Assert.*;

/**
 * Created by Thales on 4/11/2015.
 */
public class QuitandaTest {

    @Test
    public void testaNomeValido() throws Exception {
        // name length = 2
        assertTrue(Quitanda.validaItemNome("ab"));          // no whitespaces - lowercase
        assertTrue(Quitanda.validaItemNome("AB"));          // no whitespaces - uppercase
        assertTrue(Quitanda.validaItemNome("   aB   "));    // with whitespaces both sides
        assertTrue(Quitanda.validaItemNome("Ba     "));     // with whitespaces before
        assertTrue(Quitanda.validaItemNome("    Ba"));      // with whitespaces after

        // name length = 15
        assertTrue(Quitanda.validaItemNome("abcabcabcabcabc"));         // no whitespaces - lowercase
        assertTrue(Quitanda.validaItemNome("ABCABCABCABCABC"));         // no whitespaces - uppercase
        assertTrue(Quitanda.validaItemNome("   aBcAbCaBcAbCaBc    "));  // with whitespaces both sides
        assertTrue(Quitanda.validaItemNome("aBcAbCaBcAbCaBc    "));     // with whitespaces after
        assertTrue(Quitanda.validaItemNome("    aBcAbCaBcAbCaBc"));     // with whitespaces before
    }

    @Test
    public void testaNomeInvalido() throws Exception {
        // name length = 1
        assertFalse(Quitanda.validaItemNome("a"));          // no spaces -lowercase
        assertFalse(Quitanda.validaItemNome("A"));          // no spaces - uppercase
        assertFalse(Quitanda.validaItemNome("   x   "));    // whitespaces both sides
        assertFalse(Quitanda.validaItemNome("   x"));       // whitespaces before
        assertFalse(Quitanda.validaItemNome("x   "));       // whitespaces after

        // name length = 16
        assertFalse(Quitanda.validaItemNome("abcabcabcabcabca"));          // no spaces -lowercase
        assertFalse(Quitanda.validaItemNome("ABCABCABCABCABCA"));          // no spaces - uppercase
        assertFalse(Quitanda.validaItemNome("   X   "));    // whitespaces both sides
        assertFalse(Quitanda.validaItemNome("   X"));       // whitespaces before
        assertFalse(Quitanda.validaItemNome("X   "));       // whitespaces after

        // 2 <= (name.length) <= 15
        assertFalse(Quitanda.validaItemNome("abc1abc"));       // a digit character
        assertFalse(Quitanda.validaItemNome("abc%abc"));       // an invalid character
    }

    @Test
    public void testaQuantidadesValida() throws Exception {

        // tests all valid numbers in range for each parameter
        // in ascending order (changing only the last parameter)
        int[] s1 = {30};
        int[] s2 = {30, 30};
        int[] s3 = {30, 30, 30};
        int[] s4 = {30, 30, 30, 30};
        int[] s5 = {30, 30, 30, 30, 30};

        // one parameter
        while(s1[0] <= 2000)
        {
            assertTrue(Quitanda.validaItemQuantidades(s1));
            s1[0]++;
        }

        // two parameters
        while(s2[1] <= 2000)
        {
            assertTrue(Quitanda.validaItemQuantidades(s2));
            s2[1]++;
        }

        // three parameters
        while(s3[2] <= 2000)
        {
            assertTrue(Quitanda.validaItemQuantidades(s3));
            s3[2]++;
        }

        // four parameters
        while(s4[3] <= 2000)
        {
            assertTrue(Quitanda.validaItemQuantidades(s4));
            s4[3]++;
        }

        // five parameters
        while (s5[4] <= 2000) {
            assertTrue(Quitanda.validaItemQuantidades(s5));
            s5[4]++;
        }
    }

    @Test
    public void testaQuantidadesInvalida() throws Exception {

        // test two invalid numbers outside valid range
        int[] s0 = {};
        int[] s1 = {29};
        int[] s2 = {29, 29};
        int[] s3 = {29, 29, 29};
        int[] s4 = {29, 29, 29, 29};
        int[] s5 = {29, 29, 29, 29, 29};
        int[] s6 = {30, 30, 30, 30, 30, 30};

        // no parameter
        assertFalse(Quitanda.validaItemQuantidades(s0));

        // one parameter
        assertFalse(Quitanda.validaItemQuantidades(s1));
        s1[0] = 2001;
        assertFalse(Quitanda.validaItemQuantidades(s1));

        // two parameters
        assertFalse(Quitanda.validaItemQuantidades(s2));
        s2[1] = s2[0] = 2001;
        assertFalse(Quitanda.validaItemQuantidades(s2));

        // three parameters
        assertFalse(Quitanda.validaItemQuantidades(s3));
        s3[2] = s3[1] = s3[0] = 2001;
        assertFalse(Quitanda.validaItemQuantidades(s3));

        // four parameters
        assertFalse(Quitanda.validaItemQuantidades(s4));
        s4[3] = s4[2] = s4[1] = s4[0] = 2001;
        assertFalse(Quitanda.validaItemQuantidades(s4));

        // five parameters
        assertFalse(Quitanda.validaItemQuantidades(s5));
        s5[4] = s5[3] = s5[2] = s5[1] = s5[0] = 2001;
        assertFalse(Quitanda.validaItemQuantidades(s5));

        // six parameters
        assertFalse(Quitanda.validaItemQuantidades(s6));

        // not ascending order
        s5[4] = s5[3] = s5[2] = s5[1] = s5[0] = 50;

        // first parameter bigger than the rest
        s5[0] += 10;
        assertFalse(Quitanda.validaItemQuantidades(s5));
        s5[0] = 50;

        // second parameter bigger than the rest
        s5[1] += 10;
        assertFalse(Quitanda.validaItemQuantidades(s5));
        s5[1] = 50;

        // third parameter bigger than the rest
        s5[2] += 10;
        assertFalse(Quitanda.validaItemQuantidades(s5));
        s5[2] = 50;

        // fourth parameter bigger than the rest
        s5[3] += 10;
        assertFalse(Quitanda.validaItemQuantidades(s5));
        s5[3] = 50;

    }

    @Test
    public void testaItemValido() throws Exception {

        assertTrue(Quitanda.validaItem("ab, 30"));
        assertTrue(Quitanda.validaItem("ab, 30, 40"));
        assertTrue(Quitanda.validaItem("ab, 30, 40, 50"));
        assertTrue(Quitanda.validaItem("ab, 30, 40, 50, 60"));
        assertTrue(Quitanda.validaItem("ab, 30, 40, 50, 60, 70"));

        assertTrue(Quitanda.validaItem("abcabcabcabcabc, 30"));
        assertTrue(Quitanda.validaItem("abcabcabcabcabc, 30, 40"));
        assertTrue(Quitanda.validaItem("abcabcabcabcabc, 30, 40, 50"));
        assertTrue(Quitanda.validaItem("abcabcabcabcabc, 30, 40, 50, 60"));
        assertTrue(Quitanda.validaItem("abcabcabcabcabc, 30, 40, 50, 60, 70"));

        assertTrue(Quitanda.validaItem("AB, 30"));
        assertTrue(Quitanda.validaItem("AB, 30, 40"));
        assertTrue(Quitanda.validaItem("AB, 30, 40, 50"));
        assertTrue(Quitanda.validaItem("AB, 30, 40, 50, 60"));
        assertTrue(Quitanda.validaItem("AB, 30, 40, 50, 60, 70"));

        assertTrue(Quitanda.validaItem("ABCABCABCABCABC, 30"));
        assertTrue(Quitanda.validaItem("ABCABCABCABCABC, 30, 40"));
        assertTrue(Quitanda.validaItem("ABCABCABCABCABC, 30, 40, 50"));
        assertTrue(Quitanda.validaItem("ABCABCABCABCABC, 30, 40, 50, 60"));
        assertTrue(Quitanda.validaItem("ABCABCABCABCABC, 30, 40, 50, 60, 70"));

    }

    @Test
    public void testaItemInvalido() throws Exception {

        assertFalse(Quitanda.validaItem(" "));  // no name, no quantities
        assertFalse(Quitanda.validaItem("a"));  // invalid name, no quantities
        assertFalse(Quitanda.validaItem("#"));  // invalid name, no quantities
        assertFalse(Quitanda.validaItem("ab1"));    // invalid name, no quantities
        assertFalse(Quitanda.validaItem("ab, "));   // valid name, no quantities
        assertFalse(Quitanda.validaItem("ab, 29")); // valid name, invalid quantity
        assertFalse(Quitanda.validaItem("ab1, 50"));    // invalid name, valid quantity
        assertFalse(Quitanda.validaItem("ab, 30, 40, 50, 60, 70, 80")); // valid name, invalid quantities
        assertFalse(Quitanda.validaItem("abcabcabcabcabca, 50"));   // invalid name, valid quantity
        assertFalse(Quitanda.validaItem("abcabcabcabcabca, 30, 40, 50, 60, 70, 80"));   // invalid name and quantities
        assertFalse(Quitanda.validaItem("abc, 100, 50"));   // valid name, invalid quantity
        assertFalse(Quitanda.validaItem("ab1, 100, 50"));   // invalid name and quantity
        assertFalse(Quitanda.validaItem("ab1, abc"));   // invalid name and quantity
        assertFalse(Quitanda.validaItem("ab1, 50, abc"));   // invalid name and quantity

    }
}