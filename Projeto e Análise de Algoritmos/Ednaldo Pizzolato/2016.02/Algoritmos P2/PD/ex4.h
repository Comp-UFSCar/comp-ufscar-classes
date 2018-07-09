/*Programação Dinâmica*/

/*Dada uma sequência de números inteiros, retorne a maior soma possível usando apenas elementos de posições contíguas*/

int numElementos;
int Tabela[numElementos][2];

recebe numElementos;
 for(int i=0; i<numElementos; i++){
	 recebe Tabela[i][0];
	 Tabela[i][1]='0';
 }
 
 Tabela[0][1]=Tabela[0][0];
 
 for(int i=1; i<numElementos; i++){
	 Tabela[i][1]=max{Tabela[i][0]; Tabela[i][0]+Tabela[i-1][1]}
 }
 
 return max{Tabela[][1]};