# ECEC 353 - System Programming
## Programming assignment 1

## Author

* Lucas David - GitHub @lucasdavid - ld492@drexel.edu

## Introduction

Programming assignment developed to the course ECEC 353. The goal of this assignment
is to create a multi-thread program capable of perform gauss-elimination over a matrix
of size `MATRIX_SIZE`.
All the modifications made by the student are inside the file `gauss_elimiate.c`.
The program gives the correct output regardless if `MATRIX_SIZE` is mutiple of
`NUM_THREADS` or not.

## Compilation and execution instructions

The procedure of compilation and execution of the program is the same as described
by the professor in the header of the file `gauss_elimiate.c`:

    $ gcc -o gauss_eliminate gauss_eliminate.c compute_gold.c -lpthread -std=c99 -lm
    $ ./gauss_eliminate

## Required files

* gauss_elimiate.c
* gauss_eliminate.h
* compute_gold.c

## Implementations and modifications

### Constants

    NUM_THREADS 4 : number of threads created by the program

### Struct gauss_attr : used to pass arguments to the threads

    typedef struct {
        Matrix U;
        int i;
        int interval;
        float coeficient;
        int pivot;
    } gauss_attr;

### Functions

 * `void gauss_eliminate_using_pthreads(Matrix)`: performs gauss_reduction_step() and gauss_elimination_step() over all lines of the matrix.
 * `void * gauss_reduction_step  ( void * )`: creates `NUM_THREADS` threads that will divide each element in the current row by the current pivot.
 * `void * gauss_elimination_step( void * )`: creates `NUM_THREADS` threads that will subtract each adjacent row by the current one.

## Report

### Matrix 512 x 512

#### 2 threads

    Performing gaussian elimination using the reference code.
    CPU run time = 0.66 s.
    Single-threaded Gaussian elimination was successful.
    Performing gaussian elimination using pthreads.
    CPU run time = 0.60 s.
    Multi-threaded Gaussian elimination was successful.
    Test PASSED

#### 4 threads

    Performing gaussian elimination using the reference code.
    CPU run time = 0.66 s.
    Single-threaded Gaussian elimination was successful.
    Performing gaussian elimination using pthreads.
    CPU run time = 0.44 s.
    Multi-threaded Gaussian elimination was successful.
    Test PASSED

#### 8 threads

    Performing gaussian elimination using the reference code.
    CPU run time = 0.63 s.
    Single-threaded Gaussian elimination was successful.
    Performing gaussian elimination using pthreads.
    CPU run time = 0.33 s.
    Multi-threaded Gaussian elimination was successful.
    Test PASSED

#### 16 threads

    Performing gaussian elimination using the reference code.
    CPU run time = 0.66 s.
    Single-threaded Gaussian elimination was successful.
    Performing gaussian elimination using pthreads.
    CPU run time = 0.45 s.
    Multi-threaded Gaussian elimination was successful.
    Test PASSED

### Matrix 1024 x 1024

#### 2 threads

    Performing gaussian elimination using the reference code.
    CPU run time = 4.20 s.
    Single-threaded Gaussian elimination was successful.
    Performing gaussian elimination using pthreads.
    CPU run time = 4.81 s.
    Multi-threaded Gaussian elimination was successful.
    Test PASSED

#### 4 threads

    Performing gaussian elimination using the reference code.
    CPU run time = 4.13 s.
    Single-threaded Gaussian elimination was successful.
    Performing gaussian elimination using pthreads.
    CPU run time = 2.70 s.
    Multi-threaded Gaussian elimination was successful.
    Test PASSED

#### 8 threads

    Performing gaussian elimination using the reference code.
    CPU run time = 4.17 s.
    Single-threaded Gaussian elimination was successful.
    Performing gaussian elimination using pthreads.
    CPU run time = 2.21 s.
    Multi-threaded Gaussian elimination was successful.
    Test PASSED

