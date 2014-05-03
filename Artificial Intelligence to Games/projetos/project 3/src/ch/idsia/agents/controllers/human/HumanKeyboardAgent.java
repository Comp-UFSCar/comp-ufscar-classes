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
import ch.idsia.benchmark.mario.engine.sprites.Mario;
import ch.idsia.benchmark.mario.environments.Environment;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sergey Karakovskiy
 * Date: Mar 29, 2009
 * Time: 12:19:49 AM
 * Package: ch.idsia.controllers.agents.controllers;
 */
public class HumanKeyboardAgent extends KeyAdapter implements Agent
{
List<boolean[]> history = new ArrayList<boolean[]>();
private boolean[] Action = null;
private String Name = "HumanKeyboardAgent";

/*final*/
protected byte[][] levelScene;
/*final */
protected byte[][] enemies;
protected byte[][] mergedObservation;

protected float[] marioFloatPos = null;
protected float[] enemiesFloatPos = null;

protected int[] marioState = null;

protected int marioStatus;
protected int marioMode;
protected boolean isMarioOnGround;
protected boolean isMarioAbleToJump;
protected boolean isMarioAbleToShoot;
protected boolean isMarioCarrying;
protected int getKillsTotal;
protected int getKillsByFire;
protected int getKillsByStomp;
protected int getKillsByShell;
// values of these variables could be changed during the Agent-Environment interaction.
// Use them to get more detailed or less detailed description of the level.
// for information see documentation for the benchmark <link: marioai.org/marioaibenchmark/zLevels
int zLevelScene = 1;
int zLevelEnemies = 0;


public HumanKeyboardAgent()
{
    this.reset();
//        RegisterableAgent.registerAgent(this);
}

public boolean[] getAction()
{
    return Action;
}

public void integrateObservation(Environment environment)
{
    levelScene = environment.getLevelSceneObservationZ(zLevelScene);
    enemies = environment.getEnemiesObservationZ(zLevelEnemies);
    mergedObservation = environment.getMergedObservationZZ(1, 0);

    this.marioFloatPos = environment.getMarioFloatPos();
    this.enemiesFloatPos = environment.getEnemiesFloatPos();
    this.marioState = environment.getMarioState();

    // It also possible to use direct methods from Environment interface.
    //
    marioStatus = marioState[0];
    marioMode = marioState[1];
    isMarioOnGround = marioState[2] == 1;
    isMarioAbleToJump = marioState[3] == 1;
    isMarioAbleToShoot = marioState[4] == 1;
    isMarioCarrying = marioState[5] == 1;
    getKillsTotal = marioState[6];
    getKillsByFire = marioState[7];
    getKillsByStomp = marioState[8];
    getKillsByShell = marioState[9];
}

public void giveIntermediateReward(float intermediateReward)
{

}

public void reset()
{
    // Just check you keyboard. Especially arrow buttons and 'A' and 'S'!
    Action = new boolean[Environment.numberOfKeys];
}

public void setObservationDetails(final int rfWidth, final int rfHeight, final int egoRow, final int egoCol)
{}

public boolean[] getAction(Environment observation)
{
    float[] enemiesPos = observation.getEnemiesFloatPos();
    return Action;
}

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
        case KeyEvent.VK_LEFT:
            Action[Mario.KEY_LEFT] = isPressed;
            break;
        case KeyEvent.VK_RIGHT:
            Action[Mario.KEY_RIGHT] = isPressed;
            break;
        case KeyEvent.VK_DOWN:
            Action[Mario.KEY_DOWN] = isPressed;
            break;
        case KeyEvent.VK_UP:
            Action[Mario.KEY_UP] = isPressed;
            break;

        case KeyEvent.VK_S:
            Action[Mario.KEY_JUMP] = isPressed;
            break;
        case KeyEvent.VK_A:
            Action[Mario.KEY_SPEED] = isPressed;
            break;
    }
}

public List<boolean[]> getHistory()
{
    return history;
}
}
