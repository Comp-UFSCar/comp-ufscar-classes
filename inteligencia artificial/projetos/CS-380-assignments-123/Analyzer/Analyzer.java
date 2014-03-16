package Analyzer;

import Engine.Engine;
import Engine.movement.MovementToken;
import Analyzer.Exception.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;

/**
 *
 * @author Lucas David
 */
public class Analyzer {

    Engine game;
    int solvedCheckings;
    int depth;
    int nodes;
    LinkedList<MovementToken> steps; // required steps to solve Engine game.

    public Analyzer(Engine _game) {
        if (_game == null) {
            throw new InvalidGameLoadedException();
        }

        game = _game;
    }

    /**
     * Print current matrix of loaded game.
     *
     * #return this
     */
    public Analyzer print() {
        game.print();
        System.out.println();
        return this;
    }

    /**
     * Reset statistics of current analyzes.
     *
     */
    void reset() {
        solvedCheckings = depth = nodes = 0;
        steps = new LinkedList<>();
        // requests garbage collection.
        System.gc();
    }

    Analyzer printSolvedMessage(Engine _configuration) {
        printSolvedMessage(_configuration.solved());
        _configuration.print();

        return this;
    }

    Analyzer printSolvedMessage(boolean _solved) {
        if (!_solved) {
            System.out.print("Problem unsolved, ");
        } else {
            MovementToken step;
            int last = steps.size();

            System.out.println("Problem solved with the following steps:");

            while (last > 0) {
                step = steps.remove(--last);
                System.out.println("(" + step.piece() + ", " + step.movement() + ")");
            }
        }

        System.out.println(solvedCheckings + " calls of the method solved() were required.\n"
                + depth + " was the recursion tree's depth.\n"
                + nodes + " was the number of nodes created.");
        return this;
    }

    /**
     * Find a solution for the game using "Breadth Tree style" recursion.
     *
     * #return this
     */
    public Analyzer breadthFirstSearch() {
        reset();

        LinkedList<Engine> configurations = new LinkedList<>();
        configurations.add(game);

        return printSolvedMessage(breadthFirstSearch(configurations));
    }

    /**
     * Find a solution for the game using "Breadth Tree style" recursion.
     *
     * @return instance of <Engine>, a state, solved or not.
     */
    Engine breadthFirstSearch(LinkedList<Engine> _configurations) {
        Engine current = _configurations.remove(0);

        solvedCheckings++;
        if (current.normalize().solved()) {
            return current;
        }

        for (MovementToken movement : current.allMovements()) {
            nodes++;
            _configurations.add(current.applyMoveClonning(movement));
        }

        if (_configurations.size() > 0) {
            return breadthFirstSearch(_configurations);
        }

        return current;
    }

    /**
     * Find a solution for the game using "Depth Tree style" recursion.
     *
     * #return this.
     */
    public Analyzer depthFirstSearch() {
        reset();
        return printSolvedMessage(depthFirstSearch(game));
    }

    /**
     * Find a solution for the game using "Depth Tree style" recursion.
     *
     * @return instance of <Engine>, a state, solved or not.
     */
    Engine depthFirstSearch(Engine _configuration) {
        depth++;
        solvedCheckings++;
        if (_configuration.normalize().solved()) {
            // a solution was found in this branch, return a confirmation.
            return _configuration;
        }

        // the current configuration isn't solved. For all movements of it,
        for (MovementToken movement : _configuration.allMovements()) {
            nodes++;
            // and for all movements of it's sons, recursivelly, verify if they are solved
            Engine child = depthFirstSearch(_configuration.applyMoveClonning(movement));

            solvedCheckings++;
            if (child.solved()) {
                steps.add(movement);
                return child;
            }
        }

        // there are no movements left, the game cannot be solved by this branch
        depth--;
        return _configuration;
    }

    public Analyzer interactiveDeepeningDepthFirstSearch() {
        int localDepth = 1;
        Engine configuration;

        do {
            reset();
            configuration = depthLimitedSearch(game, depth = localDepth++);
        } while (!configuration.solved());

        return printSolvedMessage(configuration);
    }

    Engine depthLimitedSearch(Engine _configuration, int _depth) {
        depth++;
        solvedCheckings++;
        // normalized game is solved or depth constraint failed
        if (_configuration.normalize().solved() || _depth == 0) {
            return _configuration;
        }

        for (MovementToken movement : _configuration.allMovements()) {
            nodes++;
            Engine child = depthLimitedSearch(_configuration.applyMoveClonning(movement), _depth - 1);

            solvedCheckings++;
            if (child.solved()) {
                steps.add(movement);
                return child;
            }
        }

        depth--;
        return _configuration;
    }

