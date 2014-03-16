#include "TelaInicial.h"


TelaInicial::TelaInicial(void)
{
	fundo=IMG_Load("resources/imagens/bg/menu.png");
	texto=IMG_Load("resources/imagens/bg/inst.png");
	jogar = new Botao(400,150,"resources/imagens/botoes/jogarv.png","resources/imagens/botoes/jogarp.png",GameState::Estados::JOGO);
	instrucoes = new Botao(400,300,"resources/imagens/botoes/instv.png","resources/imagens/botoes/instp.png",GameState::Estados::INSTRUÇÕES);
	saida = new Botao(400,450,"resources/imagens/botoes/sairv.png","resources/imagens/botoes/sairp.png",GameState::Estados::SAIR);
	voltar = new Botao(0,450,"resources/imagens/botoes/sairv.png","resources/imagens/botoes/sairp.png",GameState::Estados::MENU);
	
}

TelaInicial::~TelaInicial(void)
{
}

int TelaInicial::MenuLoop(SDL_Surface *tela,GameState *gs)
{
	//enquanto não quiser sair fica em loop 
	while(gs->estado==0)
	{
		//Coloca o background
		UtilidadesSDL::AplicarSuperficie(0,0,fundo,tela,NULL);
		//Imprime todos os botões
		jogar->Imprime(tela);
		instrucoes->Imprime(tela);
		saida->Imprime(tela);

		//Enquanto tiver um evento para manipular, testarei todos os eventos possiveis
		while(SDL_PollEvent( &evento ))
		{
			//checa os eventos possiveis para cada botão
			jogar->LidarEventos(evento,gs);
			instrucoes->LidarEventos(evento,gs);
			saida->LidarEventos(evento,gs);
		}
		if(SDL_Flip(tela)==-1)
			return 1;
	}
	
	return 0;
}

int TelaInicial::InstrucoesLoop(SDL_Surface *tela,GameState *gs)
{
	while(gs->estado==1)
	{
		//Coloca o background
		UtilidadesSDL::AplicarSuperficie(0,0,fundo,tela,NULL);
		//Coloca o texto
		UtilidadesSDL::AplicarSuperficie(0,0,texto,tela,NULL);
		//Imprime todos os botões
		voltar->Imprime(tela);
		//Enquanto tiver um evento para manipular, testarei todos os eventos possiveis
		while(SDL_PollEvent( &evento ))
		{
			//checa os eventos possiveis para cada botão
			voltar->LidarEventos(evento,gs);
		}
		if(SDL_Flip(tela)==-1)
			return 1;
	}
}
