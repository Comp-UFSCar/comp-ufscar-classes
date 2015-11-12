/* 
 * File:   arquivo.h
 * Author: thales
 *
 * Created on January 24, 2013, 6:02 AM
 */

#ifndef ARQUIVO_H
#define	ARQUIVO_H

#include "estruturas.h"


#ifdef	__cplusplus
extern "C" {
#endif

    void criaArquivo(){
        FILE *arquivo;
        arquivo = fopen("bancoCliente.bd", "w");
        if(arquivo == 0)
            perror("\n Erro ao criar arquivo");
        else
            printf("\n Banco Inicializado com sucesso!");
        fclose(arquivo);
    }
    
    
    void escreveArquivo(tCadastro * c, int posAtual){
        FILE *arquivo;
        int i;
        stat("/", &fi);
        
        arquivo = fopen("bancoCliente.bd", "r+");
        if(arquivo == 0){
            printf("Banco Não Encontrado");
            criaArquivo();
        }
        
        arquivo = fopen("bancoCliente.bd", "r+");
        if(arquivo == 0)
            perror("Erro! Impossivel criar arquivo");
        else{
            // Escrever no arquivo
            fseek(arquivo, 0, SEEK_END);
            for(i = 0; i < posAtual; i++){
                fwrite(&c[i], sizeof(tCadastro), 1, arquivo);
            }
            char fimArquivo[fi.st_blksize-sizeof(tCadastro)*posAtual]; 
            fwrite(fimArquivo, sizeof(fimArquivo), 1, arquivo);
            
            fclose(arquivo);
            printf("Bloco inserido no arquivo");
        }
    }
    
    tCadastro * leituraArquivo(int qtdMax){
        FILE *arquivo;
        stat("/", &fi);
        
        long int posicao;
        tCadastro * c = malloc(sizeof(tCadastro)*qtdMax);
        
        arquivo = fopen("bancoCliente.bd", "r");
        if(!arquivo)
            perror("Erro de acesso ao arquivo...\n");
        else{
            fseek(arquivo, 0, SEEK_END);
            if(ftell(arquivo) == 0)
                printf("O cadastro esta vazio!");
            else
            {
                posicao = ftell(arquivo);
                rewind(arquivo);
                int tamanho =  posicao/fi.st_blksize;
                //auxiliar = malloc(tamanho * sizeof(tBloco));
                // Leitura do arquivo
                //fread(&auxiliar, fi.st_blksize, 1, arquivo);
                int i;
                for(i = 0; i < qtdMax; i++)
                {
                    fread(&c[i], sizeof(tCadastro), 1, arquivo);
                }
            }
            fclose(arquivo);
            return c;
        }
    }
    

#ifdef	__cplusplus
}
#endif

#endif	/* ARQUIVO_H */

