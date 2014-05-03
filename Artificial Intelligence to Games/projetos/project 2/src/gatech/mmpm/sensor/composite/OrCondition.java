/* Copyright 2010 Santiago Ontanon and Ashwin Ram */

package gatech.mmpm.sensor.composite;
import gatech.mmpm.Context;
import gatech.mmpm.GameState;

import gatech.mmpm.sensor.Sensor;


/*import gatech.mmpm.util.XMLWriter;
import org.jdom.Element;
*/


/**
 * Sensor that applies a OR logical operator over
 * a set of expressions.
 * 
 * Keep in mind that Make Me Play Me uses <em>fuzzy logic</em>
 * so OR works in a non-crispy way. Specifically, the evaluation
 * of a chain of ORs is maximum returned value by the children. 
 * Short-circuit evaluation is <em>not</em> used in any case. 
 * 
 * @note If you decide to change the name of this class, you
 * should also change the Builtin2Java class.
 * 
 * @author Pedro Pablo Gomez Martin
 * @date August, 2009
 */
public class OrCondition extends LogicalOperator {

	public OrCondition() {

		super();

	} // Constructor

	public OrCondition(Sensor ... children) {

		super(children);

	} // Constructor

	public OrCondition(OrCondition rhs) {

		super(rhs);
		
	} // Copy constructor
	
/*
	public void reset()
	{
		for(Sensor c:m_lc) c.reset();
	}	
*/
	
//	/**
//	 * Writes the OR Sensor to an XMLWriter object 
//	 * @param w The XMLWriter object
//	 */
//	public void writeToXML(XMLWriter w) {
//		w.rawXML(" <Sensor type = \"" + this.getClass().getName() + "\">");
//		for(Sensor c:m_lc) c.writeToXML(w);
//		w.rawXML(" </Condition>");
//	}
//
//
//	public static Sensor loadFromXMLInternal(Element xml) {
//		OrCondition ret = new OrCondition();
//
//		for(Object o:xml.getChildren("Condition")) {
//			Element c_xml = (Element)o;
//			Sensor c = Sensor.loadFromXML(c_xml);
//			ret.addCondition(c);
//		}
//
//		return ret;
//	}

	public Object clone() {

		return new OrCondition(this);

	} // clone

	public Object evaluate(int cycle, GameState gameState, String player, Context parameters) {
		
		float retValue = 0.0f;
		
		for(Sensor s:_children) {
			Object result;
			result = s.evaluate(cycle, gameState, player, parameters);
			if (retValue < (Float) result)
				retValue = (Float) result;
		}
		
		return retValue;

	} // evaluate
	
} // class Or
