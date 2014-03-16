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

FROG_SAPO_A	= 9				 ; define numerico referente ao sapo A!
FROG_SAPO_B = FROG_SAPO_A *2 ; define numerico referente ao sapo B. Deve ser sempre um numero com no minimo o dobro de SAPO_A, a fim de distinguir
							 ; com qual sapo os objetos colidiram (os objetos tem sempre um valor menor que o SAPO_A).
FROG_CAMPO_TAM = 648
FROG_INTRO_TAM = 1600

; Tamanho do campo
FROG_LINHAS	  = 15
FROG_COLUNAS  = 15

; Define a coordenadas (X,Y), onde o campo comecara a ser desenhado
FROG_CAMPO_INI_X = 3
FROG_CAMPO_INI_Y = 5

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
	FROG_Campo		word FROG_LINHAS *FROG_COLUNAS dup(0)
	FROG_Campo_Temp byte FROG_CAMPO_TAM dup(0)
	FROG_Intro		byte FROG_INTRO_TAM dup(0)
	
	; variaveis referentes ao sapo A
	FROG_sapoA_X	 byte 0
	FROG_sapoA_Y	 byte 0
	FROG_ApassouFase byte 0
	FROG_AperdeuJogo byte 0
	FROG_A_Vidas	 byte 3
	
	; variaveis referentes ao sapo B
	FROG_sapoB_X	 byte 0
	FROG_sapoB_Y	 byte 0
	FROG_BpassouFase byte 0
	FROG_BperdeuJogo byte 0
	FROG_B_Vidas	 byte 3
	
	FROG_ganharamJogo byte 0

	FROG_resetJogo byte 0 ; quando ativo, simplesmente reseta o jogo
	FROG_coop byte 0		; o jogo ee cooperativo?
	FROG_Movimentos byte 0	; contador de movimentos realizados (por ambos os sapos, quando no modo cooperativo)
	FROG_Movimentos_Total byte 0 ; 

	; as tres variaveis abaixo sao utilizadas para recuperar a informacao contida em arquivos
	FROG_fCampo	 byte "src/Frogger/campo00.txt", 0
	FROG_fIntro  byte "src/Frogger/frogger.txt",0
	FROG_fHandle dword ?

	; variavel auxiliar para simular a respiracao do sapo e na movimentacao da agua.
	FROG_respiracao byte 0
	
	; Os quatro vetores seguintes sao utilizados pelo motor de movimentacao do cenario.
	; FROG_TransitoLinha:	armazena quais FROG_LINHAS da matriz contem elementos nocivos ao sapo;
	; FROG_TransitoVeloc:	armazena a velocidade com que os elementos contidos nas FROG_LINHAS referenciadas por FROG_TransitoLinha andam no cenario;
	; FROG_VelocAtual:		serve como contador para ajustar o delay de velocidade sem perder os valores de FROG_TransitoVeloc;
	; FROG_TransitoSentido:	armazena o sentido dos elementos contidos em FROG_TransitoLinha;
	FROG_TransitoLinha	 word 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14
	FROG_TransitoVeloc	 word 15 dup(0)			
	FROG_VelocAtual		 word 15 dup(0)				
	FROG_TransitoSentido word 15 dup(0)
	
.code

