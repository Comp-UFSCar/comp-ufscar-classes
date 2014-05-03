/* Copyright 2010 Santiago Ontanon and Ashwin Ram */

package gatech.mmpm.tools;

import gatech.mmpm.util.Pair;
import jargs.gnu.CmdLineParser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

/**
 * Application that allows game developers to create the IDomain class
 * of their domains.
 * <p>
 * It receives the specification via an XML-file and construct the .java
 * file that should be included on it.
 * 
 * @author Jai Rad, Prafulla Mahindrakar, Santi Onta��n, Marco Antonio G�mez Mart�n
 */
public class EntitiesGenerator {

	ArrayList <String> javaKeyWords = new ArrayList<String>();
	ArrayList <String> javaDataTypes = new ArrayList<String>();

	List<Pair<String,Character>> classNames = new LinkedList<Pair<String,Character>>();


	public void generateClasses(String filename, String dir) 
	{
		//call the methods dataTypeCheckers() and keyWordCheckers() to initialize the ArrayLists!
		dataTypeCheckers();
		keyWordCheckers();

		String disclaimer = "/*********************************************************************************\n";
		disclaimer += "  MACHINE GENERATED CODE. DO NOT EDIT. FIX THE SOURCE XML AND REGENERATE IT AGAIN\n";
		disclaimer += "Organization 					: 				Georgia Institute of Technology\n";
		disclaimer += "												Cognitive Computing Lab (CCL)\n";
		disclaimer += "Authors							: 				Jai Rad\n";
		disclaimer += "												Prafulla Mahindrakar\n";        
		disclaimer += "												Santi Onta��n\n";        
		disclaimer += "												Marco Antonio G�mez Mart�n\n";        
		disclaimer += "****************************************************************************/\n";        

		boolean xmlOk = true;


		try 
		{
			// Build the document with SAX and Xerces, no validation
			SAXBuilder builder = new SAXBuilder();
			// Create the document
			Document doc = builder.build(new File(filename));
			Element root = doc.getRootElement();

			// Get the actions and entities packages.
			String actionPackage = null;
			String entitiesPackage = null;
			
			Element actionSetNode = root.getChild("ActionSet");
			if (actionSetNode != null)
				actionPackage = actionSetNode.getAttributeValue("package");
			if (actionPackage == null) actionPackage = "";
			
			Element entitySetNode = root.getChild("EntitySet");
			if (entitySetNode == null)
				// ��NO ENTITY SET IN THE XML!!
				throw new Exception("No EntitySet specified in the XML file.");
			entitiesPackage = entitySetNode.getAttributeValue("package");
			if (entitiesPackage == null)
				entitiesPackage = "";
			
			//An arrayList that would contain names of all classes (entities)
			//its useful to determine whether the super class was present or not!
			ArrayList <ParseClassInfo> entityStateClasses = new ArrayList<ParseClassInfo>();

			String packageString = "";
			//          String mainEntityClassString = "";
			String entityClassString = "";
			String entityClassName;
			String featureString = "";
			String activeEntityString = "";
			String shortNameString = "";
			String addActions = "";
			String actionString = null;
			String constructor1 = "";
			String constructor2 = "";
			String clone = "";
			String getterMethods = "";
			String setterMethods = "";          	
			String endOfClassString = "}";

			List<Element> entities = root.getChild("EntitySet").getChildren();

			for (Element loopEntity : entities)
			{
				xmlOk = true;
				String superClassName = "";
				Pair<String,Character> classEntry = new Pair<String,Character>(null,null);

				Element entityName = loopEntity.getChild("Name");
				Element entityType = loopEntity.getChild("Type");
				Element superName  = loopEntity.getChild("Super");
				Element shortName  = loopEntity.getChild("ShortName");
				
				//reset the actions!
				addActions = "";
				
				//reset the shrotnameString
				shortNameString = "";
				
				//reset the package string
				packageString = "";
				if (entitiesPackage.length() > 0)
					packageString += "package " + entitiesPackage + ";\n\n";
				
				packageString += "import gatech.mmpm.Entity;\n" +
								 "import gatech.mmpm.PhysicalEntity;\n\n";

				
				//System.out.println("\nparsing entity -> " + entityName.getText());

				classEntry.setFirst(entityName.getText());
				if (shortName!=null) {
					classEntry.setSecond(shortName.getText().charAt(0));
				} else {
					classEntry.setSecond(null);
				}
				classNames.add(classEntry);

				String parseInfo = "PARSING " + entityName.getText() + " :";

				if ( javaKeyWords.contains(entityName.getText()) )
				{
					System.out.println(parseInfo + "Java key-words can't be used as class names!");
					xmlOk = false;
					//continue;
				}
				if ( superName == null )
				{
					entityClassString = "public class " + entityName.getText() + " extends Entity {\n";
					superClassName = "Entity";
					//System.out.println(entityClassString);
				}
				else if ( superName.getText().equals("PhysicalEntity") ) {
					entityClassString = "public class " + entityName.getText() + " extends PhysicalEntity {\n";
					superClassName = "PhysicalEntity";
				} else if ( superName.getText().equals("Entity") ) {
					entityClassString = "public class " + entityName.getText() + " extends Entity {\n";
					superClassName = "Entity";
				} else 
				{
					//Check to see if super class is present or not!
					if ( isPresent(entityStateClasses, superName.getText()) )
						if ( isPresent(entityStateClasses, entityName.getText()) )
						{
							System.out.println(parseInfo + "Class " + entityName.getText() + " already exists!");
							xmlOk = false;
						}
						else
						{
							entityClassString = "public class " + entityName.getText() + " extends " + superName.getText() + " {\n";
							superClassName = superName.getText();
						}

					else
					{
						System.out.println(parseInfo + "Super class " + superName.getText() + " not found!");
						xmlOk = false;
						//continue;
					}
					//System.out.println(entityClassString);
				}


				//This is the entity class name --- file name should be the same
				entityClassName = (entityName.getText()).trim();

				constructor1 = "\tpublic " + entityClassName + "(String a_entityID, String a_owner)\n";
				constructor1 += "\t{\n";
				constructor1 += "\t\tsuper(a_entityID,a_owner);\n";

				constructor2 = "\tpublic " + entityClassName + "( " + entityClassName + " incoming " + ")\n";
				constructor2 = constructor2 + "\t{\n";
				constructor2 = constructor2 + "\t\tsuper(incoming);\n";            	

				clone = "\tpublic Object clone() {\n" +
				"\t\t" + entityClassName + " e = new " + entityClassName +"(this);\n" + 
				"\t\treturn e;\n\t}\n";

				//set these to null, as its a new Entity now
				getterMethods = "";
				setterMethods = "";
				ArrayList <Pair<String,String>> classFeatureList = new ArrayList<Pair<String,String>>();

				Element featureSet = loopEntity.getChild("Features");
				//FeatureSet will have a list of features, so u access them as children

				if ( featureSet == null ) 
					; //Do nothing as if featureSet is null, there are no children, so let the features be equal to null
				else
				{                	
					List<Element> features = featureSet.getChildren();
					featureString = "";
					for( Element f : features)
					{
						//Need to put stuff for cardinality > 1

						String featureName = f.getChild("Name").getText();
						String dataType = (f.getChild("datatype")!=null ? f.getChild("datatype").getText():null);
						String featureValue = "";

						if ( dataType==null) {
							if (isFeatureInTree(superClassName, entityStateClasses, featureName) ) {
								dataType = dataTypeofFeatureInTree(superClassName, entityStateClasses, featureName);
							} 
						}

						if ( f.getChild("defaultValue") == null )
							featureValue = "";
						else
						{
							//No validation is made here with respect to the dataType and the featureValue!
							featureValue = f.getChild("defaultValue").getText();
						}

						if ( javaDataTypes.contains(dataType))
						{
							if ( javaKeyWords.contains(featureName))
							{
								System.out.println(parseInfo + "Java key-words can't be used as feature names --> " + featureName);
								xmlOk = false;
								//continue;
							}
							else
							{
								{
									if (featureValue.equals("")) featureString = featureString + "\tpublic " + dataType + " " + featureName + ";\n";
															else featureString = featureString + "public " + dataType + " " + featureName + " = " + featureValue + ";\n";
									constructor2 = constructor2 + "\t\tthis." + featureName + " = " + "incoming." + featureName + ";\n";

									getterMethods = getterMethods + "\n\tpublic " + dataType + " get" + featureName + "()\n";
									getterMethods = getterMethods + "\t{\n";
									getterMethods = getterMethods + "\t\treturn " + featureName + ";\n";
									getterMethods = getterMethods + "\t}\n";

									setterMethods = setterMethods + "\n\tpublic void set" + featureName + "( " + dataType + " incoming )\n";
									setterMethods = setterMethods + "\t{\n";
									setterMethods = setterMethods + "\t\tthis." + featureName + " = incoming;\n";
									setterMethods = setterMethods + "\t}\n";

									if (!dataType.equals("String")) {
										setterMethods = setterMethods + "\n\tpublic void set" + featureName + "( " + "String" + " incoming )\n";
										setterMethods = setterMethods + "\t{\n";
										String typeCastStr = "(";
										if ( dataType.equals("int")) typeCastStr = "Integer.parseInt(";
										if ( dataType.equals("float")) typeCastStr = "Float.parseFloat(";
										if ( dataType.equals("double")) typeCastStr = "Double.parseDouble(";                							
										if ( dataType.equals("boolean")) typeCastStr = "Boolean.parseBoolean(";                							
										setterMethods = setterMethods + "\t\tthis." + featureName + " = " + typeCastStr + "incoming);\n";
										setterMethods = setterMethods + "\t}\n";
									}
									classFeatureList.add(new Pair<String,String>(featureName,dataType));


								}
//								else
//								{
//									//default values are allowed only for int,double,float,char and boolean! NO STRING!!!
//									//Should these also be PUBLIC?
//									//Need more tests! ---> JAI WORK LEFT
//
//									if ( isFeatureInTree(superClassName, entityStateClasses, featureName) )
//									{
//										//constructor2 = constructor2 + "\t\t" + featureName + " = " + featureValue + ";\n";
//										//constructor1 = constructor1 + "\t\t" + featureName + " = " + featureValue + ";\n";
//									}
//									else
//									{
//										featureString = featureString + "public " + dataType + " " + featureName + " = " + featureValue + ";\n";
//
//										getterMethods = getterMethods + "\n\tpublic " + dataType + " get" + featureName + "()\n";
//										getterMethods = getterMethods + "\t{\n\n";
//										getterMethods = getterMethods + "\t\treturn " + featureName + ";\n\n";
//										getterMethods = getterMethods + "\t}\n";
//
//										setterMethods = setterMethods + "\n\tpublic void set" + featureName + "( " + dataType + " incoming )\n";
//										setterMethods = setterMethods + "\t{\n";
//										setterMethods = setterMethods + "\t\tthis." + featureName + " = incoming;\n";
//										setterMethods = setterMethods + "\t}\n";
//
//										if (!dataType.equals("String")) {
//											setterMethods = setterMethods + "\n\tpublic void set" + featureName + "( " + "String" + " incoming )\n";
//											setterMethods = setterMethods + "\t{\n";
//											String typeCastStr = "(";
//											if ( dataType.equals("int")) typeCastStr = "Integer.parseInt(";
//											if ( dataType.equals("float")) typeCastStr = "Float.parseFloat(";
//											if ( dataType.equals("double")) typeCastStr = "Double.parseDouble(";                							
//											if ( dataType.equals("boolean")) typeCastStr = "Boolean.parseBoolean(";                							
//											setterMethods = setterMethods + "\t\tthis." + featureName + " = " + typeCastStr + "incoming);\n";
//											setterMethods = setterMethods + "\t}\n";
//										}
//
//
//									}
//								}
							}
						}
						else
						{
							System.out.println(parseInfo + "Invalid data type used in the xml file for feature '" + featureName + "' -->" + dataType);
							xmlOk = false;
							//continue;                			
						}
					}   
				}

				Element actionSet = loopEntity.getChild("Actions");
				actionString = null;

				if (actionSet!=null) {
					List<Element> actions = actionSet.getChildren();
					for( Element a : actions) {
						if (a.getName().equals("Action")) {
							if (actionString==null) {
								actionString = "";

								packageString+="import gatech.mmpm.Action;\n";
								//packageString+="import plans." + packageName + ".*;\n\n";
								if (actionPackage.length() > 0)
									packageString += "import " + actionPackage + ".*;\n";
								packageString += "\n";

							}

							actionString+="\t\ta = new " + a.getAttributeValue("name") + "(null,null);\n";
							for(Object o_param:a.getChildren("parameter")) {
								Element param = (Element)o_param;
								actionString+="\t\ta.setParameterValue(\"" + param.getAttributeValue("name") + "\", \"" + param.getAttributeValue("value") + "\");\n";
							}
							actionString+="\t\taddExecutableAction(\"" + classEntry.getFirst() + "\",a);\n\n";
						}
					}
					
					if (actionString!=null) {
						addActions = "\n\n\tpublic static void registerActions() " + "\n\t{\n";
						addActions += "\t\tAction a;\n";
						addActions+=actionString;
						addActions+="\t}\n";
					} else {
						addActions = "";
					}
				}


				constructor2 = constructor2 + "\t}\n";
				constructor1 = constructor1 + "\t}\n";
				featureString = featureString + "\n\n";
				//Single static method defined for returning if the entity is active or not
				//it defaults to "NOT ACTIVE" if Type is not specified
				activeEntityString = "\n\n\tpublic static boolean isActive() " + "\n\t{";
				if ( entityType == null )
					activeEntityString = activeEntityString + "\n\t\t" + "return false;" + "\n" + "\t}\n";
				else
					if ( (entityType.getTextTrim()).equalsIgnoreCase("active") )
						activeEntityString = activeEntityString + "\n\t\t" + "return true;" + "\n" + "\t}\n";
					else     	
						activeEntityString = activeEntityString + "\n\t\t" + "return false;" + "\n" + "\t}\n";

				if (classEntry.getSecond() != null) {
					shortNameString = "\n\n\tpublic static char shortName() " + "\n\t{";
					shortNameString += "\n\t\t" + "return '" + classEntry.getSecond() + "';" + "\n" + "\t}\n";
				}
				
				if ( xmlOk )
				{
					ParseClassInfo classInfo = null;
					//Add the Entity class name to list of OK'ed classes
					classInfo = new ParseClassInfo(entityName.getText(),superClassName);
					classInfo.setFeatures(classFeatureList);
					entityStateClasses.add(classInfo);
					//File name will be entityClassName

					try
					{
						File srcDirectory = new File(dir + java.io.File.separatorChar + entitiesPackage.replace('.', java.io.File.separatorChar));
						
						srcDirectory.mkdirs();
						
						//System.out.println("writing file!");
						File entityFile = new File(srcDirectory, entityClassName + ".java");
						entityFile.createNewFile();
						FileWriter writeEntityFile = new FileWriter(entityFile);
						writeEntityFile.write(disclaimer);
						writeEntityFile.write(packageString + entityClassString);
						writeEntityFile.write(featureString);
						writeEntityFile.write(constructor1);
						writeEntityFile.write(constructor2);
						writeEntityFile.write(clone);
						writeEntityFile.write(activeEntityString);
						writeEntityFile.write(shortNameString);
						writeEntityFile.write(addActions);
						writeEntityFile.write(getterMethods);
						writeEntityFile.write(setterMethods);
						//                        writeEntityFile.write(actionMethods);
						writeEntityFile.write(endOfClassString);
						writeEntityFile.close();
						loopEntity = null;

					}
					catch (IOException e)
					{
						System.out.println("IO EXception!!! " + e);
						e.printStackTrace();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private boolean isPresent(ArrayList<ParseClassInfo> entityStateClasses, String className) 
	{
		int i=0;
		while( i < entityStateClasses.size() )
		{
			if ( entityStateClasses.get(i).className.equals(className))
				return true;
			i++;
		}
		return false;
	}


	boolean isFeatureInTree(String superClass, ArrayList <ParseClassInfo> entityClasses, String feature)
	{
		int i=0;
		ParseClassInfo localClassInfo = null;

		for(i=0; i<entityClasses.size(); i++)
		{
			if(entityClasses.get(i).className.equals(superClass))
			{
				localClassInfo = entityClasses.get(i);
				break;
			}
		}

		if (localClassInfo == null) {
			if (superClass.equals("Entity")) {

			} else if (superClass.equals("PhysicalEntity")) {
				if (feature.equals("x")) return true;
				if (feature.equals("y")) return true;
				if (feature.equals("z")) return true;
				if (feature.equals("width")) return true;
				if (feature.equals("length")) return true;
				if (feature.equals("height")) return true;
			}
			return false;
		}

		if (localClassInfo.hasFeature(feature)!=null)
			return true;
		else
			return isFeatureInTree(localClassInfo.superClassName,entityClasses,feature);

	}

	String dataTypeofFeatureInTree(String superClass, ArrayList <ParseClassInfo> entityClasses, String feature)
	{
		int i=0;
		ParseClassInfo localClassInfo = null;

		for(i=0; i<entityClasses.size(); i++)
		{
			if(entityClasses.get(i).className.equals(superClass))
			{
				localClassInfo = entityClasses.get(i);
				break;
			}
		}

		if (localClassInfo == null) {
			if (superClass.equals("Entity")) {

			} else if (superClass.equals("PhysicalEntity")) {
				if (feature.equals("x")) return "int";
				if (feature.equals("y")) return "int";
				if (feature.equals("z")) return "int";
				if (feature.equals("width")) return "int";
				if (feature.equals("length")) return "int";
				if (feature.equals("height")) return "int";
			}
			return null;
		}

		if (localClassInfo.hasFeature(feature)!=null)
			return localClassInfo.hasFeature(feature).getSecond();
		else
			return dataTypeofFeatureInTree(localClassInfo.superClassName,entityClasses,feature);

	}


	void parseActions( Element actionElement )
	{
		//Each actions has three methods associated with it
		//1. Pre Sensor checker
		//2. Alive Sensor checker
		//3. Success Sensor checker

		String actionName = actionElement.getChildText("name");
		String preCon = actionElement.getChildText("pre");
		String aliveCon = actionElement.getChildText("alive");
		String successCon = actionElement.getChildText("success");

		String actionClassHeadStr = "class " + actionName.trim() + " extends Action {\n";

		String actionClassConstructor = "public " + actionName.trim() + "(" + preCon + "," + aliveCon + "," + successCon + ")";
		actionClassConstructor = actionClassConstructor + "";

		System.out.println(actionClassHeadStr + actionClassConstructor);


	}

	void keyWordCheckers()
	{
		javaKeyWords.add("abstract");
		javaKeyWords.add("continue"); 
		javaKeyWords.add("for");  
		javaKeyWords.add("new");  
		javaKeyWords.add("switch");
		javaKeyWords.add("assert");
		javaKeyWords.add("default"); 
		javaKeyWords.add("goto");
		javaKeyWords.add("package");
		javaKeyWords.add("synchronized");
		javaKeyWords.add("boolean");
		javaKeyWords.add("do");
		javaKeyWords.add("if");
		javaKeyWords.add("private");
		javaKeyWords.add("this");
		javaKeyWords.add("break"); 
		javaKeyWords.add("double"); 
		javaKeyWords.add("implements"); 
		javaKeyWords.add("protected"); 
		javaKeyWords.add("throw");
		javaKeyWords.add("byte"); 
		javaKeyWords.add("else"); 
		javaKeyWords.add("import"); 
		javaKeyWords.add("public"); 
		javaKeyWords.add("throws");
		javaKeyWords.add("case"); 
		javaKeyWords.add("enum");
		javaKeyWords.add("instanceof"); 
		javaKeyWords.add("return"); 
		javaKeyWords.add("transient");
		javaKeyWords.add("catch"); 
		javaKeyWords.add("extends"); 
		javaKeyWords.add("int"); 
		javaKeyWords.add("short"); 
		javaKeyWords.add("try");
		javaKeyWords.add("char"); 
		javaKeyWords.add("final"); 
		javaKeyWords.add("interface"); 
		javaKeyWords.add("static"); 
		javaKeyWords.add("void");
		javaKeyWords.add("class"); 
		javaKeyWords.add("finally"); 
		javaKeyWords.add("long"); 
		javaKeyWords.add("strictfp");
		javaKeyWords.add("volatile");
		javaKeyWords.add("const"); 
		javaKeyWords.add("float"); 
		javaKeyWords.add("native"); 
		javaKeyWords.add("super");
		javaKeyWords.add("while");

	}

	void dataTypeCheckers()
	{
		javaDataTypes.add("boolean");
		javaDataTypes.add("int");
		javaDataTypes.add("float");
		javaDataTypes.add("long");
		javaDataTypes.add("double");
		javaDataTypes.add("char");
		javaDataTypes.add("String");


	}
	
	public static void printUsage() {
		System.out.println("EntitiesGenerator: creates Java code with the class that inherits from gatech.mmpm.IDomain that");
		System.out.println("                 contains the information of the domain according to an XML file.");
		System.out.println();
		System.out.println("Usage: EntitiesGenerator [-d srcDirectory] xmlFile");
		System.out.println();
		System.out.println("\t[-d|--dir]: root directory of the source code where the java files");
		System.out.println("              will be generated.");
		System.out.println("\txmlFile:    file with the domain specification.");
	}
	
	public static void main(String[] args) {

		String xmlFile = null;
		String srcRootDir = null;

		System.out.println("DEPRECATED: this tool generates clases for an ");
		System.out.println("old version of D2 and should not be used.");
		System.out.println();
		System.out.println("Use the MMPM tool instead.");
		
		CmdLineParser parser = new CmdLineParser();
		
		CmdLineParser.Option rootDirOpt = parser.addStringOption('d', "dir");
		CmdLineParser.Option helpOpt = parser.addBooleanOption('h', "help");

        try {
        	parser.parse(args);
        } catch (CmdLineParser.OptionException e) {
        	System.err.println(e.getMessage());
        	printUsage();
        	System.exit(1);
        }

        boolean help = (Boolean)parser.getOptionValue(helpOpt, false);
        if (help) {
        	printUsage();
        	System.exit(0);
        }
        
        srcRootDir = (String)parser.getOptionValue(rootDirOpt, "");
        
        String[] remainingOpts = parser.getRemainingArgs();
        
        if (remainingOpts.length != 1) {
        	// Too many args, or no xmlFile with the domain specs.
        	printUsage();
        	System.exit(1);
        } else
        	xmlFile = remainingOpts[0];

        /// Run de process!
		EntitiesGenerator dg = new EntitiesGenerator();
		
		dg.generateClasses(xmlFile, srcRootDir);
	}
}
