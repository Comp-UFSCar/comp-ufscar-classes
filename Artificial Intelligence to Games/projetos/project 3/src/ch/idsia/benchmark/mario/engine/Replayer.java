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

import ch.idsia.benchmark.mario.environments.Environment;
import ch.idsia.tools.ReplayerOptions;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Queue;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Created by IntelliJ IDEA.
 * User: Sergey Karakovskiy, firstName_at_idsia_dot_ch
 * Date: May 5, 2009
 * Time: 9:34:33 PM
 * Package: ch.idsia.utils
 */
public class Replayer
{
private ZipFile zf = null;
private ZipEntry ze = null;
private BufferedInputStream fis;
private ReplayerOptions options;

public Replayer(String replayOptions)
{
    this.options = new ReplayerOptions(replayOptions);
}

public boolean openNextReplayFile() throws IOException
{
//    if (zf != null)
//        zf.close();

    String fileName = options.getNextReplayFile();
    if (fileName == null)
        return false;

    if (!fileName.endsWith(".zip"))
        fileName += ".zip";

    zf = new ZipFile(fileName);
    ze = null;
    fis = null;

    try
    {
        openFile("chunks");
        options.setChunks((Queue) readObject());
    } catch (Exception ignored)
    {} //if file with replay chunks not found, than use user specified chunks

    return true;
}

public void openFile(String filename) throws Exception
{
    ze = zf.getEntry(filename);

    if (ze == null)
        throw new Exception("[Mario AI EXCEPTION] : File <" + filename + "> not found in the archive");
}

private void openBufferedInputStream() throws IOException
{
    fis = new BufferedInputStream(zf.getInputStream(ze));
}

public boolean[] readAction() throws IOException
{
    if (fis == null)
        openBufferedInputStream();

    boolean[] buffer = new boolean[Environment.numberOfKeys];
//    int count = fis.read(buffer, 0, size);
    byte actions = (byte) fis.read();
    for (int i = 0; i < Environment.numberOfKeys; i++)
    {
        if ((actions & (1 << i)) > 0)
            buffer[i] = true;
        else
            buffer[i] = false;
    }

    if (actions == -1)
        buffer = null;

    return buffer;
}

public Object readObject() throws IOException, ClassNotFoundException
{
    ObjectInputStream ois = new ObjectInputStream(zf.getInputStream(ze));
    Object res = ois.readObject();
//    ois.close();

    return res;
}

public void closeFile() throws IOException
{
//    fis.close();
}

public void closeReplayFile() throws IOException
{
    zf.close();
}

public boolean hasMoreChunks()
{
    return options.hasMoreChunks();
}

public int actionsFileSize()
{
    int size = (int) ze.getSize();
    if (size == -1)
        size = Integer.MAX_VALUE;
    return size;
}

public ReplayerOptions.Interval getNextIntervalInMarioseconds()
{
    return options.getNextIntervalInMarioseconds();
}

public ReplayerOptions.Interval getNextIntervalInTicks()
{
    return options.getNextIntervalInTicks();
}
}