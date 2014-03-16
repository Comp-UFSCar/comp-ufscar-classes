# include <iostream>
# include "Node.h"

using namespace std;

# include "Tree.h"

int main() {

    int ValorRemov;
    Tree<int> *seringueira = new Tree<int>();

    cout << "\nPrograma teste para a implementacao da Arvore Binaria de Busca!\n\n";

    cout << "Sera que ela esta vazia?\n" << (seringueira->vazio() ? "Sim! Obviamente ne? Ela acabou de ser criada..." : "Nao! Isso ee um bug serio!") << endl;

    cout << "\n\nInserindo alguns elementos:\n\n";
    cout << (seringueira->insere(1) ? "Inseriu 1!" : "Falha ao inserir!") << endl;
    cout << (seringueira->insere(2) ? "Inseriu!" : "Falha ao inserir!") << endl;
    cout << (seringueira->insere(5) ? "Inseriu!" : "Falha ao inserir!") << endl;
    cout << (seringueira->insere(900) ? "Inseriu!" : "Falha ao inserir!") << endl;
    cout << (seringueira->insere(33) ? "Inseriu!" : "Falha ao inserir!") << endl;
    cout << (seringueira->insere(3) ? "Inseriu!" : "Falha ao inserir!") << endl;
    cout << (seringueira->insere(7) ? "Inseriu!" : "Falha ao inserir!") << endl;
    cout << (seringueira->insere(9) ? "Inseriu!" : "Falha ao inserir!") << endl;
    cout << (seringueira->insere(1000) ? "Inseriu!" : "Falha ao inserir!") << endl;
    cout << (seringueira->insere(10) ? "Inseriu!" : "Falha ao inserir 10!") << endl;
    cout << (seringueira->insere(1) ? "Inseriu 1!" : "Falha ao inserir 1! Claro, ele jaa esta na pilha!") << endl;

    cout << "\nPerguntando se ela esta vazia de novo...\n" << (!seringueira->vazio() ? "Nao! Obviamente ne? Inserimos alguns elementos..." : "Sim! Isso ee um bug serio!") << endl;

    cout << "\nEm ordem:" << endl;
    seringueira->ExibirEmOrdem();

    cout << "\n\nPre-ordem:" << endl;
    seringueira->ExibirPreOrdem();

    cout << "\n\nPos-ordem:" << endl;
    seringueira->ExibirPosOrdem();

    ValorRemov = 102301392;
    cout << "\n\nTentando remover o 102301392: ";
    cout << (seringueira->remove(ValorRemov) ? ("Removi o numero!") : "Ops! Nao removeu! Tem certeza que ele esta na arvore?") << endl;

    ValorRemov = 900;
    cout << "Tentando remover o 900: ";
    cout << (seringueira->remove(ValorRemov) ? ("Removi o numero!") : "Ops! Nao removeu! Tem certeza que ele esta na arvore?") << endl;

    ValorRemov = 1;
    cout << "Tentando remover o 1: ";
    cout << (seringueira->remove(ValorRemov) ? ("Removi o numero!") : "Ops! Nao removeu! Tem certeza que ele esta na arvore?") << endl;

    cout << endl << "Em ordem:" << endl;
    seringueira->ExibirEmOrdem();

    cout << endl;
    delete seringueira;

    return 0;
}
