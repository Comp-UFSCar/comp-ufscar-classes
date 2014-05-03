package s3.ai;



import java.io.IOException;
import java.util.List;

import s3.base.S3;
import s3.base.S3Action;
import s3.entities.WPlayer;

public class AIEmpty implements AI {

	public String m_playerID;

	public void gameEnd() {
	}

	public void gameStarts() {
	}

	public AIEmpty(String playerID) {
		m_playerID = playerID;
	}
	
	public String getPlayerId() {
		return m_playerID;
	}
		
	public void game_cycle(S3 game,WPlayer player,List<S3Action> actions) throws IOException, ClassNotFoundException {

		// This AI is empty
	}
}
