/**
 * 
 */
package s3.ai.builtin;

import java.io.IOException;
import java.util.List;

import s3.ai.AI;
import s3.base.S3;
import s3.base.S3Action;
import s3.entities.WArcher;
import s3.entities.WCatapult;
import s3.entities.WFootman;
import s3.entities.WFortress;
import s3.entities.WGoldMine;
import s3.entities.WBarracks;
import s3.entities.WBlacksmith;
import s3.entities.WKnight;
import s3.entities.WLumberMill;
import s3.entities.WPeasant;
import s3.entities.WPlayer;
import s3.entities.WTownhall;
import s3.entities.WUnit;
import s3.util.Pair;

/**
 * @author kane AI that builds a barracks and two footmen and ATTACKS! then
 *         harvests and trains footmen, attacking when there are two.
 */
public class RushAI implements AI {

	int DEBUG = 0;

	/**
	 * the player name represented by this AI.
	 */
	private String m_playerID;

	/**
	 * default constructor.
	 * 
	 * @param playerID
	 */
	public RushAI(String playerID) {
		m_playerID = playerID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see base.ai.AI#gameEnd()
	 */
	public void gameEnd() {
		// do nothing, except maybe celebrate
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see base.ai.AI#gameStarts()
	 */
	public void gameStarts() {
		// start kicking ass
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see base.ai.AI#game_cycle(game.S2, entities.WPlayer, java.util.List)
	 */
	public void game_cycle(S3 game, WPlayer player, List<S3Action> actions)
			throws ClassNotFoundException, IOException {
		if (game.getCycle() % 25 != 0) {
			return;
		}
		if (checkTownhall(game, player, actions)) {
			return;
		}

		if (checkBarracks(game, player, actions)) {
			return;
		}

		checkPeasant(game, player, actions);

		buildFootmen(game, player, actions);

		attack(game, player, actions);
	}

	/**
	 * attacks the enemy!
	 * 
	 * @param game
	 * @param player
	 * @param actions
	 */
	private void attack(S3 game, WPlayer player, List<S3Action> actions) {
		List<WUnit> footmen = game.getUnitTypes(player, WFootman.class);
		WPlayer enemy = null;
		for (WPlayer entity : game.getPlayers()) {
			if (entity != player) {
				enemy = entity;
				break;
			}
		}
		WUnit enemyTroop = game.getUnitType(enemy, WFootman.class);
		if (null == enemyTroop) {
			enemyTroop = game.getUnitType(enemy, WKnight.class);
		}
		if (null == enemyTroop) {
			enemyTroop = game.getUnitType(enemy, WArcher.class);
		}
		if (null == enemyTroop) {
			enemyTroop = game.getUnitType(enemy, WCatapult.class);
		}
		if (null == enemyTroop) {
			enemyTroop = game.getUnitType(enemy, WPeasant.class);
		}
		if (null == enemyTroop) {
			enemyTroop = game.getUnitType(enemy, WTownhall.class);
		}
		if (null == enemyTroop) {
			enemyTroop = game.getUnitType(enemy, WBarracks.class);
		}
		if (null == enemyTroop) {
			enemyTroop = game.getUnitType(enemy, WLumberMill.class);
		}
		if (null == enemyTroop) {
			enemyTroop = game.getUnitType(enemy, WBlacksmith.class);
		}
		if (null == enemyTroop) {
			enemyTroop = game.getUnitType(enemy, WFortress.class);
		}
		if (null == enemyTroop) {
			// if enemyTroop is still null here, we should have won the game
			return;
		}

		for(WUnit u:footmen) {
			actions.add(new S3Action(u.entityID,S3Action.ACTION_ATTACK, enemyTroop.entityID));
		}

	}

	/**
	 * trains a footman if there is enough gold.
	 * 
	 * @param game
	 * @param player
	 * @param actions
	 */
	private void buildFootmen(S3 game, WPlayer player, List<S3Action> actions) {
		WBarracks barracks = (WBarracks) game.getUnitType(player, WBarracks.class);

		if (null == barracks) {
			return;
		}
		if (barracks.getStatus()==null && player.getGold() >= 600) {
			actions.add(new S3Action(barracks.entityID,S3Action.ACTION_TRAIN, WFootman.class.getSimpleName()));
		}
	}

	/**
	 * Checks that there is at least one peasant mining gold
	 * 
	 * @param game
	 * @param player
	 * @param actions
	 */
	private void checkPeasant(S3 game, WPlayer player, List<S3Action> actions) {
		WPeasant peasant = (WPeasant) game.getUnitType(player, WPeasant.class);
		if (null == peasant) {
			return;
		}
		if (peasant.getStatus() != null) return;

		List<WUnit> mines = game.getUnitTypes(null, WGoldMine.class);
		WGoldMine mine = null;
		int leastDist = 9999;
		for (WUnit unit : mines) {
			int dist = Math.abs(unit.getX() - peasant.getX())
					+ Math.abs(unit.getY() - peasant.getY());
			if (dist < leastDist) {
				leastDist = dist;
				mine = (WGoldMine) unit;
			}
		}
		if (null != mine) {
			actions.add(new S3Action(peasant.entityID, S3Action.ACTION_HARVEST, mine.entityID));
		}
	}

	/**
	 * Checks that a barracks exists, and builds one if it doesn't
	 * 
	 * @param game
	 * @param player
	 * @param actions
	 */
	private boolean checkBarracks(S3 game, WPlayer player, List<S3Action> actions) {
		if (DEBUG >= 1)
			System.out.println("Rush-AI: checkBarracks");
		if (null == game.getUnitType(player, WBarracks.class)) {
			List<WUnit> peasants = game.getUnitTypes(player, WPeasant.class);
			WPeasant peasant = null;
			for(WUnit p:peasants) {
				if (p.getStatus()!=null &&
					p.getStatus().m_action==S3Action.ACTION_BUILD &&
					p.getStatus().m_parameters.get(0)==WBarracks.class.getSimpleName()) {
					// There is already a peasant building a barracks:
					return false;
				}
			}
			for(WUnit p:peasants) {
				if (p.getStatus()==null ||
					p.getStatus().m_action!=S3Action.ACTION_BUILD) {
					peasant = (WPeasant)p;
				}
			}
			if (null == peasant) {
				// TODO train peasant
				return true;
			}
			Pair<Integer, Integer> loc = game.findFreeSpace(peasant.getX(), peasant.getY(), 3);
			if (null == loc) {
				// can't build anything.
				return true;
			}
			if (DEBUG >= 1)
				System.out.println("Rush-AI: building barracks at " + loc.m_a + " , " + loc.m_b);

			actions.add(new S3Action(peasant.entityID,S3Action.ACTION_BUILD, WBarracks.class.getSimpleName(), loc.m_a, loc.m_b));
		}
		return false;
	}

	/**
	 * Checks that a townhall exists, and builds one if it doesn't.
	 * 
	 * @param game
	 * @param player
	 * @param actions
	 */
	private boolean checkTownhall(S3 game, WPlayer player, List<S3Action> actions) {
		if (DEBUG >= 1)
			System.out.println("Rush-AI: checkTownhall");
		if (null == game.getUnitType(player, WTownhall.class)) {
			WPeasant peasant = (WPeasant) game.getUnitType(player, WPeasant.class);
			if (null == peasant) {
				// we're screwed, can't build, can't harvest
				return true;
			}
			Pair<Integer, Integer> loc = game.findFreeSpace(peasant.getX(), peasant.getY(), 3);
			actions.add(new S3Action(peasant.entityID,S3Action.ACTION_BUILD, WTownhall.class.getSimpleName(), loc.m_a, loc.m_b));
			return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see base.ai.AI#getPlayerId()
	 */
	public String getPlayerId() {
		return m_playerID;
	}

}
