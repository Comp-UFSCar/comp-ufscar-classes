/* Copyright 2010 Santiago Ontanon and Ashwin Ram */

package gatech.mmpm.tools.parseddomain;

/**
 * Class with static methods to check names read from the XML.
 * 
 * @author Pedro Pablo Gomez Martin
 * @date August, 2009
 */
public class NameChecker {

	/**
	 * Check a string and determines if is a valid identifier.
	 * It cannot contain spaces and ilegal characters, neither
	 * be a Java reserve word or class in <tt>java.lang</tt>.
	 * 
	 * @param id Id to be checked.
	 * @return True if it is a valid id.
	 */
	public static boolean isValidIdentifier(String id) {
		
		if (!id.matches("^[a-zA-Z]\\w*$"))
			return false;
		
		if (_javaKeyWords.contains(id))
			return false;
		
		try {
			Class.forName("java.lang." + id);
		} catch (ClassNotFoundException e) {
			// java.lang.<id> does not exist.
			// The id is valid.
			return true;
		}
		return false;
		
	} // isValidIdentifier

	//--------------------------------------------
	
	/**
	 * Check a string and determines if is a valid package
	 * name (package.subpackage. ... ).
	 * 
	 * Each subpackage name must be itself a valid
	 * identifier. It <em>do not</em> check if the package
	 * exists.
	 * 
	 * @param packageName Package name to be checked.
	 * @return True if it is a valid package name.
	 */
	public static boolean isValidPackageName(String packageName) {

		if (!packageName.matches("^[a-zA-Z]\\w*(\\.[a-zA-Z]\\w*)*$"))
			return false;
		String[] packages;
		packages = packageName.split("\\.");
		int i = 0;
		while (i < packages.length && isValidIdentifier(packages[i]))
			++i;

		return (i == packages.length);
		
	} // isValidPackageName

	//--------------------------------------------

	/**
	 * Check if a string represent a valid basic type
	 * (boolean, char, int, long, float or double).
	 * 
	 * @param t Type name to be checked.
	 * @return True if it is a basic data type.
	 * <tt>String</tt> is <em>not</em> a basic data type.
	 */
	public static boolean isValidBasicDataType(String t) {

		return _javaDataTypes.contains(t);

	} // isValidBasicDataType
	
	//--------------------------------------------

	/**
	 * List with the Java reserved word.
	 */
	protected static java.util.List<String> _javaKeyWords;

	/**
	 * List of the basic Java types.
	 */
	protected static java.util.List<String> _javaDataTypes;
	
	static {
		_javaKeyWords = new java.util.ArrayList<String>();
		
		_javaKeyWords.add("abstract");
		_javaKeyWords.add("continue"); 
		_javaKeyWords.add("for");  
		_javaKeyWords.add("new");  
		_javaKeyWords.add("switch");
		_javaKeyWords.add("assert");
		_javaKeyWords.add("default"); 
		_javaKeyWords.add("goto");
		_javaKeyWords.add("package");
		_javaKeyWords.add("synchronized");
		_javaKeyWords.add("boolean");
		_javaKeyWords.add("do");
		_javaKeyWords.add("if");
		_javaKeyWords.add("private");
		_javaKeyWords.add("this");
		_javaKeyWords.add("break"); 
		_javaKeyWords.add("double"); 
		_javaKeyWords.add("implements"); 
		_javaKeyWords.add("protected"); 
		_javaKeyWords.add("throw");
		_javaKeyWords.add("byte"); 
		_javaKeyWords.add("else"); 
		_javaKeyWords.add("import"); 
		_javaKeyWords.add("public"); 
		_javaKeyWords.add("throws");
		_javaKeyWords.add("case"); 
		_javaKeyWords.add("enum");
		_javaKeyWords.add("instanceof"); 
		_javaKeyWords.add("return"); 
		_javaKeyWords.add("transient");
		_javaKeyWords.add("catch"); 
		_javaKeyWords.add("extends"); 
		_javaKeyWords.add("int"); 
		_javaKeyWords.add("short"); 
		_javaKeyWords.add("try");
		_javaKeyWords.add("char"); 
		_javaKeyWords.add("final"); 
		_javaKeyWords.add("interface"); 
		_javaKeyWords.add("static"); 
		_javaKeyWords.add("void");
		_javaKeyWords.add("class"); 
		_javaKeyWords.add("finally"); 
		_javaKeyWords.add("long"); 
		_javaKeyWords.add("strictfp");
		_javaKeyWords.add("volatile");
		_javaKeyWords.add("const"); 
		_javaKeyWords.add("float"); 
		_javaKeyWords.add("native"); 
		_javaKeyWords.add("super");
		_javaKeyWords.add("while");
	}

	static {
		_javaDataTypes = new java.util.ArrayList<String>();
		_javaDataTypes.add("boolean");
		_javaDataTypes.add("char");
		_javaDataTypes.add("int");
		_javaDataTypes.add("float");
		_javaDataTypes.add("long");
		_javaDataTypes.add("double");
	}
	// D2 didn't include short here...

} // NameChecker
