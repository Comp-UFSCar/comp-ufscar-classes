/* Copyright 2010 Santiago Ontanon and Ashwin Ram */

package gatech.mmpm.tools.parseddomain;

import org.jdom.Element;
import java.util.List;

/**
 * Class to store information about an entity set, as parsed from a
 * domain XML file.
 * 
 * It is used by the generator tool of the domain classes
 * using the XML
 * 
 * @author Pedro Pablo Gomez Martin
 * @date August, 2009
 */
public class ParsedEntitySet {
	
	/**
	 * Constructor. It creates an empty object. The init() method
	 * should be called afterwards.
	 */
	public ParsedEntitySet() {

		_entities = new java.util.LinkedList<ParsedEntity>();

	} // Constructor

	//--------------------------------------------

	/**
	 * Constructor when native domain is provided.
	 */
	public ParsedEntitySet(ParsedEntitySet nativeEntities) {

		_entities = new java.util.LinkedList<ParsedEntity>(nativeEntities._entities);
		
	} // Constructor(nativeEntities)
	
	//--------------------------------------------

	/**
	 * Initialize the object using the information of the
	 * XML node. It must be the next format:
	 * 
	 * <pre>
	 * &lt;EntitySet package="package"&gt;
	 *    &lt;Entity&gt;
	 *       ...
	 *    &lt;/Entity&gt; 
	 *    &lt;Entity&gt;
	 *       ...
	 *    &lt;/Entity&gt;
	 *    ... 
	 * &lt;/EntitySet&gt;
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
			throw new ParseException("Invalid package in EntitySet (" + _packageName + ")");
		
		List<?> children = node.getChildren();

		for (Object o:children) {
			Element c = (Element) o;
			if (c.getName() != "Entity") {
				throw new ParseException("Invalid " + c.getName() + " node. Entity expected.");
			}
			ParsedEntity e = new ParsedEntity();
			e.init(c);
			_entities.add(e);
		} // for

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
	 * @param actionSet Valid actions in this domain.
	 * 
	 * @throw ParseException is any problem occurs.
	 */
	public void process(ParsedActionSet actionSet) throws ParseException {

		String rootEntityName = null;
		for (ParsedEntity e:_entities) {
			if (e._native && (e._superclassName == null)) {
				rootEntityName = e._name;
				break;
			}
		}
		if (rootEntityName == null)
			// Not found???!
			throw new ParseException("Native root entity class not found?!");

		// Test if all the superclasses exist, and link them.
		for (ParsedEntity e:_entities) {
			if (e._native)
				continue;

			String parentName;
			parentName = e.getSuperClassName();
			// null will be valid, and the
			// generator will assume the native root Entity.
			if (parentName == null) {
				parentName = rootEntityName;
				e._superclassName = rootEntityName;
			}

			e._parent = getEntity(parentName);

		} // for
		
		for (ParsedEntity e:_entities) {
			// Let the entities to check their own features.
			e.process(actionSet);
		}
		
		// TODO: additional tests...
		
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
	 * @param actionSet Valid actions in this domain (if any)
	 * 
	 * @throw ParseException is any problem occurs.
	 */
	public void processAsNativeDomain(ParsedActionSet actionSet) throws ParseException {

		boolean rootFound = false;
		
		for (ParsedEntity e:_entities) {
			e._native = true;
			String parentName;
			parentName = e.getSuperClassName();
			// null will be valid, and the
			// generator will assume its the root entity.
			if (parentName == null) {
				if (rootFound)
					throw new ParseException("Two root entities founded in native domain!");
				rootFound = true;
			}
			else { 
				ParsedEntity parentEntity;
				parentEntity = getEntity(parentName);
				if (parentEntity == null)
					throw new ParseException("Entity superclass (" + parentName +
					                         ") not found for " + e.getName() +
					                         " in native domain.");
				else
					e._parent = parentEntity;
			}
		} // for

		for (ParsedEntity e:_entities) {
			// Let the entities to check their own features.
			e.process(actionSet);
		}

	} // processAsNativeDomain

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
	 * Return an entity by its name.
	 * 
	 * @param name Searched entity name.
	 * @return Entity, or null if not found. 
	 */
	public ParsedEntity getEntity(String name) {
		
		for (ParsedEntity e:_entities)
			if (e.getName().equals(name))
				return e;
		return null;
		
	} // getEntity
	
	//--------------------------------------------

	/**
	 * Return all the entities.
	 * 
	 * @return Entities. The list should not be changed.
	 */
	public List<ParsedEntity> getEntities() {

		return _entities;

	}
	
	protected String _packageName;

	protected List<ParsedEntity> _entities;
	
} // class ParsedEntitySet
