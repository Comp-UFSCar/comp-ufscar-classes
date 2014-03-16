#include <sys/types.h>
#include <sys/stat.h>
#ifdef WIN32 // Se for windows
#include <Windows.h>
#include <WinBase.h>
#include <io.h>
	int getTamanhoClusterSO()
	{
		LPCWSTR pszDrive = NULL; // Pega o root da partição atual do sistema (se for = NULL)
		bool fResult; // Necessário apenas para a execução da função GetDiskFreeSpace
		// Conjunto de dados passados como parâmetro p/ a função GetDiskFreeSpace,
		//a função retornará os dados p/ essas variáveis
		DWORD dwSectPerClust, dwBytesPerSect, dwFreeClusters, dwTotalClusters;
		fResult = GetDiskFreeSpace (pszDrive,
                                     &dwSectPerClust,
                                     &dwBytesPerSect,
                                     &dwFreeClusters,
                                     &dwTotalClusters);

		// Número de bytes por setor multiplicado pelo numero de setores por cluster é igual ao tamanho do cluster
		return (int) dwBytesPerSect*dwSectPerClust;
	}

	// funcao para o truncamento do arquivo após exclusão de um registro que deixa um bloco vazio
	// (ultimo bloco vai p/ o lugar do bloco vazio e arquivo é truncado)
	FILE *truncarArquivo(FILE *arquivo)
	{
		fseek(arquivo,0,SEEK_END);
		int tamanho = ftell(arquivo); // tamanho do arquivo
		fseek(arquivo,0,SEEK_SET);

		// funcao de truncamento, se retornar 0 deu certo
		if (_chsize(fileno(arquivo), tamanho - getTamanhoClusterSO()) != 0)
		{
			printf("\nErro ao manipular o arquivo!\n");
		}

		return arquivo;
	}
#else
	#ifdef LINUX // Se for linux
	#include <unistd.h>
		int getTamanhoClusterSO()
		{
			// Tamanho do cluster p/ sistemas linux está na variavel st_blksize da struct stat
			struct stat attrib;
			stat("/",&attrib); // Diretorio root do sistema
			return (int) attrib.st_blksize;
		}

		// funcao para o truncamento do arquivo após exclusão de um registro que deixa um bloco vazio
		// (ultimo bloco vai p/ o lugar do bloco vazio e arquivo é truncado)
		FILE *truncarArquivo(FILE *arquivo)
		{
			fseek(arquivo,0,SEEK_END);
			int tamanho = ftell(arquivo); // tamanho do arquivo
			fseek(arquivo,0,SEEK_SET);

			// funcao de truncamento, se retornar 0 deu certo
			if (ftruncate(fileno(arquivo), tamanho - getTamanhoClusterSO()) != 0)
			{
				printf("\nErro ao manipular o arquivo!\n");
			}

			return arquivo;
		}
	#else // se nao for nenhum dos dois SO's
		int getTamanhoClusterSO()
		{
			// Tamanho fixo de 4kb caso o sistema seja diferente de Linux ou Windows
			tamanhoCluster = 4096;
		}

		// Não trunca o arquivo caso o sistema nao seja linux ou windows
		FILE *truncarArquivo(FILE *arquivo)
		{
			return arquivo;
		}
	#endif
#endif
