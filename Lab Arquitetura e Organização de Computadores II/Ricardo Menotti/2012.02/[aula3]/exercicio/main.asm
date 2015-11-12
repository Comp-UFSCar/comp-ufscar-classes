TITLE MASM Template						(main.asm)

; Description:
; 
; Revision date:

INCLUDE Irvine32.inc
.data

var1 SBYTE -4, -2, 3, 1
var2 WORD 1000h, 2000h, 3000h, 4000h
var3 SWORD -16, -42
var4 DWORD 1, 2, 3, 4, 5

.code

main PROC
	call Clrscr

          ;mov   ax, var1 - precisa ser do mesmo tamanho: 
     mov  al, var1     
     mov   ax, var2
          ;mov   eax, var3
     mov ax, var3
          ;mov   var2, var3 - não pode acessar memoria/memoria
     mov ax, var3
     mov var2, ax
          ;movzx ax, var2
     movzx eax, var2
          ;movzx var2, al
     movzx bx, al
     mov  var2, bx
     mov   ds, ax
          ;mov   ds, 1000h
     mov ax, 1000h
     mov ds, ax
     mov   al, var1
     mov   ah, [var1+3]
     mov   ax, var2
     mov   ax, [var2 +4]
     mov   ax, var3
     mov   ax, [var3-2]
     mov   edx, var4
          ;mov   edx, var2
     mov dx, var2
     mov   edx, [var4+4]
          ;mov   edx, var1
     mov dl, var1

	exit
main ENDP

END main