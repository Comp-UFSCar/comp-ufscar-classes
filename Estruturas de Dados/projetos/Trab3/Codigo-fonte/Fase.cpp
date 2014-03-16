#include "Fase.h"


Fase::Fase(int v, int d,string linha)
{
	int i=0;
	velocidade=v;
	distancia=d;
	while (linha[i] !=';')
	{
		CommandSpell aux(linha[i]);
		aux.Posicionar(500,0-(i*distancia));
		comandos.insere(aux);
		i++;
	}
	
}

Fase::Fase(void)
{
	CommandSpell aux;
	velocidade=1;
	distancia=1;
	(*this).comandos.insere(aux);
}

Fase::~Fase(void)
{
}

void Fase::mover()
{
	Node<CommandSpell> *aux;
	aux= comandos.getHeader()->dir;
	while(aux != comandos.getHeader())
	{
		aux->info.mover(velocidade);
		aux=aux->dir;
	}
}

void Fase::printAllCommandSpell(SDL_Surface *tela)
{
	Node<CommandSpell> *aux;
	aux=comandos.getHeader();
	aux=aux->esq;
	while(aux!=comandos.getHeader())
	{
		if(aux->info.getY()>(-140) && aux->info.getY()<(740))
		{
			aux->info.printCommandSpell(tela);			
		}
		aux=aux->esq;
	}
}

bool Fase::checarVitoria()
{
	Node<CommandSpell> *aux;
	aux=comandos.getHeader();
	aux=aux->esq;
	int acertos=0,erros=0;
	while(aux!=comandos.getHeader())
	{
		if(aux->info.getEstado()==CommandSpell::Estados::CORRETO)
		{
			acertos++;
		}
		else
			erros++;
		aux=aux->esq;
	}
	if((acertos/3)>erros)
		return true;
	return false;
}
