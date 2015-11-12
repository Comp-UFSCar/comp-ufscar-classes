#ifndef _NO_H_
#define _NO_H_
#include "dado.h"
#include "config.h"

/*
    Estrutura do tipo tNo que contém um vetor tDado com o MAXIMO_CHAVES
    que a ORDEM da árvore permite, também um outro vetor de ponteiro
    do tipo tNo com tamanho ORDEM e uma variável de controle integer
    com a quantidade de chaves no nó.

    OBS: ORDEM, MAXIMO_CHAVES, estão definidos no arquivo [config.h]
*/

typedef struct tNo{

    tDado dado[MAXIMO_CHAVES];
    struct tNo *pont[ORDEM];
    int cont;

} tNo;

#endif
