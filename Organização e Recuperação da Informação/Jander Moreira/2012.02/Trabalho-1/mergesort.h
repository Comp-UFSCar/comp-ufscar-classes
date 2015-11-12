//http://www.ic.unicamp.br/~islene/mc102/aula22/mergesort.c

#include <limits.h>

/* Intercala as seqüências v[p]..v[q-1] e v[q]..v[r] */
void intercala (int p, int q, int r, Registro v[],int tam, int *comp, int *reg) {

  int i, j, k;
  Registro w[tam];

  i = p;
  j = q;
  k = 0;

  while (i < q && j <= r) {
      *comp = *comp + 1;
    if (v[i].chave <= v[j].chave){
      w[k++] = v[i++];
        *reg = *reg + 1;
    }
    else{
      w[k++] = v[j++];
        *reg = *reg + 1;
    }
  }

  while (i < q){
    *reg = *reg + 1;
    w[k++] = v[i++];
  }

  while (j <= r){
    *reg = *reg + 1;
    w[k++] = v[j++];
  }

  for (i = p; i <= r; i++)
  {
    v[i] = w[i-p];
    *reg = *reg + 1;
  }

}

void mergesort (int p, int r, Registro v[], int *comp, int *movreg) {
	
  if (p < r) {
    int q = (p + r)/2;
    mergesort (p, q, v, comp, movreg);
    mergesort (q+1, r, v, comp, movreg);
    intercala (p, q+1, r, v, r+1,comp, movreg);
  }
}
