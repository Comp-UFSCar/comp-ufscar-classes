package Assignment_4;

import java.util.List;

/**
 *
 * @author lucasdavid
 */
public class AlphaBetaThread extends Thread {

    static int score;
    static OthelloMove best;
    static boolean interrupted;

    OthelloMove moveAssigned;

    static OthelloState root;
    static boolean maximize;
    static int depth;
    static long initialExecutionTime;
    static long maximumExecutionTime;
    
    public AlphaBetaThread(OthelloMove _move) {
        moveAssigned = _move;
    }

    @Override
    public void run() {
        if (maximize) {
            int currentCost = innerAlgorithm(root.applyMoveCloning(moveAssigned), depth - 1, Integer.MIN_VALUE, Integer.MAX_VALUE, false);

            synchronized (this) {
                if (score < currentCost) {
                    score = currentCost;
                    best = moveAssigned;
                }
            }
        } else {
            int currentCost = innerAlgorithm(root.applyMoveCloning(moveAssigned), depth - 1, Integer.MIN_VALUE, Integer.MAX_VALUE, true);

            synchronized (this) {
                if (score > currentCost) {
                    score = currentCost;
                    best = moveAssigned;
                }
            }
        }
    }

    /**
     * Alpha-Beta pruning Reference: http://en.wikipedia.org/wiki/Alpha%E2%80%93beta_pruning
     */
    int innerAlgorithm(OthelloState _state, int _depth, int _a, int _b, boolean _maximize) {
        // execution time constraint reached. Return current solution
        if (initialExecutionTime + maximumExecutionTime <= System.nanoTime()) {
            interrupted = true;
            return _state.score();
        }
        
        List<OthelloMove> moves = _state.generateMoves();

        if (_depth == 0 || moves.isEmpty()) {
            return _state.score();
        }

        if (_maximize) {
            for (OthelloMove move : moves) {
                int current = innerAlgorithm(_state.applyMoveCloning(move), _depth - 1, _a, _b, false);

                if (current > _a) {
                    _a = current;
                }
                if (_b <= _a) {
                    break;
                }
            }
            return _a;
        } else {
            for (OthelloMove move : moves) {
                int current = innerAlgorithm(_state.applyMoveCloning(move), _depth - 1, _a, _b, true);

                if (current < _b) {
                    _b = current;
                }
                if (_b <= _a) {
                    break;
                }
            }
            return _b;
        }
    }
}
