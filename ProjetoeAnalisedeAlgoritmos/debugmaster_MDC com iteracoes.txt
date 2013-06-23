// Aula02 - MDC.cpp : Defines the entry point for the console application.
//

#include "stdafx.h"
#include <stdio.h>

int mdc(int,int,int &);

int main()
{
    int i, mdc_ab, iteracoes = 0, maximaIteracao = 0;
    for(i=104;i<10000000;i++){
        mdc_ab = mdc(i,103,iteracoes);
        if(iteracoes > 8)
            printf("MDC(%d,18) = %d em %d iteracoes \n", i, mdc_ab, iteracoes);
    iteracoes = 0;
    }
	return 0;
}

int mdc(int a, int b, int &iteracoes){

    if(a==0 && b==0){
        iteracoes = 0;
        return 0;
    }

    if(b == 0)
        return a;
    else{
        iteracoes += 1;
        return mdc(b, a%b, iteracoes);
    }

}

