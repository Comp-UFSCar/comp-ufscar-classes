#pragma once
class Codificador
{
public:
	Codificador(void);
	~Codificador(void);

	void ExibirCadeia(bool = true);
	bool DefinirCadeia(char *);
	void Codificar();
	void Descodificar();

private:
	char *CadeiaOriginal; // Definido atraves de DefinirCadeia(char *a)
	char *CadeiaAtual;	  // Armazena CadeiaOriginal de forma codificada ou normal.
	int *NumerosSorteados; // Definido atraves de Codificar()
	int TAM;				// Tamanho da cadeia de entrada.
};