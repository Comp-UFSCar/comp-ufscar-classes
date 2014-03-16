/* 
 * File:   Interpretador.h
 * Author: lucas
 *
 * Created on October 7, 2012, 2:53 PM
 */

#ifndef INTERPRETADOR_H
#define	INTERPRETADOR_H

class Interpretador {
public:
    Interpretador();
    Interpretador(char *);
    virtual ~Interpretador();
    
    bool AtualizarSeq(char *);
    
    void ExibirResultado();
    void ExibirSeq(int);
    
private:
    char SeqOriginal[32], SeqMapeada[32];
    
    int NumSeqInvalidas;
    char **SeqInvalidas;

    void MapearSeq();
    void DefSeqInvalidas();
    
    bool AnalisarSeq();
};

#endif	/* INTERPRETADOR_H */

