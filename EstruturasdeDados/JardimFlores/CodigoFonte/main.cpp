//============================================================================
// Name        : main.cpp
// Author      : Thiago Faria, João Vitor, Lucas David
// Version     :
// Copyright   : Your copyright notice
// Description :
//============================================================================

#include <ctime>
#include "SDL.h"
#include "Pilha.h"
#include "Carta.h"

Carta baralho[52], buque[28];   //  Lista de cartas e buque

int ScreenW = 1080,             //  tamanho da tela
    ScreenH = 700,
    Screebpp = 32;

//Ponteiros para as imagens
SDL_Surface* cards_sheet = NULL;    //  CARTAS A SEREM MOSTRADA
SDL_Surface* bg = NULL;             //  Plano de fundo
SDL_Surface* screen = NULL;         //  TELA

//Estrutra de evento
SDL_Event evento;

//Variável para sair, para arrastar, para guardar a carta que está sendo arrastada e sua posição antiga
bool sair = false,arrastando = false;
int pos[2];

//declaração da carta que vai ser arrastada
Carta *arrastada;

//declaração das cartas que receberão as fundações
Carta fundA;
Carta fund2;
Carta fundC;
Carta fund4;

//declarando pilhas que receberão as cartas
Pilha<Carta> *p1 = new Pilha<Carta>();
Pilha<Carta> *p2 = new Pilha<Carta>();
Pilha<Carta> *p3 = new Pilha<Carta>();
Pilha<Carta> *p4 = new Pilha<Carta>();
Pilha<Carta> *p5 = new Pilha<Carta>();
Pilha<Carta> *p6 = new Pilha<Carta>();

//função criada para melhor aplicar imagens na tela
void Apply_Surface (int x, int y,SDL_Surface *fonte,SDL_Surface *destino,SDL_Rect* clip=NULL){

	SDL_Rect limites;                               //  faz um retângulo temporário para guardar os limites da tela

	// dá os limites para o retângulo
	limites.x = x;
	limites.y = y;

	SDL_BlitSurface(fonte,clip,destino,&limites);   //  Blit surface
}

//função para desenhar uma carta na tela
void Print_Carta(Carta c,SDL_Surface *fonte,SDL_Surface *destino){

	SDL_Rect clipe; //  cria um retângulo temporario para dividir a spritesheet em pedaços do tamanho de cartas

	//tamanho da carta
	clipe.h = 167;
	clipe.w = 102;

	//posiciona o retângulo to tamanho da carta na posição exata onde está a carta a ser impressa
	clipe.x=(c.getNum()-1)*(c.getArea().w);
	clipe.y=(c.getNaipe()-1)*(c.getArea().h);


	//Mapeia a cor chave pra remove-la
	Uint32 colorkey=SDL_MapRGB(fonte->format,64,0,64);

	//transforma todos os pixels escolhidos em transparente
	SDL_SetColorKey(fonte,SDL_SRCCOLORKEY,colorkey);

	//Coloca a carta na tela
	Apply_Surface(c.getX(),c.getY(),fonte,destino,&clipe);
}

//desenha uma pilha na tela
void Print_Pilha(Pilha<Carta> &p){
	Carta c;
	Pilha<Carta> aux;

	//passa todas as cartas para uma pilha auxiliar para imprimir na ordem certa
	while(!p.Vazia()){
		p.Desempilha(c);
		aux.Empilha(c);
	}

	//imprime e reempilha a pilha original
	while(!aux.Vazia())	{
		aux.Desempilha(c);
		Print_Carta(c,cards_sheet,screen);
		p.Empilha(c);
	}
}

//função que cria todas as cartas que serão usadas no jogo
void CreateCards(){
	int i, j, cont = 0;

	for(i = 1; i < 5; i++){
		for(j = 1; j < 14; j++){
			baralho[cont] = Carta(j,i);
			cont++;
		}
	}

}

//carrega as imagens que serao usadas
bool LoadFile(){
	cards_sheet = SDL_LoadBMP("cartas.bmp");
	bg          = SDL_LoadBMP("bg.bmp");

	return cards_sheet != NULL && bg != NULL;
}

//embaralha as cartas
void Embaralhar(){

	Carta aux = Carta();    //  Variaveis auxiliares temporárias
	int i, iCarta;

    //  embaralha cartas
	srand((unsigned)time(0));
    for(int i = 0; i < 52; i++){
        iCarta = rand() % 52;

        aux             = baralho[i];
        baralho[i]      = baralho[iCarta];
        baralho[iCarta] = aux;
    }
}

