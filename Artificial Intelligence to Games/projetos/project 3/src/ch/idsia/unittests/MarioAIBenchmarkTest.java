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

package ch.idsia.unittests;

import ch.idsia.benchmark.mario.engine.GlobalOptions;
import ch.idsia.benchmark.mario.engine.sprites.Mario;
import ch.idsia.benchmark.tasks.BasicTask;
import ch.idsia.tools.MarioAIOptions;
import ch.idsia.tools.ReplayerOptions;
import junit.framework.TestCase;
import org.junit.Test;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;

import java.util.Random;

/**
 * Created by IntelliJ IDEA.
 * User: Sergey Karakovskiy, sergey.karakovskiy@gmail.com
 * Date: Aug 28, 2010
 * Time: 8:39:30 PM
 * Package: ch.idsia.unittests
 */
public final class MarioAIBenchmarkTest extends TestCase
{
@BeforeTest
public void setUp()
{
}

@AfterTest
public void tearDown()
{
}

@Test
public void testRandomPersistence()
{
    Random r1 = new Random(42);
    int seq[] = new int[]{2, 0, 2, 0, 1, 3, 1, 2, 2, 0, 3, 1, 1, 1, 1, 2, 1, 3, 3, 3, 3, 0, 1, 1, 2, 3, 1, 3, 0, 0, 2, 1, 0, 1, 3, 1, 0, 0, 2, 1, 3, 2};
    for (int i = 0; i < 42; ++i)
    {
        assertEquals(r1.nextInt(4), seq[i]);
    }
}


@Test
public void testAgentLoadAndGetAgentName()
{
    final MarioAIOptions marioAIOptions = new MarioAIOptions("-vis off -lca off -ag ch.idsia.agents.controllers.ForwardJumpingAgent");
    assertNotNull(marioAIOptions.getAgent());
    assertEquals(marioAIOptions.getAgent().getName(), "ForwardJumpingAgent");
    assertEquals(marioAIOptions.getAgentFullLoadName(), "ch.idsia.agents.controllers.ForwardJumpingAgent");
}

@Test
public void testAgentLoadHumanKeyboardAgent()
{
    final MarioAIOptions marioAIOptions = new MarioAIOptions("-vis off -lca off");
    assertNotNull(marioAIOptions.getAgent());
    assertEquals(marioAIOptions.getAgent().getName(), "HumanKeyboardAgent");
    assertEquals(marioAIOptions.getAgentFullLoadName(), "ch.idsia.agents.controllers.human.HumanKeyboardAgent");
}


@Test
public void testForwardJumpingAgentFitnessWithoutCreatures()
{
    final MarioAIOptions marioAIOptions = new MarioAIOptions("-vis off -le off -ag ch.idsia.agents.controllers.ForwardJumpingAgent -echo on");
    final BasicTask basicTask = new BasicTask(marioAIOptions);
    basicTask.setOptionsAndReset(marioAIOptions);
    basicTask.runSingleEpisode(1);
    System.out.println(basicTask.getEnvironment().getEvaluationInfoAsString());
    assertEquals(7288, basicTask.getEnvironment().getEvaluationInfo().computeWeightedFitness());
}

@Test
public void testForwardJumpingAgentFitnessWithDefaultCreatures()
{
    final MarioAIOptions marioAIOptions = new MarioAIOptions("-vis off -ag ch.idsia.agents.controllers.ForwardJumpingAgent -echo on");
    final BasicTask basicTask = new BasicTask(marioAIOptions);
    basicTask.setOptionsAndReset(marioAIOptions);
    basicTask.runSingleEpisode(1);
    System.out.println(basicTask.getEnvironment().getEvaluationInfoAsString());
    assertEquals(8134, basicTask.getEnvironment().getEvaluationInfo().computeWeightedFitness());
}

@Test
public void testReceptiveField_1x2()
{
    final MarioAIOptions marioAIOptions = new MarioAIOptions("-vis on -fps 75 -ag ch.idsia.agents.controllers.ForwardAgent -echo on -rfw 1 -rfh 2 -srf on");
    final BasicTask basicTask = new BasicTask(marioAIOptions);
    basicTask.setOptionsAndReset(marioAIOptions);
    basicTask.runSingleEpisode(1);
    System.out.println(basicTask.getEnvironment().getEvaluationInfoAsString());
    assertEquals(6557, basicTask.getEnvironment().getEvaluationInfo().computeWeightedFitness());
}

@Test
public void testReceptiveField_3x1()
{
    final MarioAIOptions marioAIOptions = new MarioAIOptions("-vis off -ag ch.idsia.agents.controllers.ForwardAgent -echo on -rfw 3 -rfh 1 -srf on");
    final BasicTask basicTask = new BasicTask(marioAIOptions);
    basicTask.setOptionsAndReset(marioAIOptions);
    basicTask.runSingleEpisode(1);
    System.out.println(basicTask.getEnvironment().getEvaluationInfoAsString());
    assertEquals(6557, basicTask.getEnvironment().getEvaluationInfo().computeWeightedFitness());
}

@Test
public void testReceptiveField_1x1()
{
    final MarioAIOptions marioAIOptions = new MarioAIOptions("-vis off -ag ch.idsia.agents.controllers.ForwardAgent -echo on -rfw 1 -rfh 1 -srf on");
    final BasicTask basicTask = new BasicTask(marioAIOptions);
    basicTask.setOptionsAndReset(marioAIOptions);
    basicTask.runSingleEpisode(1);
    System.out.println(basicTask.getEnvironment().getEvaluationInfoAsString());
    assertEquals(6557, basicTask.getEnvironment().getEvaluationInfo().computeWeightedFitness());
}

@Test
public void testForwardAgentFitnessWithDefaultCreatures()
{
    final MarioAIOptions marioAIOptions = new MarioAIOptions("-vis off -ag ch.idsia.agents.controllers.ForwardAgent -echo on");
    final BasicTask basicTask = new BasicTask(marioAIOptions);
    basicTask.setOptionsAndReset(marioAIOptions);
    basicTask.runSingleEpisode(1);
    System.out.println(basicTask.getEnvironment().getEvaluationInfoAsString());
    assertEquals(5225, basicTask.getEnvironment().getEvaluationInfo().computeWeightedFitness());
}

@Test
public void testForwardAgentFitnessWithDefaultCreaturesVisual()
{
    final MarioAIOptions marioAIOptions = new MarioAIOptions("-vis on -fps 100 -ag ch.idsia.agents.controllers.ForwardAgent -echo on");
    final BasicTask basicTask = new BasicTask(marioAIOptions);
    basicTask.setOptionsAndReset(marioAIOptions);
    basicTask.runSingleEpisode(1);
    System.out.println(basicTask.getEnvironment().getEvaluationInfoAsString());
    assertEquals(5225, basicTask.getEnvironment().getEvaluationInfo().computeWeightedFitness());
}

@Test
public void testForwardAgentCoinsCollected()
{
    //TODo:!H! testForwardAgentCoinsCollected
}

@Test
public void testForwardAgentFitnessWithoutCreatures()
{
    final MarioAIOptions marioAIOptions = new MarioAIOptions("-vis off -le 0 -ag ch.idsia.agents.controllers.ForwardAgent -echo on");
    final BasicTask basicTask = new BasicTask(marioAIOptions);
    basicTask.setOptionsAndReset(marioAIOptions);
    basicTask.runSingleEpisode(1);
    System.out.println(basicTask.getEnvironment().getEvaluationInfoAsString());
    assertEquals(7400, basicTask.getEnvironment().getEvaluationInfo().computeWeightedFitness());
}

@Test
public void testForwardJumpingAgentCoinsCollected()
{
    //TODo:!H! testForwardJumpingAgentCoinsCollected
}

/**
 * In this test benchmark in launched and stopped. Press "SPACE" key to resume the game.
 * You can press "SPACE" to stop and resume the gameplay.
 */
@Test
public void testStopGameplay()
{
    final MarioAIOptions marioAIOptions = new MarioAIOptions("-vis on -rfw 9 -rfh 5 -stop on -ll 100 -ag ch.idsia.agents.controllers.ForwardAgent -echo on");
    assertEquals(true, marioAIOptions.isStopGamePlay());
    final BasicTask basicTask = new BasicTask(marioAIOptions);
    basicTask.setOptionsAndReset(marioAIOptions);
    basicTask.runSingleEpisode(1);
}

@Test
public void testScaredShooty_with10Goombas()
{
    final MarioAIOptions marioAIOptions = new MarioAIOptions("-vis off -rfw 5 -rfh 5 -ag ch.idsia.agents.controllers.ScaredShooty -lf on -ltb off -lg off -lb off -i on -le g:10");
    final BasicTask basicTask = new BasicTask(marioAIOptions);
    basicTask.setOptionsAndReset(marioAIOptions);
    basicTask.runSingleEpisode(1);
    assertEquals(10, basicTask.getEnvironment().getEvaluationInfo().killsByFire);
}

@Test
public void testScaredShooty_with10Goombas5RedKoopas()
{
    final MarioAIOptions marioAIOptions = new MarioAIOptions("-vis off -rfw 5 -rfh 5 -ag ch.idsia.agents.controllers.ScaredShooty -lf on -ltb off -lg off -lb off -i on -le g:10,rk:5");
    final BasicTask basicTask = new BasicTask(marioAIOptions);
    basicTask.setOptionsAndReset(marioAIOptions);
    basicTask.runSingleEpisode(1);
    assertEquals(15, basicTask.getEnvironment().getEvaluationInfo().killsByFire);
}

@Test
public void testScaredShooty_with10GoombasWinged10Goombas()
{
    final MarioAIOptions marioAIOptions = new MarioAIOptions("-vis on -rfw 5 -rfh 5 -ag ch.idsia.agents.controllers.ScaredShooty -lf on -ltb off -lg off -lb off -i on -le g:10,gw:10");
    final BasicTask basicTask = new BasicTask(marioAIOptions);
    basicTask.setOptionsAndReset(marioAIOptions);
    basicTask.runSingleEpisode(1);
    assertEquals(10, basicTask.getEnvironment().getEvaluationInfo().killsByFire);
}

@Test
public void testScaredShooty_with6Goombas3Spikies()
{
    final MarioAIOptions marioAIOptions = new MarioAIOptions("-vis off -rfw 5 -rfh 5 -ag ch.idsia.agents.controllers.ScaredShooty -lf on -ltb off -lg off -lb off -i on -le g:6,s:3");
    final BasicTask basicTask = new BasicTask(marioAIOptions);
    basicTask.setOptionsAndReset(marioAIOptions);
    basicTask.runSingleEpisode(1);
    assertEquals(6, basicTask.getEnvironment().getEvaluationInfo().killsByFire);
}

@Test
public void testRecorderOptionsParser_OneFile()
{
    ReplayerOptions recOp = new ReplayerOptions("test;3:5;6:11");
    assertEquals("test", recOp.getNextReplayFile());
}

@Test
public void testRecorderOptionsParser_TwoFiles()
{
    ReplayerOptions recOp = new ReplayerOptions("test;3:5;6:11,test2");
    assertEquals("test", recOp.getNextReplayFile());
    assertEquals("test2", recOp.getNextReplayFile());
}

@Test
public void testScale2XEnabledOnStartup()
{
    MarioAIOptions marioAIOptions = new MarioAIOptions("-z on");

    BasicTask basicTask = new BasicTask(marioAIOptions);
    basicTask.setOptionsAndReset(marioAIOptions);
    assertTrue(GlobalOptions.isScale2x);
}

@Test
public void testLevelCompletionRegression()
{
	final MarioAIOptions marioAIOptions = new MarioAIOptions("-vis off -ag ch.idsia.agents.controllers.ForwardJumpingAgent -i on");
	final BasicTask basicTask = new BasicTask(marioAIOptions);
	basicTask.setOptionsAndReset(marioAIOptions);
	basicTask.runSingleEpisode(1);
	boolean end = basicTask.getEvaluationInfo().levelLength == basicTask.getEvaluationInfo().distancePassedCells;
	boolean win = basicTask.getEvaluationInfo().marioStatus == Mario.STATUS_WIN;
	System.out.println(basicTask.getEnvironment().getEvaluationInfoAsString());
	assertEquals(end, win);
}

}