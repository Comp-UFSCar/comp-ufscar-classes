typedef struct aluno
{
	// Struct de registros (cadastro de alunos)
        char ra[7]; // Campo tem 7 como tamanho maximo p/ caber o 'o/' (fim de string)
        char cpf[16]; // 16 para poder ter pontos e hifens digitados, al�m do 'o/'
        char nome[51];
        char curso[51];
} aluno;

typedef struct bloco
{
   int numRegistrosValidos; // N�mero de registros que est�o com valores v�lidos
   aluno alunos[1]; // Registro � alocado como tendo somente uma posicao apenas para 
					// n�o ser necessario alocar espa�o separadamente para ele  
					// e sim usar o espa�o alocado p/ o bloco, somente
} bloco;