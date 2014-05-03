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
package ch.idsia.agents.controllers;

import ch.idsia.agents.Agent;
import ch.idsia.benchmark.mario.environments.Environment;

/**
 * Created by IntelliJ IDEA. User: Sergey Karakovskiy Date: Apr 25, 2009 Time: 12:30:41 AM Package:
 * ch.idsia.agents.controllers;
 */
public class BasicMarioAIAgent implements Agent {

    protected boolean action[] = new boolean[Environment.numberOfKeys];
    protected String name = "Instance_of_BasicAIAgent._Change_this_name";

    /*final*/
    protected byte[][] levelScene;
    /*final */
    protected byte[][] enemies;
    protected byte[][] mergedObservation;

    protected float[] marioFloatPos = null;
    protected float[] enemiesFloatPos = null;

    protected int[] marioState = null;

    protected int marioStatus;
    protected int marioMode;
    protected boolean isMarioOnGround;
    protected boolean isMarioAbleToJump;
    protected boolean isMarioAbleToShoot;
    protected boolean isMarioCarrying;
    protected int getKillsTotal;
    protected int getKillsByFire;
    protected int getKillsByStomp;
    protected int getKillsByShell;

    protected int receptiveFieldWidth;
    protected int receptiveFieldHeight;
    protected int marioEgoRow;
    protected int marioEgoCol;

// values of these variables could be changed during the Agent-Environment interaction.
// Use them to get more detailed or less detailed description of the level.
// for information see documentation for the benchmark <link: marioai.org/marioaibenchmark/zLevels
    int zLevelScene = 1;
    int zLevelEnemies = 0;

    public BasicMarioAIAgent(String s) {
        setName(s);
    }

    public boolean[] getAction() {
        return new boolean[Environment.numberOfKeys];

    }

    public void integrateObservation(Environment environment) {
        levelScene = environment.getLevelSceneObservationZ(zLevelScene);
        enemies = environment.getEnemiesObservationZ(zLevelEnemies);
        mergedObservation = environment.getMergedObservationZZ(1, 0);

        this.marioFloatPos = environment.getMarioFloatPos();
        this.enemiesFloatPos = environment.getEnemiesFloatPos();
        this.marioState = environment.getMarioState();

        receptiveFieldWidth = environment.getReceptiveFieldWidth();
        receptiveFieldHeight = environment.getReceptiveFieldHeight();

    // It also possible to use direct methods from Environment interface.
        //
        marioStatus = marioState[0];
        marioMode = marioState[1];
        isMarioOnGround = marioState[2] == 1;
        isMarioAbleToJump = marioState[3] == 1;
        isMarioAbleToShoot = marioState[4] == 1;
        isMarioCarrying = marioState[5] == 1;
        getKillsTotal = marioState[6];
        getKillsByFire = marioState[7];
        getKillsByStomp = marioState[8];
        getKillsByShell = marioState[9];
    }

    public void giveIntermediateReward(float intermediateReward) {

    }

    public void reset() {
        action = new boolean[Environment.numberOfKeys];// Empty action
    }

    public void setObservationDetails(final int rfWidth, final int rfHeight, final int egoRow, final int egoCol) {
        receptiveFieldWidth = rfWidth;
        receptiveFieldHeight = rfHeight;

        marioEgoRow = egoRow;
        marioEgoCol = egoCol;
    }

    @Deprecated
    public boolean[] getAction(Environment observation) {
        return new boolean[Environment.numberOfKeys]; // Empty action
    }

    public String getName() {
        return name;
    }

    public void setName(String Name) {
        this.name = Name;
    }

    public int getEnemiesCellValue(int x, int y) {
        if (x < 0 || x >= levelScene.length || y < 0 || y >= levelScene[0].length) {
            return 0;
        }

        return enemies[x][y];
    }

    public int getReceptiveFieldCellValue(int x, int y) {
        if (x < 0 || x >= levelScene.length || y < 0 || y >= levelScene[0].length) {
            return 0;
        }

        return levelScene[x][y];
    }
//    public void integrateObservation(int[] serializedLevelSceneObservationZ, int[] serializedEnemiesObservationZ, float[] marioFloatPos, float[] enemiesFloatPos, int[] marioState)
//    {
//        int k = 0;
//        for (int i = 0; i < levelScene.length; ++i)
//        {
//            for (int j = 0; j < levelScene[0].length; ++j)
//            {
//                levelScene[i][j] = (byte)serializedLevelSceneObservationZ[k];
//                enemies[i][j] = (byte)serializedEnemiesObservationZ[k++];
//                mergedObservation[i][j] = levelScene[i][j];
//                // Simulating merged observation!
//                if (enemies[i][j] != 0)
//                {
//                    mergedObservation[i][j] = enemies[i][j];
//                }
////                System.out.print(observation[i][j] + "\t");
//            }
////            System.out.println();
//        }
//        this.marioFloatPos = marioFloatPos;
//        this.enemiesFloatPos = enemiesFloatPos;
//        this.marioState = marioState;
//
//        marioStatus = marioState[0];
//        marioMode = marioState[1];
//        isMarioOnGround = marioState[2] == 1 ;
//        isMarioAbleToJump = marioState[3] == 1;
//        isMarioAbleToShoot = marioState[4] == 1;
//        isMarioCarrying = marioState[5] == 1;
//        getKillsTotal = marioState[6];
//        getKillsByFire = marioState[7];
//        getKillsByStomp = marioState[8];
//        getKillsByShell = marioState[9];
//    }
}
