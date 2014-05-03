/**
 * 
 */
package s3.ai.builtin;

import java.util.LinkedList;
import java.util.List;

import s3.base.S3;
import s3.base.S3Action;
import s3.entities.S3Entity;
import s3.entities.WBuilding;
import s3.entities.WCatapult;
import s3.entities.WBarracks;
import s3.entities.WBlacksmith;
import s3.entities.WLumberMill;
import s3.entities.WPeasant;
import s3.entities.WPlayer;
import s3.entities.WUnit;
import s3.util.Pair;

/**
 * @author Santi: AI that builds a barracks, blacksmith and lumbermill, two peasants harvesting gold, and two harvesting wood, 
 *         then builds
 * 	       catapults without stop and attacks! After a while, increases the number of peasants to 3 and 3, and builds a
 * 		   second barracks. It also looks for goldmines where there is still available gold
 * 		   Also, it sends catapults in waves
 */
public class CatapultRush extends FootmenRush {
	
	public CatapultRush(String playerID) {
		super(playerID);
		troopClass = WCatapult.class;
	}

	List<Request> checkBarracks(S3 game, WPlayer player, List<S3Action> actions) {
		Class []buildOrder = {WLumberMill.class,WBlacksmith.class,WBarracks.class};
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

				Pair<Integer, Integer> loc = game.findFreeSpace(peasant.getX(), peasant.getY(), b.getWidth()+1);
				if (null == loc) {
					loc = game.findFreeSpace(peasant.getX(), peasant.getY(), b.getWidth());
					if (loc==null) return requests;
				}
				
				if (DEBUG >= 1)
					System.out.println("Rush-AI: building " + buildOrder[i].getSimpleName() + " at " + loc.m_a + " , " + loc.m_b);

				requests.add(new Request(150,peasant.entityID,b.getCost_gold(),b.getCost_wood(),new S3Action(peasant.entityID,S3Action.ACTION_BUILD, buildOrder[i].getSimpleName(), loc.m_a, loc.m_b)));				
			}
		}
				
		return requests;
	}

}
