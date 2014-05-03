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
import ch.idsia.benchmark.mario.engine.MarioVisualComponent;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * Created by IntelliJ IDEA.
 * User: Sergey Karakovskiy
 * Date: Mar 29, 2009
 * Time: 3:34:13 PM
 * Package: .Tools
 */

public class GameViewer extends JFrame
{
Dimension defaultSize = new Dimension(900, 800);
Point defaultLocation = new Point(350, 10);

//    Thread animator;
//    int frame;
int delay;
int FPS = 5;
private MarioVisualComponent marioVisualComponent;

public void AdjustFPS()
{
    int fps = FPS; // GlobalOptions.FPS;
    delay = (fps > 0) ? (fps >= GlobalOptions.MaxFPS) ? 0 : (1000 / fps) : 100;
    System.out.println("Game Viewer animator delay: " + delay);
}

GameViewerView gameViewerViewPanel = new GameViewerView();

public void setMarioVisualComponent(MarioVisualComponent marioVisualComponent)
{
    this.marioVisualComponent = marioVisualComponent;
}

private class GameViewerView extends JPanel implements Runnable
{
    Thread animator;
//        private MarioVisualComponent marioVisualComponent;

    public void start()
    {
        animator = new Thread(this);
        animator.start();
    }

    public void stop()
    {
        animator = null;
    }

    public void paint(Graphics g)
    {
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(Color.YELLOW);
        int y_dump = 0;
        g.drawString("Current GAME STATE: ", 320, y_dump += 11);
        g.setColor(Color.GREEN);
        if (this.getMarioVisualComponent() != null)
        {
            for (String s : this.getMarioVisualComponent().getTextObservation(
                    ShowEnemiesObservation.getState(),
                    ShowLevelMapObservation.getState(),
                    ShowMergedObservation.getState(),
                    ZLevelMapValue,
                    ZLevelEnemiesValue))
            {
                g.setColor((s.charAt(0) == '~') ? Color.YELLOW : Color.GREEN);
                g.drawString(s, 0, y_dump += 11);
            }

        }

    }

    public void run()
    {
        // Remember the starting time
        long tm = System.currentTimeMillis();
        while (Thread.currentThread() == animator)
        {
            // Display the next frame of animation.
            repaint();

            // Delay depending on how far we are behind.
            try
            {
                tm += delay;
                Thread.sleep(Math.max(0, tm - System.currentTimeMillis()));
            } catch (InterruptedException e)
            {
                break;
            }

            // Advance the frame
//                frame++;
        }
    }

    public MarioVisualComponent getMarioVisualComponent()
    {
        return marioVisualComponent;
    }
}

public void tick()
{
    if (GlobalOptions.isGameplayStopped)
        return;
    gameViewerViewPanel.repaint();
}

public TextField Console = new TextField();
public Label LabelConsole = new Label("TextFieldConsole:");
public Checkbox ShowLevelMapObservation = new Checkbox("Show Level Map Observation", true);
public Checkbox ShowEnemiesObservation = new Checkbox("Show Enemies Observation");
public Checkbox ShowMergedObservation = new Checkbox("Show Merged Observation");
public Button btnUpdate = new Button("Update");
public Checkbox ContinuousUpdates = new Checkbox("Continuous Updates", false);
CheckboxGroup ZLevelMap = new CheckboxGroup();
Checkbox Z0_Map = new Checkbox("Z0_Map", ZLevelMap, true);
Checkbox Z1_Map = new Checkbox("Z1_Map", ZLevelMap, false);
Checkbox Z2_Map = new Checkbox("Z2_Map", ZLevelMap, false);
CheckboxGroup ZLevelEnemies = new CheckboxGroup();
Checkbox Z0_Enemies = new Checkbox("Z0_Enemies", ZLevelEnemies, true);
Checkbox Z1_Enemies = new Checkbox("Z1_Enemies", ZLevelEnemies, false);
Checkbox Z2_Enemies = new Checkbox("Z2_Enemies", ZLevelEnemies, false);


private ToolsConfigurator toolsConfigurator = null;
private int ZLevelMapValue = 0;
private int ZLevelEnemiesValue = 0;

public GameViewer(MarioAIOptions marioAIOptions)
{
    super(GlobalOptions.getBenchmarkName() + " Game Viewer");
    Dimension size = null;
    Point location = null;

    setSize((size == null) ? defaultSize : size);
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    defaultLocation.setLocation(screenSize.getWidth() - defaultSize.getWidth(), 0);

    setLocation((location == null) ? defaultLocation : location);

    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    GameViewerActions gameViewerActions = new GameViewerActions();
    ShowEnemiesObservation.addItemListener(gameViewerActions);
    Console.addActionListener(gameViewerActions);
    ShowLevelMapObservation.addItemListener(gameViewerActions);
    ShowMergedObservation.addItemListener(gameViewerActions);
    btnUpdate.addActionListener(gameViewerActions);
    ContinuousUpdates.addItemListener(gameViewerActions);
    Z0_Map.addItemListener(gameViewerActions);
    Z1_Map.addItemListener(gameViewerActions);
    Z2_Map.addItemListener(gameViewerActions);
    Z0_Enemies.addItemListener(gameViewerActions);
    Z1_Enemies.addItemListener(gameViewerActions);
    Z2_Enemies.addItemListener(gameViewerActions);


    JPanel GameViewerOptionsPanel = new JPanel(new GridLayout(0, 3));
    GameViewerOptionsPanel.add(Z0_Map);
    GameViewerOptionsPanel.add(Z0_Enemies);
    GameViewerOptionsPanel.add(LabelConsole);
    GameViewerOptionsPanel.add(Z1_Map);
    GameViewerOptionsPanel.add(Z1_Enemies);
    GameViewerOptionsPanel.add(Console);
    GameViewerOptionsPanel.add(Z2_Map);
    GameViewerOptionsPanel.add(Z2_Enemies);
    GameViewerOptionsPanel.add(btnUpdate);
    GameViewerOptionsPanel.add(ShowLevelMapObservation);
    GameViewerOptionsPanel.add(ShowEnemiesObservation);
    GameViewerOptionsPanel.add(ShowMergedObservation);
    GameViewerOptionsPanel.add(ContinuousUpdates);
    ContinuousUpdates.setState(marioAIOptions.isGameViewerContinuousUpdates());

    GameViewerOptionsPanel.setBorder(new TitledBorder(new EtchedBorder(), "Mario AI Game Viewer Options"));

    Dimension sizeOfView = new Dimension(1600, 960);
    gameViewerViewPanel.setPreferredSize(sizeOfView);
    gameViewerViewPanel.setMinimumSize(sizeOfView);
    gameViewerViewPanel.setMaximumSize(sizeOfView);
    gameViewerViewPanel.setBorder(new TitledBorder(new EtchedBorder(), "Game Viewer View"));

//        JPanel lowerPanel = new JPanel(new GridLayout(0, 1));
//        lowerPanel.add(GameViewerOptionsPanel);
//        lowerPanel.add(new JScrollPane(gameViewerViewPanel));

    JPanel borderPanel = new JPanel(new BorderLayout());
//        levelEditView = new LevelEditView(tilePicker);
//        borderPanel.add(BorderLayout.CENTER, new JScrollPane(levelEditView));
    borderPanel.add(BorderLayout.NORTH, GameViewerOptionsPanel);
    borderPanel.add(BorderLayout.CENTER, new JScrollPane(gameViewerViewPanel));
    setContentPane(borderPanel);

    GlobalOptions.registerGameViewer(this);
}


public class GameViewerActions implements ActionListener, ItemListener
{
    public void actionPerformed(ActionEvent ae)
    {
        Object ob = ae.getSource();
        if (ob == Console)
        {
            LabelConsole.setText("TextFieldConsole sent message:");
            toolsConfigurator.setConsoleText(Console.getText());
        } else if (ob == btnUpdate)
        {
            System.out.println("ob = " + ob);
            gameViewerViewPanel.repaint();
        }
//            else
//            {
//                iw.setVisible(false);
//                b.setLabel("Show");
//            }
    }

