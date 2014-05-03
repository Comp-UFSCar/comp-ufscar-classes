/*
 * Copyright (c) 2009-2010, Sergey Karakovskiy and Julian Togelius
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *  Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *  Neither the name of the Mario AI nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
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

import ch.idsia.agents.learning.SimpleMLPAgent;
import ch.idsia.agents.learning.SmallMLPAgent;
import ch.idsia.benchmark.tasks.LearningTask;

/**
 * Created by IntelliJ IDEA.
 * User: Sergey Karakovskiy, sergey.karakovskiy@gmail.com
 * Date: Jun 9, 2010
 * Time: 7:03:17 PM
 * Package: ch.idsia.agents
 */
public class BasicLearningAgent extends SimpleMLPAgent implements LearningAgent
{
Agent agent;
Agent finalAgent;

public BasicLearningAgent(Agent agent)
{
    this.agent = agent;
}

public BasicLearningAgent()
{
    this.agent = new SmallMLPAgent();
}

public void learn()
{

}

public void giveReward(float r)
{

}

public void newEpisode()
{

}

public Agent getBestAgent()
{
    return null;
}

public void setEvaluationQuota(long num)
{

}

public void setLearningTask(LearningTask task)
{

}

public void init()
{

}
}
