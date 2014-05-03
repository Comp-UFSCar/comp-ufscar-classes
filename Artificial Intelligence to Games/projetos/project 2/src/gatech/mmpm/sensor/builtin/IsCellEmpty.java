/* Copyright 2010 Santiago Ontanon and Ashwin Ram */

package gatech.mmpm.sensor.builtin;


import gatech.mmpm.ActionParameterType;
import gatech.mmpm.Context;
import gatech.mmpm.GameState;
import gatech.mmpm.sensor.Sensor;
import gatech.mmpm.util.Pair;

/**
 * This condition tests whether a cell of the map is empty
 *          
 * @author David Llanso
 * @organization: Georgia Institute of Technology
 * @date 05-November-2009
 * 
 */
public class IsCellEmpty extends Sensor 
{
		
	public IsCellEmpty()
	{	
	} // Constructor

	//---------------------------------------------------------------

	public IsCellEmpty(IsCellEmpty ice) 
	{
		super(ice);

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
		float[] coor = getCoorParam(parameters,"coor");
		
		if (coor == null) {

//			System.out.println("Coordinates " + coor + " are not, empty -> null!");
			
			return 0.0f;
		}
		
		if (gs.getEntityAt(coor) != null) {
			
//			System.out.println("Coordinates " + coor + " are not empty, there is a " + gs.getEntityAt(coor));
			
			return 0.0f;
		}
		
		return 1.0f;
		
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
		_listOfNeededParameters.add(new Pair<String,ActionParameterType>("coor", ActionParameterType.COORDINATE));
		
	} // static initializer

} // Class IsCellEmpty
