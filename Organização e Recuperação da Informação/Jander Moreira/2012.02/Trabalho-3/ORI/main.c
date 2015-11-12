/* 
 * File:   main.c
 * Author: thales
 *
 * Created on January 23, 2013, 7:28 PM
 */

#include "tamanhoBloco.h"
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <malloc.h>
#include "estruturas.h"
#include "arquivo.h"

#define MAXSTR 40

int main(int argc, char** argv) {
    
    tBloco bloco;
    tBloco *auxiliar;
    int qtdBlocos = 1, op;
    
    inicializaBloco(&bloco);

//    inicializaBloco(&bloco[1]);
//    qtdBlocos = 2;
//    cadastroAutomatico(&bloco[ultimoBloco]);
//    exibeTodosCadastro(&bloco[ultimoBloco],ultimoBloco+1);
//    cadastrarNoBloco(&bloco);
    
    do { 
        printf("\t...M.e.n.u...\n");
        printf("\t| (1) Cadastrar funcionario\n");
        printf("\t| (2) Exibir funcionarios cadastrados\n");
        printf("\t| (3) Preencher cadastros restantes no bloco\n");
        printf("\t| (4) Escrever arquivo\n");
        printf("\t| (5) Ler arquivo\n");
        printf("\t| (0) Sair\n\t: ");
        scanf("%d",&op);
        getchar();
        
        switch(op){
            case 0:
                break;
                
                // Cadastrar funcionario
            case 1:
                cadastrarNoBloco(&bloco);
                break;
                
                // Exibe todos os funcionarios cadastrados
            case 2:
                exibeTodosCadastro(&bloco, qtdBlocos);
                break;
                
                // Preenche automatico ateh completar o bloco
            case 3:
                cadastroAutomatico(&bloco);
                break;
                
                // Escreve blocos no arquivo
            case 4:
                escreveArquivo(&bloco);
                break;
                
                // Leitura do arquivo para memoria
            case 5:
                auxiliar = leituraArquivo();
                //auxiliar = malloc(1 * sizeof(tBloco));
                break;
        }
        
    } while(op != 0);
    
    
    
    return (EXIT_SUCCESS);
}

