package carrinhodecompras;

/*
 * Solucao para o problema do carrinho.
 *
 * AUTOR:
 * Lucas Oliveira David e Pedro Henrique Rodrigues
 *
 * DESCRICAO 
 *
 * A tecnica utilizada foi a programacao dinamica, que consiste em compultar a solucao para problemas parciais,
 * que possuem intersecao diferente de zero. Posteriormente, solucoes maiores baseadas nas anteriores sao calculadas
 * e assim sucessivamente ate que se chegue ao tamanho do problema original.
 * 
 * Referencia: LEVITIN, Anany. Introduction to The Design & Analysis of Algorithms. 2ND Edition.
 *
 * ULTIMA MODIFICACAO: 27/12/2012
 */

public class SolucaoProgDinamica {

    public ListaProdutos resolver(Problema problema, int pesoMaximoPermitido) {
        
        ListaProdutos ret = new ListaProdutos();    // Armazena a resposta
        int numProdutos = problema.getTamanho();
        
        // Tabela utilizada pela prog. dinamica para descobrir a solucao otima
        int tblSolucaoOtima[][] = new int[numProdutos +1][pesoMaximoPermitido +1];
        
        // As posicoes i ou j iguais a zero definem que voce esta levando zero produtos 
        // ou esta com um carrinho de capacidade zero, logo ela nao ee preenchida.
        for(int j = 1; j <= pesoMaximoPermitido; j++)
            for(int i = 1; i <= numProdutos; i++) {
                
                Produto produtoAtual = problema.get(i -1);
                // o peso do produto ee maior do que o carrinho, descartamos esse produto
                if(j < produtoAtual.peso) 
                    tblSolucaoOtima[i][j] = tblSolucaoOtima[i -1][j];
                else
                    // o produto cabe no carrinho, verificamos se ee melhor adiciona-lo
                    // ou simplesmente manter a resposta anterior
                    
                    // manter os valores anteriores ee melhor do que atualizar os produtos.
                    // Considerei que, caso existam duas solucoes otimas, o algoritmo deve
                    // retornar a primeira. Isso e garantido pela utilizacao do >=
                    if(tblSolucaoOtima[i -1][j] >= produtoAtual.valor +tblSolucaoOtima[i -1][j -produtoAtual.peso])
                        tblSolucaoOtima[i][j] = tblSolucaoOtima[i -1][j];
                    else
                        // ok! Achamos uma solucao melhor que a anterior. Ela e inserida na tabela de solucoes
                        tblSolucaoOtima[i][j] = produtoAtual.valor +tblSolucaoOtima[i -1][j -produtoAtual.peso];
            }
        
        /*
         * Neste ponto, sabemos que a ultima posicao da tabela contem o valor otimo. Falta identificar quais
         * produtos compoem a lista otima de itens: 
         * Isso ee bem simples: sempre que um valor na posicao [i][j] da tabela muda, em relacao a posicao [i -1][j],
         * sabemos que o item i foi incluido. Logo o inserimos na resposta ret e verificamos a coluna que contem
         * as melhores solucoes de pesos (coluna atual -peso do produto atual)
         */
        int j = pesoMaximoPermitido;
        for(int i = numProdutos; i > 0; i--)
            if(tblSolucaoOtima[i][j] != tblSolucaoOtima[i -1][j]) {
                Produto produtoOtimo = problema.get(i -1);
                ret.adicionar(produtoOtimo);
                j -= produtoOtimo.peso;
            }
        
        return ret;
    }
}
