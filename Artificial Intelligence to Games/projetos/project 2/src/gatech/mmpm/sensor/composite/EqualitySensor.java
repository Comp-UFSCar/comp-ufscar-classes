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
 * Sensor that applies an equality operator (<tt>==</tt> or <tt>!=</tt>) to the
 * result of the evaluation of two other sensors.
 * 
 * @note If you decide to change the name of this class, you should also change
 *       the Builtin2Java class.
 * 
 * @author Pedro Pablo Gomez Martin
 * @date August, 2009
 */
public class EqualitySensor extends Sensor {

	public enum Operator {
		EQUAL_THAN {
			public boolean eval(Object lhs, Object rhs) {
				return lhs.equals(rhs);
			}

			public String toString() {
				return "==";
			}
		},
		NOT_EQUAL_THAN {
			public boolean eval(Object lhs, Object rhs) {
				return !lhs.equals(rhs);
			}

			public String toString() {
				return "!=";
			}
		};

		public abstract boolean eval(Object lhs, Object rhs);
	    
		public static <E extends Enum<E>> E stringToEnum(Class<E> c, String s) {
			for (E e : java.util.EnumSet.allOf(c)) 
			{
				if (e.toString().equals(s))
					return e;
			}
			throw new IllegalArgumentException("Unknown enum string for " +
	    			c.getName() + ": " + s);
		}
	    
		public static Operator stringToEnum(String s) {
			if(s.equals("=="))
				return EQUAL_THAN;
			if(s.equals("!="))
				return NOT_EQUAL_THAN;
			throw new IllegalArgumentException("Unknown enum string for: " + s);
		}
	}

	public EqualitySensor(Sensor lhs, Operator operator, Sensor rhs) {

		_lhs = lhs;
		_rhs = rhs;
		_operator = operator;

	} // Constructor

	public EqualitySensor(EqualitySensor rhs) {

		this((Sensor) rhs._lhs.clone(),
		     rhs._operator,
		     (Sensor) rhs._rhs.clone());

	} // Copy constructor

	public Object clone() {

		return new EqualitySensor(this);

	} // clone

	public ActionParameterType getType() {

		return ActionParameterType.BOOLEAN;

	} // getType

	public Object evaluate(int cycle, GameState gs, String player,
			Context parameters) {

		Object lhsResult;
		Object rhsResult;
		lhsResult = _lhs.evaluate(cycle, gs, player, parameters);
		rhsResult = _rhs.evaluate(cycle, gs, player, parameters);
		
		// Santi: if any of the values is null we consider them to be different (even if both are null)
		if (lhsResult!=null && rhsResult!=null) {
			return _operator.eval(new Value(lhsResult), new Value(rhsResult)) ? 1.0f : 0.0f;
		} else {
			return 0.0f;
		}
	} // evaluate

	/**
	 * Writes the EqualitySensor Sensor to an XMLWriter object
	 * 
	 * @param w The XMLWriter object
	 */
	public void writeToXML(XMLWriter w) {
		w.tagWithAttributes("Sensor", "type = '" + this.getClass().getName()
				+ "'");
		_lhs.writeToXML(w);
		_rhs.writeToXML(w);
		w.tag("Operator", _operator);
		w.tag("/Sensor");
	}

	/**
	 * Creates a EqualitySensor Sensor from an xml Element.
	 * 
	 * @param xml Element for creating the EqualitySensor Sensor.
	 * @return Created Sensor.
	 */
	public static Sensor loadFromXMLInternal(Element xml) {
		List<?> l = xml.getChildren("Sensor");
		Element c_xml = (Element) l.get(0);
		Sensor lhs = Sensor.loadFromXML(c_xml);
		c_xml = (Element) l.get(1);
		Sensor rhs = Sensor.loadFromXML(c_xml);
		Operator operator = Operator.stringToEnum(xml.getChildText("Operator"));
		EqualitySensor ret = new EqualitySensor(lhs, operator, rhs);

		return ret;
	}

	protected boolean internalEquivalents(Context parameters1, int cycle1, GameState gs1, String player1, Sensor s2, Context parameters2, int cycle2, GameState gs2, String player2) {

		EqualitySensor relOp;
		relOp = (EqualitySensor) s2;

		if (_operator != relOp._operator)
			return false;
		if ((_lhs == null) && (relOp._lhs != null))
			return false;
		if ((_rhs == null) && (relOp._rhs != null))
			return false;

		return (_lhs.equivalents(parameters1, cycle1, gs1, player1,
                                 relOp._lhs, parameters2, cycle2, gs2, player2) &&
                _rhs.equivalents(parameters1, cycle1, gs1, player1,
                                 relOp._rhs, parameters2, cycle2, gs2, player2));

	} // internalEquivalents

	protected Sensor _lhs;

	protected Sensor _rhs;

	protected Operator _operator;

} // class EqualitySensor
