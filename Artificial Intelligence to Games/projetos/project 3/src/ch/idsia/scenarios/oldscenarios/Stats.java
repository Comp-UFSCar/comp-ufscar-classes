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
import ch.idsia.agents.controllers.TimingAgent;
import ch.idsia.benchmark.mario.engine.GlobalOptions;
import ch.idsia.benchmark.mario.simulation.SimulationOptions;
import ch.idsia.tools.MarioAIOptions;
import ch.idsia.utils.statistics.StatisticalSummary;

public class Stats
{
final static int numberOfTrials = 100;

public static void main(String[] args)
{
    Agent controller = AgentsPool.loadAgent(args[0], false);
    final int startingSeed = Integer.parseInt(args[1]);
    doStats(controller, startingSeed);
    //System.exit(0);
}

public static void doStats(Agent agent, int startingSeed)
{
    TimingAgent controller = new TimingAgent(agent);
//        RegisterableAgent.registerAgent (controller);
    SimulationOptions options = new MarioAIOptions(new String[0]);

//        options.setEvaluationQuota(1);
    options.setVisualization(false);
    options.setFPS(GlobalOptions.MaxFPS);
    System.out.println("Testing controller " + controller + " with starting seed " + startingSeed);

    double competitionScore = 0;

    competitionScore += testConfig(controller, options, startingSeed, 0, true);
    competitionScore += testConfig(controller, options, startingSeed, 0, false);
    competitionScore += testConfig(controller, options, startingSeed, 3, true);
    competitionScore += testConfig(controller, options, startingSeed, 3, false);
    competitionScore += testConfig(controller, options, startingSeed, 5, true);
    competitionScore += testConfig(controller, options, startingSeed, 5, false);
    //testConfig (controller, options, startingSeed, 8, true);
    //testConfig (controller, options, startingSeed, 8, false);
    competitionScore += testConfig(controller, options, startingSeed, 10, true);
    competitionScore += testConfig(controller, options, startingSeed, 10, false);
    //testConfig (controller, options, startingSeed, 15, true);
    //testConfig (controller, options, startingSeed, 15, false);
    //testConfig (controller, options, startingSeed, 20, true);
    //testConfig (controller, options, startingSeed, 20, false);
    System.out.println("Stats sum: " + competitionScore);
}

public static double testConfig(TimingAgent controller, SimulationOptions options, int seed, int level, boolean paused)
{
    options.setLevelDifficulty(level);
    StatisticalSummary ss = test(controller, options, seed);
    System.out.printf("Level %d %s %.4f (%.4f) (min %.4f max %.4f) (avg time %.4f)\n",
            level, paused ? "paused" : "unpaused",
            ss.mean(), ss.sd(), ss.min(), ss.max(), controller.averageTimeTaken());
    return ss.mean();
}


public static StatisticalSummary test(Agent controller, SimulationOptions options, int seed)
{
    StatisticalSummary ss = new StatisticalSummary();
    for (int i = 0; i < numberOfTrials; i++)
    {
        options.setLevelRandSeed(seed + i);
        controller.reset();
        options.setAgent(controller);
//            Evaluator evaluator = new Evaluator (options);
//            EvaluationInfo result = evaluator.evaluate().get(0);
//            ss.add (result.computeDistancePassed());
    }
    return ss;
}


}
