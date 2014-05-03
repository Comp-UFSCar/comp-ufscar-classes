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


public class Shell extends Sprite
{

//    private float runTime;
private boolean onGround = false;

private int width = 4;
int height = 24;

private LevelScene world;
public int facing;

public boolean avoidCliffs = false;
public int anim;

private float yaa = 1;

public boolean dead = false;
private int deadTime = 0;
public boolean carried;


public Shell(LevelScene world, float x, float y, int type)
{
    kind = KIND_SHELL;
    sheet = Art.enemies;

    this.x = x;
    this.y = y;
    this.world = world;
    xPicO = 8;
    yPicO = 31;

    yPic = type;
    height = 12;
    facing = 0;
    wPic = 16;

    xPic = 4;
    ya = -5;

    yaa = creaturesGravity * 2;
}

public boolean fireballCollideCheck(Fireball fireball)
{
    if (deadTime != 0) return false;

    float xD = fireball.x - x;
    float yD = fireball.y - y;

    if (xD > -16 && xD < 16)
    {
        if (yD > -height && yD < fireball.height)
        {
            if (facing != 0) return true;

            xa = fireball.facing * 2;
            ya = -5;
            if (spriteTemplate != null) spriteTemplate.isDead = true;
            deadTime = 100;
            hPic = -hPic;
            yPicO = -yPicO + 16;
            return true;
        }
    }
    return false;
}

public void collideCheck()
{
    if (carried || dead || deadTime > 0) return;

    float xMarioD = world.mario.x - x;
    float yMarioD = world.mario.y - y;
    float w = 16;
    if (xMarioD > -w && xMarioD < w)
    {
        if (yMarioD > -height && yMarioD < world.mario.height)
        {
            if (world.mario.ya > 0 && yMarioD <= 0 && (!world.mario.onGround || !world.mario.wasOnGround))
            {
                world.mario.stomp(this);
                if (facing != 0)
                {
                    xa = 0;
                    facing = 0;
                } else
                {
                    facing = world.mario.facing;
                }
            } else
            {
                if (facing != 0)
                {
                    world.mario.getHurt(this.kind);
                } else
                {
                    world.mario.kick(this);
                    facing = world.mario.facing;
                }
            }
        }
    }
}

public void move()
{
    if (carried)
    {
        world.checkShellCollide(this);
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

    if (facing != 0) anim++;

    float sideWaysSpeed = 11f;
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

    if (facing != 0)
    {
        world.checkShellCollide(this);
    }

    xFlipPic = facing == -1;

//        runTime += (Math.abs(xa)) + 5;

    xPic = (anim / 2) % 4 + 3;


    if (!move(xa, 0))
    {
        facing = -facing;
    }
    onGround = false;
    move(0, ya);

    ya *= 0.85f;
    if (onGround)
    {
        xa *= Sprite.GROUND_INERTIA;
    } else
    {
        xa *= Sprite.AIR_INERTIA;
    }

    if (!onGround)
    {
        ya += yaa;
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

//        byte block = levelScene.level.getBlock(x, y);

    if (blocking && ya == 0 && xa != 0)
    {
        world.bump(x, y, true);
    }

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

public void die()
{
    dead = true;

    carried = false;

    xa = -facing * 2;
    ya = -5;
    deadTime = 100;
}

public boolean shellCollideCheck(Shell shell)
{
    if (deadTime != 0) return false;

    float xD = shell.x - x;
    float yD = shell.y - y;

    if (xD > -16 && xD < 16)
    {
        if (yD > -height && yD < shell.height)
        {
            if (world.mario.carried == shell || world.mario.carried == this)
            {
                world.mario.carried = null;
                world.mario.setRacoon(false);
            }

            die();
            shell.die();
            return true;
        }
    }
    return false;
}


public void release(Mario mario)
{
    carried = false;
    facing = mario.facing;
    x += facing * 8;
}
}