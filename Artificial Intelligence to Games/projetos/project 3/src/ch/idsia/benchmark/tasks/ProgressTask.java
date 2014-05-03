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

package ch.idsia.benchmark.tasks;

import ch.idsia.agents.Agent;
import ch.idsia.benchmark.mario.engine.GlobalOptions;
import ch.idsia.tools.MarioAIOptions;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Sergey Karakovskiy
 * Date: Apr 8, 2009
 * Time: 11:26:43 AM
 * Package: ch.idsia.maibe.tasks
 */

public final class ProgressTask extends BasicTask implements Task, Cloneable
{
private int uniqueSeed;
private int fitnessEvaluations = 0;
public int uid;
private String fileTimeStamp = "-uid-" + uid + "-" + GlobalOptions.getTimeStamp();

//    private int startingSeed;

public ProgressTask(MarioAIOptions evaluationOptions)
{
    super(evaluationOptions);
    System.out.println("evaluationOptions = " + evaluationOptions);
    setOptionsAndReset(evaluationOptions);
}

public int totalEpisodes = 0;

private float evaluateSingleLevel(int ld, int tl, int ls, boolean vis, Agent controller)
{
    this.totalEpisodes++;

    float distanceTravelled = 0;
    options.setAgent(controller);
//        options.setLevelDifficulty(ld);
//        options.setTimeLimit(tl);
//        options.setLevelRandSeed(ls);
//        options.setVisualization(vis);
//        options.setFPS(vis ? 42 : 100);
//        this.setAgent(controller);
    this.setOptionsAndReset(options);
    this.runSingleEpisode(1);
    distanceTravelled += this.getEnvironment().getEvaluationInfo().computeDistancePassed();
    return distanceTravelled;
}

public int evaluate(Agent controller)
{
//        controller.reset();
//        options.setLevelRandSeed(startingSeed++);
//        System.out.println("controller = " + controller);
    int fitn = (int) this.evaluateSingleLevel(0, 40, this.uniqueSeed, false, controller);
//        System.out.println("fitn = " + fitn);
//        if (fitn > 1000)
//            fitn = this.evaluateSingleLevel(0, 150, this.uniqueSeed, false, controller);
////        System.out.println("fitn2 = " + fitn);
//        if (fitn > 4000)
//            fitn = 10000 + this.evaluateSingleLevel(1, 150, this.uniqueSeed, false, controller);
////        System.out.println("fitn3 = " + fitn);
//        if (fitn > 14000)
//            fitn = 20000 + this.evaluateSingleLevel(3, 150, this.uniqueSeed, false, controller);
//        if (fitn > 24000)
//        {
////            this.evaluateSingleLevel(3, 150, this.uniqueSeed, true, controller);
//            fitn = 40000 + this.evaluateSingleLevel(5, 160, this.uniqueSeed, false, controller);
//        }
////        if (fitn > 34000)
////            fitn = 40000 + this.evaluateSingleLevel(5, 160, this.uniqueSeed, false, controller);
//        if (fitn > 44000)
//            fitn = 50000 + this.evaluateSingleLevel(7, 160, this.uniqueSeed, false, controller);

    this.uniqueSeed += 1;
    this.fitnessEvaluations++;
    this.dumpFitnessEvaluation(fitn, "fitnesses-");
    return fitn;
}

public void dumpFitnessEvaluation(float fitness, String fileName)
{
    try
    {
        BufferedWriter out = new BufferedWriter(new FileWriter(fileName + fileTimeStamp + ".txt", true));
        out.write(this.fitnessEvaluations + " " + fitness + "\n");
        out.close();
    } catch (IOException e)
    {
        e.printStackTrace();
    }
}

public void doEpisodes(int amount, boolean verbose, final int repetitionsOfSingleEpisode)
{
    System.out.println("amount = " + amount);
}

public boolean isFinished()
{
    System.out.println("options = " + options);
    return false;
}

}
