Trabalho 3 - Arquivo sem ordenação
--------------------------------------------------------------------
Grupo que desenvolveu:
			Cristiano de Oliveira Faustino  
			Camilo Moreira			 
			João Paulo Souza Soares	 
			Lucas Oliveira David
---------------------------------------------------------------------
Tamanho do bloco/cluster:

	O tamanho do bloco/cluster do programa foi obtido usando-se funções
	para descobrir o tamanho dos clusters do próprio sistema operacional.
	Com o retorno dessas funções, o tamanho do bloco/cluster foi definido
	como o valor em bytes do cluster do próprio SO.

	Foi necessário o uso de funções diferentes de acordo com o sistema operacional,
	pois o atributo st_blksize da struct stat já definida pela biblioteca <sys/stat.h>
	só existia nos sistemas operacionais baseados em unix. Para windows foi utilizada a
	função GetFreeDiskSpace da linguagem c++, que permitia a obtenção do tamanho do cluster
	através da multiplicação do número de setores por cluster pelo número de bytes por setor.
---------------------------------------------------------------------
Execução do programa:

	O programa foi feito em linguagem C/C++. Ambas foram utilizadas em conjunto para a
	obtenção de melhores resultados na manipulação de memória (linguagem c) e no uso de
	diretivas que permitiam a execução de funções diferentes de acordo com o 
	sistema operacional (c++). Foram usadas as funções de manipulação de arquivos, de
	alocação dinâmica e as mais simples como as de exibição dos dados (printf) e 
	leitura dos dados (scanf).

	Os blocos foram alocados em sua totalidade, isto é, foram alocados o número de bytes
	igual ao número de bytes do cluster do SO diretamente, sem alocar em um espaço separado
	o vetor de registros armazenados no bloco. Usou-se a definição de que por mais que um vetor
	não seja declarado com mais de uma posição, um v[1] por exemplo, caso tente acessar v[2] no
	arquivo, se o número de bytes igual ao tamanho de v[1] estiver alocado, o valor de v[2] poderá
	ser utilizado e gravado posteriormente.

	Os registros cadastrados e gravados no arquivo são informações básicas de alunos, que são ra,
	nome, cpf e curso. As mesmas tem suas limitações de caracteres conforme abaixo:
		RA = no máximo 6 caracteres (o 7º é para a indicação de fim de string)
		CPF = no máximo 15 caracteres ( o 16º é para a indicação de fim de string e o cpf permite pontos e hífens)
		Nome e Curso = no máximo 50 caracteres (os 51º's de ambos os campos são para a indicação de fim de string)

	As lógicas para inclusão, listagem e exclusão estão comentadas no código da aplicação, caso seja
	necessário ter essas informações.
---------------------------------------------------------------------
Instruções de uso:

	Quando não houverem registros, a listagem exibirá uma mensagem de que o arquivo não existe, para
	criá-lo basta inserir um primeiro registro.

	O menu de opções será chamado de volta após cada término de execução de uma de suas funções,
	basta pressionar qualquer tecla quando ler o aviso "Pressione qualquer tecla para continuar"
	que ele será redirecionado ao menu.