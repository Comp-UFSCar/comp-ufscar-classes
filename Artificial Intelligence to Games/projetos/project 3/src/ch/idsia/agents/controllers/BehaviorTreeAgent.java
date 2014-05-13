package ch.idsia.agents.controllers;

import ch.idsia.benchmark.mario.environments.Environment;
import ch.idsia.agents.Agent;

import ch.idsia.agents.controllers.behavior.BasicTypes.*;
import ch.idsia.agents.controllers.behavior.actions.*;
import ch.idsia.agents.controllers.behavior.conditions.*;

/**
 *
 * @author lucasdavid
 */
public final class BehaviorTreeAgent extends BasicMarioAIAgent implements Agent {

    BehaviorTree behavior;
    int realJumpCounter;

    public BehaviorTreeAgent() {
        super("BehaviorTreeAgent");

        behavior = new BehaviorTree(
                new Sequence()
                .add(new StopJump())
                .add(new StopFire())
                .add(new StopMove())
                .add(
                        new Selector()
                        .add(new Sequence("EnemyFighter")
                                // iminent threat
                                .add(new NearEnemy())
                                .add(new Selector()
                                        // if mario can fight, kill threat
                                        .add(new Sequence()
                                                .add(new MarioCanFight())
                                                .add(new FightEnemy())
                                        )
                                        // jump over it, otherwise
                                        .add(new Sequence()
                                                .add(new Jump())
                                                .add(new MoveRight())
                                        )
                                )
                        )
                        // if you can move right, do it
                        .add(new Sequence("StageWalker")
                                .add(new CanMoveRight())
                                .add(new MoveRight())
                        )
                        // if there is an obstacle, jump it
                        .add(new Sequence("Jumper")
                                .add(new MoveRight())
                                .add(new Selector("ObstacleAvoider")
                                        // floating platform nearby => small jump should be executed
                                        .add(new Sequence()
                                                .add(new NearFloatingPlatform())
                                                .add(new IsGrounded())
                                                .add(new Jump())
                                                .add(new ValidJump())
                                        )
                                        // execute highest jump possible
                                        .add(new Sequence("Long jumper")
                                                .add(new Jump())
                                                .add(new ValidJump())
                                        )
                                )
                        )
                )
        );

        reset();
    }

    public boolean grounded() {
        return isMarioOnGround;
    }

    public boolean ableToJump() {
        return isMarioAbleToJump;
    }

    public int marioEgoRow() {
        return marioEgoRow;
    }

    public int marioEgoCol() {
        return marioEgoCol;
    }

    public int status() {
        return marioStatus;
    }

    public int mode() {
        return marioMode;
    }

    public byte[][] enemies() {
        return enemies;
    }

    @Override
    public void reset() {

        action = new boolean[Environment.numberOfKeys];
    }

    @Override
    public boolean[] getAction() {

        behavior.run(action, this);

        return action;
    }

}
