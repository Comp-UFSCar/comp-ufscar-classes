#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <string.h>
#include <malloc.h>

/*
    SISTEMA BANCARIO
    
    Membros da equipe:
           
           Cristiano de Oliveira Faustino      RA: 407631
           Rafael Dassi Kiyoto                 RA: 407704
           Guilherme Silva Neres               RA: 407674
           
    
    Objetivo:
    
    O objetivo do projeto era realizar um programa que simulasse operações de um sistema de internet banking
    com suas operacoes de saque, deposito e transferencia, bem como extrato e demais possibilidades
    (como a criação e exclusao de conta, funcoes ja implementadas).
    
    O projeto tambem tinha como objetivo exercitar o aprendizado da lógica de programação, desenvolvendo funcoes
    proprias e utilizando-se da manipulacao de arquivos para armazenar os dados.
    
    Tarefas desenvolvidas:
            
            Rafael Kiyoto:
                   
                   Desenvolveu todas as operacoes de menu, como as listagens de opcoes e selecao das mesmas,
                   todas as funcoes de menu passaram por seu desenvolvimento e tambem foi determinado qual
                   procedimento seria chamado a partir daí.
            
            Cristiano Faustino:
                      
                   Desenvolveu toda a manipulacao de arquivos, como gravacao, leitura, atualizacao e preenchidmento dos mesmos
                   tambem desenvolveu as funcoes de captura de dados,criacao e exclusao de conta, saque, transferencia e deposito, 
                   bem como auxiliou os demais membros da equipe para que conseguissem desenvolver a lógica do sistema.
                   
            Guilherme Neres
             
                   Desenvolveu as funcoes de alteracao de senha, falar com o gerente, e o bloqueio e desbloqueio de senhas
                   
    
    Dificuldades encontradas
    
           Foram encontradas dificuldades na elaboracao dos procedimentos de transferencia e tambem na atualizacao dos arquivos,
           na base do programa foram encontradas dificuldades na conclusao de como seria elaborado o sistema.
           
    Observacoes
      
           O sistema foi elaborado em arquivo texto para visualizacao dos dados salvos (verificar no proprio arquivo)
           e tambem para utilizar-se da funcao captura, elaborada na ultima mini-avaliacao.
           
           Para testar, eh necessaria a criacao do arquivo usuarios.txt e colocar os seguintes dados na primeira linha (sem aspas):
                
                "1001;1001;459;2"
           
           Esses dados servem para a verificacao de senha do gerente, que é pedida na criacao e exclusao de conta, 
           e tambem no desbloqueio da senha.
*/

// Struct para guardar os dados do usuario logado ou de outros usuarios
typedef struct dados_usuario
{
    int agencia, cc,senha,bloqueado;
    char nome[50];
    float saldo_poup,saldo_cc,limite;
}dados_usuario;

// Struct para guardar dados de movimentacao nas contas do usuario
typedef struct movimentacao
{
    char data[30],tipo_op[6],ref[100];
    float valor;
}movimentacao;

/*Prototipos das funcoes utilizadas */

// Opcoes iniciais
void menu_principal(int *terminal);
void logar(int *terminal,int erros,int agencia, int cc);
void gerente(int *terminal);
void exibe_criar_conta(int *terminal);
void excluir_conta(int *terminal);
void sair();

// Opcoes do usuario
void menu_usuario(int *terminal,dados_usuario *usuario);
void menu_extrato(int *terminal,dados_usuario *usuario);
void menu_transferencia(int *terminal,dados_usuario *usuario);
void menu_saque(int *terminal,dados_usuario *usuario);
void menu_deposito(int *terminal,dados_usuario *usuario);
void exibe_alterar_senha(int *terminal,dados_usuario *usuario);
void saldo_limite(int *terminal,dados_usuario *usuario);

// Funcoes que vao retornar valores
char *captura(char buffer[200],int pos);
int verifica_login(int agencia, int cc, int senha);
int desbloquear_senha(int *terminal,int agencia,int cc);
int verifica_saldo_limite(char de_conta,dados_usuario *usuario,float valor);

//Procedimentos do sistema
void criar_conta(char nome[50],int agencia,int cc,int senha);
void preenche_dados_usuario(int *terminal,dados_usuario *usuario,int agencia,int cc,int pos);
void atualiza_senha(int *terminal,dados_usuario *usuario,int senha_nova);
void bloquear_senha(int *terminal,int agencia,int cc);
void deposito(char para_conta,int *terminal,dados_usuario *usuario,float valor,int erros);
void saque(char de_conta,int *terminal,dados_usuario *usuario,float valor,int erros);
void transferencia(int opc,int *terminal,dados_usuario *usuario,float valor,int erros);
void extrato_cc(int *terminal,dados_usuario *usuario);
void extrato_poup(int *terminal,dados_usuario *usuario);

// Procedimentos que atualizam dados nos arquivos
void atualiza_usuarios(int *terminal,dados_usuario *usuario);
void atualiza_conta(char conta,int *terminal,dados_usuario *usuario);
void insere_mov(char para_conta,char tipo[4],int *terminal,dados_usuario *usuario,float valor,char ref[100]);

/* Fim dos prototipos das funÃƒÂ§ÃƒÂµes */


int main()
{
    /* A funcao principal apenas sorteia o numero do terminal que sera utilizado
       e posteriormente chama o procedimento que exibe o menu principal */
    int terminal;
    srand(time(NULL));
    terminal = rand()%1000;
    menu_principal(&terminal);
    return 0;
}

char *captura(char buffer[200],int pos)
{
    /*Funcao utilizada para pegar corretamente os dados de uma string separada por ponto e virgula
      que eh o formato em que estao salvos os arquivos do sistema*/

    // Variavel que verifica o num de ponto e virgulas
    int ponto_virgula = 0;

    /* Variaveis que manipulam a posicao das strings, auxiliar_pv manipula a posicao do ponto e virgula anterio
       (quando for entre o 1 e o 2, auxiliar_pv valera 1)
    */
    int cont = 0,auxiliar_pv;
    int cont2 = 0;

    // Variavel que retornara
    static char retorno[150];

    // auxiliar_pv recebe qual ponto e virgula ele deve procurar, 1 - primeiro, 2 - segundo ...
    auxiliar_pv = pos - 1;

    // Enquanto nao tiver chego no ponto e virgula que termina a string desejada e enquanto nao for o fim da string buffer
    while ((ponto_virgula < pos)&&(buffer[cont] != '\0'))
    {

        // se auxiliar_pv for 0, quer dizer que eh entre o comeco da string e o primeiro ponto e virgula
        // senao, eh entre 2 ponto e virgulas, ou entre 1 ponto e virgula e o fim da string buffer
        if (auxiliar_pv == 0)
        {
            // Se nao for ponto e virgula, armazena na string de retorno
            if (buffer[cont] != ';')
            {
                retorno[cont2] = buffer[cont];
                cont++;
                cont2++;
            }
            else
            {
                // Se for ponto e virgula, acrescenta o numero e sai da estrutura de repeticao
                // (pois para auxiliar_pv = 0 pos deve ser 1)
                ponto_virgula++;
            }
        }
        else
        {
            // Enquanto nao achar o ponto e virgula que corresponde a auxiliar_pos e enquanto nao for o fim da string buffer
            // (ou o ponto e virgula antes da string desejada)
            while ((ponto_virgula < auxiliar_pv)&&(buffer[cont] != '\0'))
            {
                // Se nao for ponto e virgula apenas anda a variavel buffer (pelo cont), senao aumenta o numero do ponto_virgula
                if (buffer[cont] == ';')
                {
                    ponto_virgula++;
                }
                cont++;

            }

            // Se nao for ponto e virgula, armazena na string de retorno
            if (buffer[cont] != ';')
            {
                retorno[cont2] = buffer[cont];
                cont++;
                cont2++;
            }
            else
            {
                // Se for ponto e virgula, acrescenta o numero e sai da estrutura de repeticao
                // (pois no while anterior ja chegou ao auxiliar_pv e pos = auxiliar_pv + 1)
                ponto_virgula++;
            }

        }
    }

    // Determina a posicao do final da string de retorno
    retorno[cont2] = '\0';

    // Retorna string
    return retorno;

}

int verifica_login(int agencia, int cc, int senha)
{
    // Variaveis utilizadas para as validacoes
    int correta = 0,bloqueada = 0,encontrou = 0;


    FILE *arq_usuarios;

    // Utilizado para guardar dados do arquivo
    char buffer[200];

    // Abre arquivo
    arq_usuarios = fopen("usuarios.txt","r");

    // Se conseguir abrir arquivo
    if (arq_usuarios!=NULL)
    {
        // Enquanto nao for o fim do arquivo e nao encontrou o usuario desejado
        while ((!feof(arq_usuarios)) && (encontrou == 0))
        {
            fgets(buffer,200,arq_usuarios);

            /* Verifica se ha aquela agencia e aquela cc cadastrada
               E tambem impede que tentem logar na conta de gerente
              (Onde o ultimo parametro de cada linha do arquivo corresponde a: 1 - Senha Bloqueada, 2 - Gerente, 0 - Senha ativa)
            */
            if ((atoi(captura(buffer,1))==agencia) &&(atoi(captura(buffer,2))==cc) && (atoi(captura(buffer,4))!=2))
            {
                // Quer dizer que encontrou a conta
                encontrou = 1;

                // verifica se a senha esta correta
                if (atoi(captura(buffer,3))==senha)
                    correta = 1;

                // Verifica se a conta esta bloqueada (1 - Bloqueada, 0 - Desbloqueada, 2 - Gerente)
                if (atoi(captura(buffer,4))==1)
                    bloqueada = 1;

            }
        }

        // Fecha arquivo
        fclose(arq_usuarios);

        /*
          Se for 0 nao encontrou login
          Se for 1 o login e senha esta correto e a senha nao esta bloqueada
          Se for 2 a senha esta incorreta
          Se for 3 a senha esta bloqueada
          Se for 4, eh erro do sistema
        */
        if (encontrou == 0)
            return 0;
        else if (bloqueada == 1)
            return 3;
        else if (correta == 0)
            return 2;
        else
            return 1;
    }
    else
        return 4;
}


void sair()
{
    // Procedimento utilizado apenas para sair do sistema do banco
    printf("Obrigado por acessar nosso banco!\n");
    system("pause");
}

void erro()
{
    // Procedimento utilizado para exibir mensagem de erro quando nao abrir um arquivo ou nao conseguir alocar um vetor
    printf("Erro no sistema!\n");
    system("pause");
}

