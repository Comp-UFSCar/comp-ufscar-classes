/*******************************************************************
 *                       MACHINE GENERATED CODE                    *
 *                            DO NOT EDIT                          *
 *        FIX THE SOURCE XML INSTEAD, AND REGENERATE IT AGAIN      *
 *                                                                 *
 *                                                                 *
 * Tool: gatech.mmpm.tools.DomainGenerator                         *
 *                                                                 *
 * Organization: Georgia Institute of Technology                   *
 *               Cognitive Computing Lab (CCL)                     *
 * Authors:      Pedro Pablo Gomez Martin                          *
 *               Marco Antonio Gomez Martin                        *
 * Based on previous work of:                                      *
 *               Jai Rad                                           *
 *               Prafulla Mahindrakar                              *
 *               Santi Onta��n                                     *
 *******************************************************************/


package s3.mmpm.sensors;

import gatech.mmpm.Context;
import gatech.mmpm.GameState;
import gatech.mmpm.sensor.Sensor;
import gatech.mmpm.ActionParameterType;
import gatech.mmpm.util.Pair;




/**
 * Class that represents a particular sensor type
 * of the game. It contains machine generate code.
 * Go to gatech.mmpm.Sensor for more information.
 */
public class NumberOfNonNeutralEntities extends Sensor {

	public NumberOfNonNeutralEntities() {
	} // Constructor

	//---------------------------------------------------------------

	public NumberOfNonNeutralEntities(NumberOfNonNeutralEntities rhs) {

		super(rhs);

	} // Copy constructor 
	
	//---------------------------------------------------------------

	public Object clone() {

		NumberOfNonNeutralEntities e = new NumberOfNonNeutralEntities(this);
		return e;

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
	public ActionParameterType getType() {
	
		return ActionParameterType.INTEGER;

	} // getType

	//---------------------------------------------------------------
	
	public Object evaluate(int cycle, GameState gs, String player, Context parameters) {

		Class<? extends gatech.mmpm.Entity> type = getTypeParam(parameters,"type");
		String owner = getStringParam(parameters,"owner");

		int count = 0;

		for(gatech.mmpm.Entity e:gs.getAllEntities())
		{
			if (e.getowner()!=null && !(e instanceof s3.mmpm.entities.WPlayer)) {
				if( (type == null || type == e.getClass()) &&
					(owner == null || owner.equals(e.getowner())) )
				{
					count++;
				}
			}
		}
		return count;
	
	} // evaluate
	
	//---------------------------------------------------------------
	
	/**
	 * Protected method called from equivalents to compare
	 * two sensors. Subclasses must override this method to
	 * decide if a sensor of the current class is equivalent
	 * to the current sensor.
	 * 
	 * @param s Sensor to compare the current one with.
	 * It will always be an instance of the current class.
	 * @return True if both sensors are equivalents.
	 * 
	 * @note This method should never be externally called.
	 * Use equivalents() instead.
	 */
	protected boolean internalEquivalents(Sensor s) {

		// Auto-generated sensors of the same class are
		// always equivalent in between.
		return true;

	} // internalEquivalents

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

} // class NumberOfNonNeutralEntities

/*******************************************************************
 *                       MACHINE GENERATED CODE                    *
 *                            DO NOT EDIT                          *
 *        FIX THE SOURCE XML INSTEAD, AND REGENERATE IT AGAIN      *
 *                                                                 *
 *                                                                 *
 * Tool: gatech.mmpm.tools.DomainGenerator                         *
 *                                                                 *
 * Organization: Georgia Institute of Technology                   *
 *               Cognitive Computing Lab (CCL)                     *
 * Authors:      Pedro Pablo Gomez Martin                          *
 *               Marco Antonio Gomez Martin                        *
 * Based on previous work of:                                      *
 *               Jai Rad                                           *
 *               Prafulla Mahindrakar                              *
 *               Santi Onta��n                                     *
 *******************************************************************/

