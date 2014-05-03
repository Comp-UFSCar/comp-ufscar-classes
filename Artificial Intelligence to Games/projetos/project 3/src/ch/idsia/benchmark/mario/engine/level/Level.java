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

package ch.idsia.benchmark.mario.engine.level;

import ch.idsia.tools.MarioAIOptions;

import java.io.*;

public class Level implements Serializable
{
private static final long serialVersionUID = -2222762134065697580L;

static public class objCounters implements Serializable
{
    public int deadEndsCount = 0;
    public int cannonsCount = 0;
    public int hillStraightCount = 0;
    public int tubesCount = 0;
    public int blocksCount = 0;
    public int coinsCount = 0;
    public int gapsCount = 0;
    public int hiddenBlocksCount = 0;
    public int totalCannons;
    public int totalGaps;
    public int totalDeadEnds;
    public int totalBlocks;
    public int totalHiddenBlocks;
    public int totalCoins;
    public int totalHillStraight;
    public int totalTubes;
    // TODO:TASK:[M] : include in Evaluation info:
    public int totalPowerUps;

    public int mushrooms = 0;
    public int flowers = 0;
    public int creatures = 0;
    public int greenMushrooms = 0;

    private static final long serialVersionUID = 4505050755444159808L;

    public void reset(final MarioAIOptions args)
    {
        deadEndsCount = 0;
        cannonsCount = 0;
        hillStraightCount = 0;
        tubesCount = 0;
        blocksCount = 0;
        coinsCount = 0;
        gapsCount = 0;
        hiddenBlocksCount = 0;
        mushrooms = 0;
        flowers = 0;
        creatures = 0;
        greenMushrooms = 0;
        totalHillStraight = args.getHillStraightCount() ? Integer.MAX_VALUE : 0;
        totalCannons = args.getCannonsCount() ? Integer.MAX_VALUE : 0;
        totalGaps = args.getGapsCount() ? Integer.MAX_VALUE : 0;
        totalDeadEnds = args.getDeadEndsCount() ? Integer.MAX_VALUE : 0;
        totalBlocks = args.getBlocksCount() ? Integer.MAX_VALUE : 0;
        totalHiddenBlocks = args.getHiddenBlocksCount() ? Integer.MAX_VALUE : 0;
        totalCoins = args.getCoinsCount() ? Integer.MAX_VALUE : 0;
        totalTubes = args.getTubesCount() ? Integer.MAX_VALUE : 0;
        resetUncountableCounters();
    }

