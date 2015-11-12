package t1;

import java.io.IOException;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;

public class TestaAnalisadorSintatico {

    public static void main(String args[]) throws IOException, RecognitionException {
        SaidaParser out = new SaidaParser();
// Descomente as linhas abaixo para testar o analisador gerado.
// Obs: a linha abaixo está configurada para usar como entrada o arquivo lua1.txt
// Modifique-a para testar os demais exemplos
//        ANTLRInputStream input = new ANTLRInputStream(TestaAnalisadorSintatico.class.getResourceAsStream("exemplos/lua5.txt"));
//        LuaLexer lexer = new LuaLexer(input);
//        CommonTokenStream tokens = new CommonTokenStream(lexer);
//        LuaParser parser = new LuaParser(tokens);
//        parser.addErrorListener(new T1ErrorListener(out));
//        parser.programa();
//        if (!out.isModificado()) {
//            out.println("Fim da analise. Sem erros sintaticos.");
//            out.println("Tabela de simbolos:");
//
//            TabelaDeSimbolos.imprimirTabela(out);
//            System.out.print(out);
//        } else {
//            out.println("Fim da analise. Com erros sintaticos.");
//        }
    }
}