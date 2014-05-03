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
import s3.entities.WStable;
import s3.entities.WTower;
import s3.entities.WTownhall;
import s3.entities.WUnit;
import s3.util.Pair;

/**
 * @author Santi: footmen rush in waves
 */
public class FootmenRush implements AI {
	public class Request {
		public int priority;
		public int unitID;
		public int costGold,costWood;
		public S3Action action;
		
		public Request(int p,int ID,int g,int w,S3Action a) {
			priority = p;
			unitID = ID;
			costGold = g;
			costWood = w;
			action = a;
		}
	}

	Class troopClass = null;
	int DEBUG = 0;
	int nGoldPeasants = 2;
	int nWoodPEasants = 2;
	int nBarracks = 1;
	int nTrainedTroops = 0;
	int WAVE_SIZE = 4;
	String m_playerID;

	public FootmenRush(String playerID) {
		m_playerID = playerID;
		troopClass = WFootman.class;
	}

	public void gameEnd() {
	}

	public void gameStarts() {
	}

	public void game_cycle(S3 game, WPlayer player, List<S3Action> actions)
			throws ClassNotFoundException, IOException {
		if (game.getCycle() % 25 != 0) return;
		
		List<Request> requests = new LinkedList<Request>();
		
		requests.addAll(checkTownhall(game, player, actions));
		requests.addAll(checkBarracks(game, player, actions));
		requests.addAll(checkPeasants(game, player, actions));
		requests.addAll(buildTroops(game, player, actions));
		requests.addAll(attack(game, player, actions));

        executeRequests(requests, game, player, actions);
	}

    protected void executeRequests(List<Request> requests, S3 game, WPlayer player, List<S3Action> actions) {
			// also if any does not use any resources, execute it: (like attacks)
			List<Request> toExecute = new LinkedList<Request>();

			for(Request r:requests)
				if (r.costGold==0 && r.costWood==0) toExecute.add(r);
			requests.removeAll(toExecute);

			// sort requests, and if the one with highest priority can be satisfied, do it:
			{
				Request highest = null;
				for(Request r:requests) {
					if (highest==null || r.priority>highest.priority) {
						highest = r;
					}
				}

				if (highest!=null) {
					if (highest.costGold<=player.getGold() &&
						highest.costWood<=player.getWood()) toExecute.add(highest);
				}
			}

            // Filter the toExecute list for actions to the same peasant:
            List<Request> toDelete = new LinkedList<Request>();
            for(Request r1:toExecute) {
                for(Request r2:toExecute) {
                    if (r1!=r2) {
                        if (r1.unitID==r2.unitID) {
                            if (r1.priority>=r2.priority && !toDelete.contains(r2)) {
                                toDelete.add(r2);
                            }
                        }
                    }
                }
            }

            toExecute.removeAll(toDelete);

			for(Request r:toExecute) {
				actions.add(r.action);

				// count how many footmen we train:
				if (r.action.m_action== S3Action.ACTION_TRAIN &&
					r.action.m_parameters.get(0).equals(WFootman.class.getSimpleName())) {
					nTrainedTroops++;
					if (nTrainedTroops==5) nGoldPeasants++;
					if (nTrainedTroops==10) {
						nBarracks++;
						nGoldPeasants++;
					}
					if (nTrainedTroops==15) {
						nGoldPeasants++;
					}
				}

			}
    }

