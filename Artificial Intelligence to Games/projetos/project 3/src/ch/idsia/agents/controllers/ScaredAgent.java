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

package ch.idsia.agents.controllers;

import ch.idsia.agents.Agent;
import ch.idsia.benchmark.mario.engine.sprites.Mario;
//import ch.idsia.benchmark.mario.environments.Environment;

/**
 * Created by IntelliJ IDEA.
 * User: Sergey Karakovskiy, firstname_at_idsia_dot_ch
 * Date: May 9, 2009
 * Time: 9:46:59 AM
 * Package: ch.idsia.controllers.agents
 */
public class ScaredAgent extends BasicMarioAIAgent implements Agent
{
public ScaredAgent()
{
    super("ScaredAgent");
}

int trueJumpCounter = 0;
//    int trueSpeedCounter = 0;

public boolean[] getAction()
{
    if (/*levelScene[11][13] != 0 ||*/ levelScene[this.receptiveFieldWidth / 2][this.receptiveFieldHeight / 2 + 1] != 0 ||
            /* levelScene[12][13] == 0 ||*/ levelScene[this.receptiveFieldWidth / 2 / 2 + 1][this.receptiveFieldHeight / 2 + 1] == 0)
    {
        if (isMarioAbleToJump || (!isMarioOnGround && action[Mario.KEY_JUMP]))
        {
            action[Mario.KEY_JUMP] = true;
        }
        ++trueJumpCounter;
    } else
    {
        action[Mario.KEY_JUMP] = false;
        trueJumpCounter = 0;
    }

    if (trueJumpCounter > 46)
    {
        trueJumpCounter = 0;
        action[Mario.KEY_JUMP] = false;
    }

    return action;  //To change body of implemented methods use File | Settings | File Templates.
}

public void reset()
{
    action[Mario.KEY_RIGHT] = true;
    action[Mario.KEY_SPEED] = false;
}
}
