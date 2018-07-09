/*Programação Dinâmica*/

/*Longest Increasing Sequence*/

int numElementos;
int Tabela[numElementos][2];

recebe numElementos;
 for(int i=0; i<numElementos; i++){
	 recebe Tabela[i][0];
	 Tabela[i][1]='0';
 }
 Tabela[0][1]='1';

 for(i=1; i<numElementos;i++){
	 for(int j=i-1; j>0; j--){
		 if(Tabela[j][0]<Tabela[i][0]){
				Tabela[i][1]=max{Tabela[i][1];Tabela[j][1]+1};
		 }
	 }
 }
 
 return max{Tabela[][1]};