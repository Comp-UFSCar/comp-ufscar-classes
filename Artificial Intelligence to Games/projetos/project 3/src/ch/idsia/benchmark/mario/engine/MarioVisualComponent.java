/*
 * Copyright (c) 2009-2010, Sergey Karakovskiy and Julian Togelius
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the Mario AI nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package ch.idsia.benchmark.mario.engine;

import ch.idsia.agents.Agent;
import ch.idsia.agents.controllers.human.CheaterKeyboardAgent;
import ch.idsia.benchmark.mario.engine.level.BgLevelGenerator;
import ch.idsia.benchmark.mario.engine.level.Level;
import ch.idsia.benchmark.mario.engine.sprites.Mario;
import ch.idsia.benchmark.mario.engine.sprites.Sprite;
import ch.idsia.benchmark.mario.environments.Environment;
import ch.idsia.benchmark.mario.environments.MarioEnvironment;
import ch.idsia.tools.GameViewer;
import ch.idsia.tools.MarioAIOptions;
import ch.idsia.tools.Scale2x;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.image.VolatileImage;
import java.text.DecimalFormat;
import java.util.List;


/**
 * Created by IntelliJ IDEA. User: Sergey Karakovskiy, sergey at idsia dot ch Date: Feb 26, 2010 Time: 3:54:52 PM
 * Package: ch.idsia.benchmark.mario.engine
 */

