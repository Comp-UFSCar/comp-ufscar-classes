//============================================================================
// Name        : main.cpp
// Author      : João Vitor, Thiago Faria, Lucas David
// Version     :
// Copyright   : Your copyright notice
// Description :
//============================================================================

#include <ctime>
#include "SDL.h"
#include "Pilha.h"
#include "Carta.h"

//Lista de cartas e bouquê
Carta baralho[52],bouque[28];

//tamanho da tela
int ScreenW=1080,ScreenH=700,Screebpp=32;

//Pointeiros para as imagens
SDL_Surface* cards_sheet = NULL; //CARTAS A SEREM MOSTRADA
SDL_Surface* bg = NULL; //Plano de fundo
SDL_Surface* screen = NULL;// TELA

//Estrutra de evento
SDL_Event evento;

//Varaiavel para sair , para arrastar, para guardar a carta que está sendo arrastada e sua posição antiga
bool sair=false,arrastando=false;
int cr=0,pos[2];

//declaração das cartas que receberão as fundações 
Carta *fund1 = new Carta();
Carta *fund2 = new Carta();
Carta *fund3 = new Carta();
Carta *fund4 = new Carta();

//declarando declaração pilhas que receberão as cartas
Pilha<Carta> *p1 = new Pilha<Carta>();
Pilha<Carta> *p2 = new Pilha<Carta>();
Pilha<Carta> *p3 = new Pilha<Carta>();
Pilha<Carta> *p4 = new Pilha<Carta>();
Pilha<Carta> *p5 = new Pilha<Carta>();
Pilha<Carta> *p6 = new Pilha<Carta>();



//função criada para melhor aplicar imagens na tela
void Apply_Surface (int x, int y,SDL_Surface *fonte,SDL_Surface *destino,SDL_Rect* clip=NULL)
{
	//faz um retangulo temporario para guardar os limites da tela
	SDL_Rect limites;

	// da os limites para o retangulo
	limites.x = x;
	limites.y=y;

	//Blit surface
	SDL_BlitSurface(fonte,clip,destino,&limites);
}

//função para desenhar uma carta na tela
void Print_Carta(Carta c,SDL_Surface *fonte,SDL_Surface *destino)
{
	//cria um retangulo temporario para dividir a spritesheet em pedaços do tamanho de cartas
	SDL_Rect clipe;

	//tamanho da carta
	clipe.h=167;
	clipe.w=102;

	//posiciona o retangulo to tamanho da carta na posição exata onde está a carta a ser impressa
	clipe.x=(c.getNum()-1)*(c.getArea().w);
	clipe.y=(c.getNaipe()-1)*(c.getArea().h);

	
	//Mapeia a cor chave pra remove-la
	Uint32 colorkey=SDL_MapRGB(fonte->format,64,0,64);

	//transforma todos os pixels escolhidos em transparente
	SDL_SetColorKey(fonte,SDL_SRCCOLORKEY,colorkey);
	
	//Coloca a carta na tela
	Apply_Surface(c.getX(),c.getY(),fonte,destino,&clipe);
}

//função que cria todas as cartas que serão usadas no jogo
void CreateCards()
{
	int i,j,cont=0;

	for(i=1;i<5;i++)
	{
		for(j=1;j<14;j++)
		{
			baralho[cont]=Carta(j,i);
			cont++;
		}
	}

}

//carrega os arquivos que serão usado
bool LoadFile()
{
	//Carrega a imagem
	cards_sheet = SDL_LoadBMP( "cartas.bmp" );
	bg = SDL_LoadBMP("bg.bmp");

	if(cards_sheet==NULL || bg==NULL)
		return false;

	return true;

}

//embaralha as cartas
void Embaralhar()
{
	//Variaveis auxiliares temporarias
	Carta aux= Carta();
	int i,iCarta;

    //  embaralha cartas
	srand((unsigned)time(0));
    for(int i = 0; i < 52; i++) {
        iCarta = rand() % 52;

        aux             = baralho[i];
        baralho[i]      = baralho[iCarta];
        baralho[iCarta] = aux;
    }
}

