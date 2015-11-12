TITLE MASM Template						(main.asm)

; Description:
; 
; Revision date:

INCLUDE Irvine32.inc
.data
     tamanhoSeq DWORD 12d
.code
main PROC
	call Clrscr

	mov ax, 1d
     mov bx, 1d
     mov ecx, tamanhoSeq 

     seqFibonacci:
          mov dx,bx
          add bx,ax         
          mov ax,dx         
          loop seqFibonacci
     
     

	exit
main ENDP

END main