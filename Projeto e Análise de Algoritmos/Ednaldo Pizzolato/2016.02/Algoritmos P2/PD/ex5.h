/*Programação Dinâmica*/

/*Problema da Mochila: PROBLEMA ITENS ILIMITADOS*/
int numItens;
int TabelaMochila[numItens][3];
int TabelaPD[capacidade+1][2];
int capacidade;

recebe numItens;
recebe capacidade;

 for(int i=0; i<numElementos; i++){
	 recebe Tabela[i][0];
	 recebe Tabela[i][1];
	 recebe Tabela[i][2];
 }
 
 for(i=0;i<capacidade+1;i++){
	 TabelaPD[i][0]=i;
	 TabelaPD[i][1]='0';
 }

 for(i=1;i<capacidade+1;i++){
	 for(int j=0; j<numElementos; j++){
		if(TabelaPD[i][0]>=TabelaMochila[j][1]){
			TabelaPD[i][1]=max{TabelaPD[i][1];TabelaMochila[j][2]+TabelaPD[i-TabelaMochila[j][1]][1]}
		}
	 }
 }
 
 return max{TabelaPD[][1]};
 
 
 