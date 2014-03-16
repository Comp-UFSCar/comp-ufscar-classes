TITLE Finding Errors

;Author: @CamiloMoreira
;
;Revision: 

INCLUDE Irvine32.inc
.data

var1 SBYTE -4, -2, 3, 1
var2 WORD 1000h, 2000h, 3000h, 4000h
var3 SWORD -16, -42
var4 DWORD 1, 2, 3, 4, 5

.code
main PROC

;mov   ax, var1
mov   ax, var2
;mov eax, var3
;mov   var2, var3 - nao posso mover de memoria a memoria
;movzx ax, var2 - mesmo tamanho, nao precisa estender
;movzx var2, al - não posso estender na memoria
;mov   ds, ax - nao posso mudar o data segment, pois estarei perdendo o endereco dos dados. Apenas o SO pode
;mov   ds, 1000h - nao posso atribuir um imediato ao ds
mov   al, var1 
mov   ah, var1+3
mov   ax, var2
mov   ax, var2 +4 ;posso mover 2 words para frente
mov   ax, var3
;mov   ax, var3-2 - estou movendo para antes do vetor var3
mov   edx, var4
;mov   edx, var2
mov   edx, var4+4 ;posso mover 1 dwords para frente
;mov   edx, var1

	exit
main ENDP
END main