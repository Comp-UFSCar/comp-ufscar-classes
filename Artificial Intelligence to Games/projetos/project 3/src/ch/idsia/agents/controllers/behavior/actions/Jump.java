package ch.idsia.agents.controllers.behavior.actions;

import ch.idsia.agents.controllers.BehaviorTreeAgent;
import ch.idsia.agents.controllers.behavior.BasicTypes.Task;
import ch.idsia.benchmark.mario.engine.sprites.Mario;

/**
 *
 * @author lucasdavid
 */
public class Jump extends Task {

    @Override
    public boolean run(boolean[] _action, BehaviorTreeAgent _agent) {
        
        return _action[Mario.KEY_JUMP] = true;
    }
    
    
}
