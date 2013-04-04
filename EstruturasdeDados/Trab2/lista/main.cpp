/*
 * main.cpp
 *
 *  Created on: 02/06/2012
 *      Author: joaovitor
 */

#include <iostream>
#include "Lista.h"
#include <string>
using namespace std;

int main()
{
//********************programa lista******************
	Lista<int> *lista = new Lista<int>();
	cout<<"Vamos inserir 1 e 3 na lista:\n";
	lista->insere(1);
	lista->insere(3);
	lista->imprime();
	cout<<"\nAgora vamos inserir 2 a esquerda de 3\n";
	lista->insereEsq(2, lista->getHeader()->dir->dir);
	lista->imprime();
	cout<<"\nAgora vamos inserir 4 a direita de 3\n";
	lista->insereDir(4, lista->getHeader()->dir->dir->dir);
	lista->imprime();
	cout<<"\nO tamanho da nossa lista é "<<lista->getTam()<<"\n";
	cout<<"Será que o número 2 está na lista?\n";
	if(lista->busca(2))
		cout <<"Está na lista!\n";
	else
		cout <<"Não está na lista\n";
	cout<<"\nSerá que o número 56 está na lista?\n";
	if(lista->busca(56))
		cout <<"Está na lista!\n";
	else
		cout <<"Não está na lista\n";
	cout <<"\nAgora vamos remover o 2 da lista\n";
	lista->remove(lista->getHeader()->dir->dir);
	lista->imprime();
	cout <<"\nAgora vamos remover o 4 da lista\n";
	lista->remove(lista->getHeader()->esq);
	lista->imprime();
	cout<<"\nO tamanho da nossa lista passa a ser "<<lista->getTam();

	return 0;
}

