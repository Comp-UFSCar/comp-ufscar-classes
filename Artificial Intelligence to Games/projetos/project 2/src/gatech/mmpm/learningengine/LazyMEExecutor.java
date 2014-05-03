/* Copyright 2010 Santiago Ontanon and Ashwin Ram */

package gatech.mmpm.learningengine;

import gatech.mmpm.Action;
import gatech.mmpm.ConfigurationException;
import gatech.mmpm.GameState;

import java.util.List;


/**
 * Base class for all those AIs that do not calculate the
 * actions to be performed in every cycle.
 * 
 * @author Marco Antonio Gomez Martin and David Llanso
 */

public abstract class LazyMEExecutor extends AbstractMEExecutor {
	
	public LazyMEExecutor() {
	}
	
	public LazyMEExecutor(IMEExecutor orig) {
		m_aiOrig = orig;
	}
	
	public void setMEOrig(IMEExecutor orig) {
		m_aiOrig = orig;
	}
	
	public void getActions(int cycle, GameState gs, List<Action> actions) {
		if (canThink())
			think(cycle, gs, actions);
	}

	public void getActions(IGameStateProvider gameStateProvider, java.util.List<gatech.mmpm.Action> actions) {

		if (canThink())
			think(gameStateProvider.getGameCycle(), gameStateProvider.getGameState(), actions);
	}

	/**
	 * Returns if the AI is able to think in this cycle.
	 * @return
	 */
	protected abstract boolean canThink();

	/**
	 * Perform the real AI, calling D2 to retrieve the next actions
	 * to be performed.
	 * @param gs Game State
	 * @param actions List of actions that de AI suggest to execute.
	 */
	protected abstract void think(int cycle, GameState gs, List<Action> actions);
	
	/**
	 * Method that loads a ME to be executed during the game.
	 * @param me ME to be loaded.
	 * @param idomain Domain of the game to play.
	 * @throws ConfigurationException In case of errors
	 */
	public boolean loadME(java.io.InputStream me, gatech.mmpm.IDomain idomain) 
		throws ConfigurationException 
	{
		m_aiOrig.loadME(me,idomain);
		return true;
	}
	
	protected IMEExecutor m_aiOrig;
	
}
