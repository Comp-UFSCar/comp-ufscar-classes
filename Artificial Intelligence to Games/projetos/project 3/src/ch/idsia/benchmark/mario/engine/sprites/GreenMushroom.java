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

public class GreenMushroom extends Sprite 
{
private static float GROUND_INERTIA = 0.89f;
private static float AIR_INERTIA = 0.89f;

private float runTime;
private boolean onGround = false;
private boolean mayJump = false;
private int jumpTime = 0;
private float xJumpSpeed;
private float yJumpSpeed;

private int width = 4;
int height = 24;

private LevelScene world;
public int facing;

public boolean avoidCliffs = false;
private int life;

public GreenMushroom(LevelScene world, int x, int y)
{
    kind = KIND_GREEN_MUSHROOM;
    sheet = Art.items;

    this.x = x;
    this.y = y;
    this.world = world;
    xPicO = 8;
    yPicO = 15;

    yPic = 0;
    xPic = 2;
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
            world.mario.devourGreenMushroom(world.getGreenMushroomMode());
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
    float sideWaysSpeed = 1.75f;
    layer = 1;
    //        float sideWaysSpeed = onGround ? 2.5f : 1.2f;

    if (xa > 2)
    {
        facing = 1;
    }
    if (xa < -2)
    {
        facing = -1;
    }

    xa = facing * sideWaysSpeed;

    mayJump = (onGround);

    xFlipPic = facing == -1;

    runTime += (Math.abs(xa)) + 5;


    if (!move(xa, 0)) facing = -facing;
    onGround = false;
    move(0, ya);

    ya *= 0.85f;
    if (onGround)
    {
        xa *= GROUND_INERTIA;
    } else
    {
        xa *= AIR_INERTIA;
    }

    if (!onGround)
    {
        ya += 2;
    }
}

private boolean move(float xa, float ya)
{
    while (xa > 8)
    {
        if (!move(8, 0)) return false;
        xa -= 8;
    }
    while (xa < -8)
    {
        if (!move(-8, 0)) return false;
        xa += 8;
    }
    while (ya > 8)
    {
        if (!move(0, 8)) return false;
        ya -= 8;
    }
    while (ya < -8)
    {
        if (!move(0, -8)) return false;
        ya += 8;
    }

    boolean collide = false;
    if (ya > 0)
    {
        if (isBlocking(x + xa - width, y + ya, xa, 0)) collide = true;
        else if (isBlocking(x + xa + width, y + ya, xa, 0)) collide = true;
        else if (isBlocking(x + xa - width, y + ya + 1, xa, ya)) collide = true;
        else if (isBlocking(x + xa + width, y + ya + 1, xa, ya)) collide = true;
    }
    if (ya < 0)
    {
        if (isBlocking(x + xa, y + ya - height, xa, ya)) collide = true;
        else if (collide || isBlocking(x + xa - width, y + ya - height, xa, ya)) collide = true;
        else if (collide || isBlocking(x + xa + width, y + ya - height, xa, ya)) collide = true;
    }
    if (xa > 0)
    {
        if (isBlocking(x + xa + width, y + ya - height, xa, ya)) collide = true;
        if (isBlocking(x + xa + width, y + ya - height / 2, xa, ya)) collide = true;
        if (isBlocking(x + xa + width, y + ya, xa, ya)) collide = true;

        if (avoidCliffs && onGround && !world.level.isBlocking((int) ((x + xa + width) / 16), (int) ((y) / 16 + 1), xa, 1))
            collide = true;
    }
    if (xa < 0)
    {
        if (isBlocking(x + xa - width, y + ya - height, xa, ya)) collide = true;
        if (isBlocking(x + xa - width, y + ya - height / 2, xa, ya)) collide = true;
        if (isBlocking(x + xa - width, y + ya, xa, ya)) collide = true;

        if (avoidCliffs && onGround && !world.level.isBlocking((int) ((x + xa - width) / 16), (int) ((y) / 16 + 1), xa, 1))
            collide = true;
    }

    if (collide)
    {
        if (xa < 0)
        {
            x = (int) ((x - width) / 16) * 16 + width;
            this.xa = 0;
        }
        if (xa > 0)
        {
            x = (int) ((x + width) / 16 + 1) * 16 - width - 1;
            this.xa = 0;
        }
        if (ya < 0)
        {
            y = (int) ((y - height) / 16) * 16 + height;
            jumpTime = 0;
            this.ya = 0;
        }
        if (ya > 0)
        {
            y = (int) (y / 16 + 1) * 16 - 1;
            onGround = true;
        }
        return false;
    } else
    {
        x += xa;
        y += ya;
        return true;
    }
}

private boolean isBlocking(float _x, float _y, float xa, float ya)
{
    int x = (int) (_x / 16);
    int y = (int) (_y / 16);
    if (x == (int) (this.x / 16) && y == (int) (this.y / 16)) return false;

    boolean blocking = world.level.isBlocking(x, y, xa, ya);

    byte block = world.level.getBlock(x, y);

    return blocking;
}

public void bumpCheck(int xTile, int yTile)
{
    if (x + width > xTile * 16 && x - width < xTile * 16 + 16 && yTile == (int) ((y - 1) / 16))
    {
        facing = -world.mario.facing;
        ya = -10;
    }
}
}
