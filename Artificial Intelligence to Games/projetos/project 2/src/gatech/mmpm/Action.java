/* Copyright 2010 Santiago Ontanon and Ashwin Ram */

package gatech.mmpm;


import gatech.mmpm.sensor.Sensor;
import gatech.mmpm.sensor.composite.AndCondition;
import gatech.mmpm.sensor.constant.False;
import gatech.mmpm.sensor.constant.True;
import gatech.mmpm.util.XMLWriter;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import org.jdom.Element;

/**
 * Superclass of all MMPM actions.
 * 
 * @author Redesign by Pedro Pablo Gomez Martin, September, 2009
 * based in previous work of other people.
 */
public abstract class Action implements Cloneable {

	/**
	 * Constructor
	 * 
	 * @param entityID Entity identifier which receives
	 * the action.
	 * @param playerID Player identifier that makes
	 * the action.
	 */
	public Action(String entityID, String playerID) {
		// This javadoc is copied verbatim in
		// Action.java.tmplt. If you change this,
		// consider update the template.

		// Assign a unique ID to the action.
		_actionID = _nextID++;

		_entityID = entityID;
		_playerID = playerID;

	} // Constructor
	
	//---------------------------------------------------------------

	/**
	 * Copy constructor
	 * 
	 * @param rhs Template to build the new object.
	 * 
	 * Keep in mind that the new object will receive
	 * a <em>new</em> actionID.
	 */
	public Action(Action rhs) {
		
		_actionID = _nextID++;

		_entityID = rhs._entityID;
		_playerID = rhs._playerID;
		
	} // copy constructor
	
	//---------------------------------------------------------------

	public abstract Object clone();

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
		// This javadoc is copied verbatim in
		// Action.java.tmplt. If you change this,
		// consider update the template.
	
