#pragma once

template <class T> class Pilha{

	struct Node{
		T item;
		Node *next;
	};


private:
	Node* topo;
public:
	Pilha(void);
	~Pilha(void);
	T *GetTopo();

	bool Empilha(T);
	bool Desempilha(T &);
	bool Vazia();
};

template <class T> Pilha<T>::Pilha(void){
	topo = NULL;
}

template <class T> Pilha<T>::~Pilha(void){
	topo = NULL;
}


template <class T> bool Pilha<T>::Empilha(T item){

	Node *aux = new Node;
	aux->item = item;
	aux->next = topo;
	topo      = aux;

	return topo != NULL;
}

template <class T> bool Pilha<T>::Desempilha(T &x){
	if(Vazia())
        return true;

    Node *aux;
    aux  = topo;
    x    = topo->item;
    topo = topo->next;

    delete aux;
    aux  = NULL;

    return true;
}

template <class T> bool Pilha<T>::Vazia(){
	return topo == NULL;
}

template <class T> T *Pilha<T>::GetTopo(){
	return &topo->item;
}
