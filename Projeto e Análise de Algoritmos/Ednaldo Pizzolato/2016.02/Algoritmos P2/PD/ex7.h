/*Programação Dinâmica*/

/*Problema Tabuleiro Moedas*/
int Tabela[m][n];
int count=0;
recebe Tabela[m][n]; //com tudo zero, e 1 onde tem moeda

for(int i=0; i<m: i++){
	if(Tabela[i][0]==1){
		count=count+1;
	}
	Tabela[i][0]=count;
}

count=0;

for(int i=0; i<n: i++){
	if(Tabela[0][n]==1){
		count=count+1;
	}
	Tabela[0][n]=count;
}

for(i=1; i<m; i++){
	for(j=1; j<n; j++){
		Tabela[i][j]=max{Tabela[i-1][j]; Tabela[i][j-1]};
		if(Tabela[i][j]==1)
			Tabela[i][j]+=1;
	}
}

return Tabela[m-1][n-1];