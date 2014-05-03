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

import ch.idsia.benchmark.mario.engine.LevelScene;
import ch.idsia.tools.MarioAIOptions;
import junit.framework.TestCase;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * Created by IntelliJ IDEA.
 * User: Sergey Karakovskiy, sergey.karakovskiy@gmail.com
 * Date: Aug 28, 2010
 * Time: 8:31:52 PM
 * Package: ch.idsia.unittests
 */
public class LevelSceneTest extends TestCase
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
public void testSetArgs() throws Exception
{
}

@Test
public void testGetSerializedLevelSceneObservationZ_1x3() throws Exception
{
    MarioAIOptions cmd = new MarioAIOptions("-rfw 1 -rfh 3");
    LevelScene levelScene = new LevelScene();
    levelScene.reset(cmd);
//    byte[][] o = levelScene.getLevelSceneObservationZ(1);
//    int[] obs = levelScene.getSerializedLevelSceneObservationZ(1);
//    assertEquals(obs[2], o[2][0]);
}

@Test
public void testGetSerializedLevelSceneObservationZ() throws Exception
{
//    MarioAIOptions cmd = new MarioAIOptions();
//    LevelScene levelScene = new LevelScene();
//    levelScene.reset(cmd);
//    byte[][] o = levelScene.getLevelSceneObservationZ(1);
//    System.out.println("o = " + Arrays.deepToString(o));
//    for (int i = 0; i < o.length; i++)
//    {
//        byte[] bytes = o[i];
//        for (int j = 0; j < bytes.length; j++)
//        {
//            byte aByte = bytes[j];
//            if (aByte != 0)
//            {
//
//                System.out.println("\ni = " + i);
//                System.out.println("j = " + j);
//            }
//
//        }
//    }
//    int[] obs = levelScene.getSerializedLevelSceneObservationZ(1);
//    assertEquals(obs[14 * 19 + 4], o[14][4]);
//    assertEquals(obs[14 * 19 + 4], 0);
}


@Test
public void testGetSerializedLevelSceneObservationZ_3x1() throws Exception
{
    MarioAIOptions cmd = new MarioAIOptions("-rfw 3 -rfh 1");
    LevelScene levelScene = new LevelScene();
    levelScene.reset(cmd);
//    int[] obs = levelScene.getSerializedLevelSceneObservationZ(1);
}

@Test
public void testGetSerializedLevelSceneObservationZ_1x1() throws Exception
{
    MarioAIOptions cmd = new MarioAIOptions("-rfw 1 -rfh 1");
    LevelScene levelScene = new LevelScene();
    levelScene.reset(cmd);
//    int[] obs = levelScene.getSerializedLevelSceneObservationZ(1);
}

@Test
public void testGetSerializedLevelSceneObservationZ_3x3() throws Exception
{
    MarioAIOptions cmd = new MarioAIOptions("-rfw 3 -rfh 5");
    LevelScene levelScene = new LevelScene();
    levelScene.reset(cmd);
//    int[] obs = levelScene.getSerializedLevelSceneObservationZ(1);
}
}