    public void itemStateChanged(ItemEvent ie)
    {
        Object ob = ie.getSource();
        if (ob == ShowEnemiesObservation)
        {
            Console.setText("Enemies " + (ShowEnemiesObservation.getState() ? "Shown" : "Hidden"));
            gameViewerViewPanel.repaint();
        } else if (ob == ShowLevelMapObservation)
        {
            Console.setText("Level Map " + (ShowLevelMapObservation.getState() ? "Shown" : "Hidden"));
            gameViewerViewPanel.repaint();
        } else if (ob == ShowMergedObservation)
        {
            Console.setText("Merged Observation " + (ShowMergedObservation.getState() ? "Shown" : "Hidden"));
            gameViewerViewPanel.repaint();
        } else if (ob == ContinuousUpdates)
        {
            Console.setText("Continuous Updates " + (ContinuousUpdates.getState() ? "On" : "Off"));
        } else if (ob == Z0_Map)
        {
            if (Z0_Map.getState())
                ZLevelMapValue = 0;
            Console.setText("Zoom Level Map: Z" + ZLevelMapValue);
            gameViewerViewPanel.repaint();
        } else if (ob == Z1_Map)
        {
            if (Z1_Map.getState())
                ZLevelMapValue = 1;
            Console.setText("Zoom Level Map: Z" + ZLevelMapValue);
            gameViewerViewPanel.repaint();
        } else if (ob == Z2_Map)
        {
            if (Z2_Map.getState())
                ZLevelMapValue = 2;
            Console.setText("Zoom Level Map: Z" + ZLevelMapValue);
            gameViewerViewPanel.repaint();
        } else if (ob == Z0_Enemies)
        {
            if (Z0_Enemies.getState())
                ZLevelEnemiesValue = 0;
            Console.setText("Zoom Level Enemies: Z" + ZLevelEnemiesValue);
            gameViewerViewPanel.repaint();
        } else if (ob == Z1_Enemies)
        {
            if (Z1_Enemies.getState())
                ZLevelEnemiesValue = 1;
            Console.setText("Zoom Level Enemies: Z" + ZLevelEnemiesValue);
            gameViewerViewPanel.repaint();
        } else if (ob == Z2_Enemies)
        {
            if (Z2_Enemies.getState())
                ZLevelEnemiesValue = 2;
            Console.setText("Zoom Level Enemies: Z" + ZLevelEnemiesValue);
            gameViewerViewPanel.repaint();
        }
    }
}

public void setToolsConfigurator(ToolsConfigurator toolsConfigurator) {this.toolsConfigurator = toolsConfigurator;}

public void setConsoleText(String text)
{
    LabelConsole.setText("TextFieldConsole got message:");
    Console.setText(text);
}

public boolean getContinuousUpdatesState()
{
    return ContinuousUpdates.getState();
}
}
