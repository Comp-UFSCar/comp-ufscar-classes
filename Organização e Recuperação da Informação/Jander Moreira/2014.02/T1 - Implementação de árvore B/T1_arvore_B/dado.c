#include "dado.h"

/*
    Funções para controlar a struct tDado
*/

/*
INICIALIZA_DADO_PONTEIRO
    DESCRIÇÃO:  Faz a alocação dinâmica da memória com o tipo tDado e atribui
        valores 'nulos' para chave e info.
    RETORNO:    Um tDado alocado na memória.
*/
tDado * inicializaDado_ponteiro()
{
    tDado *novo = (tDado *) malloc(sizeof(tDado));
    novo->chave = 0;   // chave 'inválida'
    novo->info = 0; // null
    return novo;
}

/*
INICIALIZA_DADO
    DESCRIÇÃO:  Faz a alocação dinâmica da memória com o tipo tDado e atribui
        valores 'nulos' para chave e info.
    RETORNO:    Um tDado alocado na memória.
*/
void inicializaDado(tDado *dado)
{
    dado->chave = 0;
    dado->info = 0;
}

/*
INSERE_DADO
    DESCRIÇÃO:  Insere uma chave e uma informação ao tDado desejado.
    ENTRADA:    ponteiro para tDado, inteiro para a chave, a informação desejada.
*/
void insereDado(tDado *dado, int chave, void *info)
{
    dado->chave = chave;
    dado->info = info;
}

/*
FINALIZA_DADO
    DESCRIÇÃO:  Desaloca da memória o tDado desejado.
    ENTRADA:    ponteiro para tDado.
*/
void finalizaDado(tDado *dado)
{
    free(dado->info);
    free(dado);
}
