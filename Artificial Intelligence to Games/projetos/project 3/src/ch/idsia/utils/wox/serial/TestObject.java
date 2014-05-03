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

package ch.idsia.utils.wox.serial;

import java.util.ArrayList;

/**
 * User: sml
 * Date: 05-Aug-2004
 * Time: 11:33:26
 */

public class TestObject
{

public static class Inner
{
    int inx;

    public Inner(int inx)
    {
        this.inx = inx;
    }
}

private int x;
int[] xa = {0, 1};
int[] xb = xa;
byte[] ba = {99, 12, (byte) 0xFF};
TestObject to;
int[][] xxx = {{0, 1}, {2}};
int[] ia = {1, 2, 3};
double[] dd = {4, 5, 6};
Object[] objects;
ArrayList alist;
// check we can handle null objects
Object myNull = null;
Inner inner;

// check we can handle Class variables
// Class myClass = TestObject.class;

public TestObject(int x)
{
    this.x = x;
    objects = new Object[]{this};
    alist = new ArrayList();
    alist.add(this);
    alist.add("Hello");
    alist.add(new Integer(23));
    // alist.set(12, "Twelve");
    inner = new Inner(99);
}

public synchronized int inc()
{
    return x++;
}

// no longer needs any default constructor...
// private TestObject() {}
}
