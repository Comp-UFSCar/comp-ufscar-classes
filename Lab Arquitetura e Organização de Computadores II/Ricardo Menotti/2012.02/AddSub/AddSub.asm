TITLE Add and Subtract              (AddSub.asm)

; This program adds and subtracts 32-bit integers.
; Last update: 06/01/2006

INCLUDE Irvine32.inc

.data
	B1	byte	250
	B2	byte	-120 
	B3	byte	-130
	BList1	byte	0,	10,	20,	30,	40,	50,	60,	70,	80,	90
	BList2	byte	0,	0,	0,	0,	0,	0,	0,	0,	0
	BList3	byte 10 DUP(0)
	BString byte "Programa Teste"
	W1 word 3500
	WList1 word 0, 1000, 2000, 3000, 4000, 5000, 6000, 7000, 8000, 9000
	WList2 word 10 DUP(?)

.code
main PROC

	MOV AL, 1
	MOV AH, 2
	MOV AX, 3
	MOV EAX, 4
	MOV ESI, OFFSET B1
	MOV ECX, 100
	MOV EBX, 2
	call	DumpMem

	exit
main ENDP
END main