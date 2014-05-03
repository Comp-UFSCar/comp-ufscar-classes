/* Copyright 2010 Santiago Ontanon and Ashwin Ram */

package gatech.mmpm.tools;

import jargs.gnu.CmdLineParser;

import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import gatech.mmpm.tools.templates.TextTemplate;

import gatech.mmpm.tools.expressionparser.*;
import gatech.mmpm.tools.parseddomain.*;
import gatech.mmpm.tools.compiler.Builtin2JavaPackages;
import gatech.mmpm.tools.compiler.Builtin2Java;

import gatech.mmpm.ActionParameterType;

/**
 * Application that allows game developers to create the different
 * domain classes (IDomain, subclasses of Entity, etc.) of their
 * domains.
 * <p>
 * It receives the specification via an XML-file and construct the .java
 * files that should be included on the games and dynamically in the
 * AI engines.
 * 
 * This class can be used as a stand alone application to generate
 * Java code, or programatically using its methods (specially
 * parseDomainElement() ) from other Java applications.
 * 
 * @author Pedro Pablo Gomez Martin, based in some previous work of
 * Marco Antonio Gomez Martin, Jai Rad, Prafulla Mahindrakar and
 * Santi Onta�on.
 * 
 * @date August, 2009
 */
public class DomainGenerator {

	/**
	 * Main program. You can consult the valid args executing
	 * the program or consulting the printUsage() code.
	 * 
	 * @param args Program parameters.
	 *
	 * @note This method just calls to mainWrapper and
	 * returns it result to the underlying operative system.
	 */
	public static void main(String[] args) {

        System.exit(realMain(args));
        
	} // main

	//--------------------------------------------------------

	public DomainGenerator() {
		
		_generatedClassesNames = new LinkedList<String>();
		
		_generatedCode = new LinkedList<String>();
		
	} // constructor

	//--------------------------------------------------------

	// TODO: comentar!
	// It fills the Java code attributes.
	public boolean parseDomainElement(Element domainNode) {

		// If the name is changed, you should update the
		// main comment of the class.
		
		ParsedDomain parsedDomain;
		ParsedDomain nativeDomain;
		
		nativeDomain = getNativeDomain();
		if (nativeDomain == null)
			return false;
		
		parsedDomain = new ParsedDomain();
		try {
			parsedDomain.init(domainNode, nativeDomain);
			parsedDomain.process();
		}
		catch (ParseException e) {
			System.err.println(e.getMessage());
			return false;
		}
		
		// TODO: decide this depending on the parameters?
		// TODO: ���action base class name!!!
		generateEntitiesCode(parsedDomain, nativeDomain.getEntitySet().getPackageName(), "gatech.mmpm.Action");
		generateActionsCode(parsedDomain);
		generateMethodsCode(parsedDomain);
		generateIDomain(parsedDomain, domainNode);
		
		return true;

	} // parseDomainElement

	//--------------------------------------------------------
	//                   Protected methods
	//--------------------------------------------------------

	/**
	 * Builds a new ParsedDomain using the specification of the
	 * "native domain" (classes provided by MMPM) found in
	 * gatech.mmpm.tools.nativedomain.NativeDomain.xml. This
	 * file uses the same specification than the third-party
	 * game domains.
	 */
	protected ParsedDomain getNativeDomain() {

		ParsedDomain result;
		java.io.InputStream input = null;
		java.net.URI u = null;
		try {
			// Try in the path of this class
			u = getClass().getResource("/gatech/mmpm/tools/nativedomain/NativeDomain.xml").toURI();
			input = new java.io.FileInputStream(new java.io.File(u));
		} catch (Exception ex) {
			System.err.println("Native domain specification could not be founded (in " + u +")");
			return null;
		}

		SAXBuilder builder = new SAXBuilder();
		Document document;
		try {
			document = builder.build(input);
			Element root = document.getRootElement(); // <Domain>
			if (root == null) {
				System.err.println("Native domain has no root element (in " + u +")");
				return null;
			}

			result = new ParsedDomain();
			result.init(root, null);
			result.processAsNativeDomain();

			return result;

		} catch (Exception e) {
			System.err.println("Native domain could not be parsed (from " + u +")");
			e.printStackTrace();
			return null;
		}

	} // getNativeDomain
	
	//--------------------------------------------------------

	/**
	 * Parses the XML input file, and fill the object attributes
	 * with the resulting information.
	 *  
	 * @param is Input stream where the XML can be reached.
	 * 
	 * @return True if the XML could be parsed. 
	 */
	protected boolean parseXML(java.io.InputStream is) {

		SAXBuilder builder = new SAXBuilder();
		Document document;
		try {
			document = builder.build(is);
			Element root = document.getRootElement(); // <Domain>
			if (root == null)
				return false;

			// TODO: parameters to choose which classes
			// should be generated?
			return parseDomainElement(root);
			
		} catch (JDOMException e) {
			e.printStackTrace();
			return false;
		} catch (java.io.IOException e) {
			e.printStackTrace();
			return false;
		}

	} // parseXML
	
	//--------------------------------------------------------

