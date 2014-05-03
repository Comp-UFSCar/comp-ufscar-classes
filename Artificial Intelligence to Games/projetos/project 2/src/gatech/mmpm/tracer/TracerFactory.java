/* Copyright 2010 Santiago Ontanon and Ashwin Ram */

package gatech.mmpm.tracer;

import gatech.mmpm.ConfigurationException;
/**
 * Helper class that eases the task of creating a Tracer object to
 * the applications, based on their -t/--trace command line argument.
 * 
 * @author Marco Antonio Gomez Martin
 */
public class TracerFactory {
	
	public static ITracer BuildTracer(String arg) 
		throws ConfigurationException {
		
		if (arg.startsWith("file")) {
			// If no filename is specified, we choose one based on the
			// current time
			String fileName;
			if (arg.equals("file"))
				fileName = "trace-" + System.currentTimeMillis() + ".xml";
			else
				fileName = arg.substring("file:".length());
			
			OutputStreamTracer ret = new OutputStreamTracer(fileName);
			if (!ret.success())
				throw new ConfigurationException(ret.getErrorMessage());
			
			return ret;
		}else if (arg.startsWith("zipfile")) {
			// If no filename is specified, we choose one based on the
			// current time
			String fileName;
			if (arg.equals("zipfile"))
				fileName = "trace-" + System.currentTimeMillis() + ".zip";
			else
				fileName = arg.substring("zipfile:".length());
			
			OutputStreamZipTracer ret = new OutputStreamZipTracer(fileName);
			if (!ret.success())
				throw new ConfigurationException(ret.getErrorMessage());
			
			return ret;
		} else if (arg.startsWith("remote:")) {
			String connectionData = arg.substring("remote:".length());
			
			SocketTracer ret = new SocketTracer(connectionData);
			if (!ret.success())
				throw new ConfigurationException(ret.getErrorMessage());
			
			return ret;
		} else if (arg.startsWith("post:")) {
			String url = arg.substring("post:".length());
			
			try {
				HTTPPostTracer ret = new HTTPPostTracer(url);
				return ret;
			} catch (java.io.IOException ex) {
				ConfigurationException e =
					new ConfigurationException("URL not correct.\n" + ex.getMessage());
				e.initCause(ex);
				throw e;
			}
		} else if (arg.startsWith("postZip:")) {
			String url = arg.substring("postZip:".length());
			
			try {
				HTTPPostZipTracer ret = new HTTPPostZipTracer(url);
				return ret;
			} catch (java.io.IOException ex) {
				ConfigurationException e =
					new ConfigurationException("URL not correct.\n" + ex.getMessage());
				e.initCause(ex);
				throw e;
			}
		}
		
		return null;
	}
	
	/**
	 * Returns a String to be presented to the user explaining
	 * the different possibilities in the -t/--trace command line argument.
	 * <p>
	 * It explains the capabilities of BuildTacer method.
	 * <p>
	 * @return
	 */
	public static String getUserFriendlyHelp() {
		
		System.out.println("\t-t|--trace:  save the game to a trace that could be");
		System.out.println("\t             parsed by D2 to learn. The method specify");
		System.out.println("\t             whether the trace will be save to a file");
		System.out.println("\t             ('file:filename.xml') sent over the");
		System.out.println("\t             network through a server ('remote:IP:Port')");
		System.out.println("\t             or sent using an HTTP Post request");
		System.out.println("\t             ('post:http://...' and postZip://...)");
		return "";
	}
}
