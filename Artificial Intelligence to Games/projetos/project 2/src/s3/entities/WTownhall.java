/*********************************************************************************
Organization 					: 				Georgia Institute of Technology
												Cognitive Computing Lab (CCL)
Authors							: 				Jai Rad
												Santi Ontanon
 ****************************************************************************/
package s3.entities;

import gatech.mmpm.Entity;
import s3.base.S3Action;



public class WTownhall extends WBuilding {

	public WTownhall() {
		setConstants();
	}

	public WTownhall(WTownhall incoming) {
		super(incoming);
		setConstants();
	}

	private void setConstants() {
		max_hitpoints = 1200;
		width = 4;
		length = 4;
		cost_gold = 1200;
		cost_wood = 800;
		this.spriteName = "townhall";
		
		actionList.add(S3Action.ACTION_TRAIN);
		allowedUnits.add(WPeasant.class.getSimpleName());
	}

	public Object clone() {
		WTownhall e = new WTownhall(this);
		return e;
	}

	public static boolean isActive() {
		return true;
	}

    public Entity toD2Entity() {
		s3.mmpm.entities.WTownhall ret;
		ret = new s3.mmpm.entities.WTownhall(""+entityID, owner);
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