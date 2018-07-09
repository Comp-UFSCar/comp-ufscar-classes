/*Programação Dinâmica*/

/*Problema da sequência de moedas: Pegar a maior quantidade de dineiro sem pegar moedas adjacentes*/
 int numMoedas;
 int Tabela[numMoedas+1][2];
 
 recebe numMoedas;
 for(int i=1; i<numMoedas; i++){
	 recebe Tabela[i][0];
 }
 Tabela[0][0]='0';
 
 Tabela[0][1]='0';
 Tabela[1][1]=Tabela[1][0];
 
 for(i=0; i<numMoedas+1-2; i++){
	Tabela[i+2][1]=max{Tabela[i][1]+Tabela[i+2][0]; Tabela[i+1][1]};	
 }
 
 return Tabela[numMoedas+1][1];
 
 