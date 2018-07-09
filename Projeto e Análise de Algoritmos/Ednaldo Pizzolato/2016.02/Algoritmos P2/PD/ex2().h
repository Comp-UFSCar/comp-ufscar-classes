/*Programação Dinâmica*/
/*Para troco n, usar a menor quantidade de moedas possível*/
int numMoedas;
int vetorMoedas[numMoedas];
int valorTroco;
int TabelaMoedas[valorTroco][2];

recebe numMoedas;
recebe valorTroco;
 for(int i=0; i<numMoedas; i++){
	 recebe vetorMoedas;
 }
 for(i=0; i<valorTroco;i++){
	 TabelaMoedas[i][0]=i;
	 TabelaMoedas[i][1]=infinito;
 }
 
TabelaMoedas[0][1]='0';

for(i=1; i<valorTroco;i++){
	for(j=0; j<numMoedas; j++){
		if(TabelaMoedas[i][0])==vetorMoedas[j]){
			TabelaMoedas[i][1]=1;
			break;
		}
		else if(TabelaMoedas[i][0])>vetorMoedas[j]){
			TabelaMoedas[i][1]=min{TabelaMoedas[i][1];1+TabelaMoedas[i-vetorMoedas[j]][1]};
		}
	}
}
 

/*Programação Dinâmica*/
/*Para troco n, usar a menor quantidade de moedas possível: Jeito Inteligente*/
?????????????????????????????