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

import ch.idsia.agents.Agent;
import ch.idsia.agents.controllers.ForwardAgent;
import ch.idsia.benchmark.mario.environments.Environment;
import ch.idsia.benchmark.tasks.BasicTask;
import ch.idsia.tools.MarioAIOptions;

import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Sergey Karakovskiy, sergey@idsia.ch
 * Date: May 7, 2009
 * Time: 4:38:23 PM
 * Package: ch.idsia
 */

public class Custom
{
public static void main(String[] args)
{
//final String argsString = "-vis on";
    final MarioAIOptions marioAIOptions = new MarioAIOptions(args);
    final Agent agent = new ForwardAgent();
    final BasicTask basicTask = new BasicTask(marioAIOptions);
    for (int i = 0; i < 10; ++i)
    {
        int seed = 0;
        do
        {
            marioAIOptions.setLevelDifficulty(i);
            marioAIOptions.setLevelRandSeed(seed++);
            basicTask.setOptionsAndReset(marioAIOptions);
            basicTask.runSingleEpisode(1);
            System.out.println(basicTask.getEnvironment().getEvaluationInfoAsString());
        } while (basicTask.getEnvironment().getEvaluationInfo().marioStatus != Environment.MARIO_STATUS_WIN);
    }
    Runtime rt = Runtime.getRuntime();
    try
    {
//            Process proc = rt.exec("/usr/local/bin/mate " + marioTraceFileName);
        Process proc = rt.exec("python hello.py");
    } catch (IOException e)
    {
        e.printStackTrace();
    }
    System.exit(0);

}
}