void menu_principal(int *terminal)
{
    // Procedimento para exibir o menu principal e suas opcoes

    // Variavel para armazenar opcoes escolhidas pelo usuario
    int opc;

    // Limpa a tela
    system("cls");

    // Opcoes do menu e cabecalho
    printf("############### BANCO KNC - O SEU BANCO DE SUCESSO ###############\n\n");
    printf("Menu Principal - Terminal %d\n",*terminal);
    printf("1 - Efetuar Login\n");
    printf("2 - Falar com o Gerente\n");
    printf("3 - Criar usuario\n");
    printf("4 - Excluir usuario\n");
    printf("5 - Sair\n");

    // Instancia variavel de opcoes como zero
    opc = 0;

    // Enquanto o usuario nao escolher uma opcao valida o sistema continua pedindo
    while ((opc<1) || (opc>5))
    {
        printf("\nDigite a opcao desejada: ");
        scanf("%d",&opc);

        // Se opcao for invalida, exibe um aviso
        if ((opc<1) || (opc>5))
            printf("\nOpcao invalida!");
    }

    /*
       Se opcao for 1, vai para o procedimento de login do usuario
       Se opcao for 2, vai para o procedimento de falar com o gerente
       Se opcao for 3, vai para o procedimento de criar conta
       Se opcao for 4, vai para o procedimento de excluir conta
       Se for 5, sai do sistema
    */
    if (opc == 1)
        logar(terminal,0,0,0);
    else if (opc == 2)
        gerente(terminal);
    else if (opc == 3)
        exibe_criar_conta(terminal);
    else if (opc == 4)
        excluir_conta(terminal);
    else
        sair();

}

void logar(int *terminal,int erros,int agencia, int cc)
{
    /* Esse procedimento eh utilizado para pedir dados do cliente,
       para poder logar corretamente em sua conta. */

    // Variaveis agencia, conta corrente e senha, que serao utilizadas
    int senha;

    // Variavel para armazenar opcoes escolhidas pelo usuario
    int opc;

    // Limpa a tela
    system("cls");

    // Exibe cabecalho
    printf("############### BANCO KNC - O SEU BANCO DE SUCESSO ###############\n\n");
    printf("Efetuar Login - Terminal %d\n",*terminal);

    /* Se for a primeira tentativa ele pedira agencia e cc
       Senao ele utilizara os dados que o usuario ja digitou e pedira somente a senha
    */
    if ((agencia == 0) && (cc == 0))
    {
        printf("Digite o numero da Agencia: ");
        scanf("%d",&agencia);
        printf("\nDigite o numero da Conta Corrente: ");
        scanf("%d",&cc);
    }

    // Pede a senha
    printf("\nDigite a senha: ");
    scanf("%d",&senha);

    /*
       Utiliza a funcao verifica_login para que verifique se os dados do usuario estao corretos

       Retorna os seguintes valores:

       Se for 1 o login existe e esta correto
       Se for 2 a senha estava incorreta
       Se for 3 a senha esta bloqueada
       Se for 4 deu erro no sistema
       Se for 0 o Login nao foi encontrado

    */
    if (verifica_login(agencia,cc,senha)==1)
    {
        /* Se o login estiver correto, vai para o menu do usuario
           passando os parametros necessarios de agencia e conta corrente
           bem como o terminal.
        */
        dados_usuario usuario[1];

        preenche_dados_usuario(terminal,usuario,agencia,cc,0);
        menu_usuario(terminal,usuario);
    }
    else if (verifica_login(agencia,cc,senha)==2)
    {
        // Exibe mensagem de que a senha esta incorreta
        printf("\nSenha incorreta!\n");

        // Assume valor 0 para a variavel de opcao
        opc = 0;

        // Aumenta o numero de erros nas tentativas do usuario, pois ele errou a senha
        erros++;

        /*
         Se o numero de erros nas tentativas for menor que 3, permite tentar novamente
         Senao ele executa o metodo para bloqueio de senha
        */
        if (erros < 3)
        {
            // Enquanto nao selecionar uma opcao valida, pede novamente
            while ((opc <1) ||(opc>2))
            {
                printf("Deseja tentar logar novamente? (1 - S/ 2 - N): ");
                scanf("%d",&opc);

                // Exibe mensagem se a opcao for invalida
                if ((opc <1) ||(opc>2))
                    printf("\nOpcao invalida!\n");
            }

            /*
              Se ele desejar logar novamente, chama o proprio metodo de logar
              passando o parametro de "erros" como o valor ja acrescido
              e com os parametros de agencia e conta ja preenchidos.
              Se nao deseja tentar novamente, volta ao menu principal
            */
            if (opc == 1)
                logar(terminal,erros,agencia,cc);
            else
                menu_principal(terminal);
        }
        else
        {
            // Exibe mensagem que a senha foi bloqueada
            printf("A senha foi errada 3 vezes e sera bloqueada.\n");

            // Bloqueia a senha
            bloquear_senha(terminal,agencia,cc);

            // Codigo utilizado para o usuario poder ler a mensagem antes do sistema voltar ao menu principal
            system("pause");

            // Volta ao menu principal
            menu_principal(terminal);
        }
    }
    else if (verifica_login(agencia,cc,senha)==3)
    {
        // Exbe mensagem de que senha esta bloqueada
        printf("\nSenha esta bloqueada, favor contactar o gerente.\n");

        // Assume valor 0 para a variavel de opcao
        opc = 0;

        // Enquanto nao selecionar uma opcao valida, pede novamente
        while ((opc <1) ||(opc>2))
        {
            printf("Deseja tentar logar novamente? (1 - S/ 2 - N): ");
            scanf("%d",&opc);

            // Exibe mensagem se a opcao for invalida
            if ((opc <1) ||(opc>2))
                printf("\nOpcao invalida!\n");
        }

        /*
              Se ele desejar logar novamente, chama o proprio metodo de logar
              passando o parametro de "erros" como zero
              e com os parametros de agencia e conta tambem zerados.
              Se nao deseja tentar novamente, volta ao menu principal
        */
        if (opc == 1)
            logar(terminal,0,0,0);
        else
            menu_principal(terminal);
    }
    else if (verifica_login(agencia,cc,senha)==4)
    {
        printf("Erro no sistema!\n");

        // Assume valor 0 para a variavel de opcao
        opc = 0;

        // Enquanto nao selecionar uma opcao valida, pede novamente
        while ((opc <1) ||(opc>2))
        {
            printf("Deseja tentar logar novamente? (1 - S/ 2 - N): ");
            scanf("%d",&opc);

            // Exibe mensagem se a opcao for invalida
            if ((opc <1) ||(opc>2))
                printf("\nOpcao invalida!\n");
        }

        /*
              Se ele desejar logar novamente, chama o proprio metodo de logar
              passando o parametro de "erros" como zero
              e com os parametros de agencia e conta tambem zerados.
              Se nao deseja tentar novamente, volta ao menu principal
        */
        if (opc == 1)
            logar(terminal,0,0,0);
        else
            menu_principal(terminal);
    }
    else
    {
        // Exbe mensagem de que nao encontrou a conta
        printf("\nConta nao encontrada!\n");

        // Assume valor 0 para a variavel de opcao
        opc = 0;

        // Enquanto nao selecionar uma opcao valida, pede novamente
        while ((opc <1) ||(opc>2))
        {
            printf("Deseja tentar logar novamente? (1 - S/ 2 - N): ");
            scanf("%d",&opc);

            // Exibe mensagem se a opcao for invalida
            if ((opc <1) ||(opc>2))
                printf("\nOpcao invalida!\n");
        }

        /*
              Se ele desejar logar novamente, chama o proprio metodo de logar
              passando o parametro de "erros" como zero
              e com os parametros de agencia e conta tambem zerados.
              Se nao deseja tentar novamente, volta ao menu principal
        */
        if (opc == 1)
            logar(terminal,0,0,0);
        else
            menu_principal(terminal);
    }

}

void gerente(int *terminal)
{
    // Variaveis de agencia e conta
    int agencia,cc;

    // Variavel para armazenar opcoes escolhidas pelo usuario
    int opc;

    // Limpa a tela
    system("cls");

    // Exibe cabecalho e pede dados do usuario
    printf("############### BANCO KNC - O SEU BANCO DE SUCESSO ###############\n\n");
    printf("Contato com o Gerente - Terminal %d\n",*terminal);

    // Variaveis Manipulacao de permisssao para gerente
    int senha_arq,senha_gerente,eh_gerente =0;
    FILE *arq_usuarios;

    // Variavel para armazenar dados do arquivo
    char buffer[200];

    // abre arquivo
    arq_usuarios = fopen("usuarios.txt","r");

    if (arq_usuarios != NULL)
    {
        // Enquanto nao achou o login de gerente e nao chegou ao fim do arquivo
        while ((eh_gerente == 0)&&(!feof(arq_usuarios)))
        {
            // Pega dados do arquivo e armazena na variavel buffer
            fgets(buffer,200,arq_usuarios);

            // Se for usuario do gerente (agencia = 1001 e cc = 1001
            // dai pega a senha cadastrada para gerente
            if ((strcmp(captura(buffer,1),"1001")==0) && (strcmp(captura(buffer,2),"1001")==0))
            {

                senha_arq = atoi(captura(buffer,3));

                // Sai da estutura de repeticao com essa alteracao
                eh_gerente = 1;
            }
        }

        // Fecha arquivo
        fclose(arq_usuarios);

        // Pede senha do gerente
        printf("\nDigite a senha para acesso a criacao de conta (Somente Gerente):");
        scanf("%d",&senha_gerente);

        // Se ele obtiver a senha de gerente
        if (senha_gerente == senha_arq)
        {

            // Pede dados da conta a ser desbloqueada
            printf("Digite o numero da Agencia: ");
            scanf("%d",&agencia);
            printf("\nDigite o numero da Conta Corrente: ");
            scanf("%d",&cc);

            // Procedimento de desbloqueio de senha
            /*
               Se for 1 ele achou e desbloqueou
               Se for 2 ele achou mas nao estava bloqueada
               Senao a conta nao existe
            */
            if (desbloquear_senha(terminal,agencia,cc)==1)
            {
                // Exibe mensagem de que a conta foi desbloqueada
                printf("Senha desbloqueada com sucesso!\n");

                // Assume valor 0 para a variavel de opcao
                opc = 0;

                // Enquanto nao selecionar uma opcao valida, pede novamente
                while ((opc <1) ||(opc>2))
                {
                    printf("Deseja voltar ao menu principal? (1 - S/ 2 - N): ");
                    scanf("%d",&opc);

                    // Exibe mensagem se a opcao for invalida
                    if ((opc <1) ||(opc>2))
                        printf("\nOpcao invalida!\n");
                }

                /*
                      Se deseja voltar ao menu principal, volta a ele
                      Se nao deseja, sai do sistema
                */
                if (opc == 1)
                    menu_principal(terminal);
                else
                    sair();

            }
            else if (desbloquear_senha(terminal,agencia,cc)==2)
            {
                // Exibe mensagem de que a conta nao estava bloqueada
                printf("Conta nao estava bloqueada!\n");

                // Assume valor 0 para a variavel de opcao
                opc = 0;

                // Enquanto nao selecionar uma opcao valida, pede novamente
                while ((opc <1) ||(opc>2))
                {
                    printf("Deseja falar com o gerente novamente? (1 - S/ 2 - N): ");
                    scanf("%d",&opc);

                    // Exibe mensagem se a opcao for invalida
                    if ((opc <1) ||(opc>2))
                        printf("\nOpcao invalida!\n");
                }

                /*
                      Se deseja tentar novamente, chama novamente o procedimento de falar com o gerente
                      Se nao deseja tentar novamente, volta ao menu principal
                */
                if (opc == 1)
                    gerente(terminal);
                else
                    menu_principal(terminal);


            }
            else
            {
                // Exibe mensagem de que a conta nao foi encontrada
                printf("Conta nao encontrada!\n");

                // Assume valor 0 para a variavel de opcao
                opc = 0;

                // Enquanto nao selecionar uma opcao valida, pede novamente
                while ((opc <1) ||(opc>2))
                {
                    printf("Deseja tentar falar novamente com o gerente? (1 - S/ 2 - N): ");
                    scanf("%d",&opc);

                    // Exibe mensagem se a opcao for invalida
                    if ((opc <1) ||(opc>2))
                        printf("\nOpcao invalida!\n");
                }

                /*
                      Se deseja tentar novamente, chama novamente o procedimento de falar com o gerente
                      Se nao deseja tentar novamente, volta ao menu principal
                */
                if (opc == 1)
                    gerente(terminal);
                else
                    menu_principal(terminal);
            }
        }
        else
        {
            // Se errou a senha de gerente, exibe essa mensagem
            printf("Acesso negado, senha incorreta!\n");
            system("pause");
            menu_principal(terminal);
        }
    }
    else
    {
        erro();
        menu_principal(terminal);
    }
}

