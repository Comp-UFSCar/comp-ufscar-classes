/*********************************************************************************
Organization 					: 				Georgia Institute of Technology
												Cognitive Computing Lab (CCL)
Authors							: 				Jai Rad
												Santi Ontanon
 ****************************************************************************/
package s3.entities;

import gatech.mmpm.Entity;
import s3.base.S3Action;



public class WTower extends WBuilding {

	public WTower() {
		setConstants();
	}

	public WTower(WTower incoming) {
		super(incoming);
		setConstants();
	}

	private void setConstants() {
		attack = 5;
		range = 7;
		max_hitpoints = 110;
		width = 2;
		length = 2;
		cost_gold = 900;
		cost_wood = 300;
		speed = 40;
		this.spriteName = "tower";
		//Always stand ground!
		status = new S3Action(entityID,S3Action.ACTION_STAND_GROUND);
	}

	public Object clone() {
		WTower e = new WTower(this);
		return e;
	}

	public static boolean isActive() {
		return true;
	}

    public Entity toD2Entity() {
		s3.mmpm.entities.WTower ret;
		ret = new s3.mmpm.entities.WTower(""+entityID, owner);
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