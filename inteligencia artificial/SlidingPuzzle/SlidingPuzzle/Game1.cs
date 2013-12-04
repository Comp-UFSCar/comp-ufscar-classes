#region Using Statements
using System;
using System.Collections.Generic;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Content;
using Microsoft.Xna.Framework.Graphics;
using Microsoft.Xna.Framework.Input;
using Microsoft.Xna.Framework.Storage;
using Microsoft.Xna.Framework.GamerServices;
#endregion

namespace SlidingPuzzle
{
    /// <summary>
    /// This is the main type for your game
    /// </summary>
    public class Game1 : Game
    {
        GraphicsDeviceManager graphics;
        SpriteBatch spriteBatch;
        Ficha[] fichas;
        int posicaoVazia;

        // Texturas
        Texture2D texturaFichaBranca;
        Texture2D texturaFichaPreta;
        Texture2D fundo;
        //Microsoft.Xna.Framework.Graphics.SpriteFont font;
        Tree arvoreSolucao;
        LogicaIA inteligenciaArtificial;

        KeyboardState oldState;
    
        public Game1()
            : base()
        {
            graphics = new GraphicsDeviceManager(this);
            Content.RootDirectory = "Content";
        }

        /// <summary>
        /// Allows the game to perform any initialization it needs to before starting to run.
        /// This is where it can query for any required services and load any non-graphic
        /// related content.  Calling base.Initialize will enumerate through any components
        /// and initialize them as well.
        /// </summary>
        protected override void Initialize()
        {
            // TODO: Add your initialization logic here
            fichas = new Ficha[7];
            oldState = Keyboard.GetState();
            // Create a new SpriteBatch, which can be used to draw textures.
            spriteBatch = new SpriteBatch(GraphicsDevice);

            // Add SpriteBatch as a service
            this.Services.AddService(typeof(SpriteBatch), spriteBatch);
            base.Initialize();

           

            Vector2 posicaoInicialFichas = new Vector2(100,300);
            int i = 0;
            for (i = 0; i < 3; i++)
            {
                fichas[i] = new Ficha(this, 0, posicaoInicialFichas, texturaFichaPreta);
                fichas[i].Initialize();
                posicaoInicialFichas+= new Vector2(80,0);
            }
            posicaoVazia = i;
            fichas[posicaoVazia] = new Ficha(this, 1, posicaoInicialFichas, null);
            posicaoInicialFichas += new Vector2(80, 0);
            i++;
            for (; i < 7; i++)
            {
                fichas[i] = new Ficha(this, 2, posicaoInicialFichas, texturaFichaBranca);
                fichas[i].Initialize();
                posicaoInicialFichas += new Vector2(80, 0);
            }

            this.arvoreSolucao = new Tree(fichas,posicaoVazia);
            inteligenciaArtificial = new LogicaIA(this,arvoreSolucao);
            inteligenciaArtificial.raciocina();
            int eficacia = inteligenciaArtificial.getEficacia();
            //pra começar efetuando a primeira jogada
            inteligenciaArtificial.efetuaJogada();
        }

        /// <summary>
        /// LoadContent will be called once per game and is the place to load
        /// all of your content.
        /// </summary>
        protected override void LoadContent()
        {
            


            // TODO: use this.Content to load your game content her
            texturaFichaBranca = this.Content.Load<Texture2D>("branca");
            texturaFichaPreta = this.Content.Load<Texture2D>("preta");
            fundo = this.Content.Load<Texture2D>("fundo");
        //    font = this.Content.Load<Microsoft.Xna.Framework.Graphics.SpriteFont>("GameFont");
        }

        /// <summary>
        /// UnloadContent will be called once per game and is the place to unload
        /// all content.
        /// </summary>
        protected override void UnloadContent()
        {
            // TODO: Unload any non ContentManager content here
        }

        /// <summary>
        /// Allows the game to run logic such as updating the world,
        /// checking for collisions, gathering input, and playing audio.
        /// </summary>
        /// <param name="gameTime">Provides a snapshot of timing values.</param>
        protected override void Update(GameTime gameTime)
        {
            if (GamePad.GetState(PlayerIndex.One).Buttons.Back == ButtonState.Pressed || Keyboard.GetState().IsKeyDown(Keys.Escape))
                Exit();

           
            // TODO: Add your update logic here

            // Quando o usuario apertar espaço, uma etapa da solucao será mostrada
            KeyboardState newState = Keyboard.GetState();

            if (oldState.IsKeyUp(Keys.Space) && newState.IsKeyDown(Keys.Space))
            {
                fichas = (inteligenciaArtificial.temJogada())?inteligenciaArtificial.efetuaJogada():fichas;
                
            }
            oldState = newState;

            
            base.Update(gameTime);
        }

    

        /// <summary>
        /// This is called when the game should draw itself.
        /// </summary>
        /// <param name="gameTime">Provides a snapshot of timing values.</param>
        protected override void Draw(GameTime gameTime)
        {
            GraphicsDevice.Clear(Color.White);
            
            // TODO: Add your drawing code here
            spriteBatch.Begin();
        //    spriteBatch.DrawString(font, "Hello World!", new Vector2(320, 240), Color.Blue);
            spriteBatch.Draw(fundo, Vector2.Zero, Color.White);

            // Apenas desenha as fichas na tela
            foreach (var ficha in fichas)
            {
                ficha.Draw(gameTime);
            }

            spriteBatch.End();
            base.Draw(gameTime);
        }
    }
}
