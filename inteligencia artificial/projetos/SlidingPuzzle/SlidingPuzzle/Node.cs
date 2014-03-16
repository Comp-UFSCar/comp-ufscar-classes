using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace SlidingPuzzle
{
    class Node
    {
        List<Node> filhos;
        public Node pai;
        public int posicaoVazia;

        public Node(Ficha[] fichaJogada, int g,Node pai,int posicaoVazia)
        {
            // TODO: Complete member initialization
            this.fichaJogada = fichaJogada;
            this.posicaoVazia = posicaoVazia;
            this.g = g; // Grau que está, calculado exatamente
            this.filhos = new List<Node>();
            this.pai = pai;
        }

        public Ficha[] fichaJogada;
     

        public Node inserirFilho(Ficha[] fichaJogada, int custo,int posicaoVazia)
        {
            filhos.Add(new Node(fichaJogada, this.g + custo,this,posicaoVazia));
            return (Node) filhos.Last();
        }

        public int h;
        

        public int g;
       

        public int getF() { return this.g + this.h; }
    }

    
}
