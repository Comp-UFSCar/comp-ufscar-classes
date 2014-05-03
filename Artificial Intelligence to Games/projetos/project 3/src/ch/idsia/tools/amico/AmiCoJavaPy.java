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

package ch.idsia.tools.amico;

/**
 * Created by IntelliJ IDEA.
 * User: Sergey Karakovskiy, sergey at idsia dot ch
 * Date: Feb 18, 2010
 * Time: 6:43:16 PM
 * Package: ch.idsia.tools.amico
 */
public class AmiCoJavaPy
{
public AmiCoJavaPy(String moduleName, String className)
{
    int res = this.initModule(moduleName, className);
    if (res == 0)
    {
        System.out.println("Java: Python module initialized successfully");
    } else
        throw new Error("Java: Python module initialization failed");
}

public native int initModule(String moduleName, String className);

public native String getName();

public native void integrateObservation(int[] squashedObservation, int[] squashedEnemies, float[] marioPos, float[] enemiesPos, int[] marioState);

public native int[] getAction();

public native void giveIntermediateReward(final float intermediateReward);

public native void reset();

public native void setObservationDetails(final int rfWidth, final int rfHeight, final int egoRow, final int egoCol);

static
{
    System.out.println("Java: loading AmiCo...");
    System.loadLibrary("AmiCoJavaPy");
    System.out.println("Java: AmiCo library has been successfully loaded!");
}

public native void finalizePythonEnvironment();
}
