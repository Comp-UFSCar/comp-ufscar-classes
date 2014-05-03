/* Copyright 2010 Santiago Ontanon and Ashwin Ram */

package gatech.mmpm.tools.parseddomain;

import org.jdom.Element;
import java.util.List;

public class XMLAux {

	/**
	 * Returns the unique child element. It checks if
	 * it is only one element with that name. If no one exists, or more
	 * than one appears, it generates a ParseException.
	 *  
	 * @param node Parent XML element. 
	 * @param childName Child element name.
	 * 
	 * @return Child node with that name.
	 * 
	 * @throws ParseException if the element does not exist or
	 * it is duplicated.
	 */
	public static Element getUniqueChild(Element node,
	                                     String childName) throws ParseException {
		
		List<?> childs = node.getChildren(childName);
		
		if (childs.size() > 1)
			throw new ParseException("Only a <" + childName + "> is valid " +
			                         "for each <" + node.getName() + ">");
		
		if (childs.size() == 0)
			throw new ParseException("<" + childName + "> expected in <" +
			                         node.getName() + ">");
		
		return (Element)childs.get(0);
		
	} // getUniqueChild

		
	/**
	 * Returns the string associated with a child element. It checks if
	 * it is only one element with that name. If no one exists, or more
	 * than one appears, it generates a ParseException.
	 *  
	 * @param node Parent XML element. 
	 * @param childName Child element name.
	 * 
	 * @return String of the child element.
	 * 
	 * @throws ParseException if the element does not exist or
	 * it is duplicated.
	 */
	public static String getUniqueChildText(Element node,
	                                        String childName) throws ParseException {
		

		return getUniqueChild(node, childName).getText().trim();
		
	} // getUniqueChildText
	

	/**
	 * Returns an optional child element. It checks if
	 * it is only one element with that name. If more
	 * than one appears, it generates a ParseException.
	 *  
	 * @param node Parent XML element. 
	 * @param childName Child element name.
	 * 
	 * @return Child element with that name, or null if the
	 * child does not exist.
	 * 
	 * @throws ParseException if the element is duplicated.
	 */
	public static Element getOptionalChild(Element node,
	                                       String childName) throws ParseException {
		
		List<?> childs = node.getChildren(childName);
		
		if (childs.size() > 1)
			throw new ParseException("Only a <" + childName + "> is valid " +
			                         "for each <" + node.getName() + ">");
		
		if (childs.size() == 0)
			return null;

		return (Element)childs.get(0);
		
	} // getOptionalChild
	
	/**
	 * Returns the string associated with a child element. It checks if
	 * it is only one element with that name. If more
	 * than one appears, it generates a ParseException.
	 *  
	 * @param node Parent XML element. 
	 * @param childName Child element name.
	 * 
	 * @return String of the child element, or null if the
	 * child does not exist.
	 * 
	 * @throws ParseException if the element is duplicated.
	 */
	public static String getOptionalChildText(Element node,
	                                        String childName) throws ParseException {

		Element child;
		child = getOptionalChild(node, childName);
		if (child != null) {
			return child.getText().trim();
		}
		else
			return null;

	} // getOptionalChildText

	/**
	 * Returns the string associated with an element attribute.
	 * It checks if the attribute exists, and raises an exception
	 * if it does not.
	 * 
	 * @param node XML element with the attribute.
	 * @param name Attribute name.
	 * 
	 * @return Trim'ed attribute value.
	 * 
	 * @throws ParseException if the attribute does not exist.
	 */
	public static String getRequiredAttribute(Element node,
	                                          String name) throws ParseException {

		String value = node.getAttributeValue(name);
		
		if (value == null)
			throw new ParseException(name + " attribute is missing in " + node.getName());
		else
			return value.trim();
		
	} // getRequiredAttribute

	/**
	 * Returns the string associated with an element attribute.
	 * It just calls the parser, and trims the result.
	 * 
	 * @param node XML element with the attribute.
	 * @param name Attribute name.
	 * 
	 * @return Trim'ed attribute value.
	 */
	public static String getOptionalAttribute(Element node,
	                                          String name) {

		String value = node.getAttributeValue(name);
		
		if (value == null)
			return null;
		else
			return value.trim();
		
	} // getOptionalAttribute
	
} // class XMLAux
