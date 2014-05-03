/* Copyright 2010 Santiago Ontanon and Ashwin Ram */

package gatech.mmpm.util;

public class Pair<T1, T2> implements java.io.Serializable {
	
	/**
	 * Serial Version ID machine generated. 
	 */
	private static final long serialVersionUID = 7433870752560497552L;

	public T1 getFirst() {
		return _a;
	}
	
	public void setFirst(T1 a) {
		_a = a;
	}
	
	public void setSecond(T2 b) {
		_b = b;
	}

	public T2 getSecond() {
		return _b;
	}
	
	public T1 _a;
	public T2 _b;

	public Pair(T1 a,T2 b) {
		_a = a;
		_b = b;
	}
	
	public boolean equals(Object o) {
		if (o instanceof Pair) {
			return _a.equals(((Pair<T1,T2>)o)._a) && _b.equals(((Pair)o)._b);
		}
		return false;
	}
}
