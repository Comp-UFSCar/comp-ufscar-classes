/*Algoritmo Guloso*/
/*Huffman*/

Class No{
	int freq;
	string valorRepresentado;
	No* direita;
	No* esquerda;
	No* diagDireita;
	No* diaEsquerda;
}

string palavra;
No listaNos[];
recebe listaNos; //supondo que já receba pronto com frequencias
No a, b;

ordena(listaNos);
while(tam(listaNos)>1){
	(a,b) = remove2primeirosNos(listaNos);
	No novo = criaNo(a,b); //valorRepresentado 'a' concatena com valorRepresentado 'b'; soma frequencias
	insereInteligente(novo, listaNos);
}