	protected boolean generateEntitiesCode(ParsedDomain domain,
	                                       String nativeEntityPackageName,
	                                       String nativeActionCompleteName) {

		List<ParsedEntity> entities;
		TextTemplate template;
		TextTemplate headerTemplate;
		TextTemplate getterAndSetterTemplate;
		TextTemplate stringSetterTemplate; // void set(String rhs)
		TextTemplate genericGetter;
		TextTemplate genericSetter;

		headerTemplate = getTemplate("Header");
		if (headerTemplate == null)
			return false;
		template = getTemplate("Entity.java");
		if (template == null)
			return false;
		getterAndSetterTemplate = getTemplate("EntityFeatureSetAndGet");
		if (getterAndSetterTemplate == null)
			return false;
		stringSetterTemplate = getTemplate("EntityFeatureSetString");
		if (stringSetterTemplate == null)
			return false;
		genericGetter = getTemplate("EntityFeatureGenericGetter");
		if (genericGetter == null)
			return false;
		genericSetter = getTemplate("EntityFeatureGenericSetter");
		if (genericSetter == null)
			return false;
		
		String entitiesPackage;
		String actionsPackage;
		ParsedEntitySet entitySet;
		entitiesPackage = domain.getEntitySet().getPackageName();
		entitySet = domain.getEntitySet();

		actionsPackage = domain.getActionSet().getPackageName();
		
		entities = entitySet.getEntities();
		
		for (ParsedEntity e:entities) {
			if (e.isNative())
				continue;
			
			String entityClassName;
			String fullEntityClassName;
			String entitySuperclassName;
			List<ParsedAction> actions = e.getActions();
			String actionsImport;

			entityClassName = e.getName();
			if (entitiesPackage != null)
				fullEntityClassName = entitiesPackage + "." + entityClassName;
			else
				fullEntityClassName = entityClassName;

			if (entitySet.getEntity(e.getSuperClassName()).isNative())
				entitySuperclassName = nativeEntityPackageName + "." + e.getSuperClassName();
			else 
				entitySuperclassName = /*entitiesPackage + "." + */ e.getSuperClassName();
			
			template.reset();
			template.replace("<<<Disclaimer>>>", headerTemplate.getResult());
			
			template.replace("<<<optional package name>>>", entitySet.getJavaPackageDeclaration());			

			template.replace("<<<ActionBaseClass>>>", nativeActionCompleteName);
			
			template.replace("<<<entityName>>>", entityClassName);
			template.replace("<<<superclass>>>", entitySuperclassName);
			
			// TODO: to ActionSet
			if (actions.size() == 0) {
				actionsImport = "";
			}
			else {
				// We will create a Set with all the actions used in
				// this entity. The set is needed to remove repeated
				// actions (with different binded parameters).
				java.util.Set<String> actionsNamesSet;
				actionsNamesSet = new java.util.TreeSet<String>();
				for (ParsedAction a:actions) {
					actionsNamesSet.add(a.getName());
				} // for actions
				
				StringBuffer sbActionsImport = new StringBuffer();
				String initImport;
				if (actionsPackage.length() != 0)
					initImport = "import " + actionsPackage + ".";
				else
					initImport = "import ";
				
				for (String an:actionsNamesSet) {
					sbActionsImport.append(initImport);
					sbActionsImport.append(an);
					sbActionsImport.append(";\n");
				} // for actions
				actionsImport = sbActionsImport.toString();
			} // if-else actions.size() == 0
			
			

			template.replace("<<<optional import actions>>>", actionsImport);
			
//			template.replace("<<<entityName>>>", entityClassName);
			// It was done previously...
			
			template.replace("<<<Declared fields>>>", e.getFeaturesJavaDeclaration());

			template.replace("<<<copy declared features>>>", e.getJavaCopyDeclaredFeatures());
			template.replace("<<<default values assignments>>>", e.getJavaFeaturesDefaultValuesAssignments());

			String shortNameStr;
			if (e.getShortName() == '\0')
				shortNameStr = "\\0";
			else
				shortNameStr = "" + e.getShortName();
			template.replace("<<<shortName>>>", shortNameStr);
			
			StringBuffer sbGetterAndSetter = new StringBuffer();
			StringBuffer sbGenericGetter = new StringBuffer();
			StringBuffer sbGenericSetter = new StringBuffer();

			for (ParsedFeature f:e.getFeatures()) {
				if (f.isInherited())
					continue;

				getterAndSetterTemplate.replace("<<<featureName>>>", f.getName());
				getterAndSetterTemplate.replace("<<<dataType>>>", f.getDatatype());
				String casedFeatureName;
				casedFeatureName = ("" + f.getName().charAt(0)).toUpperCase() +
				                   f.getName().substring(1);
				getterAndSetterTemplate.replace("<<<casedFeatureName>>>", casedFeatureName);
				sbGetterAndSetter.append(getterAndSetterTemplate.getResult());
				getterAndSetterTemplate.reset();

				if (f.getDatatype().compareTo("String") != 0) {
					stringSetterTemplate.replace("<<<casedFeatureName>>>", casedFeatureName);
					stringSetterTemplate.replace("<<<featureName>>>", f.getName());
					String parser;
					String postParser = ")";
					if (f.getDatatype().compareToIgnoreCase("boolean") == 0)
						parser = "Boolean.parseBoolean(";
					else if (f.getDatatype().compareToIgnoreCase("char") == 0) {
						parser = "";
						postParser = ".charAt(0)";
					}
					else if (f.getDatatype().compareToIgnoreCase("int") == 0)
						parser = "Integer.parseInt(";
					else if (f.getDatatype().compareToIgnoreCase("long") == 0)
						parser = "Long.parseLong(";
					else if (f.getDatatype().compareToIgnoreCase("float") == 0)
						parser = "Float.parseFloat(";
					else if (f.getDatatype().compareToIgnoreCase("double") == 0)
						parser = "Double.parseDouble(";
					else {
						System.err.println("Unknown datatype!? (" + f.getDatatype() + ")");
						return false;
					}
					stringSetterTemplate.replace("<<<parser>>>", parser);
					stringSetterTemplate.replace("<<<postParser>>>", postParser);
					
					sbGetterAndSetter.append(stringSetterTemplate.getResult());
					stringSetterTemplate.reset();
				} // if datatype != "String"
				
				// generic getter
				genericGetter.replace("<<<featureName>>>", f.getName());
				genericGetter.replace("<<<casedFeatureName>>>", casedFeatureName);
				sbGenericGetter.append(genericGetter.getResult());
				genericGetter.reset();

				// generic setter
				genericSetter.replace("<<<featureName>>>", f.getName());
				genericSetter.replace("<<<casedFeatureName>>>", casedFeatureName);
				sbGenericSetter.append(genericSetter.getResult());
				genericSetter.reset();

			}
			
			template.replace("<<<getters and setters>>>", sbGetterAndSetter.toString()); 

			template.replace("<<<initialize list of features>>>", e.getJavaFeatureListInitialization());

			template.replace("<<<genericGetter>>>", sbGenericGetter.toString());
			
			template.replace("<<<genericSetter>>>", sbGenericSetter.toString());

			template.replace("<<<isActive>>>", e.isActive()?"true":"false");

			StringBuffer sbActions = new StringBuffer();
			if ((actions != null) && actions.size() > 0)
				sbActions.append("\t\tAction a;\n");
			for (ParsedAction a:actions) {
				sbActions.append("\t\ta = new " + a.getName() + "(null, null);\n");
				for (ParsedActionParameter p:a.getParameters()) {
					sbActions.append("\t\ta.setParameterValue(\""+p.getName() + "\", \"" + p.getValue() + "\");\n");
				}
				sbActions.append("\t\t_listOfActions.add(a);\n\n");
			} // for actions
			
			template.replace("<<<initialize list of actions>>>", sbActions.toString());

			_generatedClassesNames.add(fullEntityClassName);
			_generatedCode.add(template.getResult());

		} // for entities

		return true;
		
	} // generateEntitiesCode
	
