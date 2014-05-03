/* Copyright 2010 Santiago Ontanon and Ashwin Ram */

package gatech.mmpm.tracer;

import gatech.mmpm.IDomain;

/**
 * Class used by games to create traces of their execution.
 * Instead of having every method throwing exception when
 * they fail to save data to disk, it has a method that
 * allows the user to know whether the operations were
 * performed.
 * 
 * @author Marco Antonio G�mez Mart�n
 */
public abstract class ITracer {

	public abstract void beginTrace();

	public abstract void putMetadata(java.util.Properties prop);
	
	public abstract void endTrace(IDomain idomain, String winner);

	/**
	 * Called when the game wants to start to save a new game cycle.
	 * @param number Cycle's number.
	 */
	public abstract void beginGameCycle(int number);

	public abstract void endGameCycle();

	public void putGameState(gatech.mmpm.GameState gs) {
		if (m_firstGameState == null) m_firstGameState = gs;
		m_lastGameState = gs;
		putGameStateInternal(gs);
	}
	
	public abstract void putGameStateInternal(gatech.mmpm.GameState gs);
	
	public abstract void putAction(gatech.mmpm.Action a);
	
	public abstract void putAbortedAction(gatech.mmpm.Action a);
	
	/**
	 * Get the "error flag" of the object.
	 * @return True if all the operations so far were executed
	 * successfully.
	 */
	public abstract boolean success();
	
	public abstract String getErrorMessage();

	gatech.mmpm.GameState m_firstGameState = null;
	gatech.mmpm.GameState m_lastGameState = null;
}