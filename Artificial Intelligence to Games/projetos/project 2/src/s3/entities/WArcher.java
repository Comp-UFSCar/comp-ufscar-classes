/*********************************************************************************
Organization 					: 				Georgia Institute of Technology
												Cognitive Computing Lab (CCL)
Authors							: 				Jai Rad
												Santi Ontanon
 ****************************************************************************/
package s3.entities;

import gatech.mmpm.Entity;


public class WArcher extends WTroop {

	public WArcher() {
		setConstants();
	}

	public WArcher(WArcher incoming) {
		super(incoming);
		setConstants();
	}

	private void setConstants() {
		attack = 4;
		range = 4;
		max_hitpoints = 50;
		width = 1;
		length = 1;
		cost_gold = 500;
		cost_wood = 50;
		this.spriteName = "archer";
	}

	public Object clone() {
		WArcher e = new WArcher(this);
		return e;
	}

	public static boolean isActive() {
		return true;
	}

    public Entity toD2Entity() {
		s3.mmpm.entities.WArcher ret;
		ret = new s3.mmpm.entities.WArcher(""+entityID, owner);
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