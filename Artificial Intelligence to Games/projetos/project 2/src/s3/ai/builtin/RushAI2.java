/**
 * 
 */
package s3.ai.builtin;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import s3.ai.AI;
import s3.base.S3;
import s3.base.S3Action;
import s3.entities.S3Entity;
import s3.entities.S3PhysicalEntity;
import s3.entities.WArcher;
import s3.entities.WCatapult;
import s3.entities.WFootman;
import s3.entities.WFortress;
import s3.entities.WGoldMine;
import s3.entities.WBarracks;
import s3.entities.WBlacksmith;
import s3.entities.WKnight;
import s3.entities.WLumberMill;
import s3.entities.WOTree;
import s3.entities.WPeasant;
import s3.entities.WPlayer;
import s3.entities.WTownhall;
import s3.entities.WUnit;
import s3.util.Pair;

/**
 * @author Santi AI that builds a barracks, two peasants harvesting gold, and two harvesting wood, then builds
 * 	       footmen without stop and attacks!
 */
public class RushAI2 implements AI {

	int DEBUG = 0;
	int nGoldPeasants = 2;
	int nWoodPEasants = 2;
	private String m_playerID;

	public RushAI2(String playerID) {
		m_playerID = playerID;
	}

	public void gameEnd() {
	}

	public void gameStarts() {
	}

	public void game_cycle(S3 game, WPlayer player, List<S3Action> actions)
			throws ClassNotFoundException, IOException {
		if (game.getCycle() % 25 != 0) return;
		if (checkTownhall(game, player, actions)) return;
		if (checkBarracks(game, player, actions)) return;
		checkPeasants(game, player, actions);
		buildFootmen(game, player, actions);
		attack(game, player, actions);
	}

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
	private void checkPeasants(S3 game, WPlayer player, List<S3Action> actions) {
		int gp = 0;
		int wp = 0;
		List<WPeasant> freePeasants = new LinkedList<WPeasant>();
		for(S3Entity e:game.getAllUnits()) {
			if (e instanceof WPeasant && e.getOwner().equals(m_playerID)) {
				WPeasant peasant = (WPeasant)e;
				if (peasant.getStatus()!=null && peasant.getStatus().m_action==S3Action.ACTION_HARVEST) {
					if (peasant.getStatus().m_parameters.size()==1) gp++;
												  			   else wp++;
				} else {
					if (peasant.getStatus()==null)
						freePeasants.add(peasant);
				}
			}
		}
				
		if (gp<nGoldPeasants && freePeasants.size()>0) {
			WPeasant peasant = freePeasants.get(0);
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
			return;
		}
		if (wp<nWoodPEasants && freePeasants.size()>0) {
			WPeasant peasant = freePeasants.get(0);
			List<WOTree> trees = new LinkedList<WOTree>();
			for(int i = 0;i<game.getMap().getWidth();i++) {
				for(int j = 0;j<game.getMap().getHeight();j++) {
					S3PhysicalEntity e = game.getMap().getEntity(i, j);
					if (e instanceof WOTree) trees.add((WOTree)e);
				}				
			}
			
			WOTree tree = null;
			int leastDist = 9999;
			for (WOTree unit : trees) {
				int dist = Math.abs(unit.getX() - peasant.getX())
						+ Math.abs(unit.getY() - peasant.getY());
				if (dist < leastDist) {
					leastDist = dist;
					tree = unit;
				}
			}
			if (null != tree) {
				actions.add(new S3Action(peasant.entityID, S3Action.ACTION_HARVEST, tree.getX(),
						tree.getY()));
			}
		}
		
		if ((gp<nGoldPeasants || wp<nWoodPEasants) && freePeasants.isEmpty()) {
			WTownhall th = (WTownhall) game.getUnitType(player, WTownhall.class);
			if (th!=null && th.getStatus()==null) {
				actions.add(new S3Action(th.entityID,S3Action.ACTION_TRAIN, WPeasant.class.getSimpleName()));
			}
			return;
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
					p.getStatus().m_parameters.get(0).equals(WBarracks.class.getSimpleName())) {
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
			// First try one locatino with space to walk around it:
			Pair<Integer, Integer> loc = game.findFreeSpace(peasant.getX(), peasant.getY(), 4);
			if (null == loc) {
				loc = game.findFreeSpace(peasant.getX(), peasant.getY(), 3);
				if (loc==null) return true;
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
			List<WUnit> peasants = game.getUnitTypes(player, WPeasant.class);
			WPeasant peasant = null;
			for(WUnit p:peasants) {
				if (p.getStatus()!=null &&
					p.getStatus().m_action==S3Action.ACTION_BUILD &&
					p.getStatus().m_parameters.get(0).equals(WTownhall.class.getSimpleName())) {
					// There is already a peasant building a townhall:
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
