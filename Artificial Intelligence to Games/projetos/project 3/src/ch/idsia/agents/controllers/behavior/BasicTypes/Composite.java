package ch.idsia.agents.controllers.behavior.BasicTypes;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author lucasdavid
 */
public abstract class Composite extends Task {

    String name;
    boolean testing;

    List<Task> tasks;

    Composite() {
        tasks = new LinkedList<>();
    }

    Composite(String _name) {
        this();
        name = _name;
    }

    Composite(List<Task> _tasks) {
        tasks = new LinkedList<>(_tasks);
    }

    public Composite testing() {
        testing = true;
        return this;
    }

    public Composite add(Task _task) {
        tasks.add(_task);

        return this;
    }

    @Override
    public String toString() {
        return name;
    }

}
