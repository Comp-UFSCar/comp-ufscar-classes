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

package ch.idsia.benchmark.mario.engine.sprites;

import ch.idsia.benchmark.mario.engine.GlobalOptions;
import ch.idsia.benchmark.mario.engine.LevelScene;

public class FlowerEnemy extends Enemy
{
private int tick;
private int yStart;
private int jumpTime = 0;
private LevelScene world;

public FlowerEnemy(LevelScene world, int x, int y, int mapX, int mapY)
{
    super(world, x, y, 1, Sprite.KIND_ENEMY_FLOWER, false, mapX, mapY);
    noFireballDeath = false;
    this.world = world;
    this.xPic = 0;
    this.yPic = 6;
    this.yPicO = 24;
    this.height = 12;
    this.width = 2;

    yStart = y;
    ya = -8;

    this.y -= 1;

    this.layer = 0;

    for (int i = 0; i < 4; i++)
    {
        move();
    }
}

public void move()
{
    //TODO:|L| check this
    if (GlobalOptions.areFrozenCreatures == true)
    {
        return;
    }
    if (deadTime > 0)
    {
        deadTime--;

        if (deadTime == 0)
        {
            deadTime = 1;
            for (int i = 0; i < 8; i++)
            {
                world.addSprite(new Sparkle((int) (x + Math.random() * 16 - 8) + 4, (int) (y - Math.random() * 8) + 4, (float) (Math.random() * 2 - 1), (float) Math.random() * -1, 0, 1, 5));
            }
            spriteContext.removeSprite(this);
        }

        x += xa;
        y += ya;
        ya *= 0.95;
        ya += 1;

        return;
    }

    tick++;

    if (y >= yStart)
    {
        y = yStart;

        int xd = (int) (Math.abs(world.mario.x - x));
        jumpTime++;
        if (jumpTime > 40 && xd > 24)
        {
            ya = -8;
        } else
        {
            ya = 0;
        }
    } else
    {
        jumpTime = 0;
    }

    y += ya;
    ya *= 0.9;
    ya += 0.1f;

    xPic = ((tick / 2) & 1) * 2 + ((tick / 6) & 1);
}
/*    public void render(Graphics og, float alpha)
    {
        if (!visible) return;
        
        int xPixel = (int)(xOld+(x-xOld)*alpha)-xPicO;
        int yPixel = (int)(yOld+(y-yOld)*alpha)-yPicO;

        int a = ((tickCount/3)&1)*2;
//        a += ((tickCount/8)&1);
        og.drawImage(sheet[a*2+0][6], xPixel-8, yPixel+8, 16, 32, null);
        og.drawImage(sheet[a*2+1][6], xPixel+8, yPixel+8, 16, 32, null);
    }*/
}