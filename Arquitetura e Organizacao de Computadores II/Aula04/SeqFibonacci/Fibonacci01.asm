TITLE Fibonacci Sequence

; Author: @LucasDavid
; 
; Revision:

INCLUDE Irvine32.inc
.data
	fibonacci word 12 dup(?)

.code
main PROC
	mov edx, OFFSET fibonacci
	
	mov ax, 1
	mov bx, 1

	mov fibonacci, ax
	mov fibonacci[2], ax
	mov esi, 4 ;; // posiciona na terceira posicao

	L1:
		add ax, bx
		mov fibonacci[esi], ax
		
		mov cx, bx
		mov bx, ax
		mov ax, cx

		add esi, 2
		cmp esi, LENGTH fibonacci
		je EXT
	
	jmp L1

	EXT:
	exit
main ENDP

END main