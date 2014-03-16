TITLE  Addition and Subtraction        (AddSub3.asm)

; Chapter 4 example. Demonstration of ADD, SUB,
; INC, DEC, and NEG instructions, and how
; they affect the CPU status flags.
; Last update: 06/01/2006

INCLUDE Irvine32.inc

.data
Rval   SDWORD ?
Xval   SDWORD 26h
Yval   SDWORD 30h
Zval   SDWORD 40h

.code
main PROC
	; INC and DEC
	mov	ax, 1000h
	inc	ax		; 1001h
	dec	ax		; 1000h

	; Expression: Rval = -Xval + (Yval - Zval)
	mov	eax,Xval
	neg	eax		; -26h
	mov	ebx,Yval
	sub	ebx,Zval	; -10h
	add	eax,ebx
	mov	Rval,eax	; -36h

	; Zero flag example:
	mov	cx,1
	sub	cx,1		; ZF = 1
	mov	ax,0FFFFh
	inc	ax		; ZF = 1

	; Sign flag example:
	mov	cx,0
	sub	cx,1		; SF = 1
	mov	ax,7FFFh
	add	ax,2		; SF = 1

	; Carry flag example: (carry out of the MSB)
	mov	al,0FFh
	add	al,1		; CF = 1,  AL = 00
	mov al,0
	sub al,1        ; CF = 1,  AL = FF


	; Overflow flag example:  (carry out do MSB) xor (carry in para MSB)
	mov	al,+127
	add	al,1		; OF = 1
	mov	al,-128
	sub	al,1		; OF = 1

	exit
main ENDP
END main