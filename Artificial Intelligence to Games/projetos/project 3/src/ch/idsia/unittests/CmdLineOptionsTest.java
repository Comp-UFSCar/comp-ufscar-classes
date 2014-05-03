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
import ch.idsia.tools.MarioAIOptions;
import ch.idsia.utils.ParameterContainer;
import junit.framework.TestCase;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * Created by IntelliJ IDEA.
 * User: Sergey Karakovskiy, sergey.karakovskiy@gmail.com
 * Date: Aug 28, 2010
 * Time: 8:36:02 PM
 * Package: ch.idsia.unittests
 */

public class CmdLineOptionsTest extends TestCase
{
MarioAIOptions marioAIOptions;

@BeforeTest
public void setUp()
{
    marioAIOptions = new MarioAIOptions();
}

@AfterTest
public void tearDown()
{
}

@Test
public void testTotalNumberOfOptions() throws Exception
{
    assertEquals(56, marioAIOptions.getTotalNumberOfOptions());
}

@Test
public void testAllOptionsHaveDefaults()
{
    assertEquals(ParameterContainer.getTotalNumberOfOptions(), ParameterContainer.getNumberOfAllowedOptions());
}

@Test
public void testSetArgs() throws Exception
{
    String args = "-ag ch.idsia.agents.controllers.human.HumanKeyboardAgent" +
            //  " -amico off" +
            " -echo off" +
            " -ewf on" +
            " -fc off" +
            " -cgr 1.0" +
            " -mgr 1.0" +
            " -gv off" +
            " -gvc off" +
            " -i off" +
            " -jp 7" +
            " -ld 0" +
            " -ll 320" +
            " -ls 0" +
            " -lt 0" +
            " -fps 24" +
            " -mm 2" +
            " -pw off" +
            " -pr off" +
            " -rfh 19" +
            " -rfw 19" +
            " -srf off" +
            " -tl 200" +
            " -tc off" +
            " -vaot off" +
            " -vlx 0" +
            " -vly 0" +
            " -vis on" +
            " -vw 320" +
            " -vh 240" +
            " -zs 1" +
            " -ze 0" +
            " -lh 15" +
            " -lde off" +
            " -lca on" +
            " -lhs on" +
            " -ltb on" +
            " -lco on" +
            " -lb on" +
            " -lg on" +
            " -lhb off" +
            " -le g,gw,gk,gkw,rk,rkw,s,sw" +
            " -lf off" +
            " -gmm 1";
    marioAIOptions.setArgs(args);
    assertEquals(marioAIOptions.getAgentFullLoadName(), "ch.idsia.agents.controllers.human.HumanKeyboardAgent");
    assertEquals(marioAIOptions.isEcho(), false);
    assertEquals(marioAIOptions.isExitProgramWhenFinished(), true);
    assertEquals(marioAIOptions.isFrozenCreatures(), false);
    assertEquals(marioAIOptions.getCreaturesGravity(), 1.0f);
    assertEquals(marioAIOptions.getMarioGravity(), 1.0f);
    assertEquals(marioAIOptions.isGameViewer(), false);
    assertEquals(marioAIOptions.isGameViewerContinuousUpdates(), false);
    assertEquals(marioAIOptions.isMarioInvulnerable(), false);
    assertEquals(marioAIOptions.getJumpPower(), 7.0f);
    assertEquals(marioAIOptions.getLevelDifficulty(), 0);
    assertEquals(marioAIOptions.getLevelLength(), 320);
    assertEquals(marioAIOptions.getLevelRandSeed(), 0);
    assertEquals(marioAIOptions.getLevelType(), 0);
    assertEquals(marioAIOptions.getFPS(), 24);
    assertEquals(marioAIOptions.getMarioMode(), 2);
    assertEquals(marioAIOptions.isPowerRestoration(), false);
    assertEquals(marioAIOptions.getReceptiveFieldHeight(), 19);
    assertEquals(marioAIOptions.getReceptiveFieldWidth(), 19);
    assertEquals(marioAIOptions.isReceptiveFieldVisualized(), false);
    assertEquals(marioAIOptions.getTimeLimit(), 200);
    assertEquals(marioAIOptions.isToolsConfigurator(), false);
    assertEquals(marioAIOptions.isViewAlwaysOnTop(), false);
    assertEquals(marioAIOptions.getViewLocation().x, 0);
    assertEquals(marioAIOptions.getViewLocation().y, 0);
    assertEquals(marioAIOptions.isVisualization(), true);
    assertEquals(marioAIOptions.getViewWidth(), 320);
    assertEquals(marioAIOptions.getViewHeight(), 240);
    assertEquals(marioAIOptions.getZLevelScene(), 1);
    assertEquals(marioAIOptions.getZLevelEnemies(), 0);
    assertEquals(marioAIOptions.getLevelHeight(), 15);
    assertEquals(marioAIOptions.getDeadEndsCount(), false);
    assertEquals(marioAIOptions.getCannonsCount(), true);
    assertEquals(marioAIOptions.getHillStraightCount(), true);
    assertEquals(marioAIOptions.getTubesCount(), true);
    assertEquals(marioAIOptions.getCoinsCount(), true);
    assertEquals(marioAIOptions.getBlocksCount(), true);
    assertEquals(marioAIOptions.getGapsCount(), true);
    assertEquals(marioAIOptions.getHiddenBlocksCount(), false);
    assertEquals(marioAIOptions.getEnemies(), "g,gw,gk,gkw,rk,rkw,s,sw");
    assertEquals(marioAIOptions.isFlatLevel(), false);
    assertEquals(marioAIOptions.getGreenMushroomMode(), 1);
//    TODO:TASK:[M] test all cases
}

@Test
public void testSetLevelEnemies()
{
    marioAIOptions.setArgs("-le 1111111111");
    // TODO:TASK:[M] test various conditions
}

@Test
public void testSetMarioInvulnerable() throws Exception
{
    marioAIOptions.setMarioInvulnerable(true);
    assertEquals(marioAIOptions.isMarioInvulnerable(), true);
    marioAIOptions.setArgs("-i off");
    assertEquals(marioAIOptions.isMarioInvulnerable(), false);
}

@Test
public void testDefaultAgent()
{
    assertNotNull(marioAIOptions.getAgent());
    assertEquals("ch.idsia.agents.controllers.human.HumanKeyboardAgent", marioAIOptions.getAgentFullLoadName());
    assertEquals("HumanKeyboardAgent", marioAIOptions.getAgent().getName());
}

@Test
public void testStop()
{
    this.marioAIOptions.setArgs("-stop on");
    assertEquals(true, this.marioAIOptions.isStopGamePlay());
    assertEquals(GlobalOptions.isGameplayStopped, this.marioAIOptions.isStopGamePlay());
}

@Test
public void testReset()
{
    marioAIOptions.setArgs("-echo on -rfw 21 -rfh 17");
    assertTrue(marioAIOptions.isEcho());
    assertEquals(21, marioAIOptions.getReceptiveFieldWidth());
    assertEquals(17, marioAIOptions.getReceptiveFieldHeight());

    marioAIOptions.reset();
    assertFalse(marioAIOptions.isEcho());
    assertEquals(19, marioAIOptions.getReceptiveFieldWidth());
    assertEquals(19, marioAIOptions.getReceptiveFieldHeight());
}

}
