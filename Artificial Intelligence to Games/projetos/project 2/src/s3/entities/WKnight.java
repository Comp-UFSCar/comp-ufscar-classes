/*********************************************************************************
Organization 					: 				Georgia Institute of Technology
												Cognitive Computing Lab (CCL)
Authors							: 				Jai Rad
												Santi Ontanon
 ****************************************************************************/
package s3.entities;

import gatech.mmpm.Entity;


public class WKnight extends WTroop {

	public WKnight() {
		setConstants();
	}

	public WKnight(WKnight incoming) {
		super(incoming);
		setConstants();
	}

	private void setConstants() {
		attack = 8;
		max_hitpoints = 90;
		width = 1;
		length = 1;
		cost_gold = 800;
		cost_wood = 100;
		speed = 18;
		this.spriteName = "knight";
	}

	public Object clone() {
		WKnight e = new WKnight(this);
		return e;
	}

	public static boolean isActive() {
		return true;
	}


    public Entity toD2Entity() {
		s3.mmpm.entities.WKnight ret;
		ret = new s3.mmpm.entities.WKnight(""+entityID, owner);
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