TITLE Summing an Array               (SumArray.asm)

; This program sums an array of words.
; Last update: 06/01/2006

INCLUDE Irvine32.inc

.data
intarray WORD 100h,200h,300h,400h

.code
main PROC
	mov ebx, OFFSET intarray

	mov  edi,OFFSET intarray			; address of intarray
	mov  ecx,LENGTHOF intarray		; loop counter
	mov  ax,0						; zero the accumulator
L1:
	add  ax,[edi]					; add an integer
	add  edi,TYPE intarray   		; point to next integer
	loop L1						; repeat until ECX = 0

	exit
main ENDP
END main