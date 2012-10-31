#pragma once

template <class T> class Pilha
{

	struct Node
	{
		T item;
		Node *next;
	};


private:
	Node* topo;
public:
	Pilha(void);
	~Pilha(void);
	bool Empilha(T);
	bool Desempilha(T &);
	bool Vazia();
	T GetTopo();
};

template <class T> Pilha<T>::Pilha(void)
{
	topo=NULL;
}

template <class T> Pilha<T>::~Pilha(void)
{
	/*
	while(topo != NULL)
	{
		Node *aux = topo->next;
		delete topo;
		topo = aux;
	}*/
}


template <class T> bool Pilha<T>::Empilha(T item)
{
	Node *aux = new Node;	
	aux->item = item;
	aux->next = topo;
	topo  = aux;

	return (topo!= NULL);
}

template <class T> bool Pilha<T>::Desempilha(T &item)
{
	if(!Vazia())
	{
		item    = topo->item;
		topo = topo->next;

		return true;
	}
	return false;
}

template <class T> bool Pilha<T>::Vazia()
{
	if(topo==NULL)
		return true;
	else
		return false;
}

template <class T> T Pilha<T>::GetTopo()
{
	T item;
	Desempilha(item);
	Empilha(item);

	return item;
}

