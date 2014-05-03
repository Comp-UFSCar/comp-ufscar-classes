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

package ch.idsia.benchmark.mario.engine.level;

import java.util.Random;


public class BgLevelGenerator
{
private static Random levelSeedRandom = new Random();

public static Level createLevel(int width, int height, boolean distant, int type)
{
    BgLevelGenerator levelGenerator = new BgLevelGenerator(width, height, distant, type);
    return levelGenerator.createLevel(levelSeedRandom.nextLong());
}

private int width;
private int height;
private boolean distant;
private int type;

private BgLevelGenerator(int width, int height, boolean distant, int type)
{
    this.width = width;
    this.height = height;
    this.distant = distant;
    this.type = type;
}

private Level createLevel(long seed)
{
    Level level = new Level(width, height);
    Random random = new Random(seed);

    switch (type)
    {
        case LevelGenerator.TYPE_OVERGROUND:
        {

            int range = distant ? 4 : 6;
            int offs = distant ? 2 : 1;
            int oh = random.nextInt(range) + offs;
            int h = random.nextInt(range) + offs;
            for (int x = 0; x < width; x++)
            {
                oh = h;
                while (oh == h)
                {
                    h = random.nextInt(range) + offs;
                }
                for (int y = 0; y < height; y++)
                {
                    int h0 = (oh < h) ? oh : h;
                    int h1 = (oh < h) ? h : oh;
                    if (y < h0)
                    {
                        if (distant)
                        {
                            int s = 2;
                            if (y < 2) s = y;
                            level.setBlock(x, y, (byte) (4 + s * 8));
                        } else
                        {
                            level.setBlock(x, y, (byte) 5);
                        }
                    } else if (y == h0)
                    {
                        int s = h0 == h ? 0 : 1;
                        s += distant ? 2 : 0;
                        level.setBlock(x, y, (byte) s);
                    } else if (y == h1)
                    {
                        int s = h0 == h ? 0 : 1;
                        s += distant ? 2 : 0;
                        level.setBlock(x, y, (byte) (s + 16));
                    } else
                    {
                        int s = y > h1 ? 1 : 0;
                        if (h0 == oh) s = 1 - s;
                        s += distant ? 2 : 0;
                        level.setBlock(x, y, (byte) (s + 8));
                    }
                }
            }
            break;
        }
        case LevelGenerator.TYPE_UNDERGROUND:
        {
            if (distant)
            {
                int tt = 0;
                for (int x = 0; x < width; x++)
                {
                    if (random.nextDouble() < 0.75) tt = 1 - tt;
                    for (int y = 0; y < height; y++)
                    {
                        int t = tt;
                        int yy = y - 2;
                        if (yy < 0 || yy > 4)
                        {
                            yy = 2;
                            t = 0;
                        }
                        level.setBlock(x, y, (byte) (4 + t + (3 + yy) * 8));
                    }
                }
            } else
            {
                for (int x = 0; x < width; x++)
                {
                    for (int y = 0; y < height; y++)
                    {
                        int t = x % 2;
                        int yy = y - 1;
                        if (yy < 0 || yy > 7)
                        {
                            yy = 7;
                            t = 0;
                        }
                        if (t == 0 && yy > 1 && yy < 5)
                        {
                            t = -1;
                            yy = 0;
                        }
                        level.setBlock(x, y, (byte) (6 + t + (yy) * 8));
                    }
                }
            }
            break;
        }
        case LevelGenerator.TYPE_CASTLE:
        {
            if (distant)
            {
                for (int x = 0; x < width; x++)
                {
                    for (int y = 0; y < height; y++)
                    {
                        int t = x % 2;
                        int yy = y - 1;
                        if (yy > 2 && yy < 5)
                        {
                            yy = 2;
                        } else if (yy >= 5)
                        {
                            yy -= 2;
                        }
                        if (yy < 0)
                        {
                            t = 0;
                            yy = 5;
                        } else if (yy > 4)
                        {
                            t = 1;
                            yy = 5;
                        } else if (t < 1 && yy == 3)
                        {
                            t = 0;
                            yy = 3;
                        } else if (t < 1 && yy > 0 && yy < 3)
                        {
                            t = 0;
                            yy = 2;
                        }
                        level.setBlock(x, y, (byte) (1 + t + (yy + 4) * 8));
                    }
                }
            } else
            {
                for (int x = 0; x < width; x++)
                {
                    for (int y = 0; y < height; y++)
                    {
                        int t = x % 3;
                        int yy = y - 1;
                        if (yy > 2 && yy < 5)
                        {
                            yy = 2;
                        } else if (yy >= 5)
                        {
                            yy -= 2;
                        }
                        if (yy < 0)
                        {
                            t = 1;
                            yy = 5;
                        } else if (yy > 4)
                        {
                            t = 2;
                            yy = 5;
                        } else if (t < 2 && yy == 4)
                        {
                            t = 2;
                            yy = 4;
                        } else if (t < 2 && yy > 0 && yy < 4)
                        {
                            t = 4;
                            yy = -3;
                        }
                        level.setBlock(x, y, (byte) (1 + t + (yy + 3) * 8));
                    }
                }
            }
            break;
        }
    }
    return level;
}
}