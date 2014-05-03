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

package ch.idsia.tools.punj;

import org.objectweb.asm.*;
import org.objectweb.asm.tree.*;

import java.io.*;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by IntelliJ IDEA.
 * User: Sergey Karakovskiy, sergey.karakovskiy@gmail.com
 * Date: 3/9/11
 * Time: 6:27 PM
 * Package: ch.idsia.tools.punj
 */
public class PunctualJudge extends ClassLoader
{
private static long counter;

public Class<?> buildClass(byte[] data, String name)
{
    return defineClass(name, data, 0, data.length);
}

public static void incrementCounter()
{
    ++counter;
}

public static long getCounter()
{
    return counter;
}

public static void resetCounter()
{
    counter = 0;
}

/**
 * @param classFileName -- path to the class file. Example: competition.cig.sergeykarakovskiy.SergeyKarakovskiy_JumpingAgent.class
 * @return bytecode of the class
 * @throws IOException
 */
public byte[] instrumentClass(String classFileName) throws IOException
{
    byte[] instrumentedClass = null;

    final ClassReader cr = new ClassReader(new FileInputStream(classFileName));

    // create an empty ClassNode (in-memory representation of a class)
    final ClassNode clazz = new ClassNode();

    // have the ClassReader read the class file and populate the ClassNode with the corresponding information
    cr.accept(clazz, 0);

    // get the list of all methods in that class
    final List<MethodNode> methods = clazz.methods;
    for (int m = 0; m < methods.size(); m++)
    {
        final MethodNode method = methods.get(m);
        if (method.name.equals("<init>") ||
                method.name.equals("<clinit>") ||
                method.name.startsWith("reset"))
            continue;
        instrumentMethod(method);
    }

    ClassWriter cw = new ClassWriter(0);

    clazz.accept(cw);

    instrumentedClass = cw.toByteArray();
    return instrumentedClass;
}

private void instrumentMethod(MethodNode methodNode)
{
    // get the list of all instructions in that method
    final InsnList instructions = methodNode.instructions;
    ListIterator it = instructions.iterator();
    while (it.hasNext())
    {
        final AbstractInsnNode instruction = (AbstractInsnNode) it.next();
        if (instruction.getOpcode() != -1 &&
                instruction.getOpcode() != Opcodes.RETURN &&
                instruction.getOpcode() != Opcodes.IRETURN &&
                instruction.getOpcode() != Opcodes.ARETURN)
            instructions.insert(instruction, new MethodInsnNode(Opcodes.INVOKESTATIC, "ch/idsia/tools/punj/PunctualJudge", "incrementCounter", "()V"));
    }
}

}