void exibe_criar_conta(int *terminal)
{
    // Procedimento para criar uma conta de usuario

    // Limpa a tela
    system("cls");

    // Cabecalho e funcoes do menu
    printf("############### BANCO KNC - O SEU BANCO DE SUCESSO ###############\n\n");
    printf("Criar Conta - Terminal %d\n",*terminal);

    // Variaveis para manipular o acesso do gerente
    int senha_arq,senha_gerente,eh_gerente =0;

    FILE *arq_usuarios;

    // Variavel para armazenar dados do arquivo
    char buffer[200];

    // Abre arquivo para leitura
    arq_usuarios = fopen("usuarios.txt","r");

    // Se conseguiu abrir arquivo
    if (arq_usuarios != NULL)
    {
        // Enquanto nao achar o login do gerente e nao for o fim do arquivo
        while ((eh_gerente == 0)&&(!feof(arq_usuarios)))
        {
            // Pega dados de uma linha do arquivo
            fgets(buffer,200,arq_usuarios);

            // Se for usuario do gerente (agencia = 1001 e cc = 1001)
            // dai pega a senha cadastrada para gerente
            if ((strcmp(captura(buffer,1),"1001")==0) && (strcmp(captura(buffer,2),"1001")==0))
            {

                senha_arq = atoi(captura(buffer,3));

                // Aqui faz sair da estrutura de repeticao
                eh_gerente = 1;
            }
        }

        // Fecha arquivo
        fclose(arq_usuarios);

        // Pede senha do gerente
        printf("\nDigite a senha para acesso a criacao de conta (Somente Gerente):");
        scanf("%d",&senha_gerente);

        // Se a senha estiver correta
        if (senha_gerente == senha_arq)
        {
            // Inicializa variavel de opcoes
            int opc = 0;

            // Variaveis para armazenar Dados do novo cliente
            char nome[50];
            int agencia, cc,senha;

            // Pede dados do novo cliente
            printf("Digite o Nome:");
            fflush(stdin);
            gets(nome);
            printf("\nDigite o numero da Agencia:");
            scanf("%d",&agencia);
            printf("\nDigite o numero da Conta Corrente:");
            scanf("%d",&cc);
            printf("\nDigite a senha do usuario:");
            scanf("%d",&senha);

            // Procedimento para criar conta
            criar_conta(nome,agencia,cc,senha);

            // Enquanto nao selecionar uma opcao valida, pede novamente
            while ((opc <1) ||(opc>2))
            {
                printf("Deseja voltar ao menu principal? (1 - S/ 2 - N): ");
                scanf("%d",&opc);

                // Exibe mensagem se a opcao for invalida
                if ((opc <1) ||(opc>2))
                    printf("\nOpcao invalida!\n");
            }

            /*
                  Se deseja voltar ao menu principal, volta a ele
                  Se nao deseja, sai do sistema
            */
            if (opc == 1)
                menu_principal(terminal);
            else
                sair();

        }
        else
        {
            // Se errar a senha do gerente, exibe essa mensagem
            printf("Acesso negado, senha incorreta!\n");
            system("pause");
            menu_principal(terminal);
        }
    }
    else
    {
        erro();
        menu_principal(terminal);
    }
}

void excluir_conta(int *terminal)
{
    // Procedimento para excluir uma conta de usuario

    // Limpa a tela
    system("cls");

    // Cabecalho e funcoes do menu
    printf("############### BANCO KNC - O SEU BANCO DE SUCESSO ###############\n\n");
    printf("Excluir Conta - Terminal %d\n",*terminal);

    FILE *arq_usuarios;

    // Variaveis para manipular conta a ser excluida, e senha do gerente, bem como o numero de elementos no arquivo
    int eh_gerente = 0,agencia_ex,cc_ex,senha_arq,senha_gerente,elementos = 0,pos = 0;

    // Variavel utilizada para armazenar dados sobre o nome do arquivo e seu conteudo
    char buffer[200],str_agencia[10],str_cc[10],nome_arquivo[50];

    dados_usuario *todos_usuarios;

    // Abre arquivo
    arq_usuarios = fopen("usuarios.txt","r");

    if (arq_usuarios != NULL)
    {
        // enquanto nao achar os dados do gerente e nao for o fim do arquivo
        while ((eh_gerente == 0)&&(!feof(arq_usuarios)))
        {
            // Pega dados de uma linha do arquivo
            fgets(buffer,200,arq_usuarios);

            // Se for usuario do gerente (agencia = 1001 e cc = 1001
            // dai pega a senha cadastrada para gerente
            if ((strcmp(captura(buffer,1),"1001")==0) && (strcmp(captura(buffer,2),"1001")==0))
            {
                // Pega senha do gerente
                senha_arq = atoi(captura(buffer,3));
                eh_gerente = 1;
            }
        }

        // Fecha arquivo
        fclose(arq_usuarios);

        // Pede a senha do gerente
        printf("\nDigite a senha para acesso a criacao de conta (Somente Gerente):");
        scanf("%d",&senha_gerente);

        // Se a senha do gerente estiver certa, continua, senao exibe mensagem de acesso negado
        if (senha_gerente == senha_arq)
        {
            // Pede dados da conta a ser excluida
            printf("\nDigite a agencia da conta a ser excluida: ");
            scanf("%d",&agencia_ex);
            printf("\nDigite a conta a ser excluida: ");
            scanf("%d",&cc_ex);

            // Utiliza a verifica_login para ver se a conta existe, se existir e nao tiver dado erro no arquivo, continua
            if ((verifica_login(agencia_ex,cc_ex,0) != 0) &&(verifica_login(agencia_ex,cc_ex,0) != 4))
            {
                // Transforma variaveis agencia e cc em strings
                itoa(agencia_ex,str_agencia,10);
                itoa(cc_ex,str_cc,10);

                // Arruma o nome do arquivo de mov da ccorrente para remove-lo posteriormente
                strcpy(nome_arquivo,"mov_ccorrente_");
                strcat(nome_arquivo,str_agencia);
                strcat(nome_arquivo,"_");
                strcat(nome_arquivo,str_cc);
                strcat(nome_arquivo,".txt");
                remove(nome_arquivo);

                // Arruma o nome do arquivo de mov da poupanca para remove-lo posteriormente
                strcpy(nome_arquivo,"mov_poupanca_");
                strcat(nome_arquivo,str_agencia);
                strcat(nome_arquivo,"_");
                strcat(nome_arquivo,str_cc);
                strcat(nome_arquivo,".txt");
                remove(nome_arquivo);

                // Arruma o nome do arquivo dos dados do cliente para remove-lo posteriormente
                strcpy(nome_arquivo,"dados_");
                strcat(nome_arquivo,str_agencia);
                strcat(nome_arquivo,"_");
                strcat(nome_arquivo,str_cc);
                strcat(nome_arquivo,".txt");
                remove(nome_arquivo);

                // Abre esse arquivo para armazenar todos os dados, menos o que sera excluido
                arq_usuarios = fopen("usuarios.txt","r");

                if (arq_usuarios != NULL)
                {
                    // Esse while eh usado para a contagem de elementos para alocar dinamicamente o vetor de struct
                    while (!feof(arq_usuarios))
                    {

                        // Armazena linha do arquivo na variavel buffer
                        fgets(buffer,200,arq_usuarios);

                        // Aumenta o numero de elementos
                        elementos++;
                    }

                    // Volta o ponteiro do arquivo para o inicio
                    rewind(arq_usuarios);

                    // Aloca dinamicamente o vetor de struct, de acordo com o numero de elementos - 1 (potque um sera excluido)
                    todos_usuarios = (dados_usuario *) malloc((elementos-1)*sizeof(dados_usuario));

                    // Esse while eh usado para pegar todos os dados exceto aquele que se deseja excluir
                    while (!feof(arq_usuarios))
                    {

                        // Armazena linha do arquivo na variavel buffer
                        fgets(buffer,200,arq_usuarios);

                        // Se agencia e cc forem diferentes da que sera excluida, armazena dados na struct
                        if ((strcmp(captura(buffer,1),str_agencia)!=0) && (strcmp(captura(buffer,2),str_cc)!=0))
                        {
                            todos_usuarios[pos].agencia = atoi(captura(buffer,1));
                            todos_usuarios[pos].cc = atoi(captura(buffer,2));
                            todos_usuarios[pos].senha = atoi(captura(buffer,3));
                            todos_usuarios[pos].bloqueado = atoi(captura(buffer,4));
                            pos++;
                        }
                    }

                    // Fecha arquivo e abre novamente so que com permissao de escrita
                    fclose(arq_usuarios);

                    // abre arquivo para escrita
                    arq_usuarios = fopen("usuarios.txt","w");

                    if (arq_usuarios != NULL)
                    {
                        for (pos=0;pos<(elementos-1);pos++)
                        {
                            // Escreve agora os dados ja atualizados
                            if (pos==0)
                                fprintf(arq_usuarios,"%d;%d;%d;%d",todos_usuarios[pos].agencia,todos_usuarios[pos].cc,todos_usuarios[pos].senha,todos_usuarios[pos].bloqueado);
                            else
                                fprintf(arq_usuarios,"\n%d;%d;%d;%d",todos_usuarios[pos].agencia,todos_usuarios[pos].cc,todos_usuarios[pos].senha,todos_usuarios[pos].bloqueado);

                        }

                        // Fecha arquivo
                        fclose(arq_usuarios);

                        printf("Conta excluida com sucesso!\n");
                        system("pause");
                        menu_principal(terminal);
                    }
                    else
                    {
                        erro();
                        menu_principal(terminal);
                    }
                }
                else
                {
                    erro();
                    menu_principal(terminal);
                }
            }
            else if (verifica_login(agencia_ex,cc_ex,0) == 4)
            {
                erro();
                menu_principal(terminal);
            }
            else
            {
                // Exbe mensagem de que nao encontrou a conta
                printf("\nConta nao encontrada!\n");

                // Assume valor 0 para a variavel de opcao
                int opc = 0;

                // Enquanto nao selecionar uma opcao valida, pede novamente
                while ((opc <1) ||(opc>2))
                {
                    printf("Deseja tentar novamente? (1 - S/ 2 - N): ");
                    scanf("%d",&opc);

                    // Exibe mensagem se a opcao for invalida
                    if ((opc <1) ||(opc>2))
                        printf("\nOpcao invalida!\n");
                }

                /*
                      Se ele quiser tentar novamente, chama a mesma funcao com os mesmos parametros
                      senao volta ao menu principal
                */
                if (opc == 1)
                    excluir_conta(terminal);
                else
                    menu_principal(terminal);
            }
        }
        else
        {
            // Se errar a senha do gerente
            printf("Acesso negado, senha incorreta!\n");
            system("pause");
            menu_principal(terminal);
        }
    }
    else
    {
        erro();
        menu_principal(terminal);
    }
}


