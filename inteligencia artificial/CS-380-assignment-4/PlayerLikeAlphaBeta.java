package Assignment_4;

import java.util.List;

/**
 *
 * @author lucasdavid
 */
public class PlayerLikeAlphaBeta extends PlayerLikeABoss {

    @Override
    public OthelloMove getMove(OthelloState _state) {
        maximize = _state.nextPlayerToMove == OthelloState.PLAYER1;
        
        OthelloMove best = null;
        int bestCost = maximize ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        // retrieve the score of each movement and decide which one is the best based on the value of the variable maximize
        for (OthelloMove move : _state.generateMoves()) {
            int current = alphaBeta(_state.applyMoveCloning(move), depth - 1, Integer.MIN_VALUE, Integer.MAX_VALUE, !maximize);

            // if the agent is maximizing scores and the current score is grater than the previous reached
            // or the agent is minimizing scores and the current score is less than the previous,
            // update the movement.
            if (maximize && current > bestCost || !maximize && current < bestCost) {
                bestCost = current;
                best = move;
            }
        }

        return best;
    }

    /**
     * Reference: http://en.wikipedia.org/wiki/Alpha%E2%80%93beta_pruning
     */
    int alphaBeta(OthelloState _state, int _depth, int _a, int _b, boolean _maximize) {
        List<OthelloMove> moves = _state.generateMoves();

        if (_depth == 0 || moves.isEmpty()) {
            return _state.score();
        }

        for (OthelloMove move : moves) {
            int current = alphaBeta(_state.applyMoveCloning(move), _depth - 1, _a, _b, !_maximize);

            if (_maximize) {
                if (current > _a) {
                    _a = current;
                }
            } else {
                if (current < _b) {
                    _b = current;
                }
            }

            if (_b <= _a) {
                break;
            }
        }

        return _maximize
               ? _a
               : _b
               ;
    }
}
