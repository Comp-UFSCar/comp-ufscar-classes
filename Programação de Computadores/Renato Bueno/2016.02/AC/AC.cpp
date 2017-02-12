#include "AC.h"

using std::cout;
using std::endl;


AC::AC(string mod)
{
	modelo = mod;
	temp = 23;
	velocidade = 1;
	ligado = false;
}

void AC::diminuiTemp()
{
    if (ligado)
	{
		if (temp > 15)
		{
		temp = temp - 1;
		}

	}
}

void AC::aumentaTemp()
{
	if (ligado)
	{
		if (temp < 30)
		{
		temp = temp + 1;
		}

	}
}

void AC::aumentaVelocidade()
{
	if (ligado)
	{
		if (velocidade < 3)
		{
			velocidade = velocidade + 1;

		}
	}
}

void AC::diminuiVelocidade()
{
	if (ligado)
	{
		if (velocidade > 1)
		{
			velocidade = velocidade - 1;

		}
	}
}

void AC::liga_desliga()
{
	ligado = !ligado;
}

int AC::getTemp()
{    
return temp;    
}

string AC::getModelo()
{    
return modelo;    
}



int AC::getVelocidade()
{    
return velocidade;    
}

bool AC::getLigado()
{    
return ligado;    
}


void AC::mostra()
{
	if (getLigado())
	{
		cout << "AC modelo "<< getModelo()<< " Ligado, Temperatura: " << getTemp() << " Velocidade: " << getVelocidade() << endl;

	}
	else

	{
		cout << "AC modelo "<< modelo<< " Desligado" << endl;
	}
}

