#pragma once
#include "UtilidadesSDL.h"
#include "CommandSpell.h"
#include "Lista.h"

class Fase
{
public:
	Lista<CommandSpell> comandos;
private:	
	int velocidade;
	int distancia;

public:
	Fase(int v, int s,string linha);
	Fase(void);
	~Fase(void);
	void printAllCommandSpell(SDL_Surface *tela);
	bool checarVitoria(void);
	void mover();
};

