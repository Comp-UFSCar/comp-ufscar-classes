/* Copyright 2010 Santiago Ontanon and Ashwin Ram */

package gatech.mmpm.sensor.composite;


import org.jdom.Element;

import gatech.mmpm.ActionParameterType;
import gatech.mmpm.Context;
import gatech.mmpm.GameState;
import gatech.mmpm.sensor.Sensor;
import gatech.mmpm.util.XMLWriter;

/**
 * Sensor that gives the value of the specified parameter.
 * 
 * @author David Llanso 
 * @date November, 2009
 */

public class GetContextValue  extends Sensor {
	
	public GetContextValue()
	{
		_parameter = null;
		_type = null;
		
	} // GetContextValue
	
	//---------------------------------------------------------------
	
	public GetContextValue(String parameter, ActionParameterType type)
	{
		_parameter = parameter;
		_type = type;
		
	} // GetContextValue
	
	//---------------------------------------------------------------
	
	public GetContextValue(GetContextValue g)
	{
		this(g._parameter,g._type);
		
	} // GetContextValue
	
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
		return _type;

	} // getType
	
	//---------------------------------------------------------------

	public Object evaluate(int cycle, GameState gs, String player,
			Context parameters)
	{
		if(_parameter.equals("cycle"))
			return cycle;
		if(_parameter.equals("player"))
			return player;
		if(parameters == null)
			return null;
		if (!parameters.containsKey(_parameter))
			throw new RuntimeException(_parameter+
					" is not a valid parameter name for the evaluated Sensor.");
		return parameters.get(_parameter);
		
	} // evaluate
	
	//---------------------------------------------------------------

	public Object clone() 
	{
		return new GetContextValue(_parameter, _type);
		
	} // clone
	
	//---------------------------------------------------------------
	
	/**
	 * Writes the LogicalOperator Sensor to an XMLWriter object 
	 * @param w The XMLWriter object
	 */
	public void writeToXML(XMLWriter w) {
		w.tagWithAttributes("Sensor", "type = '" + this.getClass().getName() + "'");
		w.tag("Parameter", _parameter);
		w.tag("ActionParameterType", _type.toString());
		w.tag("/Sensor");
	}
	
	//---------------------------------------------------------------

	/**
	 * Creates a Sensor from an xml Element.
	 * @param xml Element for creating the Sensor.
	 * @return Created Sensor.
	 */
	public static Sensor loadFromXMLInternal(Element xml) {	
		try {
			Class<?> askedClass;
			askedClass = Class.forName(xml.getAttributeValue("type"));

			Class<? extends GetContextValue> baseClass = askedClass.asSubclass(GetContextValue.class);

			GetContextValue ret = baseClass.newInstance();
			String param = xml.getChildText("Parameter");
			String apt = xml.getChildText("ActionParameterType");
			ret._parameter = param;
			ret._type = ActionParameterType.valueOf(apt);

			return ret;
		} catch (Exception e) {
			System.out.println("System crashes when loading "+ xml.getAttributeValue("type") + " sensor.");
			e.printStackTrace();
		}
		return null;
	}
	
	public String toString() {
		return this.getClass().getSimpleName() + "(" + _parameter + ":" + _type + ")";
	}

	
	//---------------------------------------------------------------
	// Parameters
	//---------------------------------------------------------------
	
	/**
	 * Parameter to be taken.
	 */
	String _parameter;
	
	//---------------------------------------------------------------
	
	/**
	 * The type of the parameter.
	 */
	ActionParameterType _type;

} // class GetContextValue
