#pragma once
#include "Botao.h"

Botao::Botao(int posx, int posy,string img,string imgh,GameState::Estados gs)
{	
	btn=IMG_Load(img.c_str());
	hover=IMG_Load(imgh.c_str());
	area=btn->clip_rect;
	area.x=posx;
	area.y=posy;	
	troca.TrocaEstado(gs);

	encima=false;
}

Botao::Botao(void)
{
	area.h=0;
	area.w=0;
	area.x=0;
	area.y=0;
	btn=NULL;
	hover=NULL;
	encima=false;
	troca.TrocaEstado(GameState::Estados::MENU);

}

void Botao::Posicionar(int posx, int posy)
{
	area.x=posx;
	area.y=posy;
}

void Botao::Imprime(SDL_Surface *tela)
{
	if(encima==true)
	{
		UtilidadesSDL::AplicarSuperficie(area.x,area.y,hover,tela,NULL);
	}
	else
	{
		UtilidadesSDL::AplicarSuperficie(area.x,area.y,btn,tela,NULL);
	}
}

void Botao::LidarEventos(SDL_Event evento, GameState *gs)
{
	if(evento.type==SDL_MOUSEMOTION)
	{
		int x=evento.motion.x;
		int y=evento.motion.y;
		if(x>area.x && x<(area.x+area.w) && y>area.y && y<(area.y+area.h))
		{
			encima=true;
		}
		else
			encima=false;
	}
	if(evento.type==SDL_MOUSEBUTTONDOWN)
	{
		int x=evento.motion.x;
		int y=evento.motion.y;
		if(x>area.x && x<(area.x+area.w) && y>area.y && y<(area.y+area.h))
		{
			gs->TrocaEstado(troca.estado);
		}
		
	}
}

Botao::~Botao(void)
{
}
