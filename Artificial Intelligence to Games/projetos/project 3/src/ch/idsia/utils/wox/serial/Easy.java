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

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;

/**
 * Created by IntelliJ IDEA.
 * User: sml
 * Date: 08-Nov-2004
 * Time: 15:02:11
 * To change this template use Options | File Templates.
 */
public class Easy
{
public static void save(Object ob, String filename)
{
    try
    {
        ObjectWriter writer = new SimpleWriter();
        Element el = writer.write(ob);
        XMLOutputter out = new XMLOutputter(); // ("  ", true);
        FileWriter file = new FileWriter(filename);
        out.output(el, file);
        file.close();
        System.out.println("Saved object to " + filename);
    } catch (Exception e)
    {
        e.printStackTrace();
    }
}

public static Object load(String filename)
{
    try
    {
        SAXBuilder builder = new SAXBuilder();
        InputStream is = new FileInputStream(filename);
        Document doc = builder.build(is);
        Element el = doc.getRootElement();
        ObjectReader reader = new SimpleReader();
        return reader.read(el);
    } catch (Exception e)
    {
        System.err.println("Easy: cannot find wox definition with name " + filename);
        return null;
    }
}
}
