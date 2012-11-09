TITLE Prova 1 - Laboratorio de Arquitetura e Organizacao de Computadores II - 2012S2
;
; NOME: Lucas Oliveira David
; RA: 407917

INCLUDE Irvine32.inc

CONST_A = 256
CONST_B = 3
CONST_C = 2

.data
	X byte 6, 5, 1, 2, 9, 8, 4, 7, 3 ;1, 1, 1, 1, 1, 1, 1, 1, 1
	Y word 8, 0, 3, 4, 2, 6, 5, 7, 9 ;2, 2, 2, 2, 2, 2, 2, 2, 2
	Z byte 9, 8, 4, 6, 5, 0, 1, 7, 3 ;3, 3, 3, 3, 3, 3, 3, 3, 3
	R DWORD LENGTHOF X DUP(?)
	S WORD ?

.code

main PROC
	
	mov esi, offset X
	mov esi, 0
	mov edx, 0

	PercorreVetores:
		mov eax, 0
		
		mov ecx, CONST_A
		movzx ebx, X[type X *esi]
		
		Xvz256:
			add eax, ebx
			loop Xvz256

		mov ecx, CONST_B
		movzx ebx, Y[type Y *esi]

		Yvz3:
			sub eax, ebx
			loop Yvz3

		mov ecx, CONST_C
		movzx ebx, Z[type Z *esi]

		Zvz2:
			add eax, ebx
			loop Zvz2

		mov R[type R *esi], eax
		add edx, eax

		add esi, 1
		cmp esi, sizeof X
		jl PercorreVetores
	
	mov S, dx

	exit
main ENDP
END main