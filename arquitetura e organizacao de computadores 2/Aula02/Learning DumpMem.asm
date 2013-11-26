TITLE Learning Dump Mem

;Author: @CamiloMoreira
;
;Revision:

INCLUDE Irvine32.inc
.data

    B1 byte 250d
	B2 byte -120d
	B3 byte -130d
	BList1 byte 0, 10, 20, 30, 40, 50, 60, 70, 80, 90
	BList2 byte 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
	BList3 byte 10 dup(0)
	BString byte "Programa Teste"
	W1 word 3500d
	WList1 word 0, 2000, 3000, 4000, 5000, 6000, 7000, 8000, 9000
	WList2 word 10 dup(?)
.code
main PROC

	mov	al, B1   ; al = 250 = -6 (FAh)
	add	al, B2   ; al = -6 -120 = -126 (82h)
	sub	al, B3   ; al = -126 -(-130) = -126 + 130 = 4 (04h)

	mov esi, offset b1
	mov ecx, 100 ; mostrar 50 elementos
	mov ebx, 1 ; mostrar em BYTES
	call	DumpMem

	exit
main ENDP
END main