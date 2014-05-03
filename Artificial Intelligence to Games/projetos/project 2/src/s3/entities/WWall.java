package s3.entities;

import gatech.mmpm.Entity;


/**
 * @author kane
 *
 */
public class WWall extends WBuilding {

	/**
	 * Default constructor
	 */
	public WWall() {
		setConstants();
	}

	/**
	 * Clones the given building
	 * @param incoming
	 */
	public WWall(WBuilding incoming) {
		super(incoming);
		setConstants();
	}
	
	private void setConstants() {
		max_hitpoints = 100;
		width = 1;
		length = 1;
		cost_gold = 25;
		cost_wood = 25;
		this.spriteName = "wall";
		
	}

	public Object clone() {
		WWall e = new WWall(this);
		return e;
	}

	public static boolean isActive() {
		return true;
	}


    public Entity toD2Entity() {
		s3.mmpm.entities.WWall ret;
		ret = new s3.mmpm.entities.WWall(""+entityID, "P" + owner);
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
