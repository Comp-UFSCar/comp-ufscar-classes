/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package carrinhodecompras;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Solução para o problema do carrinho de compras.
 *
 * @author daniellucredio
 */
public class CarrinhoDeCompras {

    private final static int PESO_MAXIMO_CARRINHO = 60;
    private final static String ARQUIVO_ENTRADA = "instancias.txt";
    private final static String ARQUIVO_SAIDA = "saida.txt";
    private final static String PROBLEMAS_A_TESTAR = "0-159";

    /**
     * Método principal, chamado quando o programa é executado.
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        new CarrinhoDeCompras().executar();
    }

    /**
     * Método que prepara a resolução do problema. Primeiro recupera uma lista
     * com os problemas a testar (conforme especificado pela String
     * PROBLEMAS_A_TESTAR), e depois os submete, um a um, a uma solução. Marca o
     * tempo gasto, e depois escreve os resultados no arquivo de saída.
     *
     * @throws Exception
     */
    public void executar() throws Exception {
        PrintWriter pw = new PrintWriter(new FileWriter(ARQUIVO_SAIDA));

        List<Problema> problemas = lerTodosProblemas();
        List<Integer> problemasATestar = getProblemasATestar();
        for (int i : problemasATestar) {
            Problema p = problemas.get(i);
            SolucaoProgDinamica solucao = new SolucaoProgDinamica();
            Date antes = new Date();
            ListaProdutos resposta = solucao.resolver(p, PESO_MAXIMO_CARRINHO);
            Date depois = new Date();
            long tempo = depois.getTime() - antes.getTime();

            // escrever no arquivo de saída, no seguinte formato:
            // numeroProblema:tempo:listaProdutos
            pw.println(i + ":" + tempo + ":" + resposta.toString());
            System.out.println(i + ":" + tempo + ":" + resposta.toString());
            pw.flush();
        }
        pw.close();
    }

    /**
     * Lê do arquivo instancia.txt todos os problemas, e retorna uma lista
     * seguindo a mesma ordem
     */
    private List<Problema> lerTodosProblemas() throws Exception {
        List<Problema> ret = new ArrayList<Problema>();
        BufferedReader br = new BufferedReader(new InputStreamReader(CarrinhoDeCompras.class.getResourceAsStream(ARQUIVO_ENTRADA)));
        String linha = null;
        while ((linha = br.readLine()) != null) {
            // StringTokenizer separa uma String conforme um delimitador qualquer.
            // Neste caso, ":"
            StringTokenizer st = new StringTokenizer(linha, ":");
            String strNumProblema = st.nextToken();
            String strProblema = st.nextToken();

            // strProblema é uma lista de produtos separados por espaço, portanto
            // vamos utilizar outro StringTokenizer aqui
            StringTokenizer st2 = new StringTokenizer(strProblema, " ");
            Problema problema = new Problema();
            while (st2.hasMoreTokens()) {
                String nomeProduto = st2.nextToken();
                // Produto.getProdutoPeloNome() já faz a busca na lista de
                // produtos, conforme o arquivo produtos.txt
                problema.adicionarProduto(Produto.getProdutoPeloNome(nomeProduto));
            }
            ret.add(problema);
        }
        return ret;
    }

    /**
     * Retorna uma lista com os problemas a serem testados, conforme
     * especificado na string PROBLEMAS_A_TESTAR.
     */
    private static List<Integer> getProblemasATestar() {
        List<Integer> ret = new ArrayList<Integer>();
        StringTokenizer st = new StringTokenizer(PROBLEMAS_A_TESTAR, ",");
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            int indice = token.indexOf('-');
            if (indice == -1) {
                ret.add(Integer.parseInt(token));
            } else {
                String strInicio = token.substring(0, indice);
                String strFim = token.substring(indice + 1);
                int inicio = Integer.parseInt(strInicio);
                int fim = Integer.parseInt(strFim);
                for (int i = inicio; i <= fim; i++) {
                    ret.add(i);
                }

            }
        }
        return ret;
    }
}
