/* Copyright 2010 Santiago Ontanon and Ashwin Ram */

package gatech.mmpm.sensor.builtin;


import gatech.mmpm.ActionParameterType;
import gatech.mmpm.Context;
import gatech.mmpm.Entity;
import gatech.mmpm.GameState;
import gatech.mmpm.PhysicalEntity;
import gatech.mmpm.sensor.Sensor;
import gatech.mmpm.util.Pair;

/**
 * This condition returns the closest entity to a given coordinate.
 * The type and the owner of the requested entity may be given or not.
 *          
 * @author David Llanso
 * @organization: Georgia Institute of Technology
 * @date 06-November-2009
 * 
 */
public class ClosestEntity extends Sensor 
{
		
	public ClosestEntity()
	{	
	} // Constructor

	//---------------------------------------------------------------

	public ClosestEntity(ClosestEntity ce) 
	{
		super(ce);

	} // Copy constructor 

	//---------------------------------------------------------------

	public Object clone() 
	{
		return new ClosestEntity();
	
	} // clone

	//---------------------------------------------------------------

	/**
	 * Return the type of the sensor.
	 * 
	 * Keep in mind that this is <em>not</em> the real Java type,
	 * but the MMPM type. See the 
	 * gatech.mmpm.ActionParameterType.getJavaType() method
	 * for more information.
	 * 
	 * @return Type of the sensor. 
	 */
	public ActionParameterType getType() 
	{
		return ActionParameterType.ENTITY_ID;
		
	} // getType
	
	//---------------------------------------------------------------

	public Object evaluate(int cycle, GameState gs, String player, Context parameters) 
	{
		Class<? extends Entity> type = getTypeParam(parameters,"type");
		float[] coor = getCoorParam(parameters,"coor");
		String owner = getStringParam(parameters,"owner");
		
		if(coor == null)
			return null;
		
		double closestDistance = Float.MAX_VALUE;
		double newDistance;
		PhysicalEntity closestPE = null;
		PhysicalEntity newPE;
		
		for(Entity e: gs.getAllEntities())
		{
			if( (e instanceof PhysicalEntity) &&
			    ((type == null) || (type == e.getClass())) &&
			    ((owner == null) || e.getowner().equals(owner)) )
			{
				newPE = ((PhysicalEntity)e);
				newDistance = gs.getMap().distance(coor,
										newPE.get_Coords());
				if(newDistance < closestDistance)
				{
					closestDistance = newDistance;
					closestPE = newPE;
				}
			}
		}
		
		return closestPE;
		
	} // evaluate

	//---------------------------------------------------------------
	
	/**
	 * Public method that provides the parameters that 
	 * this sensor uses to be evaluated. This method provides
	 * all the parameters that can be used in the evaluation, 
	 * nevertheless some sensor can be evaluated with only 
	 * some of them.
	 * 
	 * @return The list of needed parameters this sensor needs
	 * to be evaluated.
	 */
	public java.util.List<Pair<String,ActionParameterType>> getNeededParameters() {

		return _listOfNeededParameters;
	
	} // getNeededParameters

	//---------------------------------------------------------------
	
	/**
	 * Public static method that provides the parameters that 
	 * this sensor uses to be evaluated. This method provides
	 * all the parameters that can be used in the evaluation, 
	 * nevertheless some sensor can be evaluated with only 
	 * some of them.
	 * 
	 * @return The list of needed parameters this sensor needs
	 * to be evaluated.
	 */
	public static java.util.List<Pair<String,ActionParameterType>> getStaticNeededParameters() {

		return _listOfNeededParameters;
	
	} // getStaticNeededParameters
	
	//---------------------------------------------------------------
	//                       Static fields
	//---------------------------------------------------------------
	
	static java.util.List<Pair<String,ActionParameterType>> _listOfNeededParameters;
	
	//---------------------------------------------------------------
	//                       Static initializers
	//---------------------------------------------------------------

	static {

		// Add parameters to _listOfNeededParameters.
		_listOfNeededParameters = new java.util.LinkedList<Pair<String,ActionParameterType>>(gatech.mmpm.sensor.Sensor.getStaticNeededParameters());
		_listOfNeededParameters.add(new Pair<String,ActionParameterType>("type", ActionParameterType.ENTITY_TYPE));
		_listOfNeededParameters.add(new Pair<String,ActionParameterType>("coor", ActionParameterType.COORDINATE));
		_listOfNeededParameters.add(new Pair<String,ActionParameterType>("owner", ActionParameterType.PLAYER));
		
	} // static initializer

} // Class ClosestEntity
