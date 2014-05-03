/* Copyright 2010 Santiago Ontanon and Ashwin Ram */


package gatech.mmpm.learningengine;

import gatech.mmpm.ConfigurationException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Helper class that eases the task of creating a MEExecutor object to
 * the applications, based on the "blind" parameter that the game receives
 * with the concrete MEExecutor to generate, the way in which the ME is stored
 * and where is this ME stored. Optionally, the MEExecutor can be involved
 * within a decorator.
 * 
 * @author David Llanso
 *
 */
public class MEExecutorFactory {
	
	/**
	 * This Method creates and load a IMEExecutor from a "blind" string with
	 * the needed parameters.
	 * @param info Needed parameters. format:
	 * 		MEExecutorClassName@@@MEStoredway@@@MEDirection[@@@DecoratorClassName]
	 * 		MEStoredway could be: file|zipfile...
	 * @param idomain Domain of the game to play.
	 * @return The generated MEExecutor with a loaded ME.
	 * @throws ConfigurationException If something went wrong.
	 */
	public static IMEExecutor BuildMEExecutor(String info, gatech.mmpm.IDomain idomain) 
		throws ConfigurationException {
		
		IMEExecutor meExecutor = null;
		
		String[] splitInfo = info.split("@@@");
		
		if( (splitInfo.length < 3) || (splitInfo.length > 4) )
			throw new ConfigurationException("Unexpected Format. It should be: MEExecutorClassName@@@MEStoredway@@@MEDirection[@@@DecoratorClassName]");

		// Building the MEExecutor.
		meExecutor = BuildMEExecutorInternal(splitInfo[0],"gatech.mmpm.learningengine.IMEExecutor");
		
		//In case is selected, it created the MEExecutor decorator.
		if (splitInfo.length > 3)
		{
			LazyMEExecutor lazyMEExecutor = (LazyMEExecutor) BuildMEExecutorInternal(splitInfo[3],"gatech.mmpm.learningengine.LazyMEExecutor");
			lazyMEExecutor.setMEOrig(meExecutor);
			meExecutor = lazyMEExecutor;
		}
		
		//Now the ME must be loaded.
		java.io.InputStream me = null;
		
		if(splitInfo[1].equals("file"))
			try {
				me = new java.io.FileInputStream(splitInfo[2]);
			} catch (Exception e) {
                try {
                    URL url = new URL(splitInfo[2]);
                    me = url.openStream();
                } catch (Exception ex) {
    				throw new ConfigurationException(splitInfo[2] + ": File not found.");
                }
			}
		else if(splitInfo[1].equals("zipfile"))
			try {
				me = getMEFromZipFile(splitInfo[2]);
			} catch (Exception e) {
				throw new ConfigurationException(splitInfo[2] + ": File not found.");
			}
		//TODO else if --> more ways.
		else
			throw new ConfigurationException(splitInfo[1] + " is not a valid way of ME retrieval.");
		
		meExecutor.loadME(me,idomain);
			
		
		return meExecutor;
	}
	
	private static java.io.InputStream getMEFromZipFile(String fileName) throws Exception {
		java.io.InputStream me = null;
        try {
            me = new java.io.FileInputStream(fileName);
        } catch (Exception ex) {
            URL url = new URL(fileName);
            me = url.openStream();
        }
		java.util.zip.ZipInputStream zf;
		zf = new java.util.zip.ZipInputStream(me);
		zf.getNextEntry();
		me = zf;
		return me;
	}
	
	/**
	 * This Method creates a IMEExecutor from its class name and the name of 
	 * its base class name. It checks the correct inheritance between them and
	 * checks both of them implements the IMEExecutor interface.
	 * the needed parameters.
	 * @param askedClassName Class name of the requested instance.
	 * @param baseClassName Class name of the base class of the requested instance.
	 * @return The generated MEExecutor.
	 * @throws ConfigurationException If something went wrong.
	 */
	private static IMEExecutor BuildMEExecutorInternal(String askedClassName, String baseClassName) 
		throws ConfigurationException {
		
		IMEExecutor meExecutor = null;
		
		Class<? extends IMEExecutor> concreteMEExecutorClass;
		Class<? extends IMEExecutor> baseClass;
		Class<IMEExecutor> IMEExecutorInterface = IMEExecutor.class;

		Class<?> askedClass;		// Auxiliary class
		
		try {
			askedClass = Class.forName(baseClassName);
		} catch (java.lang.ClassNotFoundException e) {
			throw new ConfigurationException(askedClassName + " not found.");
		}

		//Ensure that base class inherits from IMEExecutor interface.
		try {
			baseClass = askedClass.asSubclass(IMEExecutorInterface);
		} catch (java.lang.ClassCastException e) {
			throw new ConfigurationException(askedClass.getName() + " does not implements " + IMEExecutorInterface.getName());
		}
		
		try {
			askedClass = Class.forName(askedClassName);
		} catch (java.lang.ClassNotFoundException e) {
			throw new ConfigurationException(askedClassName + " not found.");
		}

		//Ensure that concreteMEExecutor class inherits from the base class.
		try {
			concreteMEExecutorClass = askedClass.asSubclass(baseClass);
		} catch (java.lang.ClassCastException e) {
			throw new ConfigurationException(askedClass.getName() + " does not implements " + baseClassName);
		}
		
		//Creating the instance.
		try {
			meExecutor = concreteMEExecutorClass.newInstance();
		} catch (Exception e) {
			throw new ConfigurationException(concreteMEExecutorClass.getName() + " cannot be instanciated.");
		} 

		return meExecutor;
	}
	
	/**
	 * Writes in the standard output the way in which the MEExecutor has to 
	 * be called.
	 * <p>
	 * It explains the capabilities of BuildMEExecutor method.
	 * <p>
	 */
	public static String getUserFriendlyHelp() {
		
		System.out.println("\t                The String needed for creating a MEExecutor that loads a Me must be:");
		System.out.println("\t                MEExecutorClassName@@@MEStoredway@@@MEDirection[@@@DecoratorClassName]");
		System.out.println("\t                Where: MEExecutorClassName is the class of the concrete MEExecutor.");
		System.out.println("\t                       MEStoredway must be: file|zipfile|[other ways in the future].");
		System.out.println("\t                       MEDirection, the place in which the ME is located (file, etc.).");
		System.out.println("\t                       [optional] DecoratorClassName is the class of the decorator");
		return "";
	}
	
}
