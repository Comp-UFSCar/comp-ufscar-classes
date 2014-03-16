/* Gaussian elimination code.
 * Author: Naga Kandasamy
 * Date: 02/07/2014
 * Compile as follows: gcc -o gauss_eliminate gauss_eliminate.c compute_gold.c -lpthread -std=c99 -lm
 */

#include <stdlib.h>
#include <stdio.h>
#include <time.h>
#include <sys/types.h>
#include <sys/time.h>
#include <string.h>
#include <math.h>
#include <pthread.h>
#include "gauss_eliminate.h"

#define MIN_NUMBER 2
#define MAX_NUMBER 50
#define NUM_THREADS 8

// declarations, forward
extern int compute_gold(float*, unsigned int);
Matrix allocate_matrix(int num_rows, int num_columns, int init);
void gauss_eliminate_using_pthreads(Matrix);
int perform_simple_check(const Matrix);
void print_matrix(const Matrix);
float get_random_number(int, int);
int check_results(float *, float *, unsigned int, float);

void * gauss_reduction_step  ( void * );
void * gauss_elimination_step( void * );

typedef struct {
    Matrix U;
    int i;
    int interval;
    float coeficient;
    int pivot;
} gauss_attr;

////////////////////////////////////////////////////////////////////////////////
// Program main
////////////////////////////////////////////////////////////////////////////////
int main(int argc, char** argv) {

    // Matrices for the program
    Matrix  A; // The input matrix
    Matrix  U_reference; // The upper triangular matrix computed by the reference code
    Matrix  U_mt; // The upper triangular matric computed by the pthread code
    
    // Initialize the random number generator with a seed value 
    srand(time(NULL));
    
    // Check command line arguments
    if(argc > 1){
        printf("Error. This program accepts no arguments. \n");
        exit(0);
    }       
     
    // Allocate and initialize the matrices
    A  = allocate_matrix(MATRIX_SIZE, MATRIX_SIZE, 1); // Allocate and populate a random square matrix
    U_reference = allocate_matrix(MATRIX_SIZE, MATRIX_SIZE, 0);  // Allocate space for the reference result
    U_mt = allocate_matrix(MATRIX_SIZE, MATRIX_SIZE, 0); // Allocate space for the multi-threaded result

    // Copy the contents of the A matrix into the U matrices
    for (int i = 0; i < A.num_rows; i ++){
        for(int j = 0; j < A.num_rows; j++){
            U_reference.elements[A.num_rows*i + j] = A.elements[A.num_rows*i + j];
            U_mt.elements[A.num_rows*i + j] = A.elements[A.num_rows*i + j];
        }
    }

    // printf("Performing gaussian elimination using the reference code. \n");
    struct timeval start, stop; 
    gettimeofday(&start, NULL);
    int status = compute_gold(U_reference.elements, A.num_rows);
    gettimeofday(&stop, NULL);
    printf("CPU run time = %0.2f s. \n", (float)(stop.tv_sec - start.tv_sec + (stop.tv_usec - start.tv_usec)/(float)1000000));

    if(status == 0){
        printf("Failed to convert given matrix to upper triangular. Try again. Exiting. \n");
        exit(0);
    }
    status = perform_simple_check(U_reference); // Check that the principal diagonal elements are 1 
    if(status == 0){
        printf("The upper triangular matrix is incorrect. Exiting. \n");
        exit(0); 
    }
    printf("Single-threaded Gaussian elimination was successful. \n");

    /* MODIFY THIS CODE: Perform the Gaussian elimination using the multi-threaded version. The resulting upper triangular matrix should be returned in U_mt */
    printf("Performing gaussian elimination using pthreads. \n");
    gettimeofday(&start, NULL);
    gauss_eliminate_using_pthreads(U_mt);
    gettimeofday(&stop, NULL);
    printf("CPU run time = %0.2f s. \n", (float)(stop.tv_sec - start.tv_sec + (stop.tv_usec - start.tv_usec)/(float)1000000));
    printf("Multi-threaded Gaussian elimination was successful. \n");

    // check if the pthread result is equivalent to the expected solution within a specified tolerance
    int size = MATRIX_SIZE*MATRIX_SIZE;
    int res = check_results(U_reference.elements, U_mt.elements, size, 0.001f);
    printf("Test %s\n", (1 == res) ? "PASSED" : "FAILED");

    // Free host matrices
    free(A.elements);
    free(U_reference.elements); 
    free(U_mt.elements);

    return 0;
}

/* Write code to perform gaussian elimination using pthreads.
 *
 * Lucas David
 * ld492@drexel.edu
 *
 */
