// Funcoes extras para inicializar o Registro e desenhar Menu

void exibeMenu(int *op, int tam) {
	//system("clear");
	printf("===\tTamanho atual do Registro: %d\t===\n", tam);
	printf("=== Opcoes ==========\n");
	printf("=\t1) Definir tamanho Registro\n");
	printf("=\t2) Criar Registro com chaves aleatorias\n");
	printf("=\t3) Criar Registro com chaves ordenadas (crescente)\n");
	printf("=\t4) BubbleSort\n");
	printf("=\t5) MergeSort\n");
	printf("=\t6) QuickSort\n");
	printf("=\t7) Exibir Registro\n");
	printf("=\t8) Criar Registro com chaves ordenadas (decrescente)\n");
	printf("=\t666) Saudar Cthulhu!\n");
	printf("=\t0) Sair\n======================\n");
	printf("\t--> ");
	scanf("%d",op);
}

void definirTamanho(int *tam){
	printf("=== Definir tamanho Registro ======\n\t --> ");
	scanf("%d", tam);
}

void initRegRandom(Registro vetReg[], int tam)
{
	int i;
	for(i = 0; i < tam; i++)
		vetReg[i].chave = rand()%tam*2;
}

void initRegOrdenado(Registro vetReg[], int tam)
{
	int i;
	for(i = 0; i < tam; i++)
		vetReg[i].chave = i;
}

void initRegDecrescente(Registro vetReg[], int tam)
{
    int i;
    for(i = 0; i < tam; i++)
        vetReg[i].chave = tam - i;
}

void exibeRegistro(Registro vetReg[], int tam)
{
	int i;
	for(i = 0; i < tam; ++i){
		if(i%8 == 0)
			printf("\n");
		printf("\t%d ", vetReg[i].chave);

	}
	printf("\n");
}


