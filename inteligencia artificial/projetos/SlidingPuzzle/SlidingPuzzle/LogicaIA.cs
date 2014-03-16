using Microsoft.Xna.Framework;
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
        Game game;
        LinkedList<Node> caminhoSolucao;
        

        bool acabou; // variavel para verificar se chegou a uma solução final
        public LogicaIA(Game game,Tree arvore) 
        {
            // Texturas são passadas já no construtor, não precisa se preocupar com isso
            this.game = game;
            this.OPEN = new List<Node>();
            this.arvoreConstruida = arvore;
            this.caminhoSolucao = new LinkedList<Node>();
            this.arvoreConstruida.raiz.h = calculaHeuristica2(this.arvoreConstruida.raiz); // Há necessidade de calcular a heuristica do estado inicial
            OPEN.Add(this.arvoreConstruida.raiz); // Lista OPEN inicialmente só contem o estado inicial
            this.CLOSED = new List<Node>();
            eficiencia = 0;
            this.acabou = false;
        }

        // No contexto utilizado, esse "efetuaJogada" apenas atualiza para o usuario a jogada realizada, porem a IA já calculou toda a jogada
        public Ficha[] efetuaJogada()
        {

            Ficha[] jogadaAtual = caminhoSolucao.First().fichaJogada;
            caminhoSolucao.RemoveFirst();
            return jogadaAtual;
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
                int brancoEsq = 0;
                // verifica o numero de fichas pretas em uma comparação entre as 4 primeiras posicoes do vetor de fichas
                Ficha[] fichaIter = noAtual.fichaJogada;
                for (int i = 0; i < 3; i++)
                {


                    if (fichaIter[i].tipoFicha == 0)
                        pretoEsq++;
                    else if (fichaIter[i].tipoFicha == 2)
                        brancoEsq++;
                 


                }

                if (fichaIter[3].tipoFicha == 0)
                    pretoEsq--;

                /// Jogo continua se tiver alguma peça preta à esquerda de uma branca
                if (pretoEsq > 0 || brancoEsq < 3)
                {
                    //Gerar cada caso de jogada possivel
             

                    List<Node> jogadasPossiveis = new List<Node>();
                    Ficha[] ordemFichas;
                    int custo;
                    int posicaoVazia;
                    // Para cada ficha, verifica jogadas possiveis
                    for (int i=0;i<noAtual.fichaJogada.Length ;i++)
                    {
                        posicaoVazia = noAtual.posicaoVazia;
                        // peça está a esquerda da posicaoVazia
                        if (i != posicaoVazia && posicaoVazia - i <= 3 && posicaoVazia-i>0)
                        {
                            //custo = 1 ou 2, 1 pra andar pro lado e pular 1 ficha e 2 se pular 2 fichas
                            if (posicaoVazia - i <= 2)
                                custo = 1;
                            else
                                custo = 2;

                            ordemFichas = new Ficha[7];
                           
                            // Isso é usado para copiar, nao apenas criar referencia
                            Ficha.copiaFichas(this.game, noAtual.fichaJogada, ordemFichas);

                            // Altera aqui a variavel ordemFichas, usando o mudaFicha
                            ordemFichas[posicaoVazia].mudaFicha(ordemFichas[i].tipoFicha, ordemFichas[i].texturaFicha);
                            ordemFichas[i].mudaFicha(1, null);
                            posicaoVazia = i;
                            

                            // cria o nó com a jogada e "o coloca na arvore"
                            Node jogada = noAtual.inserirFilho(ordemFichas, custo, posicaoVazia);
                            jogada.pai = noAtual;
                            jogadasPossiveis.Add(jogada);
                        }
                            // Peça está a direita da posicaoVazia
                        else if (i != posicaoVazia && i - posicaoVazia <= 3 && i - posicaoVazia > 0)
                        {

                            //custo = 1 ou 2, 1 pra andar pro lado e pular 1 ficha e 2 se pular 2 fichas
                            if (i - posicaoVazia <= 2)
                                custo = 1;
                            else
                                custo = 2;

                            ordemFichas = new Ficha[7];

                            // Isso é usado para copiar, nao apenas criar referencia
                            Ficha.copiaFichas(this.game, noAtual.fichaJogada, ordemFichas);

                            // Altera aqui a variavel ordemFichas, usando o mudaFicha
                            ordemFichas[posicaoVazia].mudaFicha(ordemFichas[i].tipoFicha, ordemFichas[i].texturaFicha);
                            ordemFichas[i].mudaFicha(1, null);
                            posicaoVazia = i;


                            // cria o nó com a jogada e "o coloca na arvore"
                            Node jogada = noAtual.inserirFilho(ordemFichas, custo, posicaoVazia);
                            jogada.pai = noAtual;
                            jogadasPossiveis.Add(jogada);
                        }

                        
                    }

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
                            no.h = calculaHeuristica2(no);
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
                                OPEN[indice].pai = no.pai;
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

        private void mostraCaminhoNaoUtilizado(Node no)
        {

        }

        public int getEficacia()
        {
            return caminhoSolucao.Count - 1;
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
            Node noReturn = new Node(null,Int32.MaxValue,null,-1);
            noReturn.h = 0;

            // Pega o nó com o menor valor de F
            foreach (var no in OPEN)
            {
                if (no.getF() < noReturn.getF())
                    noReturn = no; 
            }
            return noReturn;
        }

        
        public int calculaHeuristica1(Node no) 
        {
            int brancas = 0;
            int pretas = 0;
            
            for (int i = 0; i < 4; i++)
            {
                if (no.fichaJogada[i].tipoFicha == 0)
                    pretas++;
                else if (no.fichaJogada[i].tipoFicha == 2)
                    brancas++;
            }

            // Diferença entre pretas e brancas nas 4 primeiras posicoes
            // utilizado porque só é necessario analisar se as brancas estão à esquerda para terminar o jogo
            return pretas-brancas;
        }

        // <<AINDA PRECISA IMPLEMENTAR A SEGUNDA HEURISTICA, USANDO A VARIAVEL noAtual>>
        public int calculaHeuristica2(Node no) 
        {
            int h = 0;
            for (int i = 0; i < no.fichaJogada.Length; i++)
            {
                // se for ficha preta, entao verifica quantas brancas existem à direita
                if (no.fichaJogada[i].tipoFicha == 0)
                {
                    // para cada ficha branca que estiver à direita de uma preta, aumenta o valor de h
                    for (int j = i + 1; j < no.fichaJogada.Length; j++)
                    {
                        if (no.fichaJogada[i].tipoFicha == 2)
                            h++;
                    }
                }
            }

            // retorna h/2, pois a branca pode pular 2 fichas pretas logo de cara
            // isso impede que superestime o valor de h
            return h/2;
        }

        public bool temJogada()
        {
            return ((this.acabou) && (this.caminhoSolucao.Count > 0));
        }
    }
}