void menu_usuario(int *terminal,dados_usuario *usuario)
{
    // Procedimento para exibir o menu do usuario e suas opcoes

    // Variavel para armazenar opcoes escolhidas pelo usuario
    int opc;

    // Limpa a tela
    system("cls");

    // Cabecalho e opcoes do menu
    printf("############### BANCO KNC - O SEU BANCO DE SUCESSO ###############\n\n");
    printf("Nome: %s - Agencia: %d  - Conta Corrente: %d\n",usuario[0].nome,usuario[0].agencia,usuario[0].cc);
    printf("Menu do Usuario - Terminal %d\n",*terminal);
    printf("1 - Extrato\n");
    printf("2 - Transferencia\n");
    printf("3 - Saque\n");
    printf("4 - Deposito\n");
    printf("5 - Consulta Saldo/Limite \n");
    printf("6 - Alterar Senha \n");
    printf("7 - Deslogar\n");

    // Instancia variavel de opcoes como zero
    opc = 0;

    // Enquanto o usuario nao escolher uma opcao valida o sistema continua pedindo
    while ((opc<1) || (opc>7))
    {
        printf("\nDigite a opcao desejada: ");
        scanf("%d",&opc);

        // Se opcao for invalida, exibe um aviso
        if ((opc<1) || (opc>7))
            printf("\nOpcao invalida!");
    }

    /*
       Se opcao for 1, vai para o procedimento de menu de extrato
       Se opcao for 2, vai para o procedimento de menu de transferencia
       Se opcao for 3, vai para o procedimento de menu de saque
       Se opcao for 4, vai para o procedimento de menu de deposito
       Se opcao for 5, vai para o procedimento de saldo/limite
       Se opcao for 6, vai para o procedimento de alteracao de senha
       Se for 7, desloga do sistema, voltando ao menu principal
    */
    if (opc == 1)
        menu_extrato(terminal,usuario);
    else if (opc == 2)
        menu_transferencia(terminal,usuario);
    else if (opc == 3)
        menu_saque(terminal,usuario);
    else if (opc == 4)
        menu_deposito(terminal,usuario);
    else if (opc == 5)
        saldo_limite(terminal,usuario);
    else if (opc == 6)
        exibe_alterar_senha(terminal,usuario);
    else
        menu_principal(terminal);

}

void menu_extrato(int *terminal,dados_usuario *usuario)
{
    // Procedimento para exibir o menu de extrato e suas opcoes

    // Variavel para armazenar opcoes escolhidas pelo usuario
    int opc;

    // Limpa a tela
    system("cls");

    // Cabecalho e opcoes do menu
    printf("############### BANCO KNC - O SEU BANCO DE SUCESSO ###############\n\n");
    printf("Nome: %s - Agencia: %d  - Conta Corrente: %d\n",usuario[0].nome,usuario[0].agencia,usuario[0].cc);
    printf("Extrato - Terminal %d\n",*terminal);
    printf("1 - Conta Corrente\n");
    printf("2 - Poupanca\n");
    printf("3 - Voltar ao Menu do Usuario\n");

    // Instancia variavel de opcoes como zero
    opc = 0;

    // Enquanto o usuario nao escolher uma opcao valida o sistema continua pedindo
    while ((opc<1) || (opc>3))
    {
        printf("\nDigite a opcao desejada: ");
        scanf("%d",&opc);

        // Se opcao for invalida, exibe um aviso
        if ((opc<1) || (opc>3))
            printf("\nOpcao invalida!");
    }

    /*
       Se opcao for 1, vai para o procedimento de extrato da conta corrente
       Se opcao for 2, vai para o procedimento de extrato da conta poupanÃƒÂ§a
       Se opcao for 3, volta ao menu do usuario

    */
    if (opc == 1)
        extrato_cc(terminal,usuario);
    else if (opc == 2)
        extrato_poup(terminal,usuario);
    else
        menu_usuario(terminal,usuario);
}

void menu_transferencia(int *terminal,dados_usuario *usuario)
{
    // Procedimento para exibir o menu de transferencia e suas opcoes

    // Variavel para armazenar opcoes escolhidas pelo usuario
    int opc;
    float valor;

    // Limpa a tela
    system("cls");

    // Cabecalho e opcoes do menu
    printf("############### BANCO KNC - O SEU BANCO DE SUCESSO ###############\n\n");
    printf("Nome: %s - Agencia: %d  - Conta Corrente: %d\n",usuario[0].nome,usuario[0].agencia,usuario[0].cc);
    printf("Transferencia - Terminal %d\n",*terminal);
    printf("1 - Conta Corrente / Poupanca\n");
    printf("2 - Poupanca / Conta Corrente\n");
    printf("3 - Conta Corrente / Conta Corrente Externa\n");
    printf("4 - Conta Corrente / Conta Poupanca Externa\n");
    printf("5 - Poupanca / Conta Corrente Externa\n");
    printf("6 - Poupanca / Conta Poupanca Externa\n");
    printf("7 - Voltar ao Menu do Usuario\n");

    // Instancia variavel de opcoes como zero
    opc = 0;

    // Enquanto o usuario nao escolher uma opcao valida o sistema continua pedindo
    while ((opc<1) || (opc>7))
    {
        printf("\nDigite a opcao desejada: ");
        scanf("%d",&opc);

        // Se opcao for invalida, exibe um aviso
        if ((opc<1) || (opc>7))
            printf("\nOpcao invalida!");
    }

    /*
       Se opcao nao for voltar ao menu, chama a funcao transferencia, passando a variavel opc como parametro
       para saber qual tipo de transferencia q eh

    */
    if (opc < 7)
    {
        printf("Digite o Valor que deseja transferir: ");
        scanf("%f",&valor);

        if ((opc==1) || (opc==3) || (opc == 4))
        {
            // Verifica se ha saldo o suficiente para realizar a operacao
            if (verifica_saldo_limite('C',usuario,valor)==1)
                transferencia(opc,terminal,usuario,valor,0);
            else
                printf("Nao tem saldo nem limite suficiente na Conta Corrente!\n");
        }
        else
        {
            // Verifica se ha saldo o suficiente para realizar a operacao
            if (verifica_saldo_limite('P',usuario,valor)==1)
                transferencia(opc,terminal,usuario,valor,0);
            else
                printf("Nao tem saldo suficiente na Conta Poupanca!\n");
        }

        system("pause");
        menu_usuario(terminal,usuario);
    }
    else
        menu_usuario(terminal,usuario);
}

void menu_saque(int *terminal,dados_usuario *usuario)
{
    // Procedimento para exibir o menu de saque e suas opcoes

    // Variavel para armazenar opcoes escolhidas pelo usuario
    int opc;

    // Variavel que armazena o valor a ser sacado
    float valor;

    // Limpa a tela
    system("cls");

    // Cabecalho e opcoes do menu
    printf("############### BANCO KNC - O SEU BANCO DE SUCESSO ###############\n\n");
    printf("Nome: %s - Agencia: %d  - Conta Corrente: %d\n",usuario[0].nome,usuario[0].agencia,usuario[0].cc);
    printf("Saque - Terminal %d\n",*terminal);
    printf("1 - Conta Corrente\n");
    printf("2 - Poupanca\n");
    printf("3 - Voltar ao Menu do Usuario\n");

    // Instancia variavel de opcoes como zero
    opc = 0;

    // Enquanto o usuario nao escolher uma opcao valida o sistema continua pedindo
    while ((opc<1) || (opc>3))
    {
        printf("\nDigite a opcao desejada: ");
        scanf("%d",&opc);

        // Se opcao for invalida, exibe um aviso
        if ((opc<1) || (opc>3))
            printf("\nOpcao invalida!");
    }

    /*
       Se opcao for 1, vai para o procedimento de saque com parametro C para de_conta
       Se opcao for 2, vai para o procedimento de saque com parametro P para de_conta
       Se opcao for 3, volta ao menu do usuario

       Se for opcao 1 ou 2 tambem verifica se ha saldo o suficiente
    */
    if ((opc == 1) ||  (opc == 2))
    {
        // Pede o valor a ser sacado
        printf("Digite o Valor que deseja sacar: ");
        scanf("%f",&valor);

        if (opc == 1)
        {
            // Verifica se ha saldo o suficiente para realizar a operacao
            if (verifica_saldo_limite('C',usuario,valor)==1)
            {
                // Efetua saque de valor da conta corrente
                saque('C',terminal,usuario,valor,0);

                // Insere a movimentacao no arquivo de movimentacao
                insere_mov('C',"S",terminal,usuario,-valor,"");
            }
            else
                printf("Nao tem saldo nem limite suficiente na Conta Corrente!\n");


        }
        else
        {
            // Verifica se ha saldo o suficiente para realizar a operacao
            if (verifica_saldo_limite('P',usuario,valor)==1)
            {
                // Efetua saque de valor da conta poupanca
                saque('P',terminal,usuario,valor,0);

                // Insere a movimentacao no arquivo de movimentacao
                insere_mov('P',"S",terminal,usuario,-valor,"");
            }
            else
                printf("Nao tem saldo suficiente na Conta Poupanca!\n");
        }

        system("pause");
        menu_usuario(terminal,usuario);
    }
    else
        menu_usuario(terminal,usuario);
}

