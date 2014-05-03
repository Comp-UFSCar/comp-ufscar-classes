/*********************************************************************************
Organization 					: 				Georgia Institute of Technology
												Cognitive Computing Lab (CCL)
Authors							: 				Jai Rad
												Santi Ontanon
****************************************************************************/
package s3.entities;

import gatech.mmpm.Entity;


public class WOTree extends WOMapEntity {


	public WOTree()
	{
		this.spriteName = "tree";
	}
	public WOTree( WOTree incoming )
	{
		super(incoming);
		this.spriteName = "tree";
	}
	public Object clone() {
		WOTree e = new WOTree(this);
		return e;
	}


	public static boolean isActive() 
	{
		return false;
	}

    public Entity toD2Entity() {
		s3.mmpm.entities.WOTree ret;
		ret = new s3.mmpm.entities.WOTree(""+entityID, owner);
		ret.setx(x);
		ret.sety(y);
		return ret;
	}

}