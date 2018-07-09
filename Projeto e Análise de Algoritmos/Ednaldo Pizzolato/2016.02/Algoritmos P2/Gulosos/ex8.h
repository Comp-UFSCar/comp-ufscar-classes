/*Algoritmos Gulosos*/

/*Para troco n, usar a menor quantidade de moedas possível*/
int numMoedas;
int vetorMoedas[numMoedas];
int valorTroco;

recebe numMoedas;
recebe valorTroco;
 for(int i=0; i<numMoedas; i++){
	 recebe vetorMoedas[i];
 }

int count=0;
for(i = numMoedas-1; i>=0; i--){
	while(troco>vetorMoedas[i]){
		troco=troco-vetorMoedas[i];
		count++;
	}
}