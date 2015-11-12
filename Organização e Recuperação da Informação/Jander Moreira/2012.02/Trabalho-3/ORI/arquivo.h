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
    
    
    void escreveArquivo(tBloco * b){
        FILE *arquivo;
        int i;
    
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
            fwrite(b, fi.st_blksize, 1, arquivo);
            fclose(arquivo);
            printf("Bloco inserido no arquivo");
        }
    }

    tBloco * leituraArquivo(){
        FILE *arquivo;
        tBloco *auxiliar;
        
        long int posicao;
        
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
                auxiliar = malloc(tamanho * sizeof(tBloco));
                int i;
                for( i = 0; i < tamanho; i++)
                    inicializaBloco(&auxiliar[i]);
                
                // Leitura do arquivo
                i = 0;
                while(ftell(arquivo) < posicao)
                {
                    fread(&auxiliar[i], fi.st_blksize, 1, arquivo);
                }
            }
            fclose(arquivo);
            
        }
        return auxiliar;
    }
    

#ifdef	__cplusplus
}
#endif

#endif	/* ARQUIVO_H */

