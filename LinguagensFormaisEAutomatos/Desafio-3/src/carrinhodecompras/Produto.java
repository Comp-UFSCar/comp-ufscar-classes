package carrinhodecompras;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 *
 * @author daniellucredio
 */
public class Produto {
    // Um produto tem nome, peso e valor

    String nome;
    int peso, valor;
    // Variável que armazena todos os produtos disponíveis
    private static List<Produto> listaProdutos;

    // Método que recupera a lista de todos os produtos
    // disponíveis. Caso não exista ainda, a lista é
    // criada e inicializada com o conteúdo do arquivo
    // produtos.txt
    public static List<Produto> getListaProdutos() {
        if (listaProdutos == null) {
            listaProdutos = new ArrayList<Produto>();
            BufferedReader br = new BufferedReader(new InputStreamReader(Produto.class.getResourceAsStream("produtos.txt")));
            String line;
            try {
                while ((line = br.readLine()) != null) {
                    StringTokenizer st = new StringTokenizer(line, ",");
                    Produto p = new Produto();
                    p.nome = st.nextToken();
                    p.peso = Integer.parseInt(st.nextToken());
                    p.valor = Integer.parseInt(st.nextToken());
                    listaProdutos.add(p);
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
        return listaProdutos;
    }

    public static Produto getProdutoPeloNome(String nome) {
        getListaProdutos();
        for (Produto p : listaProdutos) {
            if (p.nome.equals(nome)) {
                return p;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return nome;
    }
}
