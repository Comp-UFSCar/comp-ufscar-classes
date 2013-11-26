TITLE Fibonacci Sequence

; Author: @CamiloMoreira
; 
; Revision:

INCLUDE Irvine32.inc
.data
    FIBONACCI WORD 12 DUP(?)
.code
main PROC
    
	mov ax, 0d ;primeiro elemento
	mov bx, 1d ;segundo elemento
	mov dx, 0d ;terceiro elemento = primeiro + segundo

	mov ecx, 12d
	mov esi, 0d

FIB:
   mov FIBONACCI[esi], ax ;armazena um elemento na memória
   mov dx, 0
   add dx, ax
   add dx, bx
   mov ax, bx ;primeiro muda para o segundo
   mov bx, dx ;segundo muda para o terceiro
   add esi, 1d
   loop FIB
   
	exit
main ENDP
END main
