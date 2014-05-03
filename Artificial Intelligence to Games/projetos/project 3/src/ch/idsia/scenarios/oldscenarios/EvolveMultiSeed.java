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

package ch.idsia.scenarios.oldscenarios;

import ch.idsia.agents.Agent;
import ch.idsia.agents.AgentsPool;
import ch.idsia.agents.learning.SimpleMLPAgent;
import ch.idsia.benchmark.mario.engine.GlobalOptions;
import ch.idsia.benchmark.tasks.MultiSeedProgressTask;
import ch.idsia.evolution.Evolvable;
import ch.idsia.evolution.ea.ES;
import ch.idsia.tools.MarioAIOptions;
import ch.idsia.utils.wox.serial.Easy;

/**
 * Created by IntelliJ IDEA.
 * User: julian
 * Date: May 24, 2009
 * Time: 1:18:44 AM
 */

public class EvolveMultiSeed
{

final static int generations = 100;
final static int populationSize = 100;

public static void main(String[] args)
{
    MarioAIOptions options = new MarioAIOptions(new String[0]);
//        options.setEvaluationQuota(1);
    Evolvable initial = new SimpleMLPAgent();

    if (args.length > 0)
    {
        initial = (Evolvable) AgentsPool.loadAgent(args[0], options.isPunj());
    }
    options.setFPS(GlobalOptions.MaxFPS);
    options.setVisualization(false);
    MultiSeedProgressTask task = new MultiSeedProgressTask(options);
    task.setNumberOfSeeds(3);
    task.setStartingSeed(0);
    ES es = new ES(task, initial, populationSize);
    System.out.println("Evolving " + initial + " with task " + task);
    for (int gen = 0; gen < generations; gen++)
    {
        //task.setStartingSeed((int)(Math.random () * Integer.MAX_VALUE));
        es.nextGeneration();
        double bestResult = es.getBestFitnesses()[0];
        System.out.println("Generation " + gen + " best " + bestResult);
        options.setVisualization(gen % 5 == 0 || bestResult > 4000);
        Agent a = (Agent) es.getBests()[0];
        a.setName(((Agent) initial).getName() + gen);
//                RegisterableAgent.registerAgent(a);
//                AgentsPool.setCurrentAgent(a);
        int result = task.evaluate(a);
        options.setVisualization(false);
        Easy.save(es.getBests()[0], "evolved-" + gen + ".xml");
        if (result > 4000)
        {
            break; //finished
        }
    }
}
}
