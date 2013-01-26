TITLE Frogger ARC2 (main.asm)

; Descricao: projeto final da disciplina de Laboratorio de Arquitetura e Organizacao de computadores 2;
; O objetivo deste trabalho ee a implementacao de um jogo em ASM similar aos conhecidos "froggers"

; Data de criacao: 18/12/2012
; Grupo:
; Antonio Pedro Avanzi Nunes - 407852
; Lucas Oliveira David 		 - 407917
; Pedro Padoveze Barbosa 	 - 407895

INCLUDE Irvine32.inc
INCLUDE macros.inc

FROG_SAPO_A	   = 9			 ; define numerico referente ao sapo A!
FROG_SAPO_B = FROG_SAPO_A *2 ; define numerico referente ao sapo B. Deve ser sempre um numero com no minimo o dobro de SAPO_A, a fim de distinguir
							 ; com qual sapo os objetos colidiram (os objetos tem sempre um valor menor que o SAPO_A).
FROG_CAMPO_TAM = 283
FROG_INTRO_TAM = 2000

; Tamanho do campo
FROG_LINHAS	  = 15
FROG_COLUNAS  = 15

; Define a coordenadas (X,Y), onde o campo comecara a ser desenhado
FROG_CAMPO_INI_X 	  = 3
FROG_FROG_CAMPO_INI_Y = 5

; =======================================================================
; PROTOTIPOS

FROG_MovimentaEsq proto, ; FROG_MovimentaEsq: movimenta o sapo "qualSapo" para a casa da esquerda, caso o outro sapo nao esteja ocupando-a
	qualSapo: dword
FROG_MovimentaDir proto, ; FROG_MovimentaDir: movimenta o sapo "qualSapo" para a casa da direita, caso o outro sapo nao esteja ocupando-a
	qualSapo: dword
FROG_MovimentaCima proto, ; FROG_MovimentaEsq: movimenta o sapo "qualSapo" para a casa de cima, caso o outro sapo nao esteja ocupando-a
	qualSapo: dword
FROG_MovimentaBaixo proto, ; FROG_MovimentaEsq: movimenta o sapo "qualSapo" para a casa de baixo, caso o outro sapo nao esteja ocupando-a
	qualSapo: dword

; FIM DOS PROTO
; ========================================================================

.data
	FROG_Campo		word FROG_LINHAS *FROG_COLUNAS dup(0) ; matriz do jogo
	FROG_Campo_Temp byte FROG_CAMPO_TAM dup(0) ; campo auxiliar na recuperacao do arquivo
	FROG_Intro		byte FROG_INTRO_TAM dup(0) ; imagem de introducao
	
	; o jogo ee cooperativo?
	FROG_coop byte 0

	; variaveis referentes ao sapo A
	FROG_sapoA_X	 byte 0
	FROG_sapoA_Y	 byte 0
	FROG_AganhouJogo byte 0
	FROG_AperdeuJogo byte 0
	FROG_A_Vidas	 byte 3

	; variaveis referentes ao sapo B
	FROG_sapoB_X	 byte 0
	FROG_sapoB_Y	 byte 0
	FROG_BganhouJogo byte 0
	FROG_BperdeuJogo byte 0
	FROG_B_Vidas	 byte 3

	FROG_Movimentos byte 0 ; contador de movimentos realizados

	; as tres variaveis abaixo sao utilizadas para recuperar a informacao contida em arquivos.
	FROG_fCampo		BYTE "src/Frogger/campo.txt", 0
	FROG_IntroFile	BYTE "src/Frogger/frogger.txt",0
	FROG_Handle		DWORD ?

	; variavel auxiliar para simular a respiracao do sapo e na movimentacao da agua.
	FROG_respiracao byte 0
	
	; Os quatro vetores seguintes sao utilizados pelo motor de movimentacao do cenario.
	; FROG_TransitoLinha:	armazena quais FROG_LINHAS da matriz contem elementos nocivos ao sapo;
	; FROG_TransitoVeloc:	armazena a velocidade com que os elementos contidos nas FROG_LINHAS
	;					referenciadas por FROG_TransitoLinha andam no cenario;
	; FROG_VelocAtual:		serve como contador para ajustar o delay de velocidade sem perder os
	;					valores de FROG_TransitoVeloc;
	; FROG_TransitoSentido:	armazena o sentido dos elementos contidos em FROG_TransitoLinha
	FROG_TransitoLinha	 word 1, 2, 3, 4, 5, 6, 8, 9, 10, 11, 12, 13
	FROG_TransitoVeloc	 word 3, 4, 4, 3, 3, 3, 4, 4,  2,  3,  1,  4					
	FROG_VelocAtual		 word 3, 4, 4, 3, 3, 3, 4, 4,  4,  3,  3,  4						
	FROG_TransitoSentido word 1, 0, 1, 1, 0, 1, 1, 0,  1,  0,  1,  0
	
.code

