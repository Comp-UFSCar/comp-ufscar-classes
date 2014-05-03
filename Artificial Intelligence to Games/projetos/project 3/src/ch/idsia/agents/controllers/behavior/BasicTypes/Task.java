package ch.idsia.agents.controllers.behavior.BasicTypes;

import ch.idsia.agents.controllers.BehaviorTreeAgent;

/**
 *
 * @author lucasdavid
 */
public abstract class Task {

    public abstract boolean run(boolean[] _action, BehaviorTreeAgent _agent);
}
