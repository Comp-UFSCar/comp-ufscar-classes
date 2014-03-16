/*
 * main.cpp
 *
 *  Created on: 02/06/2012
 *      Author: joaovitor
 */

#include <iostream>
#include "Fila.h"
using namespace std;

int main()
{
	Fila<int> *fila = new Fila<int>();

	int valor, opcao;
	while(opcao != 0){
		cout<<"\n1 - Inserir inteiro na fila\n";
		cout<<"2 - Remover inteiro da fila\n";
		cout<<"3 - Ver tamanho da fila\n";
		cout<<"4 - Procurar elemento na fila\n";
		cout<<"5 - Exibir fila\n";
		cout<<"0 - Sair do programa\n";
		cin >> opcao;
		switch(opcao){
			case 0:
				cout<<"Saindo...\n";
				break;
			case 1:{
				cout<<"Entre com o valor a ser inserido\n";
				cin>> valor;
				fila->insere(valor);
				break;
			}
			case 2:{
				fila->remove();
				break;
			}
			case 3:{
						cout<<"O tamanho da fila é "<<fila->getTam()<<endl;								;
						break;
			}
			case 4:{
				cout<<"Entre com o valor a ser procurado\n";
				cin>> valor;
				if(fila->busca(valor))
					cout<<valor<<" está na fila\n";
				else
					cout<<valor<<" não está na fila\n";
				break;
			}
			case 5:
				cout<<"\nA fila é:\n";
				fila->imprime();
				break;
			default:
				cout<<"Opção inválida!\n";
				break;
			}
	}

}


