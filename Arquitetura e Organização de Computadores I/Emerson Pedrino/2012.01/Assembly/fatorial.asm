## Programa para calcular o fatorial de um numero inteiro N.

main:	## leitura do numero N 
	
	li	$v0, 5		# syscall 5 = read_int
	syscall			# realiza o syscall
	move	$t0, $v0	# define $t0 = valor lido
	blez	$t0, erro	# se $t0 for <= 0
	move 	$t1, $t0	# define $t1 = $t0 

loop:	## realiza o loop para calculo do fatorial

	beq	$t0, 1, fat	# se $t0 for 1, sai do loop
	
	sub	$t0, $t0, 1	# subtrai 1 de $t0
	mul	$t1, $t1, $t0	# multiplica $t1 por $t0

	b	loop		# iteracao

fat:	## imprime o fatorial na tela

	move	$a0, $t1	# imprime o fatorial calculado
	li	$v0, 1
	syscall

	b 	exit		# vai para o exit
	
erro:	## caso o valor entrado seja <= 0

	la	$a0, msgErro
	li	$v0, 4
	syscall
	
exit:	## finaliza o programa
					
	li	$v0, 10			# syscall 10 = exit
	syscall				


	.data
msgErro:	.asciiz "\nErro! Valor invalido."