; ================================================
; PROCEDIMENTO PRINCIPAL.
; Executa um loop ate que o jogador ganhe, perca ou saia do jogo.
FROG_Clock proc USES EDX
	mov edx, 0
	mov eax, 1
	
	Atualiza:	
		call  FROG_VerificarVitoria
		mov al, FROG_ApassouFase
		and al, FROG_BpassouFase
		
		cmp al, 1 ; Quando ambos os sapos ganham, exibe mensagem de vitoria!
		jne Clock_NaoGanhou
			call FROG_NovoCampo
			cmp FROG_ganharamJogo, 1
			jne NovaFase
			call FROG_ExibirVitoria
			jmp  FROG_Clock_Finally
			NovaFase:
			mov dl, FROG_Movimentos
			mov FROG_Movimentos_Total, dl
			call FROG_EntreFases
			call FROG_NovoJogo
			jmp  FROG_Clock_Finally
		Clock_NaoGanhou:

		call FROG_DesenharCampo
		mov  eax, 20
		call Delay

		call FROG_ControleMovimento
		push eax
		call FROG_AtualizarTransito
		call FROG_VerificarColisao
		call FROG_ExibirHUD
		pop eax
		cmp FROG_AperdeuJogo, 1
		jne VerificaSeBPerdeu
			dec FROG_A_Vidas
			cmp FROG_A_Vidas, 0
			je SemEnterA
			call FROG_DesenharCampo
			call FROG_PressEnter
			SemEnterA:
			mov dl, FROG_Movimentos_Total
			mov FROG_Movimentos, dl
			cmp FROG_A_Vidas, 0
			je FimJogo
			call FROG_NovoJogo
			jmp  FROG_Clock_Finally

		VerificaSeBPerdeu:
		cmp FROG_BperdeuJogo, 1
		jne ContinuaJogo
			dec FROG_B_Vidas
			call FROG_DesenharCampo
			mov dl, FROG_Movimentos_Total
			mov FROG_Movimentos, dl
			cmp FROG_B_Vidas, 0
			je FimJogo
			call FROG_NovoJogo
			jmp  FROG_Clock_Finally
		
		FimJogo:
			call FROG_DesenharCampo
			call FROG_PressEnter
			call FROG_ExibirDerrota
			jmp  FROG_Clock_Finally

		ContinuaJogo:
			cmp eax, 283
			je FROG_Clock_Finally

	cmp FROG_resetJogo, 1
	jne Atualiza

	FROG_Clock_Finally:
	ret
FROG_Clock endp

; Verifica se o sapo colidiu com um carro. Para isso, percorre a matriz inteira, verificando se existe
; algum elemento para qual o valor ee maior que o valor do sapo (considera-se que nenhum outro elemento tem
; valor maior que o sapo). Se sim, houve colisao e a variavel FROG_AperdeuJogo = 1. Caso contrario, nada acontece.
FROG_VerificarColisao proc
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
; A variavel FROG_ApassouFase = 1. Caso contrario, nada acontece.
FROG_VerificarVitoria proc
	mov ecx, FROG_COLUNAS
	mov esi, 0
	PercorrePrimeiraLin:
		movzx eax, FROG_Campo[esi]
		
		cmp eax, FROG_SAPO_A
		jne Perc_Finally
			mov FROG_ApassouFase, 1
		Perc_Finally:

		cmp eax, FROG_SAPO_B
		jne Perc_Finally2
			mov FROG_BpassouFase, 1
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
		cmp FROG_ApassouFase, 1 ; nao movimenta quem ja venceu.
		je Finally

		invoke FROG_MovimentaEsq, FROG_SAPO_A
	OutLeft_A:
	
	cmp eax, 19712
	jne OutRight_A
		cmp FROG_ApassouFase, 1 ; nao movimenta quem ja venceu.
		je Finally

		invoke FROG_MovimentaDir, FROG_SAPO_A
	OutRight_A:
	
	cmp eax, 18432
	jne OutUp_A
		cmp FROG_ApassouFase, 1 ; nao movimenta quem ja venceu.
		je Finally

		invoke FROG_MovimentaCima, FROG_SAPO_A
	OutUp_A:
	
	cmp eax, 20480
	jne OutDown_A
		cmp FROG_ApassouFase, 1 ; nao movimenta quem ja venceu.
		je Finally

		invoke FROG_MovimentaBaixo, FROG_SAPO_A
	OutDown_A:

	cmp eax, 7777
	jne OutLeft_B
		cmp FROG_BpassouFase, 1 ; nao movimenta quem ja venceu.
		je Finally

		invoke FROG_MovimentaEsq, FROG_SAPO_B
	OutLeft_B:
	
	cmp eax, 8292
	jne OutRight_B
		cmp FROG_BpassouFase, 1 ; nao movimenta quem ja venceu.
		je Finally

		invoke FROG_MovimentaDir, FROG_SAPO_B
	OutRight_B:
	
	cmp eax, 4471
	jne OutUp_B
		cmp FROG_BpassouFase, 1 ; nao movimenta quem ja venceu.
		je Finally

		invoke FROG_MovimentaCima, FROG_SAPO_B
	OutUp_B:
	
	cmp eax, 8051
	jne OutDown_B
		cmp FROG_BpassouFase, 1 ; nao movimenta quem ja venceu.
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
	cmp eax, 0
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
	cmp eax, FROG_COLUNAS -1
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
	mov ecx, 15
	mov esi, 0
	
	Atualizar:
		cmp FROG_TransitoSentido[esi], 35
		je NAOMOVER
		mov dx, FROG_VelocAtual[esi]		;#?determina o delay de ciclos para que a rotação seja efetuada
		cmp dx, 0
		
		jne skip
			mov bx, FROG_TransitoLinha[esi]		;#?determina qual linha do trânsito sofrerá rotação
			mov ax, FROG_TransitoSentido	  [esi]		;#?determina qual sentido o trânsito está orientado [ímpar <- / par ->]
			shr ax, 1
			shl ax, 15
			shr ax, 15
			cmp ax, 1
			je Agua 
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
		NAOMOVER:
		add esi, type FROG_TransitoLinha
	loop Atualizar
	ret
