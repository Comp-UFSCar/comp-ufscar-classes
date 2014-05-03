/*
 * Copyright (c) 2009-2010, Sergey Karakovskiy and Julian Togelius
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the Mario AI nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
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

package ch.idsia.scenarios;

import ch.idsia.benchmark.tasks.BasicTask;
import ch.idsia.benchmark.tasks.MarioCustomSystemOfValues;
import ch.idsia.tools.MarioAIOptions; /**
 * Created by IntelliJ IDEA.
 * User: julian
 * Date: May 5, 2009
 * Time: 12:46:43 PM
 */

/**
 * The <code>Play</code> class shows how simple is to run a MarioAI Benchmark.
 * It shows how to set up some parameters, create a task,
 * use the CmdLineParameters class to set up options from command line if any.
 * Defaults are used otherwise.
 *
 * @author Julian Togelius, Sergey Karakovskiy
 * @version 1.0, May 5, 2009
 */

public final class Play
{
/**
 * <p>An entry point of the class.</p>
 *
 * @param args input parameters for customization of the benchmark.
 * @see ch.idsia.scenarios.oldscenarios.MainRun
 * @see ch.idsia.tools.MarioAIOptions
 * @see ch.idsia.benchmark.mario.simulation.SimulationOptions
 * @since MarioAI-0.1
 */

public static void main(String[] args)
{
    final MarioAIOptions marioAIOptions = new MarioAIOptions(args);
    final BasicTask basicTask = new BasicTask(marioAIOptions);
    marioAIOptions.setVisualization(true);
//        basicTask.reset(marioAIOptions);
    final MarioCustomSystemOfValues m = new MarioCustomSystemOfValues();
//        basicTask.runSingleEpisode();
    // run 1 episode with same options, each time giving output of Evaluation info.
    // verbose = false
    basicTask.doEpisodes(1, false, 1);
    System.out.println("\nEvaluationInfo: \n" + basicTask.getEnvironment().getEvaluationInfoAsString());
    System.out.println("\nCustom : \n" + basicTask.getEnvironment().getEvaluationInfo().computeWeightedFitness(m));
    System.exit(0);
}
}
