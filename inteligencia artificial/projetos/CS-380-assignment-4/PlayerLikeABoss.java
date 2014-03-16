package Assignment_4;

import java.util.List;

/**
 *
 * @author lucasdavid
 */
public class PlayerLikeABoss extends OthelloPlayer {

    int depth = 5;
    boolean maximize = true;

    public int depth() {
        return depth;
    }

    public final PlayerLikeABoss depth(int _depth) {
        depth = _depth > 0
                ? _depth
                : 5;

        return this;
    }

    public OthelloMove getMove(OthelloState _state, int _depth) {
        return depth(_depth).getMove(_state);
    }

    @Override
    public OthelloMove getMove(OthelloState _state) {
        maximize = _state.nextPlayerToMove == OthelloState.PLAYER1;

        OthelloMove bestMove = null;
        int bestCost = maximize ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        // list all current possible moves. The returned move will be one of them.
        // for each listed move...
        for (OthelloMove move : _state.generateMoves()) {
            int currentCost = miniMax(_state.applyMoveCloning(move), depth - 1, !maximize);

            // if the agent is maximizing scores and the current score is grater than the previous reached
            // or the agent is minimizing scores and the current score is less than the previous,
            // update the movement.
            if (maximize && currentCost > bestCost || !maximize && currentCost < bestCost) {
                bestCost = currentCost;
                bestMove = move;
            }
        }

        return bestMove;
    }

    /**
     * References: http://en.wikipedia.org/wiki/Minimax
     */
    private int miniMax(OthelloState _state, int _depth, boolean _maximize) {
        List<OthelloMove> moves;

        // if depth constraint was violated or current state is a leaf (there is no possible movements)
        if (0 == _depth || (moves = _state.generateMoves()).isEmpty()) {
            return _state.score();
        }

        int best = _maximize ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        // find the cost of the best subpath in the current state
        for (OthelloMove move : moves) {
            int current = miniMax(_state.applyMoveCloning(move), _depth - 1, !_maximize);

            if (_maximize && current > best || !_maximize && current < best) {
                best = current;
            }
        }

        return best;
    }
}
