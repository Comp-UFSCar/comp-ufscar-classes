/* Copyright 2010 Santiago Ontanon and Ashwin Ram */

package gatech.mmpm.sensor.composite;

/**
 * Class that stores a value for any of the 
 * gatech.mmpm.ActionParameterType. Those
 * types are mapped on Java types in a specific
 * way (see the gatech.mmpm.ActionParameterType.getJavaType()
 * method). This class wraps a value for those
 * Java values, and provides methods for comparing
 * them.
 * 
 * The class <em>is not</em> designed to be used in
 * MMPM as a substitute of the original Java types,
 * but as a helper class for composite sensors that
 * must compare those values. So this class
 * provides the comparison policies between values.
 * So, for example, RelationalCondition has two sub-sensors
 * that, when evaluated, return just Object's. These objects
 * will be in fact one of the mapped Java classes
 * depending on each sensor MMPM type. Instead of forcing
 * RelationalCondition to know how to compare each
 * arbitrary pair of objects (depending on the valid
 * conditions described in gatech.mmpm.ActionParameterType),
 * <em>in runtime</em> RelationalCondition creates a pair
 * of Value's objects wrapping each Object returned by
 * the sub-sensors. After that, it asked the first
 * Value to compare itself with the second one, using
 * the MMPM semantic of type comparison.
 * 
 * @author Pedro Pablo Gomez Martin 
 * @date August, 2009
 */
public class Value implements Comparable<Value> {

	public Value(Object o) {
		if (!(o instanceof Class) &&     // ENTITY_TYPE
		    !(o instanceof gatech.mmpm.Entity) && // ENTITY_ID
		    !(o instanceof String) &&    // PLAYER and STRING
		    !(o instanceof float[]) &&   // COORDINATE
		    !(o instanceof Float) &&     // BOOLEAN, FLOAT
		    !(o instanceof Integer)&&     // INTEGER, DIRECTION
		    !(o == null)) {
//			if (o==null) throw new RuntimeException("Invalid class parameter in Value constructor: o is null");
			throw new RuntimeException("Invalid class parameter in Value constructor: o is of type " + o.getClass().getName());
		}
		_backend = o;
	} // Constructor

	/**
	 * Return true if both objects are equal. This will be used
	 * while evaluating an built-in expression with <tt>==</tt>
	 * or <tt>!=</tt> operator on it.
	 * 
	 * @param rhs Right Hand Side operand. It should be a
	 * Value object.
	 * @return True if this object has the same value of
	 * rhs, using the comparison policies of the language.
	 * 
	 * @note This method will raise a RuntimeException if the
	 * _backend value cannot be compared with the object of the
	 * parameter. This should never occur, assuming that the
	 * "expression compiler" is well programmed.
	 * 
	 * @see gatech.mmpm.sensors.composite.EqualitySensor
	 */
	public boolean equals(Object rhs) {

		if (!(rhs instanceof Value))
			throw new RuntimeException("Invalid object in parameter to Value.equals");
		
		if ((_backend == null) || (((Value)rhs)._backend == null))
			return false;

		else if (_backend instanceof Float && ((Value)rhs)._backend instanceof Integer) {
			Float l, r;
			l = (Float) _backend;
			r = (float)((Integer) ((Value)rhs)._backend);
			return l.equals(r);
		}
		else if (_backend instanceof Integer && ((Value)rhs)._backend instanceof Float) {
			Float l, r;
			l = (float)((Integer) _backend);
			r = (Float) ((Value)rhs)._backend;
			return l.equals(r);
		}

		return _backend.equals(((Value)rhs)._backend);

	} // equals

