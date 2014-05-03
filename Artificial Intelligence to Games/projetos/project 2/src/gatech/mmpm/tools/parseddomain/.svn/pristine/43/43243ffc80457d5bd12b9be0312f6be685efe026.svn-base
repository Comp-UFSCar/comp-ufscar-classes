/* Copyright 2010 Santiago Ontanon and Ashwin Ram */

package gatech.mmpm.tools.parseddomain;

import org.jdom.Element;
import java.util.List;

/**
 * Class to store information about an action set, as parsed from a
 * domain XML file.
 * 
 * It is used by the generator tool of the domain classes
 * using the XML
 * 
 * @author Pedro Pablo Gomez Martin
 * @date August, 2009
 */
public class ParsedActionSet {
	
	/**
	 * Constructor. It creates an empty object. The init() method
	 * should be called afterwards.
	 */
	public ParsedActionSet() {

		_actions = new java.util.LinkedList<ParsedAction>();

	} // Constructor

	//--------------------------------------------

	/**
	 * Initialize the object using the information of the
	 * XML node. It must be the next format:
	 * 
	 * <pre>
	 * &lt;ActionSet package="package"&gt;
	 *    &lt;Action&gt;
	 *       ...
	 *    &lt;/Action&gt; 
	 *    &lt;Action&gt;
	 *       ...
	 *    &lt;/Action&gt;
	 *    ... 
	 * &lt;/ActionSet&gt;
	 * </pre>
	 *
	 * @param node Source XML node. 
	 * 
	 * @throws ParseException if some error occurs.
	 * 
	 * @see ParsedAction
	 */
	public void init(Element node) throws ParseException {

		_packageName = XMLAux.getOptionalAttribute(node, "package");

		if ((_packageName != null) &&
		     !NameChecker.isValidPackageName(_packageName))
			throw new ParseException("Invalid package in ActionSet (" + _packageName + ")");
		
		List<?> children = node.getChildren();

		for (Object o:children) {
			Element c = (Element) o;
			if (c.getName().compareTo("Action") != 0) {
				throw new ParseException("Invalid " + c.getName() + " node. Action expected.");
			}

			ParsedAction a = new ParsedAction();
			a.init(c);
			_actions.add(a);
		} // for

	} // init

	//--------------------------------------------

	public String getJavaPackageDeclaration() {

		if (_packageName != null)
			return "package " + _packageName + ";";
		else
			return "";

	} // getJavaPackageDeclaration

	//--------------------------------------------

	/**
	 * If it is not set, <tt>null</tt> is returned.
	 */
	public String getPackageName() {
		
		return _packageName;
		
	} // getPackageName
	
	//--------------------------------------------

	/**
	 * Return an action by its name.
	 * 
	 * @param name Searched action name.
	 * @return Action, or null if not found. 
	 */
	public ParsedAction getAction(String name) {
		
		for (ParsedAction a:_actions)
			if (a.getName().equals(name))
				return a;
		return null;
		
	} // getAction
	
	//--------------------------------------------

	/**
	 * Once initialized, analyzes the loaded information,
	 * testing if all is correct (for example, if all
	 * the action parameters have a type).
	 * 
	 * This step is needed prior all the accessible
	 * information in the class is correctly set.
	 * 
	 * @param actionSet Valid actions in this domain.
	 * 
	 * @throw ParseException is any problem occurs.
	 */
	public void process() throws ParseException {

		for (ParsedAction a:_actions) {
			for (ParsedActionParameter p:a.getParameters()) {
				if (p.getType() == null)
					throw new ParseException("Type expected in action " + a.getName());
				if (p.getValue() != null)
					throw new ParseException("Default value in action " + a.getName() + " not supported.");
			}
		} // for actions

	} // process

	//--------------------------------------------

	/**
	 * Return all the actions.
	 * 
	 * @return Actions. The list should not be changed.
	 */
	public List<ParsedAction> getAction() {

		return _actions;

	}
	
	protected String _packageName;

	protected List<ParsedAction> _actions;
	
} // class ParsedActionSet

