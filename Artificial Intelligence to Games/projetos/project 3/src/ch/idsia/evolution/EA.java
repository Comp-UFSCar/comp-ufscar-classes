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

package ch.idsia.evolution;

/**
 * Interface to a Generic Evolutionary Algorithm.<br>
 * The contructor simply needs an <code>{@link Evolvable}</code>,
 * a Task (depending on which the evaluations will run in series, in parallel or in different threads)
 * and possibly few other paremeters.
 * <p/>
 * All the <code>EA</code> must assume that the best individual is the first one in ascending order,
 * where the ordering is produced by sorting according to the <code>compareTo</code> method,
 * of the individual's fitness. In most of the cases ordering according to max fitness is assumed.
 * <p/>
 * <p/>
 * <p/>
 * <p/>
 * Is STRONGLY suggested to have a <code>public static void main(String args[])</code>
 * method inside which a SELF CONTAINED bare bone example using an <code>EvolvableArray</code>
 * should be implemented. <br>
 * The class SSIndividual might be used to simplify the implementation of the algorithm,
 * however it should not be exposed in any way.
 *
 * @see Evolvable
 */

public interface EA
{

/**
 * Returns the best <code>Evolvables</code> of the population (e.g. the elite or the
 * Pareto Set). The array may have only one element (e.g. for a hill climber).
 * <br>
 * Is not a copy of the bests!!!
 * <br>
 * If you really want only the best guy, in the majority of the cases, is simply
 * possible to pick the first guy of the set <code>ES.getBests()[0]</code>.
 * This does not make ANY SENSE when there is not an absolute ordering between
 * them (e.g. Pareto set).
 *
 * @return array of <code>Evolvables</code> the array may have only one element.
 */
public Evolvable[] getBests();

/**
 * Returns the best <code>fitnesses</code> of the population (e.g. of the the elite or
 * of the Pareto Set).
 *
 * @return array of <code>fitnesses</code> the array may have only one element.
 */
public float[] getBestFitnesses();

/**
 * Steps the <code>EA</code> one generation forward  the full loop is executed:
 * <ul>
 * <li> generates a population of <code>Evolvables</code>,
 * <li> resets the evolvable
 * <li> runs the <code>Task</code> with each of the <code>Evolvables</code>,
 * <li> computes the fitnesses (e.g. averaing),
 * <li> ranks and selects the <code>Evolvables</code>.
 * </ul>
 * <p/>
 * An exception is generated if the number of objective in the task
 * does not agree with the specific ea.
 *
 * @throws Exception, any Exception, if anything goes wrong
 */
public void nextGeneration() throws Exception;

/**
 * Returns a description of this Evolutionary algorithm, as a good practise,
 * all the specific parameters of the tasks should be printed as well.
 *
 * @return tasks information.
 */
public String toString();
}
