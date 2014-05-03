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

import ch.idsia.benchmark.mario.engine.sprites.Mario;
import ch.idsia.benchmark.mario.environments.Environment;

/**
 * Created by IntelliJ IDEA.
 * User: Sergey Karakovskiy, firstname_at_idsia_dot_ch
 * Date: May 12, 2009
 * Time: 7:28:57 PM
 * Package: ch.idsia.controllers.agents
 */
public class SimpleAgent implements Agent
{
protected boolean Action[] = new boolean[Environment.numberOfKeys];
protected String Name = "SimpleAgent";

public void integrateObservation(int[] serializedLevelSceneObservationZ, int[] serializedEnemiesObservationZ, float[] marioFloatPos, float[] enemiesFloatPos, int[] marioState)
{
    //To change body of implemented methods use File | Settings | File Templates.
}

public boolean[] getAction()
{
    return new boolean[0];  //To change body of implemented methods use File | Settings | File Templates.
}

public void integrateObservation(Environment environment)
{
    //To change body of implemented methods use File | Settings | File Templates.
}

public void giveIntermediateReward(float intermediateReward)
{

}

public void reset()
{
    Action = new boolean[Environment.numberOfKeys];
    Action[Mario.KEY_RIGHT] = true;
    Action[Mario.KEY_SPEED] = true;
}

public void setObservationDetails(final int rfWidth, final int rfHeight, final int egoRow, final int egoCol)
{}

public boolean[] getAction(Environment observation)
{
    Action[Mario.KEY_SPEED] = Action[Mario.KEY_JUMP] = observation.isMarioAbleToJump() || !observation.isMarioOnGround();
    return Action;
}

public String getName() { return Name; }

public void setName(String Name) { this.Name = Name; }
}
