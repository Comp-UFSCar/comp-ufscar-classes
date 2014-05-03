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

package ch.idsia.unittests;

import ch.idsia.benchmark.mario.environments.MarioEnvironment;
import ch.idsia.benchmark.tasks.BasicTask;
import ch.idsia.benchmark.tasks.ReplayTask;
import ch.idsia.tools.MarioAIOptions;
import junit.framework.TestCase;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Random;

/**
 * Created by IntelliJ IDEA.
 * User: Sergey Karakovskiy, sergey.karakovskiy@gmail.com
 * Date: Aug 28, 2010
 * Time: 8:43:51 PM
 * Package: ch.idsia.unittests
 */
public class MarioEnvironmentTest extends TestCase
{
private boolean INCLUDE_VISUAL_TESTS = false;

@BeforeTest
public void setUp()
{
}

@AfterTest
public void tearDown()
{
    MarioEnvironment.getInstance().setReplayer(null);
}

@Test
public void testGetInstance() throws Exception
{
    assertNotNull(MarioEnvironment.getInstance());
}

@Test
public void testResetDefault() throws Exception
{
}

@Test
public void testReset() throws Exception
{
}

@Test
public void testTick() throws Exception
{
}

@Test
public void testGetMarioFloatPos() throws Exception
{
}

@Test
public void testGetMarioMode() throws Exception
{
}

@Test
public void testGetEnemiesFloatPos() throws Exception
{
}

@Test
public void testIsMarioOnGround() throws Exception
{
}

@Test
public void testIsMarioAbleToJump() throws Exception
{
}

@Test
public void testIsMarioCarrying() throws Exception
{
}

@Test
public void testIsMarioAbleToShoot() throws Exception
{
}

@Test
public void testGetObservationWidth() throws Exception
{
}

@Test
public void testGetObservationHeight() throws Exception
{
}

@Test
public void testGetMergedObservationZZ() throws Exception
{
}

@Test
public void testGetLevelSceneObservationZ() throws Exception
{
}

@Test
public void testGetEnemiesObservationZ() throws Exception
{
}

@Test
public void testGetKillsTotal() throws Exception
{
}

@Test
public void testGetKillsByFire() throws Exception
{
}

@Test
public void testGetKillsByStomp() throws Exception
{
}

@Test
public void testGetKillsByShell() throws Exception
{
}

@Test
public void testGetMarioStatus() throws Exception
{
}

@Test
public void testGetSerializedFullObservationZZ() throws Exception
{
    MarioAIOptions marioAIOptions = new MarioAIOptions("-vis off -rfw 5 -rfh 7");
    MarioEnvironment env = MarioEnvironment.getInstance();

    assertNotNull(env);

    env.reset(marioAIOptions);

    int obsSize = env.getReceptiveFieldHeight()*env.getReceptiveFieldWidth();

    int[] fullObs = env.getSerializedFullObservationZZ(1, 1);
    int[] levelScene = new int[obsSize];
    int[] enemies = new int[obsSize];
    int[] marioState = new int[11]; // 11 is a size of the array

    System.arraycopy(fullObs, 0, levelScene, 0, obsSize);
    System.arraycopy(fullObs, obsSize, enemies, 0, obsSize);
    System.arraycopy(fullObs, obsSize*2, marioState, 0, 11);

    int[] levelSceneOrig = env.getSerializedLevelSceneObservationZ(1);
    int[] enemiesOrig = env.getSerializedEnemiesObservationZ(1);
    int[] marioStateOrig = env.getMarioState();

    for (int i = 0; i < levelScene.length; i++)
        assertEquals(levelScene[i], levelSceneOrig[i]);
    for (int i = 0; i < enemies.length; i++)
        assertEquals(enemies[i], enemiesOrig[i]);
    for (int i = 0; i < marioState.length; i++)
        assertEquals(marioState[i], marioStateOrig[i]);
}

@Test
public void testGetSerializedLevelSceneObservationZ() throws Exception
{
}

@Test
public void testGetSerializedEnemiesObservationZ() throws Exception
{
}

@Test
public void testGetSerializedMergedObservationZZ() throws Exception
{
}

@Test
public void testGetCreaturesFloatPos() throws Exception
{
}

@Test
public void testGetMarioState() throws Exception
{
}

@Test
public void testPerformAction() throws Exception
{
}

@Test
public void testIsLevelFinished() throws Exception
{
}

@Test
public void testGetEvaluationInfoAsFloats() throws Exception
{
}

@Test
public void testGetEvaluationInfoAsString() throws Exception
{
}

@Test
public void testGetEvaluationInfo() throws Exception
{
}

@Test
public void testSetAgent() throws Exception
{
}

@Test
public void testGetIntermediateReward() throws Exception
{

}

@Test
public void testMarioCenterPos() throws Exception
{
    MarioAIOptions marioAIOptions = new MarioAIOptions("-vis off -rfw 5 -rfh 7");
    MarioEnvironment env = MarioEnvironment.getInstance();

    assertNotNull(env);

    env.reset(marioAIOptions);

    int[] pos = env.getMarioEgoPos();
    assertEquals(2, pos[0]);
    assertEquals(3, pos[1]);
}

@Test
public void testMarioReceptiveFieldSizeW5H7_vis() throws Exception
{
    if (INCLUDE_VISUAL_TESTS)
    {
        final MarioAIOptions marioAIOptions = new MarioAIOptions("-vis on -rfw 5 -rfh 7 -le 0 -srf on -gv on");
        final BasicTask basicTask = new BasicTask(marioAIOptions);
        basicTask.setOptionsAndReset(marioAIOptions);
        basicTask.runSingleEpisode(1);
        assertEquals(marioAIOptions.getReceptiveFieldHeight(), 7);
        assertEquals(marioAIOptions.getReceptiveFieldWidth(), 5);
    }
}

/**
 * @throws Exception
 */
@Test
public void testMarioReceptiveFieldSizeW8H6_vis() throws Exception
{
    if (INCLUDE_VISUAL_TESTS)
    {
        final MarioAIOptions marioAIOptions = new MarioAIOptions("-vis on -rfw 8 -rfh 6 -le 0 -srf on");
        final BasicTask basicTask = new BasicTask(marioAIOptions);
        basicTask.setOptionsAndReset(marioAIOptions);
        basicTask.runSingleEpisode(1);
        assertEquals(6, marioAIOptions.getReceptiveFieldHeight());
        assertEquals(8, marioAIOptions.getReceptiveFieldWidth());
        int[] pos = basicTask.getEnvironment().getMarioEgoPos();
        assertEquals(4, pos[0]);
        assertEquals(3, pos[1]);
    }
}

@Test
public void testRecordingFitness()
{
    final MarioAIOptions marioAIOptions = new MarioAIOptions("-vis off -ag ch.idsia.agents.controllers.ForwardJumpingAgent -rec recorderTest.zip -i on");
    final BasicTask basicTask = new BasicTask(marioAIOptions);
    basicTask.setOptionsAndReset(marioAIOptions);
    basicTask.runSingleEpisode(1);
    float originalFitness = basicTask.getEnvironment().getEvaluationInfo().computeWeightedFitness();
    System.out.println(basicTask.getEnvironment().getEvaluationInfoAsString());

    final ReplayTask replayTask = new ReplayTask();
    replayTask.reset("recorderTest.zip");
    replayTask.startReplay();
    System.out.println(replayTask.getEnvironment().getEvaluationInfoAsString());
    float replayFitness = replayTask.getEnvironment().getEvaluationInfo().computeWeightedFitness();
    assertEquals(originalFitness, replayFitness);
}

@Test
public void testRecordingEvaluationString()
{
    final MarioAIOptions marioAIOptions = new MarioAIOptions("-vis off -ag ch.idsia.agents.controllers.ForwardJumpingAgent -rec recorderTest.zip -i on");
    final BasicTask basicTask = new BasicTask(marioAIOptions);
    basicTask.setOptionsAndReset(marioAIOptions);
    basicTask.runSingleEpisode(1);
    String playEvaluationString = basicTask.getEnvironment().getEvaluationInfoAsString();
    System.out.println(playEvaluationString);

    final ReplayTask replayTask = new ReplayTask();
    replayTask.reset("recorderTest.zip");
    replayTask.startReplay();
    String replayEvaluationString = replayTask.getEnvironment().getEvaluationInfoAsString();
    System.out.println(replayEvaluationString);

    for (int i = 0; i < playEvaluationString.length(); i++)
        assertEquals(playEvaluationString.charAt(i), replayEvaluationString.charAt(i));
}

@Test
public void testLazyRecordingCreation()
{
    final MarioAIOptions marioAIOptions = new MarioAIOptions("-vis off -ag ch.idsia.agents.controllers.ForwardJumpingAgent -rec lazy -i on");
    final BasicTask basicTask = new BasicTask(marioAIOptions);
    basicTask.setOptionsAndReset(marioAIOptions);
    basicTask.runSingleEpisode(1);

    Random ran = new Random(System.currentTimeMillis());

    String filename = "lazyRecorderTest" + ran.nextInt(1000) + ".zip";
    basicTask.getEnvironment().saveLastRun(filename);

    try
    {
        FileInputStream in = new FileInputStream(filename);
    } catch (FileNotFoundException ex)
    {
        fail("Recorder File Not Found");
    }
}

@Test
public void testLazyRecordingFitness()
{
    try
    {
        final MarioAIOptions marioAIOptions = new MarioAIOptions("-vis off -ag ch.idsia.agents.controllers.ForwardJumpingAgent -rec lazy -i on");
        final BasicTask basicTask = new BasicTask(marioAIOptions);
        basicTask.setOptionsAndReset(marioAIOptions);
        basicTask.runSingleEpisode(1);
        float originalFitness = basicTask.getEnvironment().getEvaluationInfo().computeWeightedFitness();
        System.out.println(basicTask.getEnvironment().getEvaluationInfoAsString());

        basicTask.getEnvironment().saveLastRun("lazyRecorderTest.zip");

        final ReplayTask replayTask = new ReplayTask();
        replayTask.reset("lazyRecorderTest.zip");
        replayTask.startReplay();
        System.out.println(replayTask.getEnvironment().getEvaluationInfoAsString());
        float replayFitness = replayTask.getEnvironment().getEvaluationInfo().computeWeightedFitness();
        assertEquals(originalFitness, replayFitness);
    } catch (Exception ex)
    {
        fail("Exception during test");
    }
}

@Test
public void testMultipleLazyRecordings()
{
    try
    {
        final MarioAIOptions marioAIOptions = new MarioAIOptions("-vis off -ag ch.idsia.agents.controllers.ForwardJumpingAgent -rec lazy -i on");
        final BasicTask basicTask = new BasicTask(marioAIOptions);
        basicTask.setOptionsAndReset(marioAIOptions);
        basicTask.runSingleEpisode(1);

        marioAIOptions.setLevelDifficulty(1);
        basicTask.setOptionsAndReset(marioAIOptions);
        basicTask.runSingleEpisode(1);
        float originalFitness = basicTask.getEnvironment().getEvaluationInfo().computeWeightedFitness();

        basicTask.getEnvironment().saveLastRun("lazyRecorderTest.zip");

        final ReplayTask replayTask = new ReplayTask();
        replayTask.reset("lazyRecorderTest.zip");
        replayTask.startReplay();
        System.out.println(replayTask.getEnvironment().getEvaluationInfoAsString());
        float replayFitness = replayTask.getEnvironment().getEvaluationInfo().computeWeightedFitness();
        assertEquals(originalFitness, replayFitness);

    } catch (Exception ex)
    {
        fail("Exception during test");
    }
}

@Test
public void testRecordingTrace()
{
    final MarioAIOptions marioAIOptions = new MarioAIOptions("-vis off -ag ch.idsia.agents.controllers.ForwardJumpingAgent -rec recorderTest.zip -i on");
    final BasicTask basicTask = new BasicTask(marioAIOptions);
    basicTask.setOptionsAndReset(marioAIOptions);
    basicTask.runSingleEpisode(1);

    int[][] firstTrace = basicTask.getEnvironment().getEvaluationInfo().marioTrace;
    System.out.println(basicTask.getEnvironment().getEvaluationInfoAsString());

    final ReplayTask replayTask = new ReplayTask();
    replayTask.reset("recorderTest.zip");
    replayTask.startReplay();

    int[][] secondTrace = replayTask.getEnvironment().getEvaluationInfo().marioTrace;
    System.out.println(replayTask.getEnvironment().getEvaluationInfoAsString());

    for (int j = 0; j < firstTrace[0].length; ++j)
        for (int i = 0; i < firstTrace.length; ++i)
            assertEquals(firstTrace[i][j], secondTrace[i][j]);
}

}