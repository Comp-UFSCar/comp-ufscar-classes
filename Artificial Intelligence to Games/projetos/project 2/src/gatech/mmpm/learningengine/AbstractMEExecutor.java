/* Copyright 2010 Santiago Ontanon and Ashwin Ram */

package gatech.mmpm.learningengine;

import gatech.mmpm.Action;
import gatech.mmpm.GameState;

import java.util.List;


public abstract class AbstractMEExecutor implements IMEExecutor {

	public List<Action> getActions(int cycle, GameState gs) {
		List<Action> ret = new java.util.LinkedList<Action>();
		
		this.getActions(cycle, gs, ret);
		return ret;
	}

	public void getActions(IGameStateProvider gameStateProvider, java.util.List<gatech.mmpm.Action> actions) {
		getActions(gameStateProvider.getGameCycle(), gameStateProvider.getGameState(), actions);
	}

}