	/**
	 * Compares two values following the comparison policies
	 * for values of MMPM. This will be used while evaluating
	 * a built-in expression with <tt>&lt;</tt>, <tt>&gt;</tt>
	 * and such operator on it.
	 * 
	 * @param rhs Right hand side operand.
	 * @return <tt>0</tt> if this object is equal to <tt>rhs</tt>;
	 * a value less than <tt>0</tt> if this object is "numerically"
	 * less than the argument <tt>rhs</tt>; and a value greater
	 * than <tt>0</tt> if this object is "numerically" greater than
	 * the argument.
	 * 
	 * @note This method will raise a RuntimeException if the
	 * _backend value cannot be compared with the object of the
	 * parameter. This should never occur, assuming that the
	 * "expression compiler" is well programmed. Keep in mind that
	 * once the MMPM types has been mapped to Java types, some
	 * non-sense comparison will be allowed, for example &lt; for
	 * PLAYER because it is represented has a Java String.   
	 * 
	 * @see gatech.mmpm.sensors.composite.RelationalCondition
	 */
	public int compareTo(Value rhs) {

		if ((rhs == null) || 
		    (_backend instanceof Class) || (rhs._backend instanceof Class) ||     // ENTITY_TYPE
		    (_backend instanceof gatech.mmpm.Entity) || (rhs._backend instanceof gatech.mmpm.Entity) || // ENTITY_ID
		    (_backend instanceof float[]) || (rhs._backend instanceof float[])) {   // COORDINATE
			throw new RuntimeException("Invalid object class in Value.compareTo");
		}
		
		if ((_backend == null) || (((Value)rhs)._backend == null))
			return -1;

		if (_backend instanceof String) {
			String l, r;
			l = (String) _backend;
			r = (String) rhs._backend;
			return l.compareTo(r);
		}
		else if (_backend instanceof Float && rhs._backend instanceof Float) {
			Float l, r;
			l = (Float) _backend;
			r = (Float) rhs._backend;
			return l.compareTo(r);
		}
		else if (_backend instanceof Integer && rhs._backend instanceof Integer) {
			Integer l, r;
			l = (Integer) _backend;
			r = (Integer) rhs._backend;
			return l.compareTo(r);
		}
		else if (_backend instanceof Float && rhs._backend instanceof Integer) {
			Float l, r;
			l = (Float) _backend;
			r = (float)((Integer) rhs._backend);
			return l.compareTo(r);
		}
		else if (_backend instanceof Integer && rhs._backend instanceof Float) {
			Float l, r;
			l = (float)((Integer) _backend);
			r = (Float) rhs._backend;
			return l.compareTo(r);
		}
 
		// We should not reach this point...
		throw new RuntimeException("Invalid object class in Value.compareTo");
		
	} // compareTo

	/**
	 * Adds two values of MMPM. This will be used while evaluating
	 * a built-in expression with <tt>&lt;</tt>, <tt>&gt;</tt>
	 * and such operator on it.
	 * 
	 * @param rhs Right hand side operand.
	 * @return This object + <tt>rhs</tt>.
	 * 
	 * @note This method will raise a RuntimeException if the
	 * _backend value cannot be added with the object of the
	 * parameter. This should never occur, assuming that the
	 * "expression compiler" is well programmed. Keep in mind that
	 * once the MMPM types has been mapped to Java types, some
	 * non-sense comparison will be allowed, for example &lt; for
	 * PLAYER because it is represented has a Java String.   
	 * 
	 * @see gatech.mmpm.sensors.composite.ArithmeticCondition
	 */
	public Object add(Value rhs) {

		if ((rhs == null) || 
		    (_backend instanceof Class) || (rhs._backend instanceof Class) ||     // ENTITY_TYPE
		    (_backend instanceof gatech.mmpm.Entity) || (rhs._backend instanceof gatech.mmpm.Entity) ) // ENTITY_ID
		{   
			throw new RuntimeException("Invalid object class in Value.add");
		}
		
		if ((_backend == null) || (((Value)rhs)._backend == null))
			return null;

		if (_backend instanceof String && rhs._backend instanceof String) {
			String l, r;
			l = (String) _backend;
			r = (String) rhs._backend;
			return l.concat(r);
		}
		else if (_backend instanceof Float && rhs._backend instanceof Float) {
			Float l, r;
			l = (Float) _backend;
			r = (Float) rhs._backend;
			return l + r;
		}
		else if (_backend instanceof Integer && rhs._backend instanceof Integer) {
			Integer l, r;
			l = (Integer) _backend;
			r = (Integer) rhs._backend;
			return l + r;
		}
		else if (_backend instanceof Float && rhs._backend instanceof Integer) {
			Float l;
			Integer r;
			l = (Float) _backend;
			r = (Integer) rhs._backend;
			return l + r;
		}
		else if (_backend instanceof Integer && rhs._backend instanceof Float) {
			Float r;
			Integer l;
			r = (Float) rhs._backend;
			l = (Integer) _backend;
			return l + r;
		}
		else if (_backend instanceof float[] && rhs._backend instanceof float[]) {
			float[] l, r;
			l = (float[]) _backend;
			r = (float[]) rhs._backend;
			float res[] = new float[l.length];
			for(int i = 0; i < l.length; i++)
				res[i] = l[i] + r[i];
			return res;
		}
 
		// We should not reach this point...
		throw new RuntimeException("Invalid object class in Value.add");
		
	} // add

