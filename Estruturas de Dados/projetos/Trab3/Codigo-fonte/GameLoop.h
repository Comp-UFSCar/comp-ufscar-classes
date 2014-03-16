#pragma once
#include "UtilidadesSDL.h"
#include "TelaInicial.h"
#include "Jogo.h"
#include "CommandSpell.h"

class GameLoop
{
private:

	//Tela do jogo
	SDL_Surface *tela;
	int wtela,htela;

	//Estado em que o jogo se encontra
	GameState *gs;

	//o jogo propriamente dito
	Jogo *jogo;

	//o menu
	TelaInicial menu;
public:
	GameLoop(void);
	~GameLoop(void);
	int LoopTotal();
};

