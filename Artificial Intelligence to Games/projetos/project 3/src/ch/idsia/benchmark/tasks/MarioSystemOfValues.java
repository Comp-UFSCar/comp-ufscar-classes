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

package ch.idsia.benchmark.tasks;

/**
 * Created by IntelliJ IDEA.
 * User: Sergey Karakovskiy, sergey at idsia dot ch
 * Date: Mar 27, 2010 Time: 5:55:38 PM
 * Package: ch.idsia.scenarios.champ
 */

/**
 * tune the parameters of the multiobjective function with MarioSystemOfValues class
 * assigning a high value to a certain parameter should steer your agent to maximize
 * objective function w.r.t this value,
 * e.g.
 * assigning timeLeft = 0 and coins = 1000 should make your agent collect all coins before
 * advanching to finish. If win = 0 as well, this agent will not have motivation to win.
 * or
 * very high value of kills should produce a true `MARIONATOR`, making him a perfect killer.
 * By tuning killedByFire, killedByShell, killedByStomp you make the killer
 * stylish and of refined manners.
 */

public class MarioSystemOfValues extends SystemOfValues
{
final public int distance = 1;
final public int win = 1024;
final public int mode = 32;
final public int coins = 16;
final public int hiddenItems = 24;
final public int flowerFire = 64;  // not used for now
final public int kills = 42;
final public int killedByFire = 4;
final public int killedByShell = 17;
final public int killedByStomp = 12;
final public int timeLeft = 8;
final public int hiddenBlocks = 24;

public interface timeLengthMapping
{
    final public static int TIGHT = 10;
    final public static int MEDIUM = 20;
    final public static int FLEXIBLE = 30;
}
}
