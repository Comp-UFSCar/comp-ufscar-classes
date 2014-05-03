/*********************************************************************************
Organization 					: 				Georgia Institute of Technology
												Cognitive Computing Lab (CCL)
Authors							: 				Jai Rad
												Santi Ontanon
 ****************************************************************************/
package s3.entities;

import gatech.mmpm.Entity;


public class WCatapult extends WTroop {

	public WCatapult() {
		setConstants();
	}

	public WCatapult(WCatapult incoming) {
		super(incoming);
		setConstants();
	}

	private void setConstants() {
		attack = 25;
		range = 8;
		max_hitpoints = 100;
		width = 1;
		length = 1;
		cost_gold = 900;
		cost_wood = 300;
		speed = 100;
		this.spriteName = "catapult";
	}

	public Object clone() {
		WCatapult e = new WCatapult(this);
		return e;
	}

	public static boolean isActive() {
		return true;
	}

    public Entity toD2Entity() {
		s3.mmpm.entities.WCatapult ret;
		ret = new s3.mmpm.entities.WCatapult(""+entityID, owner);
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