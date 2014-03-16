#pragma once
#include "SDL.h"
#include "SDL_image.h"
#include "SDL_mixer.h"
#include <fstream>
#include <string>
#include <iostream>
#include <sstream>

using namespace std;




class UtilidadesSDL
{
public:
	UtilidadesSDL(void);
	~UtilidadesSDL(void);
	static void AplicarSuperficie(int x, int y, SDL_Surface* fonte, SDL_Surface* desctino, SDL_Rect* clip);
	static bool InicializarSDL();
	
};

