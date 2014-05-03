/* Copyright 2010 Santiago Ontanon and Ashwin Ram */

package gatech.mmpm.tools;

import java.io.File;
import java.io.FileOutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

/**
 * Application that allows game developers to create the IDomain class
 * of their domains.
 * <p>
 * It receives the specification via an XML-file and construct the .java
 * file that should be included on it.
 * 
 * @author Marco Antonio and Pedro Pablo Gomez Martin
 */
public class IDomainGenerator {

	java.io.PrintWriter diagnosticsWriter = null;
	java.io.PrintWriter dumpWriter = null;
    TransformerFactory tfactory;
    SAXTransformerFactory stf;
    Templates xslTemplate;
    Templates stylesheet;
    Transformer transformer;
    StreamResult outputStream;
    String relativeFileName;
	
	void createErrorChannels() {
	    diagnosticsWriter = new java.io.PrintWriter(System.err, true);
	    dumpWriter = diagnosticsWriter;
	}
	
	TransformerFactory createTransformerFactory() {
		TransformerFactory tfactory;
		try
		{
			tfactory = TransformerFactory.newInstance();
			//tfactory.setErrorListener(new DefaultErrorHandler(false));
		}
		catch (TransformerFactoryConfigurationError pfe)
		{
			pfe.printStackTrace(dumpWriter);
			tfactory = null;  // shut up compiler
		}
		return tfactory;
	}
	
	Templates createXSLTemplate(String fileName) {

		Templates ret;
		java.io.InputStream input = null;
		try {
			
			try {
				// Try in the path of this class
				java.net.URI u = this.getClass().getResource("/gatech/mmpm/tools/" + fileName).toURI();
//				System.out.println(u);
				input = new java.io.FileInputStream(new java.io.File(u));
			} catch (java.io.IOException ex) {}
			  catch (java.lang.NullPointerException ex) {}
			if (input == null) {
				try {
					// Error; try in the file system
					input = new java.io.FileInputStream(fileName);
				} catch (java.io.IOException ex) {
				}
			}
			
			
			/*
			// DOM
			// Parse in the xml data into a DOM
			DocumentBuilderFactory dfactory = DocumentBuilderFactory.newInstance();
			dfactory.setNamespaceAware(true);
			DocumentBuilder docBuilder = dfactory.newDocumentBuilder();
			Node xslDOM = docBuilder.parse(new InputSource(fileName));
	
			ret = tfactory.newTemplates(new DOMSource(xslDOM, fileName));
			/*/
			// SAX
			ret = tfactory.newTemplates(new StreamSource(input));
			/**/
		} catch (Exception ex) {
			ex.printStackTrace(dumpWriter);
			return null;
		}
		
		return ret;
	}
	
	Transformer createTransformer() {
		try {
			return stylesheet.newTransformer();
		} catch (TransformerConfigurationException ex) {
			ex.printStackTrace(dumpWriter);
			return null;
		}
	}
	
	StreamResult createStreamResult(String outDir) {
		try {
			if (outDir == null)
				return new StreamResult(System.out);
			else {
				// Ensure the directory exists
				String file = outDir + 
							  (outDir.endsWith(java.io.File.separator) ? "" : java.io.File.separator) +
							  relativeFileName;

				java.io.File f = new java.io.File(file).getParentFile();
				f.mkdirs();
				
				StreamResult ret;
				ret = new StreamResult(new FileOutputStream(file));
				ret.setSystemId(file);

				return ret;
			}
		} catch (java.io.IOException ex) {
			ex.printStackTrace(dumpWriter);
			return null;
		}
	}
	
	StreamResult createStreamResult(java.io.Writer writer) {
		if (writer == null)
			return new StreamResult(System.out);
		else {
			
			StreamResult ret;
			ret = new StreamResult(writer);
				return ret;
		}
	}
	
	String getRelativeFileName(String xmlFile) {
		try {
			// Build the document with SAX and Xerces, no validation
			SAXBuilder builder = new SAXBuilder();
			// Create the document
			org.jdom.Document doc = builder.build(new File(xmlFile));
			Element root = doc.getRootElement();
			if (!root.getName().equals("Domain")) {
				// XML without <Domain> in the root; try to recover...
				root = root.getChild("Domain");
			}
			// Get the actions and entities packages.
			String domainPkg = root.getAttributeValue("package");
			String className = root.getAttributeValue("classname");
			
			if (domainPkg == null) domainPkg = "";
			if (className == null) className = "";
			
			return domainPkg.replace('.', java.io.File.separatorChar) +
					java.io.File.separatorChar + className + ".java";
		} catch (Exception ex) {
			ex.printStackTrace(dumpWriter);
			return null;
		}
	}
	
