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
import ch.idsia.tools.MarioAIOptions;
//import ch.idsia.tools.EvaluationInfo;
//import ch.idsia.tools.Evaluator;

//import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: julian
 * Date: Jun 13, 2009
 * Time: 2:44:59 PM
 */
public class MultiDifficultyProgressTask implements Task
{
private MarioAIOptions options;
private int startingSeed = 0;
private int[] difficulties = {0, 3, 5, 10};

public MultiDifficultyProgressTask(MarioAIOptions evaluationOptions)
{
    setOptionsAndReset(evaluationOptions);
}

public int evaluate(final Agent controller)
{
    int distanceTravelled = 0;
    float[] fitnesses = new float[difficulties.length + 1];
    for (int difficulty : difficulties)
    {
        controller.reset();
        options.setLevelRandSeed(startingSeed);
        options.setLevelDifficulty(difficulty);
        options.setAgent(controller);
//            Evaluator evaluator = new Evaluator(options);
//            List<EvaluationInfo> results = evaluator.evaluate();
//            EvaluationInfo result = results.get(0);
//            float thisDistance = result.computeDistancePassed();
//            fitnesses[i + 1] = thisDistance;
//            distanceTravelled += thisDistance;
    }
    distanceTravelled = distanceTravelled / difficulties.length;
    return distanceTravelled;
    //return new double[]{distanceTravelled};
}

public void setStartingSeed(int seed)
{
    startingSeed = seed;
}

public void setOptionsAndReset(MarioAIOptions options)
{
    this.options = options;
}

public void setOptionsAndReset(final String options)
{
    //To change body of implemented methods use File | Settings | File Templates.
}

public void doEpisodes(int amount, boolean verbose, final int repetitionsOfSingleEpisode)
{

}

public boolean isFinished()
{
    return false;
}

public void reset()
{

}

public String getName()
{
    return null;  //To change body of implemented methods use File | Settings | File Templates.
}

public void printStatistics()
{
    //To change body of implemented methods use File | Settings | File Templates.
}
}
