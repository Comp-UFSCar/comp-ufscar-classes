package t1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;

public class Corrigir {

    // Especifique o caminho dos casos de teste.
    // Deve haver dois subdiretorios: entrada e saida
    private final static String CAMINHO_CASOS_TESTE = "/mnt/Windows/Users/Thales/Dropbox/Faculdade/5o Semestre/Compiladores I/T1/casosDeTesteSintatico";
    
    // As flags GERA e VERIFICA são de uso do professor
    // GERA = true significa que a saída vai ser gerada, sobrescrevendo qualquer
    // conteudo do subdiretorio saida
    // VERIFICA = true gera a saída junto com o conteúdo da entrada, para
    // verificação
    // Alunos: deixem ambas como "false"
    private final static boolean GERA = false;
    private final static boolean VERIFICA = false;
//
//    
//    // Descomente o método abaixo para testar
//    // Obs: este é o mesmo método que será usado pelo professor na correção
//    // A nota que você obtiver aqui será usada no cálculo de sua nota do trabalho
    public static void main(String[] args) throws IOException, RecognitionException {
        File diretorioCasosTeste = new File(CAMINHO_CASOS_TESTE + "/entrada");
        File[] casosTeste = diretorioCasosTeste.listFiles();
        int totalCasosTeste = casosTeste.length;
        int casosTesteErrados = 0;
        for (File casoTeste : casosTeste) {

            SaidaParser out = new SaidaParser();
            TabelaDeSimbolos.limparTabela();
            ANTLRInputStream input = new ANTLRInputStream(new FileInputStream(casoTeste));
            LuaLexer lexer = new LuaLexer(input);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            LuaParser parser = new LuaParser(tokens);
            parser.addErrorListener(new T1ErrorListener(out));

            parser.programa();

            if (!out.isModificado()) {
                out.println("Fim da analise. Sem erros sintaticos.");
                out.println("Tabela de simbolos:");

                TabelaDeSimbolos.imprimirTabela(out);
                System.out.print(out);
            } else {
                out.println("Fim da analise. Com erros sintaticos.");
            }

            if (GERA) {

                File saidaCasoTeste = new File(CAMINHO_CASOS_TESTE + "/saida/" + casoTeste.getName());
                saidaCasoTeste.createNewFile();
                PrintWriter pw = new PrintWriter(new FileWriter(saidaCasoTeste));

                if (VERIFICA) {
                    BufferedReader br = new BufferedReader(new FileReader(casoTeste));
                    String linha = null;
                    while ((linha = br.readLine()) != null) {
                        pw.println(linha);
                    }

                    pw.println("===========================");
                }


                pw.print(out);
                pw.flush();
                pw.close();
            } else {
                File saidaCasoTeste = new File(CAMINHO_CASOS_TESTE + "/saida/" + casoTeste.getName());
                FileReader fr = new FileReader(saidaCasoTeste);
                StringReader sr = new StringReader(out.toString());
                int charFr = -1;
                int charSr = -1;
                boolean passou = true;
                while ((charFr = fr.read()) != -1 && (charSr = sr.read()) != -1) {
                    if (charFr != charSr) {
                        casosTesteErrados++;
                        passou = false;
                        break;
                    }
                }
                System.out.println((passou ? "passou" : "falhou") + " - " + casoTeste.getName());
                if (!passou) {
                    System.out.println(out.toString());
                }
            }
        }
        double nota = ((double) (totalCasosTeste - casosTesteErrados) / totalCasosTeste) * 10.0d;
        System.out.println("Nota = " + nota);
    }
}