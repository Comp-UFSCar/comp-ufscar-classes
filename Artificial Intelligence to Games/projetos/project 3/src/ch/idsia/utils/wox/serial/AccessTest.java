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

import java.io.ObjectStreamClass;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.security.AccessController;


/**
 * Created by IntelliJ IDEA.
 * User: sml
 * Date: 06-Aug-2004
 * Time: 13:11:21
 * To change this template use Options | File Templates.
 */
public class AccessTest
{
public static void main(String[] args) throws Exception
{

    // getConstructor(Sub.class);

    // now how the hell is the assignment done????
    Constructor subCons = forceDefaultConstructor(Sub.class);

    Sub ob = (Sub) subCons.newInstance(new Object[]{});

    System.out.println("Sub: " + ob);

}


/**
 * reflection factory for forcing default constructors
 */
private static final ReflectionFactory reflFactory = (ReflectionFactory)
        AccessController.doPrivileged(
                new ReflectionFactory.GetReflectionFactoryAction());


public static class Sub extends Super
{
    int y;

    public Sub(double z)
    {
    }

    public String toString()
    {
        return x + " : " + y;
    }
}

public static class Super
{
    int x;

    public Super()
    {
        x = 100;
    }

    // public Super(String dummy) {}
}


private static Constructor getConstructor(Class cl) throws Exception
{
    Method getCons = ObjectStreamClass.class.getDeclaredMethod(
            "getSerializableConstructor", new Class[]{Class.class});
    getCons.setAccessible(true);
    Constructor cons = (Constructor) getCons.invoke(null, (Object[]) new Class[]{cl});
    System.out.println("Returning: " + cons);
    return cons;
}


/**
 * Returns subclass-accessible no-arg constructor of first non-serializable
 * superclass, or null if none found.  Access checks are disabled on the
 * returned constructor (if any).
 */
private static Constructor forceDefaultConstructor(Class cl) throws Exception
{
    Constructor cons = Object.class.getDeclaredConstructor(new Class[0]);
    cons = reflFactory.newConstructorForSerialization(cl, cons);
    cons.setAccessible(true);
    System.out.println("Cons: " + cons);
    return cons;
}

private static Constructor getSerializableConstructorOld(Class cl)
{
    Class initCl = cl;
    while (Serializable.class.isAssignableFrom(initCl))
    {
        if ((initCl = initCl.getSuperclass()) == null)
        {
            return null;
        }
    }
    try
    {
        // Constructor cons = initCl.getDeclaredConstructor(new Class[0]);
        Constructor cons = Object.class.getDeclaredConstructor(new Class[0]);
        int mods = cons.getModifiers();
//            if ((mods & Modifier.PRIVATE) != 0 ||
//                    ((mods & (Modifier.PUBLIC | Modifier.PROTECTED)) == 0 &&
//                    !packageEquals(cl, initCl))) {
//                return null;
//            }
        cons = reflFactory.newConstructorForSerialization(cl, cons);
        cons.setAccessible(true);
        System.out.println("Cons: " + cons);
        return cons;
    } catch (NoSuchMethodException ex)
    {
        System.out.println("Ex: " + ex);
        return null;
    }
}


}