//impilha a carta e dá uma posição pra ela
void Empilha_Carta(Carta &c,Pilha<Carta> *p,int x, int y)
{

	if(p->Vazia())
	{
		//Posiciona a carta em uma posição na pilha na tela
		c.Posicionar(x,y);
	}
	else
	{
		//Posiciona a carta em uma posição na pilha na tela, mas especificamente, a 40 pixels abaixo da anterior
		c.Posicionar(p->GetTopo().getX(),p->GetTopo().getY()+40);
	}
	
	//Empilha a carta na pilha
	p->Empilha(c);
	
}

//impilha e posiciona as pilhas do jogo
void Prepara_Pilhas()
{
	int cont=0,i;

	//  preenchendo as pilhas com as 34 primeiras cartas (7 pilhas, com 5 cartas cada uma) e da as posições em que elas vao estar na hora da impressão
    for(i=0;i<4; i++) 
	{		
		//faz o mesmo para todas as pilhas

		//Empilha e Posiciona a carta em uma posição na pilha na tela
		Empilha_Carta(baralho[cont],p1,290,230);
		//adiciona no contador para q ande no vetor
		cont++;

		//Empilha e Posiciona a carta em uma posição na pilha na tela
		Empilha_Carta(baralho[cont],p2,420,230);
		//adiciona no contador para q ande no vetor
		cont++;

		//Empilha e Posiciona a carta em uma posição na pilha na tela
		Empilha_Carta(baralho[cont],p3,540,230);
		//adiciona no contador para q ande no vetor
		cont++;

		//Empilha e Posiciona a carta em uma posição na pilha na tela
		Empilha_Carta(baralho[cont],p4,660,230);
		//adiciona no contador para q ande no vetor
		cont++;

		//Empilha e Posiciona a carta em uma posição na pilha na tela
		Empilha_Carta(baralho[cont],p5,780,230);
		//adiciona no contador para q ande no vetor
		cont++;

		//Empilha e Posiciona a carta em uma posição na pilha na tela
		Empilha_Carta(baralho[cont],p6,900,230);
		//adiciona no contador para q ande no vetor
		cont++;
	}

	i=0;
	while(cont<38)
	{
		//coloca as cartas restantes no bouquê
		bouque[cont-24]=baralho[cont];
		//Posiciona a carta em uma posição no buquê na tela
		baralho[cont].Posicionar(0,i*(32));		

		cont++;
		i++;		
	}
	i=0;
	while(cont<53)
	{
		//coloca as cartas restantes no bouquê
		bouque[cont-24]=baralho[cont];
		//Posiciona a carta em uma posição no buquê na tela
		baralho[cont].Posicionar(146,i*(32));		

		cont++;
		i++;		
	}
    
}

//desenha todas as cartas sendo usadas na tela 
void Print_All()
{
	for(int i=0;i<53;i++)
		//Imprime a carta
		Print_Carta(baralho[i],cards_sheet,screen);
}

//função que checa se o clique do mouse foi encima de uma carta e retorna o referencial dela na lista de cartas
int Clique_Carta(int x, int y)
{	
	int cont=-1;

	while(cont<52)
	{
		cont++;

		//ve se as posições x e y estão dentro da posição de uma carta
		if((x>baralho[cont].getX()) && (x<(baralho[cont].getX()+baralho[cont].getW())) && (y>baralho[cont].getY()) && (y<(baralho[cont].getY()+baralho[cont].getH())))
		{
			//se está tirando do bouquê
			if(x<106)
			{
				//e foi clicado na parte visivel da carta
				if(y<(baralho[cont].getY()+32))
					return cont;
			}
			return cont;
			
		}
	}	
	return -1;
}

