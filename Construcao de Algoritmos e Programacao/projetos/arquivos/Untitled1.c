#include <stdio.h>
#include <stdlib.h>
#include <time.h>

typedef struct FRAC
{
   int  num, div;
}frac;

void simplifica(frac *fracao)
{
   int aux = 2;
   
   while((aux <= fracao[0].num) && (aux <= fracao[0].div))
   {
              if((fracao[0].num % aux == 0) &&(fracao[0].div % aux == 0))
              {
                 fracao[0].num = fracao[0].num / aux;
                 fracao[0].div = fracao[0].div / aux;
              }
              else
              {
                  aux++;
              }
   }
}

int main()
{
    frac fracao[1];
    
    fracao[0].num = 9;
    fracao[0].div = 8;
     simplifica(fracao);
     
     printf("%d / %d",fracao[0].num,fracao[0].div);
    
    system("pause");
    return 0;
}
