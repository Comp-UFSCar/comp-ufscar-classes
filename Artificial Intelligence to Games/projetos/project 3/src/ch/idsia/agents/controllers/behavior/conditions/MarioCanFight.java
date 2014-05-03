package ch.idsia.agents.controllers.behavior.conditions;

import ch.idsia.agents.controllers.BehaviorTreeAgent;
import ch.idsia.agents.controllers.behavior.BasicTypes.Task;

/**
 *
 * @author lucasdavid
 */
public class MarioCanFight extends Task {

    @Override
    public boolean run(boolean[] _action, BehaviorTreeAgent _agent) {
        
        // Mario is in fire-flower mode, which implies he can throw fire balls.
        return _agent.mode() == 2;
    }
}
