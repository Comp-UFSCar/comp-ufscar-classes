package Assignment_4;

import java.util.List;

/**
 *
 * @author lucasdavid
 * 
 * I just created this concurrent MiniMax for fun. It is not used in any other place.
 */
public class MiniMaxThread extends Thread {

    OthelloState root;
    List<OthelloMove> moves;

    int depth;
    boolean maximize;
    
    static int score;
    static OthelloMove best;

    public MiniMaxThread(OthelloState _state, List<OthelloMove> _moves, boolean _maximize, int _depth) {
        root = _state;
        moves = _moves;
        maximize = _maximize;
        depth = _depth;
    }

    @Override
    public void run() {
        if (maximize) {
            score = Integer.MIN_VALUE;
            for (OthelloMove move : moves) {
                int currentCost = innerAlgorithm(root.applyMoveCloning(move), depth - 1, false);

                if (score < currentCost) {
                    score = currentCost;
                    best = move;
                }
            }
        } else {
            score = Integer.MAX_VALUE;
            for (OthelloMove move : moves) {
                int currentCost = innerAlgorithm(root.applyMoveCloning(move), depth - 1, true);

                if (score > currentCost) {
                    score = currentCost;
                    best = move;
                }
            }
        }

    }

    /**
     * Minimax Algorithm References: http://en.wikipedia.org/wiki/Minimax
     */
    int innerAlgorithm(OthelloState _state, int _depth, boolean _maximize) {
        List<OthelloMove> moves;

        if (0 == _depth || (moves = _state.generateMoves()).isEmpty()) {
            return _state.score();
        }

        int best;

        if (_maximize) {
            best = Integer.MIN_VALUE;
            for (OthelloMove move : moves) {
                int current = innerAlgorithm(_state.applyMoveCloning(move), _depth - 1, false);

                if (current > best) {
                    best = current;
                }
            }
        } else {
            best = Integer.MAX_VALUE;
            for (OthelloMove move : moves) {
                int current = innerAlgorithm(_state.applyMoveCloning(move), _depth - 1, true);

                if (current < best) {
                    best = current;
                }
            }
        }

        return best;
    }
}
