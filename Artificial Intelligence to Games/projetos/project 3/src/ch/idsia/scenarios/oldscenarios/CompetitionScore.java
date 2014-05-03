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

/**
 * Created by IntelliJ IDEA.
 * User: julian
 * Date: Aug 13, 2009
 * Time: 6:32:50 PM
 */
public class CompetitionScore
{

final static int numberOfTrials = 10;
private static int killsSum = 0;
private static float marioStatusSum = 0;
private static int timeLeftSum = 0;
private static int marioModeSum = 0;


public static void main(String[] args)
{
    Agent controller = AgentsPool.loadAgent(args[0], false);
    final int startingSeed = Integer.parseInt(args[1]);
    score(controller, startingSeed);
    System.exit(0);
}

public static void score(Agent agent, int startingSeed)
{
    killsSum = 0;
    marioStatusSum = 0;
    timeLeftSum = 0;
    marioModeSum = 0;
    TimingAgent controller = new TimingAgent(agent);
//        RegisterableAgent.registerAgent (controller);
    SimulationOptions options = new MarioAIOptions(new String[0]);

//        options.setEvaluationQuota(1);
    options.setVisualization(false);
    options.setFPS(GlobalOptions.MaxFPS);
    System.out.println("Scoring controller " + controller + " with starting seed " + startingSeed);

    double competitionScore = 0;

    competitionScore += testConfig(controller, options, startingSeed, 0, false);
    competitionScore += testConfig(controller, options, startingSeed, 3, false);
    competitionScore += testConfig(controller, options, startingSeed, 5, false);
    competitionScore += testConfig(controller, options, startingSeed, 10, false);
    System.out.println("Competition score: " + competitionScore + "\n\n");
    System.out.println("Number of levels cleared = " + marioStatusSum);
    System.out.println("Additional (tie-breaker) info: ");
    System.out.println("Total time left = " + timeLeftSum);
    System.out.println("Total kills = " + killsSum);
    System.out.println("Mario mode (small, large, fire) sum = " + marioModeSum);
}

public static double testConfig(TimingAgent controller, SimulationOptions options, int seed, int level, boolean paused)
{
    options.setLevelDifficulty(level);

    StatisticalSummary ss = test(controller, options, seed);
    double averageTimeTaken = controller.averageTimeTaken();
    System.out.printf("Difficulty %d score %.4f (avg time %.4f)\n",
            level, ss.mean(), averageTimeTaken);
    if (averageTimeTaken > 40)
    {
        System.out.println("Maximum allowed average time is 40 ms per time step.\n" +
                "Controller disqualified");
        System.exit(0);
    }
    return ss.mean();
}

public static StatisticalSummary test(Agent controller, SimulationOptions options, int seed)
{
    StatisticalSummary ss = new StatisticalSummary();
    int kills = 0;
    int timeLeft = 0;
    int marioMode = 0;
    float marioStatus = 0;
    for (int i = 0; i < numberOfTrials; i++)
    {
        options.setLevelLength(200 + (i * 128) + (seed % (i + 1)));
        options.setLevelType(i % 3);
        options.setLevelRandSeed(seed + i);
        controller.reset();
        options.setAgent(controller);
//            Evaluator evaluator = new Evaluator (options);
//            EvaluationInfo result = evaluator.evaluate().get(0);
//            ss.add (result.computeDistancePassed());
//            kills += result.computeKillsTotal();
//            timeLeft += result.timeLeft;
//            marioMode += result.marioMode;
//            marioStatus += result.marioStatus;
    }
    killsSum += kills;
    marioStatusSum += marioStatus;
    timeLeftSum += timeLeft;
    marioModeSum += marioMode;
    return ss;
}
}
