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
 * Distance between a coordinate and an entity. Both 
 * parameters must be specified.
 *          
 * @author David Llanso
 * @organization: Georgia Institute of Technology
 * @date 06-November-2009
 * 
 */
public class DistanceEC extends Sensor 
{
		
	public DistanceEC()
	{	
	} // Constructor

	//---------------------------------------------------------------

	public DistanceEC(DistanceEC dec) 
	{
		super(dec);

	} // Copy constructor 

	//---------------------------------------------------------------

	public Object clone() 
	{
		return new DistanceEC();
	
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
		return ActionParameterType.FLOAT;
		
	} // getType
	
	//---------------------------------------------------------------

	public Object evaluate(int cycle, GameState gs, String player, Context parameters) 
	{
		Entity e = getEntityParam(parameters,"entity");
		float[] coor = getCoorParam(parameters,"coor");
		float d = 0;

		if(coor != null && e!=null && e instanceof PhysicalEntity)
			d = (float)gs.getMap().distance(coor,
									((PhysicalEntity)e).get_Coords());
		else
			d = Float.MAX_VALUE;

//        System.out.println("DistanceEC: " + d);
        return d;
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
		_listOfNeededParameters.add(new Pair<String,ActionParameterType>("entity", ActionParameterType.ENTITY_ID));
		_listOfNeededParameters.add(new Pair<String,ActionParameterType>("coor", ActionParameterType.COORDINATE));
		
	} // static initializer

} // Class DistanceEC
