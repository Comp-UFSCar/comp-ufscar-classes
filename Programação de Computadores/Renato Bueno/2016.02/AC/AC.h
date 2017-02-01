#include <iostream>
using std::string;

class AC
{

public:

	AC(string mod="ACPadrao");
	void aumentaTemp();
	void diminuiTemp();
	void aumentaVelocidade();
	void diminuiVelocidade();
	void liga_desliga();

    string getModelo();
    int getTemp();
    int getVelocidade();
    bool getLigado();
	void mostra();

private:
    string modelo;
	int temp;
	int velocidade;
	bool ligado;
};

