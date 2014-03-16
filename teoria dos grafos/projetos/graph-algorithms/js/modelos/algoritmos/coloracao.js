/*
 * COLORACAO.JS
 * 
 * DATA CRIACAO: 05/01/2013
 * 
 * DESCRICAO: declaracao da funcao de coloracao de grafos, algoritmo que
 * tem como objetivo colorir um dado grafo com o menor numero de cores possivel
 * 
 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 * LISTA DE MODIFICACOES
 * 
 * 05/01/2012: criacao do arquivo, +Coloracao()
 * 19/01/2013: +selectionSort(_C)
 * 01/02/2013: exibir numero de cores utilizadas
 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 */

(function() {
    
    var Coloracao = function() {
        if (Grafo.Direcionado) {
            lnConsole.Inserir('Desculpe, mas Coloracao ainda não foi implementado para grafos direcionados!')
            return 0
        }
        lnConsole.Inserir('Inicio do algoritmo de Coloracao.')
        console.log('Inicio do algoritmo de Coloracao.')
        
        for (i = 0; i < Grafo.MatrizAdj.length; i++)
            if (Grafo.MatrizAdj[i][i] != 0) {
                lnConsole.Inserir('Erro! Coloração de grafos não admite <i>loops</i>!')
                return false
            }

        var mat = [], C = [], cores = [], corAtual, i, j, k,
            qtdCoresUtil = 0

        for (i = 0; i < Grafo.MatrizAdj.length; i++) {
            mat[i] = []
            for (j = 0; j < Grafo.MatrizAdj.length; j++)
                mat[i][j] = Grafo.MatrizAdj[i][j] // copia matriz de adjacencia original do grafo
            
            // Preencher C[] com os respectivos graus de cada vertice.
            C[i] = { vertice: i, grau: 0, cores: [] }

            for (j = i +1; j < Grafo.MatrizAdj.length; j++)
                if (Grafo.MatrizAdj[i][j] != 0)
                    C[i].grau++
        }

        // ordena decrescentemente os vertices pela sua conectividade
        C = SelectionSort(C)

        for (i = 0; i < C.length; i++)
            for (j = 0; j < i +1; j++) // Preenche cores de acordo com cada vertice
                C[i].cores.push(j)

        // Como coloracao de grafos nao admite loops, vamos armazenar as cores dos vertices
        // na posicao i,i da propria matriz de adjacencia.
        for (i = 0; i < C.length; i++) {
            corAtual = mat[ C[i].vertice ][ C[i].vertice ] = C[i].cores[0] // vertice C[i].vertice recebe a primeira cor de sua lista            
            lnConsole.Inserir('Cor do v�rtice [' +(C[i].vertice +1) +']: ' +(corAtual +1))
            
            if (corAtual > qtdCoresUtil) // acha o numero minimo de cores
                qtdCoresUtil = corAtual  // a fim de colorir o grafo
            
            for (j = i +1; j < C.length; j++)                             // para todos os outros vertices que ainda nao
                if (Grafo.MatrizAdj[ C[i].vertice ][ C[j].vertice ] != 0) // foram analisados que sao vizinhos de C[i].vertice
                    C[j].cores.splice(C[j].cores.indexOf( corAtual ), 1)  // retira cor da lista de cores do vertice adjacente
        }
    
        lnConsole.Inserir('N�mero de cores utilizadas: ' +qtdCoresUtil)

        Grafo.Algoritmos.MatrizAdj = mat
        initApp('coloracao')
        return false
    }
    
    function SelectionSort(_C) {
        var menorElem, tmpTroca, i, j;

        for (i = 0; i < _C.length; i++) {
            menorElem = i

            for (j = i +1; j < _C.length; j++)
                if (_C[menorElem].grau < _C[j].grau) menorElem = j // ordena decrescentemente

            if ( menorElem == i )
                continue
            
            tmpTroca      = _C[i]
            _C[i] = _C[menorElem]
            _C[menorElem] = tmpTroca
        }

        return _C
    }

    Grafo.Algoritmos.Coloracao = Coloracao // retorna metodo publico
}())