	//--------------------------------------------------------

	protected boolean generateActionsCode(ParsedDomain domain) {

		List<ParsedAction> actions;
		TextTemplate template;
		TextTemplate headerTemplate;
		TextTemplate getterAndSetterTemplate;
		TextTemplate stringGetterTemplate; // String getStringName()
		TextTemplate stringSetterTemplate; // void set(String rhs)
		TextTemplate genericGetter;
		TextTemplate genericStringGetter;
		TextTemplate genericSetter;
		TextTemplate getContext;
		TextTemplate onCondition;

		headerTemplate = getTemplate("Header");
		if (headerTemplate == null)
			return false;
		template = getTemplate("Action.java");
		if (template == null)
			return false;
		getterAndSetterTemplate = getTemplate("ActionParameterSetAndGet");
		if (getterAndSetterTemplate == null)
			return false;
		stringSetterTemplate = getTemplate("ActionParameterSetString");
		if (stringSetterTemplate == null)
			return false;
		stringGetterTemplate = getTemplate("ActionParameterGetString");
		if (stringSetterTemplate == null)
			return false;
		genericGetter = getTemplate("ActionParameterGenericGetter");
		if (genericGetter == null)
			return false;
		genericStringGetter = getTemplate("ActionParameterGenericStringGetter");
		if (genericStringGetter == null)
			return false;
		genericSetter = getTemplate("ActionParameterGenericSetter");
		if (genericSetter == null)
			return false;
		getContext = getTemplate("ActionGetContext");
		if (getContext == null)
			return false;
		onCondition = getTemplate("ActionOnCondition");
		if (onCondition == null)
			return false;

		if (getContext == null)
			return false;

		String actionsPackage;
		ParsedActionSet actionSet;
		
		actionSet = domain.getActionSet();
		
		actionsPackage = actionSet.getPackageName();
		
		actions = actionSet.getAction();
		
		SymbolTable symbolTable = getSymbolTable(domain.getSensorSet(),
                domain.getGoalSet());

		for (ParsedAction a:actions) {
			
			String actionClassName;
			String fullActionClassName;

			actionClassName = a.getName();
			if (actionsPackage != null)
				fullActionClassName = actionsPackage + "." + actionClassName;
			else
				fullActionClassName = actionClassName;

			
			template.reset();
			template.replace("<<<Disclaimer>>>", headerTemplate.getResult());
			
			template.replace("<<<optional package name>>>", actionSet.getJavaPackageDeclaration());			

			template.replace("<<<actionName>>>", actionClassName);
			
			template.replace("<<<Declared fields>>>", a.getParametersJavaDeclaration());

			template.replace("<<<copy declared parameters>>>", a.getJavaCopyDeclaredParameters());

			StringBuffer sbGetterAndSetter = new StringBuffer();
			StringBuffer sbGenericGetter = new StringBuffer();
			StringBuffer sbGenericStringGetter = new StringBuffer();
			StringBuffer sbGenericSetter = new StringBuffer();
			StringBuffer sbGetContext = new StringBuffer();

			for (ParsedActionParameter p:a.getParameters()) {

				getterAndSetterTemplate.replace("<<<parameterName>>>", p.getName());
				getterAndSetterTemplate.replace("<<<dataType>>>", p.getType().getJavaTypeDeclaration());
				String casedParameterName;
				casedParameterName = ("" + p.getName().charAt(0)).toUpperCase() +
				                      p.getName().substring(1);
				getterAndSetterTemplate.replace("<<<casedParameterName>>>", casedParameterName);
				sbGetterAndSetter.append(getterAndSetterTemplate.getResult());
				getterAndSetterTemplate.reset();

				if (p.getType().getJavaTypeDeclaration().compareTo("String") != 0) {

					stringGetterTemplate.replace("<<<casedParameterName>>>", casedParameterName);
					stringGetterTemplate.replace("<<<parameterName>>>", p.getName());
					stringGetterTemplate.replace("<<<parameterType>>>", p.getType().toString());
					
					stringSetterTemplate.replace("<<<casedParameterName>>>", casedParameterName);
					stringSetterTemplate.replace("<<<parameterName>>>", p.getName());
					
					stringSetterTemplate.replace("<<<parameterTypeClass>>>", 
					                             p.getType().getJavaTypeDeclaration());
					stringSetterTemplate.replace("<<<parameterType>>>", p.getType().toString());
					
					sbGetterAndSetter.append(stringGetterTemplate.getResult());
					stringGetterTemplate.reset();
					
					sbGetterAndSetter.append(stringSetterTemplate.getResult());
					stringSetterTemplate.reset();
					
				} // if datatype != "String"

				// generic getter
				genericGetter.replace("<<<parameterName>>>", p.getName());
				genericGetter.replace("<<<casedParameterName>>>", casedParameterName);
				sbGenericGetter.append(genericGetter.getResult());
				genericGetter.reset();
				
				// generic string getter
				genericStringGetter.replace("<<<parameterName>>>", p.getName());
				genericStringGetter.replace("<<<casedParameterName>>>", casedParameterName);
				// If the current parameter is a String, then the
				// method to get the parameter value as string has the
				// common getter name of get<Name>().
				// But if the parameter has any other value, then
				// the getter get<Name>() will return the native type
				// and the getString<Name>() method should be used. 
				if (p.getType().getJavaTypeDeclaration().compareTo("String") != 0)
					genericStringGetter.replace("<<<optionalString>>>", "String");
				else 
					genericStringGetter.replace("<<<optionalString>>>", "");

				sbGenericStringGetter.append(genericStringGetter.getResult());
				genericStringGetter.reset();

				// generic setter
				genericSetter.replace("<<<parameterName>>>", p.getName());
				genericSetter.replace("<<<casedParameterName>>>", casedParameterName);
				sbGenericSetter.append(genericSetter.getResult());
				genericSetter.reset();

				// getContext
				getContext.replace("<<<parameterName>>>", p.getName());
				sbGetContext.append(getContext.getResult());
				getContext.reset();
				
			} // for each action parameter
			
			template.replace("<<<getters and setters>>>", sbGetterAndSetter.toString()); 

			template.replace("<<<initialize list of parameters>>>", a.getJavaParameterListInitialization());

			template.replace("<<<genericGetter>>>", sbGenericGetter.toString());
			
			template.replace("<<<genericSetter>>>", sbGenericSetter.toString());

			template.replace("<<<genericStringGetter>>>", sbGenericStringGetter.toString());
			
			template.replace("<<<getContext>>>", sbGetContext.toString());
			
			
			// Conditions.
			Builtin2JavaPackages packages;
			packages = new Builtin2JavaPackages(domain);
			ExpressionNode expression;

			// We must add to the symbol table the parameters
			SymbolTable currentSymbolTable;
			currentSymbolTable = symbolTable.clone();
			for (ParsedActionParameter p:a.getParameters()) {
				if (symbolTable.getEntry(p.getName()) != null) {
					System.err.println("Invalid (duplicated) parameter name '" +
					                   p.getName() + "' in method '" +
					                   a.getName() + "'.");
					return false;
				}
				SymbolTableEntry e;
				
				e = new SymbolTableEntry(SymbolTable.EntryType.PARAMETER,
				                         p.getName(), p.getType());
				currentSymbolTable.addEntry(e);
			} // for (parameters)

			if (!generateConditionCode(a.getPreCondition(), "<<<preCondition>>>",
			                           "gatech.mmpm.Action._preCondition",
			                           currentSymbolTable, packages, template,domain))
				continue;

			if (!generateConditionCode(a.getSuccessCondition(), "<<<successCondition>>>",
                    "gatech.mmpm.Action._successCondition",
                    currentSymbolTable, packages, template,domain))
				continue;
			
			if (!generateConditionCode(a.getPreFailureCondition(), "<<<preFailureCondition>>>",
                    "gatech.mmpm.Action._preFailureCondition",
                    currentSymbolTable, packages, template,domain))
				continue;

			if (!generateConditionCode(a.getFailureCondition(), "<<<failureCondition>>>",
                    "gatech.mmpm.Action._failureCondition",
                    currentSymbolTable, packages, template,domain))
				continue;
			
			if (!generateConditionCode(a.getValidCondition(), "<<<validCondition>>>",
                    "gatech.mmpm.Action._validCondition",
                    currentSymbolTable, packages, template,domain))
				continue;
			
			if (!generateConditionCode(a.getPostCondition(), "<<<postCondition>>>",
                    "gatech.mmpm.Action._postCondition",
                    currentSymbolTable, packages, template,domain))
				continue;

			// -----------------
			// OnXXXXCondition
			// -----------------
			
			String onActionCode;
			onActionCode = generateOnConditionCode(a, a.getOnPreCondition(), "Pre",
			                                       currentSymbolTable, packages,
			                                       onCondition, domain);
			if (onActionCode == null)
				continue;
			template.replace("<<<onPreCondition>>>", onActionCode);

			onActionCode = generateOnConditionCode(a, a.getOnPostCondition(), "Post", 
			                                       currentSymbolTable, packages,
			                                       onCondition, domain);
			if (onActionCode == null)
				continue;
			template.replace("<<<onPostCondition>>>", onActionCode);

			onActionCode = generateOnConditionCode(a, a.getOnPreFailureCondition(), "PreFailure",
			                                       currentSymbolTable, packages,
			                                       onCondition, domain);
			if (onActionCode == null)
				continue;
			template.replace("<<<onPreFailureCondition>>>", onActionCode);

			onActionCode = generateOnConditionCode(a, a.getOnFailureCondition(), "Failure", 
			                                       currentSymbolTable, packages,
			                                       onCondition, domain);
			if (onActionCode == null)
				continue;
			template.replace("<<<onFailureCondition>>>", onActionCode);

			onActionCode = generateOnConditionCode(a, a.getOnSuccessCondition(), "Success", 
			                                       currentSymbolTable, packages,
			                                       onCondition, domain);
			if (onActionCode == null)
				continue;
			template.replace("<<<onSuccessCondition>>>", onActionCode);

			onActionCode = generateOnConditionCode(a, a.getOnValidCondition(), "Valid",
			                                       currentSymbolTable, packages,
			                                       onCondition, domain);
			if (onActionCode == null)
				continue;
			template.replace("<<<onValidCondition>>>", onActionCode);

			template.replace("<<<optional sensor imports>>>", packages.getImportLines());

			_generatedClassesNames.add(fullActionClassName);
			_generatedCode.add(template.getResult());

		} // for actions

		return true;
		
	} // generateActionsCode
			
