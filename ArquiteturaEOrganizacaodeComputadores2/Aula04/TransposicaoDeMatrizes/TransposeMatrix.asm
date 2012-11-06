title Transposicao de Matrizes

;Author: @LucasDavid
;
;Revision:

include Irvine32.inc

	M_TAM_X = 3
	M_TAM_Y = 3

.data
	matrizA  byte M_TAM_X *M_TAM_Y dup (?)
	matrizB  byte M_TAM_Y *M_TAM_X dup (?)

	prompt1 byte "Matriz original: ", 0h
	prompt2 byte "Matriz transposta: ", 0h
	prompt3 byte "Insira o ", 0h
	prompt4 byte " valor na matriz: ", 0h
	
	col1 byte "[", 0h
	col2 byte "]", 0h
	space byte " ", 0h

.code

include incMatriz.asm

PreparaAmbiente proc near
	mov	eax, blue +(white * 16)
	call	SetTextColor
	call	Clrscr
	ret
PreparaAmbiente endp

main proc
	
	call PreparaAmbiente

	call PreencheMatriz
	call TranspMatrizEmOutra
	;call TranspMatrizNelaMesma
	call ExibeMatriz
		
	call	WaitMsg			; "Press any key..."	

	exit
main endp

end main