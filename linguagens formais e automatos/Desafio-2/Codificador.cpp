#include "stdafx.h"
#include "Codificador.h"
#include "time.h"
#include <iostream>

using namespace std;

Codificador::Codificador()
{
	CadeiaAtual = CadeiaAtual = NULL;
	NumerosSorteados = NULL;
}
Codificador::~Codificador()
{
	/*if(CadeiaOriginal)
		delete [] CadeiaOriginal;
	if(CadeiaAtual)
		delete [] CadeiaAtual;
	if(NumerosSorteados)
		delete [] NumerosSorteados;*/
}

/*
	bool ExibirCadeia(bool pOriginal = true)
	Criacao: 10/11/2012
	Ultima modificacao: 10/11/2012

	Parametro: flag que dita qual string deve ser impressa.
	Retorno: nada
*/
void Codificador::ExibirCadeia(bool pOriginal)
{
	cout <<
		( pOriginal
		? CadeiaOriginal
		: CadeiaAtual )
		<< endl;
}

/*
	bool DefinirCadeia(char *pString)
	Criacao: 10/11/2012
	Ultima modificacao: 10/11/2012

	Parametro: ponteiro para cadeia a ser copiada
	Retorno:
		verdadeiro: a string foi copiada com sucesso
		falso: string ee invalida ou alocacao dinamica mal sucedida
*/
bool Codificador::DefinirCadeia(char *pString)
{
	if(strlen(pString) == 0)
	{
		cout << "Cadeia invalida!" << endl;
		return false;	
	}

	if(CadeiaOriginal) // Uma cadeia ja esta alocada
	{
		delete [] CadeiaOriginal;
		CadeiaOriginal = NULL;
	}
	
	if(CadeiaAtual) // Uma cadeia ja esta alocada
	{
		delete [] CadeiaAtual;
		CadeiaAtual = NULL;
	}

	TAM = strlen(pString);

	CadeiaOriginal = new char[TAM]; // Aloca dinamicamente o tamanho da cadeia copiada
	CadeiaAtual = new char[2*TAM];	// Aloca dinamicamente o dobro da cadeia copiada. No por caso,
									// onde todos os caracteres sao validos, sera necessario uma string com o
									// dobro do tamanho de CadeiaOriginal para guardar os numeros sorteados
	
	if(!CadeiaOriginal || !CadeiaAtual)
	{
		cout << "Erro ao alocar memoria dinamicamente!" << endl;
		return false;
	}

	// copia a string
	strcpy_s(CadeiaOriginal, strlen(CadeiaOriginal), pString);
	strcpy_s(CadeiaAtual, strlen(CadeiaAtual), pString);

	return true;
}

/*
	bool Codificar()
	Criacao: 10/11/2012
	Ultima modificacao: 10/11/2012

	Parametro: nada
	Retorno: nada
*/
void Codificador::Codificar()
{
	int i = 0;

	//	1. ELIMINACAO DE CARACTERES ESPECIAIS
	while(CadeiaAtual[i] != '\0')
	{
		if(CadeiaAtual[i] >= 'a' && CadeiaAtual[i] <= 'z') // funcao toUpper()
			CadeiaAtual[i] -= 32;

		if(CadeiaAtual[i] < 'A' || CadeiaAtual[i] > 'Z') // remove caracteres invalidos
		{
			int cpyIndex = i;					// desloca vetor sobre caractere invalido
			while(CadeiaAtual[cpyIndex -1] != '\0')
			{
				CadeiaAtual[cpyIndex] = CadeiaAtual[cpyIndex +1];
				cpyIndex++;
			}
			i--;	// caractere atual tambem precisa ser verificado!
		}

		i++;
	}
	
	// 2. SORTEANDO NUMEROS
	TAM = i; // i agora contem o novo tamanho da string

	if(NumerosSorteados) // uma cadeia ja esta alocada, desaloca-a
	{
		delete [] NumerosSorteados;
		NumerosSorteados = NULL;
	}
	NumerosSorteados = new int[TAM];
	
	i = 0;
	srand(time(0));

	while(i < TAM) // i armazenou o tamanho da cadeia atraves da interaCao anterior
		NumerosSorteados[i++] = rand() %10;
	
	
	// 3. SOMAS CICLICAS DO NUMEROS SORTEADOS COM A STRING
	i = 0;

	while(i < TAM)
	{
		CadeiaAtual[i] += NumerosSorteados[i];
		if(CadeiaAtual[i] > 'Z')
			CadeiaAtual[i] += 'A' -'Z';
		
		i++;
	}

	// 4. COVERTER PARA LETRAS A PARTIR DE 'A'
	i = 0;

	while(i < TAM)
		NumerosSorteados[i++] += 'A';
	
	// 5. ESCREVER TODAS AS LETRAS GERADAS A PARTIR DE NumerosSorteados
	i = 0;
	cout << "\nNum sorteados convertidos :: ";
	while(i < TAM) // i armazenou o tamanho da cadeia atraves da interaCao anterior
		cout << (char)NumerosSorteados[i++];
	
	cout << endl;

	// 6. ESCREVER TODAS OS CARACTERES CONVERTIDOS NA ORDEM INVERSA
	i = 0;
	char tmpTrocaChar;

	while(i < TAM/2)
	{
		tmpTrocaChar = CadeiaAtual[i];
		CadeiaAtual[i] = CadeiaAtual[TAM -i -1];
		CadeiaAtual[TAM -i -1] = tmpTrocaChar;

		i++;
	}
	
	// 7. CONCATENAR
	int j = 0;
	i = TAM;

	while(j < TAM)
		CadeiaAtual[i++] = NumerosSorteados[j++];
	CadeiaAtual[i] = '\0';
	
	return;	
}

/*
	bool Descodificar()
	Criacao: 10/11/2012
	Ultima modificacao: 10/11/2012

	Parametro: nada
	Retorno: nada
*/
void Codificador::Descodificar()
{
	// 1. DESCOBRIR NUMEROS SORTEADOS
	int i = TAM = strlen(CadeiaAtual) /2;
	int j = 0;

	if(NumerosSorteados) // uma cadeia ja esta alocada, desaloca-a
	{
		delete [] NumerosSorteados;
		NumerosSorteados = NULL;
	}
	NumerosSorteados = new int[TAM];

	while(CadeiaAtual[i] != '\0')	// copia todos os numeros sorteados e os decodificca
		NumerosSorteados[j++] = CadeiaAtual[i++] -'A';

	CadeiaAtual[TAM] = '\0';
	
	// 2. REINVENTER OS CARACTERES CONVERTIDOS
	char tmpTrocaChar;
	i = 0;

	while(i < TAM /2)
	{
		tmpTrocaChar = CadeiaAtual[i];
		CadeiaAtual[i] = CadeiaAtual[TAM -i -1];
		CadeiaAtual[TAM -i -1] = tmpTrocaChar;

		i++;
	}

	// 3. DECODIFICAR MENSAGEM EM CadeiaAtual
	i = 0;
	while(i < TAM) // i armazenou o tamanho da cadeia atraves da interaCao anterior
	{
		CadeiaAtual[i] -= NumerosSorteados[i];
		if(CadeiaAtual[i] < 'A')
			CadeiaAtual[i] += 'Z' -'A';
		
		i++;
	}
	
	return;
}