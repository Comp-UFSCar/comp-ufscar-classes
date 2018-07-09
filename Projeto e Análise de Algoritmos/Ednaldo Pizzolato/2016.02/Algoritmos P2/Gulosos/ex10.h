/*Algoritmos Gulosos*/
/*Earliest Time First*/
Class Horario{
	int ai;
	int bi;
}

int numAtividades;
recebe numAtividades;
Horario vetor[numAtividades];
recebe vetor;
Horario atual;
int menor=infinito;
int count = 0;

while(numAtividades>0){
	for(int i=0; i<numAtividades; i++){
		if(vetor[i].bi<menor){
			atual=vetor[i];
		}
	}
	for(i=0; i<numAtividades; i++){
		if(atual!=vetor[i]){
			if(vetor[i].ai<=atual.bi){
				delete vetor[i];
				numAtividades-1;
			}
		}
		else{
			count++;
			delete vetor[i];
			numAtividades-1;
		}
	}
}

return count;

