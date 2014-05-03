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

package ch.idsia.scenarios.oldscenarios;

import ch.idsia.benchmark.experiments.EpisodicExperiment;
import ch.idsia.benchmark.experiments.Experiment;
import ch.idsia.benchmark.tasks.ProgressTask;
import ch.idsia.benchmark.tasks.Task;
import ch.idsia.tools.MarioAIOptions;

/**
 * Created by IntelliJ IDEA.
 * User: Sergey Karakovskiy, sergey at idsia dot ch
 * Date: Feb 24, 2010
 * Time: 12:57:18 PM
 * Package: ch.idsia.scenarios
 */
public class GeneralScenario
{
public static void main(String[] args)
{
//        Agent agent = new ForwardAgent();
    MarioAIOptions options = new MarioAIOptions(args);
    Task task = new ProgressTask(options);
    Experiment exp = new EpisodicExperiment(task, options.getAgent());
    exp.doEpisodes(2);
}

/*
        MarioAIOptions options = new MarioAIOptions(args);
//        Evaluator evaluator = new Evaluator(options);
//        Agent agent = new ForwardAgent();
        options.setFPS(24);
//        options.setVisualization(false);
        String amicoModuleName = options.getPyAmiCoModuleName();
        System.out.println("amicoModuleName = " + amicoModuleName);
//        String amicoAgentName = "ForwardAgent";
        System.out.println("options.getAgentFullLoadName() = " + options.getAgentFullLoadName());
        Agent agent = new AmiCoAgent(amicoModuleName, options.getAgentFullLoadName());
        options.setAgent(agent);
//        evaluator.evaluate();
//        System.out.println("evaluator = " + evaluator.getMeanEvaluationSummary());
        System.exit(0);

 */
}
