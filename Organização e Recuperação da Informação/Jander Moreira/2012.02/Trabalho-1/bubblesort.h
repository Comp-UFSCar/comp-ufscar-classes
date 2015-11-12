// Função para realizar o bubblesort no vetor
void bubbleSort(Registro vetReg[], int size, int *comp, int *mov)
 {
    // Contadores para análise do algoritmo
    *comp = 0;
    *mov = 0;

    int swapped;
    int i;
    Registro temp;
    for (i = 1; i < size; i++)
    {
        swapped = 0;    //this flag is to check if the array is already sorted
        int j;
        for(j = 0; j <= size - i; j++)
        {
            *comp = *comp + 1;   // Para cada comparacao, incrementa
            if(vetReg[j].chave > vetReg[j+1].chave)
            {
                *mov = *mov + 3;  // Movimentos do registro
                temp = vetReg[j];
                vetReg[j] = vetReg[j+1];
                vetReg[j+1] = temp;

                swapped = 1;
            }
        }
        if(!swapped){
            break; //if it is sorted then stop
        }
    }
 }

