/* 
 * File:   estruturas.h
 * Author: thales
 *
 * Created on January 23, 2013, 9:53 PM
 */

#ifndef ESTRUTURAS_H
#define	ESTRUTURAS_H

#ifdef	__cplusplus
extern "C" {
#endif

        // Estrutura tCadastro contendo tPessoa, endereco, cargo e salario.
typedef struct {
    char nome[50];
    int idade, sexo;
    char endereco[80];
    char cargo[30];
    float salario;
}tCadastro;

/*
 * Metodos para manipular as estruturas
 */

        // Realizar o cadastro completo de um funcionario
void cadastrar(tCadastro *c) {
    printf("\tNome: ");
    gets(c->nome);
    do {
        printf("\tIdade: ");
        scanf("%d", &c->idade);
    } while (c->idade <= 0);
    do {
        printf("\tSexo (M = 0 | H = 1): ");
        scanf("%d", &c->sexo);
    } while (c->sexo < 0 || c->sexo > 1);
    getchar();
    printf("Endereco: ");
    gets(c->endereco);
    printf("Cargo: ");
    gets(c->cargo);
    printf("Salario: ");
    scanf("%f",&c->salario);
}

        // Exibir o cadastro completo do funcionario
void exibeCadastro(const tCadastro *c){
    printf("\nNome: %s\nIdade: %d\nSexo: %d", c->nome, c->idade, c->sexo);
    printf("\nEndereco: %s\nCargo: %s\nSalario: %.2f\n\n",c->endereco, c->cargo,c->salario);
}

        // Exibe todos os cadastro no bloco
void exibeTodosCadastro(const tCadastro *c, int numCadastrados){
    int i;
    for(i = 0; i < numCadastrados; i++ ){
        printf("\n...C.a.d.a.s.t.r.o...no:.%d...\n", i);
        exibeCadastro(&c[i]);    
    }
}

        // Inserir numero maximo de cadastros num bloco -- para testes apenas!
void cadastroAutomatico(tCadastro *c,int *numCadastrados, int tamanho){
    int i;
    for(i = *numCadastrados; i < tamanho; i++)
    {   
        strcpy(c[i].nome,"Cthulhu");
        c[i].idade = 1000;
        c[i].sexo = 1;
        strcpy(c[i].cargo,"Deus das Trevas");
        c[i].salario = 25000.00;
        strcpy(c[i].endereco,"Rlyeh");
        *numCadastrados = *numCadastrados + 1;
    }
}

#ifdef	__cplusplus
}
#endif

#endif	/* ESTRUTURAS_H */

