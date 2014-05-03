package ch.idsia.agents.controllers.behavior.BasicTypes;

import ch.idsia.agents.controllers.BehaviorTreeAgent;
import java.util.List;

/**
 *
 * @author lucasdavid
 */
public class Sequence extends Composite {

    public Sequence() {
        super();
    }

    public Sequence(String _name) {
        super(_name);
    }

    public Sequence(List<Task> _tasks) {
        super(_tasks);
    }

    @Override
    public boolean run(boolean[] _action, BehaviorTreeAgent _agent) {

        if (testing) {
            System.out.println(this);
        }

        for (Task task : tasks) {
            if (!task.run(_action, _agent)) {
                return false;
            }
        }
        return true;
    }

}
