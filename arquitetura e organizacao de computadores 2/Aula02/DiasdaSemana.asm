TITLE DiasdaSemana

;Author: @CamiloMoreira
;
;Revision:

INCLUDE Irvine32.inc
.data

    SEGUNDA byte 0d
	TERCA byte 1d
	QUARTA byte 2d
	QUINTA byte 3d
    SEXTA byte 4d
    SABADO byte 5d
    DOMINGO byte 6d
	DIAS dword SEGUNDA, TERCA, QUARTA, QUINTA, SEXTA, SABADO, DOMINGO

.code
main PROC
    
    mov esi, offset DIAS
	mov ebx, 4 ; mostrar em DWORDS
	mov ecx, 8 ; mostrar 8 elementos
	call	DumpMem

	exit
main ENDP
END main