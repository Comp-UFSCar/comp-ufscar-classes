#include <iostream>
#include "vetor.h"

vetor::vetor(int T)
{
   if (T > 0) 
     tam = T;
   else
     tam = 10;                 

   ptrvalores = new int[tam];

   for (int i=0;i<tam;i++)
      SetValor(i,0);
}

vetor::~vetor()
{
   delete [] ptrvalores;
}

int vetor::get_tamanho() const{
    return tam;   
}

bool vetor::SetValor(int pos, int val)
{
   if (pos >=0 && pos < tam)
      {
      ptrvalores[pos] = val; 
      return true;
      }
   else 
      return false;   
}

bool vetor::GetValor(int pos, int& val) const
{
    if (pos >= 0 && pos < tam)
      {
         val =ptrvalores[pos];
         return true;
      }
    else
        return false;
}

void vetor::imprime() const
{
     int valor;
     for (int i=0;i<get_tamanho();i++){
        GetValor(i, valor);
        std::cout << valor << " ";
        }
     std::cout << std::endl;
}

