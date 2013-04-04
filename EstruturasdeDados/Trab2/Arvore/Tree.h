#ifndef TREE_H_INCLUDED
#define TREE_H_INCLUDED

template <class T> class Tree {
    private:
        Node<T> *root;

        void DropTree(Node<T> *);

        T Procurar(Node<T> *, T);

        void PreOrdem(Node<T> *);
        void EmOrdem(Node<T> *);
        void PosOrdem(Node<T> *);
        bool remove(Node<T> *, T);
        Node<T>* NoMaior(Node<T> *);

    public:
        Tree();
        Tree(T);
        ~Tree();

        bool insere(T);
        bool vazio();
        void ExibirPreOrdem();
        void ExibirEmOrdem();
        void ExibirPosOrdem();
        T Procurar(T);
        bool remove(T);
};

template <class T> Tree<T>::Tree() {
    root = NULL;
}

template <class T> Tree<T>::Tree(T item)
{
    root = new Node<T>(item);

    root->dir = root->esq = NULL;
}

template <class T> Tree<T>::~Tree()
{
    DropTree(root);
    root = NULL;
}

template <class T> bool Tree<T>::vazio()
{
    return root == NULL;
}

template <class T> bool Tree<T>::insere(T pItem)
{
    Node<T> *tmp = root, *ant;

    while(tmp != NULL)
    {
        if(tmp->info == pItem)
            return 0;

        ant = tmp;
        tmp = pItem < tmp->info ? tmp->esq : tmp->dir;
    }
    if(vazio())
        root = new Node<T>(pItem);

    else if (ant->info < pItem)
        ant->dir = new Node<T>(pItem);

    else
        ant->esq = new Node<T>(pItem);
}

template <class T> void Tree<T>::PreOrdem(Node<T> *pRoot)
{
    if (pRoot == NULL) return;

    PreOrdem(pRoot->esq);
    cout << pRoot->info << " ";
    PreOrdem(pRoot->dir);
}

template <class T> void Tree<T>::EmOrdem(Node<T> *pRoot) {
    if (pRoot == NULL) return;

    cout << pRoot->info << " ";
    EmOrdem(pRoot->esq);
    EmOrdem(pRoot->dir);
}

template <class T> void Tree<T>::PosOrdem(Node<T> *pRoot) {
    if (pRoot == NULL) return;

    PosOrdem(pRoot->esq);
    PosOrdem(pRoot->dir);
    cout << pRoot->info << " ";
}

template <class T> void Tree<T>::ExibirPreOrdem(){ PreOrdem(root); }
template <class T> void Tree<T>::ExibirEmOrdem() { EmOrdem(root);  }
template <class T> void Tree<T>::ExibirPosOrdem(){ PosOrdem(root); }

template <class T> void Tree<T>::DropTree(Node<T> *pRoot) {
    if (pRoot == NULL) return;

    DropTree(pRoot->esq);
    DropTree(pRoot->dir);

    if(pRoot->esq == NULL && pRoot->dir == NULL) {
        remove(pRoot->info);
        pRoot = NULL;
    }
}

template <class T> T Tree<T>::Procurar(Node<T> *pRoot, T pItem) {
    while (pRoot != NULL)
    {
        if (pItem == pRoot->info)
            return pRoot->info;

        pRoot = pItem < pRoot->info ? pRoot->esq : pRoot = pRoot->dir;
    }

    return NULL;    //  elemento T nao encontrado
}

template <class T> T Tree<T>::Procurar(T pItem) {
    return Procurar(root, pItem);
}

template <class T> bool Tree<T>::remove(Node<T> *pRoot, T pItem) {
    Node<T> *temp = NULL;

    if(pRoot == NULL) return 0;

    if(pItem == pRoot->info) {
        temp = pRoot;

        if(pRoot->esq != NULL && pRoot->dir != NULL) {
            temp = NoMaior(pRoot->esq);
            pRoot->info = temp->info;

            return remove(pRoot->esq, pRoot->info);
        }
        else {

            if(pRoot->esq == NULL && pRoot->dir != NULL) {
                temp = pRoot->dir;
                pRoot->info = temp->info;
                pRoot->dir = temp->dir;
                pRoot->esq = temp->esq;
            }
            else if(pRoot->esq != NULL && pRoot->dir == NULL) {
                temp = pRoot->esq;
                pRoot->info = temp->info;
                pRoot->dir = temp->dir;
                pRoot->esq = temp->esq;
            }

            delete temp;
            temp = NULL;
        }

        return 1;
    }
    else
        return remove(pItem < pRoot->info ? pRoot->esq : pRoot->dir, pItem);
}

template <class T> bool Tree<T>::remove(T pItem){
    return(remove(root, pItem));
}

template <class T> Node<T> * Tree<T>::NoMaior(Node<T> *pRoot){

    while(pRoot->dir != NULL)
        pRoot = pRoot->dir;

    return pRoot;
}

#endif // TREE_H_INCLUDED
