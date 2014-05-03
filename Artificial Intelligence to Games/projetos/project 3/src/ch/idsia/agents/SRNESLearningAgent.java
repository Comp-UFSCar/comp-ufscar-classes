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

import ch.idsia.agents.learning.MediumSRNAgent;
import ch.idsia.benchmark.mario.environments.Environment;
import ch.idsia.benchmark.tasks.LearningTask;
import ch.idsia.benchmark.tasks.Task;
import ch.idsia.evolution.ea.ES;

/**
 * Created by IntelliJ IDEA.
 * User: odin
 * Date: Jul 27, 2010
 * Time: 9:40:09 PM
 */


public class SRNESLearningAgent implements LearningAgent
{
private MediumSRNAgent agent;
Agent bestAgent;
private static float bestScore = 0;
private Task task;
ES es;
int populationSize = 100;
int generations = 5000;
long evaluationQuota; //common number of trials
long currentEvaluation; // number of exhausted trials
private String name = getClass().getSimpleName();

public void init()
{
    es = new ES(task, agent, populationSize);
}

public SRNESLearningAgent()
{
    this.agent = new MediumSRNAgent();
}

public void learn()
{
    this.currentEvaluation++;

    int localBestScore = 0;

    for (int gen = 0; gen < generations; gen++)
    {
        System.out.println(gen + " generation");
        es.nextGeneration();
        float fitn = es.getBestFitnesses()[0];

        if (fitn > bestScore)
        {
            bestScore = fitn;
            bestAgent = (Agent) es.getBests()[0];
        }
    }
}

public void giveReward(float r)
{
}

public void newEpisode()
{
    task = null;
    agent.reset();
}


public void setLearningTask(LearningTask learningTask)
{
    this.task = learningTask;
}

public void setEvaluationQuota(long num)
{
    this.evaluationQuota = num;
}

public Agent getBestAgent()
{
    return bestAgent;
}

public boolean[] getAction()
{
    return agent.getAction();
}

public void integrateObservation(final Environment environment)
{
    agent.integrateObservation(environment);
}

public void giveIntermediateReward(final float intermediateReward)
{
    agent.giveIntermediateReward(intermediateReward);
}

/**
 * clears all dynamic data, such as hidden layers in recurrent networks
 * just implement an empty method for a reactive controller
 */
public void reset()
{
    agent.reset();
}

public void setObservationDetails(final int rfWidth, final int rfHeight, final int egoRow, final int egoCol)
{
    agent.setObservationDetails(rfWidth, rfHeight, egoRow, egoCol);
}

public String getName()
{
    return this.name.equals("") ? getClass().getSimpleName() : this.name;
}

public void setName(final String name)
{
    this.name = name;
}
}
