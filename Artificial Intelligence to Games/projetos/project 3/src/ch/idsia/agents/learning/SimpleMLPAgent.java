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

package ch.idsia.agents.learning;

import ch.idsia.agents.Agent;
import ch.idsia.benchmark.mario.environments.Environment;
import ch.idsia.evolution.Evolvable;
import ch.idsia.evolution.MLP;

/**
 * Created by IntelliJ IDEA.
 * User: julian
 * Date: Apr 28, 2009
 * Time: 2:09:42 PM
 */
public class SimpleMLPAgent implements Agent, Evolvable
{

private MLP mlp;
private String name = "SimpleMLPAgent";
final int numberOfOutputs = 6;
final int numberOfInputs = 10;
private Environment environment;

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


public SimpleMLPAgent()
{
    mlp = new MLP(numberOfInputs, 10, numberOfOutputs);
}

private SimpleMLPAgent(MLP mlp)
{
    this.mlp = mlp;
}

public Evolvable getNewInstance()
{
    return new SimpleMLPAgent(mlp.getNewInstance());
}

public Evolvable copy()
{
    return new SimpleMLPAgent(mlp.copy());
}

public void integrateObservation(Environment environment)
{
    this.environment = environment;
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
{ mlp.reset(); }

public void setObservationDetails(final int rfWidth, final int rfHeight, final int egoRow, final int egoCol)
{}

public void mutate()
{ mlp.mutate(); }

public boolean[] getAction()
{
//        double[] inputs = new double[]{probe(-1, -1, levelScene), probe(0, -1, levelScene), probe(1, -1, levelScene),
//                              probe(-1, 0, levelScene), probe(0, 0, levelScene), probe(1, 0, levelScene),
//                                probe(-1, 1, levelScene), probe(0, 1, levelScene), probe(1, 1, levelScene),
//                                1};
    double[] inputs = new double[]{probe(-1, -1, mergedObservation), probe(0, -1, mergedObservation), probe(1, -1, mergedObservation),
            probe(-1, 0, mergedObservation), probe(0, 0, mergedObservation), probe(1, 0, mergedObservation),
            probe(-1, 1, mergedObservation), probe(0, 1, mergedObservation), probe(1, 1, mergedObservation),
            1};
    double[] outputs = mlp.propagate(inputs);
    boolean[] action = new boolean[numberOfOutputs];
    for (int i = 0; i < action.length; i++)
        action[i] = outputs[i] > 0;
    return action;
}

public String getName()
{
    return name;
}

public void setName(String name)
{
    this.name = name;
}

private double probe(int x, int y, byte[][] scene)
{
    int realX = x + 11;
    int realY = y + 11;
    return (scene[realX][realY] != 0) ? 1 : 0;
}
}
