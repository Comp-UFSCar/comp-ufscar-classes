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

        return _agent.grounded() && _agent.getReceptiveFieldCellValue(_agent.marioEgoRow() + 1, _agent.marioEgoCol() + 1) != 0
                || !_agent.grounded() && _agent.getReceptiveFieldCellValue(_agent.marioEgoRow() + 1, _agent.marioEgoCol()) != 0;
    }

}
