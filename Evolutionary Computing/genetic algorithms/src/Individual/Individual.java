package Individual;

/**
 *
 * @author lucasdavid
 */
public class Individual {

    // length of sequence of genes in each Individual
    public static int numberOfGenes = 32;
    // mutation factor applied  to individuals during its birth
    public static float mutationFactor = .1f;

    // sequence of genes which represents a solution
    public boolean genes[];

    /**
     * Create a random individual.
     */
    public Individual() {
        genes = new boolean[numberOfGenes];
        
        for (int i = 0; i < genes.length; i++) {
            genes[i] = Math.random() > .5;
        }
    }

    /**
     * Create a new individual with the segments given and random crossing point.
     * 
     * @param _a parent node
     * @param _b parent node
     */
    Individual(Individual _a, Individual _b) {
        this(_a, _b, (int) (Math.random() * _a.genes.length));
    }

    /**
     * Create a new individual with the segments and crossing point given.
     * 
     * @param _a parent node
     * @param _b parent node
     */
    Individual(Individual _a, Individual _b, int _crossingPoint) {
        genes = new boolean[_a.genes.length];
        
        for (int i = 0; i < _a.genes.length; i++) {
            genes[i] = i < _crossingPoint
                     ? _a.genes[i]
                     : _b.genes[i]
                     ;
        }
    }

    /**
     * Mutate individual's genes based on the mutationFactor parameter.
     * 
     * @return this
     */
    Individual mutate() {
        for (int i = 0; i < genes.length; i++) {
            if (Math.random() < mutationFactor) {
                // invert gene if mutation has occurred
                genes[i] = !genes[i];
            }
        }

        return this;
    }

    /**
     * Generate a new individual with the help of a partner.
     * 
     * @param _partner which will generate a new individual
     * 
     * @return Individual
     */
    public Individual cross(Individual _partner) {
        return new Individual(this, _partner).mutate();
    }
    
    @Override
    public String toString() {
        String string = "";
        
        for (boolean gene : genes) {
            string += gene
                    ? "1 "
                    : "0 "
                    ;
        }
        
        return string;
    }
}
