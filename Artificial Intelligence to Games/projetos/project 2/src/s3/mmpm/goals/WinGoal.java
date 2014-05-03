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


package s3.mmpm.goals;

import gatech.mmpm.Context;
import gatech.mmpm.GameState;
import gatech.mmpm.sensor.Sensor;
import gatech.mmpm.ActionParameterType;
import gatech.mmpm.util.Pair;
import gatech.mmpm.sensor.composite.Invocation;
import gatech.mmpm.util.Pair;
import gatech.mmpm.sensor.composite.GetContextValue;
import gatech.mmpm.sensor.builtin.FLOAT;
import s3.mmpm.sensors.NumberOfNonNeutralEntities;
import gatech.mmpm.sensor.composite.ArithmeticSensor;
import gatech.mmpm.sensor.constant.ConstantInteger;
import gatech.mmpm.sensor.composite.Conditional;
import gatech.mmpm.sensor.constant.ConstantFloat;
import gatech.mmpm.sensor.composite.EqualitySensor;




/**
 * Class that represents a particular sensor type
 * of the game. It contains machine generate code.
 * Go to gatech.mmpm.Sensor for more information.
 */
public class WinGoal extends Sensor {

	public WinGoal() {
	} // Constructor

	//---------------------------------------------------------------

	public WinGoal(WinGoal rhs) {

		super(rhs);

	} // Copy constructor 
	
	//---------------------------------------------------------------

	public Object clone() {

		WinGoal e = new WinGoal(this);
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
	
		return ActionParameterType.BOOLEAN;

	} // getType

	//---------------------------------------------------------------
	
	public Object evaluate(int cycle, GameState gs, String player, Context parameters) {

		return _backend.evaluate(cycle, gs, player, parameters);
	
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
	
	
	/**
	 * Backend sensor, that makes the real evaluation.
	 * This sensor has been generated using the built-in
	 * MMPM language, and this field will be 
	 */
	static Sensor _backend = new Conditional(new EqualitySensor(new Invocation(new NumberOfNonNeutralEntities(), new Pair<String, Sensor>("type", null), new Pair<String, Sensor>("owner", null)), EqualitySensor.Operator.EQUAL_THAN, new ConstantInteger(0)), new ConstantFloat(0.5), new ArithmeticSensor(new Invocation(new FLOAT(), new Pair<String, Sensor>("value", new Invocation(new NumberOfNonNeutralEntities(), new Pair<String, Sensor>("type", null), new Pair<String, Sensor>("owner", new GetContextValue("player", ActionParameterType.PLAYER))))), ArithmeticSensor.Operator.DIV, new Invocation(new FLOAT(), new Pair<String, Sensor>("value", new Invocation(new NumberOfNonNeutralEntities(), new Pair<String, Sensor>("type", null), new Pair<String, Sensor>("owner", null))))));

	
	//---------------------------------------------------------------
	//                       Static initializers
	//---------------------------------------------------------------

	static {

		// Add parameters to _listOfNeededParameters.
		_listOfNeededParameters = new java.util.LinkedList<Pair<String,ActionParameterType>>(gatech.mmpm.sensor.Sensor.getStaticNeededParameters());
		

	} // static initializer

} // class WinGoal

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