void gauss_eliminate_using_pthreads(Matrix U)
{
    int            i, pivot;
    pthread_t      tid[NUM_THREADS];
    pthread_attr_t attr[NUM_THREADS];
    gauss_attr     * params;
    
    for ( pivot = 0; pivot < MATRIX_SIZE; pivot++ )
    {
        // find the coeficient used to reduce a row ( U[pivot][pivot] )
        float coeficient = U.elements[pivot * U.num_columns + pivot];
        
        if ( coeficient == 0 )
        {
            printf("Numerical instability detected. The principal diagonal element is zero. \n");
            return;
        }
        
        // initiate all reduction threads
        for ( i = 0; i < NUM_THREADS; i++ )
        {
            params = (gauss_attr *) malloc(sizeof(gauss_attr));

            params->U     = U;
            params->pivot = pivot;
            // ceil function is required when MATRIX_SIZE is not multiple of NUM_THREADS
            params->i        = i * ceil((float)MATRIX_SIZE / NUM_THREADS);
            params->interval = ceil((float)MATRIX_SIZE / NUM_THREADS);
            params->coeficient = coeficient;
            
            pthread_attr_init(&attr[i]);
            pthread_create(&tid[i], &attr[i], gauss_reduction_step, params);
        }

        // join all of them
        for ( i = 0; i < NUM_THREADS; i++ )
            pthread_join(tid[i], NULL);

        // initiate all elimination threads
        for ( i = 0; i < NUM_THREADS; i++ )
        {
            params = (gauss_attr *) malloc(sizeof(gauss_attr));
            
            params->U     = U;
            params->pivot = pivot;
            // ceil function is required when MATRIX_SIZE is not multiple of NUM_THREADS
            params->i     = i * ceil((float)MATRIX_SIZE / NUM_THREADS);
            params->interval = ceil((float)MATRIX_SIZE / NUM_THREADS);
            
            pthread_attr_init(&attr[i]);
            pthread_create(&tid[i], &attr[i], gauss_elimination_step, params);
        }
        
        // join all of them
        for ( i = 0; i < NUM_THREADS; i++ )
            pthread_join(tid[i], NULL);
    }
}

void * gauss_reduction_step( void *_params )
{
    gauss_attr *params = (gauss_attr *) _params;
    Matrix U = params->U;
    
    int i = params->i;
    int interval = params->interval;
    
    int   pivot = params->pivot;
    float coeficient = params->coeficient;
    
    // for each row element in the interval given to this thread, reduce it.
    //  && j < MATRIX_SIZE is required because we used the function ceil on line 140
    for ( int j = i; j < i + interval && j < MATRIX_SIZE; j++ )
        U.elements[pivot * U.num_columns + j] /= coeficient;

    free (params);
    pthread_exit(NULL);
}

void * gauss_elimination_step( void *_params )
{
    gauss_attr *params = (gauss_attr *) _params;
    Matrix U = params->U;
    int line = params->i;
    int interval = params->interval;
    int pivot = params->pivot;
    
    int i, j;
    
    // for each line given to this thread...
    //  && j < MATRIX_SIZE is required because we used the function ceil on line 160
    for ( i = line; i < line + interval && i < MATRIX_SIZE; i++ ) {
        
        // ignore all lines above or equal the pivot line, considering this one should not be eliminated
        if ( i <= pivot ) continue;
        
        // finding the coeficient which should be used to eliminate that row
        float coeficient = U.elements[i * U.num_columns + pivot];
        
        // for each element in this line, eliminate it.
        for ( j = 0; j < MATRIX_SIZE; j++ ) {
            U.elements[i * U.num_columns + j] -= coeficient * U.elements[pivot * U.num_columns + j];
        }
    }

    free (params);
    pthread_exit(NULL);
}

/* Function checks if the results generated by the single threaded and multi threaded versions match. */
int check_results(float *A, float *B, unsigned int size, float tolerance)
{
    for(int i = 0; i < size; i++)
        if(fabsf(A[i] - B[i]) > tolerance)
            return 0;
    return 1;
}


// Allocate a matrix of dimensions height*width
//  If init == 0, initialize to all zeroes.  
//  If init == 1, perform random initialization.
Matrix allocate_matrix(int num_rows, int num_columns, int init){
        Matrix M;
        M.num_columns = M.pitch = num_columns;
        M.num_rows = num_rows;
        int size = M.num_rows * M.num_columns;
        
    M.elements = (float*) malloc(size*sizeof(float));
    for(unsigned int i = 0; i < size; i++){
        if(init == 0) M.elements[i] = 0; 
        else
            M.elements[i] = get_random_number(MIN_NUMBER, MAX_NUMBER);
    }
    return M;
}   


// Returns a random floating-point number between the specified min and max values 
float get_random_number(int min, int max){
    return (float)floor((double)(min + (max - min + 1)*((float)rand()/(float)RAND_MAX)));
}

// Performs a simple check on the upper triangular matrix. Checks to see if the principal diagonal elements are 1
int perform_simple_check(const Matrix M){
    for(unsigned int i = 0; i < M.num_rows; i++)
            if((fabs(M.elements[M.num_rows*i + i] - 1.0)) > 0.001) return 0;
    return 1;
} 