	//--------------------------------------------------------

	/**
	 * Generate the java classes for the sensors and goals of
	 * the domain.
	 * 
	 * @param domain Domain which sensors and goals must be
	 * generated.
	 */
	protected boolean generateMethodsCode(ParsedDomain domain) {

		// Template initialization
		TextTemplate template;
		TextTemplate headerTemplate;
		TextTemplate backendTemplate;

		headerTemplate = getTemplate("Header");
		if (headerTemplate == null)
			return false;
		template = getTemplate("Sensor.java");
		if (template == null)
			return false;
		backendTemplate = getTemplate("SensorBackEnd.java");

		SymbolTable symbolTable = getSymbolTable(domain.getSensorSet(),
		                                         domain.getGoalSet());


		if (!generateMethodsCode(domain, domain.getSensorSet(), symbolTable,
		                         template, headerTemplate, backendTemplate))
			return false;

		if (!generateMethodsCode(domain, domain.getGoalSet(), symbolTable,
		                         template, headerTemplate, backendTemplate))
			return false;

		return true;
		
	} // generateMethodsCode

	//--------------------------------------------------------

	protected boolean generateIDomain(ParsedDomain domain, Element domainNode) {

		// We will use the tool developed by Marco Antonio
		// using XSLT.
		
		// The tool to generate the IDomain subclass for the
		// current game uses XSLT and the JRE XML classes.
		// We must provide it the XML with the domain, but we have
		// processed it yet using jdom.
		// As far as I know, the solution is to serialize
		// back the jdom Element to XML, and provide it
		// to the XSLT tool.
		org.jdom.output.XMLOutputter xmlWriter;
		xmlWriter = new org.jdom.output.XMLOutputter();
		java.io.StringWriter outputString;
		outputString = new java.io.StringWriter();
		try {
			xmlWriter.output(domainNode, outputString);
		}
		catch (java.io.IOException e) {
			System.err.println(e.getMessage());
			return false;
		}
		
		IDomainGenerator dg = new IDomainGenerator();
		java.io.StringReader stringReader;
		stringReader = new java.io.StringReader(outputString.toString());
		
		java.io.StringWriter stringWriter;
		stringWriter = new java.io.StringWriter();
		
		
		dg.run(stringReader, stringWriter);
		
		String completeClassName;
		if (domain.getPackageName().isEmpty())
			completeClassName = domain.getClassName();
		else
			completeClassName = domain.getPackageName() + "." + domain.getClassName();

		_generatedClassesNames.add(completeClassName);
		_generatedCode.add(stringWriter.toString());

		return true;

	} // generateIDomain
			
