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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sergey Karakovskiy, sergey at idsia dot ch
 * Date: Feb 24, 2010
 * Time: 1:46:36 PM
 * Package: ch.idsia.maibe.experiments
 */
public class EpisodicExperiment extends Experiment
{
public EpisodicExperiment(Task task, Agent agent)
{
    super(task, agent);
}

// returns the rewards of each step as an array of doubles

public List<List<Double>> doEpisodes(int amount)
{
    List<List<Double>> allRewards = new ArrayList<List<Double>>();
    for (int i = 0; i < amount; ++i)
    {
        List<Double> rewards = new ArrayList<Double>();
        this.stepNumber = 0;
        // the agent is informed of the start of the episode
//            this.agent.newEpisode();
        this.agent.reset();
        this.task.reset();
        while (!this.task.isFinished())
        {
            Double r = this.oneInteraction();
            rewards.add(r);
        }
        allRewards.add(rewards);
    }
    return allRewards;
}

}
