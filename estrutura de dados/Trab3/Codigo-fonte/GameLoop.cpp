#include "GameLoop.h"


GameLoop::GameLoop(void)
{
	UtilidadesSDL::InicializarSDL();
	wtela=800;
	htela=600;

	//escolhe o icone para o jogo
	SDL_WM_SetIcon(IMG_Load("resources\imagens\icone\icon.png"), NULL);
	
	//Prepara a tela
	tela = SDL_SetVideoMode(wtela,htela,32,SDL_SWSURFACE);

	//o jogo começa no estado menu
	gs = new GameState();
	gs->TrocaEstado(GameState::Estados::MENU);

	//coloca o nome que aparece no topo da tela e o nome do icone
	SDL_WM_SetCaption("O Aprendiz de Feiticeiro", "Aprendiz");
	jogo=new Jogo();

	
}


GameLoop::~GameLoop(void)
{
}

int GameLoop::LoopTotal()
{
	
	bool sair=false;
	//enquanto os estados estiverem em qualquer outra coisa que não seja SAIR,sair vai continuar como falso
	while(!sair)
	{
		if (gs->estado==0)
			menu.MenuLoop(tela,gs);
		if(gs->estado==1)
			menu.InstrucoesLoop(tela,gs);
		if(gs->estado==2)
			jogo->LoopJogo(tela,gs);
		if(gs->estado==3)
			jogo->LoopVitoria(tela,gs);
		if(gs->estado==4)
			sair=true;
	}
		
	return 0;

}
