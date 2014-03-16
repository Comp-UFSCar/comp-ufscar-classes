/*
 * File:   main.c
 * Author: Raul Vieira Cioldin
 * ==============
 * Tecnicas de processamento de imagens em C,
 * manipulando matrizes.
 */

#include <stdio.h>
#include <stdlib.h>
#include <malloc.h>

int main(int argc, char** argv) {
    // verifica se um parametro foi passado, senao exibe a forma de uso
    if (!argv[1]){
        mostraUso();
        return 0;
    }

    // Procedimento que carrega os dados do arquivo em uma matriz
    int **matriz;
    int linhas, colunas;
    printf("Abrindo arquivo...\n");
    matriz = (int **)carregaArquivo(argv[1], &linhas, &colunas);
    if (!matriz){
        printf("ERRO: nao foi possivel abrir o arquivo %s\n", argv[1]);
        return 0;
    }
        
    // entrando no loop do menu
    int opcao;
    do{
        // limpa a tela se o clear estiver disponível
        system("clear");
        printf("Selecione uma opcao ou 0 para encerrar\n" \
                "[1] - Salvar\n" \
                "[2] - Limiarizar\n" \
                "[3] - Inverter\n" \
                "[4] - Media 3x3\n" \
                "[5] - Mediana 3x3\n" \
                "[6] - Numero de brancos\n" \
                "======> ");
        scanf("%d", &opcao);
        switch(opcao){
            case 0:
                break;
            case 1:
                printf("\nNome do novo arquivo (enter para sobrescrever): ");
                char caminho[20];
                // workaround do buffer sujo
                getchar();
                fgets(caminho, sizeof(caminho), stdin);
                if (caminho)
                    salvaArquivo(caminho, matriz, linhas, colunas);
                else
                    salvaArquivo(argv[1], matriz, linhas, colunas);
                teclaEnter();
                break;
            case 2:
                limiarizar(matriz, linhas, colunas);
                mostraMatriz(matriz, linhas, colunas);
                break;
            case 3:
                inverter(matriz, linhas, colunas);
                mostraMatriz(matriz, linhas, colunas);
                break;
            case 4:
                if (media3x3(matriz, linhas, colunas))
                    mostraMatriz(matriz, linhas, colunas);
                break;
            case 5:
                if (mediana3x3(matriz, linhas, colunas))
                    mostraMatriz(matriz, linhas, colunas);
                break;
            case 6:
                printf("\nNumero de brancos: %d\n", nroBrancos(matriz, linhas, colunas));
                // espera input do teclado
                teclaEnter();
                break;
            default:
                printf("Opcao nao reconhecida");
                teclaEnter();
                break;
        }
    }while(opcao != 0);

    // liberacao de memoria
    desalocaMatriz(matriz, linhas);
    
    return 0;
}