	void transformWithSAX(String xmlFile) {
		try {
			transformer.transform(new StreamSource(xmlFile), outputStream);
		} catch (Exception ex) {
			ex.printStackTrace(dumpWriter);
		}
    }

	void transformWithSAX(java.io.Reader reader) {
		try {
			transformer.transform(new StreamSource(reader), outputStream);
		} catch (Exception ex) {
			ex.printStackTrace(dumpWriter);
		}
    }
	
	void transformWithDOM(String xmlFile) {
		try {
			// Parse in the xml data into a DOM
			DocumentBuilderFactory dfactory;
			dfactory = DocumentBuilderFactory.newInstance();
	
			dfactory.setCoalescing(true);
			dfactory.setNamespaceAware(true);
	
			DocumentBuilder docBuilder = dfactory.newDocumentBuilder();
	
			Node xmlDoc = docBuilder.parse(new InputSource(xmlFile));
			Document doc = docBuilder.newDocument();
			org.w3c.dom.DocumentFragment outNode =
				doc.createDocumentFragment();
	
			transformer.transform(new DOMSource(xmlDoc, xmlFile),
					new DOMResult(outNode));
	
			// Now serialize output to disk with identity transformer
			Transformer serializer = stf.newTransformer();
	
			java.util.Properties serializationProps =
				stylesheet.getOutputProperties();
	
			serializer.setOutputProperties(serializationProps);
	
			serializer.transform(new DOMSource(outNode), outputStream);
		} catch (Exception ex) {
			ex.printStackTrace(dumpWriter);
		}
	}
	
	void closeOutput(String outFileName) {
		if (null != outFileName && outputStream!=null) {
			java.io.OutputStream out = outputStream.getOutputStream();
			java.io.Writer writer = outputStream.getWriter();
			try {
				if (out != null) out.close();
				if (writer != null) writer.close();
			} catch(java.io.IOException ie) {}
		}        
	}

	boolean preTransform() {
		
		createErrorChannels();
		tfactory = createTransformerFactory();
		if (tfactory == null)
			return false;
		stf = (SAXTransformerFactory)tfactory;
		stylesheet = createXSLTemplate("domain.xsl");
		if (stylesheet == null) 
			return false;
		transformer = createTransformer();
		if (transformer == null) 
			return false;

		return true;
		
	}
	
	void run(String xmlFile, String outDir) {
		
		if (!preTransform())
			return;

		outputStream = createStreamResult(outDir);
		if (outputStream == null)
			return;
		relativeFileName = getRelativeFileName(xmlFile);
		// We can do it using SAX to read the xml file...
		transformWithSAX(xmlFile);
		// or using DOM
		//transformWithDOM(xmlFile);
		
		closeOutput(outDir);
	}

	void run(java.io.Reader reader, java.io.Writer writer) {

		if (!preTransform())
			return;
		outputStream = createStreamResult(writer);
		transformWithSAX(reader);

	}

	
	public static void printUsage() {
		System.out.println("IDomainGenerator: creates Java code with the class that inherits from");
		System.out.println("    gatech.mmpm.IDomain that contains the information of the domain according");
		System.out.println("    to an XML file.");
		System.out.println();
		System.out.println("Usage: IDomainGenerator xmlFile [srcRootDir]");
		System.out.println();
		System.out.println("\txmlFile:    file with the domain specification.");
		System.out.println("\tsrcRootDir: root directory of the source code. The file will be");
		System.out.println("\t            generated according to the package and classname.");
		System.out.println("\t            If '-' or no specified, standard output will");
		System.out.println("\t            be used.");
		System.out.println();
	}
	
	public static void main(String[] args) {

		String xmlFile;
		String outDir = null;
		
		if ((args.length == 0) || (args.length > 2)) {
			printUsage();
			System.exit(1);
		}
		
		xmlFile = args[0];
		
		if ((args.length == 2) && (!args[1].equals("-")))
			outDir = args[1];

		/// Run de process!
		
		IDomainGenerator dg = new IDomainGenerator();
		
		dg.run(xmlFile, outDir);
	}
}
