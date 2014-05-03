/*********************************************************************************
Organization 					: 				Georgia Institute of Technology
												Cognitive Computing Lab (CCL)
Authors							: 				Jai Rad
												Santi Ontanon
****************************************************************************/
package s3.entities;


public abstract class WOMapEntity extends S3PhysicalEntity {


	public WOMapEntity()
	{
	}
	public WOMapEntity( WOMapEntity incoming )
	{
		super(incoming);
	}


	public static boolean isActive() 
	{
		return false;
	}
}