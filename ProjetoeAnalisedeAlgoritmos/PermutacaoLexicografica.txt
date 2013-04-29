#include <cstdlib>
#include <iostream>

using namespace std;

void permutarLexico(int);
void insertionSort(char[], int);

int main()
{
    permutarLexico(3);
    system("PAUSE");
    return EXIT_SUCCESS;
}

void permutarLexico(int n){
     
     char inicial[n];
     
     for(int i=0; i<n; i++){
         inicial[i] = 'a' + i;
         cout << inicial[i];
     }
     cout << endl;
     for(int k=n;k>0;k++){
         for(int i=n-2; i>=0; i--){
             if(inicial[i] < inicial[i+1]){
                 int j = n-1;
                 while (inicial[i]>inicial[j])
                     j++;
                 int aux = inicial[i];
                 inicial[i]=inicial[j];
                 inicial[j]=aux;
                 insertionSort(inicial+i+1,n-1-i);
                 for(int i=0; i<n; i++)
                     cout << inicial[i];
                 cout << endl;
                 system("pause");
             }
         }
     }
     system("pause");

}

void insertionSort(char arr[], int n) {
      int i, j, tmp;

	 //for (i = 0; i < n; i++)
	//	 printf("%c ", arr[i]);
//	 cout << endl;

      for (i = 1; i < n; i++) {    
        tmp = arr[i];
        j = i;
        while (j > 0 && arr[j - 1] > tmp) {
            arr[j] = arr[j - 1];
            j--;
        }
        arr[j] = tmp;

			
      }

//	 for (i = 0; i < n; i++)
//		 printf("%c ", arr[i]);
//	 cout << endl << endl;
}
