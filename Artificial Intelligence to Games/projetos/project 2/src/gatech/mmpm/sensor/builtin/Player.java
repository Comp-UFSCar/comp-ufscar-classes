/* Copyright 2010 Santiago Ontanon and Ashwin Ram */

package gatech.mmpm.sensor.builtin;


import gatech.mmpm.ActionParameterType;
import gatech.mmpm.Context;
import gatech.mmpm.GameState;
import gatech.mmpm.sensor.Sensor;
import gatech.mmpm.util.Pair;

/**
 * Sensor that turns a string into an ENTITY_TYPE. The 'type' 
 * parameter must be specified.
 *  
 * @author David Llanso
 * @organization: Georgia Institute of Technology
 * @date 12-November-2009
 *  
 */
public class Player extends Sensor 
{

	public Player() 
	{
	} // Constructor

	//---------------------------------------------------------------

	public Player(Player p) 
	{
		super(p);

	} // Copy constructor 

	//---------------------------------------------------------------

	public Object clone() 
	{
		return new Player();
		
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
		return ActionParameterType.PLAYER;
		
	} // getType

	//---------------------------------------------------------------

	public Object evaluate(int cycle, GameState gs, String player, Context parameters) 
	{
		String thisplayer = getStringParam(parameters,"player");
		if(thisplayer == null)
			return null;
		
		return ActionParameterType.PLAYER.fromString(thisplayer);
		
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
		_listOfNeededParameters.add(new Pair<String,ActionParameterType>("player", ActionParameterType.PLAYER));

	} // static initializer
	
} // class Player
