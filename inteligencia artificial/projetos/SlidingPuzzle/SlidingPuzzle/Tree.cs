using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace SlidingPuzzle
{
    class Tree
    {
        public Node raiz { get; private set; }

        public Tree(Ficha[] fichasIniciais,int posicaoVazia)
        {
            this.raiz = new Node(fichasIniciais,0,null,posicaoVazia);
        }

        

    }
}
