#pragma once
#include "GameState.h"
#include "UtilidadesSDL.h"
#include "Fase.h"
#include "Botao.h"
#include <fstream>


class Jogo
{
private:
	//eventos que serão mainuplados durante o jogo
	SDL_Event evento;

	//botao para voltar
	Botao *voltar;

	//botao para iniciar a fase
	Botao *pfase;

	//plano de fundo 
	SDL_Surface *fundo;

	//circulo de invicação
	SDL_Surface *circulo;

	//imagem da criatura que foi invocada
	SDL_Surface *criatura;
	
	//variavel de referencia para ver qual eh a imagem e qual eh a fase q está sendo jogada
	int nfase;

	//fase atual
	Fase *fase;

	//ponteiro para o commandspell que deve ser apertado
	Node<CommandSpell> *atual;

	//ponteiro para o som de quando acerta e erra o commandspell
	Mix_Chunk *acertou;
	Mix_Chunk *errou;
	//ponteiro para a msuca q será tocada
	Mix_Music *vitoria;


public:
	void loadFase();
	int LoopJogo(SDL_Surface *tela, GameState *gs);
	int LoopVitoria(SDL_Surface *tela, GameState *gs);
	void ImprimeJogo(SDL_Surface *tela);
	void ganhar(GameState *gs);
	Fase FaseJogando();
	Jogo(void);
	~Jogo(void);
};

