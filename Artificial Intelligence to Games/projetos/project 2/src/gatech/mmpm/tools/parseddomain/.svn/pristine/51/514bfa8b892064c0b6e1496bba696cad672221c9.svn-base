/* Copyright 2010 Santiago Ontanon and Ashwin Ram */

package gatech.mmpm.tools.parseddomain;

import org.jdom.Element;

import java.util.List;

/**
 * Class to parse &lt;SensorSet&gt; and &lt;GoalSet&gt; nodes.
 * They are nearly the same, but because the first one has
 * not returned type (it is assumed to be boolean) and
 * the second one has one.
 * 
 * @author Pedro Pablo Gomez Martin
 * @date August, 2009
 */
public class ParsedMethodSet {

	/**
	 * Constructor. It creates an empty object. The init() method
	 * should be called afterwards.
	 */
	public ParsedMethodSet() {

		_methods = new java.util.LinkedList<ParsedMethod>();

	} // Constructor

	//--------------------------------------------

	/**
	 * Constructor when native domain is provided.
	 */
	public ParsedMethodSet(ParsedMethodSet nativeMethods) {

		_methods = new java.util.LinkedList<ParsedMethod>(nativeMethods._methods);
		
	} // Constructor(nativeEntities)
	
	//--------------------------------------------
	
	/**
	 * Initialize the object using the information of the
	 * XML node. It must be the next format:
	 * 
	 * <pre>
	 * &lt;SensorSet package="package"&gt;
	 *    &lt;Sensor&gt;
	 *       ...
	 *    &lt;/Sensor&gt; 
	 *    ... 
	 * &lt;/SensorSet&gt;
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
			if ((c.getName().compareTo("Sensor") != 0) &&
			    (c.getName().compareTo("Goal") != 0) &&
			    (c.getName().compareTo("WinGoal") != 0)) {
				throw new ParseException("Invalid " + c.getName() + " node in " + node.getName() + " element.");
			}
			// TODO: test if we only received "Sensor" if why are SensorSet and
			// only "Goal" if why are "GoalSet".

			ParsedMethod m = new ParsedMethod();
			m.init(c);
			if (this.getMethod(m.getName()) != null) {
				throw new ParseException("Duplicated method name (" + m.getName() + ")");
			}
			_methods.add(m);
		} // for

	} // init

	//--------------------------------------------

	/**
	 * Once initialized, analyzes the loaded information,
	 * testing if all is correct (for example, if all
	 * methods has code).
	 * 
	 * This step is needed prior all the accessible
	 * information in the class is correctly set.
	 * 
	 * @throw ParseException is any problem occurs.
	 */
	public void process() throws ParseException {

		throw new RuntimeException("TODO: compile code?");
		
	} // process

	//--------------------------------------------

	/**
	 * Once initialized, analyzes the loaded information,
	 * testing if all is correct.
	 * 
	 * This method must be called only for a loaded
	 * native domain.
	 * 
	 * This step is needed prior all the accessible
	 * information in the class is correctly set.
	 * 
	 * @throw ParseException is any problem occurs.
	 */
	public void processAsNativeDomain() throws ParseException {
		
		for (ParsedMethod m:_methods) {
			m._native = true;
		} // for

	} // processAsNativeDomain
	
	//--------------------------------------------

	/**
	 * If it is not set, <tt>null</tt> is returned.
	 */
	public String getPackageName() {
		
		return _packageName;
		
	} // getPackageName
	
	//--------------------------------------------

	/**
	 * Return a method by its name.
	 * 
	 * @param name Searched method name.
	 * @return Method, or null if not found. 
	 */
	public ParsedMethod getMethod(String name) {
		
		for (ParsedMethod m:_methods)
			if (m.getName().equals(name))
				return m;
		return null;
		
	} // getMethod
	
	//--------------------------------------------

	/**
	 * Return all the methods.
	 * 
	 * @return Methods. The list should not be changed.
	 */
	public List<ParsedMethod> getMethods() {

		return _methods;

	}
	
	protected String _packageName;

	protected List<ParsedMethod> _methods;
	
} // ParsedMethodSet
