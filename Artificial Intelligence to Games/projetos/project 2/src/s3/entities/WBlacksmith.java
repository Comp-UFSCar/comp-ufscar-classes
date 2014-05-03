/*********************************************************************************
Organization 					: 				Georgia Institute of Technology
												Cognitive Computing Lab (CCL)
Authors							: 				Jai Rad
												Santi Ontanon
****************************************************************************/
package s3.entities;

import gatech.mmpm.Entity;


public class WBlacksmith extends WBuilding {


	public WBlacksmith()
	{
		setConstants();
	}
	private void setConstants() {
		max_hitpoints = 775;
		width = 3;
		length = 3;
		cost_gold = 800;
		cost_wood = 450;

		this.spriteName = "blacksmith";
	}
	public WBlacksmith( WBlacksmith incoming )
	{
		super(incoming);
		setConstants();
	}
	public Object clone() {
		WBlacksmith e = new WBlacksmith(this);
		return e;
	}


	public static boolean isActive() 
	{
		return true;
	}

    public Entity toD2Entity() {
		s3.mmpm.entities.WBlacksmith ret;
		ret = new s3.mmpm.entities.WBlacksmith(""+entityID, owner);
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