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

package ch.idsia.agents;

import ch.idsia.benchmark.mario.environments.Environment;
import ch.idsia.tools.amico.AmiCoJavaPy;

/**
 * Created by IntelliJ IDEA.
 * User: Sergey Karakovskiy, sergey at idsia dot ch
 * Date: Dec 11, 2009
 * Time: 8:29:15 PM
 * Package: ch.idsia.controllers.agents
 */
public class AmiCoAgent implements Agent
{
static AmiCoJavaPy amicoJavaPy = null;
private final String moduleName;
private final String className;
private Environment env;

public AmiCoAgent(String amicoModuleName)
{
    //xxxxx.xxxx.xxx.className.py
    //tmp contains all the string except .py
    String tmp = amicoModuleName.substring(0, amicoModuleName.lastIndexOf(".py"));
    int lastPoint = tmp.lastIndexOf(".");
    //className
    this.className = tmp.substring(lastPoint + 1);
    //xxxxx.xxxx.xxx
    this.moduleName = tmp.substring(0, lastPoint);
    this.reset();
}

@Deprecated
public void integrateObservation(int[] serializedLevelSceneObservationZ, int[] serializedEnemiesObservationZ, float[] marioFloatPos, float[] enemiesFloatPos, int[] marioState)
{
    amicoJavaPy.integrateObservation(serializedLevelSceneObservationZ, serializedEnemiesObservationZ, marioFloatPos, enemiesFloatPos, marioState);
}

public boolean[] getAction()
{
    int ZLevelScene = 1;
    int ZLevelEnemies = 0;
    // Default hardcoded values for ZLevels used by now
    // Will use extra values from int[] action in future to tailor the representation of levels
    byte[][] levelScene = env.getLevelSceneObservationZ(ZLevelScene);
    byte[][] enemies = env.getEnemiesObservationZ(ZLevelEnemies);
    int rows = env.getReceptiveFieldHeight();
    int cols = env.getReceptiveFieldWidth();
    int[] squashedLevelScene = new int[rows * cols];
    int[] squashedEnemies = new int[enemies.length * enemies[0].length];

    // serialization into arrays of primitive types to speed up the data transfer.
    for (int i = 0; i < squashedLevelScene.length; ++i)
    {
        squashedLevelScene[i] = levelScene[i / cols][i % rows];
        squashedEnemies[i] = enemies[i / cols][i % rows];
    }
    float[] marioPos = env.getMarioFloatPos();
    float[] enemiesPos = env.getEnemiesFloatPos();
    int[] marioState = env.getMarioState();

    amicoJavaPy.integrateObservation(squashedLevelScene, squashedEnemies, marioPos, enemiesPos, marioState);

    int[] action = amicoJavaPy.getAction();

    boolean[] ret = new boolean[action.length];
    for (int i = 0; i < action.length; ++i)
        ret[i] = (action[i] != 0);
    return ret;
}

public void integrateObservation(Environment environment)
{
    this.env = environment;
}

public void giveIntermediateReward(float intermediateReward)
{
    amicoJavaPy.giveIntermediateReward(intermediateReward);
}

public void reset()
{
    if (amicoJavaPy == null)
    {
        System.out.println("Java: Initialize AmiCo");
        amicoJavaPy = new AmiCoJavaPy(moduleName, className);
        if (amicoJavaPy != null)
            System.out.println("Java: Initialize AmiCo");
        else
            throw new Error("AmiCoJavaPy not initialized");
    } else
    {
        amicoJavaPy.reset();
//            System.out.println("Java: AmiCo is already initialized");
    }
}

public void setObservationDetails(final int rfWidth, final int rfHeight, final int egoRow, final int egoCol)
{
    amicoJavaPy.setObservationDetails(rfWidth, rfHeight, egoRow, egoCol);
}

public String getName()
{
    return amicoJavaPy.getName();
}

public void setName(String name)
{
    throw new Error("AmiCo agent name cannot be changed");
}

protected void finalize() throws Throwable
{
    try
    {
        if (amicoJavaPy != null)
            amicoJavaPy.finalizePythonEnvironment();
    }
    finally
    {
        super.finalize();
    }
}
}
