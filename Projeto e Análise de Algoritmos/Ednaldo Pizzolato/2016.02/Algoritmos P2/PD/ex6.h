/*Programação Dinâmica*/

/*Problema da Mochila: PROBLEMA ITENS LIMITADOS: Só tem 1 de cada*/
int numItens;
int TabelaMochila[numItens][3];
int capacidade;
int TabelaPD[capacidade+1][numItens+1];

recebe numItens;
recebe capacidade;

 for(int i=0; i<numElementos; i++){
	 recebe TabelaPD[i][0];
	 recebe TabelaPD[i][1];
	 recebe TabelaPD[i][2];
 }
 
TabelaPD[0][]=0;
TabelaPD[][0]=0;


for(i=1; i<=capacidade;i++){
	for(int j=1; j<=numItens;j++){
		if(TabelaMochila[j][1]<i){
			TabelaPD[i][j]=TabelaPD[i][j-1]; //estourou 
		}
		else{
			TabelaPD[i][j]=max{TabelaPD[i][j-1]; TabelaPD[i-TabelaMochila[i][1]][j-1]+TabelaMochila[i][2]};
		}
	}
}


return max{TabelaPD[][]};