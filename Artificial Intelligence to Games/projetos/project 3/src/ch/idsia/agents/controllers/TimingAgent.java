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

/**
 * Created by IntelliJ IDEA.
 * User: julian
 * Date: Aug 10, 2009
 * Time: 6:41:42 PM
 */

public class TimingAgent extends BasicMarioAIAgent implements Agent
{
private Agent agent;
private long timeTaken = 0;
private int actionsPerformed = 0;

public TimingAgent(Agent agent)
{
    super("TimingAgent");
    this.agent = agent;
}

public boolean[] getAction()
{
    long start = System.currentTimeMillis();
    boolean[] action = agent.getAction();
    timeTaken += (System.currentTimeMillis() - start);
    actionsPerformed++;
    return action;
}

public void reset()
{
    agent.reset();
}

public String getName()
{
    return agent.getName();
}

public void setName(String name)
{
    agent.setName(name);
}

public double averageTimeTaken()
{
    double average = ((double) timeTaken) / actionsPerformed;
    timeTaken = 0;
    actionsPerformed = 0;
    return average;
}
}
