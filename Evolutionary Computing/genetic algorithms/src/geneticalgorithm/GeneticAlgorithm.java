package geneticalgorithm;

import Individual.Individual;
import Population.Population;

/**
 *
 * @author lucasdavid
 */
public class GeneticAlgorithm {

    public static final float ACCEPTABLE_FITNESS = 1f;
    public static final long MAX_EXECUTION_TIME = 2000;

    static Population population;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        long execution = System.currentTimeMillis();

        Individual.mutationFactor = .01f;
        Individual.numberOfGenes = 128;

        population = new Population();

        while (population.fitness() < ACCEPTABLE_FITNESS
                && System.currentTimeMillis() < execution + MAX_EXECUTION_TIME) {
            population.offspring().filter();
        }

        System.out.println("Fittest solution: " + population.fittest() + "\n"
                + "Solution optimality: " + population.fitness());
    }
}