    /**
     * Walk randomly through the possible movements trying to figure out the solution.
     *
     * @param _n superior limiter of interactions. #return this
     */
    public Analyzer randomWalk(int _n) {
        reset();
        int i = 0;

        do {
            game.normalize();

            // get all possible movements
            Set<MovementToken> movements = game.allMovements();
            // extracts any movement
            MovementToken movement = (MovementToken) movements.toArray()[(int) Math.random() * movements.size()];

            // apply a random movement contained in all the possible movements.
            // we are inserting the movement at the first position of the list
            // because it will be inverted at the method printSolvedMessage()
            steps.add(0, movement);
            game.applyMove(movement);

            solvedCheckings++;
        } while (++i < _n && !game.solved());

        return printSolvedMessage(game.solved());
    }

    /**
     * Finds a solution to the problem using A* algorithm.
     *
     * @return this
     */
    public Analyzer AStart() {
        reset();

        HashMap<Engine, Integer> opened = new HashMap<>();
        HashSet<Engine> closed = new HashSet<>();

        game.h(0);
        game.h(AStartManhattanDistanceHeuristic());

        opened.put(game, game.f());

        while (opened.size() > 0) {
            Engine nearest = null;

            Iterator<Engine> keySetIterator = opened.keySet().iterator();
            while (keySetIterator.hasNext()) {
                Engine current = keySetIterator.next();

                if (nearest == null || nearest.f() > current.f()) {
                    nearest = current;
                }
            }

            opened.remove(nearest);
            closed.add(nearest);

            solvedCheckings++;
            if (nearest.solved()) {
                return printSolvedMessage(AStartPathToSolution(nearest));
            }

            // for every possible move of the current node
            for (MovementToken _m : nearest.allMovements()) {
                // apply movement
                Engine child = nearest
                        .applyMoveClonning(_m)
                        .normalize();

                // if child was already visited, ignore it
                if (closed.contains(child)) {
                    continue;
                }

                nodes++;

                // calculate f(child) = g(current) + 1 + h(child)
                child.g(nearest.g() + 1);
                child.h(AStartManhattanDistanceHeuristic());

//                // That is only necessary if the costs are not uniform (it is not the case)
//                // relaxing edge
//                try {
//                    // child was already reached by other path, which is longer than the current one, change its f()
//                    // and its parent by changing the child object.
//                    if (opened.get(child) > child.f()) {
//                        opened.remove(child);
                opened.put(child, child.f());
//                    }
//                } catch (NullPointerException e) {
//                    // the child hadn't been reached yet. Add it to the opened set.
//                    opened.put(child, child.f());
//                }
            }
        }

        return printSolvedMessage(game.solved());
    }

    int AStartManhattanDistanceHeuristic() {
        // if the main piece is not completely over the exit, return distance to do so. Return zero otherwise.
        try {
            int[] a = game.fieldsOf(Engine.MAIN_PIECE).remove(0);
            int[] b = game.fieldsOf(Engine.EXIT).remove(0);

            return Math.abs(a[0] - b[0]) + Math.abs(a[1] - b[1]);
        } catch (IndexOutOfBoundsException e) {
            return 0;
        }
    }

    int AStarModifiedHeuristic() {
        // if the main piece is not completely over the exit, return distance to do so. Return zero otherwise.    
        LinkedList<int[]> emptyFields = game.fieldsOf(Engine.EMPTY_FIELD);
        LinkedList<int[]> mainFields = game.fieldsOf(Engine.MAIN_PIECE);
        LinkedList<int[]> exitFields = game.fieldsOf(Engine.EXIT);

		// the exit is completly covered by the main piece
        if (exitFields.size() == 0) {
        	return 0;
        }

        int[] a = mainFields.get(0), b = exitFields.get(0);
        int distance = Math.abs(a[0] - b[0]) + Math.abs(a[1] - b[1]);

        return distance + distance / (emptyFields.size() / mainFields.size() + 1);
    }

    Engine AStartPathToSolution(Engine _game) {
        Engine current = _game;
        while (current.parent() != null) {
            steps.add(current.actionTakenByParent());
            current = current.parent();
        }
        return _game;
    }
}