FROG_AtualizarTransito endp

FROG_RotacionarTransito proc
	pushad

	mov ax, FROG_TransitoSentido	  [esi]		;#?determina qual sentido o trânsito está orientado [ímpar <- / par ->]
	shl ax, 15
	shr ax, 15

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

	mov ax, FROG_TransitoSentido	  [esi]		;#?determina qual sentido o trânsito está orientado [ímpar <- / par ->]
	shl ax, 15
	shr ax, 15

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
			mov bx, 0

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
				dec FROG_sapoA_X
			NAchouSapo1:

			cmp dx, FROG_SAPO_B
			jne NAchouSapo2
				dec FROG_sapoB_X
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
		jne NaoAchouSapo3
			mov FROG_BperdeuJogo, 1
		NaoAchouSapo3:

		mov FROG_Campo[eax], 0
		add esi, (FROG_COLUNAS -1) *type FROG_Campo
		
		LP_RotDireita:
			mov dx, FROG_Campo[esi -type FROG_Campo]
			
			cmp dx, FROG_SAPO_A
			jne NAchouSapo4
				inc FROG_sapoA_X
			NAchouSapo4:

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

; =================================================
; Procedimento: FROG_DesenharCampo
; Descricao: parte grafica do jogo.
FROG_DesenharCampo proc
	pushad

	mov edx, 0
	mov dh, FROG_CAMPO_INI_X
	mov dl, FROG_CAMPO_INI_Y
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
		mov dl, FROG_CAMPO_INI_Y
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
		mov dl, FROG_CAMPO_INI_Y
		call Gotoxy
	loop DesenharFROG_LINHAS
	
	popad		
	ret
FROG_DesenharCampo endp

; =================================================
; Procedimento: FROG_DesenharCaracteres
; Descricao: parte grafica do jogo. Aplica as texturas dos objetos no campo.
FROG_DesenharCaracteres PROC USES EDX ECX

	cmp ax, 0
	jne NaoEhChao

	mov edx, 0
	mov ecx, esi
ProcurarLinha:
	cmp ecx, 30
	jb AchouLinha
	inc edx
	sub ecx, 30
	jmp ProcurarLinha
