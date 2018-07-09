/*Algoritmo Guloso*/
/*Minimum Spanning Tree: Algoritmo de PRIM*/
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
recebe lista;

for(int i=0; i<numNos; i++){
	verificaVisitou[i].visitado=0;
	verificaVisitou[i].nome=lista[i].nome;
}

//supor que o primeiro nó escolhido seja o A: primeiro caso
verificaVisitou[0].visitado=1;
i=0;
minimo=infinito;
minimoChar='0';
No* PAux;
PAux=lista[i];
while(PAux->next!=NULL){
	if(PAux->peso<minimo){
		minimoChar=PAux->nome;
	}
	PAux=Paux->next;
}
i=0;
while(verificaVisitou[i].nome!=minimoChar){
	verificaVisitou[i].visitado=1;
	i++;
}

//caso geral
while(temNaoVisitado(verificaVisitou)){
	minimo=infinito;
	for(i=0;i<numNos;i++){
		if(verificaVisitou[i].visitado==1){
			j=0;
			PAux=lista[i];
			while(PAux->next!=NULL){
				if(PAux->peso<minimo){
					minimoChar=PAux->nome;
				}
				PAux=PAux->next;
			}
		}
	}
	i=0;
	while(verificaVisitou[i].nome!=minimoChar){
		verificaVisitou[i].visitado=1;
		i++;
	}
}






