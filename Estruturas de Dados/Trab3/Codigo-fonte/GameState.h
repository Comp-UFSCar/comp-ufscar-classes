#pragma once
class GameState
{
		
public:
	enum Estados{MENU,INSTRU��ES,JOGO,VITORIA,SAIR};
	Estados estado;

	GameState(void);
	GameState(Estados est);
	~GameState(void);
	void TrocaEstado(Estados x);
	
};