AchouLinha:

	shl edx, 5
	add edx, 48

		mov al, FROG_Campo_Temp[edx]
		shl al, 4
		inc edx
		add al, FROG_Campo_Temp[edx]
		call SetTextColor
		sub edx, 5
		cmp bx, 1
		jne Chao_SegundaLinha
		add edx, 7
	Chao_SegundaLinha:
		mov al, FROG_Campo_Temp[edx]
		call writeChar
		inc edx
		mov al, FROG_Campo_Temp[edx]
		call writeChar
		inc edx
		mov al, FROG_Campo_Temp[edx]
		call writeChar
		jmp D_Finally

	NaoEhChao:

	cmp bx, 1
	je SegundaLinha

	cmp ax, FROG_SAPO_A
	je DesenharSapoA_1
	cmp ax, FROG_SAPO_B
	je DesenharSapoB_1

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

	cmp ax, FROG_SAPO_A
	jbe Continue

	; SapoA < ax < SapoB: houve uma colisao com o sapo A! A desenha no campo.
	cmp ax, FROG_SAPO_B
	jb DesenharMortoA_2
	; ax > SapoB: houve uma colisao com o sapo B! A desenha no campo
	ja DesenharMortoB_2

	Continue:

	mov edx, 0
	mov dx, ax
	shl dx, 4
	mov cx, bx
	shl cx, 3
	add dx, cx
	cmp ax, 7
	jb BXCHECK
	mov cx, bx
	shl cx, 2
	add dx, cx
	BXCHECK:
	cmp ax, 8
	jne ACHECK
	add dx, 8
	ACHECK:
	
	add dx, 494

		mov al, FROG_Campo_Temp[edx]
		shl al, 4
		inc edx
		add al, FROG_Campo_Temp[edx]
		call SetTextColor
		sub edx, 5
		cmp FROG_respiracao, 3
		jb RESPIRACAO
		cmp dx, 600
		jb RESPIRACAO
		add dx, 7
		RESPIRACAO:
		mov al, FROG_Campo_Temp[edx]
		call writeChar
		inc edx
		mov al, FROG_Campo_Temp[edx]
		call writeChar
		inc edx
		mov al, FROG_Campo_Temp[edx]
		call writeChar
		jmp D_Finally

;------- Sapo A --------
	DesenharSapoA_1:
		mov	al, blue + (lightgreen * 16)
		call SetTextColor
		mWrite "¢ "
		jmp D_Finally
	DesenharSapoA_2:
		mov	al, blue + (lightgreen * 16)
		call SetTextColor
		cmp FROG_respiracao, 3
		ja FROG_respiracao_A
			mWrite ")("
			inc FROG_respiracao
			jmp D_Finally
		FROG_respiracao_A:
			mWrite ")("
			inc FROG_respiracao
			cmp FROG_respiracao, 6
			jne D_Finally
			mov FROG_respiracao, 0
			jmp D_Finally

;------- Sapo B --------
	DesenharSapoB_1:
		mov	al, white + (lightBlue *16)
		call SetTextColor
		mWrite "¢ "
		jmp D_Finally
	DesenharSapoB_2:
		mov	al, white + (lightBlue *16)
		call SetTextColor
		cmp FROG_respiracao, 3
		ja FROG_respiracao_B
			mWrite ")("
			inc FROG_respiracao
			jmp D_Finally
		FROG_respiracao_B:
			mWrite ")("
			inc FROG_respiracao
			cmp FROG_respiracao, 6
			jne D_Finally
			mov FROG_respiracao, 0
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
		mWrite ")("
		jmp D_Finally
	
	DesenharMortoB_1:
		mov	al, white + (red * 16)
		call SetTextColor
		mWrite "X X"
		jmp D_Finally
	
	DesenharMortoB_2:
		mov	al, white + (red * 16)
		call SetTextColor
		mWrite ")("

	D_Finally:	
	ret
FROG_DesenharCaracteres endp

; MUDAR DE FASE
; ===============================

