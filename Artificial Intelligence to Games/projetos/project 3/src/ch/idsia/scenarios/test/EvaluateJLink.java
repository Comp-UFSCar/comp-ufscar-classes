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

package ch.idsia.scenarios.test;

import ch.idsia.agents.Agent;
import ch.idsia.agents.learning.LargeSRNAgent;
import ch.idsia.benchmark.mario.engine.GlobalOptions;
import ch.idsia.benchmark.mario.simulation.SimulationOptions;
import ch.idsia.evolution.SRN;
import ch.idsia.tools.MarioAIOptions;

/**
 * Created by IntelliJ IDEA.
 * User: koutnij
 * Date: Jul 27, 2009
 * Time: 4:34:37 PM
 */
public class EvaluateJLink
{
/**
 * returns {in, rec, out} array. Just to make math and java codes fully independent.
 */
public static int[] getDimension()
{
    return new int[]{getInputSize() * getInputSize() * 2 + 3, 6, 6};
}

/**
 * returns length of an edge of the input window square
 */
public static int getInputSize()
{
    return 7;
}

public double evaluateLargeSRN(double[][] inputs, double[][] recurrent, double[][] output, int level, int seed)
{
    // System.out.println(inputs.length+" "+inputs[0].length);
    // System.out.println(recurrent.length+" "+recurrent[0].length);
    // System.out.println(output.length+" "+output[0].length);
    SRN srn = new SRN(inputs, recurrent, output, recurrent.length, output[0].length);
    Agent agent = new LargeSRNAgent(srn);
    SimulationOptions options = new MarioAIOptions(new String[0]);
    final int startingSeed = 0;
    options.setLevelRandSeed(seed);
//        options.setEvaluationQuota(1);
    options.setVisualization(false);
    options.setFPS(GlobalOptions.MaxFPS);
    options.setLevelDifficulty(level);
    agent.reset();
    options.setAgent(agent);
//        Evaluator evaluator = new Evaluator (options);
//        EvaluationInfo result = evaluator.evaluate().get(0);
    // System.out.print(".");
//        double score = result.computeDistancePassed();
//         System.out.println("score: " +score);
//        return score;
    return -4242;
}

}