; ================================================
; PROCEDIMENTO PRINCIPAL.
; Executa um loop ate que o jogador ganhe, perca ou saia do jogo.
FROG_Clock proc
	mov edx, 0
	mov eax, 1
	
	Atualiza:
	
		call  FROG_VerificarVitoria
		mov al, FROG_AganhouJogo
		mov ah, FROG_BganhouJogo
		
		; Quando ambos os sapos ganham, exibe mensagem de vitoria!
		cmp al, 1
		jne FROG_Clock_NaoGanhou
		cmp ah, 1
		jne FROG_Clock_NaoGanhou
			call FROG_ExibirVitoria
			jmp  FROG_Clock_Finally

		FROG_Clock_NaoGanhou:

		call FROG_DesenharCampo
		mov  eax, 100
		call Delay

		call FROG_ControleMovimento
		call FROG_AtualizarTransito
		call FROG_VerificarColisao
		call FROG_ExibirHUD
		
		cmp FROG_AperdeuJogo, 1
		jne VerificaSeBPerdeu
			dec FROG_A_Vidas
			cmp FROG_A_Vidas, 0
			je FimJogo
			call FROG_NovoJogo
			jmp  FROG_Clock_Finally

		VerificaSeBPerdeu:
		cmp FROG_BperdeuJogo, 1
		jne ContinuaJogo
			dec FROG_B_Vidas
			cmp FROG_B_Vidas, 0
			je FimJogo
			call FROG_NovoJogo
			jmp  FROG_Clock_Finally
		
		FimJogo:
			call FROG_ExibirDerrota
			jmp  FROG_Clock_Finally

		ContinuaJogo:
			cmp eax, 283
			je FROG_Clock_Finally
	jne Atualiza
	
	FROG_Clock_Finally:
	ret
FROG_Clock endp

; Verifica se o sapo colidiu com um carro. Para isso, percorre a matriz inteira, verificando se existe
; algum elemento para qual o valor ee maior que o valor do sapo (considera-se que nenhum outro elemento tem
; valor maior que o sapo). Se sim, houve colisao e a variavel FROG_AperdeuJogo = 1. Caso contrario, nada acontece.
FROG_VerificarColisao proc uses eax ebx ecx edx
	mov ecx, FROG_LINHAS *FROG_COLUNAS
	mov esi, 0

	PercorreCampo:
		movzx eax, FROG_Campo[esi]
		cmp eax, FROG_SAPO_A
		jbe L_VerfCol_Finally

		cmp eax, FROG_SAPO_B
		je L_VerfCol_Finally

		; um valor maior que o valor definido para o sapo A foi encontrado, e ele ee diferente do SAPO_B.
		; Dessa forma, um dos dois (ou ambos) colidiu com alguma coisa. Jogador A ou B (ou A E B) perde o jogo.
		ja ColidiuSapoB

		ColidiuSapoA:
			mov FROG_AperdeuJogo, 1
		jmp L_VerfCol_Finally
		
		ColidiuSapoB:
			mov FROG_BperdeuJogo, 1
		
		L_VerfCol_Finally:
		add esi, type FROG_Campo
	loop PercorreCampo

	ret
FROG_VerificarColisao endp

; Verifica se a posicao vertical do sapo ee 1.
; Se sim, ele esta na primeira linha, o que mostra que este atravessou todo o campo.
; A variavel FROG_AganhouJogo = 1. Caso contrario, nada acontece
FROG_VerificarVitoria proc
	mov ecx, FROG_COLUNAS
	mov esi, 0
	PercorrePrimeiraLin:
		movzx eax, FROG_Campo[esi]
		
		cmp eax, FROG_SAPO_A
		jne Perc_Finally
			mov FROG_AganhouJogo, 1
		Perc_Finally:

		cmp eax, FROG_SAPO_B
		jne Perc_Finally2
			mov FROG_BganhouJogo, 1
			jmp VerificarVitoria_Finally
		Perc_Finally2:

		add esi, type FROG_Campo
	loop PercorrePrimeiraLin
	
	VerificarVitoria_Finally:
	
	ret
FROG_VerificarVitoria endp

; ===================================
; MOVIMENTACAO DO SAPO
;
; Le uma tecla pressionada pelo jogador e, caso essa 
; seja uma seta direcional, movimenta o sapo pelo campo.
;
FROG_ControleMovimento proc
	mov eax, 0
	call ReadKey

	cmp eax, 15360
	jne Left_A
		call FROG_EntrarOnlineB
		je Finally

	Left_A:
	cmp eax, 19200
	jne OutLeft_A
		cmp FROG_AganhouJogo, 1 ; nao movimenta quem ja venceu.
		je Finally

		invoke FROG_MovimentaEsq, FROG_SAPO_A
	OutLeft_A:
	
	cmp eax, 19712
	jne OutRight_A
		cmp FROG_AganhouJogo, 1 ; nao movimenta quem ja venceu.
		je Finally

		invoke FROG_MovimentaDir, FROG_SAPO_A
	OutRight_A:
	
	cmp eax, 18432
	jne OutUp_A
		cmp FROG_AganhouJogo, 1 ; nao movimenta quem ja venceu.
		je Finally

		invoke FROG_MovimentaCima, FROG_SAPO_A
	OutUp_A:
	
	cmp eax, 20480
	jne OutDown_A
		cmp FROG_AganhouJogo, 1 ; nao movimenta quem ja venceu.
		je Finally

		invoke FROG_MovimentaBaixo, FROG_SAPO_A
	OutDown_A:

	cmp eax, 7777
	jne OutLeft_B
		cmp FROG_BganhouJogo, 1 ; nao movimenta quem ja venceu.
		je Finally

		invoke FROG_MovimentaEsq, FROG_SAPO_B
	OutLeft_B:
	
	cmp eax, 8292
	jne OutRight_B
		cmp FROG_BganhouJogo, 1 ; nao movimenta quem ja venceu.
		je Finally

		invoke FROG_MovimentaDir, FROG_SAPO_B
	OutRight_B:
	
	cmp eax, 4471
	jne OutUp_B
		cmp FROG_BganhouJogo, 1 ; nao movimenta quem ja venceu.
		je Finally

		invoke FROG_MovimentaCima, FROG_SAPO_B
	OutUp_B:
	
	cmp eax, 8051
	jne OutDown_B
		cmp FROG_BganhouJogo, 1 ; nao movimenta quem ja venceu.
		je Finally

		invoke FROG_MovimentaBaixo, FROG_SAPO_B
	OutDown_B:

	Finally:
	ret
