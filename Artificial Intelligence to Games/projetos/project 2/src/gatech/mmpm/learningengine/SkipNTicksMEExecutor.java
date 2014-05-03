/* Copyright 2010 Santiago Ontanon and Ashwin Ram */

package gatech.mmpm.learningengine;

import gatech.mmpm.Action;
import gatech.mmpm.GameState;

import java.util.List;


public class SkipNTicksMEExecutor extends LazyMEExecutor {

	public SkipNTicksMEExecutor() {
	}

	public SkipNTicksMEExecutor(int numTicksSkipped, IMEExecutor orig) {
		super(orig);
		m_numTicksSkipped = numTicksSkipped;
	}
	
	public void gameEnd() {
		m_aiOrig.gameEnd();
	}

	public void gameStart(String playerName) {
		m_remainings = 0;
		m_aiOrig.gameStart(playerName);
	}

	protected boolean canThink() {
		m_remainings++;
		return (m_remainings % m_numTicksSkipped) == 0;
	}

	protected void think(int cycle, GameState gs, List<Action> actions) {
		m_aiOrig.getActions(cycle, gs, actions);
	}
/*	
	public void getActions(GameState gs, List<Action> actions) {
		if ((m_remainings % m_numTicksSkipped) == 0)
			m_aiOrig.getActions(gs, actions);
		
		m_remainings++;
	}
	
	public void getActions(IGameStateProvider gameStateProvider, java.util.List<plans.Action> actions) {
		if ((m_remainings % m_numTicksSkipped) == 0)
			m_aiOrig.getActions(gameStateProvider.getGameState(), actions);
		
		m_remainings++;
	}
	*/
	int m_numTicksSkipped;
	int m_remainings;
}