	/**
	 * Subtracts two values of MMPM. This will be used while evaluating
	 * a built-in expression with <tt>&lt;</tt>, <tt>&gt;</tt>
	 * and such operator on it.
	 * 
	 * @param rhs Right hand side operand.
	 * @return This object - <tt>rhs</tt>.
	 * 
	 * @note This method will raise a RuntimeException if the
	 * _backend value cannot be subtracted with the object of the
	 * parameter. This should never occur, assuming that the
	 * "expression compiler" is well programmed. Keep in mind that
	 * once the MMPM types has been mapped to Java types, some
	 * non-sense comparison will be allowed, for example &lt; for
	 * PLAYER because it is represented has a Java String.   
	 * 
	 * @see gatech.mmpm.sensors.composite.ArithmeticCondition
	 */
	public Object sub(Value rhs) {

		if ((rhs == null) || 
		    (_backend instanceof Class) || (rhs._backend instanceof Class) ||     // ENTITY_TYPE
		    (_backend instanceof gatech.mmpm.Entity) || (rhs._backend instanceof gatech.mmpm.Entity) || // ENTITY_ID
		    (_backend instanceof String) || (rhs._backend instanceof String) ) // STRING & PLAYER
		{   
			throw new RuntimeException("Invalid object class in Value.sub");
		}
		
		if ((_backend == null) || (((Value)rhs)._backend == null))
			return null;
		
		if (_backend instanceof Float && rhs._backend instanceof Float) {
			Float l, r;
			l = (Float) _backend;
			r = (Float) rhs._backend;
			return l - r;
		}
		else if (_backend instanceof Integer && rhs._backend instanceof Integer) {
			Integer l, r;
			l = (Integer) _backend;
			r = (Integer) rhs._backend;
			return l - r;
		}
		else if (_backend instanceof Float && rhs._backend instanceof Integer) {
			Float l;
			Integer r;
			l = (Float) _backend;
			r = (Integer) rhs._backend;
			return l - r;
		}
		else if (_backend instanceof Integer && rhs._backend instanceof Float) {
			Float r;
			Integer l;
			r = (Float) rhs._backend;
			l = (Integer) _backend;
			return l + r;
		}
		else if (_backend instanceof float[] && rhs._backend instanceof float[]) {
			float[] l, r;
			l = (float[]) _backend;
			r = (float[]) rhs._backend;
			float res[] = new float[l.length];
			for(int i = 0; i < l.length; i++)
				res[i] = l[i] - r[i];
			return res;
		}
 
		// We should not reach this point...
		throw new RuntimeException("Invalid object class in Value.sub");
		
	} // sub

