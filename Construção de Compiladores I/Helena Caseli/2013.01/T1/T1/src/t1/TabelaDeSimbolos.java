package t1;

import java.util.ArrayList;
import java.util.List;

public class TabelaDeSimbolos {
    public static void limparTabela() {
        getInstancia().tabela.clear();
    }
    
    public static void adicionarSimbolo(String lexema, Tipo tipo) {
        EntradaTabelaDeSimbolos etds = new EntradaTabelaDeSimbolos(lexema, tipo);
        getInstancia().adicionarSimbolo(etds);
    }

    public static void imprimirTabela(SaidaParser out) {
        for (EntradaTabelaDeSimbolos etds : getInstancia().tabela) {
            out.println(etds.toString());
        }
    }
    private static TabelaDeSimbolos instancia;

    private TabelaDeSimbolos() {
        tabela = new ArrayList<EntradaTabelaDeSimbolos>();
    }

    private static TabelaDeSimbolos getInstancia() {
        if (instancia == null) {
            instancia = new TabelaDeSimbolos();
        }
        return instancia;
    }
    private List<EntradaTabelaDeSimbolos> tabela;

    private void adicionarSimbolo(EntradaTabelaDeSimbolos etds) {
        if (!tabela.contains(etds)) {
            tabela.add(etds);
        }
    }
}
