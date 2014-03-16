/* 
 * File:   main.cpp
 * Author: lucas
 *
 * Created on October 7, 2012, 2:53 PM
 */

#include <cstdlib>
#include "Interpretador.h"
#include <iostream>

using namespace std;

int main(int argc, char** argv) {
    Interpretador Inter;
    char SeqEntrada[32];
    
    cout << "Hello World!\n";
    
    while(1)
    {
        cout << "\nInsira sequencia [Ctrl +C para sair]: ";
        cin >> SeqEntrada;
        
        Inter.AtualizarSeq(SeqEntrada);
    
        Inter.ExibirSeq(0);
        Inter.ExibirSeq(1);
        Inter.ExibirResultado();
    }
    
    return 0;
}

