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

import ch.idsia.benchmark.mario.engine.LevelScene;
import ch.idsia.benchmark.mario.engine.level.Level;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;


public class LevelEditor extends JFrame implements ActionListener, Serializable
{
private static final long serialVersionUID = 7461321112832160393L;

private JButton loadButton;
private JButton saveButton;
private JTextField nameField;
private LevelEditView levelEditView;
private TilePicker tilePicker;

private JCheckBox[] bitmapCheckboxes = new JCheckBox[8];

public LevelEditor()
{
    super("Map Edit");

    try
    {
        System.out.println("System.getProperty(\"user.dir()\") = " + System.getProperty("user.dir"));
        Level.loadBehaviors(new DataInputStream(LevelScene.class.getResourceAsStream("resources/tiles.dat")));
//            Level.loadBehaviors(new DataInputStream(new FileInputStream("ch/idsia/mario/resources/tiles.dat")));
    }
    catch (Exception e)
    {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, e.toString(), "Failed to loadAgent tile behaviors", JOptionPane.ERROR_MESSAGE);
    }

    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    setSize(screenSize.width * 8 / 10, screenSize.height * 8 / 10);
    setLocation((screenSize.width - getWidth()) / 2, (screenSize.height - getHeight()) / 2);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    tilePicker = new TilePicker();
    JPanel tilePickerPanel = new JPanel(new BorderLayout());
    tilePickerPanel.add(BorderLayout.WEST, tilePicker);
    tilePickerPanel.add(BorderLayout.CENTER, buildBitmapPanel());
    tilePickerPanel.setBorder(new TitledBorder(new EtchedBorder(), "Tile picker"));

    JPanel lowerPanel = new JPanel(new BorderLayout());
    lowerPanel.add(BorderLayout.EAST, tilePickerPanel);

    JPanel borderPanel = new JPanel(new BorderLayout());
    levelEditView = new LevelEditView(tilePicker);
    borderPanel.add(BorderLayout.CENTER, new JScrollPane(levelEditView));
    borderPanel.add(BorderLayout.SOUTH, lowerPanel);
    borderPanel.add(BorderLayout.NORTH, buildButtonPanel());
    setContentPane(borderPanel);

    tilePicker.addTilePickChangedListener(this);
}

public JPanel buildBitmapPanel()
{
    JPanel panel = new JPanel(new GridLayout(0, 1));
    for (int i = 0; i < 8; i++)
    {
        bitmapCheckboxes[i] = new JCheckBox(Level.BIT_DESCRIPTIONS[i]);
        panel.add(bitmapCheckboxes[i]);
        if (Level.BIT_DESCRIPTIONS[i].startsWith("- ")) bitmapCheckboxes[i].setEnabled(false);

        final int id = i;
        bitmapCheckboxes[i].addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent arg0)
            {
                int bm = Level.TILE_BEHAVIORS[tilePicker.pickedTile & 0xff] & 0xff;
                bm &= 255 - (1 << id);
                if (bitmapCheckboxes[id].isSelected()) bm |= (1 << id);
                Level.TILE_BEHAVIORS[tilePicker.pickedTile & 0xff] = (byte) bm;

                try
                {
                    System.out.println("bm = " + bm);
                    Level.saveBehaviors(new DataOutputStream(new FileOutputStream("SAVEDtiles.dat")));
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(LevelEditor.this, e.toString(), "Failed to SAVE tile behaviors", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
    return panel;
}

public JPanel buildButtonPanel()
{
    loadButton = new JButton("Load");
    saveButton = new JButton("Save");
    nameField = new JTextField("resources/test.lvl", 10);
    loadButton.addActionListener(this);
    saveButton.addActionListener(this);
    JPanel panel = new JPanel();
    panel.add(nameField);
    panel.add(loadButton);
    panel.add(saveButton);
    return panel;
}

public void actionPerformed(ActionEvent e)
{
    try
    {
        if (e.getSource() == loadButton)
        {
            levelEditView.setLevel(Level.load(new ObjectInputStream(LevelScene.class.getResourceAsStream(nameField.getText().trim()))));
        }
        if (e.getSource() == saveButton)
        {
//                levelEditView.getLevel().save(new ObjectOutputStream(new FileOutputStream(nameField.getText().trim())));
            Level.save(levelEditView.getLevel(), new ObjectOutputStream(new FileOutputStream(nameField.getText().trim())));
        }
    }
    catch (Exception ex)
    {
        JOptionPane.showMessageDialog(this, ex.toString(), "Failed to loadAgent/save", JOptionPane.ERROR_MESSAGE);
    }
}

public static void main(String[] args)
{
    new LevelEditor().setVisible(true);
}

public void setPickedTile(byte pickedTile)
{
    int bm = Level.TILE_BEHAVIORS[pickedTile & 0xff] & 0xff;

    for (int i = 0; i < 8; i++)
    {
        bitmapCheckboxes[i].setSelected((bm & (1 << i)) > 0);
    }
}
}