//empilha a carta e dá uma posição pra ela
void Empilha_Carta(Carta &c,Pilha<Carta> *p,int x, int y){

	if(p->Vazia())
		c.Posicionar(x,y);  //  Posiciona a carta em uma posição na pilha na tela
	else
		//Posiciona a carta em uma posição na pilha na tela, mas especificamente, a 40 pixels abaixo da anterior
		c.Posicionar(p->GetTopo()->getX(),p->GetTopo()->getY()+40);

	p->Empilha(c);          //  Empilha a carta na pilha
}

//empilha e posiciona as pilhas do jogo
void Prepara_Pilhas(){
	int cont = 0, i;

	//  preenchendo as pilhas com as 34 primeiras cartas (6 pilhas, com 4 cartas cada uma) e da as posições em que
	//  elas vao estar na hora da impressão
    for(i = 0; i < 4; i++){
		//faz o mesmo para todas as pilhas

		Empilha_Carta(baralho[cont],p1,353,192);    //  Empilha e Posiciona a carta em uma posição na pilha na tela
		cont++;                                     //  adiciona no contador para q ande no vetor

		Empilha_Carta(baralho[cont],p2,473,192);    //  Empilha e Posiciona a carta em uma posição na pilha na tela
		cont++;                                     //  adiciona no contador para q ande no vetor

		Empilha_Carta(baralho[cont],p3,593,192);    //  Empilha e Posiciona a carta em uma posição na pilha na tela
		cont++;                                     //  adiciona no contador para q ande no vetor

		Empilha_Carta(baralho[cont],p4,713,192);    //  Empilha e Posiciona a carta em uma posição na pilha na tela
		cont++;                                     //  adiciona no contador para q ande no vetor

		Empilha_Carta(baralho[cont],p5,833,192);    //  Empilha e Posiciona a carta em uma posição na pilha na tela
		cont++;                                     //  adiciona no contador para q ande no vetor

		Empilha_Carta(baralho[cont],p6,953,192);    //  Empilha e Posiciona a carta em uma posição na pilha na tela
		cont++;                                     //  adiciona no contador para q ande no vetor
	}

	i = 0;

	//primeiro buque
	while(cont < 38){

		baralho[cont].Posicionar(0,i*(40)); //  Posiciona a carta em uma posição no buque na tela
		buque[cont -24] = baralho[cont];    //  coloca as cartas restantes no buque

		cont++;
		i++;
	}

	i = 0;
	//segundo buque
	while(cont < 53){
		baralho[cont].Posicionar(146,i*(40));   //  Posiciona a carta em uma posição no buque na tela
		buque[cont-24]=baralho[cont];           //  coloca as cartas restantes no buque
		cont++;
		i++;
	}

	//fundações
	fundA.Posicionar(352,10);
	fund2.Posicionar(552,10);
	fundC.Posicionar(752,10);
	fund4.Posicionar(952,10);
}

//desenha todas as cartas sendo usadas na tela
void Print_All(){
	//Imprime as pilhas
	Print_Pilha(*p1);
	Print_Pilha(*p2);
	Print_Pilha(*p3);
	Print_Pilha(*p4);
	Print_Pilha(*p5);
	Print_Pilha(*p6);

	//Imprime os buques
	for(int i = 0; i < 29; i++)
		Print_Carta(buque[i], cards_sheet, screen);

	//imprime as fundações
	Print_Carta(fundA, cards_sheet, screen);
	Print_Carta(fund2, cards_sheet, screen);
	Print_Carta(fundC, cards_sheet, screen);
	Print_Carta(fund4, cards_sheet, screen);

	if(arrastada != NULL)
		Print_Carta(*arrastada,cards_sheet,screen); //  imprime a carta que está sendo arrastada
}

//função que checa se o clique do mouse foi em cima de uma carta e retorna o referencial dela na lista de cartas
Carta* Clique_Carta(int x, int y){

	Carta aux;


	if(x < 106)                         //  se o clique foi no primeiro buque
		for(int i = 24; i < 39; i++)    //  checa qual carta foi clicada
			if(y > baralho[i].getY() && y < baralho[i].getY() +40)
				return &baralho[i];


	else if(x > 146 && x < 252)             //  se o clique foi no segundo buque
		for(int i = 38; i < 53; i++)
			if(y > baralho[i].getY() && y < baralho[i].getY() +40)
				return &baralho[i];


	else if(x > 350 && x < 456 && y > 192)    //  se o clique foi na 1 pilha
		return p1->GetTopo();

	else if(x > 470 && x < 576 && y > 192)    //  se o clique foi na 2 pilha
		return p2->GetTopo();

	else if(x > 590 && x < 696 && y > 192)    //  se o clique foi na 3 pilha
		return p3->GetTopo();

	else if(x > 710 && x < 816 && y > 192)    //  se o clique foi na 4 pilha
		return p4->GetTopo();

	else if(x > 830 && x < 936 && y > 192)    //  se o clique foi na 5 pilha
		return p5->GetTopo();

	else if(x > 950 && x < 1056 && y > 192)   //  se o clique foi na 6 pilha
		return p6->GetTopo();

    return &aux;
}

