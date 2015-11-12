// quickSort.c
//http://www.comp.dit.ie/rlawlor/Alg_DS/sorting/quickSort.c

int partition(Registro vetReg[], int l, int r, int *comp, int *reg) {
   int pivot, i, j;
   Registro t;
   pivot = vetReg[l].chave;
   i = l; j = r+1;

   while(1)
   {
       *comp = *comp +1;
   	do{
        ++i;
        *comp = *comp + 1;
   	}while( vetReg[i].chave <= pivot && i <= r );
   	do{
        --j;
        *comp = *comp + 1;
   	}while( vetReg[j].chave > pivot );

   	*comp = *comp + 1;
   	if( i >= j ) break;
   	t = vetReg[i]; *reg = *reg + 1;
   	vetReg[i] = vetReg[j]; *reg = *reg + 1;
   	vetReg[j] = t; *reg = *reg + 1;
   }
   *comp = *comp + 1;
   t = vetReg[l]; *reg = *reg + 1;
   vetReg[l] = vetReg[j]; *reg = *reg + 1;
   vetReg[j] = t; *reg = *reg + 1;
   return j;
}

void quickSort(Registro vetReg[], int l, int r, int *comp, int *reg)
{
   int j;
   if( l < r )
   {
   	// divide and conquer
        j = partition( vetReg, l, r, comp, reg);
       quickSort( vetReg, l, j-1, comp, reg);
       quickSort( vetReg, j+1, r, comp, reg);
   }

}

