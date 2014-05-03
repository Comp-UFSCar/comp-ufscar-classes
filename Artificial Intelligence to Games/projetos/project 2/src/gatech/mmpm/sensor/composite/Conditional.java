/* Copyright 2010 Santiago Ontanon and Ashwin Ram */

package gatech.mmpm.sensor.composite;

import gatech.mmpm.ActionParameterType;
import gatech.mmpm.Context;
import gatech.mmpm.GameState;
import gatech.mmpm.sensor.Sensor;
import gatech.mmpm.util.XMLWriter;

import java.util.List;

import org.jdom.Element;

/**
 * Sensor that, depending on the condition sensor evaluation, evaluates the
 * trueCase sensor or the falseCase sensor.
 * 
 * @author David Llanso 
 * @date November, 2009
 */

public class Conditional extends Sensor{
	
	public Conditional()
	{
		_condition = null;
		_trueCase = null;
		_falseCase = null;

	} // Conditional
	
	//---------------------------------------------------------------
	
	public Conditional(Sensor condition, Sensor trueCase, Sensor falseCase)
	{
		if (condition.getType() != ActionParameterType.BOOLEAN)
			throw new RuntimeException("condition sensor must be a BOOLEAN.");
		if (trueCase.getType() != falseCase.getType())
			throw new RuntimeException("caseTrue and caseFalse sensors must be sensors of the same type.");
		
		_condition = condition;
		_trueCase = trueCase;
		_falseCase = falseCase;

	} // Conditional
	
	//---------------------------------------------------------------
	
	public Conditional(Conditional c)
	{
		this((Sensor) c._condition.clone(),
			 (Sensor) c._trueCase.clone(),
			 (Sensor) c._falseCase.clone());

	} // Conditional
	
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
		// in that moment we have already check _trueCase 
		// and _falseCase are sensors of the same type.
		return _trueCase.getType();

	} // getType
	
	//---------------------------------------------------------------

	public Object evaluate(int cycle, GameState gs, String player,
			Context parameters)
	{
		// Keep in mind that Make Me Play Me uses <em>fuzzy logic</em>
		// so the BOOLEAN type of MMPM is a Float type in Java.
		if((Float)_condition.evaluate(cycle, gs, player, parameters) >= 1.0f)
			return _trueCase.evaluate(cycle, gs, player, parameters);
		else
			return _falseCase.evaluate(cycle, gs, player, parameters);
		
	} // evaluate
	
	//---------------------------------------------------------------

	public Object clone() 
	{
		return new Conditional(_condition,_trueCase,_falseCase);
		
	} // clone
	
	//---------------------------------------------------------------
	
	/**
	 * Writes the LogicalOperator Sensor to an XMLWriter object 
	 * @param w The XMLWriter object
	 */
	public void writeToXML(XMLWriter w) {
		w.tagWithAttributes("Sensor", "type = '" + this.getClass().getName() + "'");
		_condition.writeToXML(w);
		_trueCase.writeToXML(w);
		_falseCase.writeToXML(w);
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

			Class<? extends Conditional> baseClass = askedClass.asSubclass(Conditional.class);

			Conditional ret = baseClass.newInstance();
			List<?> l = xml.getChildren("Sensor");
			Element s_xml = (Element) l.get(0);
			ret._condition = Sensor.loadFromXML(s_xml);
			s_xml = (Element) l.get(1);
			ret._trueCase = Sensor.loadFromXML(s_xml);
			s_xml = (Element) l.get(2);
			ret._falseCase = Sensor.loadFromXML(s_xml);

			return ret;
			
		} catch (Exception e) {
			System.out.println("System crashes when loading "+ xml.getAttributeValue("type") + " sensor.");
			e.printStackTrace();
		}
		return null;
	}
	
	//---------------------------------------------------------------
	// Parameters
	//---------------------------------------------------------------

	/**
	 * Condition to decide.
	 */
	Sensor _condition;
	
	/**
	 * Sensor to evaluate in case the condition is true.
	 */
	Sensor _trueCase;
	
	/**
	 * Sensor to evaluate in case the condition is false.
	 */
	Sensor _falseCase;

} // class Conditional
