package Engine.movement;

import Engine.movement.Exception.*;

/**
 *
 * @author
 * Lucas
 * David
 */
public class MovementToken {

    EngineMovement movement;
    int piece;

    public MovementToken(int _piece, EngineMovement _movement) {
        piece = 0;
        movement = null;

        if (_piece < 2 || _movement == null) {
            throw new MovementTokenInitializationException();
        }

        piece = _piece;
        movement = _movement;
    }

    public int piece() {
        return piece;
    }

    public EngineMovement movement() {
        return movement;
    }
}
