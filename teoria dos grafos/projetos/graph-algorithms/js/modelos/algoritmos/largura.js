/*
 * LARGURA.JS
 * 
 * DATA CRIACAO: 30/11/2012
 * 
 * DESCRICAO: declaracao do algoritmo de busca em largura.
 * 
 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 * LISTA DE MODIFICACOES
 * 
 * 29/11/2012: criacao do arquivo, +largura()
 * 26/12/2012: refatoracao e impressao sobre o console adequado
 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 */

(function() {
    
    var Largura = function() {
        if (Grafo.Direcionado) {
            lnConsole.Inserir('Desculpe, mas Largura ainda não foi implementado para grafos direcionados!')
            return false
        }
        
        console.log('Inicio do algoritmo de busca em largura')        
        lnConsole.Inserir('Inicio da busca em largura')
        
        var tempo = 0, i, j,
            mat = [], vertices = [], cinzas = []
        
        // declara a nova matriz de adjacencia e um vetor que contem
        // os vertices achados durante o algoritmo de busca em largura
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
        
        vertices[Grafo.VertInicial].cor = 1 // cinza
        cinzas.push(Grafo.VertInicial)  // empilha vertice inicial na fila de prioridades
        
        console.log('Conjunto inicial:')
        console.log(vertices)
		
		//  enquanto existem cinzas, ie, vertices que possuem filhos nao olhados
		while (cinzas.length > 0) {
            var vertAtual = cinzas.shift()
            console.log('\nVertice em analise: ' +(vertAtual +1))
            lnConsole.Inserir('Vertice em análise: ' +(vertAtual +1))
            
			for (i = 0; i < Grafo.MatrizAdj.length; i++)
				//	o vertice i ee vizinho do vertice atual e ee branco
				if (Grafo.MatrizAdj[vertAtual][i] != 0 && vertices[i].cor == 0) {
					cinzas.push(i)
					vertices[i].ini = ++tempo
					vertices[i].cor = 1
					vertices[i].pred = vertAtual
					
					console.log(' Filho branco: ' +(i +1) +' no tempo ' +tempo)
					lnConsole.Inserir(' Filho branco: ' +(i +1) +' no tempo ' +tempo)
				}
			
			vertices[vertAtual].cor = 2
			vertices[vertAtual].fim = ++tempo
			console.log('Tempo de saída: ' +tempo)
			lnConsole.Inserir('Tempo de saída: ' +tempo)
		}

        // neste ponto, o algoritmo de busca em profundidade ja foi executado, mas seu resultado esta
        // contido no vetor vertices. Preenchemos, a partir de vertices a matriz mat
        for (i = 0; i < Grafo.MatrizAdj.length; i++) {
            if (i == Grafo.VertInicial)
                continue; // nao deixa que o vertice inicial tenha um predecessor
            
            mat[i][vertices[i].pred] = Grafo.MatrizAdj[i][vertices[i].pred]
            mat[vertices[i].pred][i] = Grafo.MatrizAdj[vertices[i].pred][i]
        }
        Grafo.Algoritmos.MatrizAdj = mat
        
        initApp(true)
        return false
    }
    
    Grafo.Algoritmos.Largura = Largura // retorna metodo publico
}())
