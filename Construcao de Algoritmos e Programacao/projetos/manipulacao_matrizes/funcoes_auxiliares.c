/*
* funcoes_auxiliares.c
* Conjunto de rotinas de conveniencia
*/

#include <malloc.h>
#include <stdio.h>

void mostraUso(){
    printf("==== Uso: ====\n\n" \
            "Arg: caminho do arquivo a ser carregado\n\n" \
            "Ex: trab_jander <arquivo.txt>\n");
}

void teclaEnter(){
    getchar();
    do{
        printf("Pressione <ENTER> para continuar");
    }while (10 != getchar());
}

int media(int **matriz, int i, int j){
    return (matriz[i][j] + matriz[i-1][j-1] + matriz[i-1][j] + matriz[i-1][j+1]\
            + matriz[i][j-1] + matriz[i][j+1] + matriz[i+1][j-1] + \
            matriz[i+1][j] + matriz[i+1][j+1])/9;
}

int mediana(int **matriz, int i, int j){
    // primeiramente colocar os 9 valores em um vetor
    int v[9];
    v[0] = matriz[i][j];
    v[1] = matriz[i-1][j-1];
    v[2] = matriz[i-1][j];
    v[3] = matriz[i-1][j+1];
    v[4] = matriz[i][j-1];
    v[5] = matriz[i][j+1];
    v[6] = matriz[i+1][j-1];
    v[7] = matriz[i+1][j];
    v[8] = matriz[i+1][j+1];
    // agora ordenar o vetor
    int temp,k;
    for(k=0;k<9;k++){
        if (v[k] > v[k+1]){
            temp = v[k+1];
            v[k+1] = v[k];
            v[k] = temp;
        }
    }
    // retorna a mediana
    return v[5];
}

int ** alocaMatriz(int linhas, int colunas){
    int i;
    int **matriz = malloc(linhas * sizeof (int*));
    matriz[0] = malloc(linhas * colunas * sizeof (int));
    for (i = 1; i < linhas; i++)
        matriz[i] = &matriz[0][i * colunas];

    return matriz;
}

void copiaMatriz(int **matrizBase, int **matrizCopia, int linhas, int colunas){
    int i,j;
    for(i=0;i<linhas;i++){
        for(j=0;j<colunas;j++){
            matrizCopia[i][j] = matrizBase[i][j];
        }
    }
}

void desalocaMatriz(int **matriz, int linhas){
    free(matriz[0]);
    free(matriz);
}

void mostraMatriz(int **matriz, int linhas, int colunas){
    int i,j;
    for(i=0;i<linhas;i++){
        for(j=0;j<colunas;j++){
            printf("%5d  ", matriz[i][j]);
        }
        printf("\n");
    }
    // espera um input do teclado
    teclaEnter();
}
