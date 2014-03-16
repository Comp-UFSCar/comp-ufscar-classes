#include "Jogo.h"

Jogo::Jogo(void)
{
	//carrega as imagens do jogo
	fundo=IMG_Load("resources/imagens/bg/menu.png");
	circulo=IMG_Load("resources/imagens/bg/circulo.png");

	//carrega os botões
	voltar = new Botao(0,450,"resources/imagens/botoes/sairv.png","resources/imagens/botoes/sairp.png",GameState::Estados::MENU);
	pfase = new Botao(300,450,"resources/imagens/botoes/pfasev.png","resources/imagens/botoes/pfasep.png",GameState::Estados::JOGO);

	//aponta para nenhuma criatura
	criatura=NULL;

	
	//carrega o som de acerto e erro
	acertou = Mix_LoadWAV("resources/som/acertou.wav");
	errou = Mix_LoadWAV("resources/som/erro.ogg");

	//carrega a musica
	vitoria = Mix_LoadMUS("resources/som/victory.ogg");


	loadFase();
	
}

Jogo::~Jogo(void)
{

}

int Jogo::LoopJogo(SDL_Surface *tela, GameState *gs)
{	
	loadFase();

	//enquanto o estado do jogo for "JOGO" continua a rodar o jogo propriamente dito 
	while(gs->estado==2)
	{
		ImprimeJogo(tela);
		
		//Enquanto tiver um evento para manipular, testarei todos os eventos possiveis
		while(SDL_PollEvent( &evento ))
		{			
			//checa se uma tecla foi pressionada 
			if( evento.type == SDL_KEYDOWN ) 
			{
			//e se ela está tendo do circulo de invocação
				if(atual->info.getY()>300 && atual->info.getY()<530)
				{
					Uint8 *teclas = SDL_GetKeyState( NULL );
					atual->info.LidarEventos(teclas,acertou,errou);
					atual=atual->dir;
				}
			}
			
			voltar->LidarEventos(evento,gs);
			
		}	
			//se o comando já passou da area circulo
			if(atual->info.getY()>530)
			{
				atual->info.trocarEstado(CommandSpell::Estados::ERRADO);
				atual=atual->dir;
			}
		if(SDL_Flip(tela)==-1)
			return 1;

		//checa se  a fase acabou
		if(atual==fase->comandos.getHeader() && fase->checarVitoria())
			ganhar(gs);
			
	}
	
	return 0;

}

void Jogo::loadFase()
{
	string linha,nivel;
	int vel,dis;
	stringstream aux;

	fstream arquivo;
	//abre o arquivo em que está guardada a fase em q o jogador parou
	arquivo.open("resources/files/save.txt");
	//lê uma linha do arquivo e retorna o nivel em q o jogador parou
	getline(arquivo,nivel);	

	//guarda na variavel qual eh essa fase
	//converte usado a string stream a string para um inteiro
	stringstream s0(nivel);
	s0>>nfase;
	if(nfase==17)
		nfase=0;

	//monta o caminho para o arquivo da fase a ser jogada
	aux<<"resources/files/"<<nfase<<".txt";

	//fecha o arquivo para poder abrir outro arquivo
	arquivo.close();

	//abrir o arquivo da fase	
	arquivo.open(aux.str().c_str());

	//lê a primeira linha do arquivo e recebe a velocidade da fase
	getline (arquivo,linha);
	//converte usado a string stream a string para um inteiro
	stringstream s1(linha);
	s1>>vel;

	//lê a segunda linha do arquivo e recebe a distancia entre os commandspells na fase
	getline (arquivo,linha);
	//converte usado a string stream a string para um inteiro
	stringstream s2(linha);
	s2>>dis;

	//lê a terceira linha do arquivo e recebe a lista com todos os commandspells da fase
	getline (arquivo,linha);

	fase=new Fase(vel,dis,linha);
	arquivo.close();
	atual=fase->comandos.getHeader()->dir;
}

//vai colcoar na tela todas as coisas do jogo
void Jogo::ImprimeJogo(SDL_Surface *tela)
{
	//Coloca o background
	UtilidadesSDL::AplicarSuperficie(0,0,fundo,tela,NULL);
	//Coloca o circulo de invocação
	UtilidadesSDL::AplicarSuperficie(440,280,circulo,tela,NULL);
	//Imprime todos os botões
	voltar->Imprime(tela);
	//Move os comandos
	fase->mover();
	//imprime toda a fase q está sendo jogada
	fase->printAllCommandSpell(tela);	
}

//se ganhar eu chamo esse metodo 
void Jogo::ganhar(GameState *gs)
{	
	nfase++;
	stringstream aux;
	aux<<nfase;
	ofstream save ("resources/files/save.txt");
  if (save.is_open())  
	  save << aux.str().c_str();

  //troca o estado para vitoria e exibição da imagem
  gs->TrocaEstado(GameState::Estados::VITORIA);
}

//depois de ganha a fase, entra no estado de vitoria, onde o jogador ve a criatura que ele invocou e passa para a proxima fase
int Jogo::LoopVitoria(SDL_Surface *tela, GameState *gs)
{
	//toca a musica
	Mix_PlayMusic( vitoria, -1 );

	stringstream aux;
	aux<<"resources/imagens/criaturas/"<<nfase<<".png";
	criatura=IMG_Load(aux.str().c_str());	

	while(gs->estado==3)
	{
		UtilidadesSDL::AplicarSuperficie(0,0,criatura,tela,NULL);
		voltar->Imprime(tela);
		pfase->Imprime(tela);
		while(SDL_PollEvent( &evento ))
		{
			voltar->LidarEventos(evento,gs);
			pfase->LidarEventos(evento,gs);
		}
		if(SDL_Flip(tela)==-1)
			return 1;
		if(gs->estado!=3)
		{
			Mix_HaltMusic();
		}
	}
	return 0;
}