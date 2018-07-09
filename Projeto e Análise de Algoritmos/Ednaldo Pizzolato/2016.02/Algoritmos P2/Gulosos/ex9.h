/*Algoritmos Gulosos*/

/*Problema da Mochila: Problema Particionar*/
/*Supondo que tenha o item, o valor/kg ordenado, e a quantidades em kilos*/

int numItens;
int TabelaMochila[numItens][3];
int capacidade;

recebe numItens;
recebe capacidade;

 for(int i=0; i<numItens; i++){
	 recebe TabelaMochila[i][0];
	 recebe TabelaMochila[i][1];
	 recebe TabelaMochila[i][2];
 }
 
int count=0;
for(i=0; i<numItens; i++){
	if(capacidade>=TabelaMochila[i][2]){
		capacidade=capacidade-TabelaMochila[i][2];
		count=count+(TabelaMochila[i][1]*TabelaMochila[i][2]);
	}
	else{
		count=count+(Tabela[i][1]*capacidade);
	}
}
 
 