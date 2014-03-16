using Microsoft.Xna.Framework.Graphics;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace SlidingPuzzle
{
    class LogicaIA
    {
        int eficiencia; // Medirá a eficiencia do algoritimo (quantos nós ele analisou)
        Node noAtual; // Para manipulação de dados no raciocínio e na geração do caminho em si
        List<Node> OPEN; // Lista de caminhos em aberto pra solucao (vem do algoritimo best-first)
        List<Node> CLOSED; // Lista de caminhos já fechados de verificação
        Tree arvoreConstruida;
        LinkedList<Node> caminhoSolucao;
        Texture2D texturaFichaBranca, texturaFichaPreta; // Textura das fichas, apenas para exibição

        bool acabou; // variavel para verificar se chegou a uma solução final
        public LogicaIA(Texture2D texturaBranca,Texture2D texturaPreta,Tree arvore) 
        {
            // Texturas são passadas já no construtor, não precisa se preocupar com isso
            
            this.OPEN = new List<Node>();
            this.arvoreConstruida = arvore;
            this.arvoreConstruida.raiz.h = calculaHeuristica1(); // Há necessidade de calcular a heuristica do estado inicial
            OPEN.Add(this.arvoreConstruida.raiz); // Lista OPEN inicialmente só contem o estado inicial
            this.CLOSED = new List<Node>();
            eficiencia = 0;
            this.texturaFichaBranca = texturaBranca;
            this.texturaFichaPreta = texturaPreta;
            this.acabou = false;
        }

        // No contexto utilizado, esse "efetuaJogada" apenas atualiza para o usuario a jogada realizada, porem a IA já calculou toda a jogada
        public Ficha[] efetuaJogada()
        {
            if (this.acabou)
            {
                Ficha[] jogadaAtual = caminhoSolucao.First().fichaJogada;
                caminhoSolucao.RemoveFirst();
                return jogadaAtual;
                
            }

            return null;
        }

        // Método de raciocinio da IA, usando o algoritimo best-first
        public void raciocina() 
        {
            // Enquanto nao achou solucao e há caminhos em aberto
            while (!acabou && OPEN.Count>0)
            {
                eficiencia++;
                noAtual = getMaisPromissor(); // Pega nó mais promissor, onde o F é menor
                OPEN.Remove(noAtual); // Remove o nó dos caminhos em aberto, porque ele será analisado
                int pretoEsq = 0; // Variavel para verificacao
                
                // verifica o numero de fichas pretas em uma comparação entre as 4 primeiras posicoes do vetor de fichas
                Ficha[] fichaIter;
                for (int i = 0; i < 3; i++)
                {
                    fichaIter = noAtual.fichaJogada;

                    if (fichaIter[i].tipoFicha < fichaIter[i + 1].tipoFicha && fichaIter[i + 1].tipoFicha != 2)
                        pretoEsq++;


                }
                /// Jogo continua se tiver alguma peça preta à esquerda de uma branca
                if (pretoEsq > 0)
                {
                    //Gerar cada caso de jogada possivel
                    // <<AQUI AINDA FALTA FAZER AS JOGADAS POSSIVEIS>>
                    // Após fazer a lógica de jogadas possiveis, por favor coloquem bonitinho como nó filho do noAtual e como "pai" o noAtual em si.

                    List<Node> jogadasPossiveis = new List<Node>();
                    


                    bool estaOpen, estaClosed; // variaveis para controle
                    int indice = 0; // variavel para controle do indice do CLOSED
                    // Dentro das jogadas possiveis, verificaremos
                    foreach (var no in jogadasPossiveis)
                    {
                        estaOpen = false;
                        estaClosed = false;
                        indice = 0;
                        // Verifica se está no OPEN
                        for (int j = 0; j < OPEN.Count; j++)
                        {
                            if(compararJogada(OPEN[j], no))
                            {
                                estaOpen = true;
                                indice = j;
                                break;
                            }
                        }
                        
                        // Verifica se está no CLOSED
                        for (int j = 0; j < CLOSED.Count; j++)
                        {
                            if (compararJogada(CLOSED[j], no))
                            {
                                estaClosed = true;
                                indice = j;
                                break;
                            }
                        }

                        if ((!estaOpen)&&(!estaClosed))
                        {
                            // Se nao estiver em nenhum dos 2, calcula a heuristica e coloca no OPEN
                            no.h = calculaHeuristica1();
                            OPEN.Add(no);
                        }
                        else if (estaClosed)
                        {
                            // se estiver no CLOSED, verificara se tem funcao F menor que os demais do OPEN, se sim, voltara ao OPEN
                            if (no.getF() < getMaisPromissor().getF())
                            {
                                CLOSED.RemoveAt(indice);
                                OPEN.Add(no);
                            }

                        }
                        else 
                        {
                            // se ja estiver no OPEN, verificara se chegou num caminho mais curto
                            if (no.getF() < OPEN[indice].getF())
                            {
                                OPEN[indice].g = no.g;
                            }
                        }
                        
                    }
                }
                else
                {
                    this.acabou = true;
                    geraCaminhoSolucao();
                    
                }

                CLOSED.Add(noAtual); // Adiciona ao CLOSED o no porque terminou sua analise
            }
        }

        private void geraCaminhoSolucao()
        {
            caminhoSolucao.AddFirst(noAtual);
            Node no = noAtual;
            while (no.pai != null)
            {
                caminhoSolucao.AddFirst(no.pai);
                no = no.pai;
            }
        }

        // compara as fichas e verifica se os dois nós são iguais
        private bool compararJogada(Node no1, Node no2)
        {
            Ficha[] f1 = no1.fichaJogada;
            Ficha[] f2 = no2.fichaJogada;
            bool igual = true;
            for(int i = 0; i < f1.Length; i++)
            {
                if (f1[i].tipoFicha != f2[i].tipoFicha)
                {
                    igual = false;
                    break;
                }
            }

            return igual;
        }

        // Pega o nó mais promissor na lista OPEN
        Node getMaisPromissor() 
        {
            Node noReturn = new Node(null,Int32.MaxValue,null);
            noReturn.h = 0;

            // Pega o nó com o menor valor de F
            foreach (var no in OPEN)
            {
                if (no.getF() < noReturn.getF())
                    noReturn = no; 
            }
            return noReturn;
        }

        // <<AINDA PRECISA IMPLEMENTAR A HEURISTICA, USANDO A VARIAVEL noAtual>>
        public int calculaHeuristica1() 
        {
            int h = 0;

            return h;
        }

        // <<AINDA PRECISA IMPLEMENTAR A SEGUNDA HEURISTICA, USANDO A VARIAVEL noAtual>>
        public int calculaHeuristica2() 
        {
            int h = 0;

            return h;
        }
    }
}
