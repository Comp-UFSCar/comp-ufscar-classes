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
 * User: Sergey Karakovskiy, sergey.karakovskiy@gmail.com
 * Date: Jul 8, 2010
 * Time: 5:39:55 PM
 * Package: ch.idsia.scenarios.champ
 */

public class LearningTask extends BasicTask implements Task
{
private static final long EVALUATION_QUOTA = 100000;
private long currentEvaluation = 0;
public int uid;

private String fileTimeStamp = "-uid-" + uid + "-" + GlobalOptions.getTimeStamp();
private int fitnessEvaluations = 0;

public LearningTask(MarioAIOptions marioAIOptions)
{
    super(marioAIOptions);
}

public void reset(MarioAIOptions marioAIOptions)
{
    options = marioAIOptions;
    environment.reset(marioAIOptions);
}

public int evaluate(Agent agent)
{
    if (currentEvaluation++ > EVALUATION_QUOTA)
        return 0;
    options.setAgent(agent);
    environment.reset(options);
    fitnessEvaluations++; // TODO : remove either or two currentEvaluation or fitnessEvaluations
    this.runSingleEpisode(1);
    return this.getEvaluationInfo().computeWeightedFitness();
}

public static long getEvaluationQuota()
{return LearningTask.EVALUATION_QUOTA;}

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

}
