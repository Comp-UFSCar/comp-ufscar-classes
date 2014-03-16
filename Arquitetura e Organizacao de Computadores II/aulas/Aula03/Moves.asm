TITLE Data Transfer Examples       (Moves.asm)

; Chapter 4 example. Demonstration of MOV and
; XCHG with direct and direct-offset operands.
; Last update: 06/01/2006

INCLUDE Irvine32.inc
.data
val1  WORD 1000h
val2  WORD 2000h

arrayB BYTE  10h,20h,30h,40h,50h
arrayW WORD  100h,200h,300h
arrayD DWORD 10000h,20000h

.code
main PROC

;  MOVZX - completa os bits que faltam de bx com 0
	mov    bx,0A69Bh
	movzx  eax,bx		; EAX = 0000A69Bh 
	movzx  edx,bl		; EDX = 0000009Bh
	movzx  cx,bl		; CX  = 009Bh

;  MOVSX - completa os bits que faltam de bx com 1
	mov   bx,0A69Bh
	movsx eax,bx		; EAX = FFFFA69Bh 
	movsx edx,bl		; EDX = FFFFFF9Bh
	mov	bl,7Bh
	movsx cx,bl		; CX  = 007Bh

;  Memory-to-memory exchange - troca ax e val2 de valores
	mov  ax,val1		; AX = 1000h
	xchg ax,val2		; AX = 2000h, val2 = 1000h;
	mov  val1,ax		; val1 = 2000h

;  Direct-Offset Addressing (byte array) - possivel somar bytes um endereco para percorrer um vetor de BYTES
	mov al, arrayB	; AL = 10h
	mov al, arrayB+1	; AL = 20h
	mov al, arrayB+2	; AL = 30h

;  Direct-Offset Addressing (word array) - possivel somar 2 bytes de um endereco para percorrer um vetor de WORDS
	mov ax, arrayW		; AX = 100h
	mov ax, arrayW+2	; AX = 200h

;  Direct-Offset Addressing (doubleword array) - possivel somar 4 bytes de um endereco para percorrer um vetor de DWORDS
	mov eax, arrayD				; EAX = 10000h
	mov eax, arrayD+4			; EAX = 20000h
	mov eax, arrayD+TYPE arrayD	; EAX = 20000h ; alem disso, posso usar TYPE [offset] para encontrar o tamanho do tipo

	exit
main ENDP
END main