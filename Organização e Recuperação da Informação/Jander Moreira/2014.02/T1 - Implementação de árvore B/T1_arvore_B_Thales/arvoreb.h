#ifndef _ARVOREB_H_
#define _ARVOREB_H_

#include "no.h"

/*
    Estrutura do tipo tArvoreB que contém um ponteiro do tipo tNo
    e uma variável integer para controle da ordem da árvore.
*/

typedef struct {
    tNo *raiz;
    int ordem;
} tArvoreB;

#endif // _ARVOREB_H_
