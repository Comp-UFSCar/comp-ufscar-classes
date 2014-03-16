package GeneticAlgorithm;

import java.util.ArrayList;

/**
 *
 * @author lucasdavid
 */
public final class GeneticAlgorithm {

    public static final int INITIAL_POPULATION = 200;
    public static final int NUMBER_OF_GENES = 128;
    public static final float MUTATION_FACTOR = .1f;
    public static final long TIME_CONSTRAINT = 2000;

    ArrayList<int[]> population;
    int iteractions;

    int fitness;
    int[] fittest;

    long timeConstraint;
    float mutation;
    boolean maximize;
    Evaluable evaluation;

    public GeneticAlgorithm() {
        timeConstraint = TIME_CONSTRAINT;
        mutation = MUTATION_FACTOR;
    }

    public int[] run() {
        System.out.println("\nGeneticAlgorithm@run()");

        if (mutation < 0 || timeConstraint <= 0 || population == null || population.isEmpty()) {
            throw new RuntimeException("illegal attributes for current GeneticAlgorithm instance");
        }

        long execution = System.currentTimeMillis();

        iteractions = 0;

        while (execution + timeConstraint > System.currentTimeMillis()) {
            evolve();
        }

        return fittest();
    }

    void evolve() {
        iteractions++;

        int index = 0;

        // for each pair of individuals, cross them
        for (int[] parent : population) {
            // cross parents
            int[] child = evaluation.cross(parent, null);

            // mutate child
            if (mutation > 0) {
                child = evaluation.mutate(child, mutation);
            }

            // kill weakest in the family
            int evalParent, evalChild;

            evalParent = evaluation.f(parent);
            evalChild = evaluation.f(child);

            // if child is stronger than parent, kill parent
            if (maximize && evalParent > evalChild || !maximize && evalParent < evalChild) {
                population.set(index, child);
            }

            // check if child is the strongest individual in the population
            if (maximize && evalChild > fitness || !maximize && evalChild < fitness) {
                fittest = child;
                fitness = evalChild;
            }

            index++;
        }
    }

    public GeneticAlgorithm populate(int _genes) {
        return populate(INITIAL_POPULATION, _genes);
    }

    public GeneticAlgorithm populate(int _population, int _genes) {
        population = new ArrayList<>();

        fittest = null;
        fitness = Integer.MAX_VALUE;

        for (int i = 0; i < _population; i++) {
            int[] individual = new int[_genes];

            for (int gene = 0; gene < individual.length; gene++) {
                individual[gene] = gene;
            }

            for (int gene = 1; gene < individual.length; gene++) {
                int randomIndex = (int) (Math.random() * (individual.length - 1) + 1);
                int temp = individual[gene];

                individual[gene] = individual[randomIndex];
                individual[randomIndex] = temp;
            }

            // check if child is the strongest individual in the population
            int current = evaluation.f(individual);
            if (maximize && current > fitness || !maximize && current < fitness) {
                fittest = individual;
                fitness = current;
            }

            population.add(individual);
        }

        return this;
    }

    public GeneticAlgorithm evaluation(Evaluable _evaluation) {
        evaluation = _evaluation;
        return this;
    }

    public GeneticAlgorithm time(long _timeConstraint) {
        timeConstraint = _timeConstraint;
        return this;
    }

    public GeneticAlgorithm mutation(float _mutation) {

        System.out.println("GeneticAlgorithm@mutation : " + _mutation);

        mutation = _mutation;
        return this;
    }

    public GeneticAlgorithm maximize(boolean _maximize) {

        System.out.println("GeneticAlgorithm@maximize : " + _maximize);

        maximize = _maximize;
        return this;
    }

    public GeneticAlgorithm showIndividuals() throws RuntimeException {
        if (population == null) {
            throw new RuntimeException("GeneticAlgorithm@showIndividuals requires an instantiated population");
        }

        System.out.println("GeneticAlgorithm@showIndividuals()");

        for (int[] individual : population) {
            for (int gene : individual) {
                System.out.print(gene + " ");
            }
            System.out.println();
        }

        return this;
    }

    public int[] fittest() {
        return fittest.clone();
    }

    public int fitness() {
        return fitness;
    }

    public long time() {
        return timeConstraint;
    }

    public int iteractions() {
        return iteractions;
    }

    public Evaluable evaluation() {
        return evaluation;
    }

}