FROG_ControleMovimento endp

; Os procedimentos abaixo movimentam efetivamente o sapo do campo.
; Elas sao chamadas pelo procedimento FROG_ControleMovimento.
FROG_MovimentaEsq proc,
	qualSapo: dword

	mov eax, qualSapo
	cmp eax, FROG_SAPO_A
	jne MovB_1

	MovA_1:
		movzx eax, FROG_sapoA_X
		movzx ecx, FROG_sapoA_Y
		jmp Finally_1
	MovB_1:
		movzx eax, FROG_sapoB_X
		movzx ecx, FROG_sapoB_Y
	
	Finally_1:
	cmp eax, 1
	je OFFSET_CAMPO

	mov esi, 0
	cmp ecx, 0
	je fim_loop
		mov esi, eax
		l:
			add esi, FROG_LINHAS
		loop l
		shl esi, 1
	fim_loop:

	movzx ecx, FROG_Campo[esi -type FROG_Campo] ; olho para o vizinho, para onde o sapo esta tentando ir

	mov eax, qualSapo
	cmp eax, FROG_SAPO_A ; se sapo A, verifica se o sapo B esta na casa vizinha. Caso contrario, verifica se sapo A esta na casa vizinha.
	jne MovB_2

	MovA_2:
		cmp ecx, FROG_SAPO_B ; impede movimentacao, nao deixa que dois sapos se sobreponham!
		je OFFSET_CAMPO
		dec FROG_sapoA_X
	jmp Finally_2
	MovB_2:
		cmp ecx, FROG_SAPO_A ; impede movimentacao, nao deixa que dois sapos se sobreponham!
		je OFFSET_CAMPO
		dec FROG_sapoB_X
	
	Finally_2:
	mov FROG_Campo[esi], 0
	movzx eax, FROG_Campo[esi -type FROG_Campo]
	add	  eax, qualSapo
	mov byte ptr FROG_Campo[esi -type FROG_Campo], al

	inc FROG_Movimentos
	OFFSET_CAMPO:
	ret
FROG_MovimentaEsq endp

FROG_MovimentaDir proc,
	qualSapo: dword

	mov eax, qualSapo
	cmp eax, FROG_SAPO_A
	jne MovB_1

	MovA_1:
		movzx eax, FROG_sapoA_X
		movzx ecx, FROG_sapoA_Y
		jmp Finally_1
	MovB_1:
		movzx eax, FROG_sapoB_X
		movzx ecx, FROG_sapoB_Y
	
	Finally_1:
	cmp eax, FROG_COLUNAS -2
	je OFFSET_CAMPO

	mov esi, 0
	cmp ecx, 0
	je fim_loop
		mov esi, eax
		l:
			add esi, FROG_LINHAS
		loop l
		shl esi, 1
	fim_loop:

	movzx ecx, FROG_Campo[esi +type FROG_Campo] ; olho para o vizinho, para onde o sapo esta tentando ir

	mov eax, qualSapo
	cmp eax, FROG_SAPO_A ; se sapo A, verifica se o sapo B esta na casa vizinha. Caso contrario, verifica se sapo A esta na casa vizinha.
	jne MovB_2

	MovA_2:
		cmp ecx, FROG_SAPO_B ; impede movimentacao, nao deixa que dois sapos se sobreponham!
		je OFFSET_CAMPO
		inc FROG_sapoA_X

	jmp Finally_2
	MovB_2:
		cmp ecx, FROG_SAPO_A ; impede movimentacao, nao deixa que dois sapos se sobreponham!
		je OFFSET_CAMPO
		inc FROG_sapoB_X
	
	Finally_2:
	mov FROG_Campo[esi], 0
	movzx eax, FROG_Campo[esi +type FROG_Campo]
	add eax, qualSapo
	mov byte ptr FROG_Campo[esi +type FROG_Campo], al

	inc FROG_Movimentos
	OFFSET_CAMPO:	
	ret
FROG_MovimentaDir endp

