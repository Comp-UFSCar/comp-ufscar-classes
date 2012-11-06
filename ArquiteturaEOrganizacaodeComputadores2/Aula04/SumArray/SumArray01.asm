TITLE Summing a array

; Author: @LucasDavid
; 
; Revision:

INCLUDE Irvine32.inc
.data
	vetor1 DWORD 10, 20, 30

.code
main PROC
	mov eax, 0
	mov esi, 0
	
	LP:
		add eax, vetor1[esi]
		
		add esi, type vetor1
		cmp esi, 3 *type vetor1
		jne LP
	
	exit
main ENDP

END main