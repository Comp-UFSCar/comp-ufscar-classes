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
            while(no->dado[i].chave < dadoNovo.chave) // Determine, verificando o valor das chaves, o nodu filho correto
                i++;

            busqueEInsira(no->pont[i],dadoNovo,houvePromocao,dadoPromovido,novoNo); // Realiza a insercao no noh filho

            if(houvePromocao == TRUE){  // Caso aconteca promocao
                if(noCheio(no) == FALSE){       //noh nao cheio
                    insereChave(no, *dadoPromovido);
                    houvePromocao = FALSE;
                }
                else
                {
                    tNo *novoFilho = (tNo*)malloc(sizeof(tNo)); //noh cheio
                    divida(no, dadoNovo, novoNo, novoFilho, dadoPromovido);
                    novoNo = novoFilho;
                }
            }
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

/*
REMOVE_CHAVE
    DESCRIÇÃO:  Realiza uma remoção simples de um tDado de um noh folha
    ENTRADA:    Ponteiro para tNo e tDado a ser removido
    RETORNO:    Verdadeiro se o dado foi removido com sucesso; falso caso contrario
*/
int removeChave(tNo *no, tDado dado){
    int i = 0;

    while((no->dado[i].chave != dado.chave) && (i < no->cont)){
        i = i + 1;
    }
    if(i == no->cont)
        return FALSE;   // nao encontrou a chave a ser removida
    else{
        no->cont--;
        for(i; i <= (no->cont - 1); i++){
            no->dado[i].chave = no->dado[i+1].chave;        // Recua as chaves no vetor para fechar o "buraco" deixado pela remocao
        }
        return TRUE;
    }
}

/*
REMOVA
    DESCRIÇÃO:  Busca e remove um dado de uma arvore B
    ENTRADA:    Ponteiro para tNo e tDado a ser removido
    RETORNO:    Verdadeiro se o dado foi removido com sucesso; falso caso contrario
*/
int remova(tNo *no, tDado dado){
    int status, chaveRemovida, houveViolacao;
    tNo *nodu;
    if (no == 0)    // se arvore for vazia
        return FALSE;
    else
    {
        status = busqueERemova(no, dado, chaveRemovida, houveViolacao);     // Busca recursiva na arvore
        if (no->cont == 0){
                nodu = no;
                no = no->pont[0];   // Se raiz fica vazia, passa a apontar para o primeiro ponteiro do proprio no raiz
                free(nodu);         // Libera o antigo noh da raiz
        }
        return status;
    }
}

/*
ESTA_NO_NOH
    DESCRIÇÃO:  Verifica se uma dada chave se encontra em um noh, sem percorrer pelos nohs filhos
    ENTRADA:    Ponteiro para tNo e int para chave
    RETORNO:    Verdadeiro se o dado foi encontrado; falso caso contrario
*/
int estaNoNoh(tNo *no, int chave){
    int i;
    for(i = 0; i < no->cont; i++){
        if(no->dado[i].chave == chave)
            return TRUE;
    }
    return FALSE;
}

/*
BUSQUE_E_REMOVA
    DESCRIÇÃO:  Busca e remove um dado de uma arvore B
    ENTRADA:    Ponteiro para tNo e tDado a ser removido
    RETORNO:    Verdadeiro caso a chave seja localizada; Falso caso contrário
*/
int busqueERemova(tNo *no, tDado dado, int houveViolacao){
    int status, i = 0;
    tNo *filho;
    tDado dadoSubstituto;

    if(estaNoNoh(no, dado.chave) == FALSE){         // Verifica se a chave a ser removida se encontra no noh atual
        if(ehFolha(no) == TRUE)
            return FALSE;                           // Retorna falso se chave não foi encontrada e noh atual for do tipo folha
        else
        {
            filho = escolheFilho(no, dado.chave);       // Escolhe filho do noh atual para continuar busca
            status = busqueERemova(filho, dado, houveViolacao);
            if(houveViolacao == TRUE)
                trateViolacao(no, filho);
            if(no->cont < MINIMO_CHAVES)
                houveViolacao = TRUE;
            else
                houveViolacao = FALSE;
            return status;
        }
    }
    else
    {
        if(ehFolha(no) == TRUE)
        {
            removeChave(no, dado);          // Remocao simples de um dado que se encontra em um noh folha
            if(no->cont < MINIMO_CHAVES)
                houveViolacao = TRUE;
            else
                houveViolacao = FALSE;
        }
        else
        {
            localizeSubstituto(filho, &dadoSubstituto);     // dadoSubstituto recebe o dado escolhido por referencia
            i = 0;
            while(no->dado[i].chave != dado.chave){  // Encontra a posicao da chave no vetor de dados do noh
                i = i + 1;
            }
            no->dado[i] = dadoSubstituto;
            filho = no->pont[i - 1];            // Filho recebe o ponteiro a esquerda da chave a ser removida

            status = busqueERemova(filho, dadoSubstituto, houveViolacao);   // Busca e remove o dadoSubstituto
            if(houveViolacao == TRUE)
                trateViolacao(no, filho);
            if(no->cont < MINIMO_CHAVES)
                houveViolacao = TRUE;
            else
                houveViolacao = FALSE;
        }
        return TRUE;
    }
}

