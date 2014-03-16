typedef struct aluno
{
	// Struct de registros (cadastro de alunos)
        char ra[7]; // Campo tem 7 como tamanho maximo p/ caber o 'o/' (fim de string)
        char cpf[16]; // 16 para poder ter pontos e hifens digitados, além do 'o/'
        char nome[51];
        char curso[51];
} aluno;

typedef struct bloco
{
   int numRegistrosValidos; // Número de registros que estão com valores válidos
   aluno alunos[1]; // Registro é alocado como tendo somente uma posicao apenas para 
					// não ser necessario alocar espaço separadamente para ele  
					// e sim usar o espaço alocado p/ o bloco, somente
} bloco;