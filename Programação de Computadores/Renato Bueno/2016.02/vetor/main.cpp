#include <iostream>
#include "vetor.h"

int main()
{
   	int tamanhoImpar;
   	std::cout << "O vetor par tera o tamanho padrao."<<std::endl;
   	std::cout << "Entre com o tamanho do vetor impar: ";
   	std::cin>> tamanhoImpar;

   
	vetor Par;
	vetor Impar(tamanhoImpar);

	int i=0;
    while(Par.SetValor(i,i*2))
        i++;


	i=0;
    while(Impar.SetValor(i,i*2+1))
    	i++;
	

    std::cout << "Vetor Par"<<std::endl;
    Par.imprime();
    std::cout << "Vetor Impar"<<std::endl;
    Impar.imprime();

    return 0;
}