#### 16 threads

    Performing gaussian elimination using the reference code.
    CPU run time = 4.16 s.
    Single-threaded Gaussian elimination was successful.
    Performing gaussian elimination using pthreads.
    CPU run time = 2.48 s.
    Multi-threaded Gaussian elimination was successful.
    Test PASSED

### Matrix 2048 x 2048

#### 2 threads

    Performing gaussian elimination using the reference code.
    CPU run time = 32.14 s.
    Single-threaded Gaussian elimination was successful.
    Performing gaussian elimination using pthreads.
    CPU run time = 31.66 s.
    Multi-threaded Gaussian elimination was successful.
    Test PASSED

#### 4 threads

    Performing gaussian elimination using the reference code.
    CPU run time = 32.15 s.
    Single-threaded Gaussian elimination was successful.
    Performing gaussian elimination using pthreads.
    CPU run time = 21.45 s.
    Multi-threaded Gaussian elimination was successful.
    Test PASSED

#### 8 threads

    Performing gaussian elimination using the reference code.
    CPU run time = 32.09 s.
    Single-threaded Gaussian elimination was successful.
    Performing gaussian elimination using pthreads.
    CPU run time = 21.34 s.
    Multi-threaded Gaussian elimination was successful.
    Test PASSED

#### 16 threads

    Performing gaussian elimination using the reference code.
    CPU run time = 32.26 s.
    Single-threaded Gaussian elimination was successful.
    Performing gaussian elimination using pthreads.
    CPU run time = 16.09 s.
    Multi-threaded Gaussian elimination was successful.
    Test PASSED

### Matrix 4096 x 4096

#### 2 threads

    Performing gaussian elimination using the reference code.
    CPU run time = 255.56 s.
    Single-threaded Gaussian elimination was successful.
    Performing gaussian elimination using pthreads.
    CPU run time = 236.10 s.
    Multi-threaded Gaussian elimination was successful.
    Test PASSED

#### 4 threads

    Performing gaussian elimination using the reference code.
    CPU run time = 255.25 s.
    Single-threaded Gaussian elimination was successful.
    Performing gaussian elimination using pthreads.
    CPU run time = 241.61 s.
    Multi-threaded Gaussian elimination was successful.
    Test PASSED

#### 8 threads

    Performing gaussian elimination using the reference code.
    CPU run time = 255.18 s.
    Single-threaded Gaussian elimination was successful.
    Performing gaussian elimination using pthreads.
    CPU run time = 158.94 s.
    Multi-threaded Gaussian elimination was successful.
    Test PASSED

#### 16 threads

    Performing gaussian elimination using the reference code.
    CPU run time = 258.82 s.
    Single-threaded Gaussian elimination was successful.
    Performing gaussian elimination using pthreads.
    CPU run time = 124.89 s.
    Multi-threaded Gaussian elimination was successful.
    Test PASSED

### Matrix 8192 x 8192

#### 2 threads

    Performing gaussian elimination using the reference code.
    CPU run time = 2070.44 s.
    Single-threaded Gaussian elimination was successful.
    Performing gaussian elimination using pthreads.
    CPU run time = 1839.50 s.
    Multi-threaded Gaussian elimination was successful.
    Test PASSED

#### 4 threads

*Single-threaded gaussian elimination was disabled to avoid time waste. This operation requires aprox. 2070.44 s to finish.*

    Performing gaussian elimination using pthreads.
    CPU run time = 1293.00 s.
    Multi-threaded Gaussian elimination was successful.


#### 8 threads

*Single-threaded gaussian elimination was disabled to avoid time waste. This operation requires aprox. 2070.44 s to finish.*

    Performing gaussian elimination using pthreads.
    CPU run time = 922.06 s.
    Multi-threaded Gaussian elimination was successful.

#### 16 threads

*Single-threaded gaussian elimination was disabled to avoid time waste. This operation requires aprox. 2070.44 s to finish.*

    Performing gaussian elimination using pthreads.
    CPU run time = 924.10 s.
    Multi-threaded Gaussian elimination was successful.
