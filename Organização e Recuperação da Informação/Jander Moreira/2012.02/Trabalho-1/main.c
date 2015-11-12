#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include "registro.h"
#include "bubblesort.h"
#include "mergesort.h"
#include "quicksort.h"
#include "funcoes.h"

// Este codigo possui selo Cthulhu de Insanidade

// registro.h contem estrutura de dados do tipo Registro

/*
 * bubblesort.h
 * ----------------------------------------------------------
 * |														|
 * |	void bubbleSort(Registro *, int , int *, int *);	|
 * |    													|
 * ----------------------------------------------------------
 */

/*
 * mergesort.h
 * ----------------------------------------------------------
 * |														|
 * |  mergesort(int, int, Registro *, int, int *, int *);	|
 * |    													|
 * ----------------------------------------------------------
 */

/*
 * quicksort.h
 * ----------------------------------------------------------
 * |														|
 * |  void quickSort(Registro *, int, int, int *, int *);  	|
 * |    													|
 * ----------------------------------------------------------
 */


/*
 * funcoes.h
 * ----------------------------------------------------------
 * |														|
 * |	void exibeMenu(int *, int);							|
 * |   	void definirTamanho();								|
 * |	void initRegRandom(Registro *, int);				|
 * |	void initRegOrdenado(Registro *, int);				|
 * |	void exibeRegistro(Registro *, int);				|
 * |	 													|
 * ----------------------------------------------------------
 */

int main()
{
	srand(time(NULL));	// gerar numeros aleatorios
	int op, tamanho = 0, movimentacoes = 0, comparacoes = 0, init = 0;
	clock_t tInicio, tFim, tDecorrido;

	// Inicializar o Registro com algum tamanho
	definirTamanho(&tamanho);
	Registro *registro;
	registro = malloc(tamanho * sizeof(Registro));

	// Laco do Menu
	do{
		exibeMenu(&op, tamanho);
		switch(op){
			// Alterar o tamanho do Registro
			case 1:
				definirTamanho(&tamanho);
				registro = malloc(tamanho * sizeof(Registro));
				init = 0;
				break;

			// Inserir valores aleatorios no Registro
			case 2:
				initRegRandom(registro, tamanho);
				init = 1;
				break;

			// Inserir valores ordenados no Registro
			case 3:
				initRegOrdenado(registro, tamanho);
				init = 1;
				break;

			// Aplicar o BubbleSort
			case 4:
				if(init == 0)
					printf("\nXXXXXXXX\tREGISTRO\tNAO\tINICIALIZADO\tXXXXXXXX\n\n");
				else {
					tInicio = clock();
					bubbleSort(registro, tamanho-1, &comparacoes, &movimentacoes);  // Realiza a ordenacao do vetor
					tFim = clock();
					tDecorrido = ((tFim - tInicio) / (CLOCKS_PER_SEC / 1000));
					printf("\nAplicando o BubbleSort em um Registro de %d posicoes temos:", tamanho);
					printf("\n||==>> Comparacoes:\t%d\t==>> Movimentacoes:\t%d\t||\n",comparacoes, movimentacoes);
					printf("\n||==>> Executados em um tempo de aproximadamente %ld ms\n", tDecorrido);
				}
			break;

			// Aplicar o MergeSort
			case 5:
				if(init == 0)
					printf("\nXXXXXXXX\tREGISTRO\tNAO\tINICIALIZADO\tXXXXXXXX\n\n");
				else {
					comparacoes = movimentacoes = 0;
					tInicio = clock();
					mergesort (0, tamanho - 1, registro, &comparacoes, &movimentacoes);
					tFim = clock();
					tDecorrido = ((tFim - tInicio) / (CLOCKS_PER_SEC / 1000));
					printf("\nAplicando o MergeSort em um Registro de %d posicoes temos:", tamanho);
					printf("\n||==>> Comparacoes:\t%d\t==>> Movimentacoes:\t%d\t||\n",comparacoes, movimentacoes);
					printf("\n||==>> Executados em um tempo de aproximadamente %ld ms\n", tDecorrido);
				}
			break;

			// Aplicar o QuickSort
			case 6:
				if(init == 0)
					printf("\nXXXXXXXX\tREGISTRO\tNAO\tINICIALIZADO\tXXXXXXXX\n\n");
				else {
					comparacoes = movimentacoes = 0;
					tInicio = clock();
					quickSort(registro, 0, tamanho - 1, &comparacoes, &movimentacoes);
					tFim = clock();
					tDecorrido = ((tFim - tInicio) / (CLOCKS_PER_SEC / 1000));
					printf("\nAplicando o QuickSort em um Registro de %d posicoes temos:", tamanho);
					printf("\n||==>> Comparacoes:\t%d\t==>> Movimentacoes:\t%d\t||",comparacoes, movimentacoes);
					printf("\n||==>> Executados em um tempo de aproximadamente %ld ms\n\n", tDecorrido);
				}
			break;

			// Exibir o Registro
			case 7:
				if(init == 0)
					printf("\nXXXXXXXX\tREGISTRO\tNAO\tINICIALIZADO\tXXXXXXXX\n\n");
				else
					exibeRegistro(registro, tamanho);
				break;

            // Vetor decrescente
            case 8:
				initRegDecrescente(registro, tamanho);
				init = 1;
                break;

			// CTHULHU!
			case 666:
				printf("\n\n\tPh'nglui mglw'nafh Cthulhu R'lyeh wgah'nagl fhtagn\n\n\n");
		}
	}while(op!=0);

	return 0;
}