FROG_MovimentaCima proc,
	qualSapo: dword

	mov eax, qualSapo
	cmp eax, FROG_SAPO_A
	jne MovB_1

	MovA_1:
		movzx eax, FROG_sapoA_X
		movzx ecx, FROG_sapoA_Y
		jmp Finally_1
	MovB_1:
		movzx eax, FROG_sapoB_X
		movzx ecx, FROG_sapoB_Y
	
	Finally_1:
	cmp ecx, 0
	je OFFSET_CAMPO

	mov esi, 0
	cmp ecx, 0
	je fim_loop
		mov esi, eax
		l:
			add esi, FROG_LINHAS
		loop l
		shl esi, 1
	fim_loop:

	movzx ecx, FROG_Campo[esi - (type FROG_Campo)*FROG_COLUNAS] ; olho para o vizinho, para onde o sapo esta tentando ir

	mov eax, qualSapo
	cmp eax, FROG_SAPO_A ; se sapo A, verifica se o sapo B esta na casa vizinha. Caso contrario, verifica se sapo A esta na casa vizinha.
	jne MovB_2

	MovA_2:
		cmp ecx, FROG_SAPO_B ; impede movimentacao, nao deixa que dois sapos se sobreponham!
		je OFFSET_CAMPO
		dec FROG_sapoA_Y

	jmp Finally_2
	MovB_2:
		cmp ecx, FROG_SAPO_A ; impede movimentacao, nao deixa que dois sapos se sobreponham!
		je OFFSET_CAMPO
		dec FROG_sapoB_Y
	
	Finally_2:
	mov FROG_Campo[esi], 0
	movzx eax, FROG_Campo[esi - (type FROG_Campo)*FROG_COLUNAS]
	add	  eax, qualSapo
	mov byte ptr FROG_Campo[esi - (type FROG_Campo)*FROG_COLUNAS], al

	inc FROG_Movimentos
	OFFSET_CAMPO:
	ret
FROG_MovimentaCima endp

FROG_MovimentaBaixo proc,
	qualSapo: dword

	mov eax, qualSapo
	cmp eax, FROG_SAPO_A
	jne MovB_1

	MovA_1:
		movzx eax, FROG_sapoA_X
		movzx ecx, FROG_sapoA_Y
		jmp Finally_1
	MovB_1:
		movzx eax, FROG_sapoB_X
		movzx ecx, FROG_sapoB_Y
	
	Finally_1:
	cmp ecx, FROG_LINHAS -1
	je OFFSET_CAMPO

	mov esi, 0
	cmp ecx, 0
	je fim_loop
		mov esi, eax
		l:
			add esi, FROG_LINHAS
		loop l
		shl esi, 1
	fim_loop:

	movzx ecx, FROG_Campo[esi +(type FROG_Campo)*FROG_COLUNAS] ; olho para o vizinho, para onde o sapo esta tentando ir

	mov eax, qualSapo
	cmp eax, FROG_SAPO_A ; se sapo A, verifica se o sapo B esta na casa vizinha. Caso contrario, verifica se sapo A esta na casa vizinha.
	jne MovB_2

	MovA_2:
		cmp ecx, FROG_SAPO_B ; impede movimentacao, nao deixa que dois sapos se sobreponham!
		je OFFSET_CAMPO
		inc FROG_sapoA_Y

	jmp Finally_2
	MovB_2:
		cmp ecx, FROG_SAPO_A ; impede movimentacao, nao deixa que dois sapos se sobreponham!
		je OFFSET_CAMPO
		inc FROG_sapoB_Y
	
	Finally_2:
	mov FROG_Campo[esi], 0
	movzx eax,	 FROG_Campo[esi +(type FROG_Campo)*FROG_COLUNAS]
	add eax, qualSapo
	mov byte ptr FROG_Campo[esi +(type FROG_Campo)*FROG_COLUNAS], al

	inc FROG_Movimentos
	OFFSET_CAMPO:
	ret
FROG_MovimentaBaixo endp
; FIM DAS FUNCOES DE MOVIMENTACAO
; ===============================

FROG_AtualizarTransito proc
	pushad
	mov ecx, 12
	mov esi, 0
	
	Atualizar:
		mov dx, FROG_VelocAtual        [esi]		;#?determina o delay de ciclos para que a rotaÁ„o seja efetuada
		cmp dx, 0
		
		jne skip
			mov bx, FROG_TransitoLinha[esi]		;#?determina qual linha do tr‚nsito sofrer· rotaÁ„o
			mov ax, FROG_TransitoSentido	  [esi]		;#?determina qual sentido o tr‚nsito est· orientado [1 <- / 0 ->]
			cmp bx, 6
			jna Agua
			call FROG_RotacionarTransito
			jmp QualLinha
		Agua:
			call FROG_RotacionarAgua
		QualLinha:
			mov dx, FROG_TransitoVeloc[esi]
			mov FROG_VelocAtual[esi], dx

		skip:
		dec dx
		mov FROG_VelocAtual[esi], dx
		add esi, type FROG_TransitoLinha
	loop Atualizar
	
	popad
	ret
FROG_AtualizarTransito endp