//checa se a carta pode ser colocada naquela pilha
bool Encaixa_Pilha(Pilha<Carta> p, Carta c)
{
	//checa se a carta é sequencia do topo da pilha
	if(p.GetTopo().getNum()==(c.getNum()+1))
	{
		return true;
	}
	
	return false;
}

//checa se a carta pode ser colocada naquela fundação
bool Encaixa_Fund(Carta f, Carta c)
{
	//checa se o naipe dos dois é igual e se a carta é seuqencia do topo da fundação
	if((f.getNaipe()==c.getNaipe())&&(f.getNum()==c.getNum()-1))
	{
		return true;
	}
	
	return false;
}

//Remove uma carta específica independente de onde ela esteja
bool Remove(Carta &c)
{
	Carta aux;	

	//checa primeiro se a carta estava no buquê
	for(int i=0;i<28;i++)
	{
		if((bouque[i].getNaipe()==c.getNaipe())&&(bouque[i].getNum()==c.getNum()))
		{
			//Troca o valor da carta por um valor invalido 
			bouque[i].setNum(0);
			return true;
		}
	}

	//Agora checa no topo das pilhas, já que o topo é a unica parte de onde pode sair a carta vinda das pilha
	if((p1->GetTopo().getNaipe()==c.getNaipe())&&(p1->GetTopo().getNum()==c.getNum()))
	{
		p1->Desempilha(aux);
		return true;
	}
	if((p2->GetTopo().getNaipe()==c.getNaipe())&&(p2->GetTopo().getNum()==c.getNum()))
	{
		p2->Desempilha(aux);
		return true;
	}
	if((p3->GetTopo().getNaipe()==c.getNaipe())&&(p3->GetTopo().getNum()==c.getNum()))
	{
		p3->Desempilha(aux);
		return true;
	}
	if((p4->GetTopo().getNaipe()==c.getNaipe())&&(p4->GetTopo().getNum()==c.getNum()))
	{
		p4->Desempilha(aux);
		return true;
	}
	if((p5->GetTopo().getNaipe()==c.getNaipe())&&(p5->GetTopo().getNum()==c.getNum()))
	{
		p5->Desempilha(aux);
		return true;
	}
	if((p6->GetTopo().getNaipe()==c.getNaipe())&&(p6->GetTopo().getNum()==c.getNum()))
	{
		p6->Desempilha(aux);
		return true;
	}
	return false;
}

//decide o que fazer quando a carta é solta
void Soltar_Carta(Carta &c,int x,int y)
{
	//checa se a carta foi solta na 1 pilha
	if(x>290 && x<396 && y>230 && Encaixa_Pilha(*p1,c))
	{
		//remove do local antigo e coloca na pilha
		Remove(c);
		Empilha_Carta(c,p1,290,230);
		arrastando=false;		
	}
	//checa se a carta foi solta na 2 pilha
	else if(x>420 && x<526 && y>230 && Encaixa_Pilha(*p2,c))
	{
		//remove do local antigo e coloca na pilha
		Remove(c);
		Empilha_Carta(c,p2,420,230);
		arrastando=false;		
	}
	//checa se a carta foi solta na 3 pilha
	else if(x>540 && x<646 && y>230 && Encaixa_Pilha(*p3,c))
	{
		//remove do local antigo e coloca na pilha
		Remove(c);
		Empilha_Carta(c,p3,540,230);
		arrastando=false;		
	}
	//checa se a carta foi solta na 4 pilha
	else if(x>660 && x<766 && y>230 && Encaixa_Pilha(*p4,c))
	{
		//remove do local antigo e coloca na pilha
		Remove(c);
		Empilha_Carta(c,p4,660,230);
		arrastando=false;		
	}
	//checa se a carta foi solta na 5 pilha
	if(x>780 && x<886 && y>230 && Encaixa_Pilha(*p5,c))
	{
		//remove do local antigo e coloca na pilha
		Remove(c);
		Empilha_Carta(c,p5,780,230);
		arrastando=false;		
	}
	//checa se a carta foi solta na 6 pilha
	if(x>900 && x<1006 && y>230 && Encaixa_Pilha(*p6,c))
	{
		//remove do local antigo e coloca na pilha
		Remove(c);
		Empilha_Carta(c,p6,900,230);
		arrastando=false;		
	}
	//se a carta não foi deixada em cima de nenhum pilha 
	else
	{
		baralho[cr].Posicionar(pos[0],pos[1]);
		arrastando=false;
	}
}

