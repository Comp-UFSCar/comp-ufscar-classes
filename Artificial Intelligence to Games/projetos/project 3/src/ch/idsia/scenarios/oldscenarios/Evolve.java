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
import ch.idsia.agents.learning.SimpleMLPAgent;
import ch.idsia.benchmark.mario.engine.GlobalOptions;
import ch.idsia.benchmark.tasks.ProgressTask;
import ch.idsia.benchmark.tasks.Task;
import ch.idsia.evolution.Evolvable;
import ch.idsia.evolution.ea.ES;
import ch.idsia.tools.MarioAIOptions;
import ch.idsia.utils.wox.serial.Easy;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: julian
 * Date: May 4, 2009
 * Time: 4:33:25 PM
 */

public class Evolve
{
final static int generations = 100;
final static int populationSize = 100;

public static void main(String[] args)
{
    MarioAIOptions options = new MarioAIOptions(args);
    List<Agent> bestAgents = new ArrayList<Agent>();
    DecimalFormat df = new DecimalFormat("0000");
    for (int difficulty = 0; difficulty < 11; difficulty++)
    {
        System.out.println("New Evolve phase with difficulty = " + difficulty + " started.");
        Evolvable initial = new SimpleMLPAgent();

        options.setLevelDifficulty(difficulty);
        options.setAgent((Agent) initial);

        options.setFPS(GlobalOptions.MaxFPS);
        options.setVisualization(false);

        Task task = new ProgressTask(options);
        ES es = new ES(task, initial, populationSize);

        for (int gen = 0; gen < generations; gen++)
        {
            es.nextGeneration();
            double bestResult = es.getBestFitnesses()[0];
//                LOGGER.println("Generation " + gen + " best " + bestResult, LOGGER.VERBOSE_MODE.INFO);
            System.out.println("Generation " + gen + " best " + bestResult);
            options.setVisualization(gen % 5 == 0 || bestResult > 4000);
//                options.setFPS(true);
            Agent a = (Agent) es.getBests()[0];
            a.setName(((Agent) initial).getName() + df.format(gen));
//                AgentsPool.setCurrentAgent(a);
            bestAgents.add(a);
            double result = task.evaluate(a);
//                LOGGER.println("trying: " + result, LOGGER.VERBOSE_MODE.INFO);
            options.setVisualization(false);
//                options.setFPS(true);
            Easy.save(es.getBests()[0], "evolved.xml");
            if (result > 4000)
                break; // Go to next difficulty.
        }
    }
    /*//
    //
    //
    //LOGGER.println("Saving bests... ", LOGGER.VERBOSE_MODE.INFO);

    options.setVisualization(true);
    int i = 0;
    for (Agent bestAgent : bestAgents) {
        Easy.save(bestAgent, "bestAgent" +  df.format(i++) + ".xml");
    }

    LOGGER.println("Saved! Press return key to continue...", LOGGER.VERBOSE_MODE.INFO);
    try {System.in.read();        } catch (IOException e) {            e.printStackTrace();        }

//        for (Agent bestAgent : bestAgents) {
//            task.evaluate(bestAgent);
//        }


    LOGGER.save("log.txt");*/
    System.exit(0);
}
}
