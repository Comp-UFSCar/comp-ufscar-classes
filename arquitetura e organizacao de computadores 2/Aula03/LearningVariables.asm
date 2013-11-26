TITLE Declarando Variaveis

INCLUDE Irvine32.inc
.data

    SecondsInDay = 24*60*60
	myArray WORD 20 DUP(?)
	BytesVetor = ($ - myArray)
	myArray2 WORD 30 DUP(?)
	TamanhoVetor = ($ - myArray2) / TYPE myArray2

.code
main PROC

    mov eax, BytesVetor
	mov ebx, TamanhoVetor
	call DumpRegs
    
	

	exit
main ENDP
END main