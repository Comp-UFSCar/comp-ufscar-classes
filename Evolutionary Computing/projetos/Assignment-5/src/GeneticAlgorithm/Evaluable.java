package GeneticAlgorithm;

/**
 *
 * @author lucasdavid
 */
public interface Evaluable {

    public int[] cross(int[] _parentA, int[] _parentB);

    public int[] mutate(int[] _individual, float _mutation);

    public int f(int[] _individual);

}
