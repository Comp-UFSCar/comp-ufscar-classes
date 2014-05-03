/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.idsia.agents.controllers.behavior.conditions;

import ch.idsia.agents.controllers.BehaviorTreeAgent;
import ch.idsia.agents.controllers.behavior.BasicTypes.Task;
import ch.idsia.benchmark.mario.engine.sprites.Sprite;

/**
 *
 * @author lucasdavid
 */
public class NearEnemy extends Task {

    private boolean isCreature(int c) {
        switch (c) {
            case Sprite.KIND_GOOMBA:
            case Sprite.KIND_RED_KOOPA:
            case Sprite.KIND_RED_KOOPA_WINGED:
            case Sprite.KIND_GREEN_KOOPA_WINGED:
            case Sprite.KIND_GREEN_KOOPA:
                return true;
        }
        return false;
    }

    @Override
    public boolean run(boolean[] _action, BehaviorTreeAgent _agent) {
        int x = _agent.marioEgoRow();
        int y = _agent.marioEgoCol();

        return isCreature(_agent.enemies()[x][y + 1]) || isCreature(_agent.enemies()[x][y + 2])
                || isCreature(_agent.enemies()[x - 1][y + 1]) || isCreature(_agent.enemies()[x - 2][y + 1])
                || isCreature(_agent.enemies()[x + 1][y + 1]) || isCreature(_agent.enemies()[x + 2][y + 1]);
    }

}
