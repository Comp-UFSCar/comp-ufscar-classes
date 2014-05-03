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
import ch.idsia.agents.controllers.BasicMarioAIAgent;
import ch.idsia.benchmark.mario.environments.Environment;
import ch.idsia.evolution.Evolvable;
import ch.idsia.evolution.MLP;

/**
 * Created by IntelliJ IDEA.
 * User: julian
 * Date: May 13, 2009
 * Time: 11:11:33 AM
 */
public class MediumMLPAgent extends BasicMarioAIAgent implements Agent, Evolvable
{

private static final String name = "MediumMLPAgent";
private MLP mlp;
final int numberOfOutputs = Environment.numberOfKeys;
//    final int numberOfInputs = 53;
final int numberOfInputs = 28;

public MediumMLPAgent()
{
    super(name);
    mlp = new MLP(numberOfInputs, 10, numberOfOutputs);
}

private MediumMLPAgent(MLP mlp)
{
    super(name);
    this.mlp = mlp;
}

public Evolvable getNewInstance()
{
    return new MediumMLPAgent(mlp.getNewInstance());
}

public Evolvable copy()
{
    return new MediumMLPAgent(mlp.copy());
}

public void reset()
{
    mlp.reset();
}

public void mutate()
{
    mlp.mutate();
}

public boolean[] getAction()
{
    byte[][] scene = this.mergedObservation;
//        byte[][] enemies = observation.getEnemiesObservation(/*0*/);
    double[] inputs = new double[numberOfInputs];
    int which = 0;
    for (int i = -2; i < 3; i++)
    {
        for (int j = -2; j < 3; j++)
        {
            inputs[which++] = probe(i, j, scene);
        }
    }
//        for (int i = -2; i < 3; i++) {
//            for (int j = -2; j < 3; j++) {
//                inputs[which++] = probe(i, j, enemies);
//            }
//        }
    inputs[inputs.length - 3] = isMarioOnGround ? 1 : 0;
    inputs[inputs.length - 2] = isMarioAbleToJump ? 1 : 0;
    inputs[inputs.length - 1] = 1;
    double[] outputs = mlp.propagate(inputs);
    boolean[] action = new boolean[numberOfOutputs];
    for (int i = 0; i < action.length; i++)
    {
        action[i] = outputs[i] > 0;
    }
    return action;
}

public String getName()
{
    return name;
}

public void setName(String name)
{
}

private double probe(int x, int y, byte[][] scene)
{
    int realX = x + 11;
    int realY = y + 11;
    return (scene[realX][realY] != 0) ? 1 : 0;
}

}
