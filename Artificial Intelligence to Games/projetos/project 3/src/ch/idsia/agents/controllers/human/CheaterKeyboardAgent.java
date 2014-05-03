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

package ch.idsia.agents.controllers.human;

import ch.idsia.agents.Agent;
import ch.idsia.benchmark.mario.engine.GlobalOptions;
import ch.idsia.benchmark.mario.environments.Environment;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Created by IntelliJ IDEA.
 * User: Sergey Karakovskiy
 * Date: Apr 8, 2009
 * Time: 3:36:16 AM
 * Package: ch.idsia.controllers.agents.controllers;
 */
public class CheaterKeyboardAgent extends KeyAdapter implements Agent
{
public static final int CHEAT_KEY_PAUSE = 6;
public static final int CHEAT_KEY_DUMP_CURRENT_WORLD = 7;
public static final int CHEAT_KEY_LIFE_UP = 8;
public static final int CHEAT_KEY_WIN = 9;
public static final int CHEAT_KEY_OBSERVE_LEVEL = 10;

private boolean Action[] = null;

private String Name = "Instance of CheaterKeyboardAgent";
private Integer prevFPS = 24;
//public static boolean isObserveLevel = false;

public CheaterKeyboardAgent()
{
    reset();
}

public void integrateObservation(int[] serializedLevelSceneObservationZ, int[] serializedEnemiesObservationZ, float[] marioFloatPos, float[] enemiesFloatPos, int[] marioState)
{
    //To change body of implemented methods use File | Settings | File Templates.
}

public boolean[] getAction()
{
    return Action;
}

public void integrateObservation(Environment environment) { }

public void giveIntermediateReward(float intermediateReward)
{}

public void reset()
{
    // move your coffee away from the keyboard..
    Action = new boolean[16];
}

public void setObservationDetails(final int rfWidth, final int rfHeight, final int egoRow, final int egoCol)
{}

public String getName() { return Name; }

public void setName(String name) { Name = name; }


public void keyPressed(KeyEvent e)
{
    toggleKey(e.getKeyCode(), true);
}

public void keyReleased(KeyEvent e)
{
    toggleKey(e.getKeyCode(), false);
}

private void toggleKey(int keyCode, boolean isPressed)
{
    switch (keyCode)
    {
        //Cheats;
        case KeyEvent.VK_D:
            if (isPressed)
                GlobalOptions.gameViewerTick();
            break;
        case KeyEvent.VK_V:
            if (isPressed)
                GlobalOptions.isVisualization = !GlobalOptions.isVisualization;
            break;
        case KeyEvent.VK_U:
            Action[CHEAT_KEY_LIFE_UP] = isPressed;
            break;
        case KeyEvent.VK_W:
            Action[CHEAT_KEY_WIN] = isPressed;
            break;
        case KeyEvent.VK_O:
            if (isPressed)
            {
                GlobalOptions.areFrozenCreatures = !GlobalOptions.areFrozenCreatures;
            }
            break;
        case KeyEvent.VK_L:
            if (isPressed)
                GlobalOptions.areLabels = !GlobalOptions.areLabels;
            break;
        case KeyEvent.VK_C:
            if (isPressed)
                GlobalOptions.isCameraCenteredOnMario = !GlobalOptions.isCameraCenteredOnMario;
            break;
        case 61:
            if (isPressed)
            {
                ++GlobalOptions.FPS;
                GlobalOptions.AdjustMarioVisualComponentFPS();
            }
            break;
        case 45:
            if (isPressed)
            {
                --GlobalOptions.FPS;
                GlobalOptions.AdjustMarioVisualComponentFPS();
            }
            break;
        case 56:  // chr(56) = 8
            if (isPressed)
            {
                int temp = prevFPS;
                prevFPS = GlobalOptions.FPS;
                GlobalOptions.FPS = (GlobalOptions.FPS == GlobalOptions.MaxFPS) ? temp : GlobalOptions.MaxFPS;
//                    LOGGER.println("FPS has been changed. Current FPS is " +
//                            ((GlobalOptions.FPS == GlobalOptions.MaxFPS) ? "\\infty" : GlobalOptions.FPS), LOGGER.VERBOSE_MODE.INFO);
                GlobalOptions.AdjustMarioVisualComponentFPS();
            }
            break;
        case KeyEvent.VK_G:
            if (isPressed)
            {
                GlobalOptions.isShowReceptiveField = !(GlobalOptions.isShowReceptiveField);
//                    System.out.println("GlobalOptions.receptiveFieldWidth = " + GlobalOptions.receptiveFieldWidth);
            }
            break;
        case KeyEvent.VK_SPACE:
            if (isPressed)
            {
//                    System.out.println("Space bar pressed");
                GlobalOptions.isGameplayStopped = !GlobalOptions.isGameplayStopped;
            }
            break;
        case KeyEvent.VK_F:
            if (isPressed)
            {
                GlobalOptions.isFly = !GlobalOptions.isFly;
            }
            break;
        case KeyEvent.VK_Z:
            if (isPressed)
            {
                GlobalOptions.changeScale2x();
            }
            break;
        case KeyEvent.VK_R:
            if (isPressed)
            {
                GlobalOptions.isRecording = !GlobalOptions.isRecording;
            }
    }
}
}
