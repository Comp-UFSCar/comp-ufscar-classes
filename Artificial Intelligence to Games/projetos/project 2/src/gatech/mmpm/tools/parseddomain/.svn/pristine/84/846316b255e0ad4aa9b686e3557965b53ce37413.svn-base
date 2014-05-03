/* Copyright 2010 Santiago Ontanon and Ashwin Ram */

package gatech.mmpm.tools.parseddomain;

import org.jdom.Element;
import java.util.List;


/**
 * Class to store information about an entity, as parsed from a
 * domain XML file.
 * 
 * It is used by the generator tool of the domain classes
 * using the XML
 * 
 * @author Pedro Pablo Gomez Martin
 * @date August, 2009
 * 
 * @note This class is uncompleted, until the complete XML specification
 * is done (actions). "Type" element (active|passive) is deprecated and
 * will be ignored. <tt>isActive()</tt> method will return <tt>true</tt>
 * if actions were provided.
 * 
 * @note S2Domain.xml has an &lt;Anims&gt; child element that is not
 * parsed (what is that?).
 */
public class ParsedEntity {

	/**
	 * Constructor. It creates an empty object. The init() method
	 * should be called afterwards.
	 */
	public ParsedEntity() {
	} // constructor

	//--------------------------------------------

	/**
	 * Initialize the object using the information of the
	 * XML node. It must be the next format:
	 * 
	 * <pre>
	 * &lt;Entity&gt;
	 *    &lt;Name&gt;EntityName&lt;/Name&gt; 
	 *    &lt;Super&gt;Datatype&lt;/Super&gt; 
	 *    &lt;ShortName&gt;Datatype&lt;/ShortName&gt; 
	 *    &lt;Features&gt;
	 *       &lt;Feature&gt;
	 *          ...
	 *       &lt;/Feature&gt;
	 *       &lt;Feature&gt;
	 *          ...
	 *       &lt;/Feature&gt;
	 *       ...
	 *    &lt;/Features&gt; 
	 *    &lt;Actions&gt; 
	 *       &lt;Action&gt;
	 *          ...
	 *       &lt;/Action&gt;
	 *       &lt;Action&gt;
	 *          ...
	 *       &lt;/Action&gt;
	 *       ...
	 *    &lt;/Actions&gt; 
	 * &lt;/Entity&gt;
	 * </pre>
	 * 
	 * 
	 * It does not test some things (for example if
	 * the superclass exists, or if the entity name is
	 * duplicated).
	 * 
	 * @param node Source XML node. 
	 * 
	 * @throws ParseException if some error occurs.
	 * 
	 * @see ParsedFeature
	 * @see ParsedAction
	 */
	public void init(Element node) throws ParseException {
		
		if (node.getName().compareToIgnoreCase("Entity") != 0)
			throw new ParseException("Entity node expected");
		
		_name = XMLAux.getUniqueChildText(node, "Name");
		
		if (!NameChecker.isValidIdentifier(_name))
			throw new ParseException("Invalid Action name (" + _name + ")");
		
		_superclassName = XMLAux.getOptionalChildText(node, "Super");
		if ((_superclassName != null) &&
		     !NameChecker.isValidIdentifier(_superclassName))
			throw new ParseException("Invalid Feature superclass " +
			                         "in " + _name + "(" + _superclassName + ")");

		String readShortName;
		readShortName = XMLAux.getOptionalChildText(node, "ShortName");
		if (readShortName == null)
			_shortName = '\0';
		else {
			if (readShortName.length() != 1) {
				throw new ParseException("Invalid shortname for " + _name + " entity.");
			}
			_shortName = readShortName.charAt(0);
		}

		String entityType;
		entityType = XMLAux.getOptionalChildText(node, "Type");
		if (entityType != null) {
			System.err.println("Deprecated use of Type element in "
			                   + _name + " entity. It will be ignored.");
			// Now entity type is decided depending whether the
			// entity has or not actions.
		}
		
		Element featuresElement;
		featuresElement = XMLAux.getUniqueChild(node, "Features");
		_features = new java.util.ArrayList<ParsedFeature>();
		if (featuresElement != null) {
			
			List<?> children = featuresElement.getChildren();

			for (Object o:children) {
				Element c = (Element) o;
				if (c.getName() != "Feature") {
					throw new ParseException("Invalid " + c.getName() + " node in <Features>.");
				}
				ParsedFeature f = new ParsedFeature();
				f.init(c);
				if (getDeclaredFeature(f.getName()) != null)
					// Repeated name.
					throw new ParseException("Repeated feature name in " + _name);
				_features.add(f);
			} // for
			
		} // if (featuresElement != null)
		
		Element actionsElement;
		actionsElement = XMLAux.getOptionalChild(node, "Actions");
		_actions = new java.util.ArrayList<ParsedAction>();
		if (actionsElement != null) {
			
			List<?> children = actionsElement.getChildren();

			for (Object o:children) {
				Element c = (Element) o;
				if (c.getName() != "Action") {
					throw new ParseException("Invalid " + c.getName() + " node in <Actions>.");
				}
				ParsedAction a = new ParsedAction();
				a.init(c);
				// We will not test if an action appears twice, because
				// it could in fact do it (with different parameters).
/*				if (getDeclaredAction(a.getName()) != null)
					// Repeated name.
					throw new ParseException("Repeated action name in " + _name);*/
				_actions.add(a);
			} // for
			
		} // if (featuresElement != null)
		
	} // init
	
