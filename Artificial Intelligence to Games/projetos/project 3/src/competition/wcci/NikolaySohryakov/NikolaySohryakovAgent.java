package competition.wcci.NikolaySohryakov;

import ch.idsia.agents.Agent;
import ch.idsia.agents.controllers.BasicMarioAIAgent;
import ch.idsia.benchmark.mario.engine.sprites.Mario;
import ch.idsia.benchmark.mario.environments.Environment;

/**
 * Created by IntelliJ IDEA.
 * User: Sergey Karakovskiy, sergey.karakovskiy@gmail.com
 * Date: 16.03.11
 * Time: 3:19
 * Package: competition.wcci.NikolaySohryakov
 */
public class NikolaySohryakovAgent extends BasicMarioAIAgent implements Agent
{
public NikolaySohryakovAgent()
{
    super("ForwardJumpingAgent");
    reset();
}

public boolean[] getAction()
{
    action[Mario.KEY_SPEED] = action[Mario.KEY_JUMP] = isMarioAbleToJump || !isMarioOnGround;
    return action;
}

public void reset()
{
    action = new boolean[Environment.numberOfKeys];
    action[Mario.KEY_RIGHT] = true;
    action[Mario.KEY_SPEED] = true;
}
}
