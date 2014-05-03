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


public class Fireball extends Sprite
{
private static float GROUND_INERTIA = 0.89f;
private static float AIR_INERTIA = 0.89f;

private float runTime;
private boolean onGround = false;

private int width = 4;
int height = 24;

private LevelScene world;
public int facing;

public boolean avoidCliffs = false;
public int anim;

public boolean dead = false;
private int deadTime = 0;

public Fireball(LevelScene world, float x, float y, int facing)
{
    kind = KIND_FIREBALL;
    sheet = Art.particles;

    this.x = x;
    this.y = y;
    this.world = world;
    xPicO = 4;
    yPicO = 4;

    yPic = 3;
    height = 8;
    this.facing = facing;
    wPic = 8;
    hPic = 8;

    xPic = 4;
    ya = 4;
}

public void move()
{
    if (deadTime > 0)
    {
        for (int i = 0; i < 8; i++)
        {
            world.addSprite(new Sparkle((int) (x + Math.random() * 8 - 4) + 4, (int) (y + Math.random() * 8 - 4) + 2, (float) Math.random() * 2 - 1 - facing, (float) Math.random() * 2 - 1, 0, 1, 5));
        }
        spriteContext.removeSprite(this);

        return;
    }

    if (facing != 0) anim++;

    float sideWaysSpeed = 8f;
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

    world.checkFireballCollide(this);

    xFlipPic = facing == -1;

    runTime += (Math.abs(xa)) + 5;

    xPic = (anim) % 4;


    if (!move(xa, 0))
    {
        die();
    }

    onGround = false;
    move(0, ya);
    if (onGround) ya = -10;

    ya *= 0.95f;
    if (onGround)
    {
        xa *= GROUND_INERTIA;
    } else
    {
        xa *= AIR_INERTIA;
    }

    if (!onGround)
    {
        ya += 1.5;
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

public void die()
{
    dead = true;

    xa = -facing * 2;
    ya = -5;
    deadTime = 100;
}
}