/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package carrinhodecompras;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Classe que representa uma lista de produtos. Possui métodos que auxiliam na
 * sua manipulação.
 *
 * @author daniellucredio
 */
public class ListaProdutos {

    List<Produto> lista;

    public ListaProdutos() {
        lista = new ArrayList<Produto>();
    }

    public void adicionar(Produto p) {
        lista.add(p);
    }

    public int getValor() {
        int ret = 0;
        for (Produto p : lista) {
            ret += p.valor;
        }
        return ret;
    }

    public ListaProdutos getCopia() {
        return getCopia(lista.size());
    }

    /**
     * Retorna uma cópia dos n primeiros produtos da lista.
     *
     * @param numeroProdutos o número n de produtos a serem copiados
     * @return
     */
    public ListaProdutos getCopia(int numeroProdutos) {
        ListaProdutos ret = new ListaProdutos();
        for (int i = 0; i < numeroProdutos; i++) {
            ret.adicionar(lista.get(i));
        }
        return ret;
    }

    public void embaralhar() {
        Collections.shuffle(lista);
    }

    /**
     * Calcula o peso total da lista.
     *
     * @return
     */
    public int getPeso() {
        int ret = 0;
        for (Produto p : lista) {
            ret += p.peso;
        }
        return ret;
    }

    /**
     * Imprime a lista de produtos, como uma sequência de nomes separados por
     * espaço.
     *
     * @return
     */
    @Override
    public String toString() {
        String ret = "";
        Iterator<Produto> i = lista.iterator();
        while (i.hasNext()) {
            ret += i.next();
            if (i.hasNext()) {
                ret += " ";
            }
        }

        return ret;
    }
}
