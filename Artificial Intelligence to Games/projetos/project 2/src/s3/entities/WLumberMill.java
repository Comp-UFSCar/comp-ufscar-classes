/**
 * 
 */
package s3.entities;

import gatech.mmpm.Entity;


/**
 * @author gtg216r
 * 
 */
public class WLumberMill extends WBuilding {

	public WLumberMill() {
		setConstants();
	}

	private void setConstants() {
		max_hitpoints = 600;
		width = 3;
		length = 3;
		cost_gold = 600;
		cost_wood = 450;
		this.spriteName = "lumbermill";
	}

	public WLumberMill(WLumberMill incoming) {
		super(incoming);
		setConstants();
	}

	public Object clone() {
		WLumberMill e = new WLumberMill(this);
		return e;
	}

	public static boolean isActive() {
		return true;
	}

    public Entity toD2Entity() {
		s3.mmpm.entities.WLumberMill ret;
		ret = new s3.mmpm.entities.WLumberMill(""+entityID, owner);
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
