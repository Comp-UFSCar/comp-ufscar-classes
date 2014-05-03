/* Copyright 2010 Santiago Ontanon and Ashwin Ram */

package gatech.mmpm.sensor.composite;


import gatech.mmpm.ActionParameterType;
import gatech.mmpm.Context;
import gatech.mmpm.GameState;
import gatech.mmpm.sensor.Sensor;

/**
 * Sensor that applies an arithmetic operator (<tt>+</tt>, <tt>-</tt>
 * and so on) to the evaluation of two other sensors.
 * 
 * @note If you decide to change the name of this class, you
 * should also change the Builtin2Java class.
 * 
 * @author Pedro Pablo Gomez Martin
 * @date August, 2009
 */
public class ArithmeticSensor extends Sensor {

	public enum Operator {
		ADD {
			public Object eval(Value lhs, Value rhs) {
				return lhs.add(rhs);
			}
			public String toString() {
				return "+";
			}
		},
		MINUS {
			public Object eval(Value lhs, Value rhs) {
				return lhs.sub(rhs);
			}
			public String toString() {
				return "-";
			}
		},
		MULT {
			public Object eval(Value lhs, Value rhs) {
				return lhs.mult(rhs);
			}
			public String toString() {
				return "*";
			}
		},
		DIV {
			public Object eval(Value lhs, Value rhs) {
				return lhs.div(rhs);
			}
			public String toString() {
				return "/";
			}
		},
		MOD {
			public Object eval(Value lhs, Value rhs) {
				return lhs.mod(rhs);
			}
			public String toString() {
				return "%";
			}
		};
		
		public abstract Object eval(Value lhs, Value rhs);
	    
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
			if(s.equals("+"))
				return ADD;
			if(s.equals("-"))
				return MINUS;
			if(s.equals("*"))
				return MULT;
			if(s.equals("/"))
				return DIV;
			if(s.equals("%"))
				return MOD;
			throw new IllegalArgumentException("Unknown enum string for: " + s);
		}
	}	

	public ArithmeticSensor(Sensor lhs, Operator op, Sensor rhs) {
		_lhs = lhs;
		_op = op;
		_rhs = rhs;
	}

	public ArithmeticSensor(ArithmeticSensor a) {
		this((Sensor) a._lhs.clone(),
			 a._op,
			 (Sensor) a._rhs.clone());
	}

	public Object clone() {

		return new ArithmeticSensor((Sensor)_lhs.clone(), _op, (Sensor)_rhs.clone());

	}

	public Object evaluate(int cycle, GameState gs, String player,
			Context parameters) {
		
		Object lhsResult;
		Object rhsResult;
		lhsResult = _lhs.evaluate(cycle, gs, player, parameters);
		rhsResult = _rhs.evaluate(cycle, gs, player, parameters);

		return _op.eval(new Value(lhsResult), new Value(rhsResult));

	}

	@Override
	public ActionParameterType getType() {
		
		if(_lhs.getType() == ActionParameterType.COORDINATE || 
				_lhs.getType() == ActionParameterType.COORDINATE )
			return ActionParameterType.COORDINATE;
		else if(_lhs.getType() == ActionParameterType.STRING || 
				_lhs.getType() == ActionParameterType.STRING )
			return ActionParameterType.STRING;
		else if(_lhs.getType() == ActionParameterType.PLAYER || 
				_lhs.getType() == ActionParameterType.PLAYER )
			return ActionParameterType.PLAYER;
		else if(_lhs.getType() == ActionParameterType.FLOAT || 
				_lhs.getType() == ActionParameterType.FLOAT )
			return ActionParameterType.FLOAT;
		else if(_lhs.getType() == ActionParameterType.INTEGER || 
				_lhs.getType() == ActionParameterType.INTEGER )
			return ActionParameterType.INTEGER;
		return null;
	}

	
	protected Sensor _lhs;
	
	protected Sensor _rhs;
	
	protected Operator _op;

} // ArithmeticSensor
