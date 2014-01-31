package Population;

import Individual.Individual;
import java.util.LinkedList;

/**
 *
 * @author lucasdavid
 */
public class Population {

    public static final int DEFAULT_NUMBER_OF_INDIVIDUALS = 128;

    int populationStandardSize;
    LinkedList<Individual> individuals;
    LinkedList<Individual> unfitables;
    Individual fittest;

    /**
     * Create a population with a default number of individuals.
     */
    public Population() {
        this(DEFAULT_NUMBER_OF_INDIVIDUALS);
    }

    /**
     * Create a population with N individuals.
     *
     * @param _individuals
     */
    public Population(int _individuals) {
        populationStandardSize = _individuals;
        individuals = new LinkedList<>();
        unfitables = new LinkedList<>();

        for (int i = 0; i < _individuals; i++) {
            Individual individual = new Individual();
            individuals.add(individual);

            fittest(individual);
        }
    }

    /**
     * Generate offspring of pairs of individuals in the current population.
     *
     * @return this
     */
    public Population offspring() {
        for (int i = 0; i < populationStandardSize; i += 2) {
            Individual A, B, child;

            A = individuals.get(i);
            B = individuals.get(i + 1);

            // add child to the population
            individuals.add(child = A.cross(B));

            // identify less suitable individual an add him to unfitables list
            if (f(A) <= f(child) && f(A) <= f(B)) {
                unfitables.add(A);
            } else if (f(B) <= f(child) && f(B) <= f(A)) {
                unfitables.add(B);
            } else {
                unfitables.add(child);
            }

            // verify if child is the new fittest individual
            fittest(child);
        }

        return this;
    }

    /**
     * Remove less suitable individuals from population.
     *
     * @return this
     */
    public Population filter() {
        individuals.removeAll(unfitables);
        unfitables.clear();

        return this;
    }

    /**
     *
     * @return
     */
    public Individual fittest() {
        return fittest;
    }

    /**
     *
     * @return fitness of the fittest individual
     */
    public float fitness() {
        return f(fittest);
    }

    /**
     * Compare an given individual with the current fittest, and replace it when f(current) is greater than f(fittest).
     *
     * @param _individual
     *
     * @return Individual
     */
    final Individual fittest(Individual _individual) {
        try {
            if (f(_individual) > f(fittest)) {
                fittest = _individual;
            }
        } catch (NullPointerException e) {
            fittest = _individual;
        }

        return _individual;
    }

    static float f(Individual _individual) {
        float fitness = 0;

        for (boolean gene : _individual.genes) {
            if (gene) {
                fitness += 1;
            }
        }

        return fitness /= _individual.genes.length;
    }
}
