/*********************************************************************************
Organization 					: 				Georgia Institute of Technology
												Cognitive Computing Lab (CCL)
Authors							: 				Jai Rad
												Santi Ontanon
****************************************************************************/
package s3.entities;

import gatech.mmpm.Entity;


public class WOGrass extends WOMapEntity {


	public WOGrass()
	{
		this.spriteName = "grass";
	}
	public WOGrass( WOGrass incoming )
	{
		super(incoming);
		this.spriteName = "grass";
	}
	public Object clone() {
		WOGrass e = new WOGrass(this);
		return e;
	}


	public static boolean isActive() 
	{
		return false;
	}

    public Entity toD2Entity() {
		s3.mmpm.entities.WOGrass ret;
		ret = new s3.mmpm.entities.WOGrass(""+entityID, owner);
		ret.setx(x);
		ret.sety(y);
		return ret;
	}

}