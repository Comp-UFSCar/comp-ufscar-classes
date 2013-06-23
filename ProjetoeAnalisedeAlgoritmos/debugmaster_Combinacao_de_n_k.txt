// Comb_de_n_k.cpp : Calcula a C(n,k)
//

#include "stdafx.h"
#include <stdio.h>
#include <stdlib.h>
#include <tchar.h>
#include <iostream>
#include <cstdlib>

using namespace std;

int comb(int,int);

int main()
{
    cout << comb(25, 4) << endl;
    
	return 0;
}

int comb(int n, int k){
    //0 <= k <= n
    int i,j, *torre;
    torre = (int*) malloc((k+1)*sizeof(int));

    torre[0] = 1;
    for(i=1; i<=k; i++)
        torre[i] = 0;
    
    for(i=0; i<n;i++){
        if(i<k)
            j = i+1;
        else
            j = k;
        for(;j>0;j--){
            if(j == (i+1))
                torre[j] = 1;
            else if (j <= k)
                torre[j] = torre[j] + torre[j-1];
        }
        //exibe a árvore se necessário
        for(int i=0; i<=k; i++)
            cout << torre[i] << " ";
        cout << endl;
    }

    

    return torre[k];

}

