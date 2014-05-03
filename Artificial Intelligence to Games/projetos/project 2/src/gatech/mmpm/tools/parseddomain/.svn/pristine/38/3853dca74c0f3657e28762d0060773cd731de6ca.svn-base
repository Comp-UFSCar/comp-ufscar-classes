/* Copyright 2010 Santiago Ontanon and Ashwin Ram */

package gatech.mmpm.tools.parseddomain;

import org.jdom.Element;

import java.util.List;

/**
 * Class to store information about an action, as parsed from a
 * domain XML file.
 * 
 * It is used by the generator tool of the domain classes
 * using the XML
 * 
 * @author Pedro Pablo Gomez Martin
 * @date August, 2009
 * 
 * @note This class is uncompleted, until the complete XML specification
 * is done. 
 */
public class ParsedAction {

	/**
	 * Constructor. It creates an empty object. The init() method
	 * should be called afterwards.
	 */
	public ParsedAction() {
		
		_parameters = new java.util.LinkedList<ParsedActionParameter>();
		
	} // Constructor

	//--------------------------------------------

	/**
	 * Initialize the object using the information of the
	 * XML node. It must be the next format:
	 * 
	 * <pre>
	 * &lt;Action name="name"&gt;
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

		if (node.getName().compareToIgnoreCase("Action") != 0)
			throw new ParseException("Action node expected");
		
		_name = XMLAux.getRequiredAttribute(node, "name");
		
		if (!NameChecker.isValidIdentifier(_name))
			throw new ParseException("Invalid Action name (" + _name + ")");

		List<?> children = node.getChildren();

		for (Object o:children) {
			Element c = (Element) o;
			if (c.getName().compareToIgnoreCase("Parameter") == 0) {
				ParsedActionParameter p = new ParsedActionParameter();
				p.init(c);
				_parameters.add(p);
			}
			else if (c.getName().compareToIgnoreCase("PreCondition") == 0) {
				if (_preCondition != null)
					throw new ParseException("Second PreCondition found in " + _name);
				_preCondition = c.getText().trim();
			}
			else if (c.getName().compareToIgnoreCase("PostCondition") == 0) {
				if (_postCondition != null)
					throw new ParseException("Second PostCondition found in " + _name);
				_postCondition = c.getText().trim();
			}
			else if (c.getName().compareToIgnoreCase("FailureCondition") == 0) {
				if (_failureCondition != null)
					throw new ParseException("Second FailureCondition found in " + _name);
				_failureCondition = c.getText().trim();
			}
			else if (c.getName().compareToIgnoreCase("SuccessCondition") == 0) {
				if (_successCondition != null)
					throw new ParseException("Second SuccessCondition found in " + _name);
				_successCondition = c.getText().trim();
			}
			else if (c.getName().compareToIgnoreCase("ValidCondition") == 0) {
				if (_validCondition != null)
					throw new ParseException("Second ValidCondition found in " + _name);
				_validCondition = c.getText().trim();
			}
			else if (c.getName().compareToIgnoreCase("PreFailureCondition") == 0) {
				if (_preFailureCondition != null)
					throw new ParseException("Second PreFailureCondition found in " + _name);
				_preFailureCondition = c.getText().trim();
			}
			else if (c.getName().compareToIgnoreCase("OnPreCondition") == 0) {
				if (_onPreCondition != null)
					throw new ParseException("Second OnPreCondition found in " + _name);
				_onPreCondition = c.getText().trim();
			}
			else if (c.getName().compareToIgnoreCase("OnPostCondition") == 0) {
				if (_onPostCondition != null)
					throw new ParseException("Second OnPostCondition found in " + _name);
				_onPostCondition = c.getText().trim();
			}
			else if (c.getName().compareToIgnoreCase("OnPreFailureCondition") == 0) {
				if (_onPreFailureCondition != null)
					throw new ParseException("Second OnPreFailureCondition found in " + _name);
				_onPreFailureCondition = c.getText().trim();
			}
			else if (c.getName().compareToIgnoreCase("OnFailureCondition") == 0) {
				if (_onFailureCondition != null)
					throw new ParseException("Second OnFailureCondition found in " + _name);
				_onFailureCondition = c.getText().trim();
			}
			else if (c.getName().compareToIgnoreCase("OnSuccessCondition") == 0) {
				if (_onSuccessCondition != null)
					throw new ParseException("Second OnSuccessCondition found in " + _name);
				_onSuccessCondition = c.getText().trim();
			}
			else if (c.getName().compareToIgnoreCase("OnValidCondition") == 0) {
				if (_onValidCondition != null)
					throw new ParseException("Second OnValidCondition found in " + _name);
				_onValidCondition = c.getText().trim();
			}
			else if (c.getName().compareToIgnoreCase("OnPreFailureCondition") == 0) {
				
			}
			else {
				throw new ParseException("Invalid '" + c.getName() +
				                         "' node in Action '" + _name + "'.");
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

	// process should have been called!
	public String getParametersJavaDeclaration() {
		
		// For each parameter, returns
		//     protected <datatype> _<name>;
		StringBuffer sb = new StringBuffer();
		for (ParsedActionParameter p:_parameters) {
			sb.append("\tprotected ");
			sb.append(p.getType().getJavaTypeDeclaration());
			sb.append(" _");
			sb.append(p.getName());
			sb.append(";\n\n");
		} // for

		return sb.toString();
		
	} // getParametersJavaDeclaration
	
	//--------------------------------------------
	
	// process should have been called!
	public String getJavaCopyDeclaredParameters() {

		// For each parameter, returns
		//     _<name> = rhs._<name>;
		StringBuffer sb = new StringBuffer();
		for (ParsedActionParameter p:_parameters) {
			sb.append("\t\t_");
			sb.append(p.getName());
			sb.append(" = rhs._");
			sb.append(p.getName());
			sb.append(";\n");
		} // for

		return sb.toString();
		
	} // getJavaCopyDeclaredParameters
	
	//--------------------------------------------
	
	// process should have been called!
	public String getJavaParameterListInitialization() {

		// For each parameter, returns
		//     _listOfParameters.add(new ActionParameter("<feature name>", type);
		StringBuffer sb = new StringBuffer();
		for (ParsedActionParameter p:_parameters) {
			sb.append("\t\t_listOfParameters.add(new ActionParameter(\"");
			sb.append(p.getName());
			sb.append("\", ActionParameterType.");
			sb.append(p.getType());
			sb.append("));\n");
		} // for

		return sb.toString();
		
	} // getJavaParameterListInitialization
	
	//--------------------------------------------

	public String getPreCondition() {
		return _preCondition;
	}
	
	//--------------------------------------------
	
	public String getPostCondition() { 
		return _postCondition;
	}
	
	//--------------------------------------------

	public String getFailureCondition() {
		return _failureCondition;
	}
	
	//--------------------------------------------

	public String getSuccessCondition() {
		return _successCondition;
	}
	
	//--------------------------------------------

	public String getValidCondition() {
		return _validCondition;
	}
	
	//--------------------------------------------

	public String getPreFailureCondition() {
		return _preFailureCondition;
	}
	
	//--------------------------------------------

	public String getOnPreCondition() {
		return _onPreCondition;
	}
	
	//--------------------------------------------

	public String getOnPostCondition() {
		return _onPostCondition;
	}
	
	//--------------------------------------------

	public String getOnPreFailureCondition() {
		return _onPreFailureCondition;
	}
	
	//--------------------------------------------

	public String getOnFailureCondition() {
		return _onFailureCondition;
	}
	
	//--------------------------------------------

	public String getOnSuccessCondition() {
		return _onSuccessCondition;
	}
	
	//--------------------------------------------

	public String getOnValidCondition() {
		return _onValidCondition;
	}
	
	//--------------------------------------------
	//                     Fields
	//--------------------------------------------
	
	protected String _name; 

	protected List<ParsedActionParameter> _parameters;
	
	protected String _preCondition;
	protected String _postCondition;
	protected String _preFailureCondition;
	protected String _failureCondition;
	protected String _successCondition;
	protected String _validCondition;
	
	protected String _onPreCondition;
	protected String _onPostCondition;
	protected String _onPreFailureCondition;
	protected String _onFailureCondition;
	protected String _onSuccessCondition;
	protected String _onValidCondition;
	
} // class ParsedAction
