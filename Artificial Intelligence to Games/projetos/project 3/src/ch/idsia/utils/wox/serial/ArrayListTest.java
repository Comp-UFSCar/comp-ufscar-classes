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

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: sml
 * Date: 08-Aug-2004
 * Time: 23:22:56
 * To change this template use File | Settings | File Templates.
 */
public class ArrayListTest
{
public static void main(String[] args) throws Exception
{
    ArrayList al = new ArrayList();
    al.add("Hello");
    al.add("Hello");
    al.add("Hello");

    Field[] fields = SimpleWriter.getFields(ArrayList.class);
    for (int i = 0; i < fields.length; i++)
    {
        System.out.println(i + " : " + fields[i]);
        try
        {
            fields[i].setAccessible(true);
            Object val = fields[i].get(al);
            fields[i].set(al, val);
            System.out.println("Set val: " + val);

        } catch (Exception e)
        {
            System.out.println(e);
            // e.printStackTrace();
        }
    }
    Field field = java.util.AbstractList.class.getDeclaredField("modCount");
    field.setAccessible(true);
    System.out.println("Field: " + field);
    // field.set(al, new Integer(3));
    field = ArrayList.class.getField("size");
    field.setAccessible(true);
    System.out.println("Field: " + field);
    field.set(al, new Integer(10));
}
}
