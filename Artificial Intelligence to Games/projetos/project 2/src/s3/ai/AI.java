package s3.ai;

import java.io.IOException;
import java.util.List;

import s3.base.S3;
import s3.base.S3Action;
import s3.entities.WPlayer;

/**
 * Interface of the towers AIs.
 * 
 * @author Santi Ontanon
 * adapted from the same class for Towers by Marco Antonio
 */
public interface AI {

	/**
	 * Method called by the game when it starts.
	 */
	public void gameStarts();
	
	/**
	 * Execute the 'tick()' of the AI. 
	 * @param application_state State of the application.
	 * @param game Game where the AI is running.
	 * @param actions List of actions that the game should execute.
	 * 
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public void game_cycle(S3 game, WPlayer player, List<S3Action> actions) throws ClassNotFoundException, IOException;

	/**
	 * Method called by the game when it ends.
	 */
	public void gameEnd();
	
	/**
	 * @return The name of the AI.
	 */
	public String getPlayerId();
}