public class MarioVisualComponent extends JComponent
{
private CheaterKeyboardAgent cheatAgent = null;

public int width, height;

public VolatileImage thisVolatileImage;
public Graphics thisVolatileImageGraphics;
public Graphics thisGraphics;

private MarioEnvironment marioEnvironment;
private LevelRenderer layer;
private BgRenderer[] bgLayer = new BgRenderer[2];

private Mario mario;
private Level level;

final private static DecimalFormat df = new DecimalFormat("00");
final private static DecimalFormat df2 = new DecimalFormat("000");

private static String[] LEVEL_TYPES = {"Overground(0)",
        "Underground(1)",
        "Castle(2)"};

private long tm = System.currentTimeMillis();
private long tm0;
int delay;
private KeyAdapter prevHumanKeyBoardAgent;
private String agentNameStr;
private GameViewer gameViewer = null;
private static MarioVisualComponent marioVisualComponent = null;

private Scale2x scale2x = new Scale2x(320, 240);

private MarioVisualComponent(MarioAIOptions marioAIOptions, MarioEnvironment marioEnvironment)
{
    this.marioEnvironment = marioEnvironment;
    adjustFPS();

    this.setFocusable(true);
    this.setEnabled(true);
    this.width = marioAIOptions.getViewWidth();
    this.height = marioAIOptions.getViewHeight();

    Dimension size = new Dimension(width, height);

    setPreferredSize(size);
    setMinimumSize(size);
    setMaximumSize(new Dimension(width * 2, height * 2));

    setFocusable(true);

    if (this.cheatAgent == null)
    {
        this.cheatAgent = new CheaterKeyboardAgent();
        this.addKeyListener(cheatAgent);
    }

//        System.out.println("this (from constructor) = " + this);

    GlobalOptions.registerMarioVisualComponent(this);

    if (marioAIOptions.isGameViewer())
    {
        if (this.gameViewer == null)
        {

            this.setGameViewer(new GameViewer(marioAIOptions));
            this.gameViewer.setMarioVisualComponent(this);
            this.gameViewer.setVisible(true);
        }
    }
}

public static MarioVisualComponent getInstance(MarioAIOptions marioAIOptions, MarioEnvironment marioEnvironment)
{
    if (marioVisualComponent == null)
    {
        marioVisualComponent = new MarioVisualComponent(marioAIOptions, marioEnvironment);
        marioVisualComponent.CreateMarioComponentFrame(marioVisualComponent);
    }
    return marioVisualComponent;
}

private static JFrame marioComponentFrame = null;

public void CreateMarioComponentFrame(MarioVisualComponent m)
{
    if (marioComponentFrame == null)
    {
        marioComponentFrame = new JFrame(/*evaluationOptions.getAgentFullLoadName() +*/ GlobalOptions.getBenchmarkName());
        marioComponentFrame.setContentPane(m);
        m.init();
        marioComponentFrame.pack();
        marioComponentFrame.setResizable(false);
        marioComponentFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    marioComponentFrame.setVisible(true);
    m.postInitGraphics();
}

public void setLocation(Point location)
{
    marioComponentFrame.setLocation(location.x, location.y);
}

public void setAlwaysOnTop(boolean b)
{
    marioComponentFrame.setAlwaysOnTop(b);
}

public void reset()
{
    adjustFPS();
    tm = System.currentTimeMillis();
    this.tm0 = tm;
}

public void tick()
{
//    this.render(thisVolatileImageGraphics, CheaterKeyboardAgent.isObserveLevel ? level.length : 0);
    this.render(thisVolatileImageGraphics);

    String msg = "Agent: " + this.agentNameStr;
    drawStringDropShadow(thisVolatileImageGraphics, msg, 0, 6, 5);

    msg = "PRESSED KEYS: ";
    drawStringDropShadow(thisVolatileImageGraphics, msg, 0, 7, 6);

    msg = "";
    if (mario.keys != null)
    {
        for (int i = 0; i < Environment.numberOfKeys; ++i)
            msg += (mario.keys[i]) ? LevelScene.keysStr[i] : "    ";
    } else
        msg = "NULL";
    drawString(thisVolatileImageGraphics, msg, 107, 61, 1);
    if (mario.keys[Mario.KEY_SPEED])
        thisVolatileImageGraphics.drawImage(Art.particles[0][3], 234, 59, 10, 10, null);

    if (mario.cheatKeys[CheaterKeyboardAgent.CHEAT_KEY_WIN])
        mario.win();

    if (!this.hasFocus() && (tm - tm0) / (delay + 1) % 42 < 20)
    {
        String msgClick = "CLICK TO PLAY";
        drawString(thisVolatileImageGraphics, msgClick, 160 - msgClick.length() * 4, 110, 2);
//            drawString(thisVolatileImageGraphics, msgClick, 160 - msgClick.length() * 4, 110, 7);
    }
//        thisVolatileImageGraphics.setColor(Color.DARK_GRAY);
    drawStringDropShadow(thisVolatileImageGraphics, "FPS: ", 33, 2, 7);
    drawStringDropShadow(thisVolatileImageGraphics, ((GlobalOptions.FPS > 99) ? "\\infty" : "  " + GlobalOptions.FPS.toString()), 33, 3, 7);

//        msg = totalNumberOfTrials == -2 ? "" : currentTrial + "(" + ((totalNumberOfTrials == -1) ? "\\infty" : totalNumberOfTrials) + ")";

//        drawStringDropShadow(thisVolatileImageGraphics, "Trial:", 33, 4, 7);
//        drawStringDropShadow(thisVolatileImageGraphics, msg, 33, 5, 7);

    if (GlobalOptions.isScale2x)
    {
        //TODO: handle this (what?)
        thisGraphics.drawImage(scale2x.scale(thisVolatileImage), 0, 0, null);
    } else
    {
        thisGraphics.drawImage(thisVolatileImage, 0, 0, null);
    }

//    thisGraphics.drawImage(thisVolatileImage, 0, 0, null);
    if (this.gameViewer != null)
        this.gameViewer.tick();
    // Delay depending on how far we are behind.
    if (delay > 0)
    {
//            System.out.println("delay = " + delay);
        try
        {
            tm += delay;
            Thread.sleep(Math.max(0, tm - System.currentTimeMillis()));
        } catch (InterruptedException ignored) {}
    }
}

private int recordIndicator = 20;

public void render(Graphics g)
{
    int xCam = (int) (mario.xOld + (mario.x - mario.xOld)) - 160;
    int yCam = (int) (mario.yOld + (mario.y - mario.yOld)) - 120;

    if (GlobalOptions.isCameraCenteredOnMario)
    {
    } else
    {
        //int xCam = (int) (xCamO + (this.xCam - xCamO) * cameraOffSet);
        //        int yCam = (int) (yCamO + (this.yCam - yCamO) * cameraOffSet);
        if (xCam < 0) xCam = 0;
        if (yCam < 0) yCam = 0;
        if (xCam > level.length * LevelScene.cellSize - GlobalOptions.VISUAL_COMPONENT_WIDTH)
            xCam = level.length * LevelScene.cellSize - GlobalOptions.VISUAL_COMPONENT_WIDTH;
        if (yCam > level.height * LevelScene.cellSize - GlobalOptions.VISUAL_COMPONENT_HEIGHT)
            yCam = level.height * LevelScene.cellSize - GlobalOptions.VISUAL_COMPONENT_HEIGHT;
    }
//          g.drawImage(Art.background, 0, 0, null);

    for (int i = 0; i < bgLayer.length; i++)
    {
        bgLayer[i].setCam(xCam, yCam);
        bgLayer[i].render(g); //levelScene.
    }

    g.translate(-xCam, -yCam);

    for (Sprite sprite : marioEnvironment.getSprites())          // levelScene.
        if (sprite.layer == 0) sprite.render(g);

    g.translate(xCam, yCam);

    layer.setCam(xCam, yCam);
    layer.render(g, marioEnvironment.getTick() /*levelScene.paused ? 0 : */);

    g.translate(-xCam, -yCam);

    for (Sprite sprite : marioEnvironment.getSprites())  // Mario, creatures
        if (sprite.layer == 1) sprite.render(g);

    g.translate(xCam, yCam);
    g.setColor(Color.BLACK);
    //layer.renderExit(g, marioEnvironment.getTick());

    drawStringDropShadow(g, "DIFFICULTY: " + df.format(marioEnvironment.getLevelDifficulty()), 0, 0, marioEnvironment.getLevelDifficulty() > 6 ? 1 : marioEnvironment.getLevelDifficulty() > 2 ? 4 : 7);
//    drawStringDropShadow(g, "CREATURES:" + (mario.levelScene.paused ? "OFF" : " ON"), 19, 0, 7);
    drawStringDropShadow(g, "SEED:" + marioEnvironment.getLevelSeed(), 0, 1, 7);
    drawStringDropShadow(g, "TYPE:" + LEVEL_TYPES[marioEnvironment.getLevelType()], 0, 2, 7);
    drawStringDropShadow(g, "ALL KILLS: " + marioEnvironment.getKilledCreaturesTotal(), 19, 0, 1);
    drawStringDropShadow(g, "LENGTH:" + (int) mario.x / 16 + " of " + marioEnvironment.getLevelLength(), 0, 3, 7);
    drawStringDropShadow(g, "HEIGHT:" + (int) mario.y / 16 + " of " + marioEnvironment.getLevelHeight(), 0, 4, 7);
    drawStringDropShadow(g, "by Fire  : " + marioEnvironment.getKilledCreaturesByFireBall(), 19, 1, 1);
//    drawStringDropShadow(g, "COINS    : " + df.format(Mario.coins), 0, 4, 4);
    drawStringDropShadow(g, "by Shell : " + marioEnvironment.getKilledCreaturesByShell(), 19, 2, 1);
    // COINS:
    g.drawImage(Art.level[0][2], 2, 43, 10, 10, null);
    drawStringDropShadow(g, "x" + df.format(Mario.coins), 1, 5, 4);
    g.drawImage(Art.items[0][0], 47, 43, 11, 11, null);
    drawStringDropShadow(g, "x" + df.format(Mario.mushroomsDevoured), 7, 5, 4);
    g.drawImage(Art.items[1][0], 89, 43, 11, 11, null);
    drawStringDropShadow(g, "x" + df.format(Mario.flowersDevoured), 12, 5, 4);
//    drawStringDropShadow(g, "MUSHROOMS: " + df.format(Mario.mushroomsDevoured), 0, 5, 4);
    drawStringDropShadow(g, "by Stomp : " + marioEnvironment.getKilledCreaturesByStomp(), 19, 3, 1);
//    drawStringDropShadow(g, "FLOWERS  : " + df.format(Mario.flowersDevoured), 0, 6, 4);

    if (GlobalOptions.isRecording)
    {
        --recordIndicator;
        if (recordIndicator >= 0)
        {
            g.setColor(Color.RED);
            g.fillOval(303, 4, 13, 13);//19 * 8 + 5, 39, 10, 10);
            g.setColor(Color.black);
            g.drawOval(303, 4, 13, 13);//19 * 8 + 5, 39, 10, 10);
        } else if (recordIndicator == -20)
            recordIndicator = 20;
    }
    if (GlobalOptions.isReplaying)
    {
        g.setColor(new Color(0, 200, 0));
        g.fillPolygon(new int[]{303, 303, 316}, new int[]{16, 4, 10}, 3);
        g.setColor(Color.black);
        g.drawPolygon(new int[]{303, 303, 316}, new int[]{16, 4, 10}, 3);
    }

    drawStringDropShadow(g, "TIME", 33, 0, 7);
    int time = marioEnvironment.getTimeLeft();
//    if (time < 0) time = 0;

    drawStringDropShadow(g, " " + df2.format(time), 33, 1, time < 0 ? 3 : time < 50 ? 1 : time < 100 ? 4 : 7);

    drawProgress(g);

    if (GlobalOptions.areLabels)
    {
        g.drawString("xCam: " + xCam + "yCam: " + yCam, 10, 205);
        g.drawString("x : " + mario.x + "y: " + mario.y, 10, 215);
        g.drawString("xOld : " + mario.xOld + "yOld: " + mario.yOld, 10, 225);
    }
}

private void drawProgress(Graphics g)
{
    String entirePathStr = "......................................>";
    double physLength = (marioEnvironment.getLevelLength()) * 16;
    int progressInChars = (int) (mario.x * (entirePathStr.length() / physLength));
    String progress_str = "";
    for (int i = 0; i < progressInChars - 1; ++i)
        progress_str += ".";
    progress_str += "M";
    try
    {
        drawStringDropShadow(g, entirePathStr.substring(progress_str.length()), progress_str.length(), 28, 0);
    } catch (StringIndexOutOfBoundsException e)
    {
//            System.err.println("warning: progress line inaccuracy");
    }
    drawStringDropShadow(g, progress_str, 0, 28, 2);
    drawStringDropShadow(g, "intermediate reward: " + marioEnvironment.getIntermediateReward(), 0, 27, 2);
}

public static void drawStringDropShadow(Graphics g, String text, int x, int y, int c)
{
    drawString(g, text, x * 8 + 5, y * 8 + 5, 0);
    drawString(g, text, x * 8 + 4, y * 8 + 4, c);
}

public static void drawString(Graphics g, String text, int x, int y, int c)
{
    char[] ch = text.toCharArray();
    for (int i = 0; i < ch.length; i++)
        g.drawImage(Art.font[ch[i] - 32][c], x + i * 8, y, null);
}

//Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//        frame.setLocation((screenSize.length-frame.getWidth())/2, (screenSize.height-frame.getHeight())/2);
private static GraphicsConfiguration graphicsConfiguration;

public void init()
{
    graphicsConfiguration = getGraphicsConfiguration();
//        System.out.println("!!HRUYA: graphicsConfiguration = " + graphicsConfiguration);
    Art.init(graphicsConfiguration);


}

public void postInitGraphics()
{
//        System.out.println("this = " + this);
    this.thisVolatileImage = this.createVolatileImage(GlobalOptions.VISUAL_COMPONENT_WIDTH, GlobalOptions.VISUAL_COMPONENT_HEIGHT);
    this.thisGraphics = getGraphics();
    this.thisVolatileImageGraphics = this.thisVolatileImage.getGraphics();
//        System.out.println("thisGraphics = " + thisGraphics);
//        System.out.println("thisVolatileImageGraphics = " + thisVolatileImageGraphics);
}

public void postInitGraphicsAndLevel()
{
    if (graphicsConfiguration != null)
    {
//            System.out.println("level = " + level);
//            System.out.println("levelScene .level = " + levelScene.level);
//        level = marioEnvironment.getLevel();

        this.mario = marioEnvironment.getMario();
        this.mario.cheatKeys = cheatAgent.getAction();
//            System.out.println("mario = " + mario);
        this.level = marioEnvironment.getLevel();
        layer = new LevelRenderer(level, graphicsConfiguration, this.width, this.height);
        for (int i = 0; i < bgLayer.length; i++)
        {
            int scrollSpeed = 4 >> i;
            int w = ((level.length * 16) - GlobalOptions.VISUAL_COMPONENT_WIDTH) / scrollSpeed + GlobalOptions.VISUAL_COMPONENT_WIDTH;
            int h = ((level.height * 16) - GlobalOptions.VISUAL_COMPONENT_HEIGHT) / scrollSpeed + GlobalOptions.VISUAL_COMPONENT_HEIGHT;
            Level bgLevel = BgLevelGenerator.createLevel(w / 32 + 1, h / 32 + 1, i == 0, marioEnvironment.getLevelType());
            bgLayer[i] = new BgRenderer(bgLevel, graphicsConfiguration, GlobalOptions.VISUAL_COMPONENT_WIDTH, GlobalOptions.VISUAL_COMPONENT_HEIGHT, scrollSpeed);
        }
    } else throw new Error("[Mario AI : ERROR] : Graphics Configuration is null. Graphics initialization failed");
}

public void adjustFPS()
{
    int fps = GlobalOptions.FPS;
    delay = (fps > 0) ? (fps >= GlobalOptions.MaxFPS) ? 0 : (1000 / fps) : 100;
//        System.out.println("Delay: " + delay);
}

// THis method here solely for the displaying information in order to reduce
// amount of info passed between Env and VisComponent

public void setAgent(Agent agent)
{
//        System.out.println("agent = " + agent);
    this.agentNameStr = agent.getName();
    if (agent instanceof KeyAdapter)
    {
        if (prevHumanKeyBoardAgent != null)
            this.removeKeyListener(prevHumanKeyBoardAgent);
        this.prevHumanKeyBoardAgent = (KeyAdapter) agent;
        this.addKeyListener(this.prevHumanKeyBoardAgent);
    }
}

public void setGameViewer(GameViewer gameViewer)
{
    this.gameViewer = gameViewer;
}

public List<String> getTextObservation(boolean showEnemies, boolean showLevelScene, boolean showMerged, int zLevelMapValue, int zLevelEnemiesValue)
{
    return marioEnvironment.getObservationStrings(showEnemies, showLevelScene, showMerged, zLevelMapValue, zLevelEnemiesValue);
}

public void changeScale2x()
{
    marioVisualComponent.setPreferredSize(new Dimension(width, height));
    marioComponentFrame.pack();
    this.thisGraphics = getGraphics();
}


private void renderBlackout(Graphics g, int x, int y, int radius)
{
    if (radius > GlobalOptions.VISUAL_COMPONENT_WIDTH) return;

    int[] xp = new int[20];
    int[] yp = new int[20];
    for (int i = 0; i < 16; i++)
    {
        xp[i] = x + (int) (Math.cos(i * Math.PI / 15) * radius);
        yp[i] = y + (int) (Math.sin(i * Math.PI / 15) * radius);
    }
    xp[16] = GlobalOptions.VISUAL_COMPONENT_WIDTH;
    yp[16] = y;
    xp[17] = GlobalOptions.VISUAL_COMPONENT_WIDTH;
    yp[17] = GlobalOptions.VISUAL_COMPONENT_HEIGHT;
    xp[18] = 0;
    yp[18] = GlobalOptions.VISUAL_COMPONENT_HEIGHT;
    xp[19] = 0;
    yp[19] = y;
    g.fillPolygon(xp, yp, xp.length);

    for (int i = 0; i < 16; i++)
    {
        xp[i] = x - (int) (Math.cos(i * Math.PI / 15) * radius);
        yp[i] = y - (int) (Math.sin(i * Math.PI / 15) * radius);
    }
    xp[16] = GlobalOptions.VISUAL_COMPONENT_WIDTH;
    yp[16] = y;
    xp[17] = GlobalOptions.VISUAL_COMPONENT_WIDTH;
    yp[17] = 0;
    xp[18] = 0;
    yp[18] = 0;
    xp[19] = 0;
    yp[19] = y;

    g.fillPolygon(xp, yp, xp.length);
}


}