FROG_RotacionarTransito proc
	pushad
	movzx ecx, bx
	mov esi, 0
	
	cmp ecx, 0
	je l0_finally
		l0:
			add esi, FROG_COLUNAS
		loop l0
	l0_finally:
	shl esi, 1

	mov ecx, FROG_COLUNAS - 1
	
	cmp ax, 1
	jne dir
		mov eax, esi
		add eax, (FROG_COLUNAS -1) *type FROG_Campo 

		; Envia o primeiro elemento da linha 
		; para o ultima posicao desta mesma.

		mov bx, FROG_Campo[esi]
		cmp bx, FROG_SAPO_A
		je AchouSapo_1
		cmp bx, FROG_SAPO_B
		je AchouSapo_1
			mov FROG_Campo[esi], 0
			jmp Sair1
		AchouSapo_1:
			mov bx, 0
		Sair1:
	
		add esi, type FROG_Campo

		LP_RotEsquerda:
			mov dx, FROG_Campo[esi]
			cmp dx, FROG_SAPO_A
			je AchouSapo_2
			cmp dx, FROG_SAPO_B
			je AchouSapo_2

				mov FROG_Campo[esi], 0
				add FROG_Campo[esi - type FROG_Campo], dx
			AchouSapo_2:
				add esi, type FROG_Campo
		loop LP_RotEsquerda
			
		add FROG_Campo[eax], bx
		

	jmp ROT_Finally

	dir:
		mov eax, esi
		add eax, (FROG_COLUNAS -1) *type FROG_Campo
		
		mov bx, FROG_Campo[eax]
		cmp bx, FROG_SAPO_A
		je AchouSapo_3
		cmp bx, FROG_SAPO_B
		je AchouSapo_3
			mov FROG_Campo[eax], 0
			jmp Sair2
		AchouSapo_3:
			mov bx, 0
		Sair2:
		
		add esi, (FROG_COLUNAS -1) *type FROG_Campo
		
		LP_RotDireita:
			mov dx, FROG_Campo[esi -type FROG_Campo]
			cmp dx, FROG_SAPO_A ; rotaciona tanto sapo A quanto B
			jae AchouSapo_4
				mov FROG_Campo[esi -type FROG_Campo], 0
				add FROG_Campo[esi], dx
			AchouSapo_4:
			sub esi, type FROG_Campo
		loop LP_RotDireita

		add FROG_Campo[esi], bx
	
	ROT_Finally:
	popad
	ret
FROG_RotacionarTransito endp

FROG_RotacionarAgua proc
	pushad
	movzx ecx, bx
	mov esi, 0
	
	l0:
		add esi, FROG_COLUNAS
	loop l0
	shl esi, 1

	mov ecx, FROG_COLUNAS -1
	
	cmp ax, 1
	jne dir
		mov eax, esi
		add eax, (FROG_COLUNAS -1) *type FROG_Campo 

		; Envia o primeiro elemento da linha para o ultima posicao desta mesma. Se o sapo estiver nessa posicao, ele morre.
		mov bx, FROG_Campo[esi]
		cmp bx, FROG_SAPO_A
		jne VerificaSeBPerdeu
			mov FROG_AperdeuJogo, 1

		VerificaSeBPerdeu:
		cmp bx, FROG_SAPO_B
		jne NaoAchouSapo
			mov FROG_BperdeuJogo, 1

		NaoAchouSapo:
		mov FROG_Campo[esi], 0
		add esi, type FROG_Campo

		LP_RotEsquerda:
			mov dx, FROG_Campo[esi]
			
			cmp dx, FROG_SAPO_A
			jne NAchouSapo1
				dec FROG_sapoA_x
			NAchouSapo1:

			cmp dx, FROG_SAPO_B
			jne NAchouSapo2
				dec FROG_sapoB_x
			NAchouSapo2:

			mov FROG_Campo[esi], 0
			add FROG_Campo[esi -type FROG_Campo], dx

			add esi, type FROG_Campo
		loop LP_RotEsquerda
		
		add FROG_Campo[eax], bx		

	jmp ROT_Finally
	dir:
		mov eax, esi
		add eax, (FROG_COLUNAS -1) *type FROG_Campo
		
		mov bx, FROG_Campo[eax]
		
		cmp bx, FROG_SAPO_A
		jne VerificaSeBPerdeu2
			mov FROG_AperdeuJogo, 1

		VerificaSeBPerdeu2:
		cmp bx, FROG_SAPO_B
		jne NaoAchouSapo2
			mov FROG_BperdeuJogo, 1
		NaoAchouSapo2:

		mov FROG_Campo[eax], 0
		add esi, (FROG_COLUNAS -1) *type FROG_Campo
		
		LP_RotDireita:
			mov dx, FROG_Campo[esi -type FROG_Campo]
			
			cmp dx, FROG_SAPO_A
			jne NAchouSapo5
				inc FROG_sapoA_x
			NAchouSapo5:

			cmp dx, FROG_SAPO_B
			jne NAchouSapo6
				inc FROG_sapoB_x
			NAchouSapo6:
				
			mov FROG_Campo[esi -type FROG_Campo], 0
			add FROG_Campo[esi], dx

			sub esi, type FROG_Campo
		loop LP_RotDireita

		add FROG_Campo[esi], bx

	ROT_Finally:
	popad
	ret
FROG_RotacionarAgua endp

