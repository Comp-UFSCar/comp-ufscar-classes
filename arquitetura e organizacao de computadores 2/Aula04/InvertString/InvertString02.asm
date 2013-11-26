TITLE Invert String

;Author: @CamiloMoreira
;
;Revision:

INCLUDE Irvine32.inc

.data

origem BYTE "String de Origem",0
destino BYTE SIZEOF origem DUP('#')

.code
main PROC

	mov esi, 0 ; index register
	mov ecx, SIZEOF origem	; loop counter
	sub ecx, 1 ; para nao copiar o ultimo
L1:
	mov  al, origem[ecx - 1]		
	mov  destino[esi],al
	inc  esi			
	loop L1				
	
	mov destino[esi], 0 ;colocando \0

	exit
main ENDP
END main