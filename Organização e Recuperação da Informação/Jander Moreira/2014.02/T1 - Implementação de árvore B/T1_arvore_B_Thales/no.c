#include "no.h"

int noCheio(tNo *no)
{
    if(no->cont == MAXIMO_CHAVES)
        return TRUE;
    else
        return FALSE;
}

int ehFolha(tNo *no)
{
    int i = 0;
    for(i = 0; i < ORDEM; i++)
    {
        if(no->pont[i] != 0)
            return FALSE;
    }
    return TRUE;
}

tNo * escolheFilho(tNo *no, int chave)
{
    int i = 0;
    for(i = 0; i < no->cont; i++)
    {
        if(chave < no->dado[i].chave)
            return no->pont[i];
    }
    return no->pont[i];
}

int insereChave(tNo *no, tDado dado){
    if(no->cont == 0) // arvore vazia
    {
        no->cont += 1;
        no->dado[0] = dado;
        return TRUE;
    }
    else
    {
        int houvePromocao;
        tDado dadoPromovido;
        tNo *novoNo;

        // insercao caso a arvore nao esteja vazia
        busqueEInsira(no, dado, &houvePromocao, &dadoPromovido, &novoNo);
    }
}

int busqueEInsira(tNo *no, tDado dadoNovo, int *houvePromocao, tDado *dadoPromovido, tNo **novoNo)
{
    // verifica se a chave ja existe
    if(buscaChave(no, dadoNovo.chave) == TRUE)
        return FALSE;
    else
    {
        int i = 0;
        if(ehFolha(no) == TRUE) // insercao no noh folha
        {
            if(noCheio(no) == FALSE)    // ha espaco no noh
            {
                // inserir nova chave no noh atual e ajustar ponteiros
                for(i = 0; i < no->cont; i++)
                {
                    if(dadoNovo.chave < no->dado[i].chave)  // chaveNova menor que chaveAtual
                    {
                        int j;

                        for(j = no->cont; j > i; j--)   // ajusta posicao das chaves
                        {
                            no->dado[j] = no->dado[j-1];
                        }
                        no->dado[i] = dadoNovo;
                        no->cont += 1;
                        i = no->cont;
                    }
                    else if(no->dado[i+1].chave == 0) // proxima chave eh nula
                    {
                        no->dado[i+1] = dadoNovo;
                        no->cont += 1;
                        i = no->cont;
                    }
                }
                houvePromocao = FALSE;
            }
            else // nao ha espaco no noh atual
            {
                divida(no, dadoNovo, 0, novoNo, dadoPromovido);
                houvePromocao = TRUE;
                printf("\tDADO PROMOVIDO: %d\n", dadoPromovido->chave);
            }
            return TRUE;
        }
        else    // NAO EH NOH FOLHA
        {   // PROCURA PELO CAMINHO PARA INSERIR
            // Determine, verificando o valor das chaves, o nodu filho correto
        }
    }
}

void divida(tNo *no, tDado dadoNovo, tNo * noDireita, tNo **novoNo, tDado *dadoPromovido)
{
    // criacao do novo noh
    //*novoNo = (tNo*)malloc(sizeof(tNo));
    tNo *noh = (tNo*)malloc(sizeof(tNo));

    int m = ORDEM / 2;

    if(dadoNovo.chave < no->dado[m].chave)  // se nova chave menor que m
    {
        int i = 0;
        for(m; m < MAXIMO_CHAVES; m++)
        {
            noh->dado[i] = no->dado[m];
            inicializaDado(&no->dado[m]);
            noh->pont[i+1] = no->pont[m+1];
            no->pont[m+1] = 0;
            noh->cont += 1;
            i++;
            no->cont--;
        }
        // inserir nova chave no noh atual e ajustar ponteiros
        for(i = 0; i < no->cont; i++)
        {
            if(dadoNovo.chave < no->dado[i].chave)  // chaveNova menor que chaveAtual
            {
                int j;

                for(j = no->cont; j > i; j--)   // ajusta posicao das chaves
                {
                    no->dado[j] = no->dado[j-1];
                }

                no->dado[i] = dadoNovo;
                no->cont += 1;
                i = no->cont;
            }
            else if(no->dado[i+1].chave == 0) // proxima chave eh nula
            {
                no->dado[i+1] = dadoNovo;
                no->cont += 1;
                i = no->cont;
            }
        }
    }
    else
    {
        int i = 0;
        for(m = m+1; m < MAXIMO_CHAVES; m++)
        {
            noh->dado[i] = no->dado[m];
            inicializaDado(&no->dado[m]);
            noh->pont[i+1] = no->pont[m+1];
            no->pont[m+1] = 0;
            noh->cont += 1;
            i++;
            no->cont--;
        }
        // inserir nova chave no noh atual e ajustar ponteiros
        for(i = 0; i < noh->cont; i++)
        {
            if(dadoNovo.chave < noh->dado[i].chave)  // chaveNova menor que chaveAtual
            {
                int j;

                for(j = noh->cont; j > i; j--)   // ajusta posicao das chaves
                {
                    noh->dado[j] = noh->dado[j-1];
                }

                noh->dado[i] = dadoNovo;
                noh->cont += 1;
                i = noh->cont;
            }
            else if(noh->dado[i+1].chave == 0) // proxima chave eh nula
            {
                noh->dado[i+1] = dadoNovo;
                noh->cont += 1;
                i = noh->cont;
            }
        }
    }
    // No Algoritmo do Jander tem uma linha sobre ajustar ponteiro a direita de nova chave -- n implementado aqui

    noh->pont[0] = no->pont[no->cont];
    no->pont[no->cont] = 0;
    *dadoPromovido = no->dado[no->cont-1];
    inicializaDado(&no->dado[no->cont-1]);
    no->cont--;

    *novoNo = noh;
}

int buscaChave(tNo *no, int chave){
    int i = 0;

    if (no == 0)    // ponteiro nulo: arvore vazia
        return FALSE;
    else
    {
        // verifica todas chaves dentro do no
        for(i = 0; i < no->cont; i++)
        {
            if(no->dado[i].chave == chave)
                return TRUE;    // se encontrar no noh, retorna TRUE
        }
        if (ehFolha(no) == TRUE)    // se for folha
            return FALSE;
        else
            return buscaChave(escolheFilho(no, chave), chave);  // fazer busca no noh filho correto
    }
}



int removeChave(){

}
