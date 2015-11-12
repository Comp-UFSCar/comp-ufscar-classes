#ifndef _DADO_H_
#define _DADO_H_

/*
    Define o tipo tDado que contém a chave e um ponteiro para informação.
    O valor da chave funciona como índice e é utilizado para percorrer a árvore,
    quando a chave é encontrada, retorna-se a informação associada àquela chave.
*/
typedef struct {
    int chave;
    void *info;
} tDado;

#endif // _DADO_H_
