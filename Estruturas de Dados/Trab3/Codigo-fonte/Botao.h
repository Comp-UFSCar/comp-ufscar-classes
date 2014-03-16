#pragma once
#include "GameState.h"
#include "UtilidadesSDL.h"

using namespace std;
class Botao
{
private:
	//estado para o qual o bot�o o troca
	GameState troca;
	//retangulo com os limites do bot�o
	SDL_Rect area;
	//imagem normal do bot�o
	SDL_Surface *btn;
	//imagem do bot�o quando o mouse passar
	SDL_Surface *hover;
	//estado se o mouse est� em cima ou n�o
	bool encima;

	
public:
	Botao(int x, int y,string btn, string hover, GameState::Estados gs);
	Botao(void);
	void Posicionar(int x, int y);
	void Imprime(SDL_Surface *tela);
	void LidarEventos(SDL_Event evento,GameState *gs);
	~Botao(void);
};

