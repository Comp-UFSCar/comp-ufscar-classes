# tutorial.s
	.data 0x10000000
msg1:		.asciiz "Arquitetura e Organizacao de Computadores"
	.text
main:
	li $v0, 4 # código para imprimir "string"
	la $a0, msg1
	syscall
