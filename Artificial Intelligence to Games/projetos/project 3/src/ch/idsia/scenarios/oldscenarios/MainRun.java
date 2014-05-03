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
import ch.idsia.agents.controllers.ScaredAgent;
import ch.idsia.agents.controllers.TimingAgent;
import ch.idsia.benchmark.mario.simulation.SimulationOptions;
import ch.idsia.tools.MarioAIOptions;
import ch.idsia.utils.statistics.StatisticalSummary;

/**
 * Created by IntelliJ IDEA.
 * User: Sergey Karakovskiy, firstName_at_idsia_dot_ch
 * Date: May 7, 2009
 * Time: 4:35:08 PM
 * Package: ch.idsia
 */

public class MainRun
{
final static int numberOfTrials = 10;
final static boolean scoring = false;
private static int killsSum = 0;
private static float marioStatusSum = 0;
private static int timeLeftSum = 0;
private static int marioModeSum = 0;
private static boolean detailedStats = false;

public static void main(String[] args)
{
    MarioAIOptions marioAIOptions = new MarioAIOptions(args);
    SimulationOptions evaluationOptions = marioAIOptions;  // if none options mentioned, all defalults are used.
    createAgentsPool();

    if (scoring)
        scoreAllAgents(marioAIOptions);
    else
    {
//            Evaluator evaluator = new Evaluator(evaluationOptions);
//            evaluationOptions.setAgent(AgentsPool.getCurrentAgent());
//            evaluationOptions.setAgent(new AmiCoAgent(amicoModuleName, amicoAgentName));
//            while (marioAIOptions.getNumberOfTrials() >= SimulationOptions.currentTrial) {
//                List<EvaluationInfo> evaluationSummary;
//                evaluator.evaluate();
//            }
//        LOGGER.save("log.txt");
    }

    if (marioAIOptions.isExitProgramWhenFinished())
        System.exit(0);
}

private static boolean calledBefore = false;

public static void createAgentsPool()
{
    if (!calledBefore)
    {
        // Create an Agent here or mention the set of agents you want to be available for the framework.
        // All created agents by now are used here.
        // They can be accessed by just setting the commandline property -ag to the name of desired agent.
        calledBefore = true;
        //addAgentToThePool
//            AgentsPool.addAgent(new ForwardAgent());
//            AgentsPool.addAgent(new ForwardJumpingAgent());
        //ice-gic:
        AgentsPool.addAgent(new ScaredAgent());
//            AgentsPool.addAgent(new Perez());
//            AgentsPool.addAgent(new AdaptiveAgent());
//            AgentsPool.addAgent(new AIwesome());
//            AgentsPool.addAgent(new TutchekAgent());
//            AgentsPool.addAgent(new SlowAgent());
//            AgentsPool.addAgent(new AStarAgent());
//            AgentsPool.addAgent(new RjAgent());
//            AgentsPool.addAgent(new SergeyKarakovskiy_JumpingAgent());
        //CIG:
//            AgentsPool.addAgent(new TrondEllingsen_LuckyAgent());
//            AgentsPool.addAgent(new SergeyPolikarpov_SimpleCyberNeuronAgent());
//            AgentsPool.addAgent(new SpencerSchumann_SlideRule());
//            AgentsPool.addAgent(new AndySloane_BestFirstAgent());
//            AgentsPool.addAgent(AgentsPool.loadAgent("competition/cig/matthewerickson/matthewerickson.xml"));
//            AgentsPool.addAgent(AgentsPool.loadAgent("competition/icegic/erek/erekspeed.xml")); // out of memory exception
//            AgentsPool.addAgent(new PalerAgent());
//            AgentsPool.addAgent(new PeterLawford_SlowAgent());
    }
}

public static void scoreAllAgents(MarioAIOptions marioAIOptions)
{
    int startingSeed = marioAIOptions.getLevelRandSeed();
    for (Agent agent : AgentsPool.getAgentsCollection())
        score(agent, startingSeed, marioAIOptions);

//        startingSeed = 0;
//        for (Agent agent : AgentsPool.getAgentsCollection())
//            score(agent, startingSeed, marioAIOptions);

}


public static void score(Agent agent, int startingSeed, MarioAIOptions marioAIOptions)
{
    TimingAgent controller = new TimingAgent(agent);
    SimulationOptions options = marioAIOptions;

//        options.setEvaluationQuota(1);
//        options.setVisualization(false);
//        options.setMaxFPS(true);
    System.out.println("\nScoring controller " + agent.getName() + " with starting seed " + startingSeed);

    double competitionScore = 0;
    killsSum = 0;
    marioStatusSum = 0;
    timeLeftSum = 0;
    marioModeSum = 0;

    competitionScore += testConfig(controller, options, startingSeed, 0, false);
    competitionScore += testConfig(controller, options, startingSeed, 3, false);
    competitionScore += testConfig(controller, options, startingSeed, 5, false);
    competitionScore += testConfig(controller, options, startingSeed, 10, false);

    System.out.println("\nCompetition score: " + competitionScore + "\n");
    System.out.println("Number of levels cleared = " + marioStatusSum);
    System.out.println("Additional (tie-breaker) info: ");
    System.out.println("Total time left = " + timeLeftSum);
    System.out.println("Total kills = " + killsSum);
    System.out.println("Mario mode (small, large, fire) sum = " + marioModeSum);
    System.out.println("TOTAL SUM for " + agent.getName() + " = " + (competitionScore + killsSum + marioStatusSum + marioModeSum + timeLeftSum));
}

public static double testConfig(TimingAgent controller, SimulationOptions options, int seed, int levelDifficulty, boolean paused)
{
    options.setLevelDifficulty(levelDifficulty);
    StatisticalSummary ss = test(controller, options, seed);
    double averageTimeTaken = controller.averageTimeTaken();
    System.out.printf("Difficulty %d score %.4f (avg time %.4f)\n",
            levelDifficulty, ss.mean(), averageTimeTaken);
    return ss.mean();
}

public static StatisticalSummary test(Agent controller, SimulationOptions options, int seed)
{
    StatisticalSummary ss = new StatisticalSummary();
    int kills = 0;
    int timeLeft = 0;
    int marioMode = 0;
    float marioStatus = 0;

//        options.setEvaluationQuota(numberOfTrials);
    for (int i = 0; i < numberOfTrials; i++)
    {
        options.setLevelRandSeed(seed + i);
        options.setLevelLength(200 + (i * 128) + (seed % (i + 1)));
        options.setLevelType(i % 3);
        controller.reset();
        options.setAgent(controller);
//            Evaluator evaluator = new Evaluator (options);
//            EvaluationInfo result = evaluator.evaluate().get(0);
//            kills += result.computeKillsTotal();
//            timeLeft += result.timeLeft;
//            marioMode += result.marioMode;
//            marioStatus += result.marioStatus;
//            System.out.println("\ntrial # " + i);
//            System.out.println("result.timeLeft = " + result.timeLeft);
//            System.out.println("result.marioMode = " + result.marioMode);
//            System.out.println("result.marioStatus = " + result.marioStatus);
//            System.out.println("result.computeKillsTotal() = " + result.computeKillsTotal());
//            ss.add (result.computeDistancePassed());
    }

    if (detailedStats)
    {
        System.out.println("\n===================\nStatistics over " + numberOfTrials + " trials for " + controller.getName());
        System.out.println("Total kills = " + kills);
        System.out.println("marioStatus = " + marioStatus);
        System.out.println("timeLeft = " + timeLeft);
        System.out.println("marioMode = " + marioMode);
        System.out.println("===================\n");
    }

    killsSum += kills;
    marioStatusSum += marioStatus;
    timeLeftSum += timeLeft;
    marioModeSum += marioMode;

    return ss;
}
}
