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

package ch.idsia.tools;

import ch.idsia.agents.Agent;
import ch.idsia.agents.AgentsPool;
import ch.idsia.agents.controllers.TimingAgent;
import ch.idsia.benchmark.mario.simulation.SimulationOptions;
import ch.idsia.benchmark.tasks.GamePlayTask;
import ch.idsia.benchmark.tasks.Task;
import ch.idsia.utils.statistics.StatisticalSummary;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sergey Karakovskiy, sergey.karakovskiy@gmail.com
 * Date: Nov 30, 2010
 * Time: 6:56:38 AM
 * Package: ch.idsia.tools
 */
public class EvaluationInfoStatisticalSummary
{

private List<EvaluationInfo> evaluationSummary = new ArrayList<EvaluationInfo>();

public static StatisticalSummary test(Agent controller, MarioAIOptions marioAIOptions, int seed)
{
    StatisticalSummary ss = new StatisticalSummary();
    int kills = 0;
    int timeLeft = 0;
    int marioMode = 0;
    float marioStatus = 0;
    final Task task = new GamePlayTask(marioAIOptions);
    float fitness = 0;
    boolean verbose = false;
    int trials = 0;
    int disqualifications = 0;

//    for (int i = 0; i < numberOfLevels; i++)
//    {
//        marioAIOptions.setLevelRandSeed(seed + i);
//        marioAIOptions.setLevelLength(200 + (i * 128) + (seed % (i + 1)));
//        marioAIOptions.setLevelType(i % 3);
//        marioAIOptions.setLevelDifficulty(numberOfLevels / 20);
//        marioAIOptions.setAgent(controller);
//        task.setOptionsAndReset(marioAIOptions);
//        if (!task.runOneEpisode())
//        {
//            System.out.println("[MarioAI Evaluation] : out of computational time per action!");
//            disqualifications++;
//            continue;
//        }
//        EvaluationInfo evaluationInfo = basicTask.getEnvironment().getEvaluationInfo();
//        float f = evaluationInfo.computeWeightedFitness();
//        if (verbose)
//        {
////            System.out.println("LEVEL OPTIONS: -ld " + levelDifficulty + " -lt " + levelType + " -pw " + !creaturesEnable +
////                    " -tl " + timeLimit);
////            System.out.println("Intermediate SCORE = " + f + "; Details: " + evaluationInfo.toStringSingleLine());
//        }
//        fitness += f;
////        kills += result.computeKillsTotal();
////        timeLeft += result.timeLeft;
////        marioMode += result.marioMode;
////        marioStatus += result.marioStatus;
//        System.out.println("\ntrial # " + i);
////        System.out.println("result.timeLeft = " + result.timeLeft);
////        System.out.println("result.marioMode = " + result.marioMode);
////        System.out.println("result.marioStatus = " + result.marioStatus);
////        System.out.println("result.computeKillsTotal() = " + result.computeKillsTotal());
//        ss.add(f);
//
//
//        System.out.println("trials = " + trials);
//        System.out.println("disqualifications = " + disqualifications);
//        System.out.println("GamePlayTrack final score = " + fitness);
////            Evaluator evaluator = new Evaluator (options);
////            EvaluationInfo result = evaluator.evaluate().get(0);
//    }
//
//    if (detailedStats)
//    {
//        System.out.println("\n===================\nStatistics over " + numberOfLevels + " trials for " + controller.getName());
//        System.out.println("Total kills = " + kills);
//        System.out.println("marioStatus = " + marioStatus);
//        System.out.println("timeLeft = " + timeLeft);
//        System.out.println("marioMode = " + marioMode);
//        System.out.println("===================\n");
//    }
//
//    killsSum += kills;
//    marioStatusSum += marioStatus;
//    timeLeftSum += timeLeft;
//    marioModeSum += marioMode;

    return ss;
}

public static double testConfig(TimingAgent controller, SimulationOptions options, int seed, int levelDifficulty, boolean paused)
{
    options.setLevelDifficulty(levelDifficulty);
//    StatisticalSummary ss = test(controller, options, seed);
    double averageTimeTaken = controller.averageTimeTaken();
//    System.out.printf("Difficulty %d score %.4f (avg time %.4f)\n",
//            levelDifficulty, ss.mean(), averageTimeTaken);
//    return ss.mean();
    return 0;
}


public static void score(Agent agent, int startingSeed, MarioAIOptions marioAIOptions)
{
    TimingAgent controller = new TimingAgent(agent);
    //        options.setEvaluationQuota(1);
//        options.setVisualization(false);
//        options.setMaxFPS(true);
    System.out.println("\nScoring controller " + agent.getName() + " with starting seed " + startingSeed);

    double competitionScore = 0;
//    killsSum = 0;
//    marioStatusSum = 0;
//    timeLeftSum = 0;
//    marioModeSum = 0;

    competitionScore += testConfig(controller, marioAIOptions, startingSeed, 0, false);
    competitionScore += testConfig(controller, marioAIOptions, startingSeed, 3, false);
    competitionScore += testConfig(controller, marioAIOptions, startingSeed, 5, false);
    competitionScore += testConfig(controller, marioAIOptions, startingSeed, 10, false);

    System.out.println("\nCompetition score: " + competitionScore + "\n");
//    System.out.println("Number of levels cleared = " + marioStatusSum);
//    System.out.println("Additional (tie-breaker) info: ");
//    System.out.println("Total time left = " + timeLeftSum);
//    System.out.println("Total kills = " + killsSum);
//    System.out.println("Mario mode (small, large, fire) sum = " + marioModeSum);
//    System.out.println("TOTAL SUM for " + agent.getName() + " = " + (competitionScore + killsSum + marioStatusSum + marioModeSum + timeLeftSum));
}


public static void scoreAllAgents(MarioAIOptions marioAIOptions)
{
    int startingSeed = marioAIOptions.getLevelRandSeed();
    for (Agent agent : AgentsPool.getAgentsCollection())
        score(agent, startingSeed, marioAIOptions);
}

//    final MarioAIOptions marioAIOptions = new MarioAIOptions(args);
//
//
//    int levelLength = marioAIOptions.getLevelLength();
//
//    final int[] timeLimits = new int[]{levelLength / 10,
//            levelLength * 2 / 10,
//            levelLength * 4 / 10};
//
//    final int[] levelDifficulties = new int[]{0, 1, 2, 3, 4, 5, 6, 12, 16, 20};
//    final int[] levelTypes = new int[]{0, 1, 2};
//    final int[] levelLengths = new int[]{320, 320, 320, 320, 320, 320};
//    final boolean[] creaturesEnables = new boolean[]{false, true};
//    int levelSeed = marioAIOptions.getLevelRandSeed();
////        marioAIOptions.setVisualization(false);
////        marioAIOptions.setFPS(100);
//    marioAIOptions.setLevelRandSeed(6189642);
//
////        final Environment environment = new MarioEnvironment();
//    final Agent agent = new ForwardJumpingAgent();
////        final Agent agent = marioAIOptions.getAgent();
////        final Agent agent = (SimpleCNAgent) Easy.loadAgent("sergeypolikarpov.xml");
////        System.out.println("agent = " + agent);
//    marioAIOptions.setAgent(agent);
//    final BasicTask basicTask = new BasicTask(marioAIOptions);
//    float fitness = 0;
//    boolean verbose = false;
//    int trials = 0;
//    int disqualifications = 0;
//
//    for (int ll : levelLengths)
//
//        for (int levelDifficulty : levelDifficulties)
//        {
//            for (int levelType : levelTypes)
//            {
//                for (boolean creaturesEnable : creaturesEnables)
//                {
//                    for (int timeLimit : timeLimits)
//                    {
//                        trials++;
//                        marioAIOptions.setLevelLength(ll);
//                        marioAIOptions.setLevelDifficulty(levelDifficulty);
//                        marioAIOptions.setLevelType(levelType);
//                        marioAIOptions.setTimeLimit(timeLimit);
////                        basicTask.reset(marioAIOptions);
//                        if (!basicTask.runSingleEpisode(5))
//                        {
//                            System.out.println("[MarioAI Evaluation] : out of computational time per action!");
//                            disqualifications++;
//                            continue;
//                        }
//                        EvaluationInfo evaluationInfo = basicTask.getEnvironment().getEvaluationInfo();
//                        float f = evaluationInfo.computeWeightedFitness();
//                        if (verbose)
//                        {
//                            System.out.println("LEVEL OPTIONS: -ld " + levelDifficulty + " -lt " + levelType + " -pw " + !creaturesEnable +
//                                    " -tl " + timeLimit);
//                            System.out.println("Intermediate SCORE = " + f + "; Details: " + evaluationInfo.toStringSingleLine());
//                        }
//                        fitness += f;
//                    }
//                }
//            }
//        }
//
//    System.out.println("trials = " + trials);
//    System.out.println("disqualifications = " + disqualifications);
//    System.out.println("GamePlayTrack final score = " + fitness);
//
////        EvaluationInfo evaluationInfo = new EvaluationInfo(environment.getEvaluationInfoAsFloats());
////        System.out.println("evaluationInfo = " + evaluationInfo);
//    System.exit(0);

}