FROG_DesenharCampo proc
	pushad
	mov edx, 0
	mov dh, FROG_CAMPO_INI_X
	mov dl, FROG_FROG_CAMPO_INI_Y
	call Gotoxy

	mov esi, 0
	mov eax, 0
	
	mov ecx, FROG_LINHAS
	DesenharFROG_LINHAS:
		push ecx
		mov ecx, FROG_COLUNAS
		mov bx, 0

		DesenharFROG_COLUNAS:
			mov ax, FROG_Campo[esi]
			call FROG_DesenharCaracteres
			add esi, type word

		loop DesenharFROG_COLUNAS

		add dh, 1
		mov dl, FROG_FROG_CAMPO_INI_Y
		call Gotoxy
		sub esi, 30
		mov ecx, FROG_COLUNAS
		mov bx, 1

		DesenharFROG_COLUNAS_B:
			mov ax, FROG_Campo[esi]
			call FROG_DesenharCaracteres
			add esi, type word

		loop DesenharFROG_COLUNAS_B

		pop ecx
		add dh, 1
		mov dl, FROG_FROG_CAMPO_INI_Y
		call Gotoxy
	loop DesenharFROG_LINHAS
	
	mov	al, black + (white * 16)
	call SetTextColor
	
	popad		
	ret
FROG_DesenharCampo endp

FROG_DesenharCaracteres proc
	cmp bx, 1

	je SegundaLinha
	cmp ax, FROG_SAPO_A
	je DesenharSapoA_1
	cmp ax, FROG_SAPO_B
	je DesenharSapoB_1
	cmp ax, 0
	je DesenharChao0A
	cmp ax, 1
	je DesenharCarro0A
	cmp ax, 2
	je DesenharCarro1A
	cmp ax, 3
	je DesenharCarro2A
	cmp ax, 4
	je DesenharCarro3A
	cmp ax, 5
	je DesenharCarro4A
	cmp ax, 6
	je DesenharCarro5A
	cmp ax, 7
	je DesenharAgua0A

	cmp ax, FROG_SAPO_A
	jbe SegundaLinha

	; SapoA < ax < SapoB: houve uma colisao com o sapo A! A desenha no campo.
	cmp ax, FROG_SAPO_B
	jb DesenharMortoA_1

	; ax > SapoB: houve uma colisao com o sapo B! A desenha no campo
	ja DesenharMortoB_1
	
	SegundaLinha:
		
	cmp ax, FROG_SAPO_A
	je DesenharSapoA_2
	cmp ax, FROG_SAPO_B
	je DesenharSapoB_2
	cmp ax, 0
	je DesenharChao0B
	cmp ax, 1
	je DesenharCarro0B
	cmp ax, 2
	je DesenharCarro1B
	cmp ax, 3
	je DesenharCarro2B
	cmp ax, 4
	je DesenharCarro3B
	cmp ax, 5
	je DesenharCarro4B
	cmp ax, 6
	je DesenharCarro5B
	cmp ax, 7
	je DesenharAgua0B

	cmp ax, FROG_SAPO_A
	jbe D_Finally

	; SapoA < ax < SapoB: houve uma colisao com o sapo A! A desenha no campo.
	cmp ax, FROG_SAPO_B
	jb DesenharMortoA_2
	; ax > SapoB: houve uma colisao com o sapo B! A desenha no campo
	ja DesenharMortoB_2

	jmp D_Finally ; impede o desenho dos caracteres seguintes...

;------- Sapo A --------
	DesenharSapoA_1:
		mov	al, blue + (lightgreen * 16)
		call SetTextColor
		mWrite "ï ¢"
		jmp D_Finally
	DesenharSapoA_2:
		mov	al, blue + (lightgreen * 16)
		call SetTextColor
		cmp FROG_respiracao, 3
		ja FROG_respiracao_A
			mWrite ")î("
			inc FROG_respiracao
			jmp D_Finally
		FROG_respiracao_A:
			mWrite ")ô("
			inc FROG_respiracao
			cmp FROG_respiracao, 6
			jne D_Finally
			mov FROG_respiracao, 0
			jmp D_Finally

;------- Sapo B --------
	DesenharSapoB_1:
		mov	al, white + (magenta *16)
		call SetTextColor
		mWrite "ï ¢"
		jmp D_Finally
	DesenharSapoB_2:
		mov	al, white + (magenta *16)
		call SetTextColor
		cmp FROG_respiracao, 3
		ja FROG_respiracao_B
			mWrite ")î("
			inc FROG_respiracao
			jmp D_Finally
		FROG_respiracao_B:
			mWrite ")ô("
			inc FROG_respiracao
			cmp FROG_respiracao, 6
			jne D_Finally
			mov FROG_respiracao, 0
		jmp D_Finally
;------- Chao --------
	DesenharChao0A:
		cmp esi, 28*15 - 2
		jna DesenharRuaA
		mov	al, lightgray + (white * 16)
		call SetTextColor
		mWrite "±∞ "
		jmp D_Finally

	DesenharRuaA:
		cmp esi, 16*15 - 2
		jna DesenharChao1A
		mov	al, lightgray + (gray * 16)
		call SetTextColor
		mWrite "±∞≤"
		jmp D_Finally

	DesenharChao1A:
		cmp esi, 14*15 - 2
		jna DesenharMadeira
		mov	al, lightgray + (white * 16)
		call SetTextColor
		mWrite "±∞ "
		jmp D_Finally

	DesenharMadeira:
		cmp esi, 2*15 - 2
		jna DesenharGrama
		mov	al, black + (brown * 16)
		call SetTextColor
		mWrite "ÕÕÕ"
		jmp D_Finally

	DesenharGrama:
		mov	al, Green + (brown * 16)
		call SetTextColor
		mWrite "±∞±"
		jmp D_Finally

	DesenharChao0B:
		cmp esi, 28*15 - 2
		jna DesenharRuaB
		mov	al, lightgray + (white * 16)
		call SetTextColor
		mWrite "∞ ∞"
		jmp D_Finally

	DesenharRuaB:
		cmp esi, 16*15 - 2
		jna DesenharChao1B
		mov	al, lightgray + (gray * 16)
		call SetTextColor
		mWrite "≤±∞"
		jmp D_Finally

	DesenharChao1B:
		cmp esi, 14*15 - 2
		jna DesenharMadeira
		mov	al, lightgray + (white * 16)
		call SetTextColor
		mWrite "∞ ∞"
		jmp D_Finally


