// LFAAtividade2.cpp : Defines the entry point for the console application.
//

#include "stdafx.h"
#include "string.h"
#include <iostream>

using namespace std;

#include "Codificador.h"

int _tmain(int argc, _TCHAR* argv[])
{
	Codificador Cod;
	char CadeiaEntrada[100];
	strcpy_s(CadeiaEntrada, "Olá!");

	cout << "\n\n--- Codificacao de strings ---\n\n";

	Cod.DefinirCadeia(CadeiaEntrada);
	
	cout << "\nOriginal :: ";
	Cod.ExibirCadeia();

	Cod.Codificar();
	cout << "Codificada :: ";
	Cod.ExibirCadeia(0);

	Cod.Descodificar();
	cout << "\nDescodificada :: ";
	Cod.ExibirCadeia(0);

	getchar();
	return 0;
}
