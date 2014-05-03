/* Copyright 2010 Santiago Ontanon and Ashwin Ram */

package gatech.mmpm.learningengine;

import gatech.mmpm.ConfigurationException;

/**
 * Every learner engine must provide a class implementing this
 * interface to execute MEs in the games.
 * 
 * @see IMETrainer
 * 
 * @author Marco Antonio Gomez Martin, Pedro Pablo Gomez-Martin and David Llanso
 * @date August, 2009
 */
public interface IMEExecutor {
	
	/**
	 * Method that loads a ME to be executed during the game.
	 * @param me ME to be loaded.
	 * @param idomain Domain of the game to play.
	 * @throws ConfigurationException 
	 */
	public boolean loadME(java.io.InputStream me, gatech.mmpm.IDomain idomain) 
		throws ConfigurationException;
	
	/**
	 * Method called when the game starts.
	 * @param playerName Name of the player that the object controls.
	 * @param plannerUsed Name of the plan that should be used.
	 */
	public void gameStart(String playerName);
	
	/**
	 * Method called when the game ends.
	 */
	public void gameEnd();
	
	/**
	 * Returns the actions the game should perform given its state.
	 * @param cycle Current game cycle.
	 * @param gs Current game state.
	 * @param actions List of actions to be performed. The method
	 * add all the actions to the list.
	 */
	public void getActions(int cycle,
						   gatech.mmpm.GameState gs, 
						   java.util.List<gatech.mmpm.Action> actions);
	
	/**
	 * Returns the actions the game should perform given its state.
	 * @param gs Current game state.
	 * @return list of actions to be performed.
	 */
	public java.util.List<gatech.mmpm.Action> getActions(int cycle, gatech.mmpm.GameState gs);
	
	
	/**
	 * Optimized version of getActions that delays the creation
	 * of the Game State until the very moment it is needed. If
	 * in this "tick" D2 won't plan next actions, it won't ask
	 * for the game state.
	 * <p>
	 * In case D2 needs the game state, it guarantees that the
	 * game state provider will be invoke _before_ returning
	 * (D2 will NOT cache the provider to used it after the
	 * end of the execution of the method).
	 * 
	 * @param gameStateProvider Object that are able to give to D2
	 * the state of the game.
	 * @param actions List of actions to be performed. The method
	 * add all the actions to the list.
	 * @return list of actions to be performed.
	 */
	public void getActions(IGameStateProvider gameStateProvider, java.util.List<gatech.mmpm.Action> actions);
	
	/**
	 * Interface implemented by the classes in the domain that
	 * are able to provide with the game state.
	 * 
	 * @author Marco Antonio Gomez Martin
	 */
	static interface IGameStateProvider {
		gatech.mmpm.GameState getGameState();
		int getGameCycle();
	}
}
