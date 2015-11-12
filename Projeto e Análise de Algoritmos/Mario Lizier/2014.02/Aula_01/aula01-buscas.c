#include <stdio.h>
#include <stdlib.h>
#include <limits.h>
#include <assert.h>
#include <malloc.h>
#include <time.h> 

#define NTESTS 1000000

int bsequencial( long* vetor, long key, int n )
{
	int i = 0;
	while( (i < n) && (vetor[i] < key) )
		i++;
	if ( (i < n) && (vetor[i] == key ) )
		return i;
	else
		return -1;
}

int bbinaria( long* vetor, long key, int n )
{
	int imax = n-1;
	int imin = 0;
	while( imax >= imin )
	{
		int imid = imin + ((imax - imin) / 2);		
		if( key > vetor[imid] )
			imin = imid + 1;
		else if( key < vetor[imid])
			imax = imid - 1;
		else
			return imid;
	}	
	return -1;
}

int compare (const void * a, const void * b)
{
  return ( *(long*)a - *(long*)b );
}

int main( int argc, char* argv[] )
{
	assert( argc == 2 );
	int n = atoi(argv[1]);
	printf("Tamanho da entrada: %d\n\n", n);
	
	assert( n > 0 );	
	long *V = (long*)malloc(sizeof(long)*n);
		
	srand (time(NULL));
	assert( LONG_MAX >= RAND_MAX );
	int i;
	for(i=0; i<n; i++)
		V[i] = rand() % RAND_MAX;
		
	qsort( V, n, sizeof(long), compare );
	
	printf("Execução de %d buscas sequenciais:\n", NTESTS);

	clock_t start, end;
	double cpu_time_used = 0;
	start = clock();
	for ( i=0; i<NTESTS; i++) {	
		bsequencial( V, V[rand() % n], n);
	}
	end = clock();
	cpu_time_used += ((double) (end - start)) / CLOCKS_PER_SEC;

	printf( "Tempo médio: %lf\n\n", cpu_time_used);	

	printf("Execução de %d buscas binárias:\n", NTESTS);

	cpu_time_used = 0;
	start = clock();
	for ( i=0; i<NTESTS; i++) {	
		bbinaria( V, V[rand() % n], n);
	}
	end = clock();
	cpu_time_used += ((double) (end - start)) / CLOCKS_PER_SEC;

	printf( "Tempo médio: %lf\n", cpu_time_used);	
	
	free(V);
}