void menu_deposito(int *terminal,dados_usuario *usuario)
{
    // Procedimento para exibir o menu de deposito e suas opcoes

    // Variavel para armazenar opcoes escolhidas pelo usuario
    int opc;

    // Variavel que armazena o valor a ser depositado
    float valor;

    // Limpa a tela
    system("cls");

    // Cabecalho e opcoes do menu
    printf("############### BANCO KNC - O SEU BANCO DE SUCESSO ###############\n\n");
    printf("Nome: %s - Agencia: %d  - Conta Corrente: %d\n",usuario[0].nome,usuario[0].agencia,usuario[0].cc);
    printf("Deposito - Terminal %d\n",*terminal);
    printf("1 - Conta Corrente\n");
    printf("2 - Poupanca\n");
    printf("3 - Voltar ao Menu do Usuario\n");

    // Instancia variavel de opcoes como zero
    opc = 0;

    // Enquanto o usuario nao escolher uma opcao valida o sistema continua pedindo
    while ((opc<1) || (opc>3))
    {
        printf("\nDigite a opcao desejada: ");
        scanf("%d",&opc);

        // Se opcao for invalida, exibe um aviso
        if ((opc<1) || (opc>3))
            printf("\nOpcao invalida!");
    }


    /*
       Se opcao for 1, vai para o procedimento de deposito com parametro C para para_conta
       Se opcao for 2, vai para o procedimento de deposito com parametro P para para_conta
       Se opcao for 3, volta ao menu do usuario

    */
    if ((opc == 1) ||  (opc == 2))
    {
        // Pede o valor a ser depositado
        printf("Digite o Valor que deseja depositar: ");
        scanf("%f",&valor);

        if (opc == 1)
        {
            // Realiza deposito
            deposito('C',terminal,usuario,valor,0);

            // Insere a movimentacao no arquivo de movimentacao
            insere_mov('C',"D",terminal,usuario,valor,"");
        }
        else
        {
            // Realiza deposito
            deposito('P',terminal,usuario,valor,0);

            // Insere a movimentacao no arquivo de movimentacao
            insere_mov('P',"D",terminal,usuario,valor,"");
        }
        system("pause");
        menu_usuario(terminal,usuario);
    }
    else
        menu_usuario(terminal,usuario);
}

void saldo_limite(int *terminal,dados_usuario *usuario)
{
    // Procedimento para exibir saldo e limite de um usuario

    // Variavel para armazenar opcoes escolhidas pelo usuario
    int opc = 0;

    // Limpa a tela
    system("cls");

    // Cabecalho e dados
    printf("############### BANCO KNC - O SEU BANCO DE SUCESSO ###############\n\n");
    printf("Nome: %s - Agencia: %d  - Conta Corrente: %d\n",usuario[0].nome,usuario[0].agencia,usuario[0].cc);
    printf("Consulta de Saldo e Limite - Terminal %d\n",*terminal);
    printf("\nSaldo Conta Corrente: %.2f\n",usuario[0].saldo_cc);
    
    // Se saldo for menor que 0, soma ao limite para obter limite disponivel
    if(usuario[0].saldo_cc<0.00)
       printf("Limite Disponivel Conta Corrente: %.2f\n",(usuario[0].limite+usuario[0].saldo_cc));
    else
       printf("Limite Disponivel Conta Corrente: %.2f\n",(usuario[0].limite));
       
    printf("Saldo Poupanca: %.2f\n\n",usuario[0].saldo_poup);

    // Enquanto nao selecionar uma opcao valida, pede novamente
    while ((opc <1) ||(opc>2))
    {
        printf("Deseja voltar ao menu do usuario? (1 - S/ 2 - N): ");
        scanf("%d",&opc);

        // Exibe mensagem se a opcao for invalida
        if ((opc <1) ||(opc>2))
            printf("\nOpcao invalida!\n");
    }

    /*
          Se deseja voltar ao menu principal, volta a ele
          Se nao deseja, sai do sistema
    */
    if (opc == 1)
        menu_usuario(terminal,usuario);
    else
        menu_principal(terminal);
}

void exibe_alterar_senha(int *terminal,dados_usuario *usuario)
{
    // Procedimento para alterar a senha de um usuario

    // Variavel para armazenar opcoes escolhidas pelo usuario
    int opc;

    // Limpa a tela
    system("cls");

    // Variaveis para manipular
    int senha_antiga,senha_nova,conf_nova;

    // Cabecalho e opcoes do menu
    printf("############### BANCO KNC - O SEU BANCO DE SUCESSO ###############\n\n");
    printf("Nome: %s - Agencia: %d  - Conta Corrente: %d\n",usuario[0].nome,usuario[0].agencia,usuario[0].cc);
    printf("Alterar Senha - Terminal %d\n",*terminal);

    // Pede dados
    printf("Digite a senha antiga: ");
    scanf("%d",&senha_antiga);
    printf("\nDigite a senha nova: ");
    scanf("%d",&senha_nova);
    printf("\nDigite a confirmacao da senha nova: ");
    scanf("%d",&conf_nova);

    // Se a senha nova for igual a confirmacao de senha
    if (conf_nova == senha_nova)
    {
        // Se a senha antiga estiver correta
        if (verifica_login(usuario[0].agencia,usuario[0].cc,senha_antiga)==1)
        {
            // Atualiza a senha
            atualiza_senha(terminal,usuario,senha_nova);
            menu_usuario(terminal,usuario);
        }
        else
        {
            // Se errou a senha antiga
            printf("Senha antiga esta incorreta!\n");
            system("pause");
            menu_usuario(terminal,usuario);
        }
    }
    else
    {
        // Se confirmacao de senha e senha nova diferem
        printf("Senha nova e confirmacao de senha nova devem ser iguais!\n");
        system("pause");
        menu_usuario(terminal,usuario);
    }
}

void criar_conta(char nome[50],int agencia,int cc,int senha)
{
    // Procedimento utilizado para a criacao de contas

    FILE *arq;

    // Para verificar se conta ja existe
    int encontrou = 0;

    // Para armazenar dados de agencia e cc convertidos, bem como dados do arquivo na variavel buffer
    char agencia_str[10],cc_str[10],buffer[200];

    // Para montar o nome dos arquivos
    char nome_arq[50];

    // Abre arquivo para leitura
    arq = fopen("usuarios.txt","r");

    // Transforma valores inteiros de agencia e cc para valores em char
    itoa(cc,cc_str,10);
    itoa(agencia,agencia_str,10);


    if (arq != NULL)
    {
        // Verifica se conta ja existe
        while ((encontrou == 0)&&(!feof(arq)))
        {
            // Pega uma linha do arquivo e armazena na variavel buffer
            fgets(buffer,200,arq);

            // Verifica agencia e conta com as agencias e contas do arquivo
            if ((strcmp(captura(buffer,1),agencia_str)==0) && (strcmp(captura(buffer,2),cc_str)==0))
            {
                // Encontrou uma conta igual
                encontrou = 1;
            }
        }

        // Fecha arquivo
        fclose(arq);

        // Se conta nao existir encontrou = 0, se existir encontrou = 1
        if (encontrou == 0)
        {
            // Abre arquivo para anexacao
            arq = fopen("usuarios.txt","a");

            if (arq != NULL)
            {
                // Cria registro no arquivo de usuarios
                fprintf(arq,"\n%d;%d;%d;0",agencia,cc,senha);

                // fecha arquivo
                fclose(arq);



                // Cria o arquivo de saldo
                strcpy(nome_arq,"dados_");
                strcat(nome_arq,agencia_str);
                strcat(nome_arq,"_");
                strcat(nome_arq,cc_str);
                strcat(nome_arq,".txt");
                arq = fopen(nome_arq,"w");
                fprintf(arq,"%d;%d;%s;200,00;0,00;0,00",agencia,cc,nome);
                fclose(arq);

                // Cria o arquivo de movimentacao na conta corrente
                strcpy(nome_arq,"mov_ccorrente_");
                strcat(nome_arq,agencia_str);
                strcat(nome_arq,"_");
                strcat(nome_arq,cc_str);
                strcat(nome_arq,".txt");
                arq = fopen(nome_arq,"w");
                fclose(arq);


                // Cria o arquivo de movimentacao na conta poupanca
                strcpy(nome_arq,"mov_poupanca_");
                strcat(nome_arq,agencia_str);
                strcat(nome_arq,"_");
                strcat(nome_arq,cc_str);
                strcat(nome_arq,".txt");
                arq = fopen(nome_arq,"w");
                fclose(arq);

                printf("Conta criada com sucesso!\n");
            }
            else
            {
                erro();
            }
        }
        else
        {
            // Se conta ja existe
            printf("Conta ja existente!\n");
        }
    }
    else
    {
        erro();
    }
}

void preenche_dados_usuario(int *terminal,dados_usuario *usuario,int agencia,int cc,int pos)
{
    // Procedimento utilizado para armazenar dados de um usuario especÃ­fico

    FILE *arq;

    // Variaveis para manipular nome do arquivo
    char nome_arquivo[30],str_agencia[10],str_cc[10];

    // Variavel para armazenar string com todos os dados de uma linha do arquivo
    char buffer[200];

    // Variavel para manipular se encontrou o dado desejado no arquivo
    int encontrou = 0;

    // Transforma variaveis agencia e cc em strings
    itoa(agencia,str_agencia,10);
    itoa(cc,str_cc,10);

    // Arruma o nome do arquivo que sera aberto
    strcpy(nome_arquivo,"dados_");
    strcat(nome_arquivo,str_agencia);
    strcat(nome_arquivo,"_");
    strcat(nome_arquivo,str_cc);
    strcat(nome_arquivo,".txt");

    // Abre arquivo para leitura
    arq = fopen(nome_arquivo,"r");

    if (arq != NULL)
    {
        // Pega linha do arquivo e coloca na variavel buffer
        fgets(buffer,200,arq);

        // Preenche dados na struct
        usuario[pos].agencia = atoi(captura(buffer,1));
        usuario[pos].cc = atoi(captura(buffer,2));
        strcpy(usuario[pos].nome,captura(buffer,3));
        usuario[pos].limite = atof(captura(buffer,4));
        usuario[pos].saldo_cc = atof(captura(buffer,5));
        usuario[pos].saldo_poup = atof(captura(buffer,6));

        // Fecha arquivo
        fclose(arq);

        // Abre esse arquivo para pegar se o usuario esta bloqueado ou nao, e armazenar sua senha tambem
        arq = fopen("usuarios.txt","r");

        if (arq != NULL)
        {

            // Enquanto nao for o fim do arquivo e nao encontrou o usuario
            while ((!feof(arq)) && (encontrou == 0))
            {

                // Armazena linha do arquivo na variavel buffer
                fgets(buffer,200,arq);

                // Se agencia e cc forem iguais, armazena senha e variavel "bloqueada"
                if ((strcmp(captura(buffer,1),str_agencia)==0) && (strcmp(captura(buffer,2),str_cc)==0))
                {
                    usuario[pos].senha = atoi(captura(buffer,3));
                    usuario[pos].bloqueado = atoi(captura(buffer,4));
                    encontrou = 1;
                }
            }
        }
        else
        {
            erro();
            menu_principal(terminal);
        }
    }
    else
    {
        erro();
        menu_principal(terminal);
    }

}

void atualiza_senha(int *terminal,dados_usuario *usuario,int senha_nova)
{
    // Procedimento que atualiza a senha, apos usuario solicitar alteracao

    usuario[0].senha = senha_nova;

    // atualiza arquivo usuarios.txt
    atualiza_usuarios(terminal,usuario);

    printf("Senha atualizada com sucesso!\n");
    system("pause");
}

