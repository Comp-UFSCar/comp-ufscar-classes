/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.idsia.agents.controllers.behavior.conditions;

import ch.idsia.agents.controllers.BehaviorTreeAgent;
import ch.idsia.agents.controllers.behavior.BasicTypes.Task;

/**
 *
 * @author lucasdavid
 */
public class IsObstacle extends Task {

    @Override
    public boolean run(boolean[] _action, BehaviorTreeAgent _agent) {
        
        return _agent.getReceptiveFieldCellValue(_agent.marioEgoRow(), _agent.marioEgoCol() + 1) != 0;
    }

}
