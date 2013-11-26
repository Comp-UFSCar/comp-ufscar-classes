#include "CommandSpell.h"
#include <Windows.h>

CommandSpell::CommandSpell(Comandos c)
{
	Comando=c;
	Estado=NEUTRO;
	acertou=NULL;
	errou=NULL;
	x=0;
	y=0;
}

CommandSpell::CommandSpell(int c)
{

	Comando=NORTE;

	if(c=='1')
		Comando=SUL;
	if(c=='2')
		Comando=LESTE;
	if(c=='3')
		Comando=OESTE;
	if(c=='4')
		Comando=NORDESTE;
	if(c=='5')
		Comando=NOROESTE;
	if(c=='6')
		Comando=SUDESTE;
	if(c=='7')
		Comando=SUDOESTE;
			

	Estado=NEUTRO;
	acertou=NULL;
	errou=NULL;
	x=0;
	y=0;
}

CommandSpell::CommandSpell()
{
	Comando=NORTE;
	Estado=NEUTRO;
	acertou=NULL;
	errou=NULL;
	x=0;
	y=0;
}

CommandSpell::~CommandSpell(void)
{
}

//troca o estado do comando  
void CommandSpell::trocarEstado(Estados novoEstado)
{
	Estado=novoEstado;
}

//retorna o estado do Commandspell
int CommandSpell::getEstado()
{
	return Estado;
}

//Dá uma posição na tela para o Commandspell
void CommandSpell::Posicionar(int posx, int posy)
{
	x=posx;
	y=posy;
}

//Põe na tela o Commandspell
void CommandSpell::printCommandSpell(SDL_Surface* tela)
{
	stringstream caminho;
	caminho<<"resources/imagens/"<<Estado<<"/"<<Comando<<".png";

	SDL_Surface *imagem;
	imagem=IMG_Load(caminho.str().c_str());
	UtilidadesSDL::AplicarSuperficie(x,y,imagem,tela,NULL);
}

//move pela tela um commandspell
void CommandSpell::mover(int valor)
{
	y=y+valor;
}

//retorn a posição y do commandspell
int CommandSpell::getY()
{
	return y;
}

void CommandSpell::LidarEventos(Uint8 *teclas,Mix_Chunk *acertou,Mix_Chunk *errou)
{
	if(Estado==NEUTRO)
	{
		if(Comparar(teclas))
		{
			trocarEstado(CORRETO);
			Mix_PlayChannel( -1, acertou, 0 );
		}
		else
		{
			trocarEstado(ERRADO);
			Mix_PlayChannel( -1, errou, 0 );
		}
	}
	
}

bool CommandSpell::Comparar(Uint8 *teclas)
{
	
	Comandos comparar=NADA;

	//tecla pra cima foi apertada
	if(teclas[SDLK_UP])
	{
		comparar=NORTE;
		
		if(teclas[SDLK_DOWN])
			return false;		
		if(teclas[SDLK_RIGHT] && teclas[SDLK_LEFT])
			return false;	
		if(teclas[SDLK_RIGHT])
			comparar=NORDESTE;
		if(teclas[SDLK_LEFT])
			comparar=NOROESTE;
			
	}
	//tecla pra baixo foi apertada
	else if(teclas[SDLK_DOWN])
	{
		comparar=SUL;
		if(teclas[SDLK_RIGHT] && teclas[SDLK_LEFT])
			return false;
		if(teclas[SDLK_RIGHT])
			comparar=SUDESTE;
		if(teclas[SDLK_LEFT])
			comparar=SUDOESTE;
	}
	//tecla pra direita foi apertada
	else if(teclas[SDLK_RIGHT])
	{
		comparar=LESTE;
		if(teclas[SDLK_LEFT])
			return false;
			
	}
	//tecla pra esquerda foi apertada
	else if(teclas[SDLK_LEFT])
	{
		comparar=OESTE;
		if(teclas[SDLK_RIGHT])
			return false;			
	}

	if (comparar==Comando)
		return true;


	return false;
	
}