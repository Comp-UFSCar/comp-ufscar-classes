#ifndef VETOR_H
#define VETOR_H

class vetor //vetores de inteiros
{

public:
   vetor(int=10); 	//construtor. O par�metro int define o tamanho do vetor
   ~vetor(); 		//destrutor
   int get_tamanho() const;	//retorna o tamanho do vetor
   bool SetValor(int pos, int val);	//altera o valor da posi��o "pos" para "val". 
   									//retorna "true" em caso de sucesso e falso em caso de insucesso
   bool GetValor(int pos, int& val) const; 	// em caso de sucesso, retorna "true" e o valor da posi��o "pos" em "val" (passada por refer�ncia). 
   void imprime() const; //m�todo auxiliar para impress�o do vetor na tela (console)

private:
   int tam;  			//tamanho do vetor
   int *ptrvalores;		//ponteiro para a primeira posi��o do vetor

   
};
#endif /*VETOR_H*/ 


