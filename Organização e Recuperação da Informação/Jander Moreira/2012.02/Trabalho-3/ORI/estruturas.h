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

        // Estrutura tPessoa contendo nome, idade e sexo.
typedef struct {
  char nome[50];
  int idade, sexo;    
}tPessoa;
        // Estrutura tCadastro contendo tPessoa, endereco, cargo e salario.
typedef struct {
    tPessoa p;
    char endereco[80];
    char cargo[30];
    float salario;
}tCadastro;

        // Estrutura utilizada para definir o tamanho que sera escrito em cada bloco.
typedef struct {
    tCadastro *cad;
    int qtdMax;
    int qtdCadastrados;
    int tamanho;
} tBloco;

/*
 * Metodos para manipular as estruturas
 */

        // Cadastrar somente em tPessoa
void cadastraPessoa(tPessoa *p){
    printf("\tNome: ");
    gets(p->nome);
    do{
    printf("\tIdade: ");
    scanf("%d",&p->idade);
    }while(p->idade <= 0);
    do{        
        printf("\tSexo (M = 0 | H = 1): ");
        scanf("%d",&p->sexo);
    }while(p->sexo < 0 || p->sexo > 1);
}
        
        // Exibir somente o que ha em tPessoa
void exibePessoa(const tPessoa *p){
    printf("Nome: %s\nIdade: %d\nSexo: %d", p->nome, p->idade, p->sexo);
}

        // Realizar o cadastro completo de um funcionario
void cadastrar(tCadastro *c){
    cadastraPessoa(&c->p);
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
    exibePessoa(&c->p);
    printf("\nEndereco: %s\nCargo: %s\nSalario: %.2f\n\n",c->endereco, c->cargo,c->salario);
}

        // Exibe todos os cadastro no bloco
void exibeTodosCadastro(const tBloco *b, int numBloco){
    int i, j = numBloco;
    for(i = 0; i < b->qtdCadastrados; i++ ){
        printf("\n...C.a.d.a.s.t.r.o...no:.%d...\n", (i*numBloco)+1);
        exibeCadastro(&b->cad[i]);    
    }
}

        // Inicializar o bloco
void inicializaBloco(tBloco * bloco){
    stat("/", &fi);     // Pega o tamanho do cluster do sistema de arquivos
    bloco->tamanho = fi.st_blksize - sizeof(tBloco);
    bloco->qtdMax = bloco->tamanho/sizeof(tCadastro);
    bloco->cad = malloc(bloco->qtdMax * sizeof(tCadastro));
    printf("Bloco inicializado\n");
}

        // Cadastrar um funcionario no bloco
void cadastrarNoBloco(tBloco *bloco){
    cadastrar(&bloco->cad[bloco->qtdCadastrados]);    
    bloco->qtdCadastrados++;
}

        // Inserir numero maximo de cadastros num bloco -- para testes apenas!
void cadastroAutomatico(tBloco *b){
    int i;
    for(i = b->qtdCadastrados; i < b->qtdMax; i++)
    {   
        strcpy(b->cad[i].p.nome,"Cthulhu");
        b->cad[i].p.idade = 1000;
        b->cad[i].p.sexo = 1;
        strcpy(b->cad[i].cargo,"Deus das Trevas");
        b->cad[i].salario = 25000.00;
        strcpy(b->cad[i].endereco,"Rlyeh");
        b->qtdCadastrados++;
    }
}

void verificaBlocoCheio(tBloco *b){
    if(b->qtdCadastrados == b->qtdMax);
}

#ifdef	__cplusplus
}
#endif

#endif	/* ESTRUTURAS_H */

