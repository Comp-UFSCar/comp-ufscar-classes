using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace SlidingPuzzle
{
    class Tree
    {
        public Node raiz { get; private set; }

        public Tree(Ficha[] fichasIniciais)
        {
            this.raiz = new Node(fichasIniciais,0,null);
        }

        public Node inserir(Node no, Ficha[] fichaJogada, int custo)
        {

            return no.inserirFilho(fichaJogada, this.raiz.g + custo);
        }

    }
}