/*
LOCALIZE_SUBSTITUTO
    DESCRIÇÃO:  Procura um dado substituto que tenha a mesma funcao estrutural do dado a ser removido
    ENTRADA:    Ponteiro para tNo, tDado para dado passado por referencia
*/
void localizeSubstituto(tNo *no, tDado *dado){
    if(ehFolha(no)== TRUE)
        *dado = no->dado[no->cont-1];            // Se no eh folha, pega a maior chave do noh
    else
        localizeSubstituto(no->pont[0], &dado);        // Se nao for noh folha, chama recursivamente a funcao para o ponteiro mais a esquerda
}

/*
TRATE_VIOLACAO
    DESCRIÇÃO:  Tratamento de violacao para numero de chaves menor do que o minimo permitido
    ENTRADA:    Ponteiro para tNo pai e ponteiro para tNo filho no qual ocorreu a subutilizacao
*/
void trateViolacao(tNo *pai, tNo *filho){
    int i = 0;
    tNo *pontEsquerda, *pontDireita;

    while(pai->pont[i] != filho){  // Encontra a posicao no vetor de ponteiros no pai que aponta para filho
                i = i + 1;
    }
    if(i > 0){      // Se i > 0, entao o no possui irmao a esquerda pois nao eh o primeiro noh no vetor pai
        pontEsquerda = pai->pont[i-1];
        if(pontEsquerda->cont > MINIMO_CHAVES){   // Verifica se o irmao a esquerda possui chave para emprestar
            insereChave(filho, pai->dado[i-1]);     // Insere a chave a esquerda do ponteiro p/ filho do noh pai em filho
            pai->dado[i-1] = pontEsquerda->dado[pontEsquerda->cont - 1];     // Tranfere a maior chave do noh a esquerda para noh pai, na posicao a esquerda do ponteiro para noh filho
            removeChave(pontEsquerda, pontEsquerda->dado[pontEsquerda->cont - 1]); // remove a maior chave do noh a esquerda que foi transferida para o noh pai
        }
    }
    else{
        if(i < (pai->cont)){           // Checagem se filho possui irmao a direita
            pontDireita = pai->pont[i+1];
            if(pontDireita->cont > MINIMO_CHAVES){    // Verifica se o irmao a direita possui chave para emprestar
                insereChave(filho, pai->dado[i+1]);     // Insere a chave a direita do ponteiro p/ filho do noh pai em filho
                pai->dado[i+1] = pontDireita->dado[0];     // Tranfere a menor chave do noh a direita para noh pai, na posicao a direita do ponteiro para noh filho
                removeChave(pontDireita, pontDireita->dado[0]); // remove a menor chave do noh a direita que foi transferida para o noh pai
            }
        }
        else{
            facaFusao(pai, filho);       // Faz fusao caso emprestimo nao seja possivel
        }
    }
}

/*
REMOVE_PONTEIRO
    DESCRIÇÃO:  Remove ponteiro apos a remocao de um dado em um noh interno
    ENTRADA:    Ponteiro para tNo no e ponteiro para tNo remover
*/
void removePonteiro(tNo *no, tNo *remover){
    int i = 0;
    while(no->pont[i] != remover){
        i = i + 1;
    }
    for(i; i <= no->cont; i ++){
        no->pont[i] = no->pont[i+1];        // Sobrepoe o ponteiro a ser removido e ajusta os demais colocando-os uma posicao a esquerda
    }
}

/*
FACA_FUSAO
    DESCRIÇÃO:  Fusao de dois nos da arvore
    ENTRADA:    Ponteiro para tNo pai e ponteiro para tNo filho no qual ocorreu a subutilizacao
*/
void facaFusao(tNo *pai, tNo *filho){
    tNo *nodu, *noVazio;
    tDado dado;
    int i;

    while(pai->pont[i] != filho){   // Encontra a posicao do ponteiro no noh pai que aponta para filho
                i = i + 1;
    }
    if(i > 0){                      // Se i > 0, entao o no possui irmao a esquerda pois nao eh o primeiro noh no vetor pai
        nodu = pai->pont[i-1];
        noVazio = filho;
        dado = pai->dado[i-1];      // dado recebe o valor do dado entre os ponteiros nodu e noVazio
    }
    else{                           // Caso nao possua irmao a esquerda, faz fusao com irmao a direita
        nodu = filho;
        noVazio = pai->pont[i+1];
        dado = pai->dado[i+1];      // dado recebe o valor da dado entre os ponteiros nodu e noVazio
    }
    insereChave(nodu, dado);
    nodu->pont[nodu->cont] = noVazio->pont[0];  // Copia o primeiro ponteiro de noVazio como ponteiro a direita de chave em nodu
    for(i = 0; i < (noVazio->cont - 1); i++){   // Copia todas as chaves de noVazio para nodu, com respectivos ponteiros à direita
        insereChave(nodu, noVazio->dado[i]);
        nodu->pont[nodu->cont] = noVazio->pont[i+1];
    }
    free(noVazio);
    removeChave(pai, dado);
    removePonteiro(pai, noVazio);   // remove o ponteiro de noh pai para noVazio
}