/*
 * KRUSKAL.JS
 * 
 * DATA CRIACAO: 25/11/2012
 * 
 * DESCRICAO: declaracao da funcao de kruskal, a fim de encontrar a arovre spanning minimal
 * de um dado grafo.
 * 
 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 * LISTA DE MODIFICACOES
 * 
 * 24/11/2012: criacao do arquivo, +mst
 * 25/11/2012: +Kruskal(), -lsAresPrioridade.sort()
 * 26/11/2012: +lsGruposInseguros
 * 29/11/2012: linha 110 executava push em V2, o correto seria V1.
 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 */

(function(){
    
    var Kruskal = function() {
        
        if(Grafo.Direcionado) {
            lnConsole.Inserir('Kruskal ainda não foi implementado para grafos direcionados!');
            return 0;
        }
        lnConsole.Inserir('Inicio do algoritmo de Kruskal');
		
        var lsAresPrioridade = [], lsGruposInseguros = [];
        var i, j, arestaAtual, numVertices = Grafo.MatrizAdj.length;
        var menorAtual, tmpTroca, numAresColocadas = 0;
        
        var mst = [];  // minimal spanning tree
        for (i = 0; i < numVertices; i++) {
            mst[i] = [];
            for(j = 0; j < numVertices; j++)
                mst[i][j] = 0;      // inicializa a minimal spanning tree
        }
        
        // adiciona todas as arestas a lista de prioridades
        for(i = 0; i < numVertices; i++)
            for(j = i +1; j < numVertices; j++)
                if(Grafo.MatrizAdj[i][j] > 0)
                    lsAresPrioridade.push({
                        v1: i, 
                        v2: j, 
                        peso: Grafo.MatrizAdj[i][j]
                    });
        
        var ConjContemA, ConjContemB, interacao = 0;
        // enquanto numero de arestas e menor que o numero de vertices -1 
        // ou existem elementos na lista de prioridades
        while(numAresColocadas < numVertices -1 || lsAresPrioridade.length) {
            console.log('Interacao: ' + (++interacao));
            
            // procura pela menor aresta
            menorAtual = 0;
            for(i = 1; i < lsAresPrioridade.length; i++) {
                if(lsAresPrioridade[menorAtual].peso > lsAresPrioridade[i].peso) 
                    menorAtual = i;
            }
            tmpTroca = lsAresPrioridade[0];  // Coloca a menor aresta na primeira posicao do vetor
            lsAresPrioridade[0] = lsAresPrioridade[menorAtual];
            lsAresPrioridade[menorAtual] = tmpTroca;
            arestaAtual = lsAresPrioridade.shift(); // equivalente ao .pop(), mas retira da primeira posicao do vetor
            
            lnConsole.Inserir('Menor aresta encontrada: [' +arestaAtual.v1 +'] - [' +arestaAtual.v2 +']');
        
            // verifica se os vertices ligados pela aresta encontrada pertencem a um mesmo conjunto
            // (se sim, essa aresta NAO E SEGURA)
            ConjContemA = ConjContemB = -1;
            i = 0;
            
            while(i < lsGruposInseguros.length && (ConjContemA == -1 || ConjContemB == -1)) {
                j = 0;
                
                while(j < lsGruposInseguros[i].length && (ConjContemA == -1 || ConjContemB == -1)) {
                    if(lsGruposInseguros[i][j] == arestaAtual.v1) ConjContemA = i;
                    if(lsGruposInseguros[i][j] == arestaAtual.v2) ConjContemB = i;
                    
                    j++;
                }
                i++;
            }
            
            console.log('Conj A: ' +ConjContemA +'. Conj B: ' +ConjContemB);
            
            // se os dois conjuntos nao sao iguais, ou ambos nao estao em nenhum conjunto (A e B == -1)
            // um novo vertice acaba de ser encontrado ou eles pertencem a sub-arvores distintas
            if(ConjContemA == -1 && ConjContemB == -1 || ConjContemA != ConjContemB) {
                
                // Nenhum deles esta definido. Insere um novo conjunto na lista com ambos
                if(ConjContemA == -1 && ConjContemB == -1)
                    lsGruposInseguros[lsGruposInseguros.length] = [arestaAtual.v1, arestaAtual.v2];
                
                // cada um faz parte de um conjunto diferente, une ambos conjuntos
                else if(ConjContemA > -1 && ConjContemB > -1) {
                    var conjMaisProx = ConjContemA < ConjContemB ? ConjContemA : ConjContemB;
                    var conjMaisDist = ConjContemA > ConjContemB ? ConjContemA : ConjContemB;
                    
                    while(lsGruposInseguros[conjMaisDist].length > 0)
                        lsGruposInseguros[conjMaisProx].push(lsGruposInseguros[conjMaisDist].pop());
                    
                    lsGruposInseguros.splice(conjMaisDist, 1); // remove conjunto vazio
                }
                // v1 faz parte de um conjunto mas v2 nao. Insere v2 no conjunto de v1
                else if(ConjContemA > -1)
                    lsGruposInseguros[ConjContemA].push(arestaAtual.v2);
                else
                    // v2 faz parte de um conjunto mas v1 nao. Insere v1 no conjunto de v2
                    lsGruposInseguros[ConjContemB].push(arestaAtual.v1);
                
                // Adiciona a aresta na mst
                mst[arestaAtual.v1][arestaAtual.v2] = arestaAtual.peso; 
                mst[arestaAtual.v2][arestaAtual.v1] = arestaAtual.peso;
                
                console.log('[' +lsGruposInseguros[0] +'][' +lsGruposInseguros[1] +'][' +lsGruposInseguros[2] +'][' +lsGruposInseguros[3] +'][' +lsGruposInseguros[4] + ']');
                console.log('===\n');
                numAresColocadas++; // incrementa o numero de arestas encontradas
            }
            else console.log('>> Aresta ignorada!');
        }
        
        Grafo.Algoritmos.MatrizAdj = mst;   // retorna objeto publico
        initApp(true);      // reinicia a aplicacao, agora buscando a matri de adjacencia a qual
        // o algoritmo foi aplicado, e nao mais a original
        
        return false;
    }
    
    Grafo.Algoritmos.Kruskal = Kruskal; // retorna metodo publico
}());
