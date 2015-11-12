/* 
 * Desenvolva um agoritmo para testar se uma pilha P1 tem mais elementos
 * que uma pilha P2. Considere que P1 e P2 já existem.
 */
 
#include<stdio.h>
#include<stdlib.h>
#include<time.h>
#include "stack.h"

#define MAX 10

int geraTamanho(){
	return rand()%MAX;
}

void insereElementos(Stack *s){
	int i;
	for(i = geraTamanho(); i > -1; i--)
		push(s, rand()%100);
}

int verificaTamanho(Stack *s){
	Stack pAux;
	createStack(&pAux);
	
	int tamanho = 0;
	
	// Passa todos os elementos para pilha auxiliar enquanto conta	
	while(s->top > -1){
		push(&pAux, s->iten[s->top--]);
		tamanho++;
	}
			
	// "Devolve" elementos da pilha aux para pilha original
	while(pAux.top > -1)
		push(s, pAux.iten[pAux.top--]);
	
	return tamanho;
}

int main(){
	srand(time(NULL));
	
	// Cria as pilhas
	Stack p1, p2;
	createStack(&p1);
	createStack(&p2);

	// Insere elementos nas pilhas de maneira aleatoria
	insereElementos(&p1);
	insereElementos(&p2);

	show(&p1);
	show(&p2);
	
	int tamP1, tamP2;
	tamP1 = verificaTamanho(&p1);
	tamP2 = verificaTamanho(&p2);
	
	if(tamP1 > tamP2)
		printf("\n\nP1 possui mais elementos que P2");
	
	return 0;
}
