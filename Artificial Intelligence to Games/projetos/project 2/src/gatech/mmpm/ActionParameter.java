/* Copyright 2010 Santiago Ontanon and Ashwin Ram */

package gatech.mmpm;

public class ActionParameter implements java.io.Serializable {
	
	/**
	 * Serial version UID used when the object is serialized.
	 */
	private static final long serialVersionUID = -2229697673456992688L;
//	static final long serialVersionUID = 0x342342;
	// Changed to a real random value (eclipse-generated).

	public String m_name;
	public ActionParameterType m_type;
	
	public ActionParameter(String name, ActionParameterType type) {
		m_name = name;
		m_type = type;
	}

	public String getName() {
		return m_name;
	}
	
	public ActionParameterType getType() {
		return m_type;
	}

} // class ActionParameter
