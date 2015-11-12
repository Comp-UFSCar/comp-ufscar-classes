TITLE Extended Addition Example           (ExtAdd.asm)

; This program calculates the sum of two 64-bit integers.
; Chapter 7 example.
; Last update: 06/01/2006

INCLUDE Irvine32.inc

.data
op1 QWORD 0A2B2A40674981234h
op2 QWORD 08010870000234502h
sum DWORD 3 dup(0FFFFFFFFh) 	; = 0000000122C32B0674BB5736

.code
main PROC

	mov	esi,OFFSET op1		; first operand
	mov	edi,OFFSET op2		; second operand
	mov	ebx,OFFSET sum		; sum operand
	mov	ecx,2           	; number of doublewords
	call	Extended_Add

; Display the sum.
	mov	eax,sum + 8		; display high-order dword
	call	WriteHex
	mov	eax,sum + 4		; display middle dword
	call	WriteHex
	mov	eax,sum			; display low-order dword
	call	WriteHex
	call	Crlf

	exit
main ENDP

;--------------------------------------------------------
Extended_Add PROC
;
; Calculates the sum of two extended integers stored 
; as an array of doublewords.
; Receives: ESI and EDI point to the two integers,
; EBX points to a variable that will hold the sum, and
; ECX indicates the number of doublewords to be added.
; The sum must be one doubleword longer than the
; input operands.
;--------------------------------------------------------
	pushad
	clc                			; clear the Carry flag

L1:	mov	eax,[esi]      		; get the first integer
	adc	eax,[edi]      		; add the second integer
	pushfd              		; save the Carry flag
	mov	[ebx],eax      		; store partial sum
	add	esi,4         			; advance all 3 pointers
	add	edi,4
	add	ebx,4
	popfd               		; restore the Carry flag
	loop	L1           			; repeat the loop

	mov	dword ptr [ebx],0		; clear high dword of sum
	adc	dword ptr [ebx],0		; add any leftover carry
	popad
	ret
Extended_Add ENDP
END main