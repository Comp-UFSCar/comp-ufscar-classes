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

import ch.idsia.tools.ReplayerOptions;

import java.io.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by IntelliJ IDEA.
 * User: Sergey Karakovskiy, sergey@idsia.ch
 * Date: May 5, 2009
 * Time: 9:34:33 PM
 * Package: ch.idsia.utils
 */

public class Recorder
{
private ZipOutputStream zos;
boolean lastRecordingState = false;
private Queue<ReplayerOptions.Interval> chunks = new LinkedList<ReplayerOptions.Interval>();
private ReplayerOptions.Interval chunk;

private ByteArrayOutputStream byteOut;
private boolean saveReady = false;
private boolean canRecord;
private boolean lazyRec = false;

public Recorder(String fileName) throws FileNotFoundException
{
    if (!fileName.endsWith(".zip"))
        fileName += ".zip";

    zos = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(fileName)));
    canRecord = true;
}

public Recorder()
{
    byteOut = new ByteArrayOutputStream();
    zos = new ZipOutputStream(byteOut);
    canRecord = true;
    lazyRec = true;
}

public void saveLastRun(String filename) throws IOException
{
    FileOutputStream fo = new FileOutputStream(filename);
    byteOut.writeTo(fo);

}

public void createFile(String filename) throws IOException
{
    zos.putNextEntry(new ZipEntry(filename));
}

public void writeObject(Object object) throws IOException
{
    ObjectOutputStream oos = new ObjectOutputStream(zos);
    oos.writeObject(object);
    oos.flush();
}

public void closeFile() throws IOException
{
    zos.flush();
    zos.closeEntry();
}

public void closeRecorder(int time) throws IOException
{
    changeRecordingState(false, time);
    if (!chunks.isEmpty())
    {
        createFile("chunks");
        writeObject(chunks);
        closeFile();
    }
    zos.flush();
    zos.close();
    canRecord = false;
    if (lazyRec)
        saveReady = true;
}

public void writeAction(final boolean[] bo) throws IOException
{
    byte action = 0;

    for (int i = 0; i < bo.length; i++)
        if (bo[i])
            action |= (1 << i);

    zos.write(action);
}

public void changeRecordingState(boolean state, int time)
{
    if (state && !lastRecordingState)
    {
        chunk = new ReplayerOptions.Interval();
        chunk.from = time;
        lastRecordingState = state;
    } else if (!state && lastRecordingState)
    {
        chunk.to = time;
        chunks.add(chunk);
        lastRecordingState = state;
    }
}

public boolean canRecord()
{
    return canRecord;
}

public boolean canSave()
{
    return saveReady;
}
}