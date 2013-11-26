TITLE Invert String

;Author: @LucasDavid
;
;Revision:

INCLUDE Irvine32.inc
.data
	string byte "Hey, ooh! Lets go!", 0h
	target byte LENGTH string dup(?)
	
.code
main PROC
	mov esi, OFFSET target
	mov esi, 0
	mov edx, SIZEOF string
	sub edx, 1

	L1:
		mov al, string[edx]
		
		mov target[esi], al
		add esi, 1
		
		cmp edx, 0
		je EXT

		sub edx, 1
	jmp L1

	EXT:
	mov target[esi], 0
	mov eax, 10

	exit
main ENDP

END main