//prepara as cariaveis do jogo e o SDL
bool Init()
{
	//Cria todas as cartas e poe em um vetor
	CreateCards();

	//Embaralha as cartas
	Embaralhar();

	//Arruma todas as pilhas
	Prepara_Pilhas();

	//Inicaliza o SDL
	if(SDL_Init (SDL_INIT_EVERYTHING)==-1)
		return false;	

	//Prepara a tela
	screen = SDL_SetVideoMode(ScreenW,ScreenH,Screebpp,SDL_SWSURFACE);

	//teste se a tela carregou direito
	if(screen == NULL)
		return false;	

	//Da titulo a pagina
	SDL_WM_SetCaption("Jardim de Flores HARDCORE",NULL);

	return true;
}

//limpa o que foi iniciado
void CleanUp()
{
	//Libera as imagens
	SDL_FreeSurface(cards_sheet);
	SDL_FreeSurface(bg);
	SDL_FreeSurface(screen);

	//Sai do SDL
	SDL_Quit();

	//libera os ponteiros
	delete(p1,p2,p3,p4,p5,p6,fund1,fund2,fund3,fund4);
}


int main( int argc, char* args[] ){	

	//Carrega os arquivos
	if(LoadFile()==false)
		return 1;

	//Inicia o SDL ,informações da tela e das pilhas
	if(!(Init()))
		return 1;	
	
	//game loop
	while(!sair)
	{
		//Coloca o background
		Apply_Surface(0,0,bg,screen);

		//Enquanto tiver um evento para manipular, testarei todos os eventos possiveis
		while(SDL_PollEvent( &evento ))
		{

			//variaveis de apoio para guardar a posição x e y do mouse e o referencial da carta na lista de cartas
			int mx=0,my=0;

			//se o usuario clicou para sair da tela
			if(evento.type==SDL_QUIT)
				sair=true;

			//clique de um botão do mouse
			if(evento.type==SDL_MOUSEBUTTONDOWN)
			{
				//se o clique for com o botão esquerdo
				if(evento.button.button==SDL_BUTTON_LEFT)
				{
					//posição onde o mouse clicou
					mx=evento.button.x;
					my=evento.button.y;	

					//se não estiver arrastando uma carta, checa se clicou em uma carta clique
					if(!arrastando)
					{				
						//procura se clicou encima de uma carta e guarda na variavel "cr" o seu referencial na lista de cartas
						cr=Clique_Carta(mx,my);

						//e se clicou entra no modo de arrastar e guarda a antiga posição da carta
						if(cr!=-1)
						{
							arrastando=true;
							pos[0]=baralho[cr].getX();
							pos[1]=baralho[cr].getY();

						}
							
					}		
					//se já estiver arrastando uma carta e for solta-la
					else
					{
						Soltar_Carta(baralho[cr],mx,my);
						
					}
					
				}			

			}

			//se estiver no modo de arrastar
			if(arrastando)
			{
				//se o mouse se moveu
				if(evento.type==SDL_MOUSEMOTION)
				{
					//posição para onde o mouse se moveu
					mx = evento.motion.x; 
					my = evento.motion.y;

					//posiciona a carta que está sendo arrastada para a posição do mouse
					baralho[cr].Posicionar(mx,my);
				}
			}
			
			
		}	
		
		//Coloca todas as cartas na tela
		Print_All();

		//Atualiza a tela
		if(SDL_Flip(screen)==-1)
			return 1;
		
		
	}	

	//limpeza
	CleanUp();

	//fim do jogo
	return 0;
}