FROG_NovoCampo PROC
	
	inc FROG_fCampo[18]
	cmp FROG_fCampo[18], 58
	jb NAO_CARRYOU
	mov FROG_fCampo[18], 48
	inc FROG_fCampo[17]
	NAO_CARRYOU:
	call FROG_LerCampo
	cmp FROG_ganharamJogo, 1
	je ProximaFase
	call FROG_DefinirCampo
	ProximaFase:

	ret
FROG_NovoCampo ENDP

; =================================================
; Procedimento: FROG_DefinirCampo
; Descricao: extrai as informações necessárias previamente armazenadas no vetor de FROG_Campo_Temp.
FROG_DefinirCampo PROC

	mov ecx, 15
	mov esi, 24
	mov eax, 0
	mov edx, 0
	TESTE2:
	call FROG_DefinirMovimentos
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
	add esi, 4
	mov ecx, 2
	HEXALOOP:
	inc esi
	sub FROG_Campo_Temp[esi], 48
	cmp FROG_Campo_Temp[esi], 9
	jbe HEXA1
	sub FROG_Campo_Temp[esi], 7
	HEXA1:
	loop HEXALOOP
	add esi, 7
	pop ecx
	loop TESTE2

	add esi, 5
	mov ecx, 16
	TESTE3:
	push ecx
	mov ecx, 2
	HEXALOOP2:
	inc esi
	sub FROG_Campo_Temp[esi], 48
	cmp FROG_Campo_Temp[esi], 9
	jbe HEXA2
	sub FROG_Campo_Temp[esi], 7
	HEXA2:
	loop HEXALOOP2
	cmp esi, 603
	jb SIMPLES
	add esi, 4
	SIMPLES:
	add esi, 6

	pop ecx
	loop TESTE3

	ret
FROG_DefinirCampo ENDP

; =================================================
; Procedimento: FROG_LerCampo
; Descricao: busca no arquivo campo.txt informacoes referentes ao campo da fase atual.
FROG_LerCampo PROC
	mov edx, OFFSET FROG_fCampo
	call OpenInputFile
	mov FROG_fHandle, eax
	cmp eax, INVALID_HANDLE_VALUE
	jne Definir_Cont
	mov FROG_ganharamJogo, 1
	ret

	Definir_Cont:
	mov edx, OFFSET FROG_Campo_Temp
	mov ecx, FROG_CAMPO_TAM
	call ReadFromFile
	mov eax, FROG_fHandle
	call CloseFile

	ret
FROG_LerCampo ENDP

FROG_DefinirMovimentos PROC

	sub FROG_Campo_Temp[esi], 48
	movzx bx, FROG_Campo_Temp[esi]
	mov FROG_TransitoSentido[edx], bx
	add esi, 2
	sub FROG_Campo_Temp[esi], 48
	movzx bx, FROG_Campo_Temp[esi]
	mov FROG_TransitoVeloc[edx], bx
	mov FROG_VelocAtual[edx], bx
	add esi, 2
	add edx, 2

	ret
FROG_DefinirMovimentos ENDP