		// Overwritten in each entity class to return the
		// class static attribute. 
		return _listOfParameters;

	} // listOfParameters
	
	//---------------------------------------------------------------
	//                       Getter & setter
	//---------------------------------------------------------------
	
	public int getActionID() {
		return _actionID;
	}
	
	public String getPlayerID() {
		return _playerID;
	}

	public void setPlayerID(String playerID) {
		_playerID = playerID;
	}
	
	public String getEntityID() {
		return _entityID;
	}

	public void setEntityID(String entityID) {
		_entityID = entityID;
	}
	
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
	 * features are added.
	 */
	public Object parameterValue(String parameter) {
		// This javadoc is copied verbatim in
		// Action.java.tmplt. If you change this,
		// consider update the template.
		
		if (parameter.compareTo("actionID") == 0)
			return "" + getActionID();
		else if (parameter.compareTo("playerID") == 0)
			return getPlayerID();
		if (parameter.compareTo("entityID") == 0)
			return getEntityID();
		else
			return null;

	} // parameterValue

	//---------------------------------------------------------------
	
	/**
	 * Returns a parameter value, converted into a String, by its name.
	 * 
	 * @param parameter Parameter name which value want to be recovered.
	 * 
	 * @return Parameter value, or null if it do not exist.
	 * 
	 * @note This method MUST BE overwritten in subclasses if more
	 * features are added.
	 */
	public String parameterStringValue(String parameter) {
		// This javadoc is copied verbatim in
		// Action.java.tmplt. If you change this,
		// consider update the template.
		
		if (parameter.compareTo("actionID") == 0)
			return "" + getActionID();
		else if (parameter.compareTo("playerID") == 0)
			return getPlayerID();
		if (parameter.compareTo("entityID") == 0)
			return getEntityID();
		else
			return null;

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
	 * features are added.
	 * 
	 * @note Although <tt>actionID</tt> is shown as an
	 * action parameter, it cannot be changed with this
	 * method (is a read-only parameter automatically
	 * established in the constructor). 
	 */
	public void setParameterValue(String parameter, String value) {
		// This javadoc is copied verbatim in
		// Action.java.tmplt. If you change this,
		// consider update the template.

		if (parameter.compareTo("entityID") == 0)
			setEntityID(value);
		else if (parameter.compareTo("playerID") == 0)
			setPlayerID(value);
		else if (parameter.compareTo("actionID") != 0)
			throw new RuntimeException("Invalid parameter name in Action:setParameterValue() -> " + parameter);

	} // setParameterValue

	//---------------------------------------------------------------

	/**
	 * Returns true if the action has a parameter with the
	 * specified name.
	 * 
	 * @param parameter Name of the searched parameter. 
	 * @return True if the parameter exists.
	 */
	public boolean hasParameter(String parameter) {
		
		// We must use listOfParameters() to used the
		// dynamic binding and access to the dynamic class
		// list of parameters.
		for (ActionParameter ap:listOfParameters()) {
			if (ap.getName().compareTo(parameter) == 0)
				return true;
		} // for
		
		return false;
		
	} // hasParameter	

	//---------------------------------------------------------------

	/**
	 * Returns the action context. This method must be implemented
	 * in child classes.
	 * 
	 * @return The action context.
	 */
	public Context getContext() {
		// This javadoc is copied verbatim in
		// Action.java.tmplt. If you change this,
		// consider update the template.
		
		Context result = new Context();
		result.put("actionID", _actionID);
		result.put("playerID", _playerID);
		result.put("entityID", _entityID);

		return result;

	} // getContext

	//---------------------------------------------------------------
	//                 onXXXCondition()
	//---------------------------------------------------------------

	/**
	 * Does some assignments needed before calling the first time to
	 * checkPreCondition() method. This method should be overwritten in
	 * some child action classes.
	 * 
	 * @param cycle The cycle.
	 * @param gameState The game state.
	 * @param player Player who checks the condition. 
	 * @param parameters The context to evaluate the condition. 
	 */
	protected void onPreCondition(int cycle, GameState gameState, String player, Context parameters){}
	// This javadoc is copied nearly verbatim in
	// ActionOnCondition.tmplt. If you change this,
	// consider update the template.
	
	/**
	 * Does some assignments needed before calling the first time to
	 * checkSuccessCondition() method. This method should be overwritten in
	 * some child action classes.
	 * 
	 * @param cycle The cycle.
	 * @param gameState The game state.
	 * @param player Player who checks the condition. 
	 * @param parameters The context to evaluate the condition. 
	 */
	protected void onSuccessCondition(int cycle, GameState gameState, String player, Context parameters){}
	// This javadoc is copied nearly verbatim in
	// ActionOnCondition.tmplt. If you change this,
	// consider update the template.
	
	/**
	 * Does some assignments needed before calling the first time to
	 * checkFailureCondition() method. This method should be overwritten in
	 * some child action classes.
	 * 
	 * @param cycle The cycle.
	 * @param gameState The game state.
	 * @param player Player who checks the condition. 
	 * @param parameters The context to evaluate the condition. 
	 */
	protected void onFailureCondition(int cycle, GameState gameState, String player, Context parameters){}
	// This javadoc is copied nearly verbatim in
	// ActionOnCondition.tmplt. If you change this,
	// consider update the template.
	
	/**
	 * Does some assignments needed before calling the first time to
	 * checkPreFailureCondition() method. This method should be overwritten in
	 * some child action classes.
	 * 
	 * @param cycle The cycle.
	 * @param gameState The game state.
	 * @param player Player who checks the condition. 
	 * @param parameters The context to evaluate the condition. 
	 */
	protected void onPreFailureCondition(int cycle, GameState gameState, String player, Context parameters){}
	// This javadoc is copied nearly verbatim in
	// ActionOnCondition.tmplt. If you change this,
	// consider update the template.
	
	/**
	 * Does some assignments needed before calling the first time to
	 * checkValidCondition() method. This method should be overwritten in
	 * some child action classes.
	 * 
	 * @param cycle The cycle.
	 * @param gameState The game state.
	 * @param player Player who checks the condition. 
	 * @param parameters The context to evaluate the condition. 
	 */
	protected void onValidCondition(int cycle, GameState gameState, String player, Context parameters){}
	// This javadoc is copied nearly verbatim in
	// ActionOnCondition.tmplt. If you change this,
	// consider update the template.
	
	/**
	 * Does some assignments needed before calling the first time to
	 * checkPostCondition() method. This method should be overwritten in
	 * some child action classes.
	 * 
	 * @param cycle The cycle.
	 * @param gameState The game state.
	 * @param player Player who checks the condition. 
	 * @param parameters The context to evaluate the condition. 
	 */
	protected void onPostCondition(int cycle, GameState gameState, String player, Context parameters){}
	// This javadoc is copied nearly verbatim in
	// ActionOnCondition.tmplt. If you change this,
	// consider update the template.

	//---------------------------------------------------------------
	//                 checkXXXCondition()
	//---------------------------------------------------------------
	
	/**
	 * Evaluates the pre-condition with the context of the Action.
	 * 
	 * @param cycle The cycle.
	 * @param gameState The game state.
	 * @param player Player who checks the condition. 
	 * @return True if the condition is evaluated as true.
	 */
	public boolean checkPreCondition(int cycle, GameState gameState, String player)
	{
		return checkPreCondition(cycle, gameState, player, null);
		
	} // checkPreCondition

	//---------------------------------------------------------------
	
	/**
	 * Evaluates the pre-condition with a given context.
	 * 
	 * @param cycle The cycle.
	 * @param gameState The game state.
	 * @param player Player who checks the condition. 
	 * @param parameters The context to evaluate the condition. 
	 * @return True if the condition is evaluated as true.
	 */
	public boolean checkPreCondition(int cycle, GameState gameState, String player, Context parameters)
	{
		if (parameters==null) parameters = getContext();
        if(!onPreConditionCalled) {
            onPreCondition(cycle, gameState, player, parameters);
            onPreConditionCalled = true;
            Context tmp = getContext();
            parameters.put(tmp);
        }
		return ((Float)getPreCondition().evaluate(cycle, gameState, player, parameters) 
				>= Sensor.BOOLEAN_TRUE_THRESHOLD);
		
	} // checkPreCondition

	//---------------------------------------------------------------

	/**
	 * Evaluates the success condition with the context of the Action.
	 * 
	 * @param cycle The cycle.
	 * @param gameState The game state.
	 * @param player Player who checks the condition. 
	 * @return True if the condition is evaluated as true.
	 */
	public boolean checkSuccessCondition(int cycle, GameState gameState, String player)
	{
		return checkSuccessCondition(cycle, gameState, player, null);
		
	} // checkSuccessCondition

	//---------------------------------------------------------------

	/**
	 * Evaluates the success condition with a given context.
	 * 
	 * @param cycle The cycle.
	 * @param gameState The game state.
	 * @param player Player who checks the condition. 
	 * @param parameters The context to evaluate the condition. 
	 * @return True if the condition is evaluated as true.
	 */
	public boolean checkSuccessCondition(int cycle, GameState gameState, String player, Context parameters)
	{
		if (parameters==null) parameters = getContext();
		if (!onSuccessConditionCalled) {
			onSuccessCondition(cycle, gameState, player, parameters);
			onSuccessConditionCalled = true;
            // update the context after evaluating the condition:
            Context tmp = getContext();
            parameters.put(tmp);
		}
		return ((Float)getSuccessCondition().evaluate(cycle, gameState, player, parameters) 
				>= Sensor.BOOLEAN_TRUE_THRESHOLD);
		
	} // checkSuccessCondition

	//---------------------------------------------------------------

	/**
	 * Evaluates the failure condition with the context of the Action.
	 * 
	 * @param cycle The cycle.
	 * @param gameState The game state.
	 * @param player Player who checks the condition. 
	 * @return True if the condition is evaluated as true.
	 */
	public boolean checkFailureCondition(int cycle, GameState gameState, String player)
	{
		return checkFailureCondition(cycle, gameState, player, null);
		
	} // checkFailureCondition

	//---------------------------------------------------------------

	/**
	 * Evaluates the failure condition with a given context.
	 * 
	 * @param cycle The cycle.
	 * @param gameState The game state.
	 * @param player Player who checks the condition. 
	 * @param parameters The context to evaluate the condition. 
	 * @return True if the condition is evaluated as true.
	 */
	public boolean checkFailureCondition(int cycle, GameState gameState, String player, Context parameters)
	{
		if (parameters==null) parameters = getContext();
		if (!onFailureConditionCalled) {
			onFailureCondition(cycle, gameState, player, parameters);
			onFailureConditionCalled = true;
            Context tmp = getContext();
            parameters.put(tmp);
		}
		return ((Float)getFailureCondition().evaluate(cycle, gameState, player, parameters) 
				>= Sensor.BOOLEAN_TRUE_THRESHOLD);
		
	} // checkFailureCondition

	//---------------------------------------------------------------

	/**
	 * Evaluates the pre-failure condition with the context of the Action.
	 * 
	 * @param cycle The cycle.
	 * @param gameState The game state.
	 * @param player Player who checks the condition. 
	 * @return True if the condition is evaluated as true.
	 */
	public boolean checkPreFailureCondition(int cycle, GameState gameState, String player)
	{
		return checkPreFailureCondition(cycle, gameState, player, null);
		
	} // checkPreFailureCondition

	//---------------------------------------------------------------

	/**
	 * Evaluates the pre-failure condition with a given context.
	 * 
	 * @param cycle The cycle.
	 * @param gameState The game state.
	 * @param player Player who checks the condition. 
	 * @param parameters The context to evaluate the condition. 
	 * @return True if the condition is evaluated as true.
	 */
	public boolean checkPreFailureCondition(int cycle, GameState gameState, String player, Context parameters)
	{
		if (parameters==null) parameters = getContext();
		if(!onPreFailureConditionCalled) {
			onPreFailureCondition(cycle, gameState, player, parameters);
			onPreFailureConditionCalled = true;
            Context tmp = getContext();
            parameters.put(tmp);
		}
//                System.out.println(parameters);
		return ((Float)getPreFailureCondition().evaluate(cycle, gameState, player, parameters) 
				>= Sensor.BOOLEAN_TRUE_THRESHOLD);
		
	} // checkPreFailureCondition

	//---------------------------------------------------------------

	/**
	 * Evaluates the valid condition with the context of the Action.
	 * 
	 * @param cycle The cycle.
	 * @param gameState The game state.
	 * @param player Player who checks the condition. 
	 * @return True if the condition is evaluated as true.
	 */
	public boolean checkValidCondition(int cycle, GameState gameState, String player)
	{
		return checkValidCondition(cycle, gameState, player, null);
		
	} // checkValidCondition

	//---------------------------------------------------------------

	/**
	 * Evaluates the valid condition with a given context.
	 * 
	 * @param cycle The cycle.
	 * @param gameState The game state.
	 * @param player Player who checks the condition. 
	 * @param parameters The context to evaluate the condition. 
	 * @return True if the condition is evaluated as true.
	 */
	public boolean checkValidCondition(int cycle, GameState gameState, String player, Context parameters)
	{
		if (parameters==null) parameters = getContext();
		if(!onValidConditionCalled) {
			onValidCondition(cycle, gameState, player, parameters);
			onValidConditionCalled = true;
            Context tmp = getContext();
            parameters.put(tmp);
		}
		return ((Float)getValidCondition().evaluate(cycle, gameState, player, parameters) 
				>= Sensor.BOOLEAN_TRUE_THRESHOLD);
		
	} // checkValidCondition

	//---------------------------------------------------------------

	/**
	 * Evaluates the post-condition with the context of the Action.
	 * 
	 * @param cycle The cycle.
	 * @param gameState The game state.
	 * @param player Player who checks the condition. 
	 * @return True if the condition is evaluated as true.
	 */
	public boolean checkPostCondition(int cycle, GameState gameState, String player)
	{
		return checkPostCondition(cycle, gameState, player, null);
		
	} // checkPostCondition

	//---------------------------------------------------------------

	/**
	 * Evaluates the post-condition with a given context.
	 * 
	 * @param cycle The cycle.
	 * @param gameState The game state.
	 * @param player Player who checks the condition. 
	 * @param parameters The context to evaluate the condition. 
	 * @return True if the condition is evaluated as true.
	 */
	public boolean checkPostCondition(int cycle, GameState gameState, String player, Context parameters)
	{
		if (parameters==null) parameters = getContext();
		if(!onPostConditionCalled) {
			onPostCondition(cycle, gameState, player, parameters);
			onPostConditionCalled = true;
            Context tmp = getContext();
            parameters.put(tmp);
		}
		return ((Float)getPostCondition().evaluate(cycle, gameState, player, parameters) 
				>= Sensor.BOOLEAN_TRUE_THRESHOLD);
		
	} // checkPostCondition
	
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
	// This method is copied verbatim in
	// Action.java.tmplt. If you change this,
	// consider update the template.

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
	// This method is copied verbatim in
	// Action.java.tmplt. If you change this,
	// consider update the template.

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
	// This method is copied verbatim in
	// Action.java.tmplt. If you change this,
	// consider update the template.

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
	// This method is copied verbatim in
	// Action.java.tmplt. If you change this,
	// consider update the template.

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
	// This method is copied verbatim in
	// Action.java.tmplt. If you change this,
	// consider update the template.

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
	// This method is copied verbatim in
	// Action.java.tmplt. If you change this,
	// consider update the template.
	
	//---------------------------------------------------------------
	
	public boolean equivalents(Action a) {
		
		// TODO: this should be improved.
		// The second test can be avoided if
		// a better control of null values is
		// done in the first test
		// (the number of parameters in each action
		// will be the same because both are the same
		// class).
		// On the other hand, I think that actionID
		// should be skipped in this comparison.
		// In other case, it will always failed but
		// when "this" and "a" are in fact the same
		// reference.
		
		// On the other hand, now we are assuming that
		// equals() method of all Java types used to map
		// MMPM are correctly implemented...
		
		if (getClass().equals(a.getClass())) {
			if (!a.getPlayerID().equals(getPlayerID())) return false;
			if (!a.getEntityID().equals(getEntityID())) return false;
			
			for(ActionParameter ap:listOfParameters()) {
				Object v = parameterValue(ap.m_name);
				if (v!=null) {
					Object v2 = a.parameterValue(ap.m_name);					
					if (!v.equals(v2)) return false;
				}
			}
			for(ActionParameter ap:a.listOfParameters()) {
				Object v = a.parameterValue(ap.m_name);
				if (v!=null) {
					Object v2 = parameterValue(ap.m_name);					
					if (!v.equals(v2)) return false;
				}
			}
			return true;
		} else {
			return false;
		}

	} // equivalents

	public String toString() {
		String tmp = this.getClass().getSimpleName() + "(";
		boolean first = true;
		for(ActionParameter parameter:listOfParameters()) {
			if (first) {
				first = false;
			} else {
				tmp += " , ";				
			}
			tmp += parameter.getName() + " := " + parameterStringValue(parameter.getName());
		}
		return tmp + ")";
	}

    public String toSimpleString() {
		String tmp = this.getClass().getSimpleName() + "(";
		boolean first = true;
		for(ActionParameter parameter:listOfParameters()) {
            if (parameter.getName().equals("actionID")) {

            } else {
                if (parameterValue(parameter.getName()) instanceof Entity) {
                    if (first) {
                        first = false;
                    } else {
                        tmp += " , ";
                    }
                    Entity e = (Entity) parameterValue(parameter.getName());
                    tmp += e.getentityID() + "(" + e.getClass().getSimpleName() + ")";
                } else if (parameterValue(parameter.getName()) instanceof Class) {
                    Class c = (Class) parameterValue(parameter.getName());
                    tmp += " , " + c.getSimpleName();
                } else {
                    if (parameterStringValue(parameter.getName())!=null) {
                        if (first) {
                            first = false;
                        } else {
                            tmp += " , ";
                        }
                        tmp += parameterStringValue(parameter.getName());
                    }
                }
            }
		}
		return tmp + ")";
	}


	public void writeToXML(XMLWriter w) {
		writeToXMLNode(w, "Action");
	}

	public void writeToXMLNode(XMLWriter w, String nodeName) {
		w.rawXML("<" + nodeName + " type = \"" + this.getClass().getName() + "\" id = \"" + this.getActionID() + "\">");
		for(ActionParameter parameter:listOfParameters()) {
			w.rawXML(" <parameter name = \"" + parameter.getName() + "\">" + parameterStringValue(parameter.getName()) + "</parameter>");
		}
		w.rawXML("</" + nodeName + ">");		
	}
	
	public static Action loadFromXML(Element xml) {
		// We assume that the node is an Action (or AbortedAction...).
		try {
			Class<?> c;
			c = Class.forName(xml.getAttributeValue("type"));
			Method m = c.getMethod("loadFromXMLInternal", Element.class);
			Action ret = (Action) m.invoke(c, xml);
//			ret.initializeActionConditions();
			return ret;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			System.err.println("Cannot load action " + xml.getAttributeValue("type"));
			e.printStackTrace();
		}
		return null;
	}
	
	public static Action loadFromXMLInternal(Element xml) {		
		try {
			Class<?> c;
			c = Class.forName(xml.getAttributeValue("type"));
			Constructor<?> cons = c.getConstructor(new Class[]{String.class,String.class});
			String entity = null, playerID = null;
			for(Object o:xml.getChildren("parameter")) {
				Element e = (Element)o;
				if (e.getAttributeValue("name").equals("EntityID")) {
					entity = e.getText();
				}
				if (e.getAttributeValue("name").equals("playerID")) {
					playerID = e.getText();
				}
			}
//			System.out.println("Action.loadFromXMLInternal: creating action " + c.getName() + " with " + entity + " , " + playerID);
			Action a = (Action) cons.newInstance(entity,playerID);
			String idString = xml.getAttributeValue("id");
			if (idString != null)
				try {
					a._actionID = Integer.parseInt(idString);
				} catch (NumberFormatException ex) {
					// If id is not correct, we use the auto-generated
					// actionID.
				}

			for(Object o:xml.getChildren("parameter")) {
				Element e = (Element)o;
				a.setParameterValue(e.getAttributeValue("name"), e.getText());
			}
//			System.out.println("Action.loadFromXMLInternal: a =  " + a.toString());			
			return a;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
					
		return null;

	} // loadFromXMLInternal	

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
	 * Next available ID. Each Action object receives a unique ID,
	 * and this static field stores the next one that should be
	 * used. 
	 */
	static protected int _nextID = 0;

	/**
	 * Action precondition.
	 * 
	 * The attribute is <em>static</em>, so in order to
	 * be evaluated, a <em>context</em> (provided by the
	 * specific <tt>Action</tt> objects) is needed, in 
	 * a similar way to that seen in the flyweight
	 * pattern.
	 */
	static protected Sensor _preCondition = new True();
	// This javadoc is copied verbatim in
	// Action.java.tmplt. If you change this,
	// consider update the template.

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
	// This javadoc is copied verbatim in
	// Action.java.tmplt. If you change this,
	// consider update the template.

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
	// This javadoc is copied verbatim in
	// Action.java.tmplt. If you change this,
	// consider update the template.

	/**
	 * Action pre-failure condition.
	 * 
	 * The attribute is <em>static</em>, so in order to
	 * be evaluated, a <em>context</em> (provided by the
	 * specific <tt>Action</tt> objects) is needed, in 
	 * a similar way to that seen in the flyweight
	 * pattern.
	 */
	static protected Sensor _preFailureCondition = new False();
	// This javadoc is copied verbatim in
	// Action.java.tmplt. If you change this,
	// consider update the template.
	
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
	static protected Sensor _validCondition = new True();
	// This javadoc is copied verbatim in
	// Action.java.tmplt. If you change this,
	// consider update the template.

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
	static protected Sensor _postCondition = new AndCondition();
	// This javadoc is copied verbatim in
	// Action.java.tmplt. If you change this,
	// consider update the template.

	// TODO: PedroP: I don't understand that one.
	// In D2, Action.postCondition was initialised also
	// with an AndCondition with no children...
	
	/**
	 * This parameter shows whether the checkPreCondition() method
	 * has been previously called.
	 */
	protected boolean onPreConditionCalled = false;
	
	/**
	 * This parameter shows whether the checkSuccessCondition() method
	 * has been previously called.
	 */
	protected boolean onSuccessConditionCalled = false;
	
	/**
	 * This parameter shows whether the checkFailureCondition() method
	 * has been previously called.
	 */
	protected boolean onFailureConditionCalled = false;
	
	/**
	 * This parameter shows whether the checkPreFailureCondition() method
	 * has been previously called.
	 */
	protected boolean onPreFailureConditionCalled = false;
	
	/**
	 * This parameter shows whether the checkValidCondition() method
	 * has been previously called.
	 */
	protected boolean onValidConditionCalled = false;
	
	/**
	 * This parameter shows whether the checkPostCondition() method
	 * has been previously called.
	 */
	protected boolean onPostConditionCalled = false;
	
/*	
	// Basic conditions used by all components:
	protected Sensor preCondition;
	protected Sensor successCondition;
	protected Sensor failureCondition;
	
	// Additional conditions that can be defined optionally to improve the behavior of D2:
	protected Sensor validCondition;		// These conditions specify if a particular combination of parameters is
											//	valid or not. This is used by the adaptation component to ensure that
											// 	the actions being issued are valid.
	protected Sensor postCondition;		// These are conditions that will hold as a side effect of the action success,
											// 	thus, they are a super-set of the successCondition
	protected Sensor preFailureCondition;	// This is a Sensor that if satisfied while waiting for the preconditions
												// of an action, it is not worth it to wait for them. For instance, timeouts,
												// or conditions that determine impossibility of conditions to be satisfied should
												// be encoded here.
*/

	/**
	 * Action ID. It will be unique between all the action objects, and
	 * it is automatically chosen in the Action constructor.
	 * 
	 * @see _nextID
	 */
	protected int _actionID;
	
	protected String _entityID;

	protected String _playerID;
	
//	protected List<ActionParameter> parameters = new LinkedList<ActionParameter>();

	/**
	 * List of action parameter. All subclasses have their own
	 * _listOfParameter static field, that is initialized in a
	 * static initializer using the parent list and the new ones
	 * for that action.
	 */
	static java.util.List<ActionParameter> _listOfParameters = new java.util.LinkedList<ActionParameter>();
	// This javadoc is copied verbatim in
	// Action.java.tmplt. If you change this,
	// consider update the template.

/*
	protected String entityID = null;

	protected String owner = null;
	

	static java.util.List<Action> _listOfActions = new java.util.LinkedList<Action>();
	// TODO: copiado de Entity
	*/
	//---------------------------------------------------------------
	//                       Static initializers
	//---------------------------------------------------------------

	static {

		_listOfParameters.add(new ActionParameter("actionID", ActionParameterType.INTEGER));
		// Test this: is "actor" always a Player?
		_listOfParameters.add(new ActionParameter("playerID", ActionParameterType.PLAYER));
		
		_listOfParameters.add(new ActionParameter("entityID", ActionParameterType.ENTITY_ID));

		/*
		validCondition = new TrueCondition();
		preCondition = new TrueCondition();
		successCondition = new TrueCondition();
		failureCondition = new FalseCondition();		
		postCondition = new AndCondition();	
		preFailureCondition = new FalseCondition();	
		*/
	} // static initializer
	
} // class Action