void atualiza_usuarios(int *terminal,dados_usuario *usuario)
{
    /* Procedimento utilizado para atualizar dados do arquivo usuarios.txt
       que eh o arquivo utilizado para login do usuario.
    */
    FILE *arq;
    char buffer[200];
    dados_usuario *todos_usuarios;

    // Variaveis para contadores
    int i= 0,elementos = 0;

    // Abre o arquivo
    arq = fopen("usuarios.txt","r");

    if (arq != NULL)
    {
        /* Esse while apenas conta o numero de usuarios cadastrados
           para alocar dinamicamente o vetor de struct
        */
        while (!feof(arq))
        {
            fgets(buffer,200,arq);
            elementos++;

        }

        // Alocacao dinamica do vetor de struct
        todos_usuarios = (dados_usuario *) malloc(elementos*sizeof(dados_usuario));

        if (todos_usuarios != NULL)
        {
            // Volta ponteiro para o comeco do arquivo
            rewind(arq);

            // Guarda dados na struct
            while (!feof(arq))
            {
                fgets(buffer,200,arq);

                /* Se for o usuario selecionado para editar, atualiza senha e bloqueado com os valores da struct passada
                   Senao, somente coloca os valores do arquivo na struct dinamica criada
                */
                if ((atoi(captura(buffer,1))==usuario[0].agencia)&&(atoi(captura(buffer,2))==usuario[0].cc))
                {
                    todos_usuarios[i].senha = usuario[0].senha;
                    todos_usuarios[i].bloqueado = usuario[0].bloqueado;
                }
                else
                {
                    todos_usuarios[i].senha = atoi(captura(buffer,3));
                    todos_usuarios[i].bloqueado = atoi(captura(buffer,4));
                }

                todos_usuarios[i].agencia = atoi(captura(buffer,1));
                todos_usuarios[i].cc = atoi(captura(buffer,2));

                i++;
            }

            // Fecha arquivo e abre novamente so que com permissao de escrita
            fclose(arq);
            arq = fopen("usuarios.txt","w");

            if (arq != NULL)
            {
                for (i=0;i<elementos;i++)
                {
                    // Escreve agora os dados ja atualizados
                    if (i==0)
                        fprintf(arq,"%d;%d;%d;%d",todos_usuarios[i].agencia,todos_usuarios[i].cc,todos_usuarios[i].senha,todos_usuarios[i].bloqueado);
                    else
                        fprintf(arq,"\n%d;%d;%d;%d",todos_usuarios[i].agencia,todos_usuarios[i].cc,todos_usuarios[i].senha,todos_usuarios[i].bloqueado);

                }

                fclose(arq);
            }
            else
            {
                erro();
                menu_usuario(terminal,usuario);
            }
        }
        else
        {
            erro();
            menu_usuario(terminal,usuario);
        }


    }
    else
    {
        erro();
        menu_usuario(terminal,usuario);
    }
}

void atualiza_conta(char conta,int *terminal,dados_usuario *usuario)
{
    // Procedimento utilizado para atualizar dados da conta de um determinado usuario

    FILE *arq;

    // Variaveis para armazenar nomes e dados necessarios
    char nome_arquivo[30],str_agencia[15],str_cc[15];

    // Transforma variaveis agencia e cc em strings
    itoa(usuario[0].agencia,str_agencia,10);
    itoa(usuario[0].cc,str_cc,10);

    // Arruma o nome do arquivo a ser aberto
    strcpy(nome_arquivo,"dados_");
    strcat(nome_arquivo,str_agencia);
    strcat(nome_arquivo,"_");
    strcat(nome_arquivo,str_cc);
    strcat(nome_arquivo,".txt");

    // Abre arquivo para escrita
    arq = fopen(nome_arquivo,"w");

    if (arq != NULL)
    {
        // Grava dados no arquivo
        fprintf(arq,"%d;",usuario[0].agencia);
        fprintf(arq,"%d;",usuario[0].cc);
        fprintf(arq,"%s;",usuario[0].nome);
        fprintf(arq,"%.2f;",usuario[0].limite);
        fprintf(arq,"%.2f;",usuario[0].saldo_cc);
        fprintf(arq,"%.2f;",usuario[0].saldo_poup);
        fclose(arq);
    }
    else
    {
        erro();
        menu_usuario(terminal,usuario);
    }
}

void bloquear_senha(int *terminal,int agencia,int cc)
{
    // Procedimento utilizado para bloquear uma senha

    // Variavel que armazenara dados da conta a ser bloqueada
    dados_usuario usuario_bloqueio[1];

    // Preenche os dados do usuario na struct
    preenche_dados_usuario(terminal,usuario_bloqueio,agencia,cc,0);

    // Muda a conta para bloqueada
    usuario_bloqueio[0].bloqueado = 1;

    // Salva no arquivo
    atualiza_usuarios(terminal,usuario_bloqueio);
}

int desbloquear_senha(int *terminal,int agencia,int cc)
{
    // Funcao utilizada para desbloquear uma conta ja bloqueada

    // Utiliza-se a funcao verifica_login para ver se a conta existe e esta bloqueada
    if (verifica_login(agencia,cc,0)==0)
    {
        // Se conta nao existir, retorna 3, para exibir o aviso de que a conta nao existe
        return 3;
    }
    else if (verifica_login(agencia,cc,0)!=3)
    {
        // Se a conta nao estiver bloqueada, retorna 2, para exibir o aviso que ela nao esta bloqueada
        return 2;
    }
    else
    {
        // Se conta estiver bloqueada, faz procedimentos de desbloqueio

        // Variavel que armazenara dados da conta a ser desbloqueada
        dados_usuario usuario_desbloqueio[1];

        // Preenche os dados do usuario na struct
        preenche_dados_usuario(terminal,usuario_desbloqueio,agencia,cc,0);

        // Atualiza status da conta para ativa
        usuario_desbloqueio[0].bloqueado = 0;

        // Atualiza dados no arquivo
        atualiza_usuarios(terminal,usuario_desbloqueio);

        // Retorna que tudo deu certo no procedimento
        return 1;
    }
}

void saque(char de_conta,int *terminal,dados_usuario *usuario,float valor,int erros)
{
    // Procedimento usado para realizar o saque de valor na conta poupanca ou corrente

    // Variavel utilizada para a confirmacao de senha
    int senha_conf;

    /* PS: Como transferencia usa a mesma funcao,
       quando o de_conta for X (Transf CC/ Qualquer outro tipo)
       ou Y(Transf Poupanca/ Qualquer outro tipo)
       nao precisara da confirmacao de senha, pois ja eh feita na funcao de transferencia
    */

    if ((de_conta != 'X')&&(de_conta != 'Y'))
    {
        printf("\nDigite a senha para confirmacao: ");
        scanf("%d",&senha_conf);
    }
    else
    {
        senha_conf = usuario[0].senha;
    }
    // Se a confirmacao de senha estiver correta


    if (senha_conf == usuario[0].senha)
    {
        if ((de_conta == 'C') || (de_conta == 'X'))
        {
            // se for conta corrente adiciona o saldo_cc
            usuario[0].saldo_cc -= valor;

        }
        else
        {
            // se for conta poupanca adiciona o saldo_poup
            usuario[0].saldo_poup -= valor;
        }

        // Atualiza o arquivo do usuario
        atualiza_conta(de_conta,terminal,usuario);

        if ((de_conta != 'X')&&(de_conta != 'Y'))
            printf("\nSaque realizado com sucesso!\n");
    }
    else
    {
        printf("\nSenha digitada esta incorreta!\n");

        // Aumenta o numero de erros
        erros++;

        // se os erros na senha forem menores que 3
        if (erros < 3)
        {
            // Variavel que manipula opcoes
            int opc = 0;
            // Enquanto nao selecionar uma opcao valida, pede novamente
            while ((opc <1) ||(opc>2))
            {
                printf("Deseja tentar novamente? (1 - S/ 2 - N): ");
                scanf("%d",&opc);

                // Exibe mensagem se a opcao for invalida
                if ((opc <1) ||(opc>2))
                    printf("\nOpcao invalida!\n");
            }

            /*
                  Se deseja tentar novamente, passa os mesmos parametros e tenta
                  Se nao deseja, volta ao menu do usuario
            */
            if (opc == 1)
                saque(de_conta,terminal,usuario,valor,erros);
            else
                menu_usuario(terminal,usuario);
        }
        else
        {
            // Se errar 3 vezes a confirmacao de senha, a senha tambem sera bloqueada
            printf("\nSenha sera bloqueada pois foi digitada 3 vezes errada!\n");
            bloquear_senha(terminal,usuario[0].agencia,usuario[0].cc);
            system("pause");
            menu_principal(terminal);
        }
    }
}


void deposito(char para_conta,int *terminal,dados_usuario *usuario,float valor,int erros)
{
    // Procedimento usado para realizar o deposito de valor na conta poupanca ou corrente

    // Variavel utilizada para a confirmacao de senha
    int senha_conf;

    /* PS: Como transferencia usa a mesma funcao,
       quando o para_conta for X (Transf CC/ Qualquer outro tipo)
       ou Y(Transf Poupanca/ Qualquer outro tipo)
       nao precisara da confirmacao de senha, pois ja eh feita na funcao de transferencia
    */

    if ((para_conta != 'X')&&(para_conta != 'Y'))
    {
        printf("\nDigite a senha para confirmacao: ");
        scanf("%d",&senha_conf);
    }
    else
    {
        senha_conf = usuario[0].senha;
    }
    // Se a senha digitada estiver correta
    if (senha_conf == usuario[0].senha)
    {
        if ((para_conta == 'C') ||(para_conta == 'X'))
        {
            // se for conta corrente adiciona o saldo_cc
            usuario[0].saldo_cc += valor;

        }
        else
        {
            // se for conta poupanca adiciona o saldo_poup
            usuario[0].saldo_poup += valor;
        }

        // Atualiza o arquivo do usuario
        atualiza_conta(para_conta,terminal,usuario);

        if ((para_conta != 'X')&&(para_conta != 'Y'))
            printf("Deposito realizado com sucesso!\n");
    }
    else
    {
        printf("\nSenha digitada esta incorreta!\n");

        // Aumenta o numero de erros
        erros++;

        if (erros < 3)
        {
            // Variavel que manipula opcoes
            int opc = 0;

            // Enquanto nao selecionar uma opcao valida, pede novamente
            while ((opc <1) ||(opc>2))
            {
                printf("Deseja tentar novamente? (1 - S/ 2 - N): ");
                scanf("%d",&opc);

                // Exibe mensagem se a opcao for invalida
                if ((opc <1) ||(opc>2))
                    printf("\nOpcao invalida!\n");
            }

            /*
                  Se deseja tentar novamente, passa os mesmos parametros e tenta
                  Se nao deseja, volta ao menu do usuario
            */
            if (opc == 1)
                deposito(para_conta,terminal,usuario,valor,erros);
            else
                menu_usuario(terminal,usuario);
        }
        else
        {
            // Se errar 3 vezes a confirmacao de senha, a senha tambem sera bloqueada
            printf("\nSenha sera bloqueada pois foi digitada 3 vezes errada!\n");
            bloquear_senha(terminal,usuario[0].agencia,usuario[0].cc);
            system("pause");
            menu_principal(terminal);
        }
    }
}

