#include "funcoesPorSO.h"
#include "Structs.h"
#include <stdlib.h>
#include <string.h>

// Protótipos necessários p/ funções (uma função declarada acima dela a chama)
void exibeTelaExclusao();
void excluiDoArquivo(char ra[7]);
void insereRegistro();
void listaArquivo();

// Exibe menu principal
void menuPrincipal()
{
	int opc = 5;
	system("cls"); // Limpa a tela
	//  Enquanto nao for uma opção válida
	while((opc>4)||(opc<1))
	{
		printf("Menu de Opcoes - Arquivo de Alunos (c/ Controle de Blocos)\n");
		printf("1 - Inserir dados no arquivo\n");
		printf("2 - Listar dados do arquivo\n");
		printf("3 - Excluir dado do arquivo\n");
		printf("4 - Sair\n");
		scanf("%d",&opc);

		if((opc>4)||(opc<1))
			printf("Opcao Invalida!\n");

	}
    
	switch(opc)
	{
		case 1:
			insereRegistro();
			break;
		case 2:
			listaArquivo();
			break;
		case 3:
			exibeTelaExclusao();
			break;
		case 4:
			exit;
			break;
	}
}

void exibeTelaExclusao()
{
		char ra[7];
		system("cls");
		printf("Entre com o RA a ser excluido do arquivo: ");
		scanf("%s",&ra);
		excluiDoArquivo(ra);
}

// Retorna o número de registros que cabem em um bloco
int getNumRegistrosBloco(int tamanhoCluster)
{
	// é o tamanho do bloco (tamanho do cluster do SO) subtraindo tamanho da variavel fixa numRegistrosValidos
	// (presente na estrutura de bloco). Isso tudo divide-se pelo tamanho da struct de alunos (tamanho do registro)
	// Retorna a parte inteira da divisão
	return (tamanhoCluster - sizeof(int))/sizeof(struct aluno);
}


void insereRegistro()
{
	
	int tamanhoCluster = getTamanhoClusterSO(); // Tamanho do bloco do SO = tamanho do bloco alocado
	bloco *umBloco; // variável do tipo bloco, p/ manipulações
	umBloco = (bloco *) malloc(tamanhoCluster); // Aloca o espaço igual ao tamanho do cluster do SO
	int numRegistrosBloco = getNumRegistrosBloco(tamanhoCluster); // guarda o numero de registros max em um bloco
	FILE *arquivo;
	arquivo = fopen("alunos.dat","r+b"); // tenta abrir o arquivo
	int indice = 0; // p/ manipulação de indices no array de registros (alunos)
	
	// Se arquivo existir
	if(arquivo!= NULL)
	{
		// Verifica se tem espaço para o registro no ultimo bloco do arquivo
		fseek(arquivo,-tamanhoCluster,SEEK_END);
		fread(umBloco,tamanhoCluster,1,arquivo);
		if(umBloco->numRegistrosValidos < numRegistrosBloco)
		{
			// Há espaço no bloco atual, então apenas inclui registro no final
			fseek(arquivo,-tamanhoCluster,SEEK_END);
			indice = umBloco->numRegistrosValidos; // adicionará em um bloco já existente
		}
		else
		{
			// Caso não haja espaço no bloco atual, cria um novo
			fseek(arquivo,0,SEEK_END);
			free(umBloco);
			umBloco = (bloco *) malloc(tamanhoCluster);
			umBloco->numRegistrosValidos = 0; // é um novo bloco
		}
	}
	else
	{
		// Se arquivo não existir, cria um
		arquivo = fopen("alunos.dat","wb");
		umBloco->numRegistrosValidos = 0; // é um novo bloco

	}

	if(arquivo != NULL)
	{
		umBloco->numRegistrosValidos++; // atualiza contagem de registros válidos
		
		printf("\nEntre com o ra do aluno: ");
		fflush(stdin);
		scanf("%[^\n]",umBloco->alunos[indice].ra);
		printf("\nEntre com o nome do aluno: ");
		fflush(stdin);
		scanf("%[^\n]",umBloco->alunos[indice].nome);
		printf("\nEntre com o cpf do aluno: ");
		fflush(stdin);
		scanf("%[^\n]",umBloco->alunos[indice].cpf);
		printf("\nEntre com o curso do aluno: ");
		fflush(stdin);
		scanf("%[^\n]",umBloco->alunos[indice].curso);
		fflush(stdin);

		// Grava bloco inteiro no arquivo
		fwrite(umBloco,tamanhoCluster,1,arquivo);
		free(umBloco);
		fclose(arquivo);
		printf("\nArquivo gravado com sucesso!\n");
		system("pause");
		menuPrincipal();
	}
	else
	{
		printf("\nErro na criação do arquivo!\n");
		system("pause");
		menuPrincipal();
	}
	
}

