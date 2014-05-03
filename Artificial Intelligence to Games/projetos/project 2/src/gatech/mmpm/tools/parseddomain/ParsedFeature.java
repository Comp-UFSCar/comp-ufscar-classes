/* Copyright 2010 Santiago Ontanon and Ashwin Ram */

package gatech.mmpm.tools.parseddomain;

import org.jdom.Element;

/**
 * Class to store information about an entity feature, as parsed from a
 * domain XML file.
 * 
 * It is used by the generator tool of the domain classes
 * using the XML
 * 
 * @author Pedro Pablo Gomez Martin
 * @date August, 2009
 * 
 * @note This class is uncompleted, until the complete XML specification
 * is done (what is cardinality?)
 */
public class ParsedFeature {

	/**
	 * Constructor. It creates an empty object. The init() method
	 * should be called afterwards.
	 */
	public ParsedFeature() {
	} // constructor

	//--------------------------------------------

	/**
	 * Initialize the object using the information of the
	 * XML node. It must be the next format:
	 * 
	 * <pre>
	 * &lt;Feature&gt;
	 *    &lt;Name&gt;FeatureName&lt;/Name&gt; 
	 *    &lt;DataType&gt;Datatype&lt;/DataType&gt; 
	 *    &lt;Cardinality&gt;Datatype&lt;/Cardinality&gt; 
	 *    &lt;DefaultValue&gt;Datatype&lt;/DefaultValue&gt; 
	 * &lt;/Feature&gt;
	 * </pre>
	 * 
	 * Valid <tt>Datatype</tt> values are:
	 * 
	 *   - <tt>boolean</tt>
	 *   - <tt>char</tt>
	 *   - <tt>int</tt>
	 *   - <tt>long</tt>
	 *   - <tt>float</tt>
	 *   - <tt>double</tt>
	 *   - <tt>String</tt>
	 *   
	 * I'm not sure if this has sense... I would remove
	 * long and double (or int and float) because we are
	 * supposed to be working in a platform independent way
	 * with the XML, so it is no-sense to think about
	 * different data lengths...
	 * 
	 * <tt>DefaultValue</tt> is optional and should be of
	 * the specified type.
	 * 
	 * <tt>DataType</tt> is also optional. If not provided,
	 * a superclass of the entity which this feature is defined
	 * should have the same feature (it is been defined
	 * just for specify a new DefaultValue).
	 * 
	 * Some things are not tested, for example if
	 * features are overwritten from superentities (in order
	 * to avoid it with different types, or to check if
	 * a feature with a datatype missing is really inherited).
	 * 
	 * @param node Source XML node. 
	 * 
	 * @throws ParseException if some error occurs.
	 */
	public void init(Element node) throws ParseException {

		if (node.getName().compareToIgnoreCase("Feature") != 0)
			throw new ParseException("Feature node expected");
		
		_name = XMLAux.getUniqueChildText(node, "Name");
		if (!NameChecker.isValidIdentifier(_name))
			throw new ParseException("Invalid Feature name (" + _name + ")");

		_datatype = XMLAux.getOptionalChildText(node, "DataType");
		if ((_datatype != null) &&
		    !NameChecker.isValidBasicDataType(_datatype) &&
		    (_datatype.compareToIgnoreCase("String") != 0)) {
			throw new ParseException("Invalid feature type (" + _datatype +")");
		}

		_defaultValue = XMLAux.getOptionalChildText(node, "DefaultValue");

		// TODO: cardinality!!!!
		// I have no idea about the aim of this child,
		// so I don't read it.
		
	} // init

	//--------------------------------------------
	
	public String getName() {
		return _name;
	}
	
	//--------------------------------------------

	public String getDatatype() {
		return _datatype;
	}
	
	//--------------------------------------------
	
	// null if no specified.
	public String getDefaultValue() {
		return _defaultValue;
	}
	
	//--------------------------------------------

	public boolean isInherited() {
		
		return _inherited;

	}
	
	//--------------------------------------------

	/**
	 * Once initialized, analyzes the loaded information,
	 * testing if all is correct.
	 * 
	 * This step is needed prior all the accessible
	 * information in the class is correctly set.
	 * 
	 * This method must be called once owner
	 * ParsedEntity.process() has been called (and is,
	 * in fact, invoked at the end of that method).
	 * 
	 * @throw ParseException is any problem occurs.
	 */
	public void process() throws ParseException {
		
		if ((_datatype.compareToIgnoreCase("boolean") != 0) &&
		    (_datatype.compareToIgnoreCase("char") != 0) &&
		    (_datatype.compareToIgnoreCase("int") != 0) &&
		    (_datatype.compareToIgnoreCase("long") != 0) &&
		    (_datatype.compareToIgnoreCase("float") != 0) &&
		    (_datatype.compareToIgnoreCase("double") != 0) &&
		    (_datatype.compareToIgnoreCase("String") != 0))
			throw new ParseException("Invalid datatype in " + _name + "feature (" +
			                         _datatype + ")");

		// Convert the datatype to the correct Java case.
		if (_datatype.compareToIgnoreCase("String") != 0)
			_datatype = _datatype.toLowerCase();
		else
			_datatype = "String";
				
		// TODO: cardinality?

		if (_defaultValue != null) {
			// TODO: test if the value fits the datatype.
		}
	} // process
	
	//--------------------------------------------

	protected String _name;
	
	protected String _datatype;

	protected String _defaultValue;
	
	protected boolean _inherited = false;
	// Set externally by ParsedEntity::process().
	// If true, it is not used in clone()
	// or to set/get methods.
	
} // class ParsedFeature
