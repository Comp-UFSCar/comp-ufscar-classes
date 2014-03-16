/*
 * main.cpp
 *
 *  Created on: 02/06/2012
 *      Author: joaovitor
 */

#include <iostream>
#include "Cadastro.h"
#include <string>
using namespace std;

int main()
{
	Cadastro<string> *cad = new Cadastro<string>();

	int opcao;
	string valor;
	while(opcao != 0){
		cout<<"\n1 - Inserir no cadastro\n";
		cout<<"2 - Remover do cadastro\n";
		cout<<"3 - Ver quantidade de pessoas cadastradas\n";
		cout<<"4 - Procurar nome no cadastro\n";
		cout<<"5 - Exibir todos as pessoas cadastrados\n";
		cout<<"0 - Sair do programa\n";
		cin >> opcao;
		switch(opcao){
			case 0:
				cout<<"Saindo...\n";
				break;
			case 1:{
				cout<<"Entre com o nome da pessoa a ser cadastrada\n";
				cin>>valor;
				cad->insere(valor);
				break;
			}
			case 2:{
				cout<<"Entre com o nome da pessoa a ser removida\n";
				cin>>valor;
				cad->remove(valor);
				break;
			}
			case 3:{
						cout<<"Total de pessoas cadastradas: "<<cad->getTam()<<endl;								;
						break;
			}
			case 4:{
				cout<<"Entre com o nome a ser procurado\n";
				cin>> valor;
				if(cad->busca(valor))
					cout<<valor<<" está cadastrado\n";
				else
					cout<<valor<<" não está cadastrado\n";
				break;
			}
			case 5:
				cout<<"\nRelação de pessoas cadastradas:\n";
				cad->imprime();
				break;
			default:
				cout<<"Opção inválida!\n";
				break;
			}
	}

}


