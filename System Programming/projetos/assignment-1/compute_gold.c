#include <stdio.h>
#include <stdlib.h>
////////////////////////////////////////////////////////////////////////////////
// export C interface
extern int compute_gold( float*, unsigned int);

// Perform Gaussian elimination in place on the U matrix
int compute_gold(float* U, unsigned int num_elements){
	unsigned int i, j, k;
	
	for (k = 0; k < num_elements; k++){
		for (j = (k + 1); j < num_elements; j++){ // Reduce the current row
			if (U[num_elements*k + k] == 0){
				printf("Numerical instability detected. The principal diagonal element is zero. \n");
				return 0;
			}
			U[num_elements * k + j] = (float)(U[num_elements * k + j] / U[num_elements * k + k]); // Division step
		}
		U[num_elements * k + k] = 1; // Set the principal diagonal entry in U to be 1 
		for (i = (k+1); i < num_elements; i++){
			for (j = (k+1); j < num_elements; j++)
				U[num_elements * i + j] = U[num_elements * i + j] - (U[num_elements * i + k] * U[num_elements * k + j]); // Elimination step
			
			U[num_elements * i + k] = 0; 
		} 
	}	
	return 1;
}
