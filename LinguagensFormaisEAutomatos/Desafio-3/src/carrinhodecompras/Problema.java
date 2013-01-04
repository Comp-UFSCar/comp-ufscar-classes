package carrinhodecompras;

/**
 *
 * @author daniellucredio
 */
public class Problema {

    private ListaProdutos listaProdutos;

    public Problema() {
        listaProdutos = new ListaProdutos();
    }
    
    public void adicionarProduto(Produto p) {
        listaProdutos.adicionar(p);
    }
    
    public ListaProdutos getListaProdutos() {
        return listaProdutos;
    }

    public Produto get(int i) {
        return listaProdutos.lista.get(i);
    }

    public int getTamanho() {
        return listaProdutos.lista.size();
    }
    
    @Override
    public String toString() {
        return listaProdutos.toString();
    }

}