;------- Carro 0 --------
	DesenharCarro0A:
		mov	al, white + (blue * 16)
		call SetTextColor
		mWrite "…Õª"
		jmp D_Finally
	DesenharCarro0B:
		mov	al, white + (black * 16)
		call SetTextColor
		mWrite "ÕÕÕ"
		jmp D_Finally
;------- Carro 1 --------
	DesenharCarro1A:
		mov	al, white + (blue * 16)
		call SetTextColor
		mWrite "…Õª"
		jmp D_Finally
	DesenharCarro1B:
		mov	al, white + (black * 16)
		call SetTextColor
		mWrite "»©©"
		jmp D_Finally
;------- Carro 2 --------
	DesenharCarro2A:
		mov	al, blue + (white * 16)
		call SetTextColor
		mWrite "…ÀÕ"
		jmp D_Finally
	DesenharCarro2B:
		mov	al, white + (blue * 16)
		call SetTextColor
		mWrite "Õ∏Õ"
		jmp D_Finally
;------- Carro 3 --------
	DesenharCarro3A:
		mov	al, blue + (white * 16)
		call SetTextColor
		mWrite "ÕÕª"
		jmp D_Finally
	DesenharCarro3B:
		mov	al, white + (blue * 16)
		call SetTextColor
		mWrite "Õ∏ "
		jmp D_Finally
;------- Carro 4 --------
	DesenharCarro4A:
		mov	al,  red + (white * 16)
		call SetTextColor
		mWrite "ÕÕÕ"
		jmp D_Finally
	DesenharCarro4B:
		mov	al, black + (red * 16)
		call SetTextColor
		mWrite "ÕÕÕ"
		jmp D_Finally
;------- Carro 5 --------
	DesenharCarro5A:
		mov	al, gray + (lightcyan * 16)
		call SetTextColor
		mWrite "ÀÕª"
		jmp D_Finally
	DesenharCarro5B:
		mov	al, white + (black * 16)
		call SetTextColor
		mWrite "©©Œ"
		jmp D_Finally
;------- Agua 0 --------
	DesenharAgua0A:
		mov al, cyan +(blue * 16)
		call SetTextColor
	cmp FROG_respiracao, 3
	ja Agua0AChange
		mWrite "∞±∞"
	jmp D_Finally
	Agua0AChange:
		mWrite "±∞∞"
	jmp D_Finally

	DesenharAgua0B:
		mov al, cyan +(blue * 16)
		call SetTextColor
	cmp FROG_respiracao, 3
	ja Agua0BChange
		mWrite "±∞∞"
	jmp D_Finally
	Agua0BChange:
		mWrite "∞±∞"
	jmp D_Finally
	
	;------- Sapo Morto --------
	DesenharMortoA_1:
		mov	al, white + (red * 16)
		call SetTextColor
		mWrite "X X"
		jmp D_Finally
	
	DesenharMortoA_2:
		mov	al, white + (red * 16)
		call SetTextColor
		mWrite ")ô("
		jmp D_Finally
	
	DesenharMortoB_1:
		mov	al, white + (red * 16)
		call SetTextColor
		mWrite "X X"
		jmp D_Finally
	
	DesenharMortoB_2:
		mov	al, white + (red * 16)
		call SetTextColor
		mWrite ")ô("

	D_Finally:
			
	ret
FROG_DesenharCaracteres endp

FROG_ExibirHUD proc
	mov dh, 0
	mov dl, 3
	call Gotoxy
	
	MostraA:
		mWrite "Vidas do sapo A: "
		movzx eax, FROG_A_Vidas
		call WriteDec
	
	cmp FROG_coop, 1
	jne ExibirCoop
		mWrite " Vidas do sapo B: "
		movzx eax, FROG_B_Vidas
		call WriteDec
		jmp ExibirMovimentos

	ExibirCoop:
		mWrite " . F2: um segundo sapo aparecara magicamente!"
	
	ExibirMovimentos:
		mov dh, 2
		mov dl, 3
		call Gotoxy
		
		mWrite "Movimentos: "
		movzx eax, FROG_Movimentos
		call WriteDec

	ret
FROG_ExibirHUD endp

FROG_ExibirVitoria proc
	call FROG_DesenharCampo
	mov	ax, red + (white * 16)
	call SetTextColor

	mov dl, 7
	mov dh, 8
	call Gotoxy
	
	mWrite " V O C E    G A N H O U ! ! ! ! ! ! ! ! ! ! ! ! ! "	
	add dh, 2
	call Gotoxy
	mWrite " Parabens! O sapo conseguiu sobreviver aos terriveis humanos! "
	inc dh
	call Gotoxy
	mWrite " Pressione qualquer tecla para voltar ao menu inicial. "
	call ReadChar
	
	ret
