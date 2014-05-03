package ch.idsia.agents.controllers.behavior.conditions;

import ch.idsia.agents.controllers.BehaviorTreeAgent;
import ch.idsia.agents.controllers.behavior.BasicTypes.Task;

/**
 *
 * @author lucasdavid
 */
public class NearFloatingPlatform extends Task {

    @Override
    public boolean run(boolean[] _action, BehaviorTreeAgent _agent) {
        
        return _agent.getReceptiveFieldCellValue(_agent.marioEgoRow() + 2, _agent.marioEgoCol()) != 0
                && _agent.getReceptiveFieldCellValue(_agent.marioEgoRow(), _agent.marioEgoCol()) != 0;
    }

}
