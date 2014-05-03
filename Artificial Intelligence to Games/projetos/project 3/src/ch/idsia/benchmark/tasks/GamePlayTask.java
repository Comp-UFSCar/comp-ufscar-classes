/*
 * Copyright (c) 2009-2010, Sergey Karakovskiy and Julian Togelius
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *  Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *  Neither the name of the Mario AI nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
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

package ch.idsia.benchmark.tasks;

import ch.idsia.tools.EvaluationInfo;
import ch.idsia.tools.MarioAIOptions;

import java.text.DecimalFormat;

/**
 * Created by IntelliJ IDEA. \n User: Sergey Karakovskiy, sergey at idsia dot ch Date: Mar 24, 2010 Time: 12:58:00 PM
 * Package: ch.idsia.maibe.tasks
 */
public class GamePlayTask extends BasicTask implements Task
{
private static final DecimalFormat df = new DecimalFormat("#.##");
private EvaluationInfo localEvaluationInfo;
private int difqualifications = 0;

public GamePlayTask(MarioAIOptions marioAIOptions)
{
    super(marioAIOptions);

    localEvaluationInfo = new EvaluationInfo();
    localEvaluationInfo.setTaskName("GamePlayTask");
    localEvaluationInfo.distancePassedCells = 0;
    localEvaluationInfo.distancePassedPhys = 0;
    localEvaluationInfo.flowersDevoured = 0;
    localEvaluationInfo.killsTotal = 0;
    localEvaluationInfo.killsByFire = 0;
    localEvaluationInfo.killsByShell = 0;
    localEvaluationInfo.killsByStomp = 0;
    localEvaluationInfo.marioMode = 0;
    localEvaluationInfo.marioStatus = 0;
    localEvaluationInfo.mushroomsDevoured = 0;
    localEvaluationInfo.coinsGained = 0;
    localEvaluationInfo.timeLeft = 0;
    localEvaluationInfo.timeSpent = 0;
    localEvaluationInfo.hiddenBlocksFound = 0;
    localEvaluationInfo.totalNumberOfCoins = 0;
    localEvaluationInfo.totalNumberOfCreatures = 0;
    localEvaluationInfo.totalNumberOfFlowers = 0;
    localEvaluationInfo.totalNumberOfMushrooms = 0;
    localEvaluationInfo.totalNumberOfHiddenBlocks = 0;
    localEvaluationInfo.collisionsWithCreatures = 0;
    localEvaluationInfo.levelLength = 0;
}

private void updateEvaluationInfo(EvaluationInfo evInfo)
{
    localEvaluationInfo.distancePassedCells += evInfo.distancePassedCells;
    localEvaluationInfo.distancePassedPhys += evInfo.distancePassedPhys;
    localEvaluationInfo.flowersDevoured += evInfo.flowersDevoured;
    localEvaluationInfo.killsTotal += evInfo.killsTotal;
    localEvaluationInfo.killsByFire += evInfo.killsByFire;
    localEvaluationInfo.killsByShell += evInfo.killsByShell;
    localEvaluationInfo.killsByStomp += evInfo.killsByStomp;
    localEvaluationInfo.marioMode += evInfo.marioMode;
    localEvaluationInfo.marioStatus += evInfo.marioStatus;
    localEvaluationInfo.mushroomsDevoured += evInfo.mushroomsDevoured;
    localEvaluationInfo.coinsGained += evInfo.coinsGained;
    localEvaluationInfo.timeLeft += evInfo.timeLeft;
    localEvaluationInfo.timeSpent += evInfo.timeSpent;
    localEvaluationInfo.hiddenBlocksFound += evInfo.hiddenBlocksFound;
    localEvaluationInfo.totalNumberOfCoins += evInfo.totalNumberOfCoins;
    localEvaluationInfo.totalNumberOfCreatures += evInfo.totalNumberOfCreatures;
    localEvaluationInfo.totalNumberOfFlowers += evInfo.totalNumberOfFlowers;
    localEvaluationInfo.totalNumberOfMushrooms += evInfo.totalNumberOfMushrooms;
    localEvaluationInfo.totalNumberOfHiddenBlocks += evInfo.totalNumberOfHiddenBlocks;
    localEvaluationInfo.collisionsWithCreatures += evInfo.collisionsWithCreatures;
    localEvaluationInfo.levelLength += evInfo.levelLength;
}

public void doEpisodes(final int amount, final boolean verbose, final int repetitionsOfSingleEpisode)
{
    for (int i = 0; i < amount; ++i)
    {
        options.setLevelLength((200 + (i * 12) + (options.getLevelRandSeed() % (i + 1))) % 512);
        options.setLevelType(i % 3);
        options.setLevelRandSeed(options.getLevelRandSeed() + i);
        options.setLevelDifficulty(i / 20);
        options.setGapsCount(i % 3 == 0);
        options.setCannonsCount(i % 3 != 1);
        options.setCoinsCount(i % 5 != 0);
        options.setBlocksCount(i % 4 != 0);
        options.setHiddenBlocksCount(i % 6 != 0);
        options.setDeadEndsCount(i % 10 == 0);
        options.setLevelLadder(i % 10 == 2);
        options.setFrozenCreatures(i % 3 == 1);
        options.setEnemies(i % 4 == 1 ? "off" : "");
        this.reset();
        if (!this.runSingleEpisode(repetitionsOfSingleEpisode))
            difqualifications++;

        updateEvaluationInfo(environment.getEvaluationInfo());

        if (verbose)
            System.out.println(environment.getEvaluationInfoAsString());
    }
}

public EvaluationInfo getEvaluationInfo()
{
    return localEvaluationInfo;
}

public void printStatistics()
{
    System.out.println("\n[MarioAI] ~ Evaluation Results for Task: " + localEvaluationInfo.getTaskName() +
            "\n         Weighted Fitness : " + df.format(localEvaluationInfo.computeWeightedFitness()) +
            "\n             Mario Status : " + localEvaluationInfo.marioStatus +
            "\n               Mario Mode : " + localEvaluationInfo.marioMode +
            "\nCollisions with creatures : " + localEvaluationInfo.collisionsWithCreatures +
            "\n     Passed (Cells, Phys) : " + localEvaluationInfo.distancePassedCells + " of " + localEvaluationInfo.levelLength + ", " + df.format(localEvaluationInfo.distancePassedPhys) + " of " + df.format(localEvaluationInfo.levelLength * 16) + " (" + localEvaluationInfo.distancePassedCells * 100 / localEvaluationInfo.levelLength + "% passed)" +
            "\n Time Spent(marioseconds) : " + localEvaluationInfo.timeSpent +
            "\n  Time Left(marioseconds) : " + localEvaluationInfo.timeLeft +
            "\n             Coins Gained : " + localEvaluationInfo.coinsGained + " of " + localEvaluationInfo.totalNumberOfCoins + " (" + localEvaluationInfo.coinsGained * 100 / (localEvaluationInfo.totalNumberOfCoins == 0 ? 1 : localEvaluationInfo.totalNumberOfCoins) + "% collected)" +
            "\n      Hidden Blocks Found : " + localEvaluationInfo.hiddenBlocksFound + " of " + localEvaluationInfo.totalNumberOfHiddenBlocks + " (" + localEvaluationInfo.hiddenBlocksFound * 100 / (localEvaluationInfo.totalNumberOfHiddenBlocks == 0 ? 1 : localEvaluationInfo.totalNumberOfHiddenBlocks) + "% found)" +
            "\n       Mushrooms Devoured : " + localEvaluationInfo.mushroomsDevoured + " of " + localEvaluationInfo.totalNumberOfMushrooms + " found (" + localEvaluationInfo.mushroomsDevoured * 100 / (localEvaluationInfo.totalNumberOfMushrooms == 0 ? 1 : localEvaluationInfo.totalNumberOfMushrooms) + "% collected)" +
            "\n         Flowers Devoured : " + localEvaluationInfo.flowersDevoured + " of " + localEvaluationInfo.totalNumberOfFlowers + " found (" + localEvaluationInfo.flowersDevoured * 100 / (localEvaluationInfo.totalNumberOfFlowers == 0 ? 1 : localEvaluationInfo.totalNumberOfFlowers) + "% collected)" +
            "\n              kills Total : " + localEvaluationInfo.killsTotal + " of " + localEvaluationInfo.totalNumberOfCreatures + " found (" + localEvaluationInfo.killsTotal * 100 / (localEvaluationInfo.totalNumberOfCreatures == 0 ? 1 : localEvaluationInfo.totalNumberOfCreatures) + "%)" +
            "\n            kills By Fire : " + localEvaluationInfo.killsByFire +
            "\n           kills By Shell : " + localEvaluationInfo.killsByShell +
            "\n           kills By Stomp : " + localEvaluationInfo.killsByStomp +
            "\n        difqualifications : " + difqualifications);
//    System.out.println(localEvaluationInfo.toString());
//    System.out.println("Mario status sum: " + localEvaluationInfo.marioStatus);
//    System.out.println("Mario mode sum: " + localEvaluationInfo.marioMode);
}
}
