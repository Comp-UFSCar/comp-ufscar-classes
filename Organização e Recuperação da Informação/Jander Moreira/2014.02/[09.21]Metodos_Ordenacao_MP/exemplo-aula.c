#include <stdio.h>
#include <stdlib.h>

#define TAM 2000000

void gereVetorAleatorio(int *vetor, int n) {
    int i;

    srand(time(0));
    for(i = 0; i < n; i++)
        vetor[i] = rand() % TAM;
}

void mostreVetor(int *vetor, int n) {
    int i;
    printf("[ ");
    for(i = 0; i < n; i++)
        printf("%d ", vetor[i]);
    printf(" ]\n");
}

void insertsort(int *vetor, int n) {
    int i, j;
    int temp;

    for(i = 1; i < n; i++) {
        temp = vetor[i];
        j = i - 1;
        while(j > 0 && vetor[j] > temp){
            vetor[j + 1] = vetor[j];
            j--;
            printf(".");
        }
        vetor[j + 1] = temp;
        printf("\n");
    }
}

int main(){
    int vetor[TAM];
    int tamanho;

    printf(" Numero de itens: ");
    scanf("%d ", &tamanho);

    gereVetorAleatorio(vetor, tamanho);
    mostreVetor(vetor, tamanho);

    //ordeneVetor(vetor, tamanho);

    mostreVetor(vetor, tamanho);

    return 0;
}
