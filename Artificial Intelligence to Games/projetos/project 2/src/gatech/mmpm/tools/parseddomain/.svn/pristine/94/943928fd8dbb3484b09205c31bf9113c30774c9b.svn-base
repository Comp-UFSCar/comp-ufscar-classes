/* Copyright 2010 Santiago Ontanon and Ashwin Ram */

package gatech.mmpm.tools.parseddomain;

import org.jdom.Element;

/**
 * Class to store information about the domain, as parsed from a
 * domain XML file.
 * 
 * It is used by the generator tool of the domain classes
 * using the XML
 * 
 * @author Pedro Pablo Gomez Martin
 * @date August, 2009
 */
public class ParsedDomain {

	/**
	 * Constructor. It creates an empty object. The init() method
	 * should be called afterwards.
	 */
	public ParsedDomain() {
	} // Constructor

	//--------------------------------------------

	/**
	 * Initialize the object using the information of the
	 * XML node. It must be the next format:
	 * 
	 * <pre>
	 * &lt;Domain name="domainName" classname=".." package=".."&gt;
	 *    &lt;SensorSet&gt;
	 *       ...
	 *    &lt;/SensorSet&gt; 
	 *    &lt;GoalSet&gt;
	 *       ...
	 *    &lt;/GoalSet&gt; 
	 *    &lt;ActionSet&gt;
	 *       ...
	 *    &lt;/ActionSet&gt; 
	 *    &lt;EntitySet&gt;
	 *       ...
	 *    &lt;/EntitySet&gt; 
	 * &lt;/Domain&gt;
	 * </pre>
	 *
	 * @param node Source XML node.
	 * @param ParsedDomain nativeDomain Domain with the native (MMPM)
	 * provided domain. If null, it is assumed we are init the
	 * native domain itself.
	 * 
	 * @throws ParseException if some error occurs.
	 * @see ParsedMethodSet, ParsedActionSet, ParsedEntitySet
	 */
	public void init(Element node, ParsedDomain nativeDomain) throws ParseException {
		
		if (node.getName().compareToIgnoreCase("Domain") != 0)
			throw new ParseException("Domain node expected");


		if (nativeDomain != null) {
			// Native domain does not need a name neither a class
			_name = node.getAttributeValue("name");
			
			if (_name == null)
				throw new ParseException("Missing name attribute in Domain node.");
			else
				_name = _name.trim();
	
			if (!NameChecker.isValidIdentifier(_name))
				throw new ParseException("Invalid Domain name (" + _name + ")");

			_className = node.getAttributeValue("classname");
			
			if (_className == null)
				throw new ParseException("Missing classname attribute in Domain node.");
			else
				_className = _className.trim();

			if (!NameChecker.isValidIdentifier(_className))
				throw new ParseException("Invalid Domain classname (" + _name + ")");
		}

		_packageName = node.getAttributeValue("package");
		
		if (_packageName != null)
			_packageName = _packageName.trim();

		if (!NameChecker.isValidPackageName(_packageName))
			throw new ParseException("Invalid package in Domain (" + _packageName + ")");

		// Sensor set.
		if (nativeDomain == null)
			_sensorSet = new ParsedMethodSet();
		else
			_sensorSet = new ParsedMethodSet(nativeDomain.getSensorSet());
		
		// Nodes.
		Element child;

		child = XMLAux.getOptionalChild(node, "SensorSet");
		if (child != null) {
			_sensorSet.init(child);
		}

		// Native domain has no goals.
		_goalSet = new ParsedMethodSet();
		child = XMLAux.getOptionalChild(node, "GoalSet");
		if (child != null) {
			_goalSet.init(child);
		}
		
		child = XMLAux.getOptionalChild(node, "ActionSet");
		_actionSet = new ParsedActionSet();
		if (child != null)
			_actionSet.init(child);

		child = XMLAux.getUniqueChild(node, "EntitySet");
		if (nativeDomain == null)
			_entitySet = new ParsedEntitySet();
		else
			_entitySet = new ParsedEntitySet(nativeDomain.getEntitySet());
		_entitySet.init(child);

	} // init

	//--------------------------------------------

	/**
	 * Once initialized, analyzes the loaded information,
	 * testing if all is correct (for example, if all
	 * entity parent classes exist) and builds relationships
	 * between objects. This is something similar to the
	 * link step in Java Classes when loading into the
	 * JVM.
	 * 
	 * This step is needed prior all the accessible
	 * information in the class is correctly set.
	 * 
	 * @throw ParseException is any problem occurs.
	 */
	public void process() throws ParseException {
		
		_entitySet.process(_actionSet);
		
	} // process 
	
	//--------------------------------------------
	
	/**
	 * Once initialized, analyzes the loaded information,
	 * testing if all is correct (for example, if all
	 * entity parent classes exist) and builds relationships
	 * between objects. This is something similar to the
	 * link step in Java Classes when loading into the
	 * JVM.
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
		
		_entitySet.processAsNativeDomain(_actionSet);
		
		_sensorSet.processAsNativeDomain();
		
		_goalSet.processAsNativeDomain();
		// Native domain has no goals, but this doesn't hurt.
		
	} // processAsNativeDomain
	
	//--------------------------------------------
	
	public String getName() {
		
		return _name;
		
	} // getName
	
	//--------------------------------------------

	/**
	 * If it is not set, <tt>null</tt> is returned.
	 */
	public String getPackageName() {
		
		return _packageName;
		
	} // getPackageName

	//--------------------------------------------

	public String getClassName() {
		
		return _className;
		
	} // getClassName
	
	//--------------------------------------------

	public ParsedEntitySet getEntitySet() {

		return _entitySet;

	} // getEntitySet
	
	//--------------------------------------------

	public ParsedActionSet getActionSet() {
		
		return _actionSet;
		
	} // getActionSet
	
	//--------------------------------------------
	
	public ParsedMethodSet getSensorSet() {
		
		return _sensorSet;
		
	} // getMethodSet

	//--------------------------------------------
	
	public ParsedMethodSet getGoalSet() {
		
		return _goalSet;
		
	} // getGoalSet
	
	//--------------------------------------------
	//               Protected fields
	//--------------------------------------------

	protected String _name; 

	protected String _packageName;

	protected String _className;

	protected ParsedActionSet _actionSet;

	protected ParsedEntitySet _entitySet;

	protected ParsedMethodSet _sensorSet;
	
	protected ParsedMethodSet _goalSet;

} // ParsedDomain 
