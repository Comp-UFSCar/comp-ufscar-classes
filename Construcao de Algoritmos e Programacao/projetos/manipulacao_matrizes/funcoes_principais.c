/*
*  funcoes_principais.c
*  Implementacoes das funcoes do menu
*/

#include <stdio.h>
#include <stdlib.h>
#include <malloc.h>
#include <string.h>

int ** carregaArquivo(char caminho[], int *linhas, int *colunas){
    // Tentando abrir o stream de arquivo
    FILE *f = NULL;
    f = fopen(caminho, "r");
    if (!f){
        // Nao conseguiu abrir o arquivo pra leitura
        return NULL;
    }

    // os dois primeiros valores no arquivo sao respectivamente linhas e colunas
    fscanf(f, "%d", linhas);
    fscanf(f, "%d", colunas);
    
    // tenta alocar o espaco necessario
    int **matriz;
    matriz = (int **)alocaMatriz(*linhas, *colunas);
    if (!matriz){
        // nao foi possivel alocar
        return NULL;
    }

    // copia os valores do arquivo para a matriz
    int i,j;
    for (i=0;i<*linhas;i++){
        for(j=0;j<*colunas;j++){
            fscanf(f, "%d", &matriz[i][j]);
        }
    }

    fclose(f);
    return matriz;
}

int salvaArquivo(char caminho[], int **matriz, int linhas, int colunas){
    // Tentando abrir o stream de arquivo
    FILE *f = NULL;
    f = fopen(caminho, "w");
    if (!f){
            // Nao conseguiu abrir o arquivo pra escrita
            return 0;
    }
    // os dois primeiros valores no arquivo sao respectivamente linhas e colunas
    fprintf(f, "%d\n", linhas);
    fprintf(f, "%d\n", colunas);

    // copia os valores da matriz para o arquivo
    int i,j;
    for (i=0;i<linhas;i++){
        for(j=0;j<colunas;j++){
            fprintf(f, "%d\n", matriz[i][j]);
        }
    }
    
    fclose(f);

    // retirando o \n do caminho do arquivo
    char *quebra = strchr(caminho, '\n');
    if (quebra)
        *quebra = '\0';

    printf("\n%s escrito com sucesso.\n", caminho);
    return 1;
}

void inverter(int **matriz, int linhas, int colunas){
    int i,j;
    for(i=0;i<linhas;i++){
        for(j=0;j<colunas;j++){
            matriz[i][j] = 255 - matriz[i][j];
        }
    }
}

void limiarizar(int **matriz, int linhas, int colunas){
    printf("Digite o valor do limiar: ");
    int limiar;
    scanf("%d", &limiar);
    int i,j;
    for(i=0;i<linhas;i++){
        for(j=0;j<colunas;j++){
            if (matriz[i][j] > limiar){
                matriz[i][j] = 255;
            }
            else{
                matriz[i][j] = 0;
            }
        }
    }
}

int media3x3(int **matriz, int linhas, int colunas){
    // verifica se as dimensoes sao coerentes para o calculo de media
    if ( (linhas>1) && (colunas>1) ){
        int i,j;
        // tenta alocar uma nova matriz para receber a cópia
        int **matrizTemp = NULL;
        matrizTemp = (int **)alocaMatriz(linhas, colunas);
        if (!matrizTemp){
            printf("Nao foi possivel alocar");
            return 0;
        }
        // copia a matriz base para efetuar a media sem alterar os valores
        copiaMatriz(matriz, matrizTemp, linhas, colunas);
        for(i=1;i<linhas-1;i++){
            for(j=1;j<(colunas-1);j++){
                // media da celula com seus 8 vizinhos
                matriz[i][j] = media(matrizTemp, i, j);
            }
        }
        desalocaMatriz(matrizTemp, linhas);
        return 1;
    }
    else{
        printf("Nao eh possivel calcular a media 3x3");
        return 0;
    }
}

int mediana3x3(int **matriz, int linhas, int colunas){
    // verificia se as dimensoes sao coerentes para o calculo de mediana
    if (linhas>1 && colunas>1){
        int i,j;
        // tenta alocar uma nova matriz para receber a cópia
        int **matrizTemp = NULL;
        matrizTemp = (int **)alocaMatriz(linhas, colunas);
        if (!matrizTemp){
            printf("Nao foi possivel alocar");
            return 0;
        }
        // copia a matriz base para ordenar sem alterar os campos
        copiaMatriz(matriz, matrizTemp, linhas, colunas);
        for(i=1;i<linhas-1;i++){
            for(j=1;j<(colunas-1);j++){
                // mediana da celula com seus 8 vizinhos
                matriz[i][j] = mediana(matrizTemp, i, j);
            }
        }
        desalocaMatriz(matrizTemp, linhas);
        return 1;
    }
    else{
        printf("Nao eh possivel calcular a mediana 3x3");
        return 0;
    }
}

int nroBrancos(int **matriz, int linhas, int colunas){
    int i,j;
    int cont = 0;
    for(i=0;i<linhas;i++){
        for(j=0;j<colunas;j++){
            if (matriz[i][j] == 255)
                cont++;
        }
    }
    return cont;
}
