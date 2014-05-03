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

package ch.idsia.tools;

import ch.idsia.benchmark.mario.engine.GlobalOptions;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: Sergey Karakovskiy, sergey.karakovskiy@gmail.com
 * Date: Oct 15, 2010
 * Time: 1:13:13 AM
 * Package: ch.idsia.tools
 */
public class ReplayerOptions
{
public static class Interval implements Serializable
{
    public int from;
    public int to;

    public Interval()
    {
        from = 0;
        to = 0;
    }

    public Interval(String interval)
    {
        String[] nums = interval.split(":");
        from = Integer.valueOf(nums[0]);
        to = Integer.valueOf(nums[1]);
    }

    public Interval(final int i, final int i1)
    {
        from = i;
        to = i1;
    }
}

private Queue<Interval> chunks = new LinkedList<Interval>();
private Queue<String> replays = new LinkedList<String>();
private String regex = "[a-zA-Z_0-9.-]+(;\\d+:\\d+)*";

public ReplayerOptions(String options)
{
    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(options);

    while (matcher.find())
    {
        String group = matcher.group();
        replays.add(group);
    }
}

public String getNextReplayFile()
{
    String s = replays.poll();
    if (s == null)
        return null;

    String[] subgroups = s.split(";");
    if (subgroups.length == 0)
        return null;

    String fileName = subgroups[0];
    chunks.clear();

    if (subgroups.length > 1)
        for (int i = 1; i < subgroups.length; i++)
            chunks.add(new Interval(subgroups[i]));

    return fileName;
}

public Interval getNextIntervalInMarioseconds()
{
    return chunks.poll();
}

public Interval getNextIntervalInTicks()
{
    Interval i = chunks.poll();
    Interval res = null;

    if (i != null)
        res = new Interval(i.from * GlobalOptions.mariosecondMultiplier, i.to * GlobalOptions.mariosecondMultiplier);

    return res;
}

public boolean hasMoreChunks()
{
    return !chunks.isEmpty();
}

public void setChunks(Queue chunks)
{
    this.chunks = chunks;
}
}
