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

package ch.idsia.evolution.ea;

import ch.idsia.agents.Agent;
import ch.idsia.benchmark.tasks.Task;
import ch.idsia.evolution.EA;
import ch.idsia.evolution.Evolvable;

/**
 * Created by IntelliJ IDEA.
 * User: julian
 * Date: Apr 29, 2009
 * Time: 12:16:49 PM
 */
public class ES implements EA
{

private final Evolvable[] population;
private final float[] fitness;
private final int elite;
private final Task task;
private final int evaluationRepetitions = 1;

public ES(Task task, Evolvable initial, int populationSize)
{
    this.population = new Evolvable[populationSize];
    for (int i = 0; i < population.length; i++)
    {
        population[i] = initial.getNewInstance();
    }
    this.fitness = new float[populationSize];
    this.elite = populationSize / 2;
    this.task = task;
}

public void nextGeneration()
{
    for (int i = 0; i < elite; i++)
    {
        evaluate(i);
    }
    for (int i = elite; i < population.length; i++)
    {
        population[i] = population[i - elite].copy();
        population[i].mutate();
        evaluate(i);
    }
    shuffle();
    sortPopulationByFitness();
}

private void evaluate(int which)
{
    fitness[which] = 0;
    for (int i = 0; i < evaluationRepetitions; i++)
    {
        population[which].reset();
        fitness[which] += task.evaluate((Agent) population[which]);
//            System.out.println("which " + which + " fitness " + fitness[which]);
    }
    fitness[which] = fitness[which] / evaluationRepetitions;
}

private void shuffle()
{
    for (int i = 0; i < population.length; i++)
    {
        swap(i, (int) (Math.random() * population.length));
    }
}

private void sortPopulationByFitness()
{
    for (int i = 0; i < population.length; i++)
    {
        for (int j = i + 1; j < population.length; j++)
        {
            if (fitness[i] < fitness[j])
            {
                swap(i, j);
            }
        }
    }
}

private void swap(int i, int j)
{
    float cache = fitness[i];
    fitness[i] = fitness[j];
    fitness[j] = cache;
    Evolvable gcache = population[i];
    population[i] = population[j];
    population[j] = gcache;
}

public Evolvable[] getBests()
{
    return new Evolvable[]{population[0]};
}

public float[] getBestFitnesses()
{
    return new float[]{fitness[0]};
}

}
