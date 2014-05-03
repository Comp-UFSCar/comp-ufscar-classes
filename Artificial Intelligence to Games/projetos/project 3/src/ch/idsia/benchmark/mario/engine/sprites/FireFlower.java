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

import ch.idsia.benchmark.mario.engine.Art;
import ch.idsia.benchmark.mario.engine.LevelScene;


public class FireFlower extends Sprite
{
private int width = 4;
int height = 24;

private LevelScene world;
public int facing;

public boolean avoidCliffs = false;
private int life;

public FireFlower(LevelScene world, int x, int y)
{
    kind = KIND_FIRE_FLOWER;
    sheet = Art.items;

    this.x = x;
    this.y = y;
    this.world = world;
    xPicO = 8;
    yPicO = 15;

    xPic = 1;
    yPic = 0;
    height = 12;
    facing = 1;
    wPic = hPic = 16;
    life = 0;
}

public void collideCheck()
{
    float xMarioD = world.mario.x - x;
    float yMarioD = world.mario.y - y;
    float w = 16;
    if (xMarioD > -16 && xMarioD < 16)
    {
        if (yMarioD > -height && yMarioD < world.mario.height)
        {
            world.mario.devourFlower();
            spriteContext.removeSprite(this);
        }
    }
}

public void move()
{
    if (life < 9)
    {
        layer = 0;
        y--;
        life++;
        return;
    }
}
}