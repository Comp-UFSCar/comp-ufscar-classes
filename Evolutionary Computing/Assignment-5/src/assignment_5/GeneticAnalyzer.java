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

        int value = 0;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = i + 1; j < matrix[i].length; j++) {
                value += matrix[i][j];
            }
        }
        System.out.println("Distances mean : " + value / matrix.length);

        value = 0;
        try {
            for (int i = 1; i < matrix.length; i++) {
                value += matrix[i][i + 1];
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            value += matrix[matrix.length - 1][0];
        }
        System.out.println("Cost of default sequencial path : " + value);

        GeneticAlgorithm algorithm = new GeneticAlgorithm();

        algorithm
                .evaluation(new Evaluable() {

//                    /**
//                     * cross over with "2-opt"-ing.
//                     */
//                    @Override
//                    public int[] cross(int[] _parentA, int[] _parentB) {
//                        int[] child = _parentA.clone();
//
//                        int randomGene = (int) (Math.random() * (child.length - 2) + 1);
//                        int temp = child[randomGene];
//                        try {
//                            child[randomGene] = child[randomGene + 1];
//                            child[randomGene + 1] = temp;
//                        } catch (ArrayIndexOutOfBoundsException e) {
//                            child[randomGene] = child[0];
//                            child[0] = temp;
//                        }
//
//                        return child;
//                    }
                    /**
                     * Crossover with reversion of items between cutoffs.
                     */
                    @Override
                    public int[] cross(int[] _parentA, int[] _parentB) {
                        int[] child = _parentA.clone();

                        int start, end;

                        start = (int) (Math.random() * child.length);
                        end = (int) (Math.random() * (child.length - start) + start);

                        for (int i = 0; i <= (end - start) / 2; i++) {
                            int temp = child[start + i];
                            child[start + i] = child[end - i];
                            child[end - i] = temp;
                        }

                        return child;
                    }

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
                })
                .populate(10, matrix.length)
                .showIndividuals()
                .mutation(0f)
                .time(5000)
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