//checa se a carta pode ser colocada naquela pilha
bool Encaixa_Pilha(Pilha<Carta> p, Carta c){
	if(p.Vazia())                              //   se a pilha estiver vazia
		return true;

    if(p.GetTopo()->getNum() == c.getNum() +1) //   checa se a carta é sequencia do topo da pilha
        return true;

    return false;
}

//checa se a carta pode ser colocada naquela fundação
bool Encaixa_Fund(Carta f, Carta c){

	//checa se o naipe dos dois é igual e se a carta é seuqencia do topo da fundação
	if(f.getNaipe() == c.getNaipe() && f.getNum() == c.getNum() +1)
		return true;

	if(c.getNum() == 0 && f.getNum() == 1)
		return true;

	return false;
}

//Remove uma carta específica independente de onde ela esteja
bool Remove(Carta &c){

	Carta aux;

	//checa primeiro se a carta estava no buque
	for(int i = 0; i < 28; i++)
		if(buque[i].getNaipe() == c.getNaipe() && buque[i].getNum() == c.getNum()){
			buque[i].setNum(0); //  Troca o valor da carta por um valor invalido
			return true;
		}

	//Agora checa no topo das pilhas, já que o topo é a unica parte de onde pode sair a carta vinda das pilha
	if(p1->GetTopo()->getNaipe() == c.getNaipe() && p1->GetTopo()->getNum() == c.getNum()){
		p1->Desempilha(aux);
		return true;
	}

	if(p2->GetTopo()->getNaipe() == c.getNaipe() && p2->GetTopo()->getNum() == c.getNum()){
		p2->Desempilha(aux);
		return true;
	}

	if(p3->GetTopo()->getNaipe() == c.getNaipe() && p3->GetTopo()->getNum() == c.getNum()){
		p3->Desempilha(aux);
		return true;
	}

	if(p4->GetTopo()->getNaipe() == c.getNaipe() && p4->GetTopo()->getNum() == c.getNum()){
		p4->Desempilha(aux);
		return true;
	}

	if(p5->GetTopo()->getNaipe() == c.getNaipe() && p5->GetTopo()->getNum() == c.getNum()){
		p5->Desempilha(aux);
		return true;
	}
	if(p6->GetTopo()->getNaipe() == c.getNaipe() && p6->GetTopo()->getNum() == c.getNum()){
		p6->Desempilha(aux);
		return true;
	}

	return false;
}

//decide o que fazer quando a carta é solta
void Soltar_Carta(Carta &c,int x,int y){

	//checa se a carta foi solta na 1 pilha
	if(x > 350 && x < 456 && y > 192 && Encaixa_Pilha(*p1, c)){

		Remove(c);  //  remove do local antigo e coloca na pilha
		Empilha_Carta(c, p1, 350, 192);
		arrastando = false;
	}

	//checa se a carta foi solta na 2 pilha
	else if(x > 470 && x < 576 && y > 192 && Encaixa_Pilha(*p2, c)){

		Remove(c);  //  remove do local antigo e coloca na pilha
		Empilha_Carta(c, p2, 470, 192);
		arrastando = false;
	}

	//checa se a carta foi solta na 3 pilha
	else if(x > 590 && x < 696 && y > 192 && Encaixa_Pilha(*p3,c)){

		Remove(c);  //  remove do local antigo e coloca na pilha
		Empilha_Carta(c, p3, 590, 192);
		arrastando = false;
	}

	//checa se a carta foi solta na 4 pilha
	else if(x > 710 && x < 816 && y > 192 && Encaixa_Pilha(*p4, c)){

		Remove(c);  //  remove do local antigo e coloca na pilha
		Empilha_Carta(c, p4, 710, 192);
		arrastando = false;
	}

	//checa se a carta foi solta na 5 pilha
	else if(x > 830 && x < 936 && y > 192 && Encaixa_Pilha(*p5, c))
	{

		Remove(c);  //  remove do local antigo e coloca na pilha
		Empilha_Carta(c, p5, 830, 192);
		arrastando = false;
	}

	//checa se a carta foi solta na 6 pilha
	else if(x > 950 && x < 1056 && y > 192 && Encaixa_Pilha(*p6, c))
	{

		Remove(c);  //  remove do local antigo e coloca na pilha
		Empilha_Carta(c, p6, 950, 192);
		arrastando = false;
	}

	//checa se a carta foi solta na 1 fundãção
	else if(x > 352 && x < 458 && y < 167 && Encaixa_Fund(c, fundA))
	{

		Remove(c);  //  remove do local antigo e coloca na pilha
		fundA      = c;
		fundA.Posicionar(352, 10);
		arrastando = false;
	}

	//checa se a carta foi solta na 2 fundãção
	else if(x > 552 && x < 658 && y < 167 && Encaixa_Fund(c, fund2))
	{

		Remove(c);  //  remove do local antigo e coloca na pilha
		fund2      = c;
		fund2.Posicionar(552, 10);
		arrastando = false;
	}

	//checa se a carta foi solta na 3 fundãção
	else if(x > 752 && x < 858 && y < 167 && Encaixa_Fund(c, fundC))
	{

		Remove(c);  //  remove do local antigo e coloca na pilha
		fundC      = c;
		fundC.Posicionar(752,10);
		arrastando = false;
	}

	//checa se a carta foi solta na 4 fundãção
	else if(x > 952 && x < 1058 && y < 167 && Encaixa_Fund(c, fund4))
	{

		Remove(c);  //  remove do local antigo e coloca na pilha
		fund4      = c;
		fund4.Posicionar(952, 10);
		arrastando = false;
	}

	//se a carta não foi deixada em cima de nenhum pilha
	else
	{
		arrastada->Posicionar(pos[0], pos[1]);
		arrastando = false;
	}
}