	/**
	 * Multiplies two values of MMPM. This will be used while evaluating
	 * a built-in expression with <tt>&lt;</tt>, <tt>&gt;</tt>
	 * and such operator on it.
	 * 
	 * @param rhs Right hand side operand.
	 * @return This object * <tt>rhs</tt>.
	 * 
	 * @note This method will raise a RuntimeException if the
	 * _backend value cannot be multiplied with the object of the
	 * parameter. This should never occur, assuming that the
	 * "expression compiler" is well programmed. Keep in mind that
	 * once the MMPM types has been mapped to Java types, some
	 * non-sense comparison will be allowed, for example &lt; for
	 * PLAYER because it is represented has a Java String.   
	 * 
	 * @see gatech.mmpm.sensors.composite.ArithmeticCondition
	 */
	public Object mult(Value rhs) {

		if ((rhs == null) || 
		    (_backend instanceof Class) || (rhs._backend instanceof Class) ||     // ENTITY_TYPE
		    (_backend instanceof gatech.mmpm.Entity) || (rhs._backend instanceof gatech.mmpm.Entity) || // ENTITY_ID
		    (_backend instanceof String) || (rhs._backend instanceof String) ) // STRING & PLAYER
		{   
			throw new RuntimeException("Invalid object class in Value.mult");
		}
		
		if ((_backend == null) || (((Value)rhs)._backend == null))
			return null;
		
		if (_backend instanceof Float && rhs._backend instanceof Float) {
			Float l, r;
			l = (Float) _backend;
			r = (Float) rhs._backend;
			return l * r;
		}
		else if (_backend instanceof Integer && rhs._backend instanceof Integer) {
			Integer l, r;
			l = (Integer) _backend;
			r = (Integer) rhs._backend;
			return l * r;
		}
		else if (_backend instanceof Float && rhs._backend instanceof Integer) {
			Float l;
			Integer r;
			l = (Float) _backend;
			r = (Integer) rhs._backend;
			return l * r;
		}
		else if (_backend instanceof Integer && rhs._backend instanceof Float) {
			Float r;
			Integer l;
			r = (Float) rhs._backend;
			l = (Integer) _backend;
			return l * r;
		}
		else if (_backend instanceof float[] && rhs._backend instanceof float[]) {
			float[] l, r;
			l = (float[]) _backend;
			r = (float[]) rhs._backend;
			float res[] = new float[l.length];
			for(int i = 0; i < l.length; i++)
				res[i] = l[i] * r[i];
			return res;
		}
		else if (_backend instanceof Integer && rhs._backend instanceof float[]) {
			Integer l;
			float[] r;
			l = (Integer) _backend;
			r = (float[]) rhs._backend;
			float res[] = new float[r.length];
			for(int i = 0; i < r.length; i++)
				res[i] = l * r[i];
			return res;
		}
		else if (_backend instanceof Float && rhs._backend instanceof float[]) {
			Float l;
			float[] r;
			l = (Float) _backend;
			r = (float[]) rhs._backend;
			float res[] = new float[r.length];
			for(int i = 0; i < r.length; i++)
				res[i] = l * r[i];
			return res;
		}
		else if (_backend instanceof float[] && rhs._backend instanceof Integer) {
			float[] l;
			Integer r;
			l = (float[]) _backend;
			r = (Integer) rhs._backend;
			float res[] = new float[l.length];
			for(int i = 0; i < l.length; i++)
				res[i] = l[i] * r;
			return res;
		}
		else if (_backend instanceof float[] && rhs._backend instanceof Float) {
			float[] l;
			Float r;
			l = (float[]) _backend;
			r = (Float) rhs._backend;
			float res[] = new float[l.length];
			for(int i = 0; i < l.length; i++)
				res[i] = l[i] * r;
			return res;
		}
 
		// We should not reach this point...
		throw new RuntimeException("Invalid object class in Value.mult");
		
	} // mult

	/**
	 * Divides two values of MMPM. This will be used while evaluating
	 * a built-in expression with <tt>&lt;</tt>, <tt>&gt;</tt>
	 * and such operator on it.
	 * 
	 * @param rhs Right hand side operand.
	 * @return This object / <tt>rhs</tt>.
	 * 
	 * @note This method will raise a RuntimeException if the
	 * _backend value cannot be divided with the object of the
	 * parameter. This should never occur, assuming that the
	 * "expression compiler" is well programmed. Keep in mind that
	 * once the MMPM types has been mapped to Java types, some
	 * non-sense comparison will be allowed, for example &lt; for
	 * PLAYER because it is represented has a Java String.   
	 * 
	 * @see gatech.mmpm.sensors.composite.ArithmeticCondition
	 */
	public Object div(Value rhs) {

		if ((rhs == null) || 
		    (_backend instanceof Class) || (rhs._backend instanceof Class) ||     // ENTITY_TYPE
		    (_backend instanceof gatech.mmpm.Entity) || (rhs._backend instanceof gatech.mmpm.Entity) || // ENTITY_ID
		    (_backend instanceof String) || (rhs._backend instanceof String) ) // STRING & PLAYER
		{   
			throw new RuntimeException("Invalid object class in Value.div");
		}
		
		if ((_backend == null) || (((Value)rhs)._backend == null))
			return null;
		
		if (_backend instanceof Float && rhs._backend instanceof Float) {
			Float l, r;
			l = (Float) _backend;
			r = (Float) rhs._backend;
			return (Float)(l / r);
		}
		else if (_backend instanceof Integer && rhs._backend instanceof Integer) {
			Integer l, r;
			l = (Integer) _backend;
			r = (Integer) rhs._backend;
			return (Integer)(l / r);
		}
		else if (_backend instanceof Float && rhs._backend instanceof Integer) {
			float l, r;
			l = (Float) _backend;
			r = (Integer) rhs._backend;
			return (Float)(l / r);
		}
		else if (_backend instanceof Integer && rhs._backend instanceof Float) {
			float l, r;
			r = (Float) rhs._backend;
			l = (Integer) _backend;
			return (Float)(l / r);
		}
		else if (_backend instanceof float[] && rhs._backend instanceof float[]) {
			float[] l, r;
			l = (float[]) _backend;
			r = (float[]) rhs._backend;
			float res[] = new float[l.length];
			for(int i = 0; i < l.length; i++)
				res[i] = l[i] / r[i];
			return res;
		}
		else if (_backend instanceof float[] && rhs._backend instanceof Integer) {
			float[] l;
			float r;
			l = (float[]) _backend;
			r = (Integer) rhs._backend;
			float res[] = new float[l.length];
			for(int i = 0; i < l.length; i++)
				res[i] = l[i] / r;
			return res;
		}
		else if (_backend instanceof float[] && rhs._backend instanceof Float) {
			float[] l;
			float r;
			l = (float[]) _backend;
			r = (Float) rhs._backend;
			float res[] = new float[l.length];
			for(int i = 0; i < l.length; i++)
				res[i] = l[i] / r;
			return res;
		}
 
		// We should not reach this point...
		throw new RuntimeException("Invalid object class in Value.div");
		
	} // div

