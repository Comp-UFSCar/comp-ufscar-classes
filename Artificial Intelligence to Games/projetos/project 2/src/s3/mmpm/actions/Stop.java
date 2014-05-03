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


package s3.mmpm.actions;

import gatech.mmpm.Context;
import gatech.mmpm.ActionParameter;
import gatech.mmpm.ActionParameterType;
import gatech.mmpm.sensor.Sensor;
import gatech.mmpm.sensor.composite.Invocation;
import gatech.mmpm.sensor.constant.True;
import gatech.mmpm.util.Pair;
import gatech.mmpm.sensor.composite.GetContextValue;
import gatech.mmpm.sensor.builtin.Entity;
import gatech.mmpm.sensor.constant.False;
import gatech.mmpm.sensor.builtin.EntityExists;


import java.util.List;

/**
 * Class that represents a particular action type
 * of the game. It contains machine generate code.
 * Go to gatech.mmpm.Action for more information.
 */
public class Stop extends gatech.mmpm.Action {

	/**
	 * Constructor
	 * 
	 * @param entityID Entity identifier which receives
	 * the action.
	 * @param playerID Player identifier that makes
	 * the action.
	 */
	public Stop(String entityID, String playerID) {
	
		super(entityID, playerID);

	} // Constructor

	//---------------------------------------------------------------

	public Stop(Stop rhs) {

		super(rhs);

	} // Copy constructor 
	
	//---------------------------------------------------------------

	public Object clone() {

		Stop e = new Stop(this);
		return e;

	} // clone

	//---------------------------------------------------------------
	
	/**
	 * Returns a list with the names and types of the
	 * parameters that this action type has.
	 * 
	 * @return List of the action parameters.
	 * 
	 * @note This method must be overwritten in each
	 * subclass.
	 */
	public List<ActionParameter> listOfParameters() {
	
		return _listOfParameters;

	} // listOfParameters

	//---------------------------------------------------------------
	//                       Getter & setter
	//---------------------------------------------------------------



	//---------------------------------------------------------------
	//                 Generic getter and setter
	//---------------------------------------------------------------

	/**
	 * Returns a parameter value by its name.
	 * 
	 * @param parameter Parameter name which value want to be recovered.
	 * 
	 * @return Parameter value, or null if it do not exist.
	 * 
	 * @note This method MUST BE overwritten in subclasses if more
	 * parameters are added.
	 */
	public Object parameterValue(String parameter) {

			return super.parameterValue(parameter);
	
	} // parameterValue


	//---------------------------------------------------------------

	/**
	 * Returns a String with the value of a parameter by its name.
	 * 
	 * @param parameter Parameter name which value want to be recovered.
	 * 
	 * @return Parameter value (as string), or null if it do not exist.
	 * 
	 * @note This method MUST BE overwritten in subclasses if more
	 * parameters are added.
	 */
	public String parameterStringValue(String parameter) {

			return super.parameterStringValue(parameter);
	
	} // parameterStringValue

	//---------------------------------------------------------------

	/**
	 * Set a parameter value by its name.
	 * 
	 * @param parameter Parameter name which value want to be set.
	 * 
	 * @param value New value. It will be converted to the real
	 * data type.
	 * 
	 * @note This method MUST BE overwritten in subclasses if more
	 * parameters are added.
	 * 
	 * @note Although <tt>actionID</tt> is shown as an
	 * action parameter, it cannot be changed with this
	 * method (is a read-only parameter automatically
	 * established in the constructor). 
	 */
	public void setParameterValue(String parameter, String value) {

			super.setParameterValue(parameter, value);
	
	} // setParameterValue

	//---------------------------------------------------------------

	/**
	 * Returns the action context, in other words, a context
	 * with pairs with the action parameter names and their
	 * values.
	 * 
	 * @return The action context.
	 */
	public Context getContext() {

		Context result;
		result = new Context(super.getContext());

		return result;

	} // getContext

	//---------------------------------------------------------------
	//                    getXXXCondition()
	//---------------------------------------------------------------

	/**
	 * Returns the static precondition of the Action.
	 * Note that every instance of this class will return
	 * the same precondition. 
	 * Subclasses must "overwrite" this method.
	 * 
	 * @return The static precondition.
	 */
	public Sensor getPreCondition() 
	{
		return _preCondition;
	}

	//---------------------------------------------------------------

	/**
	 * Returns the static success condition of the Action.
	 * Note that every instance of this class will return
	 * the same success condition. 
	 * Subclasses must "overwrite" this method.
	 * 
	 * @return The static success condition.
	 */
	public Sensor getSuccessCondition() 
	{
		return _successCondition;
	}

	//---------------------------------------------------------------

	/**
	 * Returns the static failure condition of the Action.
	 * Note that every instance of this class will return
	 * the same failure condition. 
	 * Subclasses must "overwrite" this method.
	 * 
	 * @return The static failure condition.
	 */
	public Sensor getFailureCondition() 
	{
		return _failureCondition;
	}

