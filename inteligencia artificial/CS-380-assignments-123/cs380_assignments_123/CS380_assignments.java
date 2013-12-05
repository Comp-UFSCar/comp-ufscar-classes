package cs380_assignments_123;

import Analyzer.Analyzer;
import Engine.Engine;

/**
 *
 * @author Lucas David
 */
public class CS380_assignments {

    public static final int STARTING_PROBLEM = 0, NUM_PROBLEMS_TO_SOLVE = 11;

    public static void main(String[] args) throws Exception {

        System.out.println("\n* Testing Analyzer.AStart()");

        Engine game = new Engine();

        for (int i = STARTING_PROBLEM; i < STARTING_PROBLEM + NUM_PROBLEMS_TO_SOLVE; i++) {
            game.load("SBP-level" + i);

            new Analyzer(game)
                    .print()
                    .AStart();
            System.out.println("\n---");
        }
        System.out.println();
    }
}
