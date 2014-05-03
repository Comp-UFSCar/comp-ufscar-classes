/* Copyright 2010 Santiago Ontanon and Ashwin Ram */

package gatech.mmpm.sensor.constant;

import org.jdom.Element;

import gatech.mmpm.ActionParameterType;
import gatech.mmpm.Context;
import gatech.mmpm.GameState;
import gatech.mmpm.sensor.Sensor;
import gatech.mmpm.util.Pair;
import gatech.mmpm.util.XMLWriter;


/**
 * Sensor that provides a constant Boolean (Float) value.
 * Keep in mind that Make Me Play Me uses <em>fuzzy logic</em>
 * so the BOOLEAN type of MMPM is a Float type in Java.
 * 
 * @author David Llanso 
 * @date November, 2009
 */

public class ConstantBoolean extends ConstantSensor{
	
	public ConstantBoolean()
	{
		_value = null;
		
	} // ConstantBoolean
	
	//---------------------------------------------------------------
	
	public ConstantBoolean(Float f)
	{
		_value = f;
		
	} // ConstantBoolean
	
	//---------------------------------------------------------------

	public ConstantBoolean(ConstantBoolean cb) 
	{
		super(cb);
		_value = cb._value;

	} // Copy constructor 

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

	public Object evaluate(int cycle, GameState gs, String player,
			Context parameters)
	{
		return _value;
		
	} // evaluate
	
	//---------------------------------------------------------------

	public Object clone() 
	{
		return new ConstantBoolean(_value);
		
	} // clone
	
	//---------------------------------------------------------------
	
	/**
	 * Writes the LogicalOperator Sensor to an XMLWriter object 
	 * @param w The XMLWriter object
	 */
	public void writeToXML(XMLWriter w) {
		w.tagWithAttributes("Sensor", "type = '" + this.getClass().getName() + "'");
		w.tag("Value", ActionParameterType.BOOLEAN.toString(_value));
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

			Class<? extends ConstantBoolean> baseClass = askedClass.asSubclass(ConstantBoolean.class);

			ConstantBoolean ret = baseClass.newInstance();
			String value = xml.getChildText("Value");
			ret._value = (Float) ActionParameterType.BOOLEAN.fromString(value);

			return ret;
			
		} catch (Exception e) {
			System.out.println("System crashes when loading "+ xml.getAttributeValue("type") + " sensor.");
			e.printStackTrace();
		}
		return null;
	}

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
		_listOfNeededParameters = new java.util.LinkedList<Pair<String,ActionParameterType>>(gatech.mmpm.sensor.constant.ConstantSensor.getStaticNeededParameters());
		
	} // static initializer
	
	//---------------------------------------------------------------
	// Parameters
	//---------------------------------------------------------------
	
	/**
	 * Integer value.
	 */
	Float _value;

} // class ConstantBoolean
