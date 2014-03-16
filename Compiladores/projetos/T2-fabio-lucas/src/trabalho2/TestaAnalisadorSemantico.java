/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package trabalho2;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

public class TestaAnalisadorSemantico {

    private final static int TOTAL_CASOS_TESTE = 22;
    // Especifique abaixo qual caso de teste deseja
    // testar. Utilize 0 para testar todos
    private final static int CASO_A_SER_TESTADO = 0;

    public static void main(String[] args) throws Exception {
        int min = 1, max = TOTAL_CASOS_TESTE;
        if (CASO_A_SER_TESTADO >= min && CASO_A_SER_TESTADO <= max) {
            min = CASO_A_SER_TESTADO;
            max = CASO_A_SER_TESTADO;
        }
        for (int i = min; i <= max; i++) {
            Saida.clear();
            String nomeArquivo = String.format("casoDeTeste%02d.txt", i);
            InputStream casoDeTesteEntrada = TestaAnalisadorSemantico.class.getResourceAsStream("casosDeTeste/entrada/" + nomeArquivo);
            ANTLRInputStream input = new ANTLRInputStream(casoDeTesteEntrada);
            LuazinhaLexer lexer = new LuazinhaLexer(input);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            LuazinhaParser parser = new LuazinhaParser(tokens);
            parser.programa();
            InputStream casoDeTesteSaida = TestaAnalisadorSemantico.class.getResourceAsStream("casosDeTeste/saida/" + nomeArquivo);
            comparar(nomeArquivo, casoDeTesteSaida, Saida.getTexto());
        }
    }

    private static void comparar(String nomeArquivo, InputStream saidaCorreta, String saidaObtida) throws Exception {
        InputStreamReader isr = new InputStreamReader(saidaCorreta);
        StringReader sr = new StringReader(saidaObtida);
        boolean diferente = false;
        int charFr = -1;
        int charSr = -1;
        while ((charFr = isr.read()) != -1 & (charSr = sr.read()) != -1) {
            if (charFr != charSr) {
                diferente = true;
                System.out.println("ERRO:" + nomeArquivo);
                System.out.println("=================== Saída obtida ==================");
                System.out.println(Saida.getTexto());
                System.out.println("===================================================");
                break;
            }
        }
        if (!diferente && (charFr != -1 || charSr != -1)) {
            diferente = true;
            System.out.println("ERRO:" + nomeArquivo);
            System.out.println("=================== Saída obtida ==================");
            System.out.println(Saida.getTexto());
            System.out.println("===================================================");
        }

        if (!diferente) {
            System.out.println("OK:" + nomeArquivo);
        }

    }
}
