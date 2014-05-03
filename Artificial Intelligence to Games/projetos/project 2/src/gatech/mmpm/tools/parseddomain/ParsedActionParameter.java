/* Copyright 2010 Santiago Ontanon and Ashwin Ram */

package gatech.mmpm.tools.parseddomain;

import gatech.mmpm.ActionParameterType;

import org.jdom.Element;

/**
 * Class to store information about a parameter action, as parsed
 * from a domain XML file.
 * 
 * It is used by the generator tool of the domain classes
 * using the XML, for the <Action> of <ActionSet>, and in
 * <Actions> of <Entity>. The first one expect to have name
 * and type; the second one expect to have name and value.
 * 
 * We do not test here if the needed information is provided. 
 * 
 * 
 * 
 * @author Pedro Pablo Gomez Martin
 * @date August, 2009
 */
public class ParsedActionParameter {
	/**
	 * Constructor. It creates an empty object. The init() method
	 * should be called afterwards.
	 */
	public ParsedActionParameter() {
	} // constructor

	//--------------------------------------------

	/**
	 * Initialize the object using the information of the
	 * XML node. It must be the next format:
	 * 
	 * <pre>
	 * &lt;Parameter name="name" type="type" value="value" /&gt;
	 * </pre>
	 * 
	 * If the parameter appears in the context of an
	 * Action in an ActionSet, type is mandatory, and
	 * value can't be provided.
	 * 
	 * If the parameter appears in the context of an
	 * Action in an Entity, value is mandatory, and
	 * type can't be provided.
	 * 
	 * These restriction are <em>not</em> tested here,
	 * anyway.
	 * 
	 * @param node Source XML node. 
	 * 
	 * @throws ParseException if some error occurs.
	 */
	public void init(Element node) throws ParseException {
		
		if (node.getName().compareToIgnoreCase("Parameter") != 0)
			throw new ParseException("Parameter node expected");

		_name = XMLAux.getRequiredAttribute(node, "name");
		
		if (!NameChecker.isValidIdentifier(_name))
			throw new ParseException("Invalid action parameter name (" + _name + ")");

		_value = XMLAux.getOptionalAttribute(node, "value");
		
		String aux;
		aux = XMLAux.getOptionalAttribute(node, "type");
		if (aux != null) {
			try {
				_type = Enum.valueOf(ActionParameterType.class, aux);
			}
			catch (IllegalArgumentException ex) {
				throw new ParseException("Invalid action parameter type (" + aux +
				                         "). See gatech.mmpm.ActionParameterType " +
				                         "for valid values.");
			}
		}
	} // init
		
	//--------------------------------------------

	public String getName() {
		
		return _name;
		
	} // getName
	
	//--------------------------------------------

	public String getValue() {
		
		return _value;
		
	} // getValue
	
	//--------------------------------------------

	public ActionParameterType getType() {
		
		return _type;

	}
	
	//--------------------------------------------
	
	protected String _name;

	protected String _value;

	protected ActionParameterType _type;
	
} // class ParsedActionParameter
