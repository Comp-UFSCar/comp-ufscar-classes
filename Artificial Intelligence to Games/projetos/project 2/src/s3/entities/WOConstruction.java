/*********************************************************************************
Authors							: 				Santi Ontanon
****************************************************************************/
package s3.entities;

import gatech.mmpm.Entity;


public class WOConstruction extends WOMapEntity {


	public WOConstruction()
	{
		this.spriteName = "construction";
	}
	public WOConstruction( WOConstruction incoming )
	{
		super(incoming);
		this.spriteName = "construction";
	}
	public Object clone() {
		WOConstruction e = new WOConstruction(this);
		return e;
	}


	public static boolean isActive() 
	{
		return false;
	}

    public Entity toD2Entity() {
		s3.mmpm.entities.WOConstruction ret;
		ret = new s3.mmpm.entities.WOConstruction(""+entityID, owner);
		ret.setx(x);
		ret.sety(y);
		return ret;
	}
}