	protected List<Request> attack(S3 game, WPlayer player, List<S3Action> actions) {
		List<Request> requests = new LinkedList<Request>();
		List<WUnit> footmen = game.getUnitTypes(player, troopClass);
		int nf = 0;
		for(WUnit fm:footmen) {
			if (fm.getStatus()==null ||
				fm.getStatus().m_action==S3Action.ACTION_STAND_GROUND) {
				nf++;
			}
		}
		
		WPlayer enemy = null;
		for (WPlayer entity : game.getPlayers()) {
			if (entity != player) {
				enemy = entity;
				break;
			}
		}
		
		Class[] targetClasses = {WCatapult.class,WKnight.class, WArcher.class, WFootman.class,WPeasant.class,
				 				 WTower.class, WTownhall.class,WBarracks.class,WLumberMill.class,WBlacksmith.class,
				 				 WFortress.class, WStable.class};
		WUnit enemyTroop = null;
		
		for(Class c:targetClasses) {
			WUnit e = game.getUnitType(enemy, c);
			if (e!=null) {
				enemyTroop = e;
				break;
			}
			
		}
		
		if (enemyTroop==null) return requests;
		
		if (nf<WAVE_SIZE) {
			for(WUnit u:footmen) {
				if (u.getStatus()!=null && u.getStatus().m_action==S3Action.ACTION_ATTACK &&
                    u.getStatus().getIntParameter(0)!=enemyTroop.entityID) {
					requests.add(new Request(100,u.entityID,0,0,new S3Action(u.entityID,S3Action.ACTION_ATTACK, enemyTroop.entityID)));
				}
			}
		} else {
			for(WUnit u:footmen) {
				if (u.getStatus()==null || u.getStatus().m_action!=S3Action.ACTION_ATTACK) {
					requests.add(new Request(100,u.entityID,0,0,new S3Action(u.entityID,S3Action.ACTION_ATTACK, enemyTroop.entityID)));
				}
			}
		}
		return requests;
	}

	
	List<Request> buildTroops(S3 game, WPlayer player, List<S3Action> actions) {
		List<Request> requests = new LinkedList<Request>();
		List<WUnit> barrackss = game.getUnitTypes(player, WBarracks.class);
		WBarracks barracks = null;
		for(WUnit b:barrackss) {
			if (b.getStatus()==null|| b.getStatus().m_action==S3Action.ACTION_STAND_GROUND) {
				barracks = (WBarracks)b;
			}
		}
		if (null == barracks) return requests;
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

	
	protected List<Request> checkPeasants(S3 game, WPlayer player, List<S3Action> actions) {
		List<Request> requests = new LinkedList<Request>();
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
				int dist = peasant.distance(unit, game);
				if (dist!=-1 && dist < leastDist && ((WGoldMine)unit).getRemaining_gold()>0) {
					leastDist = dist;
					mine = (WGoldMine) unit;
				}
			}
			
			if (mine!=null) {
				requests.add(new Request(100,peasant.entityID,0,0,new S3Action(peasant.entityID,S3Action.ACTION_HARVEST, mine.entityID)));
				return requests;
			}
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

			if (tree!=null) {
				requests.add(new Request(100,peasant.entityID,0,0,new S3Action(peasant.entityID,S3Action.ACTION_HARVEST, tree.getX(),tree.getY())));
				return requests;
			}
		}
		
		if ((gp<nGoldPeasants || wp<nWoodPEasants) && freePeasants.isEmpty()) {
			WTownhall th = (WTownhall) game.getUnitType(player, WTownhall.class);
			if (th!=null && (th.getStatus()==null)) {
				requests.add(new Request(100,th.entityID,400,0,new S3Action(th.entityID,S3Action.ACTION_TRAIN, WPeasant.class.getSimpleName())));
			}
		}
		
		return requests;

	}

	List<Request> checkBarracks(S3 game, WPlayer player, List<S3Action> actions) {
		List<Request> requests = new LinkedList<Request>();
		if (DEBUG >= 1)
			System.out.println("Rush-AI: checkBarracks");
		int nb = 0;
		for(S3Entity e:game.getAllUnits()) {
			if (e instanceof WBarracks && e.getOwner().equals(m_playerID)) nb++;
		}

		if (nb<nBarracks) {
			List<WUnit> peasants = game.getUnitTypes(player, WPeasant.class);
			WPeasant peasant = null;
			for(WUnit p:peasants) {
				if (p.getStatus()!=null &&
						p.getStatus().m_action==S3Action.ACTION_BUILD &&
						p.getStatus().m_parameters.get(0).equals(WBarracks.class.getSimpleName())) {
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
			Pair<Integer, Integer> loc = game.findFreeSpace(peasant.getX(), peasant.getY(), 5);
			if (null == loc) {
				loc = game.findFreeSpace(peasant.getX(), peasant.getY(), 3);
				if (loc==null) return requests;
			}
			if (DEBUG >= 1)
				System.out.println("Rush-AI: building barracks at " + loc.m_a + " , " + loc.m_b);

			requests.add(new Request(150,peasant.entityID,700,450,new S3Action(peasant.entityID,S3Action.ACTION_BUILD, WBarracks.class.getSimpleName(), loc.m_a, loc.m_b)));
		}
		return requests;
	}

	protected List<Request> checkTownhall(S3 game, WPlayer player, List<S3Action> actions) {
		List<Request> requests = new LinkedList<Request>();
		if (DEBUG >= 1)
			System.out.println("Rush-AI: checkTownhall");
		if (null == game.getUnitType(player, WTownhall.class)) {
			List<WUnit> peasants = game.getUnitTypes(player, WPeasant.class);
			WPeasant peasant = null;
			for(WUnit p:peasants) {
				if (p.getStatus()!=null &&
						p.getStatus().m_action==S3Action.ACTION_BUILD &&
						p.getStatus().m_parameters.get(0).equals(WTownhall.class.getSimpleName())) {
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
			Pair<Integer, Integer> loc = game.findFreeSpace(peasant.getX(), peasant.getY(), 3);
			requests.add(new Request(200,peasant.entityID,1200,800,new S3Action(peasant.entityID,S3Action.ACTION_BUILD, WTownhall.class.getSimpleName(), loc.m_a, loc.m_b)));
		}
		return requests;
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
