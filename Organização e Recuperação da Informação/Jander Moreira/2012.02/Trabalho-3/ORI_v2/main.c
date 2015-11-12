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

int main(int argc, char** argv) {
    
    int op;
    
    stat("/", &fi);
    int qtdMax = fi.st_blksize/sizeof(tCadastro);
    int posAtual = 0;
    tCadastro cadastro[qtdMax];
    tCadastro * aux;
    
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
                cadastrar(&cadastro[posAtual]);
                posAtual++;
                break;
                
                // Exibe todos os funcionarios cadastrados
            case 2:
                exibeTodosCadastro(cadastro, posAtual);
                break;
                
                // Preenche automatico ateh completar o bloco
            case 3:
                cadastroAutomatico(cadastro, &posAtual, qtdMax);
                break;
                
                // Escreve blocos no arquivo
            case 4:
                escreveArquivo(cadastro, posAtual);
                break;
                
                // Leitura do arquivo para memoria
            case 5:
                aux = leituraArquivo(qtdMax);
                exibeTodosCadastro(aux, 23);
                break;
        }
        
    } while(op != 0);
    
    
    
    return (EXIT_SUCCESS);
}

