
; // EXIBEMATRIZ PROC NEAR
; // Exibe matrizA e matrizB no console do Windows, utilizando a formatacao matricial correta
; // Data de criacao: 28/10/2012
; // Ultima edicao:   28/10/2012
ExibeMatriz proc near
	
	mov edx, offset prompt1
	call WriteString
	call Crlf
	call Crlf

	mov ebx, 0
	mov ecx, 0
	mov esi, 0
	ExibMA_X:
		ExibMA_Y:
			mov edx, offset space
			call WriteString
			mov edx, offset col1
			call WriteString

			mov eax, 0
						
			mov al, matrizA[esi]
			call WriteInt

			mov edx, offset col2
			call WriteString
		
			add esi, 1
			add ebx, 1
			cmp ebx, M_TAM_Y
			jne ExibMA_Y

		call Crlf

		mov ebx, 0
		add ecx, 1
		cmp ecx, M_TAM_X
		jne ExibMA_X

	call Crlf
	
	mov edx, offset prompt2
	call WriteString
	call Crlf
	call Crlf

	mov ebx, 0
	mov ecx, 0
	mov esi, 0
	ExibMB_X:
		ExibMB_Y:
			mov edx, offset space
			call WriteString
			mov edx, offset col1
			call WriteString

			mov eax, 0
						
			mov al, matrizB[esi]
			call WriteInt

			mov edx, offset col2
			call WriteString
		
			add esi, 1
			add ebx, 1
			cmp ebx, M_TAM_X
			jne ExibMB_X

		call Crlf

		mov ebx, 0
		add ecx, 1
		cmp ecx, M_TAM_Y
		jne ExibMB_Y

	call Crlf

	ret
ExibeMatriz endp

; // PREENCHEMATRIZ PROC NEAR
; // Preenche matrizA com um indexador I, com I variando de 1 aa N
; // Variacao: preenche matrizA com valores inseridos pelo usuario (para utilizar isso, verifique comentario da linha 100)
; // Data de criacao: 28/10/2012
; // Ultima edicao:   29/10/2012
PreencheMatriz proc near
	mov eax, M_TAM_X
	mov edx, M_TAM_Y
	mul edx
	mov ecx, eax
	mov ebx, 0

	LoopPM:
		add ebx, 1

		; /// Para receber os valores do usuario, descomente as linhas [101, 102, ..., 106] e troque de 'bl' em 'mov matriaA[ebx -1], bl' para 'al'
		;mov edx, offset prompt3
		;call WriteString
		;mov eax, ebx ;call ReadInt
		;call WriteInt
		;mov edx, offset prompt4
		;call WriteString

		mov matrizA[ebx -1], bl

		loop LoopPM

	ret
PreencheMatriz endp

; // TRANSPMATRIZEMOUTRA PROC NEAR
; // Transpoe os valores em matrizA para matrizB
; // Data de criacao: 26/10/2012
; // Ultima edicao:   28/10/2012
TranspMatrizEmOutra proc near
	mov ebx, 0
	mov ecx, 0

	LoopTM:
		mov dl, matrizA[ebx]
		mov matrizB[ecx], dl

		add ebx, 1
		add ecx, M_TAM_X

		cmp ecx, eax
		jl PulaAjustaPos

		sub ecx, M_TAM_X *M_TAM_Y
		add ecx, 1

		PulaAjustaPos:
		cmp ebx, eax
		jne LoopTM
	
	ret
TranspMatrizEmOutra endp

; // TRANSPMATIRZNELAMESMA PROC NEAR
; // Transpoe os valores em matrizA nela mesma. Ao final, matrizA contem a matriz transposta em relacao ao conjuto de entradas original
; // Data de criacao: 26/10/2012
; // Ultima edicao:   28/10/2012
TranspMatrizNelaMesma proc near
	mov ebx, M_TAM_X
	mov esi, 0
	mov ecx, 0
	mov edx, 0
	
	lpPosX: ;// for(i = 0; i < M_TAM; i++)
		mov ecx, esi
		add ecx, 1

		lpPosY: ;// for(j = i +1; i < M_TAM_X; i++)
			mov eax, esi ;// eax = i *TAM +j, deslocamento a partir da origem, acha o elemento [i][j]
			mul ebx
			add eax, ecx
			mov edi, eax

			mov eax, ecx ;// eax = j *TAM +i, deslocamento a partir da origem, acha o elemento [j][i]
			mul ebx
			add eax, esi
			
			;// CUIDADO! daqui para baixo EBX nao apresenta mais o valor M_TAM
			mov bl, matrizA[eax]
			mov dl, matrizA[edi]
			mov matrizA[eax], dl
			mov matrizA[edi], bl

			mov ebx, M_TAM_X ; // EBX tem sua integridade reestabelecida

			add ecx, 1
			cmp ecx, M_TAM_Y
			jne lpPosY

		add esi, 1
		cmp esi, M_TAM_X -1
		jne lpPosX
		
	ret
TranspMatrizNelaMesma endp

LerMatriz proc near
	
	ret
LerMatriz endp