	/**
	 * Applies the MOD operator to two values of MMPM. 
	 * This will be used while evaluating
	 * a built-in expression with <tt>&lt;</tt>, <tt>&gt;</tt>
	 * and such operator on it.
	 * 
	 * @param rhs Right hand side operand.
	 * @return This object / <tt>rhs</tt>.
	 * 
	 * @note This method will raise a RuntimeException if in the
	 * _backend value cannot be applied the MOD operation with the 
	 * object of the parameter. This should never occur, assuming that
	 * the "expression compiler" is well programmed. Keep in mind that
	 * once the MMPM types has been mapped to Java types, some
	 * non-sense comparison will be allowed, for example &lt; for
	 * PLAYER because it is represented has a Java String.   
	 * 
	 * @see gatech.mmpm.sensors.composite.ArithmeticCondition
	 */
	public Object mod(Value rhs) {

		if ((rhs == null) || 
		    (_backend instanceof Class) || (rhs._backend instanceof Class) ||     // ENTITY_TYPE
		    (_backend instanceof gatech.mmpm.Entity) || (rhs._backend instanceof gatech.mmpm.Entity) || // ENTITY_ID
		    (_backend instanceof String) || (rhs._backend instanceof String) ) // STRING & PLAYER
		{   
			throw new RuntimeException("Invalid object class in Value.mod");
		}
		
		if ((_backend == null) || (((Value)rhs)._backend == null))
			return null;
		
		if (_backend instanceof Float && rhs._backend instanceof Float) {
			Float l, r;
			l = (Float) _backend;
			r = (Float) rhs._backend;
			return l % r;
		}
		else if (_backend instanceof Integer && rhs._backend instanceof Integer) {
			Integer l, r;
			l = (Integer) _backend;
			r = (Integer) rhs._backend;
			return l % r;
		}
		else if (_backend instanceof Float && rhs._backend instanceof Integer) {
			Float l;
			Integer r;
			l = (Float) _backend;
			r = (Integer) rhs._backend;
			return l % r;
		}
		else if (_backend instanceof Integer && rhs._backend instanceof Float) {
			Float r;
			Integer l;
			r = (Float) rhs._backend;
			l = (Integer) _backend;
			return l % r;
		}
		else if (_backend instanceof float[] && rhs._backend instanceof float[]) {
			float[] l, r;
			l = (float[]) _backend;
			r = (float[]) rhs._backend;
			float res[] = new float[l.length];
			for(int i = 0; i < l.length; i++)
				res[i] = l[i] % r[i];
			return res;
		}
		else if (_backend instanceof float[] && rhs._backend instanceof Integer) {
			float[] l;
			Integer r;
			l = (float[]) _backend;
			r = (Integer) rhs._backend;
			float res[] = new float[l.length];
			for(int i = 0; i < l.length; i++)
				res[i] = l[i] % r;
			return res;
		}
		else if (_backend instanceof float[] && rhs._backend instanceof Float) {
			float[] l;
			Float r;
			l = (float[]) _backend;
			r = (Float) rhs._backend;
			float res[] = new float[l.length];
			for(int i = 0; i < l.length; i++)
				res[i] = l[i] % r;
			return res;
		}
 
		// We should not reach this point...
		throw new RuntimeException("Invalid object class in Value.mod");
		
	} // mod

	/**
	 * Real object. It will be one of the basic
	 * wrapping Java classes.
	 */
	protected Object _backend;

} // class Value
