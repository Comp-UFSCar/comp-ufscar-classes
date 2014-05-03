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

import ch.idsia.benchmark.mario.engine.level.Level;

import java.awt.*;
import java.util.Random;


public class LevelRenderer
{
private int xCam;
private int yCam;
private Image image;
private Graphics2D g;
private static final Color transparent = new Color(0, 0, 0, 0);
private Level level;

private static final int exitXOffset = 2;
private Random random = new Random();
public boolean renderBehaviors = false;

int width;
int height;

public LevelRenderer(Level level, GraphicsConfiguration graphicsConfiguration, int width, int height)
{
    this.width = width;
    this.height = height;

    this.level = level;
    image = graphicsConfiguration.createCompatibleImage(width, height, Transparency.BITMASK);
    g = (Graphics2D) image.getGraphics();
    g.setComposite(AlphaComposite.Src);

    updateArea(0, 0, width, height);
}

public void setCam(int xCam, int yCam)
{
    int xCamD = this.xCam - xCam;
    int yCamD = this.yCam - yCam;
    this.xCam = xCam;
    this.yCam = yCam;

    g.setComposite(AlphaComposite.Src);
    g.copyArea(0, 0, width, height, xCamD, yCamD);

    if (xCamD < 0)
    {
        if (xCamD < -width) xCamD = -width;
        updateArea(width + xCamD, 0, -xCamD, height);
    } else if (xCamD > 0)
    {
        if (xCamD > width) xCamD = width;
        updateArea(0, 0, xCamD, height);
    }

    if (yCamD < 0)
    {
        if (yCamD < -width) yCamD = -width;
        updateArea(0, height + yCamD, width, -yCamD);
    } else if (yCamD > 0)
    {
        if (yCamD > width) yCamD = width;
        updateArea(0, 0, width, yCamD);
    }
}

private void updateArea(int x0, int y0, int w, int h)
{
    g.setBackground(transparent);
    g.clearRect(x0, y0, w, h);
    int xTileStart = (x0 + xCam) / 16;
    int yTileStart = (y0 + yCam) / 16;
    int xTileEnd = (x0 + xCam + w) / 16;
    int yTileEnd = (y0 + yCam + h) / 16;
    for (int x = xTileStart; x <= xTileEnd; x++)
    {
        for (int y = yTileStart; y <= yTileEnd; y++)
        {
            int b = level.getBlock(x, y) & 0xff;
            if (((Level.TILE_BEHAVIORS[b]) & Level.BIT_ANIMATED) == 0)
            {
                g.drawImage(Art.level[b % 16][b / 16], (x << 4) - xCam, (y << 4) - yCam, null);
            }
        }
    }
}

public void render(Graphics g, int tick)
{
    g.drawImage(image, 0, 0, null);

    for (int x = xCam / 16; x <= (xCam + width) / 16; x++)
        for (int y = yCam / 16; y <= (yCam + height) / 16; y++)
        {
            byte b = level.getBlock(x, y);

            //drawing of hidden block number
            if (b == 1 && GlobalOptions.isShowReceptiveField)
            {
                g.setColor(Color.BLUE);
                int yo = 0;
                if (x >= 0 && y >= 0 && x < level.length && y < level.height) yo = level.data[x][y];
                if (yo > 0) yo = (int) (Math.sin(yo / 4.0f * Math.PI) * 8);
                g.drawString(String.valueOf(1), (x << 4) - xCam, (y << 4) - yCam - yo + LevelScene.cellSize);
            }

            if (((Level.TILE_BEHAVIORS[b & 0xff]) & Level.BIT_ANIMATED) > 0)
            {
                int animTime = (tick / 3) % 4;

                if ((b % 16) / 4 == 0 && b / 16 == 1)
                {
                    animTime = (tick / 2 + (x + y) / 8) % 20;
                    if (animTime > 3) animTime = 0;
                }
                if ((b % 16) / 4 == 3 && b / 16 == 0)
                {
                    animTime = 2;
                }
                int yo = 0;
                if (x >= 0 && y >= 0 && x < level.length && y < level.height) yo = level.data[x][y];
                if (yo > 0) yo = (int) (Math.sin(yo / 4.0f * Math.PI) * 8);
                if (yo < 0) yo = 0;
                g.drawImage(Art.level[(b % 16) / 4 * 4 + animTime][b / 16], (x << 4) - xCam, (y << 4) - yCam - yo, null);
            }
            /*                else if (b == Level.TILE_BONUS)
            {
            int animTime = (tickCount / 3) % 4;
            int yo = 0;
            if (x >= 0 && y >= 0 && x < level.length && y < level.height) yo = level.data[x][y];
            if (yo > 0) yo = (int) (Math.sin((yo - cameraOffSet) / 4.0f * Math.PI) * 8);
            g.drawImage(Art.mapSprites[(4 + animTime)][0], (x << 4) - xCam, (y << 4) - yCam - yo, null);
            }*/

            if (renderBehaviors)
            {
                if (((Level.TILE_BEHAVIORS[b & 0xff]) & Level.BIT_BLOCK_UPPER) > 0)
                {
                    g.setColor(Color.RED);
                    g.fillRect((x << 4) - xCam, (y << 4) - yCam, 16, 2);
                }
                if (((Level.TILE_BEHAVIORS[b & 0xff]) & Level.BIT_BLOCK_ALL) > 0)
                {
                    g.setColor(Color.RED);
                    g.fillRect((x << 4) - xCam, (y << 4) - yCam, 16, 2);
                    g.fillRect((x << 4) - xCam, (y << 4) - yCam + 14, 16, 2);
                    g.fillRect((x << 4) - xCam, (y << 4) - yCam, 2, 16);
                    g.fillRect((x << 4) - xCam + 14, (y << 4) - yCam, 2, 16);
                }
                if (((Level.TILE_BEHAVIORS[b & 0xff]) & Level.BIT_BLOCK_LOWER) > 0)
                {
                    g.setColor(Color.RED);
                    g.fillRect((x << 4) - xCam, (y << 4) - yCam + 14, 16, 2);
                }
                if (((Level.TILE_BEHAVIORS[b & 0xff]) & Level.BIT_SPECIAL) > 0)
                {
                    g.setColor(Color.PINK);
                    g.fillRect((x << 4) - xCam + 2 + 4, (y << 4) - yCam + 2 + 4, 4, 4);
                }
                if (((Level.TILE_BEHAVIORS[b & 0xff]) & Level.BIT_BUMPABLE) > 0)
                {
                    g.setColor(Color.BLUE);
                    g.fillRect((x << 4) - xCam + 2, (y << 4) - yCam + 2, 4, 4);
                }
                if (((Level.TILE_BEHAVIORS[b & 0xff]) & Level.BIT_BREAKABLE) > 0)
                {
                    g.setColor(Color.GREEN);
                    g.fillRect((x << 4) - xCam + 2 + 4, (y << 4) - yCam + 2, 4, 4);
                }
                if (((Level.TILE_BEHAVIORS[b & 0xff]) & Level.BIT_PICKUPABLE) > 0)
                {
                    g.setColor(Color.YELLOW);
                    g.fillRect((x << 4) - xCam + 2, (y << 4) - yCam + 2 + 4, 4, 4);
                }
                if (((Level.TILE_BEHAVIORS[b & 0xff]) & Level.BIT_ANIMATED) > 0)
                {
                }
            }

        }
}

public void repaint(int x, int y, int w, int h)
{
    updateArea(x * 16 - xCam, y * 16 - yCam, w * 16, h * 16);
}

public void setLevel(Level level)
{
    this.level = level;
    updateArea(0, 0, width, height);
}

/*public void renderExit0(Graphics g, int tickCount, boolean bar)
{
    for (int y = level.yExit - 8; y < level.yExit; y++)
    {
        g.drawImage(Art.level[12][y == level.yExit - 8 ? 4 : 5], (level.xExit - exitXOffset << 4) - xCam - 16, (y << 4) - yCam, null);
    }
    int yh = level.yExit * 16 - (int) ((Math.sin(tickCount / 20) * 0.5 + 0.5) * 7 * 16) - 8;
    if (bar)
    {
        g.drawImage(Art.level[12][3], (level.xExit - exitXOffset << 4) - xCam - 16, yh - yCam, null);
        g.drawImage(Art.level[13][3], (level.xExit - exitXOffset << 4) - xCam, yh - yCam, null);
    }

}


public void renderExit(Graphics g, int tickCount)
{
    //int yh = level.yExit * 14 - 6;
    g.drawImage(Art.princess[0][0], level.xExit, level.yExit, null);
}*/
}