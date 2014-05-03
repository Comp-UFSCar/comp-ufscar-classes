/* Copyright 2010 Santiago Ontanon and Ashwin Ram */

package gatech.mmpm.tools.parseddomain;

import org.jdom.Element;

import java.util.List;

import gatech.mmpm.ActionParameterType;


/**
 * Class to parse &lt;Sensor&gt; and &lt;Goal&gt; nodes.
 * They are nearly the same, but because the second one has
 * not returned type (it is assumed to be boolean) and
 * the first one has one.
 * 
 * @author Pedro Pablo Gomez Martin
 * @date August, 2009
 */
public class ParsedMethod {

	/**
	 * Constructor. It creates an empty object. The init() method
	 * should be called afterwards.
	 */
	public ParsedMethod() {
		
		_parameters = new java.util.LinkedList<ParsedActionParameter>();
		
	} // Constructor

	//--------------------------------------------

	/**
	 * Initialize the object using the information of the
	 * XML node. It must be the next format:
	 * 
	 * <pre>
	 * &lt;Sensor name="name"&gt;
	 *    &lt;Parameter  ... /&gt;
	 *    &lt;Parameter  ... /&gt;
	 *    ...
	 * &lt;/Action&gt;
	 * </pre>
	 *
	 * @param node Source XML node. 
	 * 
	 * @throws ParseException if some error occurs.
	 * 
	 * @see ParsedActionParameter
	 */
	public void init(Element node) throws ParseException {

		if ((node.getName().compareToIgnoreCase("Sensor") != 0) &&
		    (node.getName().compareToIgnoreCase("Goal") != 0) &&
		    (node.getName().compareToIgnoreCase("WinGoal") != 0))
			throw new ParseException("Sensor, Goal or WinGoal node expected (get '" + node.getName() + "')");

		_name = XMLAux.getRequiredAttribute(node, "name");

		if (!NameChecker.isValidIdentifier(_name))
			throw new ParseException("Invalid Sensor/Goal name (" + _name + ")");

		if ( (node.getName().compareToIgnoreCase("Goal") == 0) ||
			 (node.getName().compareToIgnoreCase("WinGoal") == 0)) {
			_isGoal = true;
			_returnedType = ActionParameterType.BOOLEAN;
		}
		else {
			_isGoal= false;
			String aux;
			aux = XMLAux.getRequiredAttribute(node, "type");
			if (aux != null) {
				try {
					_returnedType = Enum.valueOf(ActionParameterType.class, aux);
				}
				catch (IllegalArgumentException ex) {
					throw new ParseException("Invalid returned type (" + aux +
					                         ") for sensor '" + _name + "'. See " +
					                         "gatech.mmpm.ActionParameterType for valid values.");
				}
			}
		}
		
		List<?> children = node.getChildren();
		boolean hasCode = false;

		for (Object o:children) {
			Element c = (Element) o;
			if (c.getName().compareToIgnoreCase("Parameter") == 0) {
				ParsedActionParameter p = new ParsedActionParameter();
				p.init(c);
				_parameters.add(p);
			}
			else if (c.getName().compareToIgnoreCase("Code") == 0) {
				if (hasCode) {
					// This should be changed if code for different
					// languages would be allowed.
					throw new ParseException(node.getName() + " '" + _name +"' has two " +
					                         "<Code> elements."); 
				}
				hasCode = true;
				_language = XMLAux.getOptionalAttribute(c, "language");
				_code = c.getText().trim();
			}
			else {
				throw new ParseException("Invalid '" + c.getName() +
				                         "' node in " + node.getName() + " '" + _name + "'.");
			}
		} // for
		
	} // init

	//--------------------------------------------
	
	public String getName() {
		
		return _name;
		
	} // getName
	
	//--------------------------------------------

	public List<ParsedActionParameter> getParameters() {
		
		return _parameters;
		
	}
	
	//--------------------------------------------

	public ParsedActionParameter getParameter(String name) {

		for (ParsedActionParameter p:_parameters) {
			if (p.getName().compareTo(name) == 0)
				return p;
		}

		return null;
	}
	
	//--------------------------------------------

	public boolean isGoal() {
		
		return _isGoal;
		
	}
	
	//--------------------------------------------
	
	public ActionParameterType getReturnedType() {
		return _returnedType;
	}
	
	//--------------------------------------------
	
	public String getCode() {
		// TODO add String language parameter when more than
		// one <Code> node will be allowed (for different
		// languages).
		return _code;
	}
	
	//--------------------------------------------

	public String getLanguage() {
		return _language;
	}
	
	//--------------------------------------------
	
	public boolean isNative() {
		return _native;
	}
	
	protected String _name; 

	List<ParsedActionParameter> _parameters;

	/**
	 * Returned type (automatically set to boolean
	 * for conditions). 
	 */
	protected ActionParameterType _returnedType;

	/**
	 * True if this object was initialized with a
	 * &lt;Goal&gt; node, and false if it was
	 * with a &lt;Sensor&gt; node.
	 */
	boolean _isGoal;
	
	String _code;

	String _language;

	protected boolean _native = false;
	// Set externally by ParsedMethodSet::processAsNativeDomain().

} // class ParsedMethod