    public void resetUncountableCounters()
    {
        mushrooms = 0;
        flowers = 0;
        greenMushrooms = 0;
    }
}

public static final String[] BIT_DESCRIPTIONS = {//
        "BLOCK UPPER", //
        "BLOCK ALL", //
        "BLOCK LOWER", //
        "SPECIAL", //
        "BUMPABLE", //
        "BREAKABLE", //
        "PICKUPABLE", //
        "ANIMATED",//
};

public static byte[] TILE_BEHAVIORS = new byte[256];

public static final int BIT_BLOCK_UPPER = 1 << 0;
public static final int BIT_BLOCK_ALL = 1 << 1;
public static final int BIT_BLOCK_LOWER = 1 << 2;
public static final int BIT_SPECIAL = 1 << 3;
public static final int BIT_BUMPABLE = 1 << 4;
public static final int BIT_BREAKABLE = 1 << 5;
public static final int BIT_PICKUPABLE = 1 << 6;
public static final int BIT_ANIMATED = 1 << 7;

public static objCounters counters;

//private final int FILE_HEADER = 0x271c4178;
public int length;
public int height;
public int randomSeed;
public int type;
public int difficulty;

public byte[][] map;
public byte[][] data;
// Experimental feature: Mario TRACE
public int[][] marioTrace;

public SpriteTemplate[][] spriteTemplates;

public int xExit;
public int yExit;

public Level(int length, int height)
{
//        ints = new Vector();
//        booleans = new Vector();
    this.length = length;
    this.height = height;

    xExit = 50;
    yExit = 10;
//        System.out.println("Java: Level: lots of news here...");
//        System.out.println("length = " + length);
//        System.out.println("height = " + height);
    try
    {
        map = new byte[length][height];
//        System.out.println("map = " + map);
        data = new byte[length][height];
//        System.out.println("data = " + data);
        spriteTemplates = new SpriteTemplate[length][height];

        marioTrace = new int[length][height + 1];
    } catch (OutOfMemoryError e)
    {
        System.err.println("Java: MarioAI MEMORY EXCEPTION: OutOfMemory exception. Exiting...");
        e.printStackTrace();
        System.exit(-3);
    }
//        System.out.println("spriteTemplates = " + spriteTemplates);
//        observation = new byte[length][height];
//        System.out.println("observation = " + observation);
}

public static void loadBehaviors(DataInputStream dis) throws IOException
{
    dis.readFully(Level.TILE_BEHAVIORS);
}

public static void saveBehaviors(DataOutputStream dos) throws IOException
{
    dos.write(Level.TILE_BEHAVIORS);
}

public static Level load(ObjectInputStream ois) throws IOException, ClassNotFoundException
{
    Level level = (Level) ois.readObject();
    return level;
}

public static void save(Level lvl, ObjectOutputStream oos) throws IOException
{
    oos.writeObject(lvl);
}

/**
 * Animates the unbreakable brick when smashed from below by Mario
 */
public void tick()
{
    // TODO:!!H! Optimize this!
    for (int x = 0; x < length; x++)
        for (int y = 0; y < height; y++)
            if (data[x][y] > 0) data[x][y]--;
}

public byte getBlockCapped(int x, int y)
{
    if (x < 0) x = 0;
    if (y < 0) y = 0;
    if (x >= length) x = length - 1;
    if (y >= height) y = height - 1;
    return map[x][y];
}

public byte getBlock(int x, int y)
{
    if (x < 0) x = 0;
    if (y < 0) return 0;
    if (x >= length) x = length - 1;
    if (y >= height) y = height - 1;
    return map[x][y];
}

public void setBlock(int x, int y, byte b)
{
    if (x < 0) return;
    if (y < 0) return;
    if (x >= length) return;
    if (y >= height) return;
    map[x][y] = b;
}

public void setBlockData(int x, int y, byte b)
{
    if (x < 0) return;
    if (y < 0) return;
    if (x >= length) return;
    if (y >= height) return;
    data[x][y] = b;
}

public byte getBlockData(int x, int y)
{
    if (x < 0) return 0;
    if (y < 0) return 0;
    if (x >= length) return 0;
    if (y >= height) return 0;
    return data[x][y];
}

public boolean isBlocking(int x, int y, float xa, float ya)
{
    byte block = getBlock(x, y);
    boolean blocking = ((TILE_BEHAVIORS[block & 0xff]) & BIT_BLOCK_ALL) > 0;
    blocking |= (ya > 0) && ((TILE_BEHAVIORS[block & 0xff]) & BIT_BLOCK_UPPER) > 0;
    blocking |= (ya < 0) && ((TILE_BEHAVIORS[block & 0xff]) & BIT_BLOCK_LOWER) > 0;

    return blocking;
}

public SpriteTemplate getSpriteTemplate(int x, int y)
{
    if (x < 0) return null;
    if (y < 0) return null;
    if (x >= length) return null;
    if (y >= height) return null;
    return spriteTemplates[x][y];
}

public boolean setSpriteTemplate(int x, int y, SpriteTemplate spriteTemplate)
{
    if (x < 0) return false;
    if (y < 0) return false;
    if (x >= length) return false;
    if (y >= height) return false;
    spriteTemplates[x][y] = spriteTemplate;
    return true;
}

private void readObject(ObjectInputStream aInputStream) throws ClassNotFoundException, IOException
{
    aInputStream.defaultReadObject();
    counters = (Level.objCounters) aInputStream.readObject();
}

private void writeObject(ObjectOutputStream aOutputStream) throws IOException
{
    aOutputStream.defaultWriteObject();
    aOutputStream.writeObject(counters);
}
}


//    public void ASCIIToOutputStream(OutputStream os) throws IOException {
//        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
//        bw.write("\nlength = " + length);
//        bw.write("\nheight = " + height);
//        bw.write("\nMap:\n");
//        for (int y = 0; y < height; y++)
//
//        {
//                for (int x = 0; x < length; x++)
//
//            {
//                bw.write(map[x][y] + "\t");
//            }
//            bw.newLine();
//        }
//        bw.write("\nData: \n");
//
//        for (int y = 0; y < height; y++)
//
//        {
//                for (int x = 0; x < length; x++)
//
//            {
//                bw.write(data[x][y] + "\t");
//            }
//            bw.newLine();
//        }
//
//        bw.write("\nspriteTemplates: \n");
//        for (int y = 0; y < height; y++)
//
//        {
//                for (int x = 0; x < length; x++)
//
//            {
//                if                  (spriteTemplates[x][y] != null)
//                    bw.write(spriteTemplates[x][y].getType() + "\t");
//                else
//                    bw.write("_\t");
//
//            }
//            bw.newLine();
//        }
//
//        bw.write("\n==================\nAll objects: (Map[x,y], Data[x,y], Sprite[x,y])\n");
//        for (int y = 0; y < height; y++)
//        {
//                for (int x = 0; x < length; x++)
//
//            {
//                bw.write("(" + map[x][y] + "," + data[x][y] + ", " + ((spriteTemplates[x][y] == null) ? "_" : spriteTemplates[x][y].getType()) + ")\t");
//            }
//            bw.newLine();
//        }
//
////        bw.close();
//    }