; =================================================
; Procedimento: FROG_ExibirHUD
; Descricao: exibe informacoes referentes ao sapos e ao jogo, como numero de passos,
; nome da fase, vidas restantes ou informacoes sobre como entrar no modo cooperativo.
FROG_ExibirHUD proc
	mov al, white + 16*black
	call SetTextColor

	mov dh, 19
	mov dl, 53
	call Gotoxy
	mov ecx, 20
	mov esi, 0
	WTITLE:
		mov al, FROG_Campo_Temp[esi]
		cmp al, 35
		je NTITLE
			call WriteChar
		NTITLE:
			inc esi
	loop WTITLE

	mov dh, 17
	mov dl, 53
	call Gotoxy
	mWrite "   Level: "
	mov al, FROG_fCampo[17]
	mov bl, FROG_fCampo[18]
	inc bl
	cmp bl, 58
	jne NaoEhDez
	mov bl, 48
	inc al
	NaoEhDez:
	cmp al, 48
	je SemDecimal
	call WriteChar
	SemDecimal:
	mov al, bl
	call WriteChar
	
	MostraA:
		mov dl, 5
		mov dh, 0
		call Gotoxy
		mWrite "Sapo A: "
		movzx ecx, FROG_A_Vidas
		l1:
			mWrite 3
			mWrite " "
		loop l1
		mWrite " "
	
	cmp FROG_coop, 1
	jne ExibirCoop
		mov dl, 37
		mov dh, 0
		call Gotoxy
		mWrite "Sapo B: "
		movzx ecx, FROG_B_Vidas
		l2:
			mWrite 3
			mWrite " "
		loop l2
		mWrite " "
		jmp ExibirMovimentos

	ExibirCoop:
		mov dh, 0
		mov dl, 28
		call Gotoxy
		mWrite "F2: um segundo sapo"
		inc dh
		call Gotoxy
		mWrite "aparecera magicamente!"
	
	ExibirMovimentos:
	mov dh, 2
	mov dl, 5
	call Gotoxy
		
	mWrite "Movimentos: "
	movzx eax, FROG_Movimentos
	call WriteDec

	ret
FROG_ExibirHUD endp


; =================================================
; Procedimento: FROG_PressEnter
; Descricao: exibe a mensagem "PRESS ENTER TO CONTINUE" abaixo do campo
FROG_PressEnter PROC

	mov dh, 34
	mov dl, 5
	call Gotoxy
	mov	ax, white + (black * 16)
	call SetTextColor
	mWrite "P R E S S   E N T E R   T O   C O N T I N U E"

	PressioneEnter:	
	call ReadChar
	cmp eax, 7181
	jne PressioneEnter

	ret

FROG_PressEnter ENDP

; =================================================
; Procedimento: FROG_EntreFases
; Descricao: exibe tela preta com a mensagem Level: ??
FROG_EntreFases PROC
	
	mov al, white + 16*black
	call SetTextColor
	call Clrscr

	mov dh, 15
	mov dl, 20
	call Gotoxy

	mWrite "Level: "
	mov al, FROG_fCampo[17]
	mov bl, FROG_fCampo[18]
	inc bl
	
	cmp bl, 58
	jne NaoEhDez
		mov bl, 48
		inc al
	NaoEhDez:
	
	cmp al, 48
	je SemDecimal
		call WriteChar
	SemDecimal:
	mov al, bl
	call WriteChar

	add dh, 2
	mov dl, 15
	call Gotoxy

	mov ecx, 20
	mov esi, 0
	WTITLE:
		mov al, FROG_Campo_Temp[esi]
		cmp al, 35
		je NTITLE
			call WriteChar
		NTITLE:
			inc esi
	loop WTITLE

	call ReadChar

	ret
FROG_EntreFases ENDP

; =================================================
; Procedimento: FROG_ExibirVitoria
; Descricao: exibe mensagem de vitoria quando os dois sapos atravessam todo o campo
FROG_ExibirVitoria proc
	mov al, white + 16*black
	call SetTextColor
	call Clrscr

	mov dh, 15
	mov dl, 15
	call Gotoxy
	mWrite "V O C E    G A N H O U ! !"	
	add dh, 2
	mov dl, 3
	call Gotoxy
	mWrite "Parabens! O sapo conseguiu sobreviver aos terriveis humanos! "

	call ReadChar
	
	ret
FROG_ExibirVitoria endp

; =================================================
; Procedimento: FROG_ExibirDerrota
; Descricao: exibe mensagem de derrota quando um dos sapos perde todas as suas vidas.
FROG_ExibirDerrota proc
	mov al, white + 16*black
	call SetTextColor
	call Clrscr

	mov dh, 15
	mov dl, 15
	call Gotoxy
	mWrite "V O C E    P E R D E U ! !"	
	add dh, 2
	mov dl, 3
	call Gotoxy
	mWrite "Que pena! O sapo agora se encontra no plano espiritual. "
	
	call ReadChar
	
	ret