	//--------------------------------------------
	
	public String getName() {
		
		return _name;
		
	} // getName
	
	//--------------------------------------------

	public String getSuperClassName() {
		
		return _superclassName;
		
	} // getSuperClassName
	
	//--------------------------------------------

	public char getShortName() {
		
		return _shortName;
		
	} // getShortName
	
	//--------------------------------------------
	
	/**
	 * Return a feature by its name. It only looks
	 * in the features declared in the XML of this entity.
	 * Keep in mind that inherit features could be
	 * provided anyway, if the XML redefines a feature
	 * to set a new default value.
	 * 
	 * @name Searched feature name.
	 * @return Feature, or null if not found. 
	 */
	public ParsedFeature getDeclaredFeature(String name) {
		
		for (ParsedFeature f:_features)
			if (f.getName().equals(name))
				return f;
		return null;
		
	} // getDeclaredFeature
	
	//--------------------------------------------
	
	/**
	 * Return a feature by its name. It looks for it
	 * in the current entity and in its parents.
	 * This method should only be called once the
	 * _parent attribute has been externally set.
	 * 
	 * @name Searched feature name.
	 * @return Feature, or null if not found. 
	 */
	public ParsedFeature getFeature(String name) {

		ParsedFeature result;
		result = getDeclaredFeature(name);
		if (result != null)
			return result;
		else if (_parent != null)
			return _parent.getFeature(name);
		else
			return null;
		
	} // getFeature
	
	//--------------------------------------------

	/**
	 * Return all the features defined for this entity
	 * (without including the inherited features).
	 * 
	 * @return Features. The list should not be changed.
	 */
	public List<ParsedFeature> getFeatures() {

		return _features;

	}

	//--------------------------------------------

	/**
	 * Return an action by its name. It only looks
	 * in the actions declared in the XML of this entity.
	 * 
	 * @name Searched action name.
	 * @return Action, or null if not found. 
	 */
	public ParsedAction getDeclaredAction(String name) {
		
		for (ParsedAction a:_actions)
			if (a.getName().equals(name))
				return a;
		return null;
		
	} // getDeclaredAction
	
	//--------------------------------------------

	/**
	 * Return an action by its name. It looks for it
	 * in the current entity and in its parents.
	 * This method should only be called once the
	 * _parent attribute has been externally set.
	 * 
	 * @name Searched action name.
	 * @return Action, or null if not found. 
	 */
	public ParsedAction getAction(String name) {

		ParsedAction result;
		result = getDeclaredAction(name);
		if (result != null)
			return result;
		else if (_parent != null)
			return _parent.getAction(name);
		else
			return null;
		
	} // getAction
	
	//--------------------------------------------

	/**
	 * Return all the actions defined for this entity
	 * (without including the inherited ones).
	 * 
	 * @return Actions. The list should not be changed.
	 */
	public List<ParsedAction> getActions() {

		return _actions;

	}

	//--------------------------------------------
	
	public boolean isNative() {
		return _native;
	}

	//--------------------------------------------
	
