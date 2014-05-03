/* Copyright 2010 Santiago Ontanon and Ashwin Ram */

package gatech.mmpm;

/**
 * 
 * @author Pedro Pablo Gomez martin
 * @date Enero 2010
 */
public class Context {

	public Context() {
		_hash = new java.util.HashMap<String, Object>();
	} // Constructor

	//---------------------------------------------------------------

	public Context(Context rhs) {
		_hash = new java.util.HashMap<String, Object>(rhs._hash);
	} // Copy constructor

	//---------------------------------------------------------------

	public Context clone(Context rhs) {

		return new Context(this);

	} // clone

	//---------------------------------------------------------------

	public boolean containsKey(String key) {

		return _hash.containsKey(key);

	} // containsKey
	
	//---------------------------------------------------------------

	public Object get(String key) {

		return _hash.get(key);

	} // containsKey
	
	//---------------------------------------------------------------

	public void put(String key, Object value) {
		
		// TODO: locked context?
		_hash.put(key, value);
		
	} // put

    //---------------------------------------------------------------

	public void put(Context cxt) {
        _hash.putAll(cxt._hash);
	} // put
	
	//---------------------------------------------------------------

	public String toString() {
		String ret = "[ ";
		for(String pn:_hash.keySet()) {
			ret += "(" + pn + " : " + _hash.get(pn) + ") ";
		}
		return ret + " ]";
	}
	
	//---------------------------------------------------------------
	//                            Protected fields
	//---------------------------------------------------------------

	protected java.util.HashMap<String, Object> _hash;
	
} // class Context
