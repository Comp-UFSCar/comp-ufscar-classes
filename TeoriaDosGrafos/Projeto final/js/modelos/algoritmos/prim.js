/*
 * PRIM.JS
 * 
 * DATA CRIACAO: 25/11/2012
 * 
 * DESCRICAO: declaracao da funcao de Prim, a fim de encontrar a arvore spanning minimal
 * de um grafo predeterminado por sua matriz de adjacencia.
 * 
 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 * LISTA DE MODIFICACOES
 * 
 * 25/11/2012: criacao do arquivo, +Prim()
 * 27/11/2012: +lsVertInserido, +aresOrigMenor, +aresDestMenor, +aresPesoMenor, +aresOrigAtual
 *
 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 */

(function(){
    
    var Prim = function() {
        if (Grafo.Direcionado) {
            lnConsole.Inserir('Desculpe, mas Prim ainda não foi implementado para grafos direcionados!');
            return 0;
        }
        lnConsole.Inserir('Inicio do algoritmo de Prim');
        
        var i, j, numAresInseridas = 0,
        numVertices = Grafo.MatrizAdj.length,
        lsVertInserido = [],
        aresOrigMenor, aresDestMenor, aresPesoMenor, aresOrigAtual;
        
        var mst = [];  // minimal spanning tree
        for (i = 0; i < numVertices; i++) {
            mst[i] = [];
            for(j = 0; j < numVertices; j++)
                mst[i][j] = 0;      // inicializa a minimal spanning tree
        }
        
        lsVertInserido.push(Grafo.VertInicial);   //  insere o primeiro vertice na lista de vertices que estamos visitando
        
        while(numAresInseridas < numVertices -1) {
            aresPesoMenor = -1;
            
            for (i = 0; i < lsVertInserido.length; i++) {
                aresOrigAtual = lsVertInserido[i];
            
                for (j = 0; j < numVertices; j++)
                    if(lsVertInserido.indexOf(j) == -1) // se este vertice ainda nao foi inserido
                        // se existe uma aresta e seu peso e menor do que o peso atual ou esse nao esta iniciado
                        if (Grafo.MatrizAdj[aresOrigAtual][j] > 0 &&
                            (Grafo.MatrizAdj[aresOrigAtual][j] < aresPesoMenor || aresPesoMenor == -1)) {
                            aresOrigMenor = aresOrigAtual;
                            aresDestMenor = j;
                            aresPesoMenor = Grafo.MatrizAdj[aresOrigAtual][j];
                        }
            }
            
            console.log('Orig: ' +aresOrigMenor +'. Dest: ' +aresDestMenor +'. Peso: ' +aresPesoMenor);
            
            lsVertInserido.push(aresDestMenor);
            mst[aresOrigMenor][aresDestMenor] = aresPesoMenor;
            mst[aresDestMenor][aresOrigMenor] = aresPesoMenor;
            
            numAresInseridas++;
            console.log('Fim de uma interacao. Arestas inseridas ate agora: ' + numAresInseridas +'\n');
        }
        
        Grafo.Algoritmos.MatrizAdj = mst;
        initApp(true);
        
        return false;
    }
    
    Grafo.Algoritmos.Prim = Prim; // retorna metodo publico
}());
