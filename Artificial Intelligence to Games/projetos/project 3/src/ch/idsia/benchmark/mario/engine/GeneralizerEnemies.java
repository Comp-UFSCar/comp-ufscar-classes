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

package ch.idsia.benchmark.mario.engine;

import ch.idsia.benchmark.mario.engine.sprites.Sprite;

/**
 * Created by IntelliJ IDEA.
 * User: Sergey Karakovskiy, sergey@idsia.ch
 * Date: Aug 5, 2009
 * Time: 7:04:19 PM
 * Package: ch.idsia.benchmark.mario.engine
 */

public class GeneralizerEnemies
{

public static byte ZLevelGeneralization(byte el, int ZLevel)
{
    switch (ZLevel)
    {
        case (0):
            switch (el)
            {
                // cancel irrelevant sprite codes
                case (Sprite.KIND_COIN_ANIM):
                case (Sprite.KIND_PARTICLE):
                case (Sprite.KIND_SPARCLE):
                case (Sprite.KIND_MARIO):
                    return Sprite.KIND_NONE;
            }
            return el;   // all the rest should go as is
        case (1):
            switch (el)
            {
                case (Sprite.KIND_COIN_ANIM):
                case (Sprite.KIND_PARTICLE):
                case (Sprite.KIND_SPARCLE):
                case (Sprite.KIND_MARIO):
                    return Sprite.KIND_NONE;
                case (Sprite.KIND_FIRE_FLOWER):
                    return Sprite.KIND_FIRE_FLOWER;
                case (Sprite.KIND_MUSHROOM):
                    return Sprite.KIND_MUSHROOM;
                case (Sprite.KIND_FIREBALL):
                    return Sprite.KIND_FIREBALL;
                case (Sprite.KIND_BULLET_BILL):
                case (Sprite.KIND_GOOMBA):
                case (Sprite.KIND_GOOMBA_WINGED):
                case (Sprite.KIND_GREEN_KOOPA):
                case (Sprite.KIND_GREEN_KOOPA_WINGED):
                case (Sprite.KIND_RED_KOOPA):
                case (Sprite.KIND_RED_KOOPA_WINGED):
                case (Sprite.KIND_SHELL):
                case (Sprite.KIND_WAVE_GOOMBA):
                    return Sprite.KIND_GOOMBA;
                case (Sprite.KIND_SPIKY):
                case (Sprite.KIND_ENEMY_FLOWER):
                case (Sprite.KIND_SPIKY_WINGED):
                    return Sprite.KIND_SPIKY;
                /*case (Sprite.KIND_PRINCESS):
                    return Sprite.KIND_PRINCESS;*/
            }
            System.err.println("Z1 UNKOWN el = " + el);
            return el;
        case (2):
            switch (el)
            {
                case (Sprite.KIND_COIN_ANIM):
                case (Sprite.KIND_PARTICLE):
                case (Sprite.KIND_SPARCLE):
                case (Sprite.KIND_FIREBALL):
                case (Sprite.KIND_MARIO):
                case (Sprite.KIND_FIRE_FLOWER):
                case (Sprite.KIND_MUSHROOM):
                    return Sprite.KIND_NONE;
                case (Sprite.KIND_BULLET_BILL):
                case (Sprite.KIND_GOOMBA):
                case (Sprite.KIND_GOOMBA_WINGED):
                case (Sprite.KIND_GREEN_KOOPA):
                case (Sprite.KIND_GREEN_KOOPA_WINGED):
                case (Sprite.KIND_RED_KOOPA):
                case (Sprite.KIND_RED_KOOPA_WINGED):
                case (Sprite.KIND_SHELL):
                case (Sprite.KIND_SPIKY):
                case (Sprite.KIND_ENEMY_FLOWER):
                case (Sprite.KIND_SPIKY_WINGED):
                case (Sprite.KIND_WAVE_GOOMBA):
                    return 1;
                /*case (Sprite.KIND_PRINCESS):
                    return Sprite.KIND_PRINCESS;*/
            }
            System.err.println("ERROR: Z2 UNKNOWNN el = " + el);
            return 1;
    }
    return el; //TODO: Throw unknown ZLevel exception
}
}