	//--------------------------------------------------------

	protected ExpressionNode getBuiltInExpression(ParsedMethod m,
			SymbolTable symbolTable) {
		
		// We must add to the symbol table the parameters
		SymbolTable currentSymbolTable;
		currentSymbolTable = symbolTable.clone();
		for (ParsedActionParameter p:m.getParameters()) {
			if (symbolTable.getEntry(p.getName()) != null) {
				System.err.println("Invalid (duplicated) parameter name '" +
				                   p.getName() + "' in method '" +
				                   m.getName() + "'.");
				return null;
			}
			SymbolTableEntry e;
			
			e = new SymbolTableEntry(SymbolTable.EntryType.PARAMETER,
			                         p.getName(), p.getType());
			currentSymbolTable.addEntry(e);
		} // for (parameters)

		ExpressionNode expression;

		expression = ExpressionParser.parse(m.getCode(), currentSymbolTable);

		return expression;	
	
	} // getBuiltInExpression
	
	//--------------------------------------------------------

	/**
	 * Generate the java classes for the sensors and goals of
	 * the domain.
	 * 
	 * @param domain Domain which sensors and goals must be
	 * generated.
	 */
	protected boolean generateMethodsCode(ParsedDomain domain,
                                          ParsedMethodSet methods,
	                                      SymbolTable symbolTable,
	                                      TextTemplate template,
	                                      TextTemplate headerTemplate,
	                                      TextTemplate backendTemplate) {

		String methodsPackage;
		methodsPackage = methods.getPackageName();

		for (ParsedMethod m:methods.getMethods()) {

			if (m.isNative())
				continue;

			template.reset();
			template.replace("<<<Disclaimer>>>", headerTemplate.getResult());

			String methodClassName;
			String fullMethodClassName;
			
			methodClassName = m.getName();
			if (methodsPackage != null) {
				fullMethodClassName = methodsPackage + "." + methodClassName;
			}
			else
				fullMethodClassName = methodClassName;

			if (methodsPackage != null) {
				template.replace("<<<optional package name>>>",
				                 "package " + methodsPackage + ";");
			}
			else {
				template.replace("<<<optional package name>>>", "");
			}

			template.replace("<<<sensorName>>>", methodClassName);

			template.replace("<<<SensorType>>>", m.getReturnedType().toString());
			
			if (m.getLanguage() != null) {
				if (m.getLanguage().compareToIgnoreCase("java") == 0) {
					template.replace("<<<optional backend sensor>>>", "");
					template.replace("<<<evaluate code>>>", m.getCode());
					template.replace("<<<optional sensor imports>>>", "");
				}
				else {
					throw new RuntimeException("Invalid language (" + m.getLanguage() + ")");
				}
			} // if (m.getLanguage() != null)
			else {
				// We have a sensor specified with the
				// built-in language.
				ExpressionNode expression;
				expression = getBuiltInExpression(m, symbolTable);
				
				backendTemplate.reset();
				backendTemplate.replace("<<<backendSensor>>>", Builtin2Java.compile(expression, domain));
				template.replace("<<<optional backend sensor>>>", backendTemplate.getResult());
				
				template.replace("<<<evaluate code>>>", "return _backend.evaluate(cycle, gs, player, parameters);");

				Builtin2JavaPackages b2jp;
				b2jp = new Builtin2JavaPackages(domain);
				expression.accept(b2jp);
				template.replace("<<<optional sensor imports>>>", b2jp.getImportLines());
			} // if-else (m.getLanguage() != null)
			
			String param = "";
			Iterator<ParsedActionParameter> it = m.getParameters().iterator();
			ParsedActionParameter pap;
			while(it.hasNext()) {
				pap = it.next();
				param = param + 
				"_listOfNeededParameters.add(new Pair<String,ActionParameterType>(\"" +
				pap.getName() + "\", ActionParameterType." + pap.getType() + "));\n\t\t";
			}
			template.replace("<<<needed parameters>>>", param);

			// TODO: internalEquivalents!!!!!!!!

			_generatedClassesNames.add(fullMethodClassName);
			_generatedCode.add(template.getResult());

		} // for (methods)

		return true;

	} // generateMethodsCode