void transferencia(int opc,int *terminal,dados_usuario *usuario,float valor,int erros)
{
    /* Procedimento utilizado para realizar as transferencias entre contas do mesmo usuario
       ou entre contas do usuario e contas externas

       Na variavel opc:

          Se for 1, entao eh transf Conta Corrente / Poupanca
          Se for 2, entao eh transf Poupanca / Conta Corrente
          Se for 3, entao eh transf Conta Corrente / Conta Corrente Externa
          Se for 4, entao eh transf Conta Corrente / Conta Poupanca Externa
          Se for 5, entao eh transf Poupanca / Conta Corrente Externa
          Se for 6, entao eh transf Poupanca / Conta Poupanca Externa

    */

    // Variavel utilizada para a confirmacao de senha
    int senha_conf;

    // Limpa a tela
    system("cls");

    // Cabecalho e opcoes do menu
    printf("############### BANCO KNC - O SEU BANCO DE SUCESSO ###############\n\n");
    printf("Nome: %s - Agencia: %d  - Conta Corrente: %d\n",usuario[0].nome,usuario[0].agencia,usuario[0].cc);
    printf("Transferencia - Terminal %d\n",*terminal);

    // Pede confirmacao de senha
    printf("\nDigite a senha para confirmacao: ");
    scanf("%d",&senha_conf);

    // Se confirmacao de senha estiver correta
    if (senha_conf == usuario[0].senha)
    {

        if (opc == 1)
        {
            // CC/Poupanca

            // Efetua saque na CC e deposita na Poupanca,tambem insere a movimentacao para os dois
            saque('X',terminal,usuario,valor,0);
            insere_mov('C',"TP",terminal,usuario,-valor,"Transf cc p/ poupanca");
            deposito('Y',terminal,usuario,valor,0);
            insere_mov('P',"TP",terminal,usuario,valor,"Transf cc p/ poupanca");
        }
        else if (opc == 2)
        {
            // Poupanca/CC

            // Efetua saque na Poupanca e deposita na CC,tambem insere a movimentacao para os dois
            saque('Y',terminal,usuario,valor,0);
            insere_mov('P',"TC",terminal,usuario,-valor,"Transf poupanca p/ cc");
            deposito('X',terminal,usuario,valor,0);
            insere_mov('C',"TC",terminal,usuario,valor,"Transf poupanca p/ cc");
        }
        else if ((opc >=3) && (opc<=6))
        {
            // CC e Poupanca/ Outras contas

            // Variaveis para armazenar dados da conta externa a ser transferida
            int agencia_oc,cc_oc;

            // Pede os dados da conta externa a ser transferida
            printf("\nDigite a agencia da conta a ser transferida o dinheiro: ");
            scanf("%d",&agencia_oc);

            printf("\nDigite a conta a ser transferida o dinheiro: ");
            scanf("%d",&cc_oc);

            /* Se a outra conta nao existir, o valor retornado da funcao sera zero
               Se der erro no sistema o valor retornado sera 4
                entao, se a conta existir e nao der erro no sistema ele continua, senao exibe o aviso
            */
            if ((verifica_login(agencia_oc,cc_oc,0) != 0) &&(verifica_login(agencia_oc,cc_oc,0) != 4))
            {
                // Preenche dados do usuario que recebera o dinheiro
                dados_usuario usuario_para[1];

                // Variaveis para armazenar as referencias
                char ref_de[100],ref_para[100];

                // Pega os dados da conta externa
                preenche_dados_usuario(terminal,usuario_para,agencia_oc,cc_oc,0);


                if (opc == 3)
                {
                    // CC/Outra CC

                    // Arruma o texto de referencia na conta do usuario
                    strcpy(ref_de,"Transf CC p/ ");
                    strcat(ref_de,usuario_para[0].nome);

                    // Arruma o texto de referencia na conta externa
                    strcpy(ref_para,"Transf ");
                    strcat(ref_para,usuario[0].nome);
                    strcat(ref_para," p/ CC");

                    // Efetua saque na conta do usuario e deposita na conta externa
                    // Tambem insere os registros na movimentacao
                    saque('X',terminal,usuario,valor,0);
                    insere_mov('C',"CTEC",terminal,usuario,-valor,ref_de);
                    deposito('X',terminal,usuario_para,valor,0);
                    insere_mov('C',"CTEC",terminal,usuario_para,valor,ref_para);


                }
                else if (opc == 4)
                {
                    // CC/Outra Poupanca

                    // Arruma o texto de referencia na conta do usuario
                    strcpy(ref_de,"Transf CC p/ ");
                    strcat(ref_de,usuario_para[0].nome);

                    // Arruma o texto de referencia na conta externa
                    strcpy(ref_para,"Transf ");
                    strcat(ref_para,usuario[0].nome);
                    strcat(ref_para," p/ Poupanca");

                    // Efetua saque na conta do usuario e deposita na conta externa
                    // Tambem insere os registros na movimentacao
                    saque('X',terminal,usuario,valor,0);
                    insere_mov('C',"CTEP",terminal,usuario,-valor,ref_de);
                    deposito('Y',terminal,usuario_para,valor,0);
                    insere_mov('P',"CTEP",terminal,usuario_para,valor,ref_para);
                }
                else if (opc == 5)
                {
                    // Poupanca/Outra CC

                    // Arruma o texto de referencia na conta do usuario
                    strcpy(ref_de,"Transf Poupanca p/ ");
                    strcat(ref_de,usuario_para[0].nome);

                    // Arruma o texto de referencia na conta externa
                    strcpy(ref_para,"Transf ");
                    strcat(ref_para,usuario[0].nome);
                    strcat(ref_para," p/ CC");

                    // Efetua saque na conta do usuario e deposita na conta externa
                    // Tambem insere os registros na movimentacao
                    saque('Y',terminal,usuario,valor,0);
                    insere_mov('P',"PTEC",terminal,usuario,-valor,ref_de);
                    deposito('X',terminal,usuario_para,valor,0);
                    insere_mov('C',"PTEC",terminal,usuario_para,valor,ref_para);
                }
                else if (opc == 6)
                {
                    // Poupanca/Outra Poupanca

                    // Arruma o texto de referencia na conta do usuario
                    strcpy(ref_de,"Transf Poupanca p/ ");
                    strcat(ref_de,usuario_para[0].nome);

                    // Arruma o texto de referencia na conta externa
                    strcpy(ref_para,"Transf ");
                    strcat(ref_para,usuario[0].nome);
                    strcat(ref_para," p/ Poupanca");

                    // Efetua saque na conta do usuario e deposita na conta externa
                    // Tambem insere os registros na movimentacao
                    saque('Y',terminal,usuario,valor,0);
                    insere_mov('P',"PTEP",terminal,usuario,-valor,ref_de);
                    deposito('Y',terminal,usuario_para,valor,0);
                    insere_mov('P',"PTEP",terminal,usuario_para,valor,ref_para);
                }
            }
            else if (verifica_login(agencia_oc,cc_oc,0) == 4)
            {
                printf("\nErro no sistema!\n");
                menu_usuario(terminal,usuario);
            }
            else
            {
                // Exbe mensagem de que nao encontrou a conta
                printf("\nConta nao encontrada!\n");

                // Assume valor 0 para a variavel de opcao
                int opc_ch = 0;

                // Enquanto nao selecionar uma opcao valida, pede novamente
                while ((opc_ch <1) ||(opc_ch>2))
                {
                    printf("Deseja tentar novamente? (1 - S/ 2 - N): ");
                    scanf("%d",&opc_ch);

                    // Exibe mensagem se a opcao for invalida
                    if ((opc_ch <1) ||(opc_ch>2))
                        printf("\nOpcao invalida!\n");
                }

                /*
                      Se ele quiser tentar novamente, chama a mesma funcao com os mesmos parametros
                      senao volta ao menu do usuario
                */
                if (opc_ch == 1)
                    transferencia(opc,terminal,usuario,valor,erros);
                else
                    menu_usuario(terminal,usuario);
            }
        }
        else
        {
            erro();
            menu_usuario(terminal,usuario);
        }

        printf("Transferencia realizada com sucesso!\n");
    }
    else
    {
        printf("\nSenha digitada esta incorreta!\n");

        // Aumenta o numero de erros
        erros++;

        if (erros < 3)
        {
            // Variavel que manipula opcoes
            int opc_ch = 0;
            // Enquanto nao selecionar uma opcao valida, pede novamente
            while ((opc_ch <1) ||(opc_ch>2))
            {
                printf("Deseja tentar novamente? (1 - S/ 2 - N): ");
                scanf("%d",&opc_ch);

                // Exibe mensagem se a opcao for invalida
                if ((opc_ch <1) ||(opc_ch>2))
                    printf("\nOpcao invalida!\n");
            }

            /*
                  Se deseja tentar novamente, passa os mesmos parametros e tenta
                  Se nao deseja, volta ao menu do usuario
            */
            if (opc_ch == 1)
                transferencia(opc,terminal,usuario,valor,erros);
            else
                menu_usuario(terminal,usuario);
        }
        else
        {
            // Se errar 3 vezes a confirmacao de senha, a senha tambem sera bloqueada
            printf("\nSenha sera bloqueada pois foi digitada 3 vezes errada!\n");
            bloquear_senha(terminal,usuario[0].agencia,usuario[0].cc);
            system("pause");
            menu_principal(terminal);
        }
    }
}

int verifica_saldo_limite(char de_conta,dados_usuario *usuario,float valor)
{
    // Essa funcao eh utilizada para verificar se ha saldo o suficiente para alguma operacao como transferencia ou saque

    // Variavel utilizada para armazenar o saldo_c+limite (no caso de CC) ou o saldo_poup (no caso de Poupanca)
    float saldo_total;

    /* Se conta for cc -> saldo_total = saldo_cc + limite
       Senao saldo_total = saldo_poup
    */
    if (de_conta == 'C')
        saldo_total = usuario[0].saldo_cc + usuario[0].limite;
    else
        saldo_total = usuario[0].saldo_poup;

    // Se valor a ser sacado/transferido for maior que o saldo, retorna 0, senao retorna 1
    if (saldo_total<valor)
        return 0;
    else
        return 1;
}

