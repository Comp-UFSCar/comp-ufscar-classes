using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Graphics;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace SlidingPuzzle
{
    class Ficha : DrawableGameComponent
    {
        public Texture2D texturaFicha;
        Vector2 posicao;
        SpriteBatch spriteBatch;
        public int tipoFicha;
        Game game;

        public Ficha(Game game,int tipoFicha,Vector2 posicao,Texture2D texturaFicha):base(game) 
        {
            this.game = game;
            this.posicao = posicao;
            this.texturaFicha = texturaFicha;
            this.tipoFicha = tipoFicha; // 0 = preta, 1 = vazia e 2 = branca
            this.Initialize();
        }

        public void mudaFicha(int tipo,Texture2D textura)
        {
            this.tipoFicha = tipo;
            this.texturaFicha = textura;
        }

        public override void Initialize()
        {
            spriteBatch = (SpriteBatch)Game.Services.GetService(typeof(SpriteBatch));
            base.Initialize();
        }

        public override void Update(GameTime gameTime)
        {
            base.Update(gameTime);
        }

        public override void Draw(GameTime gameTime)
        {
            if(texturaFicha!=null)
                spriteBatch.Draw(texturaFicha, posicao, Color.White);

            base.Draw(gameTime);
        }



        public static void copiaFichas(Game game,Ficha[] origem, Ficha[] destino)
        {
            for (int i = 0; i < origem.Length; i++)
            {
                destino[i] = new Ficha(game,origem[i].tipoFicha, origem[i].posicao, origem[i].texturaFicha);
            }
        }
    }

    
}
