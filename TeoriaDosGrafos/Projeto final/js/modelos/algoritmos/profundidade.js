/*
 * PROFUNDIDADE.JS
 * 
 * DATA CRIACAO: 30/11/2012
 * 
 * DESCRICAO: declaracao do algoritmo de busca em profundidade.
 * 
 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 * LISTA DE MODIFICACOES
 * 
 * 29/11/2012: criacao do arquivo, +Profundidade(), +Visitar()
 * 26/12/2012: implementacao do algoritmo, debugging
 *
 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 */

(function() {
    
    var Profundidade = function() {
        if (Grafo.Direcionado) {
            lnConsole.Inserir('Desculpe, mas Profundidade ainda não foi implementado para grafos direcionados!')
            return false
        }
        lnConsole.Inserir('Busca em profundidade.')
        console.log('Inicio do algoritmo de busca em profundidade.')
        
        var tempo = 0, i, j,
            mat = [], vertices = []
        
        // declara a nova matriz de adjacencia e um vetor que contem
        // os vertices achados durante o algoritmo de busca em profundidade
        for (i = 0; i < Grafo.MatrizAdj.length; i++) {
            mat[i] = []
            for (j = 0; j < Grafo.MatrizAdj.length; j++)
                mat[i][j] = 0
            
            vertices.push({
                ini: 0,
                fim: 0,
                pred: 0,
                cor:  0 // branco
            })
        }
        
        console.log('Conjunto inicial de vertices:')
        console.log(vertices)
        
        // algoritmo de busca em profundidade
        tempo = Visitar(vertices, Grafo.VertInicial, tempo)
        
        // neste ponto, o algoritmo de busca em profundidade ja foi executado, mas seu resultado esta
        // contido no vetor vertices. Preenchemos, a partir de vertices a matriz mat
        for (i = 0; i < Grafo.MatrizAdj.length; i++) {
            mat[i][vertices[i].pred] = Grafo.MatrizAdj[i][vertices[i].pred]
            mat[vertices[i].pred][i] = Grafo.MatrizAdj[vertices[i].pred][i]
        }
        
        Grafo.Algoritmos.MatrizAdj = mat
        
        initApp(true)
        return false
    }
	
	function Visitar(pVertices, pIndice, pTempo) {	    
        pVertices[pIndice].ini = ++pTempo
	    pVertices[pIndice].cor = 1

        console.log('Entrada no vertice ' +pIndice +' no tempo ' +pTempo)
	    
	    for (var i = 0; i < Grafo.MatrizAdj.length; i++)
	        if (Grafo.MatrizAdj[pIndice][i] != 0 && pVertices[i].cor == 0) {
	            pVertices[i].pred = pIndice
	            pTempo = Visitar(pVertices, i, pTempo)
	        }

	    pVertices[pIndice].cor = 2
	    pVertices[pIndice].fim = ++pTempo	    
	    console.log('Saida do vertice ' +pIndice +': ' +pTempo)
        
        return pTempo
	}
    
    Grafo.Algoritmos.Profundidade = Profundidade // retorna metodo publico
}())