void insere_mov(char para_conta,char tipo[4],int *terminal,dados_usuario *usuario,float valor,char ref[100])
{
    // Variavel para manipular o nome do arquivo e transformar valores inteiros em strings
    char nome_arquivo[80],str_agencia[15],str_cc[15],str_terminal[10],ref_aux[100];

    //  Variavel para pegar o tamanho do arquivo e verificar se o mesmo esta vazio
    long pos_atual;

    // Transforma variaveis agencia e cc em strings
    itoa(usuario[0].agencia,str_agencia,10);
    itoa(usuario[0].cc,str_cc,10);
    itoa(*terminal,str_terminal,10);

    FILE *arq;

    // Se para_conta for c, eh conta corrente, senao eh poupanca
    if (para_conta == 'C')
        strcpy(nome_arquivo,"mov_ccorrente_");
    else
        strcpy(nome_arquivo,"mov_poupanca_");


    // Arruma o nome do arquivo para abri-lo posteriormente
    strcat(nome_arquivo,str_agencia);
    strcat(nome_arquivo,"_");
    strcat(nome_arquivo,str_cc);
    strcat(nome_arquivo,".txt");

    // Arruma a referencia, para colocar tambem o n do terminal
    strcpy(ref_aux,ref);
    strcat(ref_aux," Terminal: ");
    strcat(ref_aux,str_terminal);

    // Abre o arquivo para leitura, para verificar se esta vazio ou nao
    arq = fopen(nome_arquivo,"r");
    if (arq != NULL)
    {
        // Posiciona ponteiro de arquivo no final do mesmo
        fseek(arq,0,SEEK_END);

        // Pega tamanho do arquivo
        pos_atual = ftell(arq);

        // Fecha arquivo
        fclose(arq);

        // Abre novamente o arquivo, mas para anexar dados
        arq = fopen(nome_arquivo,"a");


        if (arq != NULL)
        {
            // Se arquivo nao estiver vazio, pula para a prox linha
            if (pos_atual != 0)
                fprintf(arq,"\n");


            // Insere dados no arquivo
            fprintf(arq,"%s %s;",__DATE__,__TIME__);
            fprintf(arq,"%s;",tipo);
            fprintf(arq,"%.2f;",valor);
            fprintf(arq,"%s;",ref_aux);

            // Fecha arquivo
            fclose(arq);
        }
        else
        {
            erro();
            menu_usuario(terminal,usuario);
        }
    }
    else
    {
        erro();
        menu_usuario(terminal,usuario);
    }
}

void extrato_cc(int *terminal,dados_usuario *usuario)
{
    FILE *arq;

    // Variavel utilizada para guardar todos os dados de movimentacoes
    movimentacao *mov_cc;

    // Variavel para manipular o nome do arquivo e transformar valores inteiros em strings
    char nome_arquivo[80],str_agencia[15],str_cc[15],buffer[200];
    long pos_atual;
    // Variavel para manipular o numero de elementos no vetor de struct e a posicao do elemento a ser preenchido
    int elementos = 0,i=0;

    // Transforma variaveis agencia e cc em strings
    itoa(usuario[0].agencia,str_agencia,10);
    itoa(usuario[0].cc,str_cc,10);

    // Arruma o nome do arquivo para abri-lo posteriormente
    strcpy(nome_arquivo,"mov_ccorrente_");
    strcat(nome_arquivo,str_agencia);
    strcat(nome_arquivo,"_");
    strcat(nome_arquivo,str_cc);
    strcat(nome_arquivo,".txt");

    //Limpa tela
    system("cls");

    // Cabecalho e opcoes do menu
    printf("############### BANCO KNC - O SEU BANCO DE SUCESSO ###############\n\n");
    printf("Nome: %s - Agencia: %d  - Conta Corrente: %d\n",usuario[0].nome,usuario[0].agencia,usuario[0].cc);
    printf("Extrato Conta Corrente  - Terminal %d\n",*terminal);

    // Abre arquivo
    arq = fopen(nome_arquivo,"r");

    // Faz isso para verificar se o arquivo esta vazio
    fseek(arq,0,SEEK_END);
    pos_atual = ftell(arq);

    // Volta ponteiro do arquivo para o comeco
    rewind(arq);

    if (arq != NULL)
    {
        // Pega o numero de elementos no arquivo
        while (!feof(arq))
        {
            fgets(buffer,200,arq);
            elementos++;
        }

        // Se elementos forem maiores que 0, pega os dados, senao exibe mensagem
        if ((elementos>0) && (pos_atual!=0))
        {
            // Aloca dinamicamente struct
            mov_cc = (movimentacao *) malloc(elementos*sizeof(movimentacao));

            if (mov_cc != NULL)
            {
                // Volta arquivo ao comeco
                rewind(arq);

                while (!feof(arq))
                {
                    // Pega linha do arquivo
                    fgets(buffer,200,arq);

                    // armazena dados na struct
                    strcpy(mov_cc[i].data,captura(buffer,1));
                    strcpy(mov_cc[i].tipo_op,captura(buffer,2));
                    mov_cc[i].valor = atof(captura(buffer,3));
                    strcpy(mov_cc[i].ref,captura(buffer,4));

                    // Aumenta contador
                    i++;
                }

                // Fecha arquivo
                fclose(arq);

                printf("-------------------------------------------------------------------------\n");

                // Exibe dados
                for (i=(elementos-1);i>=0;i--)
                {
                    printf("DATA: %s\n",mov_cc[i].data);
                    printf("TIPO: %s   ",mov_cc[i].tipo_op);
                    printf("VALOR: %2.2f\n",mov_cc[i].valor);
                    printf("REF: %s\n",mov_cc[i].ref);
                    printf("-------------------------------------------------------------------------");
                    printf("\n");
                }
                
                // Exibe saldo disponivel e limite maximo, se saldo for menor que 0, soma ao do limite para mostrar o limite disponivel
                if(usuario[0].saldo_cc<0.00)
                  printf("Saldo restante: %.2f                        Limite disponivel: %.2f\n",usuario[0].saldo_cc,(usuario[0].limite + usuario[0].saldo_cc));
                else
                  printf("Saldo restante: %.2f                        Limite disponivel: %.2f\n",usuario[0].saldo_cc,(usuario[0].limite));
                             
                printf("-------------------------------------------------------------------------");
                printf("\n");
                
                // Legenda de Tipos de operacao
                printf("___________________________________________________________________________\n");
                printf("                    Legenda de Tipos de Operacoes:\n\n");
                printf("S - Saque                                 D - Deposito\n");
                printf("TP - Transferencia CC/Poup                TC - Transferencia Poup/CC\n");
                printf("PTEP - Transferencia Poup/Poup Externa    CTEC - Transferencia CC/CC Externa\n");
                printf("CTEP - Transferencia CC/Poup Externa      PTEC - Transferencia Poup/CC Externa\n");
                printf("___________________________________________________________________________\n");
            }
            else
            {
                erro();
                menu_usuario(terminal,usuario);
            }
        }
        else
        {
            // Se nao tiver nenhum registro, entra aqui
            printf("\nNenhum registro para exibir...\n\n");

        }

        system("pause");
        menu_usuario(terminal,usuario);
    }
    else
    {
        erro();
        menu_usuario(terminal,usuario);
    }
}

void extrato_poup(int *terminal,dados_usuario *usuario)
{
    FILE *arq;

    // Variavel utilizada para guardar todos os dados de movimentacoes
    movimentacao *mov_cc;

    // Variavel para manipular o nome do arquivo e transformar valores inteiros em strings
    char nome_arquivo[80],str_agencia[15],str_cc[15],buffer[200];
    long pos_atual;
    // Variavel para manipular o numero de elementos no vetor de struct e a posicao do elemento a ser preenchido
    int elementos = 0,i=0;

    // Transforma variaveis agencia e cc em strings
    itoa(usuario[0].agencia,str_agencia,10);
    itoa(usuario[0].cc,str_cc,10);

    // Arruma o nome do arquivo para abri-lo posteriormente
    strcpy(nome_arquivo,"mov_poupanca_");
    strcat(nome_arquivo,str_agencia);
    strcat(nome_arquivo,"_");
    strcat(nome_arquivo,str_cc);
    strcat(nome_arquivo,".txt");

    //Limpa tela
    system("cls");

    // Cabecalho e opcoes do menu
    printf("############### BANCO KNC - O SEU BANCO DE SUCESSO ###############\n\n");
    printf("Nome: %s - Agencia: %d  - Conta Corrente: %d\n",usuario[0].nome,usuario[0].agencia,usuario[0].cc);
    printf("Extrato Conta Poupanca  - Terminal %d\n",*terminal);

    // Abre arquivo
    arq = fopen(nome_arquivo,"r");

    // Faz isso para verificar se o arquivo esta vazio
    fseek(arq,0,SEEK_END);
    pos_atual = ftell(arq);

    // Volta ponteiro do arquivo para o comeco
    rewind(arq);

    if (arq != NULL)
    {
        // Pega o numero de elementos no arquivo
        while (!feof(arq))
        {
            fgets(buffer,200,arq);
            elementos++;
        }

        // Se elementos forem maiores que 0, pega os dados, senao exibe mensagem
        if ((elementos>0) && (pos_atual!=0))
        {
            // Aloca dinamicamente struct
            mov_cc = (movimentacao *) malloc(elementos*sizeof(movimentacao));

            if (mov_cc != NULL)
            {
                // Volta arquivo ao comeco
                rewind(arq);

                while (!feof(arq))
                {
                    // Pega linha do arquivo
                    fgets(buffer,200,arq);

                    // armazena dados na struct
                    strcpy(mov_cc[i].data,captura(buffer,1));
                    strcpy(mov_cc[i].tipo_op,captura(buffer,2));
                    mov_cc[i].valor = atof(captura(buffer,3));
                    strcpy(mov_cc[i].ref,captura(buffer,4));

                    // Aumenta contador
                    i++;
                }

                // Fecha arquivo
                fclose(arq);

                printf("-------------------------------------------------------------------------\n");

                // Exibe dados
                for (i=(elementos-1);i>=0;i--)
                {
                    printf("DATA: %s\n",mov_cc[i].data);
                    printf("TIPO: %s   ",mov_cc[i].tipo_op);
                    printf("VALOR: %2.2f\n",mov_cc[i].valor);
                    printf("REF: %s\n",mov_cc[i].ref);
                    printf("-------------------------------------------------------------------------");
                    printf("\n");
                }
                
                // Exibe saldo restante
                printf("Saldo restante: %.2f \n",usuario[0].saldo_poup);
                printf("-------------------------------------------------------------------------");
                printf("\n");
                
                // Legenda de tipos de operacao
                printf("___________________________________________________________________________\n");
                printf("                    Legenda de Tipos de Operacoes:\n\n");
                printf("S - Saque                                 D - Deposito\n");
                printf("TP - Transferencia CC/Poup                TC - Transferencia Poup/CC\n");
                printf("PTEP - Transferencia Poup/Poup Externa    CTEC - Transferencia CC/CC Externa\n");
                printf("CTEP - Transferencia CC/Poup Externa      PTEC - Transferencia Poup/CC Externa\n");
                printf("___________________________________________________________________________\n");
            }
            else
            {
                erro();
                menu_usuario(terminal,usuario);
            }
        }
        else
        {
            // Se nao tiver nenhum registro, entra aqui
            printf("\nNenhum registro para exibir...\n\n");

        }

        system("pause");
        menu_usuario(terminal,usuario);
    }
    else
    {
        erro();
        menu_usuario(terminal,usuario);
    }
}