	/**
	 * Once initialized, analyzes the loaded information,
	 * testing if all is correct (for example, if features
	 * are correctly overwritten).
	 * 
	 * This step is needed prior all the accessible
	 * information in the class is correctly set.
	 * 
	 * This method must be called once _parent has been
	 * externally set.
	 * 
	 * @param actionSet Valid actions in this domain.
	 * 
	 * @throw ParseException is any problem occurs.
	 */
	public void process(ParsedActionSet actionSet) throws ParseException {
		if (_hasBeenProcessed)
			return;
		
		if (_parent != null)
			_parent.process(actionSet);
		
		for (ParsedFeature f:_features) {
			String fetType = f.getDatatype();
			if (_parent != null) {
				ParsedFeature parentFeature;
				parentFeature = _parent.getFeature(f.getName());
				if (parentFeature != null) {
					// We are overwritting f.
					f._inherited = true;
					String parentFetType;
					parentFetType = parentFeature.getDatatype();
					// TODO: cardinality?!
					if (fetType == null) {
						// We didn't know the type.
						fetType = parentFetType;
						f._datatype = fetType;
					}
					else if (fetType.compareToIgnoreCase(parentFetType) != 0) {
						// Types are different!
						throw new ParseException("Type mistmatch in inherited feature (" +
						                         f.getName() + " in " + this.getName() + ")");
					}
					if (f.getDefaultValue() == null) {
						// The only reason to overwrite a feature is
						// to provide a new default value.
						throw new ParseException("Missing default value for inherited feature (" +
						                         f.getName() + " in " + this.getName() + ")");
					}
				} // if (parentFeature != null)
			} // if (_parent != null)
			// Now we test the feature is valid (this has sense if _parent == null
			// or if the feature was not inherited).
			if (f.getDatatype() == null) {
				throw new ParseException("Missing datatype for feature (" +
				                          f.getName() + " in " + this.getName() + ")");
			}
			f.process();
		} // for

		for (ParsedAction a:_actions) {
			ParsedAction actionDefinition;
			actionDefinition = actionSet.getAction(a.getName());
			if (actionDefinition == null) {
				throw new ParseException(a.getName() + " undefined for " + getName() + " entity.");
			}
			for (ParsedActionParameter p:a.getParameters()) {
				ParsedActionParameter parameterDefinition;
				parameterDefinition = actionDefinition.getParameter(p.getName());
				if (parameterDefinition == null) {
					throw new ParseException("Undefined '" + p.getName() + "' parameter in '" +
					                         a.getName() + "' action, used in '" + getName() + "' entity.");
				}
				if ((p.getType() != null) && (p.getType() != parameterDefinition.getType()))
					// In fact, type should not be provided...
					throw new ParseException("Type mismatch in '" +
					                         p.getName() + "' parameter for action '" +
					                         a.getName() + "' in entity '" + this.getName() + "'");
				// For simplicity in the future, we will set the type.
				p._type = parameterDefinition._type;
				// TODO: is p._value valid for p._type?
			}
		} // for actions

		_hasBeenProcessed = true;

	} // process
	
	//--------------------------------------------
	
	/**
	 * Returns true if this entity or any of each
	 * superentities has associated actions  
	 */
	public boolean isActive() {
		
		if ((_actions != null) && (_actions.size() > 0))
			return true;
		if (_parent != null)
			return _parent.isActive();
		else
			return false;
		
	} // isActive
	
	//--------------------------------------------

	// process should have been called!
	public String getFeaturesJavaDeclaration() {
		
		// For each not inherited feature, returns
		//     protected <datatype> _<name>;
		StringBuffer sb = new StringBuffer();
		for (ParsedFeature f:_features) {
			if (!f.isInherited()) {
				sb.append("\tprotected ");
				sb.append(f.getDatatype());
				sb.append(" _");
				sb.append(f.getName());
				sb.append(";\n\n");
			}
		} // for

		return sb.toString();
		
	} // getFeaturesJavaDeclaration
	
	//--------------------------------------------
	
	// process should have been called!
	public String getJavaCopyDeclaredFeatures() {

		// For each not inherited feature, returns
		//     _<name> = rhs._<name>;
		StringBuffer sb = new StringBuffer();
		for (ParsedFeature f:_features) {
			if (!f.isInherited()) {
				sb.append("\t\t_");
				sb.append(f.getName());
				sb.append(" = rhs._");
				sb.append(f.getName());
				sb.append(";\n");
			}
		} // for

		return sb.toString();
		
	} // getJavaCopyDeclaredFeatures
	
	//--------------------------------------------
	
	// process should have been called!
	public String getJavaFeaturesDefaultValuesAssignments() {

		// For each feature with a default value, returns
		//     _<name> = <default value>;
		StringBuffer sb = new StringBuffer();
		for (ParsedFeature f:_features) {
			if (f.getDefaultValue() != null) {
				sb.append("\t\t_");
				sb.append(f.getName());
				sb.append(" = ");
				if (f.getDatatype().compareToIgnoreCase("String") != 0)
					sb.append(f.getDefaultValue());
				else {
					sb.append("\"");
					// TODO! SCAPE CHARACTERES!!
					// If the property is a string, care should be
					// taken to substitute things like \ to \\ here
					// to generate valid Java code!!!
					sb.append(f.getDefaultValue());
					sb.append("\"");
				}
				sb.append(";\n");
			}
		} // for

		return sb.toString();
		
	} // getJavaFeaturesDefaultValuesAssignments
	
	//--------------------------------------------
	
	// process should have been called!
	public String getJavaFeatureListInitialization() {

		// For each non-inherited feature, returns
		//     _listOfFeatures.add("<feature name>");
		StringBuffer sb = new StringBuffer();
		for (ParsedFeature f:_features) {
			if (f.isInherited())
				continue;
			sb.append("\t\t_listOfFeatures.add(\"");
			sb.append(f.getName());
			sb.append("\");\n");
		} // for

		return sb.toString();
		
	} // getJavaFeatureListInitialization
	
	//--------------------------------------------
	
	protected String _name; 
	
	protected String _superclassName;

	protected char _shortName;

	protected List<ParsedFeature> _features;

	protected List<ParsedAction> _actions;

	protected ParsedEntity _parent;
	// Set externally by ParsedEntitySet::process()

	protected boolean _hasBeenProcessed = false;
	
	protected boolean _native = false;
	// Set externally by ParsedEntitySet::processAsNativeDomain().
}
