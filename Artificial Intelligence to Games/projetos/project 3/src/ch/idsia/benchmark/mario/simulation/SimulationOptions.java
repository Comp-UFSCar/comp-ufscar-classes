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

package ch.idsia.benchmark.mario.simulation;

import ch.idsia.agents.Agent;
import ch.idsia.agents.AgentsPool;
import ch.idsia.benchmark.mario.engine.GlobalOptions;
import ch.idsia.utils.ParameterContainer;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Sergey Karakovskiy
 * Date: Apr 12, 2009
 * Time: 9:55:56 PM
 * Package: .Simulation
 */

public class SimulationOptions extends ParameterContainer
{
final Point viewLocation = new Point(42, 42);
protected Agent agent;
//    protected MarioComponent marioComponent = null;

protected SimulationOptions()
{
    super();
}

public void setUpOptions(String[] args)
{
    if (args != null)
        for (int i = 0; i < args.length - 1; i += 2)
            try
            {
                setParameterValue(args[i], args[i + 1]);

                if (args[i].equals("-lf") && args[i + 1].equals("on"))
                {
                    setBlocksCount(false);
                    setCoinsCount(false);
                    setTubesCount(false);
                    setGapsCount(false);
                    setDeadEndsCount(false);
                    setCannonsCount(false);
                    setEnemies("off");
                }
            }
            catch (ArrayIndexOutOfBoundsException e)
            {
                // Basically we can push the red button to explaud the computer, since this case must happen never.
                System.err.println("Error: Wrong number of input parameters");
//                System.err.println("It is a perfect day to kill yourself with the yellow wall");
            }
    GlobalOptions.isVisualization = isVisualization();
    GlobalOptions.FPS = getFPS() /*GlobalOptions.FPS*/;
//    GlobalOptions.isPauseWorld = isPauseWorld();
    GlobalOptions.isPowerRestoration = isPowerRestoration();
//        GlobalOptions.isTimer = isTimer();
}

public boolean isExitProgramWhenFinished()
{
    return b(getParameterValue("-ewf"));
}

public void setExitProgramWhenFinished(boolean exitProgramWhenFinished)
{
    setParameterValue("-ewf", s(exitProgramWhenFinished));
}

public Point getViewLocation()
{
    viewLocation.x = i(getParameterValue("-vlx"));
    viewLocation.y = i(getParameterValue("-vly"));
    return viewLocation;
}

public boolean isViewAlwaysOnTop()
{
    return b(getParameterValue("-vaot"));
}

public void setViewerAlwaysOnTop(boolean vaot)
{
    setParameterValue("-vaot", s(vaot));
}

public void setFPS(int fps)
{
    setParameterValue("-fps", s(fps));
    GlobalOptions.FPS = getFPS();
}

public int getFPS()
{
    return i(getParameterValue("-fps"));
}

public String getAgentFullLoadName()
{
    return getParameterValue("-ag");
}

public String getLevelFileName()
{
    return getParameterValue("-s");
}

// Agent

public Agent getAgent()
{
//        return a(getParameterValue("-ag"));      }
    if (agent == null)
    {
        agent = AgentsPool.loadAgent(getParameterValue("-ag"), isPunj());
//            System.out.println("Info: Agent not specified. Default " + agent.getName() + " has been used instead");
    }
    return agent;
}

public void setAgent(Agent agent)
{
//        setParameterValue("-ag", s(agent));
    this.agent = agent;
}

public void setAgent(String agentWOXorClassName)
{
    this.agent = AgentsPool.loadAgent(agentWOXorClassName, isPunj());
}

// LevelType

public int getLevelType()
{
    return i(getParameterValue("-lt"));
}

public void setLevelType(int levelType)
{
    setParameterValue("-lt", s(levelType));
}

// LevelDifficulty

public int getLevelDifficulty()
{
    return i(getParameterValue("-ld"));
}

public void setLevelDifficulty(int levelDifficulty)
{
    setParameterValue("-ld", s(levelDifficulty));
}

//LevelLength

public int getLevelLength()
{
    return i(getParameterValue("-ll"));
}

public void setLevelLength(int levelLength)
{
    setParameterValue("-ll", s(levelLength));
}

//LevelHeight

public int getLevelHeight()
{
    return i(getParameterValue("-lh"));
}

public void setLevelHeight(int levelHeight)
{
    setParameterValue("-lh", s(levelHeight));
}

//LevelRandSeed

public int getLevelRandSeed() throws NumberFormatException
{
    return i(getParameterValue("-ls"));
}

public void setLevelRandSeed(int levelRandSeed)
{
    setParameterValue("-ls", s(levelRandSeed));
}

//Visualization

public boolean isVisualization()
{
    return b(getParameterValue("-vis"));
}

public void setVisualization(boolean visualization)
{
    setParameterValue("-vis", s(visualization));
}

//isPowerRestoration

public void setFrozenCreatures(boolean frozenCreatures)
{
    setParameterValue("-fc", s(frozenCreatures));
}

public boolean isFrozenCreatures()
{
    return b(getParameterValue("-fc"));
}


public boolean isPowerRestoration()
{
    return b(getParameterValue("-pr"));
}

public void setPowerRestoration(boolean powerRestoration)
{
    setParameterValue("-pr", s(powerRestoration));
}

//MarioMode

public int getMarioMode()
{
    return i(getParameterValue("-mm"));
}

public void setMarioMode(int marioMode)
{ setParameterValue("-mm", s(marioMode)); }

//ZLevelScene

public int getZLevelScene()
{
    return i(getParameterValue("-zs"));
}

public void setZLevelScene(int zLevelMap)
{
    setParameterValue("-zs", s(zLevelMap));
}

//ZLevelEnemies

public int getZLevelEnemies()
{
    return i(getParameterValue("-ze"));
}

public void setZLevelEnemies(int zLevelEnemies)
{
    setParameterValue("-ze", s(zLevelEnemies));
}

// TimeLimit

public int getTimeLimit()
{
    return i(getParameterValue("-tl"));
}

public void setTimeLimit(int timeLimit)
{
    setParameterValue("-tl", s(timeLimit));
}

// Invulnerability

public boolean isMarioInvulnerable()
{
    return b(getParameterValue("-i"));
}

public void setMarioInvulnerable(boolean invulnerable)
{ setParameterValue("-i", s(invulnerable)); }

// Level: dead ends count

public boolean getDeadEndsCount()
{
    return b(getParameterValue("-lde"));
}

public void setDeadEndsCount(boolean var)
{
    setParameterValue("-lde", s(var));
}

// Level: cannons count

public boolean getCannonsCount()
{
    return b(getParameterValue("-lca"));
}

public void setCannonsCount(boolean var)
{
    setParameterValue("-lca", s(var));
}

// Level: HillStraight count

public boolean getHillStraightCount()
{
    return b(getParameterValue("-lhs"));
}

public void setHillStraightCount(boolean var)
{
    setParameterValue("-lhs", s(var));
}

// Level: Tubes count

public boolean getTubesCount()
{
    return b(getParameterValue("-ltb"));
}

public void setTubesCount(boolean var)
{
    setParameterValue("-ltb", s(var));
}

// Level: blocks count

public boolean getBlocksCount()
{
    return b(getParameterValue("-lb"));
}

public void setBlocksCount(boolean var)
{
    setParameterValue("-lb", s(var));
}

// Level: coins count

public boolean getCoinsCount()
{
    return b(getParameterValue("-lco"));
}

public void setCoinsCount(boolean var)
{
    setParameterValue("-lco", s(var));
}

// Level: gaps count

public boolean getGapsCount()
{
    return b(getParameterValue("-lg"));
}

public void setGapsCount(boolean var)
{
    setParameterValue("-lg", s(var));
}

// Level: hidden blocks count

public boolean getHiddenBlocksCount()
{
    return b(getParameterValue("-lhb"));
}

public void setHiddenBlocksCount(boolean var)
{
    setParameterValue("-lhb", s(var));
}

// Level: enemies mask

public String getEnemies()
{
    return getParameterValue("-le");
}

public void setEnemies(String var)
{
    setParameterValue("-le", var);
}

// Level: flat level

public boolean isFlatLevel()
{
    return b(getParameterValue("-lf"));
}

public void setFlatLevel(boolean var)
{
    setParameterValue("-lf", s(var));
}

public boolean isTrace()
{
    String s = getParameterValue("-trace");
    boolean f = false;

    if (!s.equals("off") && !s.equals(""))
        f = true;

    return f;
}

public String getTraceFileName()
{
    String s = getParameterValue("-trace");
    String res = "";

    if (!s.equals("off") && !s.equals(""))
    {
        if (s.equals("on"))
            res = "[MarioAI]-MarioTrace.txt";
        else
            res = s;
    }

    return res;
}

public String getRecordingFileName()
{
    return getParameterValue("-rec");
}

public void setRecordFile(String var)
{
    setParameterValue("-rec", var);
}

public boolean isScale2X()
{
    return b(getParameterValue("-z"));
}

public void setScale2X(boolean z)
{
    setParameterValue("-z", s(z));
}

public void setGreenMushroomMode(int mode)
{
    setParameterValue("-gmm", s(mode));
}

public int getGreenMushroomMode()
{
    return i(getParameterValue("-gmm"));
}

public boolean isLevelLadder()
{
    return b(getParameterValue("-lla"));
}

public void setLevelLadder(boolean ladder)
{
    setParameterValue("-lla", s(ladder));
}

public void setPunj(boolean punj)
{
    setParameterValue("-punj", s(punj));
}

public boolean isPunj()
{
    return b(getParameterValue("-punj"));
}
}
