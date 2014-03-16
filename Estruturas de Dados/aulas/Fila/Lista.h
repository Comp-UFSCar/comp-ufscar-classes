/*
 * Lista.h
 *
 *  Created on: 02/06/2012
 *      Author: joaovitor
 */
#include "Node.h"
#include <iostream>
#ifndef LISTA_H_
#define LISTA_H_
using namespace std;

template <class T> class Lista {
public:
	Lista();
	virtual ~Lista();
	void insereDir(T, Node<T> *);
	void insereEsq(T, Node<T> *);
	void insere(T);
	virtual bool remove(Node<T> *);
	void imprime();
	int getTam();
	bool busca(T);
	Node<T>* getHeader();
protected:
	Node<T> *header;
};

template <class T> Lista<T>::Lista()
{
	header = new Node<T>;
	header->dir = header;
	header->esq = header;
}

template <class T> Lista<T>::~Lista()
{
	while(header->dir != header)
	{
		remove(header->dir);
	}
}

template <class T> void Lista<T>::insereDir(T info, Node<T> *node)
{
	Node<T> *novo = new Node<T>(info);
	novo->dir = node->dir;
	node->dir->esq = novo;
	novo->esq = node;
	node->dir = novo;
}

template <class T> void Lista<T>::insereEsq(T info, Node<T> *node)
{
	Node<T> *novo = new Node<T>(info);
	novo->esq = node->esq;
	node->esq->dir = novo;
	novo->dir = node;
	node->esq = novo;
}

template <class T> bool Lista<T>::remove(Node<T> *node)
{
	if(node != header)
	{
		node->esq->dir = node->dir;
		node->dir->esq = node->esq;
		delete node;
		return true;
	}
	else
	{
		return false;
	}
}

template <class T> Node<T>* Lista<T>::getHeader(){
	return header;
}

template <class T> void Lista<T>::insere(T info){
	Node<T> *novo = new Node<T>(info);
	header->esq->dir = novo;
	novo->esq = header->esq;
	novo->dir = header;
	header->esq = novo;
}

template <class T> void Lista<T>::imprime(){
	Node<T> *aux = header->dir;
	while(aux != header){
		cout<< aux->info << endl;
		aux = aux->dir;
	}
	aux = NULL;
	delete aux;
}

template <class T> int Lista<T>::getTam(){
	Node<T> *aux = header->dir;
	int cont = 0;
	while(aux != header){
		cont++;
		aux = aux->dir;
	}
	aux = NULL;
	delete aux;
	return cont;
}

template <class T> bool Lista<T>::busca(T info){
	Node<T> *aux = header->dir;
	while(aux != header){
		if(aux->info == info)
		{
			aux = NULL;
			delete aux;
			return true;
		}
		aux = aux->dir;
	}

	aux = NULL;
	delete aux;
	return false;
}

#endif /* LISTA_H_ */
