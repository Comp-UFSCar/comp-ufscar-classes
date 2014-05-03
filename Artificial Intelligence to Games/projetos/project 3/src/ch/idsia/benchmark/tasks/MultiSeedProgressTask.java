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

/**
 * Created by IntelliJ IDEA.
 * User: julian
 * Date: May 23, 2009
 * Time: 11:37:47 PM
 */

public class MultiSeedProgressTask extends BasicTask implements Task
{
private MarioAIOptions options;
private int startingSeed = 0;
private int numberOfSeeds = 3;

public MultiSeedProgressTask(MarioAIOptions evaluationOptions)
{
    super(evaluationOptions);
    setOptionsAndReset(evaluationOptions);
}

public int evaluate(Agent controller)
{
    float distanceTravelled = 0;

    options.setAgent(controller);
//        this.setAgent(controller);

    for (int i = 0; i < numberOfSeeds; i++)
    {
        controller.reset();
        options.setLevelRandSeed(startingSeed + i);
//        this.reset(options);
        this.runSingleEpisode(1);
        distanceTravelled += this.getEnvironment().getEvaluationInfo().computeDistancePassed();
    }
    distanceTravelled = distanceTravelled / numberOfSeeds;
    return (int) distanceTravelled;
}

public void setStartingSeed(int seed)
{
    startingSeed = seed;
}

public void setNumberOfSeeds(int number)
{
    numberOfSeeds = number;
}

public void setOptionsAndReset(MarioAIOptions options)
{
    this.options = options;
}

public MarioAIOptions getOptions()
{
    return options;
}

public void doEpisodes(int amount, boolean verbose, final int repetitionsOfSingleEpisode)
{

}

public boolean isFinished()
{
    return true;
}

public void reset()
{

}
}
