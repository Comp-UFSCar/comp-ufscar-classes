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


public class CoinAnim extends Sprite
{
private int life = 16;

public CoinAnim(int xTile, int yTile)
{
    kind = KIND_COIN_ANIM;
    sheet = Art.level;
    wPic = hPic = 16;

    x = xTile * 16;
    y = yTile * 16 - 16;
    xa = 0;
    ya = -6f;
    xPic = 0;
    yPic = 2;
}

public void move()
{
    if (life-- < 0)
    {
        Sprite.spriteContext.removeSprite(this);
        for (int xx = 0; xx < 2; xx++)
            for (int yy = 0; yy < 2; yy++)
                Sprite.spriteContext.addSprite(new Sparkle((int) x + xx * 8 + (int) (Math.random() * 8), (int) y + yy * 8 + (int) (Math.random() * 8), 0, 0, 0, 2, 5));
    }

    xPic = life & 3;

    x += xa;
    y += ya;
    ya += 1;
}
}