// AlgoritmosAula01.cpp : Aqui serão programados os três algoritmos de encontrar a subsequência de valor máximo 
// de uma sequência de números inteiros.
//

#include "stdafx.h"
#include <stdio.h>

void algoritmoForcaBruta(int*, int);
void algoritmoForcaBrutaMelhorado(int*, int);
void algoritmoForcaBrutaMelhorado2(int*, int);
void algoritmoDivisoes(int*, int);
void algoritmoLinear(int*, int);

int main()
{
    int numeros[10] = {5,8,-10,5,3,7,-2,3,-1,-2};

    algoritmoForcaBruta(numeros,10);
    algoritmoForcaBrutaMelhorado(numeros,10);
    algoritmoForcaBrutaMelhorado2(numeros,10);
    algoritmoLinear(numeros, 10);

	return 0;
}

void algoritmoForcaBruta(int* numeros, int n){
    
    int i=0, j=0, k=0, somaLocal=0, maximoGlobal=0, iMaximo=0, jMaximo=0, iteracoes=0;

    if(numeros == NULL)
        return;

    for(i=0; i<n; i++)
        for(j=i; j<n; j++){
            somaLocal = 0;

            for(k=i; k<=j; k++){ //de i até j, pois pode serem iguais ou j maior que i
                somaLocal += numeros[k];
                iteracoes++; //somando o numero de iteracoes da funcao
            }

            if(somaLocal > maximoGlobal){
                maximoGlobal = somaLocal;
                iMaximo = i;
                jMaximo = j;
            }
        }
    //fim algoritmo

    printf("A maior soma e de %d do %d-esimo elemento ao %d-esimo elemento em %d iteracoes\n", maximoGlobal, iMaximo+1, jMaximo+1, iteracoes);
}

void algoritmoForcaBrutaMelhorado(int* numeros, int n){
    
    int i=0, j=0, somaLocal=0, maximoGlobal=0, iMaximo=0, jMaximo=0, iteracoes=0;

    if(numeros == NULL)
        return;

    for(i=0; i<n; i++){
        somaLocal = 0;
        for(j=i; j<n; j++){
            somaLocal += numeros[j];

            if(somaLocal > maximoGlobal){
                maximoGlobal = somaLocal;
                iMaximo = i;
                jMaximo = j;
            }
            iteracoes++; //somando o numero de iteracoes da funcao
        }
    }
    //fim algoritmo

    printf("A maior soma e de %d do %d-esimo elemento ao %d-esimo elemento em %d iteracoes\n", maximoGlobal, iMaximo+1, jMaximo+1, iteracoes);
}

void algoritmoForcaBrutaMelhorado2(int* numeros, int n){
    
    int i=0, j=0, somaLocal=0, maximoGlobal=0, iMaximo=0, jMaximo=0, iteracoes=0;

    if(numeros == NULL)
        return;

    for(i=0; i<n; i++){
        somaLocal = 0;
        for(j=i; j<n; j++){
            somaLocal += numeros[j];

            if(somaLocal > maximoGlobal){
                maximoGlobal = somaLocal;
                iMaximo = i;
                jMaximo = j;
            } else if(somaLocal <= 0)
                j = n; //se for negativo, ja nao favorece uma subsequencia apos essa formada
            iteracoes++; //somando o numero de iteracoes da funcao
        }
    }
    //fim algoritmo

    printf("A maior soma e de %d do %d-esimo elemento ao %d-esimo elemento em %d iteracoes\n", maximoGlobal, iMaximo+1, jMaximo+1, iteracoes);
}

void algoritmoLinear(int* numeros, int n){
    
    int i=0, maximoLocal=0, maximoGlobal=0, iMaximo=0, iMaximoTemp=0, jMaximo=0, iteracoes=0;

    if(numeros == NULL)
        return;

    for(i=0; i<n; i++){
        if(maximoLocal + numeros[i] > 0)
            maximoLocal += numeros[i];
        else {
            maximoLocal = 0; //se for negativo, ja nao favorece uma subsequencia apos essa formada
            iMaximoTemp = i+1;
        }

        if(maximoLocal > maximoGlobal){
            maximoGlobal = maximoLocal;
            jMaximo = i;
            iMaximo = iMaximoTemp;
        }
        iteracoes++;
    }
    //fim algoritmo

    printf("A maior soma e de %d do %d-esimo elemento ao %d-esimo elemento em %d iteracoes\n", maximoGlobal, iMaximo+1, jMaximo+1, iteracoes);
}