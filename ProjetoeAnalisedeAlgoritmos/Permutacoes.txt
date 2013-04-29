// Permutations.cpp : Defines the entry point for the console application.
//

#include "stdafx.h"
#include "malloc.h"

#define TAM 8

int total = 0;
int iter = 0;

void permutations(int*, int);
void verifica(int*);

int main(int argc, _TCHAR* argv[])
{
    int *v;

    v = (int*) malloc(TAM*sizeof(int));

    for(int i = 0; i<TAM; i++)
        v[i] = 0;

    permutations(v,0);
    printf("%d\n", total);
    printf("%d\n", iter);

	return 0;
}

void permutations(int *v, int k)
{
    iter++;
    if(k>=TAM)
        return;

    for(int j=0; j<TAM;j++)
    {
        if(v[j] == 0)
        {
            bool jaFoi=false;

            v[j] = k+1;
            verifica(v);
            permutations(v,k+1);
            v[j] = 0;
        }
    }
    return;
}

void verifica(int *v)
{

    bool aux = true;
    int nums = 0;

    for(int i=0;i<TAM;i++){

        if(v[i] != 0)
            nums++;

        for(int j=i+1; j<TAM; j++)            
            if(v[i] == v[j]) 
                return;
    }

    if(nums == TAM && aux == true){ 
        for(int i=0; i< TAM; i++)
            printf("%d ", v[i]);
        printf("\n");
        total++;

    }

}