Estruturas
	
	dado____________	Base para a chave a ser utilizada como critério de
	|	int chave	|	descida na árvore B e também contém a informação 
	|	void *info	|	associada a cada chave.
	|_______________|		
	
	no______________________________	Cada nó possui um vetor com o número máximo de dados
	|	dado dado[MAXIMO_CHAVES]	|	que a, pré-definida, ordem pode conter. Também outro 
	|	no	*pont[ORDEM]			|	vetor do mesmo tipo nó que são ponteiros para os filhos.
	|	int cont					|	E um contador para controlar a quantia de chaves existentes.
	|_______________________________|
	
	arvoreb_________	Contém a árvore em "si", com um ponteiro do tipo nó que aponta para a raíz
	|	no *raiz	|	da árvore, também uma variável para receber a ordem da mesma.
	|	int ordem	|
	|_______________|