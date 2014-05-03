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

package ch.idsia.benchmark.experiments;

import ch.idsia.agents.Agent;
import ch.idsia.benchmark.tasks.Task;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sergey Karakovskiy, sergey at idsia dot ch
 * Date: Feb 23, 2010
 * Time: 10:13:39 PM
 * Package: ch.idsia.maibe.experiments
 */
public class Experiment
{
public Task task;
public Agent agent;
public int stepNumber;
// An experiment matches up a task with an agent and handles their interactions.


public Experiment(Task task, Agent agent)
{
    this.task = task;
    this.agent = agent;
    this.stepNumber = 0;
}

public void doInteractions(int number)
{
    // The default implementation directly maps the methods of the agent and the task.
//        Returns the number of interactions done.
    for (int i = 0; i < number; ++i)
    {
        this.oneInteraction();
    }
}

public double oneInteraction()
{
    ++this.stepNumber;
//        self.agent.integrateObservation(self.task.getObservation())
//        self.task.performAction(self.agent.getAction())
//        reward = self.task.getReward()
//        self.agent.giveReward(reward)
    double reward = 0;
    return reward;
}

public List<List<Double>> doEpisodes(int amount)
{
    for (int i = 0; i < amount; ++i)
    {
        this.oneInteraction();
    }
    return null;
}
}
