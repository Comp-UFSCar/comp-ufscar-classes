TITLE Learning Flags

;Author: @CamiloMoreira
;
;Revision: 

INCLUDE Irvine32.inc
.data

val1 BYTE 10h
val2 WORD 8000h 
val3 DWORD 0FFFFh
val4 WORD 7FFFh

.code
main PROC
    
    inc val2 ; CF=0 ZF=0 SF=1 OF=0 

	mov eax, 0h
	mov ebx, 0h
	mov edx, 0h

	sub eax, val3 ; CF=1 ZF=0 SF=1 OF=0 
	
	mov bx, val2
	sub val4, bx ; CF=1 ZF=0 SF=1 OF=1 

	mov dx, val2
	neg dx ; CF=1 ZF=0 SF=0 OF=0 
	add dx, bx ; CF=1 ZF=1 SF=0 OF=0 
	sub dx, val4 ; CF=1 ZF=0 SF=0 OF=0 
	mov ax, dx


	exit
main ENDP
END main