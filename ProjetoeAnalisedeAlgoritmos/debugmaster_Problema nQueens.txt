// nQueens.cpp : Defines the entry point for the console application.
//

#include "stdafx.h"
#define TAM 5

void nQueens(int, char[TAM][TAM], bool&);
void verificaeatualiza(char[TAM][TAM]);

int total = 0;
int iter = 0;

int main(int argc, _TCHAR* argv[])
{
    char m[TAM][TAM];
    bool PARE = false;
    
    for(int i=0; i< TAM; i++)
        for(int j=0; j< TAM; j++)
            m[i][j] = 'O';

    nQueens(0, m, PARE);
    printf("%d\n", total);
    printf("%d\n", iter);

	return 0;
}

void nQueens(int k, char m[TAM][TAM], bool &PARE)
{
    //função que controla o crescimento das linhas
    // se existir uma solução voltara uma matriz de Q e X, onde Q são as rainhas e X os espaços "proibidos"
    // senão existir solução irá retornar um tabuleiro vazio completo de 0

    iter++;

    if(k>=TAM)
        return;

    for(int j=0; j< TAM; j++)
    {
        if(m[k][j] == 'O')
        {
            m[k][j] = 'Q';
            verificaeatualiza(m);
            nQueens(k+1, m, PARE);
            m[k][j] = 'O';
            verificaeatualiza(m);
        }
    }
}

void verificaeatualiza(char m[TAM][TAM])
{

    bool aux = true;
    int queens= 0;

    for(int i=0; i< TAM; i++) //reinicia para atualizar
        for(int j=0; j< TAM; j++)
            if(m[i][j] != 'Q')
                m[i][j] = 'O';

    for(int i=0;i<TAM;i++)
        for(int j=0; j<TAM; j++)            
            if(m[i][j] == 'Q') // encontra a primeira rainha e preenche a matriz com os espaços que não podem ser utilizados
            {
                queens++;
                for(int k=0; k < TAM; k++)
                    if(m[i][k] == 'Q' && k != j)
                        aux = false;
                    else if(k != j)
                        m[i][k] = 'X';
                for(int k=0; k < TAM; k++)
                    if(m[k][j] == 'Q' && k != i)
                        aux = false;
                    else if (k != i)
                        m[k][j] = 'X';
                for(int k=1; k< TAM; k++){ // NÃO preenche com X as diagonais. Mas não afeta a integridade do algoritmo
                    if(i-k >= 0 && j-k >= 0 && m[i-k][j-k] == 'Q')
                        aux = false;
                    if(i-k >= 0 && j+k < TAM && m[i-k][j+k] == 'Q')
                        aux = false;
                }
            }

    if(queens == TAM && aux == true){ // se o tabuleiro está "correto" e o número de rainhas é a dimensão do tabuleiro
        for(int i=0; i< TAM; i++)
        {
            for(int j=0; j< TAM; j++)
                printf("%c ", m[i][j]);
            printf("\n");
        }
        printf("\n");
        total++;

    }

}