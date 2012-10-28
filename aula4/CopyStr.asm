TITLE Copying a String                  (CopyStr.asm)

; This program copies a string.
; Last update: 06/01/2006

INCLUDE Irvine32.inc

.data
	source  BYTE  "This is the source string",0
	target  BYTE  SIZEOF source DUP(0),0

.code
main PROC

	mov  esi,0			; index register
	mov  ecx,SIZEOF source	; loop counter
L1:
	mov  al,source[esi]		; get a character from source
	mov  target[esi],al		; store it in the target
	inc  esi				; move to next character
	loop L1				; repeat for entire string

	exit
main ENDP
END main