FROG_ExibirVitoria endp

FROG_ExibirDerrota proc
	call FROG_DesenharCampo
	mov	ax, red + (white * 16)
	call SetTextColor
	
	mov dl, 7
	mov dh, 8
	call Gotoxy
	
	mWrite " V O C E   P E R D E U ! ! ! ! ! ! ! ! ! ! ! ! ! "	
	add dh, 2
	call Gotoxy
	mWrite " Que pena! O sapo agora se encontra no plano espiritual. "
	inc dh
	call Gotoxy
	
	mWrite " Pressione qualquer tecla para tentar este incrivel desafio novamente. "
	call ReadChar
	call FROG_InitJogo
	
	ret
FROG_ExibirDerrota endp

FROG_ExibirIntro PROC
	mov edx, OFFSET FROG_IntroFile
	call OpenInputFile
	cmp eax, INVALID_HANDLE_VALUE
	jne Intro_Cont
		ret
	Intro_Cont:
		mov FROG_Handle, eax
		mov edx, OFFSET FROG_Intro
		mov ecx, FROG_INTRO_TAM
		call ReadFromFile
		mov eax, FROG_Handle
		call CloseFile
	
		mov al, black + 16*white
		call SetTextColor

		mov ecx, FROG_INTRO_TAM
		mov esi, 0
		Intro_L:
			mov al, FROG_Intro[esi]
			call WriteChar
			inc esi
		loop Intro_L

		mov eax, 0
		call ReadChar
	ret
FROG_ExibirIntro ENDP

FROG_DefinirCampo PROC
	mov edx, OFFSET FROG_fCampo
	call OpenInputFile
	mov FROG_Handle, eax
	cmp eax, INVALID_HANDLE_VALUE
	jne Definir_Cont
	ret
	Definir_Cont:
	mov edx, OFFSET FROG_Campo_Temp
	mov ecx, FROG_CAMPO_TAM
	call ReadFromFile
	mov eax, FROG_Handle
	call CloseFile

	mov ecx, 15
	mov esi, 0
	mov eax, 0
	TESTE2:
	push ecx
	mov ecx, 15
	mov ebx, 0
	TESTE:
	mov bl, FROG_Campo_Temp[esi]
	sub bl, 48
	mov FROG_Campo[eax], bx
	inc esi
	add eax, 2
	loop TESTE
	add esi, 2
	pop ecx
	loop TESTE2

	ret
FROG_DefinirCampo ENDP

FROG_InitJogo proc
	mov al, black + 16*white
	call SetTextColor
	call Clrscr
	call FROG_ExibirIntro

	mov FROG_coop, 0
	mov FROG_Movimentos, 0
	mov FROG_A_Vidas, 3
	mov FROG_B_Vidas, 3

	call FROG_NovoJogo
	ret
FROG_InitJogo endp

FROG_NovoJogo proc
	call Clrscr

	; o jogador quer sair do jogo, pois pressionou a tecla ESC na tela de intro.
	cmp eax, 283
	je FROG_InitJogo_Finally
	   
	call FROG_DefinirCampo

    mov FROG_AganhouJogo, 0
    mov FROG_AperdeuJogo, 0
	mov FROG_BganhouJogo, 0
    mov FROG_BperdeuJogo, 0

	; posiciona o sapo A no meio da ultima linha
	mov FROG_sapoA_X, (FROG_COLUNAS -1) /2 -1
    mov FROG_sapoA_Y,	FROG_LINHAS -1
	; Posiciona o sapo A na matriz.
	mov edx, 0
	movzx eax, FROG_sapoA_Y
	mov ebx, FROG_COLUNAS
	mul ebx
	movzx ebx, FROG_sapoA_X
	add eax, ebx
	mov FROG_Campo[eax *type FROG_Campo], FROG_SAPO_A
	
	cmp FROG_coop, 0 ; se o jogo nao ee cooperativo, o sapo B nao existe.
	jne FROG_coop_else
		mov FROG_BganhouJogo, 1
		jmp ChamarClock
	FROG_coop_else:
		; posiciona o sapo B ao lado do sapo A
		mov FROG_sapoB_X, (FROG_COLUNAS -1) /2 +1
		mov FROG_sapoB_Y, FROG_LINHAS -1
		; Posiciona o sapo B na matriz.
		mov edx, 0
		movzx eax, FROG_sapoB_Y
		mov ebx, FROG_COLUNAS
		mul ebx
		movzx ebx, FROG_sapoB_X
		add eax, ebx
		mov FROG_Campo[eax *type FROG_Campo], FROG_SAPO_B

		mov FROG_BganhouJogo, 0
		
	ChamarClock:
    call FROG_Clock
    
	FROG_InitJogo_Finally:
	ret
FROG_NovoJogo endp

FROG_EntrarOnlineB proc
	; o sapo B ja esta jogando, nao faz nada.
	cmp FROG_coop, 1
	je Finally
		mov FROG_coop, 1
		call FROG_NovoJogo

	Finally:
	ret
FROG_EntrarOnlineB endp

main PROC
    call FROG_InitJogo

    exit
main ENDP

END main