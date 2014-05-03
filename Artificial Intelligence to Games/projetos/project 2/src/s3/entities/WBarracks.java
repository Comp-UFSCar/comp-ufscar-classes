/*********************************************************************************
Organization 					: 				Georgia Institute of Technology
												Cognitive Computing Lab (CCL)
Authors							: 				Jai Rad
												Santi Ontanon
 ****************************************************************************/
package s3.entities;

import gatech.mmpm.Entity;
import java.util.List;

import s3.base.S3;
import s3.base.S3Action;

public class WBarracks extends WBuilding {

	public WBarracks() {
		setConstants();
	}

	private void setConstants() {
		max_hitpoints = 800;
		width = 3;
		length = 3;
		cost_gold = 700;
		cost_wood = 450;
		this.spriteName = "barracks";

		actionList.add(S3Action.ACTION_TRAIN);
		allowedUnits.add(WFootman.class.getSimpleName());
	}

	public WBarracks(WBarracks incoming) {
		super(incoming);
		setConstants();
	}

	public Object clone() {
		WBarracks e = new WBarracks(this);
		return e;
	}

	public static boolean isActive() {
		return true;
	}

	/**
	 * calls super and checks for training dependencies
	 */
	public void cycle(int m_cycle, S3 m_game, List<S3Action> failedActions) {
		super.cycle(m_cycle, m_game, failedActions);
		if (m_cycle % 25 == 0) {
			WPlayer player = m_game.getPlayer(owner);
			if (null == m_game.getUnitType(player, WStable.class)) {
				allowedUnits.remove(WKnight.class.getSimpleName());
			} else {
				addAllowed(WKnight.class.getSimpleName());
			}
			
			if (null == m_game.getUnitType(player, WBlacksmith.class)) {
				allowedUnits.remove(WCatapult.class.getSimpleName());
			} else {
				addAllowed(WCatapult.class.getSimpleName());
			}
			
			if (null == m_game.getUnitType(player, WLumberMill.class)) {
				allowedUnits.remove(WArcher.class.getSimpleName());
			} else {
				addAllowed(WArcher.class.getSimpleName());
			}
		}
	}

    public Entity toD2Entity() {
		s3.mmpm.entities.WBarracks ret;
		ret = new s3.mmpm.entities.WBarracks(""+entityID, owner);
		ret.setx(x);
		ret.sety(y);
		ret.setCurrent_hitpoints(current_hitpoints);
		ret.setCycle_created(cycle_created);
		ret.setCycle_last_attacked(cycle_last_attacked);
		ret.setRange(range);
		ret.setAttack(attack);
		if (creator==null) ret.setCreator("");
		  else ret.setCreator(creator.getEntityID()+"");
		if (status==null) ret.setStatus("0");
                     else ret.setStatus("" + status.m_action);
		return ret;
	}
}