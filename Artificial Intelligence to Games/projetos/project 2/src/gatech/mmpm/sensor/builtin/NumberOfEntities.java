/* Copyright 2010 Santiago Ontanon and Ashwin Ram */

package gatech.mmpm.sensor.builtin;


import gatech.mmpm.ActionParameterType;
import gatech.mmpm.Context;
import gatech.mmpm.Entity;
import gatech.mmpm.GameState;
import gatech.mmpm.sensor.Sensor;
import gatech.mmpm.util.Pair;
import java.util.HashMap;

/**
 * Sensor that returns the number of entities of one type, one 
 * player, one type and one player or the total number of entities 
 * depending on the specified parameters. The type and the 
 * owner of the requested entity may be given or not.
 *          
 * @author David Llanso
 * @organization: Georgia Institute of Technology
 * @date 12-November-2009
 * 
 */
public class NumberOfEntities extends Sensor 
{
		
	public NumberOfEntities()
	{	
	} // Constructor

	//---------------------------------------------------------------

	public NumberOfEntities(NumberOfEntities noe) 
	{
		super(noe);

	} // Copy constructor 

	//---------------------------------------------------------------

	public Object clone() 
	{
		return new NumberOfEntities();
	
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
		return ActionParameterType.INTEGER;
		
	} // getType
	
	//---------------------------------------------------------------

	public Object evaluate(int cycle, GameState gs, String player, Context parameters) 
	{
		Class<? extends gatech.mmpm.Entity> type = getTypeParam(parameters,"type");
		String owner = getStringParam(parameters,"owner");
		
		int count = 0;
		
//		System.out.println("NumberofEntities: type = " + type + " , owner = " + owner);
//		HashMap<String,Integer> countPerPlayer = new HashMap<String,Integer>();


		for(Entity e:gs.getAllEntities())
		{
			if( (type == null || type.isInstance(e)) &&
				(owner == null || owner.equals(e.getowner())) )
			{
  //                              if (countPerPlayer.get(e.getowner())==null) {
  //                                  countPerPlayer.put(e.getowner(),1);
  //                              } else {
  //                                  countPerPlayer.put(e.getowner(), countPerPlayer.get(e.getowner())+1);
  //                              }
				count++;
			}
		}
		
  //		System.out.println("NumberofEntities: count = " + count);
  //            for(String p:countPerPlayer.keySet()) System.out.println(p + " -> " + countPerPlayer.get(p));
		
		return count;
		
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
		_listOfNeededParameters.add(new Pair<String,ActionParameterType>("owner", ActionParameterType.PLAYER));

	} // static initializer

} // Class NumberOfEntities
