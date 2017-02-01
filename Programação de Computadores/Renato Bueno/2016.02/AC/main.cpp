#include <iostream>
#include "AC.h"

using std::cin;
using std::cout;
using std::endl;


int main() {

	AC MeuAr;
	int op = 1;
	while (op!=0)
	{
		system("cls");
		cout << "1) Aumentar Temperatura" << endl;
		cout << "2) Diminuir Temperatura" << endl;
		cout << "3) Aumentar Velocidade" << endl;
		cout << "4) Diminuir Velocidade"<< endl;
		cout << "5) Liga/Desliga" << endl;
		cout << "0) Sair" << endl;
		cout << endl;
		MeuAr.mostra();

		cin >> op;

		switch (op) 
		{
		case 1:
			MeuAr.aumentaTemp();
			break;
		case 2:
			MeuAr.diminuiTemp();
			break;
		case 3:
			MeuAr.aumentaVelocidade();
			break;
		case 4:
			MeuAr.diminuiVelocidade();
			break;
		case 5:
			MeuAr.liga_desliga();
			break;
		}
	}

    system("pause"); 
	return 0;
}
