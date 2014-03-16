#include "UtilidadesSDL.h"

UtilidadesSDL::UtilidadesSDL(void)
{
}

UtilidadesSDL::~UtilidadesSDL(void)
{
}

//método criado para melhor aplicar imagens na tela
void UtilidadesSDL::AplicarSuperficie (int x, int y,SDL_Surface *fonte,SDL_Surface *destino,SDL_Rect* clip=NULL)
{
	//faz um retangulo temporario para guardar os limites da tela
	SDL_Rect limites;

	// da os limites para o retangulo
	limites.x=x;
	limites.y=y;

	//Blit surface
	SDL_BlitSurface(fonte,clip,destino,&limites);
}

//método criado para inicializar o SDL no jogo
bool UtilidadesSDL::InicializarSDL()
{
	//Inicaliza o SDL
	if(SDL_Init (SDL_INIT_EVERYTHING)==-1)
		return false;

	//Inicializa SDL_mixer 
	if( Mix_OpenAudio( 22050, MIX_DEFAULT_FORMAT, 2, 4096 ) == -1 ) 
		 return false; 

	Mix_Init(MIX_INIT_OGG); 
	return true;
}
