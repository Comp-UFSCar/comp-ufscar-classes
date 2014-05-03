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

/**
 * Created by IntelliJ IDEA.
 * User: Sergey Karakovskiy, sergey@idsia.ch
 * Date: Aug 5, 2009
 * Time: 7:05:46 PM
 * Package: ch.idsia.benchmark.mario.engine
 */

public class GeneralizerLevelScene
{
public static final int CANNON_MUZZLE = -82;
public static final int CANNON_TRUNK = -80;
public static final int COIN_ANIM = 2;
public static final int BREAKABLE_BRICK = -20;
public static final int UNBREAKABLE_BRICK = -22; //a rock with animated question mark
public static final int BRICK = -24;           //a rock with animated question mark
public static final int FLOWER_POT = -90;
public static final int BORDER_CANNOT_PASS_THROUGH = -60;
public static final int BORDER_HILL = -62;
public static final int FLOWER_POT_OR_CANNON = -85;
public static final int LADDER = 61;
public static final int TOP_OF_LADDER = 61;
public static final int PRINCESS = 5;

public static byte ZLevelGeneralization(byte el, int ZLevel)
{
    if (el == 0)
        return 0;
    switch (ZLevel)
    {
        case (0):
            switch (el)
            {
                case 16:  // brick, simple, without any surprise.
                case 17:  // brick with a hidden coin
                case 18:  // brick with a hidden friendly flower
                    return BREAKABLE_BRICK;
                case 21:       // question brick, contains coin
                case 22:       // question brick, contains flower/mushroom
                case 23:       // question brick, N coins inside. prevents cheating
                    return UNBREAKABLE_BRICK; // question brick, contains something
                case 34:
                    return COIN_ANIM;
                case 4:
                case -111:
                case -127:
                    return BORDER_CANNOT_PASS_THROUGH;
                case 14:
                    return CANNON_MUZZLE;
                case 30:
                case 46:
                    return CANNON_TRUNK;
                case 10:
                case 11:
                case 26:
                case 27:
                    return FLOWER_POT;
                case 1:
                    return 0; //hidden block
                case -124:
                case -123:
                case -122:
                case -74:
                    return BORDER_HILL;
                case -108:
                case -107:
                case -106:
                    return 0; //background of the hill. empty space
                case (61):
                    return LADDER;
                case (93):
                    return TOP_OF_LADDER;
                case (-1):
                    return PRINCESS;
            }
            return el;
        case (1):
            switch (el)
            {
                case 16:  // brick, simple, without any surprise.
                case 17:  // brick with a hidden coin
                case 18:  // brick with a hidden flower
                case 21:       // question brick, contains coin
                case 22:       // question brick, contains flower/mushroom
                case 23:       // question brick, N coins inside. prevents cheating
                    return BRICK; // any brick
                case 1:   // hidden block
                case (-108):
                case (-107):
                case (-106):
                case (15): // Sparcle, irrelevant
                    return 0;
                case (34):
                    return COIN_ANIM;
                case (-128):
                case (-127):
                case (-126):
                case (-125):
                case (-120):
                case (-119):
                case (-118):
                case (-117):
                case (-116):
                case (-115):
                case (-114):
                case (-113):
                case (-112):
                case (-110):
                case (-109):
                case (-104):
                case (-103):
                case (-102):
                case (-101):
                case (-100):
                case (-99):
                case (-98):
                case (-97):
                case (-96):
                case (-95):
                case (-94):
                case (-93):
                case (-69):
                case (-65):
                case (-88):
                case (-87):
                case (-86):
                case (-85):
                case (-84):
                case (-83):
                case (-82):
                case (-81):
                case (-77):
                case (-111):
                case (4):  // kicked hidden brick
                case (9):
                    return BORDER_CANNOT_PASS_THROUGH;   // border, cannot pass through, can stand on
//                    case(9):
//                        return -12; // hard formation border. Pay attention!
                case (-124):
                case (-123):
                case (-122):
                case (-76):
                case (-74):
                    return BORDER_HILL; // half-border, can jump through from bottom and can stand on
                case (10):
                case (11):
                case (26):
                case (27): //flower pot
                case (14):
                case (30):
                case (46): // canon
                    return FLOWER_POT_OR_CANNON;  // angry flower pot or cannon
                case (61):
                    return LADDER;
                case (93):
                    return TOP_OF_LADDER;
                case (-1):
                    return PRINCESS;
            }
            System.err.println("ZLevelMapElementGeneralization: Unknown value el = " + el + " Possible Level tiles bug; " +
                    "Please, inform sergey@idsia.ch or julian@togelius.com. Thanks!");
            return el;
        case (2):
            switch (el)
            {
                //cancel out half-borders, that could be passed through
                case (0):
                case (-108):
                case (-107):
                case (-106):
                case 1:   //hidden block
                case (15): // Sparcle, irrelevant
                    return 0;
                case (34): // coins
                    return COIN_ANIM;
                case 16:  // brick, simple, without any surprise.
                case 17:  // brick with a hidden coin
                case 18:  // brick with a hidden flower
                case 21:       // question brick, contains coin
                case 22:       // question brick, contains flower/mushroom
                    //here bricks are any objects cannot jump through and can stand on
                case 4: //kicked hidden block
                case 9:
                case (10):
                case (11):
                case (26):
                case (27): //flower pot
                case (14):
                case (30):
                case (46): // canon
                    return BORDER_CANNOT_PASS_THROUGH;
                case (-1):
                    return PRINCESS;
            }
            return 1;  // everything else is "something", so it is 1
    }
    System.err.println("Unkown ZLevel Z" + ZLevel);
    return el; //TODO: Throw unknown ZLevel exception
}
}
