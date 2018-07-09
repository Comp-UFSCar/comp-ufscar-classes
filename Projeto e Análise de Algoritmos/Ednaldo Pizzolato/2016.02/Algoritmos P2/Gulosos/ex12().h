/*Algoritmo Guloso*/
/*Minimum Spanning Tree: Algoritmo de Kruskal*/
Class No{
	char nome;
	int peso;
	No* next;
}
Class No2{
	char nome;
	bool visitado;
}

int numNos;
recebe numNos;
No2 verificaVisitou[numNos];
No lista[numNos];
No listaOrdenada
recebe lista;

for(int i=0; i<numNos; i++){
	verificaVisitou[i].visitado=0;
	verificaVisitou[i].nome=lista[i].nome;
}

//caso geral
while(temNaoVisitado(verificaVisitou)){
	retornaMenorAresta(lista);
	
	while(verificaVisitou[i].nome!=minimoChar){
		verificaVisitou[i].visitado=1;
		i++;
	}
}






