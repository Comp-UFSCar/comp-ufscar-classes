package ch.idsia.agents.controllers.behavior.conditions;

import ch.idsia.agents.controllers.BehaviorTreeAgent;
import ch.idsia.agents.controllers.behavior.BasicTypes.Task;

/**
 *
 * @author lucasdavid
 */
public class IsAbleToJump extends Task {

    @Override
    public boolean run(boolean[] _action, BehaviorTreeAgent _agent) {
        
        return _agent.ableToJump();
    }

}
