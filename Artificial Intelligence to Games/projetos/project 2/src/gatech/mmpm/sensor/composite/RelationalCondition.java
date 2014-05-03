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
 * Sensor that applies a relational operator (<tt>&lt;</tt>,
 * <tt>&gt;</tt>, and so on) to the result of
 * the evaluation of two other sensors.
 * 
 * @note If you decide to change the name of this class, you
 * should also change the Builtin2Java class.
 * 
 * @note <tt>==</tt> and <tt>!=</tt> are applied by the
 * EqualitySensor class.
 * 
 * @author Pedro Pablo Gomez Martin
 * @date August, 2009
 */
public class RelationalCondition extends Sensor {

	public enum Operator {
/*		EQUAL_THAN {
			public <T> boolean eval(Comparable<T> lhs, T rhs) {
				return lhs.compareTo(rhs) == 0;
			}
			public String toString() {
				return "==";
			}
		},
		NOT_EQUAL_THAN {
			public <T> boolean eval(Comparable<T> lhs, T rhs) {
				return lhs.compareTo(rhs) != 0;
			}
			public String toString() {
				return "!=";
			}
		},*/
		LESS_THAN { 
			public <T> boolean eval(Comparable<T> lhs, T rhs) {
				return lhs.compareTo(rhs) < 0;
			}
			public String toString() {
				return "<";
			}
		},
		LESS_EQUAL_THAN { 
			public <T> boolean eval(Comparable<T> lhs, T rhs) {
				return lhs.compareTo(rhs) <= 0;
			}
			public String toString() {
				return "<=";
			}
		},
		GREATER_THAN { 
			public <T> boolean eval(Comparable<T> lhs, T rhs) {
				return lhs.compareTo(rhs) > 0;
			}
			public String toString() {
				return ">";
			}
		},
		GREATER_EQUAL_THAN { 
			public <T> boolean eval(Comparable<T> lhs, T rhs) {
				return lhs.compareTo(rhs) >= 0;
			}
			public String toString() {
				return ">=";
			}
		};
		
		public abstract <T> boolean eval(Comparable<T> lhs, T rhs);
	    
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
			if(s.equals("<"))
				return LESS_THAN;
			if(s.equals("<="))
				return LESS_EQUAL_THAN;
			if(s.equals(">"))
				return GREATER_THAN;
			if(s.equals(">="))
				return GREATER_EQUAL_THAN;
			throw new IllegalArgumentException("Unknown enum string for: " + s);
		}

		
	} // enum Operator
	
	public RelationalCondition(Sensor lhs, Operator operator, Sensor rhs) {
		
		_lhs = lhs;
		_rhs = rhs;
		_operator = operator;
		
	} // Constructor

	public RelationalCondition(RelationalCondition rhs) {

		this((Sensor)rhs._lhs.clone(), rhs._operator, (Sensor)rhs._rhs.clone());

	} // Copy constructor

	public Object clone() {

		return new RelationalCondition(this);

	} // clone
	
	public ActionParameterType getType() {

		return ActionParameterType.BOOLEAN;

	} // getType

	public Object evaluate(int cycle, GameState gs, String player, Context parameters) {

		Object lhsResult;
		Object rhsResult;
		lhsResult = _lhs.evaluate(cycle, gs, player, parameters);
		rhsResult = _rhs.evaluate(cycle, gs, player, parameters);
		
//		System.out.println(_operator);
//		System.out.println("LHS: " + lhsResult);
//		System.out.println("RHS: " + rhsResult);

		// The class "Value" implements Comparable<Value>
		// and that receives an Object as parameter in the constructor.
		// That Object will only be allowed to be one of the valid types
		// for expressions (that is the Java types which ActionParameterType
		// are mapped into). In compareTo(), all the nasty work about
		// knowing when two types can be compared is done, and a value is
		// returned (or a RuntimeException). The "compiler" that builds the
		// expression should be clever enough to only allow those comparisons
		// that make sense for that class (and vice versa).
		return _operator.eval(new Value(lhsResult), new Value(rhsResult))?1.0f:0.0f;

	} // evaluate

	/**
	 * Writes the RelationalCondition Sensor to an XMLWriter object
	 * 
	 * @param w The XMLWriter object
	 */
	public void writeToXML(XMLWriter w) {
		w.tagWithAttributes("Sensor", "type = '" + this.getClass().getName()
				+ "'");
		_lhs.writeToXML(w);
		_rhs.writeToXML(w);
		w.tag("Operator", "<![CDATA[" + _operator + "]]>");
		w.tag("/Sensor");
	}

	/**
	 * Creates a RelationalCondition Sensor from an xml Element.
	 * 
	 * @param xml Element for creating the RelationalCondition Sensor.
	 * @return Created Sensor.
	 */
	public static Sensor loadFromXMLInternal(Element xml) {
		List<?> l = xml.getChildren("Sensor");
		Element c_xml = (Element) l.get(0);
		Sensor lhs = Sensor.loadFromXML(c_xml);
		c_xml = (Element) l.get(1);
		Sensor rhs = Sensor.loadFromXML(c_xml);
		String s = xml.getChildText("Operator");
		Operator operator = Operator.stringToEnum(s);
		RelationalCondition ret = new RelationalCondition(lhs, operator, rhs);

		return ret;
	}

	protected boolean internalEquivalents(Context parameters1, int cycle1, GameState gs1, String player1, Sensor s2, Context parameters2, int cycle2, GameState gs2, String player2) {

		RelationalCondition relOp;
		relOp = (RelationalCondition) s2;
		
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


	public String toString() {
		return _lhs + " " + _operator + " " + _rhs;
	}

	
	protected Sensor _lhs;
	
	protected Sensor _rhs;
	
	protected Operator _operator;
	
} // RelationalCondition
