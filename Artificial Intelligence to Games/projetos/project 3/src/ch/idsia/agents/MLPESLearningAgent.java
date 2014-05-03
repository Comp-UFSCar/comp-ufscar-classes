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
import ch.idsia.benchmark.mario.engine.GlobalOptions;
import ch.idsia.benchmark.mario.environments.Environment;
import ch.idsia.benchmark.tasks.LearningTask;
import ch.idsia.evolution.ea.ES;
import ch.idsia.utils.wox.serial.Easy;

import java.text.DecimalFormat;

/**
 * Created by IntelliJ IDEA.
 * User: Sergey Karakovskiy, sergey.karakovskiy@gmail.com
 * Date: 12/12/10
 * Time: 12:24 AM
 * Package: ch.idsia.agents
 */
public class MLPESLearningAgent implements LearningAgent
{
private LearningTask learningTask = null;

private MediumSRNAgent agent;
Agent bestAgent;
private int bestScore = 5;
ES es;
int populationSize = 100;
int generations = 5000;
long evaluationQuota; //common number of trials
long currentEvaluation; // number of exhausted trials
private String name = getClass().getSimpleName();
DecimalFormat df = new DecimalFormat("###.####");

public MLPESLearningAgent()
{
    agent = new MediumSRNAgent();
}

public void learn()
{
    String fileName;

    for (int gen = 0; gen < generations; gen++)
    {
        es.nextGeneration();

        int fitn = (int) es.getBestFitnesses()[0];
        System.out.print("Generation: " + gen + " current best: " + df.format(fitn) + ";  ");

        if (fitn > bestScore /*&& marioStatus == Environment.MARIO_STATUS_WIN*/)
        {
            bestScore = fitn;
            fileName = "evolved-progress-" + name + gen + "-uid-" + GlobalOptions.getTimeStamp() + ".xml";
            final Agent a = (Agent) es.getBests()[0];
            Easy.save(a, fileName);
            learningTask.dumpFitnessEvaluation(bestScore, "fitnessImprovements-" + name + ".txt");

            System.out.println("new best:" + fitn);
            System.out.print("MODE: = " + learningTask.getEnvironment().getEvaluationInfo().marioMode);
            System.out.print("TIME LEFT: " + learningTask.getEnvironment().getEvaluationInfo().timeLeft);
            System.out.println(", STATUS = " + learningTask.getEnvironment().getEvaluationInfo().marioStatus);
            bestAgent = a;
        }
    }
}

public void giveReward(final float reward)
{

}

public void newEpisode()
{

}

public void setLearningTask(final LearningTask learningTask)
{
    this.learningTask = learningTask;
}

public void setEvaluationQuota(final long num)
{
    this.evaluationQuota = num;
}

public Agent getBestAgent()
{
    return bestAgent;
}

public void init()
{
    es = new ES(learningTask, agent, populationSize);
}

public boolean[] getAction()
{
    System.out.println("agent = " + agent);
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
    return name;  //To change body of implemented methods use File | Settings | File Templates.
}

public void setName(final String name)
{
    this.name = name;
}
}
