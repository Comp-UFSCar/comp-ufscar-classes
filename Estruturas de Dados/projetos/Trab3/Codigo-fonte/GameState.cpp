#include "GameState.h"


GameState::GameState(void)
{
	estado=MENU;
}

GameState::GameState(Estados est)
{
	estado=est;
}

GameState::~GameState(void)
{
}

void GameState::TrocaEstado(Estados x)
{
	estado=x;
}