	//--------------------------------------------------------

	/**
	 * Very specific method used in generateActionsCode() to avoid 
	 * some code repetition. This method is invoked to
	 * generate the conditions code.
	 */
	protected boolean generateConditionCode(String strCondition,
	                                        String mark, String defaultValue,
	                                        SymbolTable currentSymbolTable, Builtin2JavaPackages packages,
	                                        TextTemplate template, ParsedDomain domain) {

		ExpressionNode expression;
		if (strCondition != null) {
			try {
				expression = ExpressionParser.parse(strCondition, currentSymbolTable);
			}
			catch (Exception e) {
				System.err.println("Error parsing condition expression: " + strCondition);
				return false;
			}
			expression.accept(packages);
			template.replace(mark, Builtin2Java.compile(expression, domain));
		}
		else {
			// Action has the same condition than the
			// parent.
			template.replace(mark, defaultValue);
		}
		return true;

	}
	
	//--------------------------------------------------------

	/**
	 * Very specific method used in generateActionsCode() to avoid 
	 * some code repetition. This method is invoked to
	 * generate the onXXXCondition code.
	 */
	protected String generateOnConditionCode(ParsedAction a, String strCondition, String condType,
	                                         SymbolTable currentSymbolTable, Builtin2JavaPackages packages,
	                                         TextTemplate template, ParsedDomain domain) {

		ExpressionNode expression;

		if (strCondition != null) {
			try {
				expression = ExpressionParser.parse(strCondition, currentSymbolTable);
			}
			catch (Exception e) {
				e.printStackTrace(); // TODO: remove this!
				return null;
			}
			template.reset();
			template.replace("<<<CondType>>>", condType);
			if (!(expression instanceof AssignmentNode)) {
				System.err.println("Non-assignment expression in on " + condType + "Condition " + strCondition + "\n");
				return null;
			}
			AssignmentNode assignment;
			assignment = (AssignmentNode) expression;
			
			template.replace("<<<targetValue>>>", assignment.getIdentifier());
			template.replace("<<<Value>>>", Builtin2Java.compile(assignment.getRHS(), domain));
			
			String identifierType;
			ParsedActionParameter identifier;
			identifier = a.getParameter(assignment.getIdentifier());
			if (identifier == null) {
				System.err.println(assignment.getIdentifier() + " is not a valid parameter for " + a.getName() + " action.");
				return null;
			}
			identifierType = identifier.getType().getJavaTypeDeclaration();
			template.replace("<<<IdentifierType>>>", identifierType);			
			
			assignment.getRHS().accept(packages);
			return template.getResult();
		}
		else {
			return "";
		}
		
	} // generateOnConditionCode
	
