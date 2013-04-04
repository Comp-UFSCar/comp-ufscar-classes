/*
 * Node.h
 *
 *  Created on: 02/06/2012
 *      Author: joaovitor
 */

#ifndef NODE_H_
#define NODE_H_

template <class T> class Node {
public:
	Node();
	Node(T);
	virtual ~Node();

//private:
	T info;
	Node *esq;
	Node *dir;
};

template <class T> Node<T>::Node()
{
    esq = dir = NULL;
}

template <class T> Node<T>::Node(T valor)
{
	info = valor;
	esq = dir = NULL;
}

template <class T> Node<T>::~Node(){

}

#endif /* NODE_H_ */
