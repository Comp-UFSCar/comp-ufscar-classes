TITLE MASM Template						(main.asm)

;Author: @CamiloMoreira
;
;Revision:

INCLUDE Irvine32.inc
.data
myMessage BYTE "Hello World MASM!",0dh,0ah,0

.code
main PROC
	call Clrscr

	mov	 edx,OFFSET myMessage
	call WriteString

	exit
main ENDP

END main