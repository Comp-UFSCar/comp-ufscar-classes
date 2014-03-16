#pragma once
#include "Botao.h"
class TelaInicial
{
	//evento dentro do menu a ser esperado
	SDL_Event evento;
	//plano de fundo do menu
	SDL_Surface *fundo;
	//texto das instruções
	SDL_Surface *texto;
	//botoes do menu
	Botao *jogar;
	Botao *instrucoes;
	Botao *saida;
	//botao das instruções
	Botao *voltar;
	

public:
	TelaInicial(void);
	int MenuLoop(SDL_Surface *tela,GameState *x);
	int InstrucoesLoop(SDL_Surface *tela,GameState *x);
	~TelaInicial(void);
};

