#pragma once
#include "GameState.h"
#include "UtilidadesSDL.h"

using namespace std;
class Botao
{
private:
	//estado para o qual o botão o troca
	GameState troca;
	//retangulo com os limites do botão
	SDL_Rect area;
	//imagem normal do botão
	SDL_Surface *btn;
	//imagem do botão quando o mouse passar
	SDL_Surface *hover;
	//estado se o mouse está em cima ou não
	bool encima;

	
public:
	Botao(int x, int y,string btn, string hover, GameState::Estados gs);
	Botao(void);
	void Posicionar(int x, int y);
	void Imprime(SDL_Surface *tela);
	void LidarEventos(SDL_Event evento,GameState *gs);
	~Botao(void);
};

