package assignment_5;

import FileHandler.FileHandler;
import GeneticAlgorithm.*;

/**
 *
 * @author lucasdavid
 */
public class GeneticAnalyzer {

    int[][] matrix;

    public GeneticAnalyzer run() throws Exception {

        System.out.println("GeneticAnalyzer@run() method called.");

        FileHandler file = new FileHandler("30cities.txt");
        matrix = file.read();

        GeneticAlgorithm algorithm = new GeneticAlgorithm();

        algorithm
                .evaluation(new Evaluable() {

                    /**
                     * cross over with "2-opt"-ing.
                     */
                    @Override
                    public int[] cross(int[] _parentA, int[] _parentB) {
                        int[] child = _parentA.clone();

                        // select a random gene, ignore the first and the last one
                        int randomGene = (int) (Math.random() * (child.length - 2) + 1);
                        int temp = child[randomGene];

                        child[randomGene] = child[randomGene + 1];
                        child[randomGene + 1] = temp;

                        return child;
                    }
//                    /**
//                     * Crossover with reversion of items between cutoffs.
//                     */
//                    @Override
//                    public int[] cross(int[] _parentA, int[] _parentB) {
//                        int[] child = _parentA.clone();
//
//                        int start, end;
//
//                        start = (int) (Math.random() * child.length);
//                        end = (int) (Math.random() * (child.length - start) + start);
//
//                        for (int i = 0; i <= (end - start) / 2; i++) {
//                            int temp = child[start + i];
//                            child[start + i] = child[end - i];
//                            child[end - i] = temp;
//                        }
//
//                        return child;
//                    }

                    /**
                     * Evaluate the cost of a Hamiltonian circuit _indvididual.
                     *
                     * @param _individual
                     * @return int
                     */
                    @Override
                    public int f(int[] _individual) {

                        int cost = 0;
                        int previous = _individual[0];

                        for (int i = 1; i < _individual.length; i++) {
                            int current = _individual[i];

                            cost += matrix[previous][current];
                            previous = current;
                        }

                        return cost + matrix[previous][_individual[0]];
                    }

                    /**
                     * Mutate a solution candidate.
                     *
                     * @param _individual
                     * @param _mutation
                     *
                     * @return int[]
                     */
                    @Override
                    public int[] mutate(int[] _individual, float _mutation) {

                        for (int gene = 1; gene < _individual.length; gene++) {
                            // if mutation occurs, gene is exchanged with a random gene
                            if (Math.random() < _mutation) {
                                // finds a random index, excluding the zero
                                int randomIndex = (int) (Math.random() * (_individual.length - 1) + 1);

                                // swap genes
                                int temp = _individual[gene];
                                _individual[gene] = _individual[randomIndex];
                                _individual[randomIndex] = temp;
                            }
                        }

                        return _individual;
                    }
                })
                .populate(20, matrix.length)
                .showIndividuals()
                .time(10000)
                .mutation(0)
                .run();

        System.out.println("The following solution was found after " + algorithm.iteractions() + " interactions: ");
        int[] fittest = algorithm.fittest();

        for (int i = 0; i < fittest.length; i++) {
            System.out.print(fittest[i] + " ");
        }

        System.out.println("\nWith fitness factor " + algorithm.fitness() + "\n");

        return this;
    }
}