	/**
	 * Save all the files in the object attributes to files
	 * in the specified directory.
	 * 
	 * @param path Path of the output files.
	 * @throws java.io.IOException if some thing was wrong.
	 */
	protected void saveClassesToFiles(String path) throws java.io.IOException {
		
		java.util.Iterator<String> classNameIt, codeIt;
	
		classNameIt = _generatedClassesNames.iterator();
		codeIt = _generatedCode.iterator();

		while (classNameIt.hasNext() && codeIt.hasNext()) {
			String className = classNameIt.next();
			String code = codeIt.next();
			
			// className is in the form pack1.subpack2.classname
			// and we must convert it to pack1/subpack2/classname.java.
			String fileName = className.replace('.', java.io.File.separatorChar);

			if (path.endsWith("/") || path.endsWith("\\")) {
				fileName = path + fileName + ".java";
			}
			else
				fileName = path + java.io.File.separator + fileName + ".java";

			// Create the directories if they don't exist.
			java.io.File f = new java.io.File(fileName).getParentFile();
			f.mkdirs();
			
			// Now, we will create the file. Exceptions will not be
			// managed (just raised up).
			java.io.FileWriter fileWriter;
			fileWriter = new java.io.FileWriter(fileName);
			fileWriter.write(code);
			fileWriter.close();

		}
		
		if (classNameIt.hasNext() || codeIt.hasNext())
			// This should not happen!
			throw new RuntimeException("Internal error in DomainGenerator");

	} // saveToFiles
	
	//--------------------------------------------------------
	
	public static void printUsage() {
		// TODO:
		// programa -o <ruta salida> [fichero.xml]
		// -e entidades? -d dominio? -a acciones? -s sensores?...
		/*
		System.out.println("Trainer: uses a learning engine to train (create) a ME.");
		System.out.println();
		System.out.println("Usage: Learner -t trainerClass -g gameDomainClass -D attr=value ...");
		System.out.println();
		System.out.println("\t-t|--trainer: specify the Java class that contains the");
		System.out.println("\t              ME trainer. It must implements the ");
		System.out.println("\t              gatech.mmpm.learningengine.IMETrainer interface.");
		System.out.println();
		System.out.println("\t-g|--game:    specify the java class that contains the information");
		System.out.println("\t              about the game. It must implements the");
		System.out.println("\t              gatech.mmpm.IDomain interface.");
		System.out.println("\t-D name=value: specify an option to be sent to the trainer.");
		System.out.println("\t              It's up to it to understand this attr, value)");
		System.out.println("\t              pair.");
		System.out.println("\t-i|--input file:  specify the file name where traces will be read of.");
		System.out.println("\t                  If no one is specified, standard input will be used.");
		System.out.println("\t-o|--output file: specify the file name where write the ME. If no one");
		System.out.println("\t                  is specified, standard output will be used.");
		System.out.println();
		System.out.println("Standard input will be the source of the traces using a");
		System.out.println("specific XML format. Standard output will be the target for");
		System.out.println("the new created ME.");
		System.out.println();
		System.out.println("The program return 0 if the ME was correctly generated and");
		System.out.println("a different value in other case. Under this situation, ");
		System.out.println("standard output could have received some output that will be");
		System.out.println("invalid and should be discarded.");
		System.out.println();
		System.out.println("Notes:");
		System.out.println("\t- Traces MUST have been generated by the game which");
		System.out.println("\t  is modeled by the game domain (-d option)");
		*/
	} // printUsage

	//--------------------------------------------------------

