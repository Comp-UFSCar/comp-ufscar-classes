/*
 * Cadastro.h
 *
 *  Created on: 20/06/2012
 *      Author: joaovitor
 */

#include "Lista.h"
#include "Node.h"
#include <iostream>
#ifndef CADASTRO_H_
#define CADASTRO_H_

using namespace std;

template <class T> class Cadastro: private Lista<T>{
public:
	virtual ~Cadastro();
	bool insere(T);
	bool remove(T);
	int getTam();
	void imprime();
	bool busca(T);
};

template <class T> Cadastro<T>::~Cadastro()
{
	Lista<T>::~Lista();
}

template <class T> int Cadastro<T>::getTam(){
	return Lista<T>::getTam();
}

template <class T> bool Cadastro<T>::insere(T info){
		Node<T> *aux = Lista<T>::getHeader()->dir;
		while(aux != Lista<T>::getHeader() && info > aux->info)
				aux = aux->dir;

		if(aux->info == info && getTam() != 0)
		{
			cout<<"\nValor já existente no cadastro\n";
			return false;
		}
		Lista<T>::insereEsq(info, aux);
		return true;
}

template <class T> bool Cadastro<T>::remove(T info){
	Node<T> *aux = Lista<T>::getHeader()->dir;
	while(aux != Lista<T>::getHeader() && info >= aux->info){
		if(aux->info == info){
			Lista<T>::remove(aux);
			return true;
		}
		aux = aux->dir;
	}
	return false;
}

template <class T> void Cadastro<T>::imprime(){
	Lista<T>::imprime();
}

template <class T> bool Cadastro<T>::busca(T info){
	return Lista<T>::busca(info);
}

#endif /* CADASTRO_H_ */
