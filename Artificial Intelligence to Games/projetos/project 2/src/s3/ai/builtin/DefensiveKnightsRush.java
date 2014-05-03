/**
 * 
 */
package s3.ai.builtin;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import s3.base.S3;
import s3.base.S3Action;
import s3.entities.S3Entity;
import s3.entities.WBuilding;
import s3.entities.WFortress;
import s3.entities.WBarracks;
import s3.entities.WBlacksmith;
import s3.entities.WKnight;
import s3.entities.WLumberMill;
import s3.entities.WPeasant;
import s3.entities.WPlayer;
import s3.entities.WStable;
import s3.entities.WTower;
import s3.entities.WUnit;
import s3.util.Pair;

/**
 * @author Santi: AI that builds the necessary to train knights, peasants harvesting gold, and harvesting wood, 
 *         then builds
 * 	       knights without stop and attacks! 
 *         while it's doing up the tech tree, it builds defensive towers to gain some time
 *         After a while, increases the number of peasants, and builds a
 * 		   second barracks. It also looks for goldmines where there is still available gold
 * 		   Also, it sends knights in waves
 */
public class DefensiveKnightsRush extends FootmenRush {
	
    int nTowers = 4;

	public DefensiveKnightsRush(String playerID) {
		super(playerID);
		troopClass = WKnight.class;
		nGoldPeasants= 4;
	}

    public void game_cycle(S3 game, WPlayer player, List<S3Action> actions)
			throws ClassNotFoundException, IOException {
		if (game.getCycle() % 25 != 0) return;

		List<Request> requests = new LinkedList<Request>();

		requests.addAll(checkTownhall(game, player, actions));
		requests.addAll(checkBarracks(game, player, actions));
		requests.addAll(checkPeasants(game, player, actions));
		requests.addAll(checkTowers(game, player, actions));
		requests.addAll(buildTroops(game, player, actions));
		requests.addAll(attack(game, player, actions));

        executeRequests(requests, game, player, actions);
	}

    List<Request> checkTowers(S3 game, WPlayer player, List<S3Action> actions) {
		List<Request> requests = new LinkedList<Request>();
        boolean lumberMill = false;
		if (DEBUG >= 1)
			System.out.println("Rush-AI: checkTowers");
		int nt = 0;
		for(S3Entity e:game.getAllUnits()) {
			if (e instanceof WTower && e.getOwner().equals(m_playerID)) nt++;
            if (e instanceof WLumberMill && e.getOwner().equals(m_playerID)) lumberMill = true;
		}

        if (!lumberMill) {
            // Sine the 'checkBarracks' method already builds the lumber mill, just wait
            return requests;
        }

		if (nt<nTowers) {
			List<WUnit> peasants = game.getUnitTypes(player, WPeasant.class);
			WPeasant peasant = null;
			for(WUnit p:peasants) {
				if (p.getStatus()!=null &&
                    p.getStatus().m_action==S3Action.ACTION_BUILD &&
                    p.getStatus().m_parameters.get(0).equals(WTower.class.getSimpleName())) {
					// There is already a peasant building a barracks:
					return requests;
				}
			}
			for(WUnit p:peasants) {
				if (p.getStatus()==null ||
                    p.getStatus().m_action!=S3Action.ACTION_BUILD) {
					peasant = (WPeasant)p;
				}
			}
			if (null == peasant) return requests;
			// First try one locatino with space to walk around it:
			Pair<Integer, Integer> loc = game.findFreeSpace(peasant.getX(), peasant.getY(), 4);
			if (null == loc) {
				loc = game.findFreeSpace(peasant.getX(), peasant.getY(), 2);
				if (loc==null) return requests;
			}
			if (DEBUG >= 1)
				System.out.println("Rush-AI: building tower at " + loc.m_a + " , " + loc.m_b);

			requests.add(new Request(125,peasant.entityID,900,300,new S3Action(peasant.entityID,S3Action.ACTION_BUILD, WTower.class.getSimpleName(), loc.m_a, loc.m_b)));
		}
		return requests;
	}
	

	List<Request> checkBarracks(S3 game, WPlayer player, List<S3Action> actions) {
		Class []buildOrder = {WLumberMill.class,WBlacksmith.class,WBarracks.class,WFortress.class,WStable.class};
		List<Request> requests = new LinkedList<Request>();
		if (DEBUG >= 1) System.out.println("Rush-AI: checkBarracks");
		
		for(int i = 0;i<buildOrder.length;i++) {
			int target = 1;
			int n = 0;
			if (buildOrder[i]==WBarracks.class) target = nBarracks;
			for(S3Entity e:game.getAllUnits()) {
				if (buildOrder[i].isInstance(e) && e.getOwner().equals(m_playerID)) {
					n++;
					if (n>=target) break;
				}
			}
			if (n<target) {
				List<WUnit> peasants = game.getUnitTypes(player, WPeasant.class);
				WPeasant peasant = null;
				for(WUnit p:peasants) {
					if (p.getStatus()!=null &&
							p.getStatus().m_action==S3Action.ACTION_BUILD &&
							p.getStatus().m_parameters.get(0).equals(buildOrder[i].getSimpleName())) {
						return requests;
					}
				}
				for(WUnit p:peasants) {
					if (p.getStatus()==null ||
							p.getStatus().m_action!=S3Action.ACTION_BUILD) {
						peasant = (WPeasant)p;
					}
				}
				if (null == peasant) return requests;
				// First try one locatino with space to walk around it:
				WBuilding b = null;
				try {
					b = (WBuilding)buildOrder[i].newInstance();
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (b==null) return requests;

				Pair<Integer, Integer> loc = game.findFreeSpace(peasant.getX(), peasant.getY(), b.getWidth()+2);
				if (null == loc) {
					loc = game.findFreeSpace(peasant.getX(), peasant.getY(), b.getWidth());
					if (loc==null) return requests;
				} else {
					loc.m_a++;
					loc.m_b++;
				}
				
				if (DEBUG >= 1)
					System.out.println("Rush-AI: building " + buildOrder[i].getSimpleName() + " at " + loc.m_a + " , " + loc.m_b);

				requests.add(new Request(150,peasant.entityID,b.getCost_gold(),b.getCost_wood(),new S3Action(peasant.entityID,S3Action.ACTION_BUILD, buildOrder[i].getSimpleName(), loc.m_a, loc.m_b)));				
			}
		}
				
		return requests;
	}
	
	List<Request> buildTroops(S3 game, WPlayer player, List<S3Action> actions) {
		List<Request> requests = new LinkedList<Request>();
		List<WUnit> barrackss = game.getUnitTypes(player, WBarracks.class);
		List<WUnit> stables = game.getUnitTypes(player, WStable.class);
		WBarracks barracks = null;
		for(WUnit b:barrackss) {
			if (b.getStatus()==null|| b.getStatus().m_action==S3Action.ACTION_STAND_GROUND) {
				barracks = (WBarracks)b;
			}
		}
		if (null == barracks || stables.size()==0) return requests;
		WUnit troop = null;
		try {
			troop = (WUnit) troopClass.newInstance();
		} catch(Exception e) {
			
		}
		if (troop!=null && barracks.getStatus()==null && player.getGold() >= troop.getCost_gold() && player.getWood() >= troop.getCost_wood()) {			
			S3Action a = new S3Action(barracks.entityID,S3Action.ACTION_TRAIN, troopClass.getSimpleName()); 
			requests.add(new Request(100,barracks.entityID,troop.getCost_gold(),troop.getCost_wood(),a));			
		}
		return requests;
	}

}
