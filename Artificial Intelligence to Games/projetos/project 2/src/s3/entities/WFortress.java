/*********************************************************************************
Organization 					: 				Georgia Institute of Technology
												Cognitive Computing Lab (CCL)
Authors							: 				Jai Rad
												Santi Ontanon
 ****************************************************************************/
package s3.entities;

import gatech.mmpm.Entity;


public class WFortress extends WBuilding {

	public WFortress() {
		setConstants();
	}

	private void setConstants() {
		max_hitpoints = 1600;
		width = 4;
		length = 4;
		cost_gold = 2500;
		cost_wood = 1200;
		this.spriteName = "fortress";
	}

	public WFortress(WFortress incoming) {
		super(incoming);
		setConstants();
	}

	public Object clone() {
		WFortress e = new WFortress(this);
		return e;
	}

	public static boolean isActive() {
		return true;
	}


    public gatech.mmpm.Entity toD2Entity() {
		s3.mmpm.entities.WFortress ret;
		ret = new s3.mmpm.entities.WFortress(""+entityID, owner);
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