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

package ch.idsia.benchmark.mario.engine.mapedit;

import ch.idsia.benchmark.mario.engine.Art;
import ch.idsia.benchmark.mario.engine.LevelRenderer;
import ch.idsia.benchmark.mario.engine.level.Level;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;


public class LevelEditView extends JComponent implements MouseListener, MouseMotionListener
{
private static final long serialVersionUID = -7696446733303717142L;

private LevelRenderer levelRenderer;
private Level level;

private int xTile = -1;
private int yTile = -1;
private TilePicker tilePicker;

public LevelEditView(TilePicker tilePicker)
{
    this.tilePicker = tilePicker;
    level = new Level(256, 15);
    Dimension size = new Dimension(level.length * 16, level.height * 16);
    setPreferredSize(size);
    setMinimumSize(size);
    setMaximumSize(size);

    addMouseListener(this);
    addMouseMotionListener(this);
}

public void setLevel(Level level)
{
    this.level = level;
    Dimension size = new Dimension(level.length * 16, level.height * 16);
    setPreferredSize(size);
    setMinimumSize(size);
    setMaximumSize(size);
    repaint();
    levelRenderer.setLevel(level);
}

public Level getLevel()
{
    return level;
}

public void addNotify()
{
    super.addNotify();
    Art.init(getGraphicsConfiguration());
    levelRenderer = new LevelRenderer(level, getGraphicsConfiguration(), level.length * 16, level.height * 16);
    levelRenderer.renderBehaviors = true;
}

public void paintComponent(Graphics g)
{
    g.setColor(new Color(0x8090ff));
    g.fillRect(0, 0, level.length * 16, level.height * 16);
    levelRenderer.render(g, 0);
    g.setColor(Color.BLACK);
    g.drawRect(xTile * 16 - 1, yTile * 16 - 1, 17, 17);
}

public void mouseClicked(MouseEvent e)
{
}

public void mouseEntered(MouseEvent e)
{
}

public void mouseExited(MouseEvent e)
{
    xTile = -1;
    yTile = -1;
    repaint();
}

public void mousePressed(MouseEvent e)
{
    xTile = e.getX() / 16;
    yTile = e.getY() / 16;

    if (e.getButton() == 3)
    {
        tilePicker.setPickedTile(level.getBlock(xTile, yTile));
    } else
    {
        level.setBlock(xTile, yTile, tilePicker.pickedTile);
        levelRenderer.repaint(xTile - 1, yTile - 1, 3, 3);

        repaint();
    }
}

public void mouseReleased(MouseEvent e)
{
}

public void mouseDragged(MouseEvent e)
{
    xTile = e.getX() / 16;
    yTile = e.getY() / 16;

    level.setBlock(xTile, yTile, tilePicker.pickedTile);
    levelRenderer.repaint(xTile - 1, yTile - 1, 3, 3);

    repaint();
}

public void mouseMoved(MouseEvent e)
{
    xTile = e.getX() / 16;
    yTile = e.getY() / 16;
    repaint();
}
}