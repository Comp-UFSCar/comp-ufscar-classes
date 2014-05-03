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

import org.jdom.Element;
import org.jdom.output.XMLOutputter;

/**
 * Created by IntelliJ IDEA.
 * User: sml
 * Date: 06-Aug-2004
 * Time: 10:00:32
 * To change this template use Options | File Templates.
 */
public class ReadTest
{
public static void main(String[] args) throws Exception
{
    ObjectReader reader = new SimpleReader();
    ObjectWriter writer = new SimpleWriter();

    int n = 1000000;
    int[] a1 = new int[n];
    // int[][][] a2 = new int[3][3][3];
    double[][][] a2 = new double[3][3][3];
    TestObject to = new TestObject(2);
    to.inner = new TestObject.Inner(1024);

//        ObjectOutputStream oos = new ObjectOutputStream(System.out);
//        oos.writeObject( to );
//        ObjectInputStream ois = new ObjectInputStream(null);
//        ois.readObject();

    Element xob = writer.write(to);
    XMLOutputter out = new XMLOutputter();
    out.output(xob, System.out);

    System.out.println("");
    Object ob = reader.read(xob);

    System.out.println("Read: " + ob.getClass());

    xob = new SimpleWriter().write(ob);
    out.output(xob, System.out);
//        for (int i=0; i<a.length; i++) {
//            System.out.println(a[i]);
//        }

    System.out.println("Check ref: " + to.alist.get(0) + " = " + to);

}
}
