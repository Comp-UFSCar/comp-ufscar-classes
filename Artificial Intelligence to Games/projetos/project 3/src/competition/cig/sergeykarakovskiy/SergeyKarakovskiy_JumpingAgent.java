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

package competition.cig.sergeykarakovskiy;

import ch.idsia.agents.Agent;
import ch.idsia.agents.controllers.BasicMarioAIAgent;
import ch.idsia.benchmark.mario.engine.sprites.Mario;

/**
 * Created by IntelliJ IDEA.
 * User: Sergey Karakovskiy, firstname_at_idsia_dot_ch
 * Date: Sep 1, 2009
 * Time: 3:12:07 PM
 * Package: competition.cig.sergeykarakovskiy
 */
public class SergeyKarakovskiy_JumpingAgent extends BasicMarioAIAgent implements Agent
{
public SergeyKarakovskiy_JumpingAgent()
{
    super("SergeyKarakovskiy_JumpingAgent");
    reset();
}

public boolean[] getAction()
{
    action[Mario.KEY_SPEED] = action[Mario.KEY_JUMP] = isMarioAbleToJump || !isMarioOnGround;
    return action;
}

@Override
public void reset()
{
    for (int i = 0; i < action.length; ++i)
        action[i] = false;
    action[Mario.KEY_RIGHT] = true;
    action[Mario.KEY_SPEED] = true;
}

public String getName() { return name; }

public void setName(String Name) { this.name = Name; }
}
