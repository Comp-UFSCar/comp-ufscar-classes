#pragma once
#include "SDL.h"
class Carta
{
    private:

	int numero;
	int naipe;
	SDL_Rect area;

    public:

	Carta(void);
	Carta(int c, int n);

	int getNum();
	int getNaipe();
	int getX();
	int getY();
	int getW();
	int getH();

	void setNaipe(int x);
	void setNum(int x);
	void Posicionar(int x,int y);
	SDL_Rect getArea();

	void operator =(Carta &);
};
