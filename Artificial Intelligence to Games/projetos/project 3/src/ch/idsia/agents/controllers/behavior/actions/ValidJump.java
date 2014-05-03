package ch.idsia.agents.controllers.behavior.actions;

import ch.idsia.agents.controllers.BehaviorTreeAgent;
import ch.idsia.agents.controllers.behavior.BasicTypes.Task;
import ch.idsia.benchmark.mario.engine.sprites.Mario;

/**
 *
 * @author lucasdavid
 */
public class ValidJump extends Task {

    int count;

    @Override
    public boolean run(boolean[] _action, BehaviorTreeAgent _agent) {

        if (_action[Mario.KEY_JUMP] && ++count > 16) {
            count = 0;
            _action[Mario.KEY_JUMP] = false;
        }
        return true;
    }

}
