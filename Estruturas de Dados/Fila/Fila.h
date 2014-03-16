/*
 * Fila.h
 *
 *  Created on: 03/06/2012
 *      Author: joaovitor
 */
#include "Lista.h"
#include "Node.h"
#ifndef FILA_H_
#define FILA_H_

template <class T> class Fila: private Lista<T>{
public:
	virtual ~Fila();
	int getTam();
	void insere(T);
	bool remove();
	void imprime();
	bool busca(T);
};


template <class T> Fila<T>::~Fila()
{
	Lista<T>::~Lista();
}

template <class T> int Fila<T>::getTam(){
	return Lista<T>::getTam();
}

template <class T> void Fila<T>::insere(T info){
	Lista<T>::insere(info);
}

template <class T> bool Fila<T>::remove(){
	if(Fila<T>::getTam() != 0){
		Lista<T>::remove(Lista<T>::getHeader()->dir);
		return true;
	}
	return false;
}

template <class T> void Fila<T>::imprime(){
	Lista<T>::imprime();
}


template <class T> bool Fila<T>::busca(T info){
	return Lista<T>::busca(info);
}
#endif /* FILA_H_ */
