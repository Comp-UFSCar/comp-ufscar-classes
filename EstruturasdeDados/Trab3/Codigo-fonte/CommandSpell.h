#pragma once
#include "UtilidadesSDL.h"
class CommandSpell
{

public:
	enum Estados {NEUTRO,CORRETO,ERRADO};
	enum Comandos {NORTE,SUL,LESTE,OESTE,NORDESTE,NOROESTE,SUDESTE,SUDOESTE,NADA};	

private:	
	Estados Estado;
	Comandos Comando;
	Mix_Chunk *acertou;
	Mix_Chunk *errou;
	int x,y;
	
	
	
public:
	void trocarEstado(Estados novoEstado);
	int getEstado();
	int getY();
	void LidarEventos(Uint8 *k, Mix_Chunk *acertou,Mix_Chunk *errou);
	bool Comparar(Uint8 *k);
	void Posicionar(int x, int y);
	void printCommandSpell(SDL_Surface* tela);
	void mover(int x);
	CommandSpell(Comandos c);
	CommandSpell(int i);
	CommandSpell(void);
	~CommandSpell(void);
};

