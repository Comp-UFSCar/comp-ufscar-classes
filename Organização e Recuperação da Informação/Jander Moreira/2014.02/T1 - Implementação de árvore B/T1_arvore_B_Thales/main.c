#include <stdio.h>
#include <stdlib.h>

#include "arvoreb.h"

int main()
{

    int i;


    tNo *noRaiz = (tNo *) malloc(sizeof(tNo));

    tDado dado;
    insereDado(&dado, 10, "merda");

    printf("Insercao: %d\n", insereChave(noRaiz, dado));

    insereDado(&dado, 15, "merda");

    printf("Insercao: %d\n", insereChave(noRaiz, dado));

    insereDado(&dado, 8, "merda");

    printf("Insercao: %d\n", insereChave(noRaiz, dado));
    insereDado(&dado, 5, "merda");

    printf("Insercao: %d\n", insereChave(noRaiz, dado));
    insereDado(&dado, 20, "merda");

    printf("Insercao: %d\n", insereChave(noRaiz, dado));

    for(i = 0 ; i < noRaiz->cont; i++)
        printf("\t    %d", noRaiz->dado[i].chave);

    printf("\n");
    for(i = 0 ; i < noRaiz->cont + 1; i++)
        printf("\t%d", noRaiz->pont[i]);



/*
    *   Código para testes do buscaChave() usando valores genéricos para
    *   ponteiros e chaves
*/

//    tNo *noRaiz = inicializaNo();
//    tNo *noEsquerda = inicializaNo();
//    tNo *noDireita = inicializaNo();
//
//    noRaiz->cont = 1;
//    noRaiz->dado[0].chave = 50;
//    noRaiz->pont[0] = noEsquerda;
//    noRaiz->pont[1] = noDireita;
//
//    noEsquerda->cont = MAXIMO_CHAVES;  // RAÍZ PARA ORDEM 5
//    for(i = 0; i < noEsquerda->cont; i++)
//    {
//        noEsquerda->dado[i].chave = (i+1)*10;
//    }
//
//
//    noDireita->cont = MAXIMO_CHAVES;  // RAÍZ PARA ORDEM 5
//    for(i = 0; i < noDireita->cont; i++)
//    {
//        noDireita->dado[i].chave = (i+6)*10;
//    }
//
//    for(i = 0 ; i < MAXIMO_CHAVES; i++)
//        printf("\t    %d", noEsquerda->dado[i]);
//
//    printf("\n");
//    for(i = 0 ; i < ORDEM; i++)
//        printf("\t%d", noEsquerda->pont[i]);
//
//
//    if(buscaChave(noRaiz, 60) == TRUE)
//        printf("\n\tACHOU\n\n");
//    else
//        printf("\n\tNAO ACHOU\n\n");
//
/*
    *   Código para testes da struct tDado e suas funções
    *   Localizados nos arquivos:   [dado.h], [dado.c]
*/
/*
    tDado *dadoTeste = inicializaDado();

    printf("Inicializa\n\tPos Memoria:%d\n\tChave:%d\n\tInfo:%s\n\n", dadoTeste, dadoTeste->chave, dadoTeste->info);

    insereDado(dadoTeste, 10, "arvore B eh legal");
    printf("Insere\n\tPos Memoria:%d\n\tChave:%d\n\tInfo:%s\n\n", dadoTeste, dadoTeste->chave, dadoTeste->info);

    finalizaDado(dadoTeste);
    printf("Finaliza\n\tPos Memoria:%d\n\tChave:%d\n\tInfo:%s\n\n", dadoTeste, dadoTeste->chave, dadoTeste->info);
*/

    return 0;
}
