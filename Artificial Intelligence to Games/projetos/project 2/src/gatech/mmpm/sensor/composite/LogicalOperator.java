/* Copyright 2010 Santiago Ontanon and Ashwin Ram */

package gatech.mmpm.sensor.composite;

import java.util.List;

import org.jdom.Element;

import gatech.mmpm.ActionParameterType;
import gatech.mmpm.Context;
import gatech.mmpm.GameState;
import gatech.mmpm.sensor.Sensor;
import gatech.mmpm.util.XMLWriter;

/**
 * Sensor that applies a logical operator (AND or OR) over
 * a set of expressions. Subclasses decide which one should
 * be applied.
 * 
 * Keep in mind that Make Me Play Me uses <em>fuzzy logic</em>
 * so AND and OR operators work in a non-crispy way.
 * 
 * @author Pedro Pablo Gomez Martin and David Llanso 
 * @date August, 2009
 */
public abstract class LogicalOperator extends Sensor {

	public LogicalOperator() {
		
		_children = new java.util.LinkedList<Sensor>();
		
	} // Constructor

	public LogicalOperator(Sensor ... children) {
		
		_children = new java.util.LinkedList<Sensor>();
		for (Sensor c:children) {
			_children.add(c);
		} // for
		
	} // Constructor
	
	public LogicalOperator(LogicalOperator rhs) {

		this();
		
		for(Sensor s:rhs._children)
			addChild((Sensor)s.clone());
		
	} // Copy constructor
	
	public void addChild(Sensor s) {

		if (s.getType() != ActionParameterType.BOOLEAN)
			throw new RuntimeException("And expression can't have non-boolean children.");
		_children.add(s);
		
	} // addChile

	public List<Sensor> getChildren() {

		return _children;

	} // getConditions
	
	public ActionParameterType getType() {

		return ActionParameterType.BOOLEAN;

	} // getType
	
	/**
	 * Writes the LogicalOperator Sensor to an XMLWriter object 
	 * @param w The XMLWriter object
	 */
	public void writeToXML(XMLWriter w) {
		w.tagWithAttributes("Sensor", "type = '" + this.getClass().getName() + "'");
		for(Sensor c:_children) c.writeToXML(w);
		w.tag("/Sensor");
	}


	
	/**
	 * Creates a Sensor from an xml Element.
	 * @param xml Element for creating the Sensor.
	 * @return Created Sensor.
	 */
	public static Sensor loadFromXMLInternal(Element xml) {	
		try {
			Class<?> askedClass;
			askedClass = Class.forName(xml.getAttributeValue("type"));

			Class<? extends LogicalOperator> baseClass = askedClass.asSubclass(LogicalOperator.class);
			LogicalOperator ret = baseClass.newInstance();

			for(Object o:xml.getChildren("Sensor")) {
				Element c_xml = (Element)o;
				Sensor c = Sensor.loadFromXML(c_xml);
				ret.addChild(c);
			}

			return ret;
		} catch (Exception e) {
			System.out.println("System crashes when loading "+ xml.getAttributeValue("type") + " sensor.");
			e.printStackTrace();
		}
		return null;
	}

	protected boolean internalEquivalents(Context parameters1, int cycle1, GameState gs1, String player1, Sensor s2, Context parameters2, int cycle2, GameState gs2, String player2) {

		java.util.Iterator<Sensor> lhsIt, rhsIt;
		
		lhsIt = getChildren().iterator();
		rhsIt = ((AndCondition)s2).getChildren().iterator();

		while (lhsIt.hasNext()) {
			if (!rhsIt.hasNext())
				return false;
			else {
				Sensor lhsSensor;
				Sensor rhsSensor;
				lhsSensor = lhsIt.next();
				rhsSensor = rhsIt.next();
				if ((lhsSensor == null) &&
				    (rhsSensor != null))
					return false;
				else if (!lhsSensor.equivalents(parameters1, cycle1, gs1, player1, 
                                                rhsSensor, parameters2, cycle2, gs2, player2))
					return false;
			}
		} // while
		
		return !rhsIt.hasNext();

	} // internalEquivalents
	
	public String toString() {
		String ret = this.getClass().getSimpleName() + "(";
		boolean first = true;
		for(Sensor c:_children) {
			if (!first) ret +=", ";
			ret += c.toString();
			first = false;
		}
		ret+=")";
		return ret;
	}


	protected List<Sensor> _children;
	
} // LogicalOperator
