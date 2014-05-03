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
 * This condition tests whether two entities are neighbors.
 *          
 * @author David Llanso
 * @organization: Georgia Institute of Technology
 * @date 05-November-2009
 * 
 */
public class InNeighborsCells extends Sensor 
{
		
	public InNeighborsCells()
	{	
	} // Constructor

	//---------------------------------------------------------------

	public InNeighborsCells(InNeighborsCells inc) 
	{
		super(inc);

	} // Copy constructor 

	//---------------------------------------------------------------

	public Object clone() 
	{
		return new HasAttribute();
	
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
		return ActionParameterType.BOOLEAN;
		
	} // getType
	
	//---------------------------------------------------------------

	public Object evaluate(int cycle, GameState gs, String player, Context parameters) 
	{
		Entity e1 = getEntityParam(parameters,"entity1");
		Entity e2 = getEntityParam(parameters,"entity2");

		PhysicalEntity pe1 = e1 instanceof PhysicalEntity? (PhysicalEntity) e1 : null;
		PhysicalEntity pe2 = e2 instanceof PhysicalEntity? (PhysicalEntity) e2 : null;
		
		if ( (pe1 != null) && (pe2 != null) && 
				gs.getMap().areNeighbors(pe1, pe2) ) 
		{
			return 1.0f;
		}
		
		return 0.0f;
		
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
		_listOfNeededParameters.add(new Pair<String,ActionParameterType>("entity1", ActionParameterType.ENTITY_ID));
		_listOfNeededParameters.add(new Pair<String,ActionParameterType>("entity2", ActionParameterType.ENTITY_ID));
		
	} // static initializer

} // Class InNeighborsCells
