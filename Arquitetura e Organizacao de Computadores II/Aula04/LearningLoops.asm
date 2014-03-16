TITLE Learning Loops

; Description: @LucasDavid
; 
; Revision:

INCLUDE Irvine32.inc
.data
	count DWORD ?
	
	soma1 DWORD 0
	soma2 DWORD 0

.code
main PROC
	mov ebx, OFFSET soma1
	mov ecx, 10

	L1:
		mov count, ecx
		mov ecx, 5
		mov soma2,0
		
		L2:
			inc soma2
		loop L2

		mov ebx, soma2
		add soma1, ebx
		mov ecx, count
	
	loop L1

	exit
main ENDP

END main