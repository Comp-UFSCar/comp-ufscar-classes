#define TRUE 1
#define FALSE 0

/* Criação do TAD Pilha */

#define StackSize 100

typedef struct {
		int top;
		char iten[StackSize];	
} Stack;

void createStack(Stack *new){
	new->top = -1; // posicao 0 e 1a posicao da pilha
}

int emptyStack(Stack *s){
	if(s->top == -1)
		return TRUE;
	else
		return FALSE;
}

void pop(Stack *s, int *iten){
	if(emptyStack(s) == FALSE)
		*iten = s->iten[s->top--];		
	else
	{
		printf("Error: The stack is empty! \n");
	}		
}

void push(Stack *s, int iten){
	s->iten[++(s->top)] = iten;	
}

void show(Stack *s){
	if(emptyStack(s) == FALSE){
		printf("\n\nStack number of itens: %d \n\n",s->top+1);
		int i;
		for(i = s->top; i > -1; i--){
			printf("\t| %d |\n", s->iten[i]);
		}	
	}
	else
		printf("Stack is empty");
}
