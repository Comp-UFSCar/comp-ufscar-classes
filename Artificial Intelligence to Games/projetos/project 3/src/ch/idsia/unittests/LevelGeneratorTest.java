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

import ch.idsia.benchmark.mario.engine.level.Level;
import ch.idsia.benchmark.mario.engine.level.LevelGenerator;
import ch.idsia.benchmark.mario.engine.level.SpriteTemplate;
import ch.idsia.benchmark.mario.engine.sprites.Sprite;
import ch.idsia.benchmark.tasks.BasicTask;
import ch.idsia.tools.MarioAIOptions;
import ch.idsia.tools.RandomCreatureGenerator;
import junit.framework.TestCase;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * Created by IntelliJ IDEA.
 * User: Sergey Karakovskiy, sergey.karakovskiy@gmail.com
 * Date: Aug 28, 2010
 * Time: 10:30:34 PM
 * Package: ch.idsia.unittests
 */
public class LevelGeneratorTest extends TestCase
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
public void testRegressionLBBug() throws Exception
{
    // This test has to be first because it only happens if blocks have
    // never been on before.
    final MarioAIOptions marioAIOptions = new MarioAIOptions();
    marioAIOptions.setArgs("-lb off");
    Level level1 = LevelGenerator.createLevel(marioAIOptions);
    marioAIOptions.setArgs("-vis off -ag ch.idsia.agents.controllers.ForwardJumpingAgent -lb on");
    final BasicTask basicTask = new BasicTask(marioAIOptions);
    basicTask.setOptionsAndReset(marioAIOptions);
    basicTask.runSingleEpisode(1);
    marioAIOptions.setArgs("-lb off");
    Level level2 = LevelGenerator.createLevel(marioAIOptions);


    for (int i = 0; i < level1.length; i++)
        for (int j = 0; j < level1.height; j++)
            assertEquals(level1.getBlock(i, j), level2.getBlock(i, j));
}

@Test
public void testCreateLevel() throws Exception
{
    final MarioAIOptions marioAIOptions = new MarioAIOptions();
    Level level1 = LevelGenerator.createLevel(marioAIOptions);
    Level level2 = LevelGenerator.createLevel(marioAIOptions);

    for (int i = 0; i < level1.length; i++)
        for (int j = 0; j < level1.height; j++)
            assertEquals(level1.getBlock(i, j), level2.getBlock(i, j));
}

@Test
public void testSpriteTemplates() throws Exception
{
    final MarioAIOptions marioAIOptions = new MarioAIOptions();
    Level level1 = LevelGenerator.createLevel(marioAIOptions);
    Level level2 = LevelGenerator.createLevel(marioAIOptions);


    for (int i = 0; i < level1.length; i++)
        for (int j = 0; j < level1.height; j++)
        {
            int t1 = 0;
            int t2 = 0;
            SpriteTemplate st1 = level1.getSpriteTemplate(i, j);
            SpriteTemplate st2 = level2.getSpriteTemplate(i, j);

            if (st1 != null)
                t1 = st1.getType();

            if (st2 != null)
            {
                t2 = st2.getType();
            } else if (st1 != null)
            {
                throw new AssertionError("st1 is not null, st2 is null!");
            }

            assertEquals(t1, t2);
        }
}

@Test
public void testCreateLevelWithoutCreatures() throws Exception
{
    final MarioAIOptions marioAIOptions = new MarioAIOptions("-le off");
    Level level = LevelGenerator.createLevel(marioAIOptions);

    for (int i = 0; i < level.length; i++)
        for (int j = 0; j < level.height; j++)
        {
            SpriteTemplate st = level.getSpriteTemplate(i, j);

            if (st != null && st.getType() == Sprite.KIND_PRINCESS)
                continue;

            assertNull(st);
        }
}

//    @Test
//    public void testCreateLevelWithTubesWithoutFlowers()
//    {
//        final MarioAIOptions marioAIOptions = new MarioAIOptions("-ltb on -le g,gw,gk,gkw,rk,rkw,s,sw");
//        Level level = LevelGenerator.createLevel(marioAIOptions);
//
//        assertEquals(true, level.counters.tubesCount > 0);
//        assertEquals(true, level.counters.totalTubes == Integer.MAX_VALUE);
//    }

//    @Test
//    public void testCreateLevelWithTubesWithFlowers()
//    {
//        final MarioAIOptions marioAIOptions = new MarioAIOptions("-ltb on -le f -ld 5 -ls 222");
//        Level level = LevelGenerator.createLevel(marioAIOptions);
//
//        boolean fl = false;
//
//        for (int i = 0; i < level.length; i++)
//            for (int j = 0; j < level.height; j++)
//                if ((level.getSpriteTemplate (i, j) != null) && (level.getSpriteTemplate (i, j).getType() == Sprite.KIND_ENEMY_FLOWER))
//                {
//                    fl = true;
//                    break;
//                }
//
//        assertEquals(true, fl);
//    }

@Test
public void testRandomCreatureGenerator_RedKoopaWinged()
{
    RandomCreatureGenerator g = new RandomCreatureGenerator(0, "rkw", 0);
    assertEquals(Sprite.KIND_RED_KOOPA_WINGED, g.nextCreature());
}

@Test
public void testRandomCreatureGenerator_GreenKoopaWinged()
{
    RandomCreatureGenerator g = new RandomCreatureGenerator(0, "gkw", 0);
    assertEquals(Sprite.KIND_GREEN_KOOPA_WINGED, g.nextCreature());
}

@Test
public void testRandomCreatureGenerator_Goomba()
{
    RandomCreatureGenerator g = new RandomCreatureGenerator(0, "g", 0);
    assertEquals(Sprite.KIND_GOOMBA, g.nextCreature());
}

@Test
public void testRandomCreatureGenerator_10Goombas()
{
    final MarioAIOptions marioAIOptions = new MarioAIOptions("-le g:10");
    Level level = LevelGenerator.createLevel(marioAIOptions);

    int counter = 0;

    for (int i = 0; i < level.length; i++)
        for (int j = 0; j < level.height; j++)
        {
            SpriteTemplate st1 = level.getSpriteTemplate(i, j);

            if (st1 != null && st1.getType() != Sprite.KIND_PRINCESS)
            {
                int type = st1.getType();
                assertEquals(Sprite.KIND_GOOMBA, type);

                ++counter;
            }
        }

    System.out.println("level.counters.creatures = " + Level.counters.creatures);

    assertEquals(10, counter);
}

@Test
public void testRandomCreatureGenerator_20RedWingedKoopas()
{
    final MarioAIOptions marioAIOptions = new MarioAIOptions("-le rkw:20");
    Level level = LevelGenerator.createLevel(marioAIOptions);

    int counter = 0;

    for (int i = 0; i < level.length; i++)
        for (int j = 0; j < level.height; j++)
        {
            SpriteTemplate st1 = level.getSpriteTemplate(i, j);

            if (st1 != null && st1.getType() != Sprite.KIND_PRINCESS)
            {
                int type = st1.getType();
                assertEquals(Sprite.KIND_RED_KOOPA_WINGED, type);

                ++counter;
            }
        }

    System.out.println("level.counters.creatures = " + Level.counters.creatures);

    assertEquals(20, counter);
}
}
