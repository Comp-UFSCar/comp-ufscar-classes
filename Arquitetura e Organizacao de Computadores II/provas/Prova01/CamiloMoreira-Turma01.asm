TITLE Prova 1 - Laboratorio de Arquitetura e Organizacao de Computadores II - 2012S2

;Author: @CamiloMoreira
;
;Revision:

INCLUDE Irvine32.inc

.data
    X DWORD 2, 9, 8, 4, 7, 6, 5, 0, 1, 3
	Y DWORD 4, 2, 6, 5, 7, 8, 0, 1, 3, 9
	Z DWORD 6, 5, 0, 1, 7, 9, 8, 4, 2, 3
	R DWORD LENGTHOF X DUP(?)
	S DWORD ?
.code

main PROC

	; R = 256*X + 2*Y - 3*Z

	mov esi, 0
	mov ecx, LENGTHOF X
L1:
    mov eax, X[esi]
	mov edx, ecx
	mov ecx, 8
M256: ;multiplicando por 256
   add eax, eax ; eax = 2eax = 4eax = ... = 256eax
   loop M256

   mov ebx, Y[esi]
   add eax, ebx
   add eax, ebx

   mov ebx, Z[esi]
   sub eax, ebx
   sub eax, ebx
   sub eax, ebx

   mov R[esi], eax
   add esi, TYPE X
   mov ecx, edx
   loop L1

   ; somar em S

   mov ecx, 10
   mov esi, 0
   mov eax, 0
L2:
   add eax, R[esi]
   add esi, TYPE X
   loop L2

   mov S, eax

	exit
main ENDP

END main