void listaArquivo()
{
	int tamanhoCluster = getTamanhoClusterSO(); // Tamanho do bloco do SO = tamanho do bloco alocado
	bloco *umBloco; // variável do tipo bloco, p/ manipulações
	FILE *arquivo;
	arquivo = fopen("alunos.dat","rb"); // Abre o arquivo
	int i; // variavel p/ controle de indice do array de registros (alunos)
	system("cls"); // limpa a tela

	// Se arquivo existir
	if(arquivo != NULL)
	{
		int contadorBloco = 1; // Marca bloco atual
		umBloco = (bloco *)malloc(tamanhoCluster); // aloca bloco
		fread(umBloco,tamanhoCluster,1,arquivo); // lê bloco do arquivo

		// Enquanto não for o fim do arquivo, exibe os dados
		while(!feof(arquivo))
		{
			printf("------------------------------------------------------\n");
			printf("BLOCO %d\n",contadorBloco);
			printf("------------------------------------------------------\n");

			// Enquanto houver registro no bloco p/ exibir
			for(i=0;i<umBloco->numRegistrosValidos;i++)
			{
				printf("##########################\n");
				printf("RA: %s\n",umBloco->alunos[i].ra);
				printf("CPF: %s\n",umBloco->alunos[i].cpf);
				printf("Nome: %s\n",umBloco->alunos[i].nome);	
				printf("Curso: %s\n",umBloco->alunos[i].curso);
				printf("##########################\n\n");
			}
			
			contadorBloco++; // atualiza indice do bloco
			fread(umBloco,tamanhoCluster,1,arquivo); // lê proximo bloco
			
		}
		free(umBloco);
		fclose(arquivo);

		system("pause");
		menuPrincipal();
	}
	else
	{
		printf("\nArquivo nao existe!\n");
		system("pause");
		menuPrincipal();
	}
}

void excluiDoArquivo(char ra[7])
{
	int tamanhoCluster = getTamanhoClusterSO(); // Tamanho do bloco do SO = tamanho do bloco alocado
	bloco *umBloco; // variável do tipo bloco, p/ manipulações
	umBloco = (bloco *)malloc(tamanhoCluster); // aloca bloco
	FILE *arquivo = fopen("alunos.dat","r+b"); //Abre arquivo p/ alteração
	
	// Se arquivo existir
	if(arquivo !=NULL)
	{
		int blocoAtual = 0; // Variável que marca qual bloco está sendo analisado
		int indice = 0; // indice de onde achou o registro no array de registros do bloco
		int numRegistrosBloco = getNumRegistrosBloco(tamanhoCluster); // número máximo de registros por bloco
		fread(umBloco,tamanhoCluster,1,arquivo); // lê bloco do arquivo
		bool achou = false; // variável para controlar saídas dos loop's quando o valor foi encontrado
		int i; // variavel p/ controle de indice do array de registros (alunos)
		bool primeiroUltimo = false; // booleana que verifica se o último bloco do arquivo é também o primeiro bloco

		// enquanto houver bloco no arquivo e não encontrou a chave procurada
		while((!feof(arquivo))&&(!achou))
		{	
			i =0; // resseta indice
			blocoAtual++; //muda o valor que representa o numero do bloco

			// enquanto houverem registros válidos no bloco e não encontrou a chave procurada
			while((i<umBloco->numRegistrosValidos)&&(!achou))
			{
				// compara chave fornecida com chave do registro atual
				if(strcmp(umBloco->alunos[i].ra,ra)==0)
				{
					achou = true; // se achou a chave
					indice = i; // indice do vetor de registros onde o primeiro elemento com essa chave está
				}
				i++; // aumenta o indice de registro
			}
			
			// Lê bloco do arquivo
			fread(umBloco,tamanhoCluster,1,arquivo);
		}

		// Se achou o registro com a chave fornecida
		if(achou)
		{
			
			fseek(arquivo,tamanhoCluster*(blocoAtual-1),SEEK_SET); // aponta pro bloco onde o registro encontrado está
			fread(umBloco,tamanhoCluster,1,arquivo); // lê o bloco

			// aponta pro bloco onde o registro encontrado está, nesse caso para sobrescrever o bloco, caso seja necessário
			fseek(arquivo,tamanhoCluster*(blocoAtual-1),SEEK_SET);

			// Se o número de registros no bloco for maior que 1, significa que ainda existirão registros
			// ao excluir o registro selecionado
			if(umBloco->numRegistrosValidos>1)
			{
				umBloco->alunos[indice] = umBloco->alunos[umBloco->numRegistrosValidos-1];
				umBloco->numRegistrosValidos--;
				fwrite(umBloco,tamanhoCluster,1,arquivo);
			}
			else
			{
				// Se o número de registros no bloco não for maior que 1, significa que o bloco ficará vazio
				// ao excluir o registro selecionado
				// e portanto terá o ultimo bloco escrito por cima dele e o truncamento do arquivo
				// ou a exclusão do arquivo, se não sobrarem mais blocos

				fseek(arquivo,-tamanhoCluster,SEEK_END); // aponta p/ o ultimo bloco
				int pos = ftell(arquivo); // pega posição do ultimo bloco
				fread(umBloco,tamanhoCluster,1,arquivo); // lê último bloco
				fseek(arquivo,tamanhoCluster*(blocoAtual-1),SEEK_SET); // aponta para bloco a ser excluído
				
				// Se o ultimo bloco não for o primeiro
				if((pos!=ftell(arquivo))||(pos!=0))
				{
					fwrite(umBloco,tamanhoCluster,1,arquivo); // escreve ultimo bloco por cima do bloco a ser excluído
					arquivo = truncarArquivo(arquivo); // trunca o arquivo, retirando a duplicação do último bloco
				}
				else
				{
					// bloco selecionado é o primeiro e o último, seta flag indicando essa ocorrência
					primeiroUltimo=true;
				}
			}

			printf("\nExclusao realizada com sucesso!\n");
		
			
		}
		else
		{
			printf("\nRegistro nao encontrado!\n");
			
		}

		
		fclose(arquivo);

		// se o bloco a ser apagado for o único que restou, arquivo é apagado
		if(primeiroUltimo)
			remove("alunos.dat");

		system("pause");
		menuPrincipal();
	}
	else
	{
		printf("\nErro na leitura do arquivo!\n");
		system("pause");
		menuPrincipal();
	}
}
