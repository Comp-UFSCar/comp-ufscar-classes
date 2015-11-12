TITLE Data Decl, Version 2         (DataDecl.asm)

; This program declares a series of variables

INCLUDE Irvine32.inc

.data
				    ; AREA 1
B1       BYTE	 250d
B2       SBYTE  -120d
B3       SBYTE  -130d

				    ; AREA 2
BList1   BYTE  0d, 10d, 20d, 30d, 40d
	    BYTE 50d, 60d, 70d, 80d, 90d

				    ; AREA 3
BList2   BYTE  0d, 0d, 0d, 0d, 0d
	    BYTE  0d, 0d, 0d, 0d, 0d

				    ; AREA 4
BList3   BYTE  10 DUP(0d)


				    ; AREA 5
BString   BYTE  "Programa Teste",0

				    ; AREA 6
W1		WORD  3500d

				    ; AREA 7
WList1   WORD    0d, 1000d, 2000d, 3000d, 4000d
	    WORD 5000d, 6000d, 7000d, 8000d, 9000d

				    ; AREA 8
WList2   WORD 20 DUP(?) 



.code
main PROC

					     ; Show AREA 1:
	mov esi, OFFSET B1  	; inicio da area de mem?ria
	mov ecx, 10			; numero de elementos
	mov ebx, BYTE			; tamanho de cada elemento
	call DumpMem

					     ; Show AREA 2:
	mov esi, OFFSET BList1  	; inicio da area de mem?ria
	mov ecx, 10			; numero de elementos
	mov ebx, BYTE			; tamanho de cada elemento
	call DumpMem

						     ; Show AREA 3:
	mov esi, OFFSET BList2  	; inicio da area de mem?ria
	mov ecx, 10			; numero de elementos
	mov ebx, BYTE			; tamanho de cada elemento
	call DumpMem

						     ; Show AREA 4:
	mov esi, OFFSET BList3  	; inicio da area de mem?ria
	mov ecx, 10			; numero de elementos
	mov ebx, BYTE			; tamanho de cada elemento
	call DumpMem

						     ; Show AREA 5:
	mov esi, OFFSET BString 	; inicio da area de mem?ria
	mov ecx, 20			; numero de elementos
	mov ebx, BYTE			; tamanho de cada elemento
	call DumpMem

					     ; Show AREA 6:
	mov esi, OFFSET W1  	; inicio da area de mem?ria
	mov ecx, 10			; numero de elementos
	mov ebx, WORD			; tamanho de cada elemento
	call DumpMem

					     ; Show AREA 7:
	mov esi, OFFSET WList1  	; inicio da area de mem?ria
	mov ecx, 10			; numero de elementos
	mov ebx, WORD			; tamanho de cada elemento
	call DumpMem

						     ; Show AREA 8:
	mov esi, OFFSET WList2  	; inicio da area de mem?ria
	mov ecx, 10			; numero de elementos
	mov ebx, WORD			; tamanho de cada elemento
	call DumpMem

	exit
main ENDP
END main