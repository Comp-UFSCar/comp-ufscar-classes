#include<stdio.h>
int V[5]= {1,3,5,2,6};
//1,3,5,2,6
//1,3,2,5,6
//1,2,3,5,6
int n=5;

void swap(a)
{
    int temp,i;
    temp=V[a];
    for(i=a;i<(n-1);i++){
        V[i]=V[i+1];
    }
    V[n-1]=temp;
}

int main()
{
    /*int count = 0;
    int temp=0;
    int i,j=0;
    while(j!=1)
    {
        temp=count;
        for(i=0; i<(tam-2); i++)
        {
            if(V[i]>V[i+1])
            {
                swap(i,i+1);
                count++;
            }
        }
        if(count==temp)
            j=1;
    }
    printf("Numero minimo de trocas: %i\nVetor: %i %i %i %i %i", count, V[0], V[1], V[2], V[3], V[4]);*/
    swap(1);
    printf("Vetor: %i %i %i %i %i", V[0], V[1], V[2], V[3], V[4]);
    return 0;
}