	/**
	 * Builds a symbol table for the expression parser adding
	 * the methods of two methods set. The first one will be
	 * the sensors set, and the second one will be the goals set.
	 * 
	 * The symbol table is also populated with the "global
	 * variables" and "generic parameters" that all actions
	 * have. 
	 * 
	 * @param sensorSet Sensor set of the domain which declarations
	 * will be added to the symbol table.
	 * @param goalSet Goal set of the domain which declarations
	 * will be added to the symbol table.
	 * 
	 * @return Symbol table. null is returned if any problem occurs
	 * (for example, a sensor with the name of a generic parameter).
	 * In that case, System.err receives an explanation (this is
	 * not too clean, I know...)
	 */
	protected static SymbolTable getSymbolTable(ParsedMethodSet sensorSet,
	                                            ParsedMethodSet goalSet) {
		
		SymbolTable st = new SymbolTable();
		// We add the methods in the sensor set...
		for (ParsedMethod m:sensorSet.getMethods()) {
			List<SymbolTableEntry> parameters;
			parameters = new java.util.LinkedList<SymbolTableEntry>();
			for (ParsedActionParameter p:m.getParameters()) {
				SymbolTableEntry e;
				e = new SymbolTableEntry(SymbolTable.EntryType.PARAMETER, p.getName(), p.getType());
				parameters.add(e);
			}
			SymbolTableMethodEntry methodEntry;
			methodEntry = new SymbolTableMethodEntry(m.getName(), m.getReturnedType(), parameters);
			st.addEntry(methodEntry);
		}

		// ... and now in the goal set.
		for (ParsedMethod m:goalSet.getMethods()) {
			List<SymbolTableEntry> parameters;
			parameters = new java.util.LinkedList<SymbolTableEntry>();
			for (ParsedActionParameter p:m.getParameters()) {
				SymbolTableEntry e;
				e = new SymbolTableEntry(SymbolTable.EntryType.PARAMETER, p.getName(), p.getType());
				parameters.add(e);
			}
			SymbolTableMethodEntry methodEntry;
			methodEntry = new SymbolTableMethodEntry(m.getName(), m.getReturnedType(), parameters);
			st.addEntry(methodEntry);
		}

		// Add into the symbol table the "global variables".
		if (st.getEntry("cycle") != null) {
			System.err.println("cycle method name is not allowed.");
			return null;
		}
		if (st.getEntry("player") != null) {
			System.err.println("player method name is not allowed.");
			return null;
		}

		SymbolTableEntry e;
		e = new SymbolTableEntry(SymbolTable.EntryType.GLOBAL_VAR,
		                         "player", ActionParameterType.PLAYER);
		st.addEntry(e);
		e = new SymbolTableEntry(SymbolTable.EntryType.GLOBAL_VAR,
                "cycle", ActionParameterType.INTEGER);
		st.addEntry(e);

		// Add into the symbol table the "generic parameters" that
		// all actions will have.
		if (st.getEntry("entityID") != null) {
			System.err.println("entityID method name is not allowed.");
			return null;
		}
		if (st.getEntry("playerID") != null) {
			System.err.println("actor method name is not allowed.");
			return null;
		}
		e = new SymbolTableEntry(SymbolTable.EntryType.PARAMETER,
		                         "entityID", ActionParameterType.STRING);
		st.addEntry(e);
		e = new SymbolTableEntry(SymbolTable.EntryType.PARAMETER,
                "playerID", ActionParameterType.PLAYER);
		st.addEntry(e);
		// ��st.addEntry(e) for both of them??

		return st;

	} // getSymbolTable
	
	//--------------------------------------------------------

	/**
	 * Main program. You can consult the valid args executing
	 * the program or consulting the printUsage() code.
	 * 
	 * @param args Program parameters.
	 * 
	 * @return 0 if it works, and a different value in other
	 * case. This value should be send back to the operative system.
	 */
	protected static int realMain(String[] args) {

		CmdLineParser parser = new CmdLineParser();
		
		CmdLineParser.Option helpOpt = parser.addBooleanOption('h', "help");
		CmdLineParser.Option outputOpt = parser.addStringOption('o', "output");

		try {
			parser.parse(args);
		} catch (CmdLineParser.OptionException e) {
			System.err.println(e.getMessage());
			printUsage();
			System.exit(1);
		}
        
		boolean help = (Boolean)parser.getOptionValue(helpOpt, false);
		String outputPath = (String)parser.getOptionValue(outputOpt);

		if ((help) || (outputPath == null)) {
			printUsage();
			System.exit(0);
		}

		java.io.File file = new java.io.File(outputPath);
		if (!file.exists() || !file.isDirectory() || !file.canWrite()) {
	    	System.err.println("The specified path does not exists " +
	    	                   "or cannot be written.");
	    }
		
		String[] remainingOpts = parser.getRemainingArgs();
        
		if (remainingOpts.length > 1) {
			printUsage();
			System.exit(1);
        }
		
		String inputFileName = null;
		java.io.InputStream inStream;

		if (remainingOpts.length > 0) {
			inputFileName = remainingOpts[0];
		}
		
		if (inputFileName != null) {
			try {
				inStream = new java.io.FileInputStream(inputFileName);
			} catch (java.io.IOException ex) {
				System.err.println(inputFileName + " does not exists or could not be opened.");
				return 1;
			}
		}
		else
			inStream = System.in;

		DomainGenerator dg = new DomainGenerator();

		if (!dg.parseXML(inStream))
			return 1;

		try {
			dg.saveClassesToFiles(outputPath);
		}
		catch (java.io.IOException e) {
			System.err.println("Files could not be saved:");
			e.printStackTrace();
		}

		System.out.println("Done.");

		return 0;

	} // realMain

	//--------------------------------------------------------

	/**
	 * Auxiliary method to get a text template by its name.
	 * If it is not found, a message is sent to System.err
	 * and null is returned.
	 * 
	 * @param name Template name. ".tmplt" extension will be
	 * added.
	 */
	protected static TextTemplate getTemplate(String name) {

		TextTemplate result;
		result = new TextTemplate();
		try {
			result.initFromFile(name + ".tmplt");
		}
		catch (Exception e) {
			System.err.println(name + ".tmplt template file cannot be found.");
			return null;
		}
		return result;
		
	} // getTemplate
	
	//--------------------------------------------------------
	//                   Protected fields
	//--------------------------------------------------------

	/**
	 * List with the names of each generated class. The names
	 * contain the complete package (for example
	 * <tt>towers.mmpm.entities.Wall</tt> instead of just
	 * <tt>Wall</tt>).
	 * 
	 * The code for this class will be in the same position
	 * of the _generatedCode list.
	 */
	protected List<String> _generatedClassesNames;

	/**
	 * List with the java code of the generated class. The
	 * name of each class will be in the same position of
	 * the _generatedClassesNames list.
	 */
	protected List<String> _generatedCode;

} // class DomainGenerator