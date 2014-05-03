/*********************************************************************************
Organization 					: 				Georgia Institute of Technology
												Cognitive Computing Lab (CCL)
Authors							: 				Jai Rad
												Santi Ontanon
 ****************************************************************************/
package s3.entities;

import gatech.mmpm.Entity;


public class WGoldMine extends WBuilding {
	public int remaining_gold;

	public WGoldMine() {
		setConstants();
	}

	private void setConstants() {
		max_hitpoints = 25500;
		width = 3;
		length = 3;
		this.spriteName = "goldmine";
	}

	public WGoldMine(WGoldMine incoming) {
		super(incoming);
		this.remaining_gold = incoming.remaining_gold;
		setConstants();
	}

	public Object clone() {
		WGoldMine e = new WGoldMine(this);
		return e;
	}

	public static boolean isActive() {
		return false;
	}

	public int getRemaining_gold() {
		return remaining_gold;
	}

	public void setRemaining_gold(int incoming) {
		this.remaining_gold = incoming;
	}

    public Entity toD2Entity() {
		s3.mmpm.entities.WGoldMine ret;
		ret = new s3.mmpm.entities.WGoldMine(""+entityID, owner);
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