FROG_ExibirDerrota endp

; =================================================
; Procedimento: FROG_ExibirIntro
; Descricao: busca desenho contido no arquivo Frogger.txt a fim de exibir o desenho da tela inicial.
FROG_ExibirIntro PROC
	mov edx, OFFSET FROG_fIntro
	call OpenInputFile
	cmp eax, INVALID_HANDLE_VALUE
	jne Intro_Cont
	jmp PulouIntro
	Intro_Cont:
	mov FROG_fHandle, eax
	mov edx, OFFSET FROG_Intro
	mov ecx, FROG_INTRO_TAM
	call ReadFromFile
	mov eax, FROG_fHandle
	call CloseFile
	
	mov al, black + 16*white
	call SetTextColor

	mov ecx, FROG_INTRO_TAM
	mov esi, 0
	Intro_L:
	mov al, FROG_Intro[esi]
	call WriteChar
	mov eax, 2
	call Delay
	mov eax, 0
	call ReadKey
	cmp eax, 1
	ja PulouIntro
	inc esi
	loop Intro_L

	mov eax, 0
 
	call ReadChar
	PulouIntro:
	ret
FROG_ExibirIntro ENDP

; =================================================
; Procedimento: FROG_EntrarOnlineB
; Descricao: verifica se houve um pedido do segundo jogador para comecar 
; o modo cooperativo. Se sim, reinicia o jogo com o segundo jogador.
FROG_EntrarOnlineB proc
	cmp FROG_coop, 1 ; o sapo B ja esta jogando, nao faz nada.
	je CP_Else
		mov FROG_coop, 1
		jmp Finally
	CP_Else:
		mov FROG_coop, 0
	Finally:
	mov FROG_resetJogo, 1
	ret
FROG_EntrarOnlineB endp

; =================================================
; Procedimento: FROG_InitJogo
; Descricao: chamado pelo main.asm do Menotti. Inicializa TUDO.
FROG_InitJogo proc
	MenuInicial:
		mov al, white + 16*black
		call SetTextColor
		call Clrscr
		call FROG_ExibirIntro
		mov FROG_ganharamJogo, 0
		mov FROG_Movimentos, 0
		mov FROG_Movimentos_Total, 0
		mov FROG_fCampo[17], 48
		mov FROG_fCampo[18], 48

		; o jogador quer sair do jogo, pois pressionou a tecla ESC na tela de intro.
		cmp eax, 283
		je FROG_InitJogo_Finally
			mov FROG_coop, 0
			mov FROG_A_Vidas, 3
			mov FROG_B_Vidas, 3
			call FROG_LerCampo
			call FROG_EntreFases
			call FROG_NovoJogo
			jmp MenuInicial
	
	FROG_InitJogo_Finally:
	ret
FROG_InitJogo endp

; =================================================
; Procedimento: FROG_NovoJogo
; Descricao: chamado por InitJogo, quando um dos sapos morre ou quando há mudança de fase.
; Restaura as variaveis dos sapos.
FROG_NovoJogo proc
	NovoJogo:
	mov al, white + 16*black
	call SetTextColor
	call Clrscr

	call FROG_LerCampo
	call FROG_DefinirCampo

	mov FROG_resetJogo, 0
	
	mov FROG_ApassouFase, 0
    mov FROG_AperdeuJogo, 0
	mov FROG_BpassouFase, 0
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
		mov FROG_BpassouFase, 1
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

		mov FROG_BpassouFase, 0
		
	ChamarClock:
    call FROG_Clock

	cmp FROG_resetJogo, 1
    je NovoJogo

	FROG_InitJogo_Finally:
	ret
FROG_NovoJogo endp

main PROC
    call FROG_InitJogo

    exit
main ENDP

END main