//prepara as variaveis do jogo e o SDL
bool Init(){

	CreateCards();                          //  Cria todas as cartas e poe em um vetor
	Embaralhar();                           //  Embaralha as cartas
	Prepara_Pilhas();                       //  Arruma todas as pilhas

	if(SDL_Init(SDL_INIT_EVERYTHING)== -1)  //  Inicaliza o SDL
		return false;

	screen = SDL_SetVideoMode(ScreenW, ScreenH, Screebpp, SDL_SWSURFACE);  //  Prepara a tela


	if(screen == NULL)                      //  teste se a tela carregou direito
		return false;

	SDL_WM_SetCaption("Jardim de Flores HARDCORE",NULL);    //  Da titulo a pagina

	return true;
}

//limpa o que foi iniciado
void CleanUp(){


	SDL_FreeSurface(cards_sheet);   //  Libera as imagens
	SDL_FreeSurface(bg);
	SDL_FreeSurface(screen);


	SDL_Quit(); //  Sai do SDL


	delete (p1, p2, p3, p4, p5, p6);      //  libera os ponteiros
}


int main(int argc, char* args[]){


	if(LoadFile() == false) //  Carrega os arquivos
		return 1;

	if(!(Init()))           //  Inicia o SDL ,informações da tela e das pilhas
		return 1;

	//game loop
	while(!sair){
		Apply_Surface(0,0,bg,screen);   //  Coloca o background

		//Enquanto tiver um evento para manipular, testarei todos os eventos possiveis
		while(SDL_PollEvent(&evento)){

			//variaveis de apoio para guardar a posição x e y do mouse e o referencial da carta na lista de cartas
			int mx = 0,my = 0;

			//se o usuario clicou para sair da tela
			if(evento.type == SDL_QUIT)
				sair = true;


			if(evento.type == SDL_MOUSEBUTTONDOWN)          //  clique de um botão do mouse
				if(evento.button.button==SDL_BUTTON_LEFT){  //  se o clique for com o botão esquerdo

					//posição onde o mouse clicou
					mx = evento.button.x;
					my = evento.button.y;

					//se não estiver arrastando uma carta, checa se clicou em uma carta clique
					if(!arrastando){

						//procura se clicou encima de uma carta e guarda na variavel "cr" o seu referencial na lista de cartas
						arrastada = Clique_Carta(mx,my);

						//e se clicou entra no modo de arrastar e guarda a antiga posição da carta
						if(arrastada->getNum() != 0){

							arrastando = true;
							pos[0]     = arrastada->getX();
							pos[1]     = arrastada->getY();
						}
					}
					//se já estiver arrastando uma carta e for solta-la
					else
					{
						Soltar_Carta(*arrastada, mx, my);
						arrastada = NULL;
					}
				}

			//se estiver no modo de arrastar
			if(arrastando)
				//se o mouse se moveu
				if(evento.type == SDL_MOUSEMOTION){
					//posição para onde o mouse se moveu
					mx = evento.motion.x;
					my = evento.motion.y;

					//posiciona a carta que está sendo arrastada para a posição do mouse
					arrastada->Posicionar(mx,my);
				}
		}


		Print_All();                //  coloca todas as cartas na tela
		if(SDL_Flip(screen)==-1)    //  atualiza a tela
			return 1;
	}


	CleanUp();  //  limpeza
	return 0;   //  fim do jogo
}
