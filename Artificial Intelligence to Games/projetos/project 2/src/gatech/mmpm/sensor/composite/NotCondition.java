/* Copyright 2010 Santiago Ontanon and Ashwin Ram */

package gatech.mmpm.sensor.composite;

import org.jdom.Element;

import gatech.mmpm.ActionParameterType;
import gatech.mmpm.Context;
import gatech.mmpm.GameState;

import gatech.mmpm.sensor.Sensor;
import gatech.mmpm.util.XMLWriter;

/*
import gatech.mmpm.util.XMLWriter;
import org.jdom.Element;
*/


/**
 * Sensor that applies a NOT logical operator over
 * another boolean expression.
 * 
 * Keep in mind that Make Me Play Me uses <em>fuzzy logic</em>
 * so NOT works in a non-crispy way. Specifically, the evaluation
 * of a not expression returns <tt>1 - childValue</tt>, clamped
 * to the 0.0f ... 1.0f range.
 * 
 * @note If you decide to change the name of this class, you
 * should also change the Builtin2Java class.
 * 
 * @author Pedro Pablo Gomez Martin
 * @date August, 2009
 */
public class NotCondition extends Sensor {

	/**
	 * Constructor.
	 * 
	 * Keep in mind the similarity between this constructor
	 * and the copy constructor. Only the <em>static</em>
	 * parameter type will be used by the Java compiler
	 * to decide which one invoke!!
	 * 
	 * @param child Child sensor, which boolean value will be
	 * negated.
	 */
	public NotCondition(Sensor child) {

		if (child.getType() != ActionParameterType.BOOLEAN)
			throw new RuntimeException("Invalid child returned type in NotCondition");

		_child = child;

	} // Constructor
	
	/**
	 * Copy constructor.
	 * 
	 * Keep in mind the similarity between this constructor
	 * and the normal constructor. Only the <em>static</em>
	 * parameter type will be used by the Java compiler
	 * to decide which one invoke!!
	 * 
	 * @param rhs Original NotCondition used to create
	 * the current object.
	 */
	public NotCondition(NotCondition rhs) {
		
		_child = (Sensor)rhs._child.clone();

	} // Copy constructor
	
	public Sensor getChild() {

		return _child;

	} // getChild
	

/*	
	public void reset()
	{
		c1.reset();
	}	
*/	

//	/**
//	 * Writes the NOT Condition to an XMLWriter object 
//	 * @param w The XMLWriter object
//	 */
//	public void writeToXML(XMLWriter w) {
//		w.rawXML("<Condition type = \"" + this.getClass().getName() + "\">");
//		c1.writeToXML(w);
//		w.rawXML("</Condition>");
//	}
//
//	
//	public static Condition loadFromXMLInternal(Element xml) {
//		Element c_xml = xml.getChild("Condition");
//		Condition c = Condition.loadFromXML(c_xml);
//		
//		return new NotCondition(c);
//	}

	/**
	 * Writes the EqualitySensor Sensor to an XMLWriter object
	 * 
	 * @param w
	 *            The XMLWriter object
	 */
	public void writeToXML(XMLWriter w) {
		w.tagWithAttributes("Sensor", "type = '" + this.getClass().getName()
				+ "'");
		_child.writeToXML(w);
		w.tag("/Sensor");
	}

	/**
	 * Creates a EqualitySensor Sensor from an xml Element.
	 * 
	 * @param xml
	 *            Element for creating the EqualitySensor Sensor.
	 * @return Created Sensor.
	 */
	public static Sensor loadFromXMLInternal(Element xml) {
		Element c_xml = (Element) xml.getChild("Sensor");
		Sensor child = Sensor.loadFromXML(c_xml);
		NotCondition ret = new NotCondition(child);

		return ret;
	}

	
	public Object clone() {

		return new NotCondition(this);

	} // clone

	public ActionParameterType getType() {

		return ActionParameterType.BOOLEAN;

	} // getType
	
	public Object evaluate(int cycle, GameState gs, String player, Context parameters) {

		Object childResult;
		
		childResult = _child.evaluate(cycle, gs, player, parameters);
		
		float fuzzyResult = 1.0f - (Float) childResult;

		// Normalize
		if (fuzzyResult > 1.0f)
			fuzzyResult = 1.0f;
		else if (fuzzyResult < 0.0f)
			fuzzyResult = 0.0f;
		
		return fuzzyResult;
		
	} // evaluate

	protected boolean internalEquivalents(Context parameters1, int cycle1, GameState gs1, String player1, Sensor s2, Context parameters2, int cycle2, GameState gs2, String player2) {
		
		return _child.equivalents(parameters1, cycle1, gs1, player1, 
                                  ((NotCondition)s2)._child, parameters2, cycle2, gs2, player2);
		
	} // internalEquivalents

	protected Sensor _child;
	
} // NotCondicion
