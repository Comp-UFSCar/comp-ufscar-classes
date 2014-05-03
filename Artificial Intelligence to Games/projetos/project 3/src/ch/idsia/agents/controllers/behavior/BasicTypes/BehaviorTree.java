package ch.idsia.agents.controllers.behavior.BasicTypes;

import ch.idsia.agents.controllers.BehaviorTreeAgent;
import java.util.List;

/**
 *
 * @author lucasdavid
 */
public class BehaviorTree extends Task {

    Task root;
    Blackboard blackboard;

    public BehaviorTree(Task _task) {
        root = _task;
    }

    public BehaviorTree(List<Task> tasks) {
        root = new Sequence(tasks);
    }

    public BehaviorTree blackboard(Blackboard _blackboard) {
        blackboard = _blackboard;
        return this;
    }

    public Blackboard blackboard() {
        return blackboard;
    }

    @Override
    public boolean run(boolean[] _action, BehaviorTreeAgent _agent) {
        return root.run(_action, _agent);
    }

}
