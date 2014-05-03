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

import sun.reflect.ReflectionFactory;

import java.lang.reflect.Constructor;
import java.security.AccessController;

/**
 * Created by IntelliJ IDEA.
 * User: sml
 * Date: 07-Aug-2004
 * Time: 08:36:49
 * To change this template use File | Settings | File Templates.
 * This class contains static functions that are common to
 * both SimpleWriter and SimpleReader
 */
public class Util implements Serial
{

/**
 * reflection factory for forcing default constructors
 */
private static final ReflectionFactory reflFactory = (ReflectionFactory)
        AccessController.doPrivileged(
                new ReflectionFactory.GetReflectionFactoryAction());


public static void main(String[] args)
{
    String test = "Hello";
    System.out.println(stringable(test));
}

/**
 * Returns a no-arg constructor that
 * despite appearences can be used to construct objects
 * of the specified type!!!of first non-serializable
 */
public static Constructor forceDefaultConstructor(Class cl) throws Exception
{
    Constructor cons = Object.class.getDeclaredConstructor(new Class[0]);
    cons = reflFactory.newConstructorForSerialization(cl, cons);
    cons.setAccessible(true);
    // System.out.println("Cons: " + cons);
    return cons;
}

public static boolean stringable(Object o)
{
    // assume the following types go easily to strings...
    boolean val = (o instanceof Number) ||
            (o instanceof Boolean) ||
            (o instanceof Class) ||
            (o instanceof String);
    // System.out.println("Stringable: " + o + " : " + val + " : " + o.getClass());
    return val;
}

public static boolean stringable(Class type)
{
    // assume the following types go easily to strings...
    boolean val = (Number.class.isAssignableFrom(type)) ||
            (Boolean.class.isAssignableFrom(type)) ||
            (String.class.equals(type)) ||
            (Class.class.equals(type));
    // System.out.println("Stringable: " + type + " : " + val);
    return val;
}

public static boolean stringable(String name)
{
    // assume the following types go easily to strings...
    // System.out.println("Called (String) version");
    try
    {
        Class type = Class.forName(name);
        return stringable(type);
    } catch (Exception e)
    {
        return false;
    }
}

public static boolean primitive(Class type)
{
    for (int i = 0; i < primitives.length; i++)
    {
        if (primitives[i].equals(type))
        {
            return true;
        }
    }
    return false;
}

}
