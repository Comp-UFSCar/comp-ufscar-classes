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

import ch.idsia.tools.GameViewer;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public abstract class GlobalOptions
{
public static final int primaryVerionUID = 0;
public static final int minorVerionUID = 1;
public static final int minorSubVerionID = 9;

public static boolean areLabels = false;
public static boolean isCameraCenteredOnMario = false;
public static Integer FPS = 24;
public static int MaxFPS = 100;
public static boolean areFrozenCreatures = false;

public static boolean isVisualization = true;
public static boolean isGameplayStopped = false;
public static boolean isFly = false;

private static GameViewer GameViewer = null;
//    public static boolean isTimer = true;

public static int mariosecondMultiplier = 15;

public static boolean isPowerRestoration;

// required for rendering grid in ch/idsia/benchmark/mario/engine/sprites/Sprite.java
public static int receptiveFieldWidth = 19;
public static int receptiveFieldHeight = 19;
public static int marioEgoCol = 9;
public static int marioEgoRow = 9;

private static MarioVisualComponent marioVisualComponent;
public static int VISUAL_COMPONENT_WIDTH = 320;
public static int VISUAL_COMPONENT_HEIGHT = 240;

public static boolean isShowReceptiveField = false;
public static boolean isScale2x = false;
public static boolean isRecording = false;
public static boolean isReplaying = false;

public static int getPrimaryVersionUID()
{
    return primaryVerionUID;
}

public static int getMinorVersionUID()
{
    return minorVerionUID;
}

public static int getMinorSubVersionID()
{
    return minorSubVerionID;
}

public static String getBenchmarkName()
{
    return "[~ Mario AI Benchmark ~" + GlobalOptions.getVersionUID() + "]";
}

public static String getVersionUID()
{
    return " " + getPrimaryVersionUID() + "." + getMinorVersionUID() + "." + getMinorSubVersionID();
}

public static void registerMarioVisualComponent(MarioVisualComponent mc)
{
    marioVisualComponent = mc;
}

public static void registerGameViewer(GameViewer gv)
{
    GameViewer = gv;
}

public static void AdjustMarioVisualComponentFPS()
{
    if (marioVisualComponent != null)
        marioVisualComponent.adjustFPS();
}

public static void gameViewerTick()
{
    if (GameViewer != null)
        GameViewer.tick();
}

public static String getDateTime(Long d)
{
    final DateFormat dateFormat = (d == null) ? new SimpleDateFormat("yyyy/MM/dd HH:mm:ss:ms") :
            new SimpleDateFormat("HH:mm:ss:ms");
    if (d != null)
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
    final Date date = (d == null) ? new Date() : new Date(d);
    return dateFormat.format(date);
}

final static private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");

public static String getTimeStamp()
{
    return dateFormat.format(new Date());
}

public static void changeScale2x()
{
    if (marioVisualComponent == null)
        return;

    isScale2x = !isScale2x;
    marioVisualComponent.width *= isScale2x ? 2 : 0.5;
    marioVisualComponent.height *= isScale2x ? 2 : 0.5;
    marioVisualComponent.changeScale2x();
}
}
