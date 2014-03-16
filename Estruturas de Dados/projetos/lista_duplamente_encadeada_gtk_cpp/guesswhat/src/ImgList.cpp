/* 
 * File:   ImgList.cpp
 * Author: raul
 * 
 * Created on June 20, 2012, 8:35 PM
 */

#include "ImgList.h"

int ImgList::getCount() {
    return count;
}

ImgList::ImgList() {
}

ImgList::ImgList(string path, string keyword) {
    setPath(path);
    setKeyword(keyword);
    setNext(NULL);
    setPrev(NULL);
    count++;
}

ImgList::ImgList(const ImgList& orig) {
    setPath(orig.path);
    setKeyword(orig.keyword);
    setNext(orig.next);
    setPrev(NULL);
}

ImgList::~ImgList() {
}

string ImgList::getKeyword() {
    return keyword;
}

void ImgList::setKeyword(string keyword) {
    this->keyword = keyword;
}

string ImgList::getPath() {
    return path;
}

void ImgList::setPath(string path) {
    this->path = path;
}

ImgList* ImgList::getNext() {
    return next;
}

void ImgList::setNext(ImgList* next) {
    this->next = next;
}

ImgList* ImgList::getPrev() {
    return prev;
}

void ImgList::setPrev(ImgList* prev) {
    this->prev = prev;
}

void ImgList::add(string path, string keyword) {
    ImgList* aux = this;
    // encontra a ultima posicao
    while (aux->getNext()) {
        aux = aux->getNext();
    }
    ImgList* newImg = new ImgList(path, keyword);
    aux->setNext(newImg);
    newImg->setPrev(aux);
}

void ImgList::remove(string keyword) {
    ImgList* aux = this;
    // verifica se e o primeiro elemento da lista
    if (getKeyword().compare(keyword) == 0) {
        if (getNext()) {
            // em c++ nao e possivel reinstanciar neste contexto,
            // copiamos o proximo para depois excluir o primeiro e reinstanciar
            ImgList* backup = aux->getNext();
            delete aux;
            aux = new ImgList(*backup);
            delete backup;
            count--;
        } else {
            // se for o unico elemento, a classe comete suicidio
            delete aux;
        }

        return;
    }

    while (aux->getNext()) {
        // vai iterar todos em busca da palavra chave
        aux = aux->getNext();
        if (aux->getKeyword().compare(keyword) == 0) {
            // achou
            // o anterior de aux recebe o proximo de aux se existir
            if (aux->getNext()) {
                aux->getPrev()->setNext(aux->getNext());
                // o proximo de aux recebe o anterior de aux
                aux->getNext()->setPrev(aux->getPrev());
            } else {
                // removendo o ultimo da lista
                aux->getPrev()->setNext(NULL);
            }

            count--;
            delete aux;

        }

    }

}