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


public class BgRenderer
{
private int xCam;
private int yCam;
private Image image;
private Graphics2D g;
private static final Color transparent = new Color(0, 0, 0, 0);
private Level level;

private Random random = new Random();
public boolean renderBehaviors = false;

private int width;
private int height;
private int distance;

public BgRenderer(Level level, GraphicsConfiguration graphicsConfiguration, int width, int height, int distance)
{
    this.distance = distance;
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
    xCam /= distance;
    yCam /= distance;
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
    int xTileStart = (x0 + xCam) / 32;
    int yTileStart = (y0 + yCam) / 32;
    int xTileEnd = (x0 + xCam + w) / 32;
    int yTileEnd = (y0 + yCam + h) / 32;
    for (int x = xTileStart; x <= xTileEnd; x++)
    {
        for (int y = yTileStart; y <= yTileEnd; y++)
        {
            int b = level.getBlock(x, y) & 0xff;
            g.drawImage(Art.bg[b % 8][b / 8], (x << 5) - xCam, (y << 5) - yCam - 16, null);
        }
    }
}

public void render(Graphics g)
{
    g.drawImage(image, 0, 0, null);
}

}