	//---------------------------------------------------------------

	/**
	 * Returns the static valid condition of the Action.
	 * Note that every instance of this class will return
	 * the same valid condition. 
	 * Subclasses must "overwrite" this method.
	 * 
	 * @return The static valid condition.
	 */
	public Sensor getValidCondition() 
	{
		return _validCondition;
	}

	//---------------------------------------------------------------

	/**
	 * Returns the static postcondition of the Action.
	 * Note that every instance of this class will return
	 * the same postcondition. 
	 * Subclasses must "overwrite" this method.
	 * 
	 * @return The static postcondition.
	 */
	public Sensor getPostCondition() 
	{
		return _postCondition;
	}

	//---------------------------------------------------------------

	/**
	 * Returns the static pre-failure condition of the Action.
	 * Note that every instance of this class will return
	 * the same pre-failure condition. 
	 * Subclasses must "overwrite" this method.
	 * 
	 * @return The static pre-failure condition.
	 */
	public Sensor getPreFailureCondition() 
	{
		return _preFailureCondition;
	}

	//---------------------------------------------------------------
	//                       Static methods
	//---------------------------------------------------------------

	public static List<ActionParameter> staticListOfParameters() {

		return _listOfParameters;

	}

	//---------------------------------------------------------------
	//                       Protected fields
	//---------------------------------------------------------------


	/**
	 * List of action parameter. All subclasses have their own
	 * _listOfParameter static field, that is initialized in a
	 * static initializer using the parent list and the new ones
	 * for that action.
	 */
	static java.util.List<ActionParameter> _listOfParameters;

	/**
	 * Action precondition.
	 * 
	 * The attribute is <em>static</em>, so in order to
	 * be evaluated, a <em>context</em> (provided by the
	 * specific <tt>Action</tt> objects) is needed, in 
	 * a similar way to that seen in the flyweight
	 * pattern.
	 */
	static protected Sensor _preCondition = new Invocation(new EntityExists(), new Pair<String, Sensor>("entity", new Invocation(new Entity(), new Pair<String, Sensor>("type", null), new Pair<String, Sensor>("owner", new GetContextValue("player", ActionParameterType.PLAYER)), new Pair<String, Sensor>("entityid", new GetContextValue("entityID", ActionParameterType.STRING)))));

	/**
	 * Action success condition.
	 * 
	 * The attribute is <em>static</em>, so in order to
	 * be evaluated, a <em>context</em> (provided by the
	 * specific <tt>Action</tt> objects) is needed, in 
	 * a similar way to that seen in the flyweight
	 * pattern.
	 */
	static protected Sensor _successCondition = new True();

	/**
	 * Action failure condition.
	 * 
	 * The attribute is <em>static</em>, so in order to
	 * be evaluated, a <em>context</em> (provided by the
	 * specific <tt>Action</tt> objects) is needed, in 
	 * a similar way to that seen in the flyweight
	 * pattern.
	 */
	static protected Sensor _failureCondition = new False();

	/**
	 * Action pre-failure condition.
	 * 
	 * The attribute is <em>static</em>, so in order to
	 * be evaluated, a <em>context</em> (provided by the
	 * specific <tt>Action</tt> objects) is needed, in 
	 * a similar way to that seen in the flyweight
	 * pattern.
	 */
	static protected Sensor _preFailureCondition = gatech.mmpm.Action._preFailureCondition;
	
	/**
	 * Action valid condition.
	 * It specifies whether a particular
	 * combination of parameters is valid or not.
	 * This can be used by the learning engine adaptation
	 * components to ensure that the actions being
	 * issued are valid.
	 * 
	 * The attribute is <em>static</em>, so in order to
	 * be evaluated, a <em>context</em> (provided by the
	 * specific <tt>Action</tt> objects) is needed, in 
	 * a similar way to that seen in the flyweight
	 * pattern.
	 */
	static protected Sensor _validCondition = gatech.mmpm.Action._validCondition;

	/**
	 * Action postcondition.
	 * It will hold as a side effect of the action success,
	 * thus, it is a super-set of the successCondition
	 * 
	 * The attribute is <em>static</em>, so in order to
	 * be evaluated, a <em>context</em> (provided by the
	 * specific <tt>Action</tt> objects) is needed, in 
	 * a similar way to that seen in the flyweight
	 * pattern.
	 */
	static protected Sensor _postCondition = gatech.mmpm.Action._postCondition;

	//---------------------------------------------------------------
	//                     onXXXCondition (if any)
	//---------------------------------------------------------------


	
	//---------------------------------------------------------------
	//                       Static initializers
	//---------------------------------------------------------------

	static {

		// Add parameters to _listOfParameters.
		_listOfParameters = new java.util.LinkedList<ActionParameter>(gatech.mmpm.Action.staticListOfParameters());

	} // static initializer

} // class Stop

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

